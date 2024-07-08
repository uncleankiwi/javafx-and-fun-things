package bot_facing;

public class Plot {
	private int[] quadrants;
	StringBuilder stringBuilder1;
	StringBuilder stringBuilder2;


	public void reset() {
		quadrants = new int[]{0, 0, 0, 0};
		stringBuilder1 = new StringBuilder();
		stringBuilder2 = new StringBuilder();
	}

	public void work(int[] facing, int moveNumber) {
		//print out which quadrants were worked on this turn
		appendQuadrant(facing[0] * moveNumber, facing[1] * moveNumber,
				facing[2] * moveNumber, facing[3] * moveNumber);
		//keep track of how many times each quadrant was worked on
		for (int i = 0; i < quadrants.length; i++) {
			quadrants[i] = quadrants[i] + facing[i];
		}
	}

	public int[] getQuadrants() {
		appendQuadrant(quadrants[0], quadrants[1], quadrants[2], quadrants[3]);
		return quadrants;
	}

	public void print() {
		System.out.println(stringBuilder1);
		System.out.println(stringBuilder2);
	}

	private void appendQuadrant(int a, int b, int c, int d) {
		stringBuilder1.append(a).append("\t").append(b).append("\t\t\t");
		stringBuilder2.append(c).append("\t").append(d).append("\t\t\t");
	}
}
