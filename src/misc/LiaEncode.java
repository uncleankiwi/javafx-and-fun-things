package misc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
* Converts alphabetical characters in a string into numbers, with 'A' and 'a' both being 0, and 'Z' and 'z' being 25.
* Decoding it reverses the process, but will output all possible strings.
* 	This does not output capital letters or numbers.
* 	E.g. "11" can be decoded as 'aa' and 'k'.
 */
public class LiaEncode {
	private static final int DEFAULT_OUTPUT_LIMIT = 10;

	public static void main(String[] args) {
		testEncode("Happy Birthday to You!");
		testEncode("bbbb");
		testEncode("ll");
		System.out.println("--------------");
		testDecode("70151524 18171973024 1914 241420!");
		testDecode("1111");
		testDecode("1111", 5);
		testDecode("1111", 3);
		testDecode("1111", 1);
		testDecode("1111", 0);
	}

	private static void testEncode(String input) {
		System.out.println("Encoding " + input + ":");
		System.out.println("\t" + encode(input) + "\n");
	}

	private static void testDecode(String input, int outputLimit) {
		System.out.println("Decoding " + input + ":");
		Set<String> output = decode(input, outputLimit);
		for (String s : output) {
			System.out.println("\t" + s);
		}
		System.out.println();
	}

	private static void testDecode(String input) {
		testDecode(input, DEFAULT_OUTPUT_LIMIT);
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
		outputs.add(new PossibleOutput(input, outputs, outputLimit));
		boolean done = false;
		while (!done) {
			done = true;
			for (PossibleOutput output : outputs) {
				if (!output.step()) done = false;
			}
		}

		Set<String> stringOutputs = new HashSet<>();
		for (PossibleOutput output : outputs) {
			stringOutputs.add(output.getOutput());
		}
		return stringOutputs;
	}

	static class PossibleOutput {
		private String output;
		private String input;
		private final Set<PossibleOutput> outputs;
		private final int outputLimit;

		PossibleOutput(String input, Set<PossibleOutput> outputs, int outputLimit, String output) {
			this.output = output;
			this.input = input;
			this.outputLimit = outputLimit;
			this.outputs = outputs;
		}

		PossibleOutput(String input, Set<PossibleOutput> outputs, int outputLimit) {
			this(input, outputs, outputLimit, "");
		}

		String getOutput() {
			return this.output;
		}

		public boolean step() {
			if (input.length() == 0) return true;

			//try to parse the first two characters as a pair
			if (outputs.size() < outputLimit) {
				if (input.length() >= 2) {
					int twoDigits;
					try {
						twoDigits = Integer.parseInt(input.substring(0, 2));
						if (twoDigits >= 0 && twoDigits <= 25) {
							String remainder = input.substring(2);
							String alternateOutput = output + (char) twoDigits;
							outputs.add(new PossibleOutput(remainder, outputs, outputLimit, alternateOutput));
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
				output += (char) oneDigit;
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
