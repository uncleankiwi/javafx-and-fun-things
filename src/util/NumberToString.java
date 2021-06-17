package util;

/**
 * Converts numbers to String because the toString for Double produces
 * results in scientific notation, and the conversion to BigDecimal
 * introduces a lot of floating point errors.
 */
public class NumberToString {

	/**
	 * Converts a double into string, then converts that string into
	 * non-scientific notation.
	 *
	 * @param d Value to convert to String.
	 * @return String output.
	 */
	public static String doubleToString(double d) {
		String s = String.valueOf(d);
		if (s.contains("E")) {
			String[] sArr = s.split("E");
			int exponent = Integer.parseInt(sArr[1]);
			if (exponent > 0) {
				//move decimal point to the right

			}
			else {
				//move decimal point to the left

			}
		}
		return s;
	}
}
