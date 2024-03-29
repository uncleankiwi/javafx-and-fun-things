package misc.eda;

import java.util.*;

/*
	Generates happy numbers with unique digits within a range.
	i.e.
		1. zeroes in a happy number are ignored - 1, 10, 100 are all treated as 1.
		2. order of digits is ignored - 13, 31 are all treated as 13.
		3. when printing, the digits are displayed in ascending order.
	Unlike the HappyNumbers class, this works backwards.
	It takes the base case, 1, and works out all the 1 to n digit numbers that resolve to that,
	until it reaches the point where all numbers obtained are already in the set of answers.

	See SumToNumber class for a simplified version of the logic used to generate a set of n numbers that
	sum to a given number.
 */
public class HappyNumbersBackwards {
	public static void main(String[] args) {
		UniqueNumber n = new UniqueNumber(1);
		printUpstream(n, 3, 3);
	}

	//This branches the given happy number up to sizeToCheck digits then prunes down to sizeToPrint for printing.
	//This doesn't actually make any difference!
	//When calculating 3-digit happy numbers, that is. None are missed when setting sizeToCheck = sizeToPrint = 3.
	@SuppressWarnings("unused")
	public static void printUpstream(UniqueNumber n, int sizeToCheck, int sizeToPrint) {
		Set<UniqueNumber> set = n.upstream(sizeToCheck);
		set.stream().sorted().forEach(x -> {
			String str = x.toString();
			if (str.length() <= sizeToPrint) System.out.print(x + ", ");
		});
		System.out.println();
	}

	@SuppressWarnings("unused")
	private static void testPermutations(int n) {
		UniqueNumber x = new UniqueNumber(n);
		for (int i = 1; i <= 5; i++) {
			System.out.println("unique number " + n + ": " + i + " digits:" + x.permutationsWrapper(i));
		}
	}

	private static class UniqueNumber implements Comparable<UniqueNumber> {
		private final Map<Character, Integer> digits;
		private int size = 0;

		UniqueNumber(int n) {
			digits = new HashMap<>();
			for (char c : String.valueOf(n).toCharArray()) {
				add(c);
			}
		}

		private void add(Map<Character, Integer> tally, char c) {
			if (tally.containsKey(c)) tally.replace(c, tally.get(c) + 1);
			else tally.put(c, 1);
		}

		private void add(char c) {
			if (c != '0') {
				size++;
				add(this.digits, c);
			}
		}

		//gets a set of numbers with 1 to 'size' number of digits
		//that resolve to this number.
		//e.g. if digits were 1, the results would return a set containing 13.
		public Set<UniqueNumber> upstream(int size) {
			Set<UniqueNumber> uniqueNumbers = new HashSet<>();
			Set<UniqueNumber> pending = new HashSet<>();
			pending.add(this);
			while (pending.size() > 0) {
				Set<UniqueNumber> nextPending = new HashSet<>();
				for (UniqueNumber n : pending) {
					if (!uniqueNumbers.contains(n)) {
						uniqueNumbers.add(n);
						Set<Integer> permutations = new HashSet<>(n.permutationsWrapper(size));
						for (int m : permutations) {
							for (int i = 1; i <= size; i++) {
								nextPending.addAll(sumOfSquaresToNumber(i, m, 1));
							}
						}
					}
				}
				pending = nextPending;
			}

			return uniqueNumbers;
		}

		//gets every unique combination of digits that when individually squared, gives the sum
		private static Set<UniqueNumber> sumOfSquaresToNumber(int size, int n, int min) {
			Set<UniqueNumber> outer = new HashSet<>();
			if (size == 1) {
				if (n <= 9 * 9 && n >= min * min) {
					for (int i = min; i * i <= n && i <= 9; i++) {
						if (i * i == n) {
							UniqueNumber tally = new UniqueNumber(i);
							outer.add(tally);
						}
					}
				}
			}
			else {
				for (int i = min; i <= 9; i++) {
					Set<UniqueNumber> inner = sumOfSquaresToNumber(size - 1,n - i * i, i);
					for (UniqueNumber tally : inner) {
						tally.add(String.valueOf(i).toCharArray()[0]);
					}
					outer.addAll(inner);
				}
			}
			return outer;
		}

		//wrapper for recursive method
		private Set<Integer> permutationsWrapper(int size) {
			if (this.size > size) return new HashSet<>();
			Map<Character, Integer> digitsCopy = new HashMap<>(digits);
			//adding '0's if necessary
			for (int i = 1; i <= size - this.size; i++) {
				add(digitsCopy, '0');
			}
			Set<Integer> integerSet = new HashSet<>();
			Set<String> stringSet = permutations(digitsCopy, size);
			for (String str : stringSet) {
				integerSet.add(Integer.parseInt(str));
			}
			return integerSet;
		}

		//gets a set of numbers that are permutations of this current number
		//e.g. if this number were 13 and the size given is 3,
		//it would return 13, 31, 103, 130, etc.
		//This also needs to return Strings so that zeroes in the middle of a number
		//don't get erased by Integer.parseInt().
		private Set<String> permutations(Map<Character, Integer> tally, int size) {
			Set<String> set = new HashSet<>();
			//base case
			if (size == 1) {
				for (Map.Entry<Character, Integer> entry : tally.entrySet()) {
					set.add(entry.getKey().toString());
				}
			}
			else {
				//getting downstream permutations, then prefixing the current character
				for (Map.Entry<Character, Integer> entry : tally.entrySet()) {
					Map<Character, Integer> tallyCopy = new HashMap<>(tally);
					if (entry.getValue() == 1) {
						tallyCopy.remove(entry.getKey());
					}
					else {
						tallyCopy.replace(entry.getKey(), entry.getValue() - 1);
					}
					Set<String> partialResult = permutations(tallyCopy, size - 1);
					for (String str : partialResult) {
						set.add(str + entry.getKey());
					}
				}
			}
			return set;
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

		@Override
		public int compareTo(UniqueNumber o) {
			return Integer.valueOf(this.toString()).compareTo(Integer.valueOf(o.toString()));
		}
	}
}
