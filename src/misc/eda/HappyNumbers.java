package misc.eda;

import java.util.ArrayList;
import java.util.List;

/*
	A function f(n) takes a number, squares every one of its digits, then returns the sum of those squares.
	If n is such that f(f(f...f(n))) = 1, then n is a happy number.
	Otherwise, f...f(n)) will loop between several numbers, one of which is 4. In this case, n is an unhappy number.
 */
public class HappyNumbers {
	public static void main(String[] args) {
		isHappySteps(1235);
		isHappySteps(84736);
		isHappySteps(989);
		isHappySteps(10000);
	}

	/**
	 * Determines whether a number is happy. Shows intermediate steps.
	 * @param n input.
	 */
	public static void isHappySteps(int n) {
		Boolean isHappy = null;
		List<Integer> steps = new ArrayList<>();
		steps.add(n);
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
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < steps.size(); i++) {
			if (i != 0) stringBuilder.append(" -> ");
			stringBuilder.append(steps.get(i));
		}
		if (isHappy) stringBuilder.append(" HAPPY!");
		else stringBuilder.append(" sad...");
		System.out.println(stringBuilder);
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
