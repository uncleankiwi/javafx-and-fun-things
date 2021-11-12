package misc.eda;

import java.util.*;

/*
	A function f(n) takes a number, squares every one of its digits, then returns the sum of those squares.
	If n is such that f(f(f...f(n))) = 1, then n is a happy number.
	Otherwise, f...f(n) will loop between several numbers, one of which is 4. In this case, n is an unhappy number.
 */
public class HappyNumbers {
	public static void main(String[] args) {
		List<Integer> list = new ArrayList<>(get3DigitNumbers());
		Collections.sort(list);
		System.out.println(list);
	}

	/**
	 * Gets a set of happy numbers from 1 to 999.
	 * Memoizes results as it goes.
	 * @return set of happy numbers.
	 */
	@SuppressWarnings("unused")
	public static Set<Integer> get3DigitNumbers() {
		Set<Integer> happySet = new HashSet<>();
		Set<Integer> sadSet = new HashSet<>();
		for (int i = 1; i <= 999; i++) {
			List<Integer> tempList = new ArrayList<>();
			tempList.add(i);

			int k = i;
			Boolean isHappy = null;

			while (isHappy == null) {
				int strippedInt = stripZeros(k);
				if (happySet.contains(strippedInt)) isHappy = true;
				else if (sadSet.contains(strippedInt)) isHappy = false;
				else if (strippedInt == 1) isHappy = true;
				else if (strippedInt == 4) isHappy = false;
				else {
					k = squaredDigits(strippedInt);
					tempList.add(k);
				}

				if (isHappy == null) {
					k = squaredDigits(strippedInt);
					tempList.add(k);
				}
				else if (isHappy) {
					happySet.addAll(tempList);
				}
				else {
					sadSet.addAll(tempList);
				}
			}

		}
		return happySet;
	}

	/**
	 * Determines whether a number is happy. Shows intermediate steps.
	 * @param n input.
	 */
	@SuppressWarnings("unused")
	public static void isHappySteps(int n) {
		Intermediates intermediates = new Intermediates(n);
		intermediates.resolve();
		System.out.println(intermediates);
	}

	private static class Intermediates {
		List<Integer> steps = new ArrayList<>();
		Boolean isHappy = null;

		Intermediates(int n) {
			steps.add(n);
		}

		void resolve() {
			int n = steps.get(0);
			while (isHappy == null) {
				if (n == 1) isHappy = true;
				else if (n == 4) isHappy = false;
				else {
					int strippedInt = stripZeros(n);
					if (strippedInt == 1) isHappy = true;
					else if (strippedInt == 4) isHappy = false;
					n = squaredDigits(strippedInt);
					steps.add(n);
				}
			}
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < steps.size(); i++) {
				if (i != 0) stringBuilder.append(" -> ");
				stringBuilder.append(steps.get(i));
			}
			if (isHappy) stringBuilder.append(" HAPPY!");
			else stringBuilder.append(" sad...");
			return stringBuilder.toString();
		}
	}

	/**
	 * Recursively determines whether a number is happy.
	 * @param n input.
	 * @return if input is a happy number.
	 */
	@SuppressWarnings("unused")
	public static boolean isHappy(int n) {
		if (n == 1) return true;
		else if (n == 4) return false;
		else {
			int strippedInt = stripZeros(n);
			if (strippedInt == 1) return true;
			else if (strippedInt == 4) return false;
			else {
				return isHappy(squaredDigits(strippedInt));
			}
		}
	}

	//strips any zeroes in an integer.
	//100 will return 1. 40 will return 4.
	private static int stripZeros(int n) {
		String str = String.valueOf(n);
		StringBuilder stringBuilder = new StringBuilder();
		for (char c : str.toCharArray()) {
			if (c != '0') stringBuilder.append(c);
		}
		return Integer.parseInt(stringBuilder.toString());
	}

	private static int squaredDigits(int n) {
		int result = 0;
		for (char c : String.valueOf(n).toCharArray()) {
			result += Math.pow(Integer.parseInt(String.valueOf(c)), 2);
		}
		return result;
	}
}
