package misc.eda;

import util.Padder;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

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
	  s h e e
	p

2. Sort the key and every corresponding message letter along with it.

	a e l m p
	---------
	l   h b a
	l   h b a
	l k c b a
	s e e   h
	      p

3. Put it back together.

	l hbal hbalkcbasee h   p

For the step where we arrange the key alphabetically to get new indices,
there are a few ways of achieving this:

	1. (Simple) Use Arrays.sort, then manually match old elements to new
	while recording down the new positions. When encountering a filled index,
	it moves forward until an empty one is found.

	2. (Ok) Implement a KeyCharacter class that holds the old index and is
	also comparable, then sorting a List of these.

	3. (Overkill) Implement a KeyCharacter class... then put it into a TreeSet
	to automatically sort it.

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
		int hangingLength = message.length() % key.length();
		if (hangingLength != 0) {
			message += Padder.bar(' ', key.length() - hangingLength);
		}
		char[] messageArray = message.toCharArray();

		//Generating a Set containing indices of key's character after sorting.
		//Characters appearing more than once in the key will be assign new indices
		//in increasing order.
		Set<KeyCharacter> KeyTree = new TreeSet<>();
		int i = 0;
		for (char[] array = key.toCharArray(); i < array.length; i++) {
			KeyTree.add(new KeyCharacter(array[i], i));
		}
		int newIndex = 0;	//todo does this fetch key characters alphabetically?
		char[] encryptedArray = new char[message.length()];
		for (Iterator<KeyCharacter> iter = KeyTree.iterator(); iter.hasNext(); newIndex++) {
			int oldIndex = iter.next().oldIndex;
			for (int j = 0; j < message.length() / key.length(); j++) {
				encryptedArray[newIndex + key.length() * j] = messageArray[oldIndex + key.length() * j];
			}
		}
		return String.valueOf(encryptedArray);
	}

	public static String decrypt(String message, String key) {
		return null;
	}

	private static class KeyCharacter implements Comparable<KeyCharacter>{
		private final char c;
		final int oldIndex;

		KeyCharacter(char c, int oldIndex) {
			this.c = c;
			this.oldIndex = oldIndex;
		}

		@Override
		public int compareTo(KeyCharacter o) {
			int result = Character.compare(this.c, o.c);
			if (result == 0) result = Integer.compare(this.oldIndex, o.oldIndex);
			return result;
		}
	}
}
