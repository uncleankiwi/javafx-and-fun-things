package misc.eda;

import java.util.Arrays;

/*
Takes a String containing a number of digits and passes it to SevenSegmentService.
Receives an array of String[] instructions, which are then used to flip the bits of a seven segment
display to display each digit of the number.

Each String[] instruction results in a boolean[] state change, which are then finally drawn as
a String[] display.

		a
		-
	f  | |  b
	g	-
	e  | |  c
	   	-
	   	d
 */
public class SevenSegmentClient {
	//whether any of the bits are on to begin with
	private static final boolean[] DEFAULT_STATE = initSevenSegmentDefaultState();

	public static void main(String[] args) {
		drawCharacters("284665");
	}

	public static void drawCharacters(String input) {
		String[][] instructions = SevenSegmentService.instructionsFor(input);

		boolean[] displayState = DEFAULT_STATE;

		String[] output = new String[displayState.length];
		for (String[] instruction : instructions) {
			displayState = getSevenSegmentNextState(displayState, instruction);
			String[] nextDisplay = drawSevenSegmentSingleCharacter(displayState);
			appendDisplay(output, nextDisplay);
		}

		for (String str : output) {
			System.out.println(str);
		}
	}

	//draws a single character given the state of each bit. Each row is a string.
	private static String[] drawSevenSegmentSingleCharacter(boolean[] state) {
		String[] output = new String[5];
		output[0] = " " + conditionalBitDraw(state[0], "-") + " ";
		output[1] = conditionalBitDraw(state[5], "|") + " " + conditionalBitDraw(state[1], "|");
		output[2] = " " + conditionalBitDraw(state[6], "-") + " ";
		output[3] = conditionalBitDraw(state[4], "|") + " " + conditionalBitDraw(state[2], "|");
		output[4] = " " + conditionalBitDraw(state[3], "-") + " ";
		return output;
	}

	//determine one display state from the previous.
	//should throw exceptions when trying to flip a bit on if it's already on, and vice versa.
	private static boolean[] getSevenSegmentNextState(boolean[] previousState, String[] singleInstruction) {
		//todo
	}

	//draws one bit using the given symbol if the bit is on. Inserts a space otherwise.
	private static String conditionalBitDraw(boolean bitState, String stringToDraw) {
		if (bitState) return stringToDraw;
		else return " ";
	}

	//concatenates the seven segment displays together into one giant String[] display
	private static void appendDisplay(String[] output, String[] nextDisplay) {
		for (int i = 0; i < output.length; i++) {
			output[i] += nextDisplay[i] + " ";
		}
	}

	private static boolean[] initSevenSegmentDefaultState() {
		boolean[] defaultState = new boolean[7];
		Arrays.fill(defaultState, false);
		return defaultState;
	}
}
