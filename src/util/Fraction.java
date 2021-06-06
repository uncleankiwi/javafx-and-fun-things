package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Fraction {
	long numerator;
	long denominator;

	public Fraction(long numerator, long denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	@Override
	public String toString() {
		if (this.denominator == 1L) return String.valueOf(this.numerator);
		else return this.numerator + "/" + this.denominator;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Fraction fraction = (Fraction) o;
		return numerator == fraction.numerator && denominator == fraction.denominator;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numerator, denominator);
	}

	public void simplify() {
		//if zero
		if (numerator == 0) {
			denominator = 1;
			return;
		}

		//getting a set of common factors
		Map<Long, Long> numeratorFactors = factor(numerator);
		Map<Long, Long> denominatorFactors = factor(denominator);
		Map<Long, Long> commonFactors = new HashMap<>();

		for (Long numeratorKey : numeratorFactors.keySet()) {
			if (denominatorFactors.containsKey(numeratorKey)) {
				commonFactors.put(numeratorKey, Math.min(numeratorFactors.get(numeratorKey), denominatorFactors.get(numeratorKey)));
			}
		}
		if (commonFactors.size() == 0) return;

		//cancelling out common factors
		for (Long commonKey : commonFactors.keySet()) {
			if (numeratorFactors.get(commonKey).equals(commonFactors.get(commonKey))) {
				numeratorFactors.remove(commonKey);
			}
			else {
				numeratorFactors.replace(commonKey, numeratorFactors.get(commonKey)- commonFactors.get(commonKey));
			}

			if (denominatorFactors.get(commonKey).equals(commonFactors.get(commonKey))) {
				denominatorFactors.remove(commonKey);
			}
			else {
				denominatorFactors.replace(commonKey, denominatorFactors.get(commonKey) - commonFactors.get(commonKey));
			}
		}

		//multiplying non-common factors to get numerator and denominator again
		numerator = 1;
		denominator = 1;
		for (Map.Entry<Long, Long> entry : numeratorFactors.entrySet()) {
			numerator = numerator * (long) Math.pow(entry.getKey(), entry.getValue());
		}
		for (Map.Entry<Long, Long> entry : denominatorFactors.entrySet()) {
			denominator = denominator * (long) Math.pow(entry.getKey(), entry.getValue());
		}
	}

	public static Fraction add(Fraction f1, Fraction f2) {
		f1.simplify();
		f2.simplify();
		long newNumerator = f1.numerator * f2.denominator + f2.numerator * f1.denominator;
		long newDenominator = f1.denominator * f2.denominator;
		Fraction result = new Fraction(newNumerator, newDenominator);
		result.simplify();
		return result;
	}

	public static Fraction add(Fraction f1, long x) {
		return new Fraction(f1.numerator + f1.denominator * x, f1.denominator);
	}

	public static Fraction add(long x, Fraction f1) {
		return add(f1, x);
	}

	public static Fraction multiply(Fraction f1, Fraction f2) {
		f1.simplify();
		f2.simplify();
		long newNumerator = f1.numerator * f2.numerator;
		long newDenominator = f1.denominator * f2.denominator;
		Fraction result = new Fraction(newNumerator, newDenominator);
		result.simplify();
		return result;
	}

	/*
	Splits input into components, then puts them back together as a fraction.
	e.g. 20.19(2367)	= 20 + 19/100 + 1/100 * 2367/9999
						= ones
							+ nonRepeating / placesMultiplier
							+ 1 / placesMultiplier * repeating / repeatingDivisor
						= ones + nonRepeatingFraction + repeatingFraction
 */

	public static Fraction parseFraction(String s) throws NumberFormatException {
		StringBuilder onesStr = new StringBuilder();
		StringBuilder nonRepeatingStr = new StringBuilder();
		StringBuilder repeatingStr = new StringBuilder();

		//separating input into components
		ParseState parseState = ParseState.ONES;
		char[] sCharArr = s.toCharArray();
		for (char c : sCharArr) {
			switch (parseState) {
				case ONES: {
					if (c == '.') {
						parseState = ParseState.NON_REPEATING;
					} else {
						try {
							onesStr.append(Integer.parseInt(String.valueOf(c)));
						} catch (NumberFormatException e) {
							throw new NumberFormatException("Invalid character before decimal point");
						}
					}
					break;
				}

				case NON_REPEATING: {
					if (c == '(') {
						parseState = ParseState.REPEATING;
					} else {
						try {
							nonRepeatingStr.append(Integer.parseInt(String.valueOf(c)));
						} catch (NumberFormatException e) {
							throw new NumberFormatException("Invalid character after decimal point");
						}
					}
					break;
				}

				case REPEATING: {
					if (c == ')') {
						parseState = ParseState.AFTER_REPEATING;
					} else {
						try {
							repeatingStr.append(Integer.parseInt(String.valueOf(c)));
						} catch (NumberFormatException e) {
							throw new NumberFormatException("Invalid character in repeating decimals");
						}
					}
					break;
				}

				case AFTER_REPEATING: {
					throw new NumberFormatException("Input has unexpected trailing characters");
				}
			}
		}

		//parsing each component as long or fraction
		if (onesStr.toString().equals("")) onesStr.append("0");
		long ones = Long.parseLong(onesStr.toString());

		Fraction nonRepeatingFraction;
		int places = nonRepeatingStr.toString().length();
		long placesMultiplier = (long) Math.pow(10, places);
		if (nonRepeatingStr.toString().equals("")) {
			nonRepeatingFraction = new Fraction(0, 1);
		}
		else {
			long nonRepeating = Long.parseLong(nonRepeatingStr.toString());
			nonRepeatingFraction = new Fraction(nonRepeating, placesMultiplier);
		}

		Fraction repeatingFraction;
		if (repeatingStr.toString().equals("")) {
			repeatingFraction = new Fraction(0, 1);
		}
		else {
			if (repeatingStr.toString().equals("0")) repeatingStr.append("0");
			long repeating = Long.parseLong(repeatingStr.toString());
			StringBuilder repeatingDivisorStr = new StringBuilder();
			for (int i = 0; i < repeatingStr.length(); i++) repeatingDivisorStr.append("9");
			long repeatingDivisor = Long.parseLong(repeatingDivisorStr.toString());
			repeatingFraction = Fraction.multiply(
				new Fraction(1, placesMultiplier),
				new Fraction(repeating, repeatingDivisor));
		}

		//putting together components
		return Fraction.add(ones, Fraction.add(nonRepeatingFraction, repeatingFraction));
	}

	//returns a set of numbers that a number n is divisible by, excluding 1 and n
	public static Map<Long, Long> factor(long n) {
		Map<Long, Long> factors = new HashMap<>();
		long x = n;

		boolean allFactorsFound = (x <= 2);

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

	private enum ParseState {
		ONES,
		NON_REPEATING,
		REPEATING,
		AFTER_REPEATING
	}


}