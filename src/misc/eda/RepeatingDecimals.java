package misc.eda;

import util.Fraction;

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

See Fraction.java for more documentation.
 */
public class RepeatingDecimals {
	public static void main(String[] args) {
		//standard
		System.out.println("Standard:");
		test("0.(6)");			//	2/3
		test("0.(9)");			//	1
		test("1.(1)");			//	10/9
		test("3.(142857)");		//	22/7
		test("0.19(2367)");		//	5343/27775
		test("0.1097(3)");		//	823/7500

		//whole numbers
		System.out.println("\nWhole numbers:");
		test("0");				//	0
		test("12345");			//	12345
		test("15.91");			//	1591/100

		//no leading 0
		System.out.println("\nNo leading 0s:");
		test(".(6)");			//	2/3
		test(".19(2367)");		//	5343/27775

		//exceptions
		System.out.println("\nExceptions:");
		test("abc");
		test("(2)");
		test("0.3k");
		test(".84(4*)");
		test("3.3643(73)3");

		//unknowns
		System.out.println("\nUnknowns:");
		test("");
		test("00005.19(47)");
		test(".123(6)");
		test("14.5(0)");
		test("0.0");
		test("0.(0)");
	}

	private static void test(String decimalString) {
		String output;
		try {
			output = Fraction.parseFraction(decimalString).toString();
		}
		catch (NumberFormatException e) {
			output = e.getMessage();
		}

		System.out.println(decimalString + " -> " + output);
	}


}


