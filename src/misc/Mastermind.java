package misc;

import java.util.Arrays;
		import java.util.Random;
		import java.util.Scanner;

/* 4 different colours. Return number of exact hits and colour hits
 * menu options always available - 'q'uit returns main(), 'a' answer displays correct answer, 'n'ewgame starts over
 */
@SuppressWarnings("InfiniteLoopStatement")
public class Mastermind {
	public static int[] ans = new int[4];
	public static final int COLOUR_LOWER_BOUND = 1;
	public static final int COLOUR_UPPER_BOUND = 4;
	public static final int MAX_GUESSES = 10;
	public static int guesses = 0;
	public static final String QUIT = "q";
	public static final String ANS = "a";
	public static final String NEWGAME = "n";
	public static Random rand = new Random();
	public static Scanner scan = new Scanner(System.in);

	public static void main(String[] args) {
		newgame();

		while (true) {
			String input = scan.nextLine();
			input = input.trim();
			switch (input) {

				case ANS: {
					System.out.println("Answer: " + printAns());
					break;
				}
				case NEWGAME: {
					newgame();
					break;
				}
				case QUIT: {
					end();
					break;
				}
				default: {
					eval(input);
					break;
				}
			}

		}

	}

	private static void end() {
		System.out.println("Quitting...");
		scan.close();
		System.exit(0);
	}

	private static void eval(String input) {
		boolean correct = false;
		int RCRP = 0;
		int RC = 0;
		int[] attempt = new int[ans.length];
//		boolean[] isRCRP = new boolean[ans.length];
		//tallying non-RCRP colours for counting right colour.
		int[] attemptColourCount = new int[COLOUR_UPPER_BOUND - COLOUR_LOWER_BOUND + 1];
		//tallying non-RCRP colours in ans array
		int[] ansColourCount = new int[COLOUR_UPPER_BOUND - COLOUR_LOWER_BOUND + 1];
		Arrays.fill(attempt, -1);

		//parsing attempt
		for (int i = 0; i < ans.length; i++) {
			try {
				//may insert inappropriate integers (i.e. -2) into attempt, to be caught later
				attempt[i] = Integer.parseInt(input.substring(i, i + 1));
			}
			catch (Exception e) {
				//System.out.println("Could not parse input: " + e.getMessage());
			}
		}

		//marking attempt: right colour right placement
		for (int i = 0; i < ans.length; i++) {
			if (ans[i] == attempt[i]) {
				RCRP++;
//				isRCRP[i] = true;
			}
			else {
				if (attempt[i] >= COLOUR_LOWER_BOUND && attempt[i] <= COLOUR_UPPER_BOUND) {
					attemptColourCount[attempt[i] - COLOUR_LOWER_BOUND]++; //also tallying non RCRP colour count in attempt and ans
				}
				ansColourCount[ans[i] - COLOUR_LOWER_BOUND]++;
			}
		}

		//marking attempt: right colour only
		for (int i = 0; i < ansColourCount.length; i++) {
			RC += Math.min(attemptColourCount[i], ansColourCount[i]);
		}


		if (RCRP == ans.length) correct = true;

		//outcomes
		if (correct) {
			//win
			System.out.printf("You win! %s guesses used out of %s.%n", guesses, MAX_GUESSES);
			newgame();
		}
		else {
			if (guesses < MAX_GUESSES) {
				//wrong but not lost
				System.out.printf("Correct colour and placement: %s. Correct colour: %s. Remaining guesses: %s%n",
						RCRP, RC, MAX_GUESSES - guesses);
				guesses++;
			}
			else {
				//lose
				System.out.printf("You ran out of guesses! The correct answer was: %s%n", printAns());
				newgame();
			}

		}
	}


	private static String printAns() {
		StringBuilder msg = new StringBuilder();
		for (int an : ans) {
			msg.append(an).append(" ");
		}

		return msg.toString();
	}

	private static void newgame() {
		System.out.printf("New game started. Guess %s numbers ranging from %d to %d.\n"
						+ "Press %s to quit, %s to start a new game.%n",
				ans.length, COLOUR_LOWER_BOUND, COLOUR_UPPER_BOUND, QUIT, NEWGAME);
		for (int i = 0; i < ans.length; i++) {
			ans[i] = rand.nextInt(COLOUR_UPPER_BOUND - COLOUR_LOWER_BOUND + 1) + COLOUR_LOWER_BOUND;
		}

		//resetting guesses
		guesses = 0;
	}

}