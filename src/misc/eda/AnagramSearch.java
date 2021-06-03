package misc.eda;

import java.util.*;

/*
Returns the index position(s) of a string and anagrams of it found within another string.
Ignores case.

findAnagrams("est", "tesseract tessellation test fest ready set go")
findAnagrams("mag", "Gam-gam's game magazines")
findAnagrams("nope", "move along now, nothing to see")
 */
public class AnagramSearch {
	public static void main(String[] args) {
		test("est", "tesseract tessellation test fest ready set go");
		test("mag", "Gam-gam's game magazines");
		test("nope", "move along now, nothing to see");
		test("neo", "exactly one result");
	}

	public static List<Integer> findAnagrams(String searchString, String body) {
		List<Integer> indices = new ArrayList<>();
		Map<Character, Integer> searchMap = map(searchString);
		for (int i = 0; i < body.length() - searchString.length(); i++) {
			Map<Character, Integer> bodyMap = map((body.substring(i, i + searchString.length()).toLowerCase()));
			if (searchMap.equals(bodyMap)) indices.add(i);
		}
		return indices;
	}

	private static Map<Character, Integer> map(String str) {
		Map<Character, Integer> cMap = new HashMap<>();
		for (char c : str.toCharArray()) {
			if (cMap.containsKey(c)) {
				cMap.put(c, cMap.get(c) + 1);
			}
			else {
				cMap.put(c, 0);
			}
		}
		return cMap;
	}

	public static void test(String searchString, String body) {
		System.out.println("Searching for [" + searchString + "] in [" + body + "] -> " + findAnagrams(searchString, body));
	}
}
