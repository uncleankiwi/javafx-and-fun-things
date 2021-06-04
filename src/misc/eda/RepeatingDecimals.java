package misc.eda;

import java.util.HashSet;
import java.util.Set;

/*
Converts a decimal number in a string into a fraction in its lowest form.
Numbers surrounded by brackets in the decimal number repeat to infinity.
The idea here is that
	0.3333... 	= 0.(3)
				= 3/10 + 3/100 + 3/1000 + ...
				= 3/10 + 1/10(3/10 + 1/10(3/10 + ...
				= 3/9 * 9/10
					+ 3/9 * 9/10 * 1/10
					+ 3/9 * 9/10 * 1/100
					+ ..
				= 3/9
				= 1/3

	0.19(2367)	= 19/100 + 1/100 * 2367/9999
				= nonRepeatingDigits / placesMultiplier
					+ 1 / placesMultiplier * repeatingDigits / repeatingDivisor
 */
public class RepeatingDecimals {
	public static void main(String[] args) {
		System.out.println(factor(12));
		System.out.println(factor(36));

		test("0.(6)");			//2/3
		test("0.(9)");			//1
		test("1.(1)");			//10/9
		test("3.(142857)");		//22/7
		test("0.19(2367)");		//5343/27775
		test("0.1097(3)");		//"823/7500"
	}

	public static String convertToFraction(String decimalString) {
		return null; //TODO
	}

	public static String simplify(String numerator, String divisor) {
		return null; //TODO
	}

	//returns a set of numbers that a number n is divisible by, excluding 1 and n
	public static Set<Integer> factor(int n) {
		Set<Integer> factors = new HashSet<>();
		int x = n;
		boolean allFactorsFound = false;
		while (!allFactorsFound) {
			for (int i = 2; i <= x / 2; i++) {
				if (x % i == 0 && x != i) {	//i is a factor
					factors.add(i);
					x = x / i;
					break;
				}
				else if (i == x / 2) {
					allFactorsFound = true;
					if (x != n) factors.add(x);
				}
			}
		}
		return factors;
	}

	private static void test(String decimalString) {
		System.out.println(decimalString + " -> " + convertToFraction(decimalString));
	}

}
