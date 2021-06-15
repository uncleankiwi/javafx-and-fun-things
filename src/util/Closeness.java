package util;

import java.math.BigDecimal;
import java.util.Arrays;

/*
Closeness = k * (10 * logarithmic closeness + arithmetic closeness),
where k = 1 or -1.

Logarithmic difference
= length of agreeing digits + length(

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
	- |100 - 3| = 97
	- 0 digits agree.
	- Length(disagreeing digits) - length(|difference|) = 1
	- Log difference = 1 + 0 = 1
	- Arithmetic difference = 10 - 9(first digit) = 1.

100, 197 -> 11
	- 1 digit agrees.
	- |97 - 0| = 97 -> arithmetic difference of 1st digit = 9.
	- Arithmetic closeness = 10 - 9 = 1.

100, 97 -> -27
	- Value is smaller, so result is negative.
	- |97 - 100| = 3
	- Logarithmic difference length(100) - length(2)
	- No digits agree

540333, 535222 -> -25

 */
/**
 * Holds a get() method that determines the closeness of two numbers.
 */
public final class Closeness {
	public static void main(String[] args) {
		get(3, 5);
		System.out.println("...");
		get(35678, 0.00004);
		System.out.println("...");
		get(Math.PI, 8);
		System.out.println("...");
		get(700000000, 0);
	}

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
	 * @param v value to compare to reference.
	 * @return Closeness of the two numbers.
	 */
	public static int get(double r, double v) {
		//Return 0 if r < 0 < v or v < 0 < r.
		if ((r < 0 && 0 < v) || (v < 0 && r < v)) return 0;

		//Polarity of result: positive if v > r, negative if v < r
		int polarity = (v > r) ? 1 : -1;

		//Padding right with zero so that both numbers have the same number of digits
		//after the decimal point, then removing the decimal point.


		//


		//Converting both to a string and then padding with 0s such that they have the
		//same length and their decimal points are in the same location.
		//BigDecimal class has to be used to prevent the string from getting converted
		//into scientific notation.

		String referenceStr = (new BigDecimal(r)).toPlainString();
		String valueStr = (new BigDecimal(v)).toPlainString();

		if (!referenceStr.contains(".")) {
			referenceStr += ".0";
		}
		if (!valueStr.contains(".")) {
			valueStr += ".0";
		}

		String[] referenceArray = referenceStr.split("\\.");
		String[] valueArray = valueStr.split("\\.");
		System.out.println(Arrays.toString(referenceArray) + "::::" + Arrays.toString(valueArray));

		int placesLeft = Math.max(referenceArray[0].length(), valueArray[0].length());
		int placesRight = Math.max(referenceArray[1].length(), valueArray[1].length());

		referenceStr = Padder.bar('0', placesLeft - referenceArray[0].length()) +
			referenceArray[0] +
			"." +
			referenceArray[1] +
			Padder.bar('0', placesRight - referenceArray[1].length());
		valueStr = Padder.bar('0', placesLeft - valueArray[0].length()) +
			valueArray[0] +
			"." +
			valueArray[1] +
			Padder.bar('0', placesRight - valueArray[1].length());
		System.out.println(referenceStr);
		System.out.println(valueStr);

		return 0;
	}
}
