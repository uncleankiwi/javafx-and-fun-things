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
			getSevenSegmentNextState(displayState, instruction);
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
	//matches lower case instructions to ASCII characters 97 - 103
	//todo need to return state?
	private static void getSevenSegmentNextState(boolean[] previousState, String[] singleInstruction) {
		for (String bitStr : singleInstruction) {
			//getting which bit to flip. 'a' to 'g' correspond to 97 - 103
			char instructionLowerCaseChar = bitStr.toLowerCase().toCharArray()[0];
			int bitIndex = instructionLowerCaseChar - 'a';

			//getting whether to turn it on or off
			char instructionChar = bitStr.toCharArray()[0];
			boolean turnOn = (instructionLowerCaseChar == instructionChar);

			//throw exceptions if turning a bit on when it's already on, and vice versa
			if (previousState[bitIndex] == turnOn) {
				throw new InvalidBitOperation(bitIndex, turnOn);
			}

			previousState[bitIndex] = turnOn;
		}
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

	public static class InvalidBitOperation extends RuntimeException {
		InvalidBitOperation(int bitIndex, boolean on) {
			super("Cannot flip bit " + bitIndex + " as it is already " + (on ? "on" : "off"));
		}
	}
}
