package misc.eda;

import java.util.*;

/*
Given a number n, convert it into a new number containing the number of contiguous
digits in each sequence and the digit itself.

e.g.
	662 -> 2 6, 1 2 -> 2612

Repeat this for l number of times and return the result at every step.
 */
public class LookAndSaySequence {
	public static void main(String[] args) {
		test(1, 7, asList(1, 11, 21, 1211, 111221, 312211, 13112221));
		test(123, 4, asList(123, 111213, 31121113, 1321123113));
		test(70, 5, asList(70, 1710, 11171110, 31173110, 132117132110L));
		test(111312211, 2, asList(111312211, 3113112221L));
		test(2, 8, asList(2, 12, 1112, 3112, 132112, 1113122112, 311311222112L, 13211321322112L));
		test(144, 4, asList(144, 1124, 211214, 1221121114));
		test(11111111, 3, asList(11111111, 81, 1811));
		test(0, 4, asList(0, 10, 1110, 3110));
	}

	private static void test(long n, long l, List<Long> expectedResult) {
		String output = n + " x" + l + " -> ";
		List<Long> actualResults = lookAndSay(n, l);
		output += actualResults.toString();
		boolean isExpectedResult = (expectedResult.size() == actualResults.size());

		for (int i = 0; i < actualResults.size(); i++) {
			if (!actualResults.get(i).equals(expectedResult.get(i))) {
				isExpectedResult = false;
				break;
			}
		}
		if (!isExpectedResult) output += " (Output wrong!)";
		System.out.println(output);
	}

	private static List<Long> asList(long... k) {
		List<Long> list = new LinkedList<>();
		for (long x : k) {
			list.add(x);
		}
		return list;
	}

	public static List<Long> lookAndSay(long n, long l) {
		List<Long> result = new LinkedList<>();
		result.add(n);
		String nStr = String.valueOf(n);

		for (int i = 2; i <= l; i++) {
			//for every step of the look-and-say sequence
			Character lastChar = null;
			List<Map.Entry<Long, Long>> numbers = new LinkedList<>();
			for (char c : nStr.toCharArray()) {
				if (lastChar == null || !lastChar.equals(c)) {
					numbers.add(new AbstractMap.SimpleEntry<>(Long.parseLong(String.valueOf(c)), 1L));
					lastChar = c;
				}
				else {
					Map.Entry<Long, Long> lastEntry = numbers.get(numbers.size() - 1);
					lastEntry.setValue(lastEntry.getValue() + 1);
				}
			}
			StringBuilder stringBuilder = new StringBuilder();
			for (Map.Entry<Long, Long> entry : numbers) {
				stringBuilder.append(entry.getValue())
					.append(entry.getKey());
			}
			nStr = stringBuilder.toString();
			result.add(Long.parseLong(nStr));
		}

		return result;
	}
}
