package bot_facing;


/*
	A	B
	C	D

	right turn >:	C A D B
	C	A
	D	B

	left turn <:	B D A C
	B	D
	A	C

	backwards turn:	D C B A
	D	C
	B	A
 */
public class Bot {
	int[] facing;

	public void reset() {
		facing = new int[]{1, 1, 0, 0};
	}

	public int[] turn(char c) {
		int A = facing[0];
		int B = facing[1];
		int C = facing[2];
		int D = facing[3];
		switch (c) {
			case '^':
				//don't turn
				break;
			case '<':
				facing[0] = B;
				facing[1] = D;
				facing[2] = A;
				facing[3] = C;
				break;
			case '>':
				facing[0] = C;
				facing[1] = A;
				facing[2] = D;
				facing[3] = B;
				break;
			case 'v':
				facing[0] = D;
				facing[1] = C;
				facing[2] = B;
				facing[3] = A;
				break;
			default:
				throw new RuntimeException("Unrecognized bot instruction " + c);
		}
		return facing;
	}
}
