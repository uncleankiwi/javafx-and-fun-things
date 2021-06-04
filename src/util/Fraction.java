package util;

import java.util.HashMap;
import java.util.Map;

class Fraction {
	long numerator;
	long denominator;

	Fraction(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	@Override
	public String toString() {
		return this.numerator + "/" + this.denominator;
	}

	public void simplify() {

	}

	//returns a set of numbers that a number n is divisible by, excluding 1 and n
	public static Map<Long, Long> factor(long n) {
		Map<Long, Long> factors = new HashMap<>();
		long x = n;

		boolean allFactorsFound = false;
		while (!allFactorsFound) {
			for (int i = 2; i <= x; i++) {
				if (x % i == 0 && x != i) {	//i is a factor
					addFactor(factors, i);
					x = x / i;
					break;
				}
				else if (x == i) {
					allFactorsFound = true;
					if (x != n) addFactor(factors, x);
				}
			}
		}
		return factors;
	}

	private static void addFactor(Map<Long, Long> factors, long factor) {
		if (factors.containsKey(factor)) {
			factors.replace(factor, factors.get(factor) + 1);
		}
		else {
			factors.put(factor, 1L);
		}
	}


}