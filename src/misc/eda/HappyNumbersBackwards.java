package misc.eda;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/*
	Generates happy numbers with unique digits within a range.
	i.e.
		1. zeroes in a happy number are ignored - 1, 10, 100 are all treated as 1.
		2. order of digits is ignored - 13, 31 are all treated as 13.
		3. when printing, the digits are displayed in ascending order.
	Unlike the HappyNumbers class, this works backwards.
	It takes the base case, 1, and works out all the 1 to n digit numbers that resolve to that,
	until it reaches the point where all numbers obtained are already in the set of answers.
 */
public class HappyNumbersBackwards {
	public static void main(String[] args) {

	}

	private static class UniqueNumber {
		private final Map<Character, Integer> digits;

		UniqueNumber(int n) {
			digits = new HashMap<>();
			for (char c : String.valueOf(n).toCharArray()) {
				if (c != '0') {
					if (digits.containsKey(c)) digits.replace(c, digits.get(c));
					else digits.put(c, 1);
				}
			}
		}

		//gets a set of numbers with 1 to 'size' number of digits
		//that resolve to this number.
		//e.g. if digits were 1, the results would return a set containing 13.
		Set<UniqueNumber> upstream(int size) {
			//todo
			return null;
		}

		//gets a set of numbers that are permutations of this current number
		//e.g. if this number were 13 and the size given is 3,
		//it would return 13, 31, 103, 130, etc.
		private Set<Integer> permutations(int size) {
			//todo
			return null;
		}

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			for (Map.Entry<Character, Integer> entry : digits.entrySet()) {
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
			UniqueNumber that = (UniqueNumber) o;
			return Objects.equals(digits, that.digits);
		}

		@Override
		public int hashCode() {
			return Objects.hash(digits);
		}
	}
}
