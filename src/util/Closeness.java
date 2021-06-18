package util;

/*
Closeness = k * (10 * logarithmic closeness + arithmetic closeness),
where k = 1 or -1.
Exception: when the two numbers are 2 orders of magnitude or more apart.
e.g. 3 and 100.

Logarithmic closeness
= length of agreeing digits + length(max(r,v)) - length(disagreeing digits difference)

Arithmetic closeness
= |10 - first digit of difference between disagreeing digits|

Examples: get(reference value, given value) -> expected result
54100, 54103 -> 47
	- Value is greater than reference, so result is positive.
	- Values agree for the first 4 digits, so the left digit is 4.
	- The absolute value of difference in the first digit that disagrees:
		|3 - 0| = 3.
	- Length(disagreeing digits) - length(absolute difference) = 0,
		so
	- Finally, arithmetic closeness = 10 - difference = 10 - 3 = 7, so right digit is 7.

54100, 54097 -> -47
	- Value is smaller, so result is negative.
	- Two digits agree, so left digit is 2 for now.
	- |97 - 100| = 3
	- Length(disagreeing digits) - length(absolute difference) = 2, which
		is added to the logarithmic difference: 2 + 2 = 4
	- Again, arithmetic closeness = 10 - 3 = 7.

30, 100 -> 13
	- Value is larger, so result is positive?
	- No digits agree. Left digit is 0 for now.
	- |100 - 30| = 70
	- Resulting logarithmic difference = 1 + 0 = 1
	- Arithmetic difference = 10 - 7(first digit) = 3.

3, 100 -> 11
	- 0. 2 orders of magnitude in difference.

100, 197 -> 11
	- 1 digit agrees.
	- |97 - 0| = 97 -> arithmetic difference of 1st digit = 9.
	- Arithmetic closeness = 10 - 9 = 1.

100, 97 -> -27
	- Value is smaller, so result is negative.
	- |97 - 100| = 3
	- Agreeing digits = 0.
	- Logarithmic difference = agreeing digits + length(100) - length(2)
		= 0 + 3 - 1 = 2
	- Arithmetic difference = 10 - 3 = 7

540333, 535222 -> -25
	- Polarity = -1.
	- Agreeing digits = 1.
	- |35222 - 40333| = 5111
	- Logarithmic difference = 1 + length(40333) - length(5111)
		= 1 + 5 - 4 = 2
	- Arithmetic difference = 10 - 5 = 5

 */
/**
 * Holds a get() method that determines the closeness of two numbers.
 */
public final class Closeness {

	/**
	 * Returns a measure of the 'closeness' of a value relative to a reference
	 * number.
	 *
	 * Closeness = k * (10 * logarithmic closeness + arithmetic closeness),
	 * where k = 1 or -1.
	 *
	 * <p>The right-most digit is the 'arithmetic closeness' between the first
	 * digits in the reference and given values that do not match up, equal to
	 * (10 - their absolute difference).</p>
	 *
	 * <p>The remaining digits on the left is the number of decimal places up
	 * to which the two numbers agree, and can be considered the 'logarithmic
	 * closeness' of the two numbers.</p>
	 *
	 * <p>The resulting number is also positive if the value is greater than</p>
	 * the reference, or negative if smaller.
	 *
	 * <p>e.g. 54100 (reference) and 54097 (value) give -47, while 54100 and
	 * 54103 give 47.</p>
	 *
	 * <p>The result is similar to to -log(|reference - value|), but uses a
	 * string implementation to arrive at the result.</p>
	 *
	 * <p>This assumes that both parameters have the same precision.</p>
	 *
	 * @param r Reference value to be compared to.
	 * @param v Value to compare to reference.
	 * @return Closeness of the two numbers.
	 */
	public static int get(double r, double v) {
		//Return 0 if r < 0 < v or v < 0 < r.
		if ((r < 0 && 0 < v) || (v < 0 && r < v)) return 0;

		//Polarity of result: positive if v > r, negative if v < r
		int polarity = (v > r) ? 1 : -1;

		//Padding right with zero so that both numbers have the same number of digits
		//after the decimal point, then removing the decimal point if any.
		String referenceStr = NumberToString.doubleToString(Math.abs(r));
		String valueStr = NumberToString.doubleToString(Math.abs(v));

		String[] referenceArray = referenceStr.split("\\.");
		String[] valueArray = valueStr.split("\\.");

		int rDecimalPlaces = 0;
		String rDecimalStr = "";
		if (referenceArray.length > 1) {
			rDecimalStr = referenceArray[1];
			rDecimalPlaces = referenceArray[1].length();
		}
		String vDecimalStr = "";
		int vDecimalPlaces = 0;
		if (valueArray.length > 1) {
			vDecimalStr = valueArray[1];
			vDecimalPlaces = valueArray[1].length();
		}
		int placesRight = Math.max(rDecimalPlaces, vDecimalPlaces);

		referenceStr = referenceArray[0] + rDecimalStr +
			Padder.bar('0', placesRight - rDecimalPlaces);
		valueStr = valueArray[0] + vDecimalStr +
			Padder.bar('0', placesRight - vDecimalPlaces);

		//Remove 0s on the left, but leave a zero if result is empty
		referenceStr = Padder.unpadLeft(referenceStr, '0');
		valueStr = Padder.unpadLeft(valueStr, '0');
		if (referenceStr.equals("")) referenceStr = "0";
		if (valueStr.equals("")) valueStr = "0";

		//Return 0 if the numbers are 2 or more orders of magnitude apart.
		if (Math.abs(referenceStr.length() - valueStr.length()) >= 2) return 0;

		//Determine all components needed to calculate logarithmic closeness:
		//Logarithmic closeness
		//= agreeingDigitLength + largerLength - length(disagreeing digits difference)
		//where largerLength = length(max(r,v))
		int agreeingDigitLength = 0;
		if (referenceStr.length() == valueStr.length()) {
			for (int i = 0; i < referenceStr.length(); i++) {
				if (referenceStr.charAt(i) == valueStr.charAt(i)) agreeingDigitLength++;
				else break;
			}
		}

		int largerLength = Math.max(referenceStr.length(), valueStr.length());

		referenceStr = referenceStr.substring(agreeingDigitLength);
		valueStr = valueStr.substring(agreeingDigitLength);
		String disagreeingDigitDifference = String.valueOf(Math.abs(
			Long.parseLong(referenceStr) - Long.parseLong(valueStr)));
		int disagreeingDigitLength = disagreeingDigitDifference.length();

		int logarithmicCloseness = agreeingDigitLength + largerLength - disagreeingDigitLength;

		//arithmetic closeness
		//= |10 - first digit of difference between disagreeing digits|
		int arithmeticCloseness = 0;
		if (disagreeingDigitDifference.length() > 0) {
			int firstDisagreeingDigit = Integer.parseInt(String.valueOf(disagreeingDigitDifference.charAt(0)));
			arithmeticCloseness = 10 - firstDisagreeingDigit;
		}


		return polarity * (logarithmicCloseness * 10 + arithmeticCloseness);
	}
}
