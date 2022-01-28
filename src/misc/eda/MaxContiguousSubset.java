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
		test(new int[]{-8, 3, 3, 2, -4, 5, 6, 9, -100, 4, 4, 3, -9, -10});
		test(new int[]{-8, 3, 3, 2, -100, 5, 6, 9, -7, 4, 4, 3, -9, -10});
		test(new int[]{-8, 3, 3, 2, -2, 5, 6, 9, -3, 4, 4, 3, -9, -10});

		//weird
		test(new int[] {});
		test(new int[] {0});
		test(new int[] {-50});
		test(new int[] {-3, -8});
	}

	public static void test(int[] input) {
		System.out.println("testing: " + Arrays.toString(input) + " -> "  + Arrays.toString(largestSubsequence(input)));
//		System.out.println("testing: " + Arrays.toString(input) + " -> "  + Arrays.toString(bruteForceSubsequence(input)));
	}

	//O(n^2) brute force method that basically checks all possible subsequences
	@SuppressWarnings("unused")
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

	//O(n). At every index, determine if the previous element's sequence should be added to this element's
	//base case: first positive element's sum and position memoized
	//kth element: if k - 1's sequence total is negative, start a new sequence from k.
	@SuppressWarnings("unused")
	public static int[] largestSubsequence(int[] input) {
		Subsequence largestSub = new Subsequence();
		Subsequence[] subs = new Subsequence[input.length];
		for (int i = 0; i < input.length; i++) {
			Subsequence sub = new Subsequence();
			subs[i] = sub;
			//base case
			if (i == 0) {
				sub.start = i;
				sub.end = i + 1;
				sub.total = input[i];
			}
			//kth case
			else {
				//if k - 1's sequence total is negative
				if (subs[i - 1].total < 0) {
					sub.start = i;
					sub.end = i + 1;
					sub.total = input[i];
				}
				//if k - 1's sequence is positive, add it to this element's memoized sequence
				else {
					sub.start = subs[i - 1].start;
					sub.end = i + 1;
					sub.total = subs[i - 1].total + input[i];
				}
			}
			if (sub.total > largestSub.total) largestSub = sub;
		}
		return Arrays.copyOfRange(input, largestSub.start, largestSub.end);
	}
}
