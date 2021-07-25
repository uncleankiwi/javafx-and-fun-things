package misc.eda;

import util.Maths;

/*
Left-truncatable prime: a number that always results in a prime number every time the left-most
digit is removed.

Right-truncatable prime: same but for the right-most digit.

Left- and right-truncatable primes may not contain a '0' in them.

Given a number,
	- return "Both" if it is both left- and right-truncatable
	- return "Left" or "Right" if it is only left- or right-truncatable respectively
	- return "Prime" if it is neither or contains a 0
	- return "Not prime" if it is not prime

Input
		Not prime
		Prime
			(Neither/contains 0)
			Both
				Left-truncatable
				Right-truncatable
 */
public class TruncatablePrimes {
	private static final Type NOT_PRIME = Type.Not_prime;
	private static final Type PRIME = Type.Prime;
	private static final Type BOTH = Type.Both;
	private static final Type LEFT = Type.Left;
	private static final Type RIGHT = Type.Right;

	public static void main(String[] args) {
		test(47, LEFT);
		test(347, LEFT);
		test(62383, LEFT);
		test(79, RIGHT);
		test(7331, RIGHT);
		test(233993, RIGHT);
		test(3797, BOTH);
		test(739397, BOTH);
		test(349, PRIME);
		test(5, BOTH);
		test(12, NOT_PRIME);
		test(9137, LEFT);
		test(5939, RIGHT);
		test(317, BOTH);
		test(139, PRIME);
		test(103, PRIME);
	}

	private static void test(int n, Type expectedType) {
		Type actualType = classify(n);
		String output = n + " -> " + actualType;
		if (!actualType.equals(expectedType)) output += " (incorrect output!)";
		System.out.println(output);
	}

	public static Type classify(int n) {
		if (!Maths.isPrime(n)) {
			return NOT_PRIME;
		}
		else if (contains0(n)) return PRIME;
		else {
			boolean leftTruncatable = isLeftTruncatable(n);
			boolean rightTruncatable = isRightTruncatable(n);
			if (leftTruncatable && rightTruncatable) return BOTH;
			if (leftTruncatable) return LEFT;
			if (rightTruncatable) return RIGHT;
			else return PRIME;
		}
	}

	private static boolean contains0(int n) {
		char[] nArr = String.valueOf(n).toCharArray();
		for (char c : nArr) {
			if (c == '0') return true;
		}
		return false;
	}

	//won't check if n itself is prime, since that check should have been done already.
	private static boolean isLeftTruncatable(int n) {
		String nStr = String.valueOf(n);
		for (int i = 1; i < nStr.length(); i++) {
			String subNStr = nStr.substring(i);
			if (!Maths.isPrime(Integer.parseInt(subNStr))) return false;
		}
		return true;
	}

	//won't check if n itself is prime, since that check should have been done already.
	private static boolean isRightTruncatable(int n) {
		String nStr = String.valueOf(n);
		if (nStr.length() <= 1) return true;

		for (int i = nStr.length() - 1; i >= 0; i--) {
			String subNStr = nStr.substring(0, i);
			if (!Maths.isPrime(Integer.parseInt(subNStr))) return false;
		}
		return true;
	}

	private enum Type {
		Not_prime("Not prime"),
		Prime("Prime"),
		Both("Both"),
		Left("Left"),
		Right("Right");

		private final String name;
		Type(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			return this.name;
		}
	}
}
