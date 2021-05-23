package misc;

/*
given a starting square, an ending square, and a max number of moves,
find out if it's possible for a bishop piece to move from start to end in that number of moves.

chess notation:
	a4 - column/file a, row/rank 4

expected output:
	canBishopMove("a1", "b4", 2) -> true
	canBishopMove("a1", "b5", 5) -> false
	canBishopMove("f1", "f1", 0) -> true
*/


public class BishopMoves {
	public static void main(String[] args) {
		System.out.println(canBishopMove("a1", "b4", 2));
		System.out.println(canBishopMove("a1", "b5", 5));
		System.out.println(canBishopMove("f1", "f1", 0));
	}

	public static boolean canBishopMove(String start, String end, int maxMoves) {
		//parsing inputs
		int x1 = parseRank(start);
		int y1 = parseFile(start);
		int x2 = parseRank(end);
		int y2 = parseFile(end);

		int[][] board = new int[8][8];

		board[x1][y1] = 1;

		return board[x2][y2] == 1;
	}

	//two methods to convert rank and file into 0 - 8 array coordinates
	private static int parseRank(String s) {
		if (s.length() != 2) throw new RuntimeException("Error parsing square " + s + ".");
		char rank = s.charAt(1);
		//check if rank is a number from 1 to 8: ASCII 49 ~ 56
		if (rank < 49 || rank > 56) throw new RuntimeException("Error parsing rank " + rank + " of square " + s + ".");
		return rank - '1';
	}

	private static int parseFile(String s) {
		if (s.length() != 2) throw new RuntimeException("Error parsing square " + s + ".");
		char file = s.charAt(0);
		//check if rank has a lower than expected ASCII code than 'a'.
		//if so, it might be upper case, so try to convert to lower case by adding 32
		if (file < 97) file += 32;
		if (file < 97 || file > 104) throw new RuntimeException("Error parsing file " + file + " of square " + s + ".");
		return file - 'a';
	}

}
