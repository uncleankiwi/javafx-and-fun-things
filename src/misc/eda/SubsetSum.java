package misc.eda;

import java.util.*;

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
		System.out.println(tally.getSubsets(3, 0));
	}

	private static class Tally implements Cloneable {
		private Map<Integer, Integer> tally = new HashMap<>();

		Tally(int[] arr) {
			for (int n : arr) {
				this.add(n);
			}
		}

		private void add(int n) {
			if (!tally.containsKey(n)) tally.put(n, 1);
			else tally.replace(n, tally.get(n) + 1);
		}

		@SuppressWarnings("SameParameterValue")
		List<List<Integer>> getSubsets(int size, int sum) {
			return getSubsets(this, size, sum);
		}

		private static List<List<Integer>> getSubsets(Tally t, int size, int sum) {
			List<List<Integer>> parentList = new ArrayList<>();
			if (size == 1) {
				if (t.tally.containsKey(sum)) {
					List<Integer> childList = new ArrayList<>();
					childList.add(sum);
					parentList.add(childList);
				}
			}
			else if (size > 1) {
				Tally copy = t.clone();
				while (copy.tally.size() > 0) {
					//get any element in this tally
					int currentInt = copy.tally.keySet().stream().findFirst().get();

					//find all valid lists downstream, then append this element
					Tally copy2 = copy.clone();
					copy2.subtract(currentInt);
					List<List<Integer>> childList = getSubsets(copy2, size - 1, sum - currentInt);
					for (List<Integer> list : childList) {
						list.add(currentInt);
					}

					//add results to list, then remove this element from tally
					parentList.addAll(childList);
					copy.remove(currentInt);
				}
			}
			return parentList;
		}

		//removes 1 count from the given number in this tally
		void subtract(int n) {
			if (this.tally.containsKey(n)) {
				if (this.tally.get(n) == 1) {
					this.tally.remove(n);
				}
				else {
					this.tally.replace(n, this.tally.get(n) - 1);
				}
			}
			else {
				throw new RuntimeException("Tally doesn't contain the key " + n);
			}
		}

		//removes all counts of the given number in this tally
		void remove(int n) {
			if (this.tally.containsKey(n)) {
				this.tally.remove(n);
			}
			else {
				throw new RuntimeException("Tally doesn't contain the key " + n);
			}
		}

		@Override
		public Tally clone() {
			try {
				Tally clone = (Tally) super.clone();
				clone.tally = new HashMap<>(this.tally);
				return clone;
			} catch (CloneNotSupportedException e) {
				throw new AssertionError();
			}
		}
	}
}
