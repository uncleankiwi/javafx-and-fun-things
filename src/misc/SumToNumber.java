package misc;

import util.Combination;

import java.util.*;

/*
Find all the possible sets of single-digit numbers of a certain size that sum to a number n.
 */
public class SumToNumber {
	public static void main(String[] args) {
		System.out.println(combinationNoRestriction(3, 7));
		System.out.println(Combination.get(5, 3));
	}

	//test method: generate all possible combinations of a certain size
	//ignores the sum-to-size restriction
	//The number of possible sets works out to (n+r-1)C(r) - calculate this
	//with Combination.get(n + r - 1, r).
	public static List<Tally> combinationNoRestriction(int size, int min) {
		if (size == 1) {
			List<Tally> outer = new ArrayList<>();
			for (int i = min; i <= 9; i++) {
				Tally tally = new Tally(i);
				outer.add(tally);
			}
			return outer;
		}
		else {
			List<Tally> outermost = new ArrayList<>();
			for (int i = min; i <= 9; i++) {
				List<Tally> outer = combinationNoRestriction(size - 1, i);
				for (Tally tally : outer) {
					tally.add(i);
				}
				outermost.addAll(outer);
			}
			return outermost;
		}
	}

	public static Set<Set<Integer>> combinations(int size, int n) {
		for (int i = 1; i <= size; i++) {

		}
		return null;
	}

	//min - the single digit may not be less than this
	private static Set<Integer> complement(int n, int min) {
		if (min > n) return null;
		else if (n > 9) return null;
		else {
			Set<Integer> result = new HashSet<>();
			result.add(n);
			return result;
		}
	}

	private static class Tally {
		private final Map<Integer, Integer> digits = new HashMap<>();

		//add a digit to this number
		public void add(int n) {
			if (n <= 9 && n >= 1) {
				if (digits.containsKey(n)) digits.replace(n, digits.get(n) + 1);
				else digits.put(n, 1);
			}
		}

		Tally(int n) {
			this.add(n);
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			for (Map.Entry<Integer, Integer> entry : digits.entrySet()) {
				for (int i = 1; i <= entry.getValue(); i++) {
					stringBuilder.append(entry.getKey());
				}
			}
			return stringBuilder.toString();
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Tally that = (Tally) o;
			return Objects.equals(digits, that.digits);
		}

		@Override
		public int hashCode() {
			return Objects.hash(digits);
		}
	}
}
