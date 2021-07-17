package util;

import java.util.HashSet;
import java.util.Set;

/**
 * A collection of math-related methods.
 */
public class Maths {
	/**
	 * Returns the set of prime factors of a number, including itself,
	 * and excluding 1 (which isn't a prime).
	 * Similar to Fraction.factor().
	 * @param x a number to get the prime factors of.
	 * @return Its set of prime factors.
	 */
	public static Set<Long> primeFactorsOf(long x) {
		Set<Long> result = new HashSet<>();
		while (x > 1) {
			for (long i = 1; i <= x; i = i + 2) {
				if (i == 1) i = 2;	//aside from all odd numbers, also check 2.
				if (x % i == 0) {
					result.add(i);
					x = x / i;
					break;
				}
				if (i == 2) i = 1;
			}
		}

		return result;
	}
}
