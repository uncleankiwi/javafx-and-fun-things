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

	public enum DrawMode {
		BIT,
		NUMBER,
		SQUARE
	}

	public static void main(String[] args) {

		System.out.println(toNumberedBitString("18:56", DrawMode.BIT, false));
		System.out.println(toNumberedBitString("18:56", DrawMode.NUMBER, false));
		System.out.println(toNumberedBitString("18:56", DrawMode.SQUARE, false));

		System.out.println("\nSingle line outputs:");

		System.out.println(toNumberedBitString("18:56", DrawMode.BIT, true));
		System.out.println(toNumberedBitString("18:56", DrawMode.NUMBER, true));
		System.out.println(toNumberedBitString("18:56", DrawMode.SQUARE, true));

		System.out.println("\nInvalid characters:");

		System.out.println(toNumberedBitString("1s2 4", DrawMode.BIT, false));
		System.out.println(toNumberedBitString("1s2 4", DrawMode.NUMBER, false));
		System.out.println(toNumberedBitString("1s2 4", DrawMode.SQUARE, false));

		System.out.println("\nBunch of numbers:");

		System.out.println(toNumberedBitString("1234567890", DrawMode.BIT, false));
		System.out.println(toNumberedBitString("1234567890", DrawMode.NUMBER, false));
		System.out.println(toNumberedBitString("1234567890", DrawMode.SQUARE, false));
	}

	public static String toNumberedBitString(String time, DrawMode drawMode, boolean asSingleLine) {
		String[] outputArr = {"", "", "", "", ""};
		char[] timeArr = time.toCharArray();

		//for every character in input string...
		for (int i = 0; i < timeArr.length; i++) {
			int number;
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
			appendOutput(outputArr, number, drawMode);

			//and then add a space
			if (i < timeArr.length - 1) appendOutput(outputArr, SPACE, drawMode);
		}

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < outputArr.length; i++) {
			output.append(outputArr[i]);
			//add a new line if output is not a single line
			if ((i < outputArr.length - 1) && !asSingleLine) output.append("\n");
		}

		return output.toString();
	}

	private static void appendOutput(String[] outputArr, int number, DrawMode drawMode) {
		final int CHAR_WIDTH = 3;
		for (int i = 0; i < BITMAP_HEIGHT; i++) {
			if (number == SPACE) {
				switch (drawMode) {
					case BIT: {
						outputArr[i] += 0;
						break;
					}
					case NUMBER:
					case SQUARE: {
						outputArr[i] += " ";
						break;
					}
				}
			}
			else if (number == UNSUPPORTED_CHAR) {
				for (int j = 0; j < CHAR_WIDTH; j++) {
					switch (drawMode) {
						case BIT: {
							outputArr[i] += 0;
							break;
						}
						case NUMBER:
						case SQUARE: {
							outputArr[i] += "?";
							break;
						}
					}
				}
			}
			else {
				for (int j = 0; j < cArr[number][i].length; j++) {
					switch (drawMode) {
						case BIT: {
							outputArr[i] += cArr[number][i][j];
							break;
						}
						case NUMBER: {
							//substitute number for a supported character if number is > 9
							String s = (number <= 9) ? String.valueOf(number) : supportedChars[number - 10];

							outputArr[i] += cArr[number][i][j] == 1 ? s : " ";
							break;
						}
						case SQUARE: {
							outputArr[i] += cArr[number][i][j] == 1 ? "■" : " ";
							break;
						}
					}
				}
			}
		}
	}

	private static final String[] supportedChars = {":"};

	private static final int[][][] cArr = {
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

	private static final int BITMAP_HEIGHT = cArr[0].length;
}
