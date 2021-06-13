package util;

/**
 * Contains methods for formatting output in the log.
 **/
public final class Padder {
	/**Pads a string with tabs on the right such that the output is of
	 * the given length (in tabs).
	 *
	 * @param s String to be padded.
	 * @param tabs Length of string after padding, measured in tabs.
	 * @return The padded string.
	 */
	public static String padRightTabs(String s, int tabs) {
		final int TAB_WIDTH = 4;
		if (s.length() >= tabs * TAB_WIDTH) return s;

		StringBuilder tabString = new StringBuilder();
		for (int i = 0; i < tabs - (s.length() / TAB_WIDTH); i++) {
			tabString.append("\t");
		}
		return s + tabString;
	}

	/**
	 * Returns a string of a given length filled with the given character.
	 *
	 * @param c The character to fill the string with.
	 * @param length Length of string to output.
	 * @return String output.
	 */
	public static String bar(char c, int length) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < length; i++) {
			output.append(c);
		}
		return output.toString();
	}
}
