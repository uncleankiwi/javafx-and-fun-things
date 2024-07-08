package bot_facing;

import java.util.*;

public class InstructionFactory {
	@SuppressWarnings({"unused", "UnnecessaryLocalVariable"})
	public static List<char[]> getInstructions(long seed) {
		List<Character> cList = new ArrayList<>();
		cList.add('^');
		cList.add('>');
		cList.add('v');
		cList.add('<');
		List<char[]> instructions = permute(new char[4], 0, cList);
//		shuffle(instructions, seed);
		return instructions;
	}

	@SuppressWarnings("unused")
	private static void shuffle(List<char[]> list, long seed) {
		Random rand = new Random(seed);
		for (int i = 0; i < list.size(); i++) {
			int randIndex = (int) (rand.nextDouble() * list.size());
			//if a random index selected is elsewhere, swap these two elements
			if (randIndex != i) {
				char[] temp = list.get(i);
				list.set(i, list.get(randIndex));
				list.set(randIndex, temp);
			}
		}
	}

	/*
	Creates a number of copes of 'existing' and appends one character from 'toUse' to each at 'posToAppend' index.
	 */
	private static List<char[]> permute(char[] existing, int posToAppend, List<Character> toUse) {
		List<char[]> result = new ArrayList<>();
		if (toUse.size() <= 1) {
			if (toUse.size() == 1) {
				existing[posToAppend] = toUse.get(0);
			}
			result.add(existing);
		}
		else {
			for (int i = 0; i < toUse.size(); i++) {
				List<Character> toUseCopy = new ArrayList<>(toUse);

				char[] arrCopy = Arrays.copyOf(existing, existing.length);
				char c = toUseCopy.remove(i);
				arrCopy[posToAppend] = c;
				result.addAll(permute(arrCopy, posToAppend + 1, toUseCopy));
			}
		}
		return result;
	}
}
