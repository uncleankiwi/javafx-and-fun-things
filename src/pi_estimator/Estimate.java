package pi_estimator;

/*
An estimate of pi using a total number of digits d.
This estimate may take the form of an integer n, or a rational fraction in the form of n/m,
where the total number of digits in n and m is d.

Digits - the total number of digits used in the rational number for estimating pi.
	e.g. 22/7: 3 digits. 3: 1 digit.

Closeness - measures how close this estimate is to pi. The higher the value, the close the match.
	See util.Closeness for documentation on this value.

Absolute closeness - closeness but always positive.
 */

import util.Closeness;
import util.Fraction;

public class Estimate {
	private final Fraction fraction;
	private final int closeness;
	private final int digits;

	public Estimate(long numerator, long denominator, int digits) {
		this.fraction = new Fraction(numerator, denominator);
		this.digits = digits;

		//calculating closeness
		this.closeness = Closeness.get(Math.PI, fraction.asDouble());
	}

	@SuppressWarnings("unused")
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
		return fraction.toString() + ": closeness " + this.closeness();
	}
}
