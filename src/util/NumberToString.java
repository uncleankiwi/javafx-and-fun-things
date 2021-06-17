package util;

/**
 * Converts numbers to String because the toString for Double produces
 * results in scientific notation, and the conversion to BigDecimal
 * introduces a lot of floating point errors.
 */
public class NumberToString {

	/**
	 * Converts a double into string, then converts that string into
	 * non-scientific notation. Doesn't use IEEE 754.
	 *
	 * @param d Value to convert to String.
	 * @return String output.
	 */
	public static String doubleToString(double d) {
		String s = String.valueOf(d);
		if (s.contains("E")) {
			String[] oldDouble = s.split("E");
			String[] oldFraction = oldDouble[0].split("\\.");
			String oldFractionPointless = oldFraction[0] + oldFraction[1];
			int exponent = Integer.parseInt(oldDouble[1]);
			int newRightLength;
			int newLeftLength;
			//suppose we convert 1.234E3 or 1.234E-3
			if (exponent > 0) {
				//1234 - right digits may be empty
				newRightLength = Math.max(0, oldFraction[1].length() - exponent);
				newLeftLength = exponent + oldFraction[0].length();
				//padding with extra 0s to the right if necessary
				oldFractionPointless = oldFractionPointless + Padder.bar('0', Math.max(0, newLeftLength - oldFractionPointless.length()));
			}
			else {
				//0.001234 - the left digits always contain a 0 at the very least, but that will be added later
				newRightLength = Math.abs(exponent) + oldFraction[1].length();
				newLeftLength = Math.max(0, oldFraction[0].length() - Math.abs(exponent));
				//padding with 0s to the left if necessary
				oldFractionPointless = Padder.bar('0', Math.max(0, newRightLength - oldFractionPointless.length())) + oldFractionPointless;
			}

			if (newLeftLength == 0) return "0." + oldFractionPointless;		//number is in the form of 0.nnnnn
			else if (newRightLength == 0) return oldFractionPointless;		//number is an integer: nnnn
			else {															//number is in the form of nnn.nnnn
				return oldFractionPointless.substring(0, newLeftLength) + "." +
					oldFractionPointless.substring(newLeftLength);
			}
		}
		return s;
	}
}
