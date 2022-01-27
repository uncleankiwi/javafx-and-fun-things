package misc.eda;

import java.util.Arrays;

/*
Given an input array of integers, return the contiguous subsequence of integers in it that has the largest sum.
Or, if the input array is empty or if the largest sum is negative, return an empty array.
 */
public class MaxContiguousSubset {

	public static void main(String[] args) {
		//normal tests
		test(new int[] {1, 2});
		test(new int[] {11, 21, -3, 2, 11});

		//mid exclusion
		test(new int[] {10, -20, 11});
		test(new int[] {3, 4, -20, 5});
		test(new int[] {4, -20, 2, 8});
		test(new int[] {14, -20, 2, 8});
		test(new int[] {1, 6, 2, -20, 11, 3, 1});

		//side exclusion
		test(new int[] {-1, 0, -1, 30, 3});
		test(new int[] {4, -2, -2, 0});
		test(new int[] {-2, -1, 5, 6, 7, -20, 3, 4, 1, -1, -1});
		test(new int[] {-2, -1, 1, 3, 2, -20, 8, 8, 1, -1, -1});
		test(new int[]{-8, 3, 3, 2, -100, 5, 6, 9, -100, 4, 4, 3, -9, -10});

		//weird
		test(new int[] {});
		test(new int[] {0});
		test(new int[] {-50});
		test(new int[] {-3, -8});
	}

	public static void test(int[] input) {
//		System.out.println("testing: " + Arrays.toString(input) + " -> "  + Arrays.toString(largestSubsequence(input)));
		System.out.println("testing: " + Arrays.toString(input) + " -> "  + Arrays.toString(bruteForceSubsequence(input)));
	}

	//O(n^2) brute force method that basically checks all possible subsequences
	public static int[] bruteForceSubsequence(int[] input) {
		Subsequence largestSub = new Subsequence();
		for (int i = 0; i < input.length; i++) {
			if (input[i] > 0) {
				Subsequence subsequence = new Subsequence();
				subsequence.start = i;
				for (int j = i; j < input.length; j++) {
					subsequence.total += input[j];
					subsequence.end = j + 1;
					if (subsequence.total > largestSub.total) {
						largestSub.copy(subsequence);
					}
				}
			}
		}
		return Arrays.copyOfRange(input, largestSub.start, largestSub.end);
	}

	//a subsection of an array summing total, and beginning from the indices. Start is inclusive, end is exclusive.
	private static class Subsequence {
		int start;
		int end;
		int total;

		void copy(Subsequence sub) {
			this.start = sub.start;
			this.end = sub.end;
			this.total = sub.total;
		}
	}

	public static int[] largestSubsequence(int[] input) {

		if (input.length == 0) return new int[0];

		int[] arraySummedForward = new int[input.length];
		int[] arraySummedBackwards = new int[input.length];
		int largestForwardSum = -1;
		int largestForwardSumIndex = -1;
		int largestBackwardSum = -1;
		int largestBackwardSumIndex = -1;

		//going forwards to find elements at the end to omit
		for (int i = 0; i < input.length; i++) {
			if (i == 0) {
				arraySummedForward[0] = input[0];
				arraySummedBackwards[input.length - 1] = input[input.length - 1];
			}
			else {
				arraySummedForward[i] = input[i] + arraySummedForward[i - 1];
//				arraySummedBackwards[input.length - 1 - i] = input[input.length - 1 - i] + arraySummedBackwards[input.length - i];
			}
			if (arraySummedForward[i] > largestForwardSum) {
				largestForwardSum = arraySummedForward[i];
				largestForwardSumIndex = i;
			}
//			if (arraySummedBackwards[input.length - 1 - i] > largestBackwardSum) {
//				largestBackwardSum = arraySummedBackwards[input.length - 1 - i];
//				largestBackwardSumIndex = input.length - 1 - i;
//			}
		}

		//going backwards to determine how many elements at the front to omit

		for (int i = largestForwardSumIndex; i >= 0; i--) {
			if (i == largestForwardSumIndex) {
				largestBackwardSumIndex = largestForwardSumIndex;
			}
			else {
				arraySummedBackwards[i] = input[i] + arraySummedBackwards[i + 1];
			}
			if (arraySummedBackwards[i] > largestBackwardSum) {
				largestBackwardSum = arraySummedBackwards[i];
				largestBackwardSumIndex = i;
			}
		}

		if (largestForwardSum < 0 && largestBackwardSum < 0) {
			return new int[0];
		}

//		System.out.println("largest forward, backwards:" + largestForwardSumIndex + ":" + largestBackwardSumIndex);
		return Arrays.copyOfRange(input, largestBackwardSumIndex, largestForwardSumIndex + 1);

		//return output;
	}


}
