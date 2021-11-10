package misc.eda;

import java.util.Arrays;

/*
Given an array of integers,
	- return 0 if the array is empty
	- return the integer that has the greatest number of '5's in it
	- if there is more than one integer in the array with the greatest number of '5's, return the largest of those
	- if none of the integers have '5's, return the first integer of the array
 */
public class Take5 {
	public static void main(String[] args) {
		test(new int[]{}, 0);
		test(new int[]{-55, -155, 45, 31, 67}, -55);
		test(new int[]{5, 12, 55, 11}, 55);
		test(new int[]{5, 12, -55,  11}, -55);
		test(new int[]{515, 1255, -55,  1}, 1255);
		test(new int[]{44, 12, 7, 40}, 44);
		test(new int[]{-1, -43, -67, 3}, -1);
	}

	private static void test(int[] array, int expectedResult) {
		int actualResult = findNumberWithMostFives(array);
		String output = Arrays.toString(array) + " -> " + actualResult;
		if (actualResult != expectedResult) output += " (output wrong!)";
		System.out.println(output);
	}

	public static int findNumberWithMostFives(int[] array) {
		if (array.length == 0) return 0;
		else {
			int result = array[0];
			int fives = numberOfFives(result);

			for (int n : array) {
				int nFives = numberOfFives(n);
				if (nFives > fives){
					result = n;
					fives = nFives;
				}
				else if (fives == nFives && fives != 0) {
					result = Math.max(result, n);
				}
			}
			return result;
		}
	}

	//finds the number of '5's in the given integer
	private static int numberOfFives(int n) {
		char[] nArr = String.valueOf(n).toCharArray();
		int result = 0;
		for (char c : nArr) {
			if (c == '5') result++;
		}
		return result;
	}
}
