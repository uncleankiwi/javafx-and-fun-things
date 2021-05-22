package misc;

/*
convert a "hh:mm" string into
1. ascii image of a digital clock representation of that time
2. another ascii image, but every bit/pixel representing a number should display that number
3. a bitmap string - representation #1 converted into a single line
4. a bitmap string - representation #2 converted into a single line

extra feature: if it encounters a character it can't recognise, insert a 3x5 block of 0s or ?s.
*/

public class TimeToBitmap {
	private static final int UNSUPPORTED_CHAR = -2;
	private static final int SPACE = -1;

		public static void main(String[] args) {
		System.out.println(toNumberedBitString("18:56", false, false));
		System.out.println(toNumberedBitString("18:56", true, false));
		System.out.println(toNumberedBitString("18:56", false, true));
		System.out.println(toNumberedBitString("18:56", true, true));
	}

	public static String toNumberedBitString(String time, boolean drawWithNumber, boolean asSingleLine) {
		String[] outputArr = new String[5];
		char[] timeArr = time.toCharArray();

		//for every character in input string...
		for (int i = 0; i < timeArr.length; i++) {
			int number = 0;
			//if it's a colon
			if (timeArr[i] == ':') {
				number = 10;
			}
			else {
				try {
					//if it's a number
					number = Integer.parseInt(String.valueOf(timeArr[i]));

				}
				catch (NumberFormatException e) {
					//if it's an unrecognisable character
					number = UNSUPPORTED_CHAR;
				}
			}

			//write to output
			appendOutput(outputArr, number, drawWithNumber);

			//and then add a space
			if (i < timeArr.length - 1) appendOutput(outputArr, SPACE, drawWithNumber);
		}

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < outputArr.length; i++) {
			output.append(outputArr[i]);
			//add a new line if output is not a single line
			if ((i < outputArr.length - 1) && !asSingleLine) output.append("\n");
		}

		return output.toString();
	}

	private static void appendOutput(String[] outputArr, int number, boolean drawWithNumber) {
		for (int i = 0; i < BITMAP_HEIGHT; i++) {
			outputArr[i] += cArr[number][i];
		}
	}


	private static final char[][][] cArr = {
			{
					{1, 1, 1},	//0
					{1, 0, 1},
					{1, 0, 1},
					{1, 0, 1},
					{1, 1, 1}
			},
			{
					{0, 1, 0},	//1
					{1, 1, 0},
					{0, 1, 0},
					{0, 1, 0},
					{1, 1, 1}
			},
			{
					{1, 1, 1},	//2
					{0, 0, 1},
					{1, 1, 1},
					{1, 0, 0},
					{1, 1, 1}
			},
			{
					{1, 1, 1},	//3
					{0, 0, 1},
					{1, 1, 1},
					{0, 0, 1},
					{1, 1, 1}
			},
			{
					{1, 0, 1},	//4
					{1, 0, 1},
					{1, 1, 1},
					{0, 0, 1},
					{0, 0, 1}
			},
			{
					{1, 1, 1},	//5
					{1, 0, 0},
					{1, 1, 1},
					{0, 0, 1},
					{1, 1, 1}
			},
			{
					{1, 1, 1},	//6
					{1, 0, 0},
					{1, 1, 1},
					{1, 0, 1},
					{1, 1, 1}
			},
			{
					{1, 1, 1},	//7
					{0, 0, 1},
					{0, 0, 1},
					{0, 0, 1},
					{0, 0, 1}
			},
			{
					{1, 1, 1},	//8
					{1, 0, 1},
					{1, 1, 1},
					{1, 0, 1},
					{1, 1, 1}
			},
			{
					{1, 1, 1},	//9
					{1, 0, 1},
					{1, 1, 1},
					{0, 0, 1},
					{0, 0, 1}
			},
			{
					{0},		//:
					{1},
					{0},
					{1},
					{0}
			}
	};

	private static final int BITMAP_HEIGHT = cArr[0][0].length;
}
