package misc.eda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Return all possible subsets of a list of integers from a list, with each subset being of a given size, and
summing to a given number.
The parent list may have duplicate elements.
 */
public class SubsetSum {
	public static void main(String[] args) {
		test(new int[] { 0, 1, -1, -1, 2 });	//{ { 0, 1, -1 }, { -1, -1, 2 } }")]
		test(new int[] { 0, 0, 0, 5, -5 });	//{ { 0, 0, 0 }, { 0, 5, -5 } }")]
		test(new int[] { 0, -1, 1, 0, -1, 1 });	//{ { 0, -1, 1 }, { 0, 1, -1 }, { -1, 1, 0 }, { -1, 0, 1 }, { 1, 0, -1 } }")]
		test(new int[] { 0, 5, 5, 0, 0 });	//{ { 0, 0, 0 } }")]
		test(new int[] { 0, 5, -5, 0, 0 });	//{ { 0, 5, -5 }, { 0, 0, 0 }, { 5, -5, 0 } }")]
		test(new int[] { 1, 2, 3, -5, 8, 9, -9, 0 });	//{ { 1, 8, -9 }, { 2, 3, -5 }, { 9, -9, 0 } }")]
		test(new int[] { 0, 0, 0 });	//{ { 0, 0, 0 } }")]
		test(new int[] { 1, 5, 5, 2 });	//{  }")]
		test(new int[] { 1, 1 });	//{  }")]
   		test(new int[] { });
	}

	private static void test(int[] parent) {
		Tally tally = new Tally(parent);
		System.out.println(tally.getSubsets(3, 3));
	}

	private static class Tally {
		private final Map<Integer, Integer> tally = new HashMap<>();

		Tally(int[] arr) {
			for (int n : arr) {
				this.add(n);
			}
		}

		private void add(int n) {
			if (!tally.containsKey(n)) tally.put(n, 1);
			else tally.replace(n, tally.get(n) + 1);
		}

		List<List<Integer>> getSubsets(int size, int sum) {
			List<List<Integer>> parentList = new ArrayList<>();
			if (size == 1) {
				if (this.tally.containsKey(sum)) {
					List<Integer> childList = new ArrayList<>();
					childList.add(sum);
					parentList.add(childList);
				}
			}
			else if (size > 1) {
				List<Integer> keyList = new ArrayList<>(this.tally.keySet());
				for (int i = 0; i < keyList.size(); i++) {

				}
			}
			return parentList;
		}


	}
}
