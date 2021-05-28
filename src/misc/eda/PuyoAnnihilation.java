package misc.eda;
/*
Remove contiguous segments of similar elements repeatedly until no more can be removed.
	['B', 'B', 'A', 'C', 'A', 'A', 'C'] -> ['A']
	['B', 'B', 'C', 'C', 'A', 'A', 'A'] -> []
	['C', 'A', 'C'] -> ['C', 'A', 'C']

All removal possible during 1 turn should occur simultaneously during that turn:
	['A', 'B', 'B', 'A', 'C', 'C', 'A'] -> []
 */

import java.util.Arrays;

public class PuyoAnnihilation {
	public static void main(String[] args) {
		test(new char[] {'B', 'B', 'A', 'C', 'A', 'A', 'C'});
		test(new char[] {'B', 'B', 'C', 'C', 'A', 'A', 'A'});
		test(new char[] {'C', 'A', 'C'});
		test(new char[] {'A', 'B', 'B', 'A', 'C', 'C', 'A'});
	}

	public static char[] annihilate(char[] input) {
		while (true) {

			//if this element is the same as the previous element, mark this and the previous element for removal
			if (input.length == 0) break;

			char previousChar = input[0];
			boolean[] toRemove = new boolean[input.length];
			int removeCount = 0;
			for (int i = 1; i < input.length; i++) {
				if (input[i] == previousChar) {
					toRemove[i] = true;
					removeCount++;
					if (!toRemove[i - 1]) {
						removeCount++;
						toRemove[i - 1] = true;
					}
				}
				previousChar = input[i];
			}

			//copy over elements not marked for removal
			if (removeCount == 0) break;
			else {
				char[] newInput = new char[input.length - removeCount];
				int savedCount = 0;
				for (int i = 0; i < toRemove.length; i++) {
					if (!toRemove[i]) {
						newInput[savedCount] = input[i];
						savedCount++;
					}
				}
				input = newInput;
			}
		}
		return input;
	}

	public static void test(char[] input) {
		System.out.println(Arrays.toString(input) + " -> " + Arrays.toString(annihilate(input)));
	}
}
