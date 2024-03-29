package misc;

import java.util.*;

/*
* Converts alphabetical characters in a string into numbers, with 'A' and 'a' both being 0, and 'Z' and 'z' being 25.
* Decoding it reverses the process, but will output all possible strings.
* 	This does not output capital letters or numbers.
* 	E.g. "11" can be decoded as 'aa' and 'k'.
 */
public class LiaEncode {
	private static final int DEFAULT_OUTPUT_LIMIT = 10;

	public static void main(String[] args) {
		testEncode("Labore et dolore magna aliqua.");
		testEncode("bbbb");
		testEncode("ll");
		testEncode("!!some_tag??");
		System.out.println("--------------");
		testDecode("1111");
		testDecode("1111", 5);
		testDecode("1111", 3);
		testDecode("1111", 1);
		testDecode("1111", 0);
		System.out.println("--------------");
		testSplittingDecode("110114174 419 3141114174 1206130 011816200.", 20);
		testSplittingDecode("...", 3);
		testSplittingDecode("!!1814124_1906??", 20);
	}

	private static void testEncode(String input) {
		System.out.println("Encoding " + input + ":");
		System.out.println("\t>" + encode(input) + "\n");
	}

	private static void testDecode(String input, int outputLimit) {
		System.out.println("Decoding " + input + ":");
		Set<String> output = decode(input, outputLimit);
		for (String s : output) {
			System.out.println("\t>" + s);
		}
		System.out.println();
	}

	@SuppressWarnings("SameParameterValue")
	private static void testDecode(String input) {
		testDecode(input, DEFAULT_OUTPUT_LIMIT);
	}

	private static void testSplittingDecode(String input, int outputLimit) {
		System.out.println("Decoding " + input + ":");
		List<Set<String>> output = splittingDecode(input, outputLimit);
		for (Set<String> set : output) {
			StringBuilder builder = new StringBuilder();
			boolean first = true;
			for (String str : set) {
				if (first) first = false;
				else builder.append(" ");
				builder.append(str);
			}
			System.out.println("\t>" + builder);
		}
		System.out.println();
	}

	public static String encode(String input) {
		char[] arr = input.toCharArray();
		StringBuilder stringBuilder = new StringBuilder();
		for (char c : arr) {
			if (c >= 'A' && c <= 'Z') {
				stringBuilder.append(c - 'A');
			}
			else if (c >= 'a' && c <= 'z') {
				stringBuilder.append(c - 'a');
			}
			else {
				stringBuilder.append(c);
			}
		}
		return stringBuilder.toString();
	}

	@SuppressWarnings("unused")
	public static Set<String> decode(String input) {
		return decode(input, DEFAULT_OUTPUT_LIMIT);
	}

	//outputLimit caps the number of possible outputs it will collect.
	//Possible outputs beyond that count are simply discarded.
	public static Set<String> decode(String input, int outputLimit) {
		Set<PossibleOutput> outputs = new HashSet<>();
		Set<PossibleOutput> newOutputs = new HashSet<>();
		outputs.add(new PossibleOutput(input, outputs, newOutputs, outputLimit));
		boolean done = false;
		while (!done) {
			done = true;
			for (PossibleOutput output : outputs) {
				if (!output.step()) done = false;
			}
			outputs.addAll(newOutputs);
			newOutputs.clear();
		}

		Set<String> stringOutputs = new HashSet<>();
		for (PossibleOutput output : outputs) {
			stringOutputs.add(output.getOutput());
		}
		return stringOutputs;
	}

	/*
	 * Sees every contiguous group of digits as a 'word'.
	 * Splits each word, decodes them separately, then returns one set of possibilities per word, all contained
	 * in a parent list.
	 * Non-numerical characters are seen as a word. Every such character will have a set to itself.
	 */
	public static List<Set<String>> splittingDecode(String input, int outputLimit) {
		List<Set<String>> output = new ArrayList<>();
		char[] inputArr = input.toCharArray();
		int left = -1;
//		int right = -1;
		for (int i = 0; i < inputArr.length; i++) {
			char c = inputArr[i];

			if (!(c >= '0' && c <= '9')) {
				//this character is not a digit. decode previous word if any and current character.
				if (left >= 0) {
					output.add(decode(input.substring(left, i), outputLimit));
				}
				output.add(decode(input.substring(i, i + 1), outputLimit));
				left = -1;
			}
			else if (i + 1 >= input.length()) {
				//wrap up because end of string reached
				if (left < 0) left = i;
				output.add(decode(input.substring(left, i + 1), outputLimit));
			}
			else {
				if (left < 0) left = i;
			}
		}

		return output;
	}

	static class PossibleOutput {
		private String output;
		private String input;
		private final Set<PossibleOutput> outputs;
		private final Set<PossibleOutput> newOutputs;
		private final int outputLimit;

		PossibleOutput(String input, Set<PossibleOutput> outputs, Set<PossibleOutput> newOutputs, int outputLimit, String output) {
			this.newOutputs = newOutputs;
			this.output = output;
			this.input = input;
			this.outputLimit = outputLimit;
			this.outputs = outputs;
		}

		PossibleOutput(String input, Set<PossibleOutput> outputs, Set<PossibleOutput> newOutputs, int outputLimit) {
			this(input, outputs, newOutputs, outputLimit, "");
		}

		String getOutput() {
			return this.output;
		}

		public boolean step() {
			if (input.length() == 0) return true;

			//try to parse the first two characters as a pair
			if (outputs.size() + newOutputs.size() < outputLimit) {
				if (input.length() >= 2) {
					int twoDigits;
					try {
						twoDigits = Integer.parseInt(input.substring(0, 2));
						if (twoDigits >= 0 && twoDigits <= 25) {
							String remainder = input.substring(2);
							String alternateOutput = output + (char) (twoDigits + (int)'a');
							newOutputs.add(new PossibleOutput(remainder, outputs, newOutputs, outputLimit, alternateOutput));
						}
					}
					catch (NumberFormatException ignored) {}
				}
			}

			//now parse the first character by itself
			int oneDigit;
			String oneLetter = input.substring(0, 1);
			try {
				oneDigit = Integer.parseInt(oneLetter);
				output += (char) (oneDigit + (int)'a');
			}
			catch (NumberFormatException ignored) {
				output += oneLetter;
			}
			if (input.length() >= 1) input = input.substring(1);
			return false;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			PossibleOutput that = (PossibleOutput) o;
			return getOutput().equals(that.getOutput());
		}

		@Override
		public int hashCode() {
			return Objects.hash(getOutput());
		}
	}
}
