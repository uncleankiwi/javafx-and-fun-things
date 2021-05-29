package misc.eda;

/*
Swap the first consonant(s) of two given words.
	e.g. "loving shepherd" -> "shoving lepherd"
If only one word begins with consonants, transplant those to the other word.
	e.g. "ottoman carpet" -> "cottoman arpet"
If both words begin with a vowel, transplant the first vowels plus any following contiguous consonants.
	e.g. "every afternoon" -> "aftery evernoon"
		"unassuming ambience" -> "ambassuming unience"
		"each egg" -> "egg each"
 */


public class Spoonerism {
	public static void main(String[] args) {
		test("loving shepherd");
		test("ottoman carpet");
		test("feeling ornery");
		test("every afternoon");
		test("unassuming ambience");
		test("each egg");
	}

	private static String spoonerise(String input) {
		input = input.toLowerCase();

		String[] inputWords = input.split(" ");
		if (inputWords.length < 2) return null;
		String word1 = inputWords[0];
		String word2 = inputWords[1];

		String word1Front = getWordFront(word1);
		String word1Back = getWordBack(word1, word1Front);
		String word2Front = getWordFront(word2);
		String word2Back = getWordBack(word2, word2Front);

		//if only 1 word starts with consonants, transplant consonants over
		//otherwise, swap their front parts
		if (isVowel(word1.toCharArray()[0]) && !isVowel(word2.toCharArray()[0])) {
			return word2Front + word1 + " " + word2Back;
		}
		else if (!isVowel(word1.toCharArray()[0]) && isVowel(word2.toCharArray()[0])) {
			return word1Back + " " + word1Front + word2;
		}
		else {
			return word2Front + word1Back + " " + word1Front + word2Back;
		}
	}

	private static String getWordFront(String input) {
		StringBuilder outputBuilder = new StringBuilder();
		boolean firstConsonantFound = !isVowel(input.charAt(0));
		//add input string's characters to output until first vowel is found and firstConsonantFound is true
		for (char c : input.toCharArray()) {
			if (isVowel(c)) {
				if (firstConsonantFound) return outputBuilder.toString();
				else outputBuilder.append(c);
			}
			else {
				firstConsonantFound = true;
				outputBuilder.append(c);
			}
		}
		return outputBuilder.toString();
	}

	private static String getWordBack(String input, String wordFront) {
		return input.substring(wordFront.length());
	}

	private static boolean isVowel(char c) {
		for (char vowel : vowels) {
			if (c == vowel) {
				return true;
			}
		}
		return false;
	}

	static final char[] vowels = new char[] {'a', 'e', 'i','o', 'u'};

	static void test(String input) {
		System.out.println(input + " -> " + spoonerise(input));
	}
}
