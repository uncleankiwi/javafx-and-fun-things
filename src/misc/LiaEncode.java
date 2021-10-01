package misc;

/*
* Converts alphabetical characters in a string into numbers, with 'A' and 'a' both being 0, and 'Z' and 'z' being 26.
* Decoding it reverses the process, but will output all possible strings.
* 	This does not output capital letters or numbers.
* 	E.g. "11" can be decoded as 'aa' and 'k'.
 */
public class LiaEncode {
	public static void main(String[] args) {
		System.out.println(encode("@Lia_Sakurada"));
		System.out.println(encode("aaa"));
		System.out.println(encode("ka"));
	}

	public static String encode(String input) {
		char[] arr = input.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (char c : arr) {
			if (c >= 'A' && c <= 'Z') {
				stringBuilder.append(c - 'A');
			}
			else if (c >= 'a' && c <= 'z') {
				stringBuilder.append(c - 'a');
			}
			else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	public static String decode(String input) {
		return "";
	}
}
