package misc.eda;

/*
Persistence of a number n: how many times a function f(n) must be called on n until
n is a single digit number.
e.g.
	additivePersistence(123456) -> 2
	1 + 2 + 3 + 4 + 5 + 6 = 21
	2 + 1 = 3

e.g.
	multiplicativePersistence(123456) -> 2
	1 x 2 x 3 x 4 x 5 x 6 = 720
	7 x 2 x 0 = 0
 */
public class IntegerPersistence {
	public static void main(String[] args) {
		test(123456);
		test(3);
		test(0);
		test(654321);
		test(1679583);
		test(77);
	}

	public static int additivePersistence(int input) {
		int output = 0;
		while (String.valueOf(input).length() > 1) {
			input = additiveReduction(input);
			output++;
		}
		return output;
	}

	private static int additiveReduction(int input) {
		char[] charArray = String.valueOf(input).toCharArray();
		int output = 0;
		for (char c : charArray) {
			output += Integer.parseInt(String.valueOf(c));
		}
		return output;
	}

	public static int multiplicativePersistence(int input) {
		int output = 0;
		while (String.valueOf(input).length() > 1) {
			input = multiplicativeReduction(input);
			output++;
		}
		return output;
	}

	private static int multiplicativeReduction(int input) {
		char[] charArray = String.valueOf(input).toCharArray();
		int output = 1;
		for (char c : charArray) {
			output *= Integer.parseInt(String.valueOf(c));
		}
		return output;
	}

	public static void test(int input) {
		System.out.println("Additive persistence of " + input + " -> " + additivePersistence(input));
		System.out.println("Multiplicative persistence of " + input + " -> " + multiplicativePersistence(input));
		System.out.println();
	}
}
