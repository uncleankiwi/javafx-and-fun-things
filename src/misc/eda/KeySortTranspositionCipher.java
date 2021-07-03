package misc.eda;

import util.Padder;

/*
Given a message and a key, encrypt or decrypt it.

e.g. Key 'maple', message 'blah blah black sheep'.
Encryption steps:
1. Assign every letter of message to a letter of key.

	m a p l e
	---------
	b l a h
	b l a h
	b l a c k
	s h e e p

2. Sort the key and every corresponding message letter along with it.

	a e l m p
	---------
	l   h b a
	l   h b a
	l k c b a
	h p e s e

3. Put it back together.

	l hbal hbalkcbahpese

 */
public class KeySortTranspositionCipher {
	public static void main(String[] args) {
		test("blah blah black sheep", "maple");

		test("O may no wintry season, bare and hoary,\n" +
			"See it half finish'd: but let Autumn bold,\n" +
			"With universal tinge of sober gold,\n" +
			"Be all about me when I make an end. ", "Endymion");

		test("Four Seasons fill the measure of the year;\n" +
			"There are four seasons in the mind of man:\n" +
			"He has his lusty Spring, when fancy clear\n" +
			"Takes in all beauty with an easy span:\n" +
			"He has his Summer, when luxuriously\n" +
			"Spring's honied cud of youthful thought he loves\n" +
			"To ruminate, and by such dreaming high\n" +
			"Is nearest unto heaven: quiet coves\n" +
			"His soul has in its Autumn, when his wings\n" +
			"He furleth close; contented so to look\n" +
			"On mists in idleness—to let fair things\n" +
			"Pass by unheeded as a threshold brook.\n" +
			"He has his Winter too of pale misfeature,\n" +
			"Or else he would forego his mortal nature.", "The Human Seasons");
	}
	private static void test(String message, String key) {
		System.out.println("Msg:" + message + " Key:" + key);
		String encrypted = encrypt(message, key);
		System.out.println("Encrypted:" + encrypted);
		System.out.println("Decrypted:" + decrypt(encrypted, key));
		System.out.println();
	}

	public static String encrypt(String message, String key) {
		//padding until message length is multiple of key's length
		message += Padder.bar(' ', message.length() % key.length());

		//Generating an array containing indices of key's character after sorting
		//Characters appearing more than once in the key will be assign new indices
		//in increasing order.
		int[] posAfterSort = new int[key.length()];


		return null;
	}

	public static String decrypt(String message, String key) {
		return null;
	}
}
