package util;

/*
Methods for formatting output in the log.
 */
public class OutputDrawer {
	//Pads a string with tabs on the right such that the output is of
	//the given length (in tabs).
	public static String padRightTabs(String s, int tabs) {
		final int TAB_WIDTH = 4;
		if (s.length() >= tabs * TAB_WIDTH) return s;

		StringBuilder tabString = new StringBuilder();
		for (int i = 0; i < tabs - (s.length() / TAB_WIDTH); i++) {
			tabString.append("\t");
		}
		return s + tabString;
	}

	//Returns a string of c of given length
	public static String bar(char c, int length) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < length; i++) {
			output.append(c);
		}
		return output.toString();
	}
}
