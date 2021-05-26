package misc.eda;

/*
A sheet of material of length l (in cm) and thickness t (in mm) is rolled up around a tube with
outer diameter d (in cm).
Calculate the diameter (in cm) of the resulting roll given that the measurement is made
where the material is the thickest around the tube. Round to p significant places.
 */
public class RollDiameter {
	public static void main(String[] args) {
		test(0, 0.025, 4, 4);		//Expected answer: 4
		test(50, 0.025, 4, 4);		//Expected answer: 4.02
		test(4321, 0.025, 4, 4);	//Expected answer: 5.4575
		test(10000, 0.025, 4, 4);	//Expected answer: 6.9175
		test(123456, 0.025, 4, 4); 	//Expected answer: 20.2275
	}

	public static double calculateRollDiameter(double length, double thickness, double diameter, int places) {
		//resulting diameter has to be incremented twice per round; once when it reaches where the material starts,
		//and again when material reaches the opposite side
		boolean justIncrementedDiameter = true;
		//diameter not including outermost layer. used to calculate length consumed in outer-most layer
		double innerDiameter = diameter;
		//diameter including outermost layer
		double outerDiameter = diameter;
		while (length > 0) {
			//length -= half a round
			//0.5 * 2 * pi * r = pi * d / 2
			length -= Math.PI * innerDiameter / 2;
			outerDiameter += thickness / 10;
			if (!justIncrementedDiameter) {
				innerDiameter += 2 * thickness / 10;
				justIncrementedDiameter = true;
			}
			else justIncrementedDiameter = false;
		}

		return Double.parseDouble(String.format("%." + places + "f", outerDiameter)); //"%." + places + "f"
	}

	public static void test(double length, double thickness, double diameter, int places) {
		System.out.println("Length:" + length + " Thickness: " + thickness + " Tube diameter: " + diameter + " -> " +
			"Resulting diameter: " + calculateRollDiameter(length, thickness, diameter, places));
	}
}
