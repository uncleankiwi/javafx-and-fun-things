package random_grapher;

public class Random {
	private final java.util.Random rand;

	public Random(long seed) {
		this.rand = new java.util.Random(seed);
	}

	//Modified makeshift Box-Muller Transform method to generate values that follow a Normal distribution.
	@SuppressWarnings("unused")
	public double normal() {
		double u = this.rand.nextDouble();
		double v = this.rand.nextDouble();
		return Math.sqrt(-2 * Math.log(u)) * Math.cos(2 * Math.PI * v);
	}

	//transforming a normal distribution and then cutting off anything beyond +-2
	public double normalTransformed(int min, int median, int max) {
		double d = normal();
		d += median;
		if (d > median) {
			
		}
		return 0;
	}

	@SuppressWarnings("unused")
	public int normalDiscrete(int min, int median, int max) {
		double d = normal();
		int result;
		if (d < 0) {
			result = median - (int) (-d / 2 * (median - min));
		}
		else {
			result = median + (int) (d / 2 * (max - median));
		}
		return result;
	}
}
