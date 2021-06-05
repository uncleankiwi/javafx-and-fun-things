package misc.eda;

import util.Fraction;

import java.util.Objects;

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
//		test("0.(6)");			//	2/3
//		test("0.(9)");			//	1
//		test("1.(1)");			//	10/9
//		test("3.(142857)");		//	22/7
		test("0.19(2367)");		//	5343/27775
		test("0.1097(3)");		//	823/7500
		test("0");				//	0
		test("12345");			//	12345
		test("15.91");			//	1591/100
//		test("abc");			//	exception
//		test(".(6)");			//	2/3
		test(".19(2367)");		//	5343/27775
		test(".123(6)");
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


