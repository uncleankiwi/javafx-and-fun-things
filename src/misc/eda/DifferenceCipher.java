package misc.eda;

import java.util.Arrays;

/*
Encrypt: take a string, return an array of integers.
Decrypt: do the reverse.
First int in the array is the ASCII code of the first letter of the string.
Second int and onwards is the difference between the previous letter and the current.
e.g.
	"He" -> [72, 29]
	ASCII codes of H, e: 72, 101. 101 - 72 = 29.
 */
public class DifferenceCipher {

	public static void main(String[] args) {
		testDifferenceCipher("Hello.");
		testDifferenceCipher("Yes this is a long message.");
	}

	public static int[] encryptDifferenceCipher(String decrypted) {
		int[] encrypted = new int[decrypted.length()];
		if (decrypted.length() == 0) return encrypted;

		encrypted[0] = decrypted.charAt(0);
		if (decrypted.length() == 1) return encrypted;

		for (int i = 1; i < decrypted.length(); i++) {
			encrypted[i] = decrypted.charAt(i) - decrypted.charAt(i - 1);
		}

		return encrypted;
	}

	public static String decryptDifferenceCipher(int[] encrypted) {
		StringBuilder decrypted = new StringBuilder();
		if (encrypted.length == 0) return decrypted.toString();

		decrypted.append((char) encrypted[0]);
		if (encrypted.length == 1) return decrypted.toString();

		for (int i = 1; i < encrypted.length; i++) {
			decrypted.append((char) (decrypted.charAt(i - 1) + encrypted[i]));
		}

		return decrypted.toString();
	}

	public static void testDifferenceCipher(String string) {
		int[] encrypted = encryptDifferenceCipher(string);
		System.out.println(string + " -> " + Arrays.toString(encrypted));
		String decrypted = decryptDifferenceCipher(encrypted);
		System.out.println(Arrays.toString(encrypted) + " -> " + decrypted);
	}
}
