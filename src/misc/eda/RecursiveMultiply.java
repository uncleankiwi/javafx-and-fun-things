package misc.eda;

//Given a string, multiply the integer it represents by a given integer without casting the string as a number.
//Return the result as a string.
//Extra limitations: use recursion and don't use arrays/lists.
public class RecursiveMultiply {
	public static void main(String[] args) {
		test("22", 11);
		test("0", 11);
		test("0000", 11);
		test("11", 11);								//expected: 121
		test("111111111", 11);						//expected: 1222222221
		test("9473745364784876253253263723", 11);		//expected: 104211199012633638785785900953
	}

	public static String recursiveMultiply(String input, int number) {
		String rightPart = String.valueOf(numberAt(input, input.length() - 1) * number);
		String result = rightPart;
		if (input.length() != 1) {
			String leftPart = recursiveMultiply(input.substring(0, input.length() - 1), number) + "0";

			//creating a third string for carried numbers
			//it should be 1 '0' longer than the left part since the left-most digit could also get carried
			StringBuilder carryBuilder = new StringBuilder("0");
			for (int i = 0; i < leftPart.length(); i++) {
				carryBuilder.append("0");
			}
			String carry = carryBuilder.toString();
			String addedResult = carry;

			//adding left, right, carry strings and putting into addedResult
			for (int i = 0; i < addedResult.length(); i++) {
				int numberLeft = 0;
				if (leftPart.length() - 1 - i >= 0) {
					numberLeft = numberAt(leftPart, leftPart.length() - 1 - i);
				}
				int numberRight = 0;
				if (rightPart.length() - 1 - i >= 0) {
					numberRight = numberAt(rightPart, rightPart.length() - 1 - i);
				}
				int numberCarry = numberAt(carry, carry.length() - 1 - i);
				int localResult = numberLeft + numberRight + numberCarry;

				//putting into addedResult
				String localResultString = String.valueOf(localResult);
				addedResult = setNumberAt(addedResult, addedResult.length() - 1 - i,
					numberAt(localResultString, localResultString.length() - 1));

				//carry if necessary
				if (localResultString.length() > 1) {
					carry = setNumberAt(carry, carry.length() - 2 - i,
						numberAt(localResultString, 0));
				}
			}

			result = addedResult;
		}

		//trim zeroes on left
		if (result.charAt(0) == '0' && result.length() > 1) {
			int zeroes = 0;
			for (int i = 1; i < result.length() - 1; i++) {		//-1 to leave 1 '0' if input is multiple zeroes or something
				if (result.charAt(i) == '0') zeroes++;
				else break;
			}
			result = result.substring(zeroes + 1);
		}

		return result;
	}

	public static void test(String input, int number) {
		System.out.println(input + " * " + number + " = " + recursiveMultiply(input, number));
	}

	private static int numberAt(String string, int index) {
		return Integer.parseInt(String.valueOf(string.charAt(index)));
	}

	//sets the number at index position in string to a new number
	private static String setNumberAt(String string, int index, int number) {
		String left = (index > 0) ? string.substring(0, index) : "";
		String right = (index < string.length() - 1) ? string.substring(index + 1) : "";
		return left + number + right;
	}
}
