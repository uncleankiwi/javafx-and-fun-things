package rps_sim;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;
import java.util.Set;

@SuppressWarnings("EnhancedSwitchMigration")
class Token extends ImageView {
	@SuppressWarnings("FieldCanBeLocal")
	private final double MOVE_DISTANCE = 5;
	private final Image ROCK_IMAGE;
	private final Image PAPER_IMAGE;
	private final Image SCISSORS_IMAGE;
	private final Arena arena;
	private final double xRange;
	private final double yRange;
	private final double WIDTH;
	private final double HEIGHT;
	Type type;
	Token target = null;

	Token(Image rockImage, Image paperImage, Image scissorsImage, Arena arena) {
		ROCK_IMAGE = rockImage;
		PAPER_IMAGE = paperImage;
		SCISSORS_IMAGE = scissorsImage;
		this.arena = arena;
		WIDTH = ROCK_IMAGE.getWidth();
		HEIGHT = ROCK_IMAGE.getHeight();
		xRange = Arena.WIDTH - WIDTH;
		yRange = Arena.HEIGHT - HEIGHT;
		this.minWidth(ROCK_IMAGE.getWidth());
		this.minHeight(ROCK_IMAGE.getHeight());
	}

	void randomPos() {
		Random random = new Random();
		this.setLayoutX(xRange * random.nextDouble());
		this.setLayoutY(yRange * random.nextDouble());
	}

	Double getRandomBearing() {
		return 2 * Math.PI * Math.random();
	}

	Double averageBearing(Double a, Double b) {
		if (a != null && b != null) {
			//lemma: if two angles are more than 180 degrees apart, then taking their average will
			//involve some 'wraparound' the 0 degree line. e.g. The average of 315 and 45 degrees is 0, not 180.
			//One way of doing this is by taking the average, and then subtracting or adding 180 degrees
			//(whichever doesn't make the result go out of the 0-360 range).
			boolean wraparound = Math.abs(a - b) > Math.PI;
			a += b;
			a /= 2;
			if (wraparound) {
				a = reverseBearing(a);
			}
			return a;
		}
		else if (a != null) {
			return a;
		}
		else {
			return b;
		}
	}

	//get the opposite direction
	Double reverseBearing(Double bearing) {
		if (bearing == null) {
			return null;
		}
		if (bearing > Math.PI) {
			bearing -= Math.PI;
		}
		else {
			bearing += Math.PI;
		}
		return bearing;
	}

	//find the nearest token that this token can be a target of, then reverses that bearing
	//i.e. this token wants to run away in this direction to avoid getting flipped
	Double getReversePredatorBearing() {
		return reverseBearing(getPredatorBearing());
	}

	Double getTargetBearing() {
		return getNearestBearing(getTargetSet());
	}

	Double getPredatorBearing() {
		return getNearestBearing(getPredatorSet());
	}

	//gets the bearing of the nearest token in the given set
	Double getNearestBearing(Set<Token> tokens) {
		Double bearing = null;
		//looking if there are tokens that are eligible targets
		if (tokens.size() != 0) {
			//looking up nearest of those targets
			Double greatestDistanceSquared = null;
			target = null;
			for (Token t : tokens) {
				double tDistanceSquared = Math.pow(t.getLayoutX() - getLayoutX(), 2) + Math.pow(t.getLayoutY() - getLayoutY(), 2);
				if (greatestDistanceSquared == null || tDistanceSquared < greatestDistanceSquared) {
					target = t;
					greatestDistanceSquared = tDistanceSquared;
				}
			}

			double x = target.getLayoutX() - getLayoutX();
			double y = target.getLayoutY() - getLayoutY();

			bearing = Math.atan2(y, x);
		}
		return bearing;
	}

	void doMove() {
		Double bearing = averageBearing(getRandomBearing(), getTargetBearing());

		if (bearing != null) {
			this.setLayoutX(setBounds(this.getLayoutX() + MOVE_DISTANCE * Math.cos(bearing), 0, Arena.WIDTH - WIDTH));
			this.setLayoutY(setBounds(this.getLayoutY() + MOVE_DISTANCE * Math.sin(bearing), 0, Arena.HEIGHT - HEIGHT));
		}
	}

	//flip any valid pieces that this token currently overlaps
	void doConsume() {
		if (target != null && isOverlapping(target)) {
			target.setType(type);
		}
	}

	boolean isOverlapping(Token other) {
		return (Math.abs(this.getLayoutX() - other.getLayoutX()) < WIDTH) && (Math.abs(this.getLayoutY() - other.getLayoutY()) < HEIGHT);
	}

	private Set<Token> getTargetSet() {
		Set<Token> targetSet;
		switch (type) {
			case ROCK:
				targetSet = arena.scissorsTokens;
				break;
			case PAPER:
				targetSet = arena.rockTokens;
				break;
			case SCISSORS:
				targetSet = arena.paperTokens;
				break;
			default:
				throw new RuntimeException("Unhandled token type " + type);
		}
		return targetSet;
	}

	private Set<Token> getPredatorSet() {
		Set<Token> predatorSet;
		switch (type) {
			case ROCK:
				predatorSet = arena.paperTokens;
				break;
			case PAPER:
				predatorSet = arena.scissorsTokens;
				break;
			case SCISSORS:
				predatorSet = arena.rockTokens;
				break;
			default:
				throw new RuntimeException("Unhandled token type " + type);
		}
		return predatorSet;
	}

	//ensure that the value k is such that min <= k <= max
	@SuppressWarnings("SameParameterValue")
	private double setBounds(double k, double min, double max) {
		k = Math.min(k, max);
		return Math.max(min, k);
	}

	void setType(Type t) {
		this.type = t;
		switch (t) {
			case ROCK:
				setImage(ROCK_IMAGE);
				arena.rockTokens.add(this);
				arena.paperTokens.remove(this);
				arena.scissorsTokens.remove(this);
				break;
			case PAPER:
				setImage(PAPER_IMAGE);
				arena.rockTokens.remove(this);
				arena.paperTokens.add(this);
				arena.scissorsTokens.remove(this);
				break;
			case SCISSORS:
				setImage(SCISSORS_IMAGE);
				arena.rockTokens.remove(this);
				arena.paperTokens.remove(this);
				arena.scissorsTokens.add(this);
				break;
			default:
				throw new RuntimeException("Unhandled token type " + t);
		}
	}
}
