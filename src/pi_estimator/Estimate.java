package pi_estimator;

/*
An estimate of pi using a total number of digits d.
This estimate may take the form of an integer n, or a rational fraction in the form of n/m,
where the total number of digits in n and m is d.

Digits - the total number of digits used in the rational number for estimating pi.
	e.g. 22/7: 3 digits. 3: 1 digit.

Closeness - measures how close this estimate is to pi.
	Right-most digit is how much the first incorrect digit is off from the corresponding digit in pi.
	The other digits on the left are how many digits are correct.
	The value is negative if the estimate is lower than pi, positive if otherwise.
	e.g.
		Closeness of 3 is -11, since it has 1 digit correct (3)
		and the first incorrect digit (0) is 1 lower than the actual digit (1).

		Closeness of 22/7 should be 31.
		The fraction works out to 3.142... Since there are 3 correct digits (3.14)
		and the first incorrect digit (2) is 1 higher than the actual digit (1).

Absolute closeness - closeness but always positive.
 */

import util.Fraction;

public class Estimate {
	private final Fraction fraction;
	private final int closeness;
	private final int digits;

	public Estimate(long numerator, long denominator, int digits) {
		this.fraction = new Fraction(numerator, denominator);
		this.digits = digits;

		//calculating closeness
		String fractionString = String.valueOf(fraction.asDouble());
		String piString = String.valueOf(Math.PI);
		int correctNumbers = 0;
		int wrongDigit = 0;
		int correctPiDigit = 0;

		for (int i = 0; i < fractionString.length(); i++) {
			if (fractionString.charAt(i) != '.') {
				if (fractionString.charAt(i) == piString.charAt(i)) correctNumbers++;
				else {
					wrongDigit = Integer.parseInt(String.valueOf(fractionString.charAt(i)));
					correctPiDigit = Integer.parseInt(String.valueOf(piString.charAt(i)));
					break;
				}
			}
		}

		int wrongDigitDifference = wrongDigit - correctPiDigit;
		int closenessSign = (wrongDigitDifference > 0) ? 1 : -1;
		this.closeness = closenessSign * correctNumbers * 10 + wrongDigitDifference;
	}

	public int digits() {
		return digits;
	}

	public int closeness() {
		return closeness;
	}

	public int absoluteCloseness() {
		if (closeness < 0) return -1 * closeness;
		else return closeness;
	}

	@Override
	public String toString() {
		return fraction.toString();
	}
}
