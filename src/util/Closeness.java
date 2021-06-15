package util;


/*

 */

import java.math.BigDecimal;
import java.util.Arrays;

/*
Examples: (reference value, given value)
54100, 54097: -47
54

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
	 * @param reference value to be compared to.
	 * @param value value to compare to reference.
	 * @return Closeness of the two numbers.
	 */
	public static int get(double reference, double value) {
		//Converting both to a string and then padding with 0s such that they have the
		//same length and their decimal points are in the same location.
		//BigDecimal class has to be used to prevent the string from getting converted
		//into scientific notation.

		String referenceStr = (new BigDecimal(reference)).toPlainString();
		String valueStr = (new BigDecimal(value)).toPlainString();

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
