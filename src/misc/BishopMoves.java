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


import java.util.ArrayList;
import java.util.List;

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
		int moves = 0;

		//for every move
		while (moves < maxMoves) {
			//get lists of coordinates of non-propagated squares (1)
			//also check if any of them are the end square
			//if there are no non-propagated squares, quit.
			List<Integer> xArr = new ArrayList<>();
			List<Integer> yArr = new ArrayList<>();
			boolean hasNonPropagatedSquares = false;
			for (int x = 0; x < 8; x++) {
				for (int y = 0; y < 8; y++) {
					if (board[x][y] == 1) {
						hasNonPropagatedSquares = true;
						xArr.add(x);
						yArr.add(y);
						if (x == x2 && y == y2) return true;
					}
				}
			}
			if (!hasNonPropagatedSquares) break;

			//propagate from the lists by marking as (1)
			moves++;

			//mark the squares in the lists as propagated (2)
			for (int i = 0; i < xArr.size(); i++) {
				board[xArr.get(i)][yArr.get(i)] = 2;
			}
		}



		return false;
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
