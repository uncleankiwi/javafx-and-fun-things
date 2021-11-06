package misc.eda;

import java.util.HashMap;
import java.util.Map;

/*
Given a string of characters, count the occurrences of each character.
	If all characters appear the same number of times, return that number.
	If one character appears more/fewer times than the others, return that character and
		how many times it appears less/more often than others.
	Otherwise, return "invalid string".
 */
public class OffByOne {
	public static void main(String[] args) {
		test("aaa");
		test("a");
		test("ab");
		test("bbaa");
		test("tttrr");
		test("aaabbbccc");
		test("aaabbc");
		test("uucuup");
	}

	private static void test(String str) {
		System.out.println(str + " -> " + gradeString(str));
	}

	public static String gradeString(String str) {
		Map<Character, Integer> map = mapChars(str);
		OffByOneTracker tracker = new OffByOneTracker();
		for (Map.Entry<Character, Integer> entry : map.entrySet()) {
			boolean isValid = tracker.add(entry.getKey(), entry.getValue());
			if (!isValid) break;
		}
		return tracker.toString();
	}

	public static Map<Character, Integer> mapChars(String str) {
		HashMap<Character, Integer> map = new HashMap<>();
		for (char c : str.toCharArray()) {
			if (map.containsKey(c)) {
				map.replace(c, map.get(c) + 1);
			} else {
				map.put(c, 1);
			}
		}
		return map;
	}

	static class OffByOneTracker {
		Status status;
		char char1;
		char char2;
		int count1 = -1;
		int count2 = -1;
		int oddCharacter = -1;	//which of the char1 and char2 variables are odd

		boolean add(char c, int count) {
			if (status == Status.Invalid) return false;
			if (count1 == -1) {
				//uninitialized
				status = Status.AllSame;
				char1 = c;
				count1 = count;
				return true;
			}
			else if (count2 == -1 && count == count1) {
				//so far all counts are the same
				return true;
			}
			else if (count2 == -1) {	// && count != count1
				//first odd one out
				status = Status.OneOdd;
				count2 = count;
				char2 = c;
				return true;
			}
			else if (oddCharacter == -1) {
				//not sure which is the odd one out; finding out now
				if (count == count1) {
					oddCharacter = 2;
					return true;
				}
				else if (count == count2) {
					oddCharacter = 1;
					return true;
				}
				else {
					status = Status.Invalid;
					return false;
				}
			}
			else {
				//we know which is the odd one out.
				if (count == count1 && oddCharacter == 2) return true;
				else if (count == count2 && oddCharacter == 1) return true;
				else {
					status = Status.Invalid;
					return false;
				}
			}
		}

		@Override
		public String toString() {
			if (status == Status.AllSame) return "All characters appear " + count1 + " times";
			else if (status == Status.Invalid) return "Invalid string";
			else {
				String str = "";
				int difference;
				if (oddCharacter == 1) {
					str += char1;
					difference = count1 - count2;
				}
				else {
					str += char2;
					difference = count2 - count1;
				}

				str += " appears " + Math.abs(difference);
				if (difference < 0) str += " less";
				else str += " more";
				str += " times than the other characters";
				return str;
			}
		}

		enum Status {
			AllSame,
			OneOdd,
			Invalid
		}
	}
}
