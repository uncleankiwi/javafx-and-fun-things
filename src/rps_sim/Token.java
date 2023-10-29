package rps_sim;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.Pair;

import java.util.Random;
import java.util.Set;

/*
Notes about the logic contained in this class.

Given:
	1. The node moves a set distance d each frame.
	2. The distance moved is in a direction that is the 'average' of all the bearings that matter, namely
		a. the bearing towards its nearest valid target
		b. a random bearing 360 degrees around itself
		c. the bearing away from its nearest 'predator' - a token that this token is a target of

If there are only 2 bearings (e.g. a and b), calculating the 'average' bearing is simply:
	1. determining if the 0 degree line lies between the two bearings - if it is, there will be 'wraparound'
		e.g. taking the average of 359 and 1 degrees is not simply (359 + 1)/2 = 180 degrees, but 0 degrees.
		Another way of looking at this is that if one angle is 0-180 degrees and the other is 180-360 degrees,
		there will be wraparound.
	2. take the average: c = (a + b)/2
	3. reverse the result direction if there is wraparound. Add or subtract 180 degrees such that the result is
		within the 0-360 degree range.

Things, however, are different if there are 3 bearings to take into consideration and each has its own weight.
	A different approach involving vectors is needed:
	1. calculate delta x and delta y for each bearing separately, factoring in distance d multiplied by bearing weight.
		Bearings that are null (because there are no valid targets, for example) will cause the remaining
		vectors to weigh more to keep distance travelled constant.
	2. add all delta x and delta y to get the resulting vector
	3. from that vector,

 */

@SuppressWarnings("EnhancedSwitchMigration")
class Token extends ImageView {
	@SuppressWarnings("FieldCanBeLocal")
	private final double MOVE_DISTANCE = 3;
	private final Image ROCK_IMAGE;
	private final Image PAPER_IMAGE;
	private final Image SCISSORS_IMAGE;
	private final Arena arena;
	private final double xRange;
	private final double yRange;
	private final double WIDTH;
	private final double HEIGHT;
	Type type;
	Token targetMemo = null;
	private final double TARGET_BEARING_WEIGHT = 4;
	private final double PREDATOR_BEARING_WEIGHT = 1;
	@SuppressWarnings("FieldCanBeLocal")
	private final double RANDOM_BEARING_WEIGHT = 3;

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

	private Token getNearestTarget() {
		Token t = getNearestToken(getTargetSet());
		targetMemo = t;
		return t;
	}

	private Token getNearestPredator() {
		return getNearestToken(getPredatorSet());
	}

	//gets the bearing of the nearest token in the given set
	private Token getNearestToken(Set<Token> tokens) {
		Token token = null;
		if (tokens.size() != 0) {
			Double greatestDistanceSquared = null;
			for (Token t : tokens) {
				double tDistanceSquared = Math.pow(t.getLayoutX() - getLayoutX(), 2) + Math.pow(t.getLayoutY() - getLayoutY(), 2);
				if (greatestDistanceSquared == null || tDistanceSquared < greatestDistanceSquared) {
					token = t;
					greatestDistanceSquared = tDistanceSquared;
				}
			}
		}
		return token;
	}

	private double calcWeight(double weight, double otherWeight, Token otherToken) {
		if (otherToken == null) {
			return weight / (weight + RANDOM_BEARING_WEIGHT);
		}
		else {
			return weight / (weight + otherWeight + RANDOM_BEARING_WEIGHT);
		}
	}


	/*
		We want to find vector components a and b such that:
		a^2 + b^2 = m^2
		and
		a / b = x / y

	Where m is the magnitude, and x and y are the target token's coordinates.
	Distance d doesn't factor in until all the vectors have been summed.

	This method is used twice: once when getting vectors from the target and predator
	(m = weight in this case) and again when calculating the net movement
	of the token (m = distance d).
	 */
	private Pair<Double> scaleVector(double x, double y, double m) {
		Pair<Double> result = new Pair<>(0d,0d);
		//checking to see if x or y are 0
		if (x == 0) {
			result.y = y > 0 ? m : -m;
		}
		else if (y == 0) {
			result.x = x > 0 ? m : -m;
		}
		else if (m != 0){
			//solving the two equations in the comments above
			result.x = Math.sqrt(
					Math.pow(m, 2) /
							(1 + Math.pow(y / x, 2))
			);
			if (x < 0) {
				result.x *= -1;
			}
			result.y = result.x * y / x;
		}
		return result;
	}

	/*
	A general use method for calculating the vectors for moving towards target and away from predator.
	The vector for predator will take this result and reverse it.
	 */
	private Pair<Double> getTokenVector(Token token, Token otherToken, double weight, double otherWeight) {
		Pair<Double> result = new Pair<>(0d,0d);
		if (token != null && weight != 0) {
			double w = calcWeight(weight, otherWeight, otherToken);
			double x = token.getLayoutX() - this.getLayoutX();
			double y = token.getLayoutY() - this.getLayoutY();

			result = scaleVector(x, y, w);
		}
		return result;
	}

	private Pair<Double> getTargetVector(Token target, Token predator) {
			return getTokenVector(target, predator, TARGET_BEARING_WEIGHT, PREDATOR_BEARING_WEIGHT);
		}

		private Pair<Double> getPredatorVector(Token target, Token predator) {
			Pair<Double> v = getTokenVector(predator, target, PREDATOR_BEARING_WEIGHT, TARGET_BEARING_WEIGHT);
			v.x = -v.x;
			v.y = -v.y;
			return v;
		}

		@SuppressWarnings("ConstantValue")
		private Pair<Double>getRandomVector(Token target, Token predator) {
			Pair<Double> result = new Pair<>(0d, 0d);
			if (RANDOM_BEARING_WEIGHT == 0) {
				return result;
			}
			double w = RANDOM_BEARING_WEIGHT / ( (target != null ? TARGET_BEARING_WEIGHT : 0) +
					(predator != null ? PREDATOR_BEARING_WEIGHT : 0) +
					RANDOM_BEARING_WEIGHT);
			double randomAngle = 2 * Math.PI * Math.random();

			result.x = w * Math.sin(randomAngle);
			result.y = w * Math.cos(randomAngle);
			return result;
		}

		void doMove() {
			Token target = getNearestTarget();
			Token predator = getNearestPredator();
			Pair<Double> targetVector = getTargetVector(target, predator);
			Pair<Double> predatorVector = getPredatorVector(target, predator);
			Pair<Double> randomVector = getRandomVector(target, predator);
		double deltaX = targetVector.x + predatorVector.x + randomVector.x;
		double deltaY = targetVector.y + predatorVector.y + randomVector.y;
		Pair<Double> netMovement = scaleVector(deltaX, deltaY, MOVE_DISTANCE);

		this.setLayoutX(setBounds(this.getLayoutX() + netMovement.x, 0, Arena.WIDTH - WIDTH));
		this.setLayoutY(setBounds(this.getLayoutY() + netMovement.y, 0, Arena.HEIGHT - HEIGHT));
	}

	//flip any valid pieces that this token currently overlaps
	void doConsume() {
		if (targetMemo != null && isOverlapping(targetMemo)) {
			targetMemo.setType(type);
		}
	}

	private boolean isOverlapping(Token other) {
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
