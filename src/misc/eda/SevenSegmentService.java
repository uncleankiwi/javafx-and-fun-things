package misc.eda;

import java.util.*;

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
	public static void main(String[] args) {
		System.out.println(Arrays.deepToString(instructionsFor("08555")));
	}

	private static Map<String, String[]> BIT_MAP;

	public static String[][] instructionsFor(String string) {
		String[][] allInstructions = new String[string.length()][];
		char[] stringArr = string.toCharArray();
		for (int i = 0; i < stringArr.length; i++) {
			String previous;
			if (i == 0) previous = null;
			else previous = String.valueOf(stringArr[i - 1]);
			String next = String.valueOf(stringArr[i]);
			allInstructions[i] = singleInstruction(previous, next);
		}

		return allInstructions;
	}

	//A single set of instruction for flipping the bits between displaying
	//prev and next.
	//prev can be null; in this case, the display is treated as empty.
	private static String[] singleInstruction(String prev, String next) {
		if (prev == null) return getBitmap(next);
		else {
			String[] oldOn = getBitmap(prev);
			String[] newOn = getBitmap(next);
			//Characters only in old set: lower case
			//Characters only in new set: upper case
			List<String> toOff = getUnique(oldOn, newOn);
			List<String> toOn = getUnique(newOn, oldOn);
			String[] result = new String[toOff.size() + toOn.size()];

			int resultIndex = 0;
			int toOffIndex = 0;
			int toOnIndex = 0;

			//Putting bits into an instructions, sorted by alphabetical order.
			//Bits to be turned on are also capitalized before putting them in.
			while (toOffIndex < toOff.size() && toOnIndex < toOn.size()) {
				if (toOff.get(toOffIndex).compareTo(toOn.get(toOnIndex)) > 0) {
					result[resultIndex] = toOff.get(toOffIndex);
					toOffIndex++;
				}
				else {
					result[resultIndex] = toOn.get(toOnIndex).toUpperCase();
					toOnIndex++;
				}
				resultIndex++;
			}
			while(toOffIndex < toOff.size()) {
				result[resultIndex] = toOff.get(toOffIndex);
				toOffIndex++;
				resultIndex++;
			}
			while(toOnIndex < toOn.size()) {
				result[resultIndex] = toOn.get(toOnIndex).toUpperCase();
				toOnIndex++;
				resultIndex++;
			}

			return result;
		}
	}

	//gets the unique strings that are in list that aren't in comparison
	private static List<String> getUnique(String[] list, String[] comparison) {
		List<String> result = new ArrayList<>();
		for (String str : list) {
			if (Arrays.stream(comparison).noneMatch(x -> x.equals(str))) {
				result.add(str);
			}
		}
		return result;
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
