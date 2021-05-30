package misc.eda;

import java.util.ArrayList;
import java.util.List;

/*
Given an integer, return if it is a strong prime, a balanced prime, a weak prime, or not a prime.
	e.g. 211 -> "Balanced"
	17 -> "Strong"
	19 -> "Weak"
	4 -> "Not a prime"
	0 -> "Out of bounds"

Strong prime (number theory): closer to the prime after it than the one before
Weak prime: the opposite.
Balanced prime: equidistant.
 */
public class StrongPrimes {
	public static void main(String[] args) {
//		test(211);
		test(17);
//		test(19);
//		test(4);
//		test(0);
//		test(-200);
	}

	public static String primeStrength(int number) {
		final String OUT_OF_BOUNDS = "Out of bounds";
		final String NOT_PRIME = "Not a prime";
		final String BALANCED = "Balanced";
		final String WEAK = "Weak";
		final String STRONG = "Strong";

		if (number < 2) return OUT_OF_BOUNDS;

		List<Integer> primes = new ArrayList<>();
		primes.add(1);
		int i = 3;
		boolean numberFound = false;
		while (true) {
			boolean isPrime = true;
			for (int prime : primes) {
				if (i % prime == 0 && prime != 1) {
					isPrime = false;
					break;
				}
			}
			if (isPrime) {
				primes.add(i);

				if (i == number) numberFound = true; //number confirmed to be a prime
				else if (i > number && !numberFound) return NOT_PRIME; //number not a found. error.
				else if (i > number) break;	//prime number after 'number' parameter has been found
			}

			if (i >= number)

			if (i == Integer.MAX_VALUE) return OUT_OF_BOUNDS;
			else i += 2;

			System.out.println(primes.size());
		}

		System.out.println(primes.size() + " " + primes);

		int primeBefore = primes.get(primes.size() - 3);
		int primeAfter = primes.get(primes.size() - 1);
		if (number - primeBefore == primeAfter - number) return BALANCED;
		else if (number - primeBefore > primeAfter - number) return STRONG;
		else return WEAK;
	}

	static void test(int number) {
		System.out.println(number + " -> " + primeStrength(number));
	}

	@SuppressWarnings("unused")
	public static List<Integer> listPrimes(int upperLimit) {
		List<Integer> primes = new ArrayList<>();
		primes.add(1);
		for (int i = 3; i <= upperLimit; i += 2) {
			if (i == 1) primes.add(i);
			else {
				boolean isPrime = true;
				for (int prime : primes) {
					if (i % prime == 0 && prime != 1) {
						isPrime = false;
						break;
					}
				}
				if (isPrime) primes.add(i);
			}
		}
		return primes;
	}

}
