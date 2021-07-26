package misc.eda;

import java.util.HashMap;
import java.util.Map;

/*
Receives a String containing a number of digits and converts them into an array
of String[] instructions will display each of the digits in turn on a seven segment
display.

The String instructions assume that the display is blank to begin with; none
of the bits are on.

The first instruction flips the bits on to display the first digit, and the second
onwards are for transforming the previous display into the current display. Capital
letters are for turning the corresponding bit on; lower case turns the bit off.
		a
		_
	f  | |  b
	g	-
	e  | |  c
	   	-
	   	d
 */
public class SevenSegmentService {
	private static Map<String, String[]> BIT_MAP;


	public static String[][] instructionsFor(String string) {

	}

	public static String[] getBitmap(String character) {
		if (BIT_MAP == null) {
			BIT_MAP = new HashMap<>();
			BIT_MAP.put("0", new String[]{"a", "b", "c", "d", "e", "f"});
			BIT_MAP.put("1", new String[]{"b", "c"});
			BIT_MAP.put("2", new String[]{"a", "b", "d", "e", "g"});
			BIT_MAP.put("3", new String[]{"a", "b", "c", "d", "g"});
			BIT_MAP.put("4", new String[]{"b", "c", "f", "g"});
			BIT_MAP.put("5", new String[]{"a", "c", "d", "f", "g"});
			BIT_MAP.put("6", new String[]{"a", "c", "d", "e", "f", "g"});
			BIT_MAP.put("7", new String[]{"a", "b", "c"});
			BIT_MAP.put("8", new String[]{"a", "b", "c", "d", "e", "f", "g"});
			BIT_MAP.put("9", new String[]{"a", "b", "c", "f", "g"});
		}
		return BIT_MAP.get(character);
	}
}
