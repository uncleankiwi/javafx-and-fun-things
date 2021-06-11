package pi_estimator;

//pi: 3.141592653589793
//22/7: 3.142857142857143

import java.util.List;

/*
Generates a list of rational numbers that estimate pi
The estimates use anywhere from 1 to a given number of total digits.
The list of results should contain pairs of estimate that straddle pi ('bounding pairs');
all others are ignored as they're never going to be any closer to pi than these anyway.

The best estimate in every 'band' of estimates is also placed in the bestEstimates list.
e.g.
	for 1 digit, it should return 3 and 4.	<-- best estimate in the 1-digit band: 3

	for 2 digits, it should return these pairs:
		3/1 = 3, 4/1 = 4
		6/2 = 3, 7/2 = 3.5
		9/3 = 3, [ignored]

	for 3 digits, it should add:
		[ignored] and 10/3 = 3.33
		12/4 = 3 and 13/4 = 3.25
		15/5 = 3 and 16/5 = 3.2
		18/6 = 3 and 19/6 = 3.16...
		21/7 = 3 and 22/7 = 3.142...		<-- best estimate in the 3-digit band: 22/7
		25/8 = 3.125 and 26/8 = 3.25
		28/9 = 3.11... and 39/9 = 3.22...
 */
public class EstimateGenerator {
	private static List<Estimate> estimates;
	private static List<Estimate> bestEstimates;

	public static void main(String[] args) {
		System.out.println(Math.PI);
		System.out.println((double) 22/7);


		Estimate estimate = new Estimate(22, 7, 3);
		System.out.println(estimate.closeness());
	}

	//fills the lists with estimates that use up to maxDigits
	private static void populate(int maxDigits) {
		for (int digits = 1; digits < maxDigits; digits++) {
			int bestCloseness = 0;	//highest closeness in this band
			Estimate bestEstimate;	//estimate with highest closeness in band

			if (digits == 1) {

			}
			else {

			}
		}
	}

	public static List<Estimate> get() {
		return estimates;
	}

	public static List<Estimate> getBest() {
		return bestEstimates;
	}
}
