package util;


/*

 */

import java.math.BigDecimal;
import java.util.Arrays;

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
	 * Similar to -log(|reference - value|), but using a string implementation, and
	 * large differences between references and value returns 0. The result is also
	 * negative value < reference, and positive otherweise.
	 *
	 * <p>The right-most digit is <br />
	 * (100 - |first two incorrect digits - two correct digits|) / 10<br />
	 * which is how close the first incorrect digit is to be correct. This accounts
	 * for carry-over amounts.</p>
	 *
	 * <p>The remaining digits on the left of the output is the number of digits
	 * in the given value that match the exact same decimal position in the
	 * reference value.</p>
	 *
	 * <p>The result is then multiplied by -1 if the value is smaller than the
	 * reference.</p>
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
