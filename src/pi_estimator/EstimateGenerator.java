package pi_estimator;

import java.util.ArrayList;
import java.util.List;

/*
Generates a list of rational numbers that estimate pi.
The estimates use anywhere from 1 to a given number of total digits.
The list of results should contain pairs of estimate that straddle pi ('bounding pairs');
all others are ignored as they're never going to be any closer to pi than these anyway.

The best estimate (bestimate?) in every 'band' of estimates is also placed in the bestEstimates list.
e.g.
	for 1 digit, it should return 3 and 4.	<-- best estimate in the 1-digit band: 3

	for 2 digits, it should return these pairs:
		3/1 = 3, 4/1 = 4					<-- best estimate should return the first one found, i.e. 3/1.
		6/2 = 3, 7/2 = 3.5
		(9/3 = 3 is ignored)

	for 3 digits, it should add:
		(10/3 = 3.33 is ignored)
		12/4 = 3 and 13/4 = 3.25
		15/5 = 3 and 16/5 = 3.2
		18/6 = 3 and 19/6 = 3.16...
		21/7 = 3 and 22/7 = 3.142...		<-- best estimate in the 3-digit band: 22/7
		25/8 = 3.125 and 26/8 = 3.25
		28/9 = 3.11... and 39/9 = 3.22...

NB:
	pi: 3.141592653589793
	22/7: 3.142857142857143

Lemma:
	If 10/k and 99/k are both larger or both smaller than pi,
	then 10/k and 99/k are both lousy estimates of pi.

	If both are bigger, since 10/k is bigger than pi and is an integer,
	-> k <= 3
	The smallest estimate when both are bigger is therefore 10/3.

	If both are smaller, then 99/k is smaller than pi.
	-> k >= 31
	For these values of k, the value of 99/k is maximum when k is 99,
	thus the estimate is 1, which is far smaller than pi.

So for a denominator k, if there is no n/k and (n+1)/k such that
	n/k < pi < (n+1)/k,
every n/k and (n+1)/k is a poor estimate of pi and can be excluded from
the results.
i.e. Only estimates that are bounding pairs of pi will be looked at.

 */
public class EstimateGenerator {
	private static List<Estimate> estimates;
	private static List<Estimate> bestEstimates;

	public static void main(String[] args) {
		populate(9);
//		System.out.println("\nEstimates:");
//		get().forEach(System.out::println);
		System.out.println("\nBest estimates:");
		getBest().forEach(System.out::println);
	}

	//fills the lists with estimates that use up to the given number of maxDigits
	public static void populate(int maxDigits) {
		estimates = new ArrayList<>();
		bestEstimates = new ArrayList<>();

		for (int digits = 1; digits <= maxDigits; digits++) {
			int bestAbsoluteCloseness = 0;	//highest absolute closeness in this band
			Estimate bestEstimate = null;	//estimate with highest closeness in band

			//finding all estimates in a band
			int[] denominatorRange = denominatorRange(digits);
			int[] numeratorRange = numeratorRange(digits);
			for (int denominator = denominatorRange[0]; denominator <= denominatorRange[1]; denominator++) {
				//finding a pair of values only if they're a bounding pair
				if ((double) numeratorRange[0] / denominator < Math.PI && (double) numeratorRange[1] / denominator > Math.PI) {
					int lowerNumerator = (int) (Math.PI * denominator);
					Estimate lowerEstimate = new Estimate(lowerNumerator, denominator, digits);
					Estimate higherEstimate = new Estimate(lowerNumerator + 1, denominator, digits);
					estimates.add(lowerEstimate);
					estimates.add(higherEstimate);
					if (lowerEstimate.absoluteCloseness() > bestAbsoluteCloseness) {
						bestAbsoluteCloseness = lowerEstimate.absoluteCloseness();
						bestEstimate = lowerEstimate;
					}
					else if (higherEstimate.absoluteCloseness() > bestAbsoluteCloseness) {
						bestAbsoluteCloseness = higherEstimate.absoluteCloseness();
						bestEstimate = higherEstimate;
					}
				}
			}

			//adding the best estimate found in this band
			bestEstimates.add(bestEstimate);
		}
	}

	public static List<Estimate> get() {
		return estimates;
	}

	public static List<Estimate> getBest() {
		return bestEstimates;
	}

	//The following two methods returns a range of acceptable numerators and denominators.
	//E.g. they'll return 10-99 when given 4 since it's split 2-2 between numerator and denominator.
	private static int[] numeratorRange(int digits) {

		int numeratorDigits = (int) Math.ceil((double) digits / 2);
		return new int[] {lowestNDigitNumber(numeratorDigits), lowestNDigitNumber(numeratorDigits + 1) - 1};
	}

	private static int[] denominatorRange(int digits) {
		if (digits == 1) return new int[] {1, 1};

		int denominatorDigits = digits / 2;
		return new int[] {lowestNDigitNumber(denominatorDigits), lowestNDigitNumber(denominatorDigits + 1) - 1};
	}

	private static int lowestNDigitNumber(int digits) {
		return (int) Math.pow(10, digits - 1);
	}
}
