package misc.eda;

//Given a string, multiply the integer it represents by 11 without casting the string as a number.
//Return the result as a string.
public class RecursiveMultiply {
	public static void main(String[] args) {
		test("11");								//expected: 121
		test("111111111");						//expected: 1222222221
		test("9473745364784876253253263723");		//expected: 104211199012633638785785900953

	}

	public static String recursiveMultiply11(String input) {

	}

	public static void test(String input) {
		System.out.println(input + " * 11 = " + recursiveMultiply11(input));
	}
}
