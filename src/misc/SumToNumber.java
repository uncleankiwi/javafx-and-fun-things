package misc;

import java.util.*;

/*
Find all the possible sets of single-digit numbers of a certain size that sum to a number n.
 */
public class SumToNumber {
	public static void main(String[] args) {

	}

	//Test method: generate all possible combinations of a certain size.
	//Ignores the sum-to-size restriction.
	//The number of possible sets works out to (n+r-1)C(r) - calculate this
	//with Combination.get(n + r - 1, r).
	@SuppressWarnings("unused")
	public static Set<Tally> combinationNoRestriction(int size, int min) {
		Set<Tally> outer = new HashSet<>();
		for (int i = min; i <= 9; i++) {
			if (size == 1) {
				Tally tally = new Tally(i);
				outer.add(tally);
			}
			else {
				Set<Tally> inner = combinationNoRestriction(size - 1, i);
				for (Tally tally : inner) {
					tally.add(i);
				}
				outer.addAll(inner);
			}
		}
		return outer;
	}

	/**
	 * Find all the possible sets of single-digit numbers of a certain size that sum to a number n.
	 * @param size Number of single digit numbers per set.
	 * @param n The number that every set must add up to.
	 * @param min Internal use - prevents duplicate answers. just set to 1.
	 * @return The sets of numbers. May be empty.
	 */
	@SuppressWarnings("unused")
	public static Set<Tally> combination(int size, int n, int min) {
		Set<Tally> outer = new HashSet<>();
		if (size == 1) {
			if (n <= 9 && n >= min) {
				Tally tally = new Tally(n);
				outer.add(tally);
			}
		}
		else {
			for (int i = min; i <= 9; i++) {
				Set<Tally> inner = combination(size - 1,n - i, i);
				for (Tally tally : inner) {
					tally.add(i);
				}
				outer.addAll(inner);
			}
		}
		return outer;
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
