package bot_facing;

import java.util.Arrays;
import java.util.List;

/*
Suppose we have an agriculture bot standing in the middle of a garden plot.
When activated, it waters the half of the plot that it's currently facing.
			|
		w	|	w
		----x-----				x - bot
			|					w - quadrants receiving water
			|

The bot can be given a series of instructions that is some permutation of "^ > < v",
representing the directions up, right, left, and down respectively.

When it executes an "^" instruction, it waters the quadrants it's currently facing.
When it executes an ">", "<", or "v" instruction, it turns right/left/backwards relative to the direction
it was facing, and then waters the quadrants it is now facing.

There are 4! = 24 permutations of the instructions.
How many instruction sets out of these will cause at least 1 quadrant to receive no water?

=========================================

We will represent the direction the bot is facing (and will act on) as an 2D array written into a 1D array.
	i.e. The quadrants
	{{A,B},
	{C D}}
	are represented as
	{A B C D}

At the start of an instruction sequence, the bot faces {1 1 0 0} (North).
The plot object
Upon receiving an instruction, it turns (or not, if that instruction was "^") and a matrix transformation is
done on its facing.
Its new facing is then passed to the plot, which
	1) keeps track of the quadrants watered this turn and
	2) adds the facing matrix to the output that will determine how many times each quadrant was watered.
 */
public class Bootstrapper {
	@SuppressWarnings("SpellCheckingInspection")
	public static void main(String[] args) {
		Bot bot = new Bot();
		Plot plot = new Plot();
		List<char[]> instructions = InstructionFactory.getInstructions(9);
		for (char[] moveset : instructions) {
			bot.reset();
			plot.reset();

			for (int i = 0; i < moveset.length; i++) {
				plot.work(bot.turn(moveset[i]), i + 1);
			}
			System.out.println(Arrays.toString(moveset) + "\t\t--->\t\t" + Arrays.toString(plot.getQuadrants()));
			plot.print();
			System.out.println();

		}
	}
}

