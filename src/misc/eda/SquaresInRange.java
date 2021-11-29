package misc.eda;

import util.Stopwatch;

//how many square integers are within a given range
public class SquaresInRange {
	public static void main(String[] args) {
		test(3, 9);			//2
		test(17, 24);		//0
		test(1, 1000000000);	//31622
		test(100, 1000);		//22
		test(433, 100000);	//296
	}

	private static void test(int a, int b) {
		Stopwatch stopwatch = new Stopwatch("Range [" + a + "," + b + "]");
		System.out.println("Squares in range: " + squares(a, b));
		stopwatch.stop();
	}

	public static int squares(int a, int b) {
		//have to add 1 if the smaller of the two integers is a square
		int smaller = Math.min(a, b);
		int smallerSqrtFloor = (int) Math.floor(Math.sqrt(smaller));
		int smallerSquareOffset = smallerSqrtFloor * smallerSqrtFloor == smaller ? 1 : 0;
		return Math.abs((int) Math.sqrt(a) - (int) Math.sqrt(b)) + smallerSquareOffset;
	}
}
