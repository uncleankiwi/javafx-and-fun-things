package misc.eda;

/*
Given h number of holes in a centrifuge, return if it is possible to place t
tubes in it such that the centrifuge is balanced.

Assume the centrifuge can never be balanced if there is only 1 tube or 1 hole.
 */
public class CentrifugeBalancer {
	public static void main(String[] args) {
		System.out.println("Holes\tTubes");
		System.out.println("=============");
		test(6, 4);	//true
		test(2,1);	//false
		test(3,3);	//true
		test(5,1);	//false
		test(12,7);	//true
		test(1,1);	//false
		test(21,18);	//true
		test(1,0);	//false
		test(4,2);	//true
		test(5,3);	//false
		test(21,13);	//false
		test(3,3);	//true
		test(50,1);	//false
		test(8,6);	//true
		test(9,5);	//false
		test(2,1);	//false
		test(59,59);	//true
		test(11,4);	//false

	}

	private static void test(int h, int t) {
		System.out.println(h + "\t" + t + "\t->\t" + tryPlace(h, t));
	}

	public static boolean tryPlace(int h, int t) {
		if (h <= 1) return false;
		else if (t <= 1) return false;
		else {
			return true;	//todo
		}
	}
}
