package misc;


import java.util.ArrayList;
import java.util.List;

/*
* Find the minimum number of extra letters that have to be appended to a word to make it a palindrome.
* e.g.
* 	mirror -> 3
* 	mada -> 1
* 	madam -> 0
* 	m -> 0
*	"" -> 0
* */
public class PalindromeSteps {
	public static void main(String[] args) {
		resultPrinter("mirror");
		resultPrinter("mada");
		resultPrinter("madam");
		resultPrinter("m");
		resultPrinter("");
	}

	public static int lettersFromPalindrome(String input) {
		List<Character> forward = new ArrayList<>();
		List<Character> backwards = new ArrayList<>();
		char[] inputArray = input.toCharArray();
		for (int i = 0; i < inputArray.length; i++){
			forward.add(inputArray[i]);
			backwards.add(inputArray[inputArray.length - i - 1]);
		}

		//for every ith position on the forward string, see if the backwards string
		//matches from here till the end
		int matchPosition = 0;
		for (int i = 0; i < forward.size(); i++) {
			for (int j = i; j < forward.size(); j++) {
				if (forward.get(j) == backwards.get(j - i)) {
					matchPosition = j;
				}
				else {
					matchPosition = 0;
					break;
				}
			}
			if (matchPosition != 0) break;
		}


		return matchPosition;
	}

	public static void resultPrinter(String input) {
		System.out.println(input + " -> " + lettersFromPalindrome(input));
	}
}
