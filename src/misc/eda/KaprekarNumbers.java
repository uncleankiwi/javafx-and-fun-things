package misc.eda;
/*
Return a list of Kaprekar numbers within a given range.
A number n of length l is a Kaprekar number if
	- n^2 is split into 2 numbers; the number on the right having length l
	- and adding the left and right numbers gives n.

e.g.
	1, 10 -> 1, 9
	100, 300 -> 297
	1, 100 -> 1, 9, 45, 55, 99
 */

import java.util.ArrayList;
import java.util.List;

public class KaprekarNumbers {
	public static void main(String[] args) {
		test(1, 10);
		test(100, 300);
		test(1, 100);
	}

	public static List<Integer> getKaprekarNumbers(int low, int high) {
		List<Integer> output = new ArrayList<>();
		for (int i = low; i <= high; i++) {
			if (isKaprekarNumber(i)) output.add(i);
		}
		return output;
	}

	public static boolean isKaprekarNumber(int number) {
		int numberLength = String.valueOf(number).length();
		String squaredStr = String.valueOf(number * number);
		String rightStr = squaredStr.substring(squaredStr.length() - numberLength);
		String leftStr = squaredStr.substring(0, squaredStr.length() - numberLength);
		if (leftStr.equals("")) leftStr = "0";
		int leftRightSum = Integer.parseInt(leftStr) + Integer.parseInt(rightStr);
		return number == leftRightSum;
	}

	public static void test(int low, int high) {
		System.out.println("(" + low + ", " + high + ") -> " + getKaprekarNumbers(low, high));
	}
}
