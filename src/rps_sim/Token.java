package rps_sim;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Random;

class Token extends ImageView {
	private final Image ROCK_IMAGE;
	private final Image PAPER_IMAGE;
	private final Image SCISSORS_IMAGE;
	private final Arena arena;
	private final double xRange;
	private final double yRange;
	Type type;

	Token(Image rockImage, Image paperImage, Image scissorsImage, Arena arena) {
		ROCK_IMAGE = rockImage;
		PAPER_IMAGE = paperImage;
		SCISSORS_IMAGE = scissorsImage;
		this.arena = arena;
		xRange = Arena.WIDTH - ROCK_IMAGE.getWidth();
		yRange = Arena.HEIGHT - ROCK_IMAGE.getHeight();
		this.minWidth(ROCK_IMAGE.getWidth());
		this.minHeight(ROCK_IMAGE.getHeight());
	}

	void randomPos() {
		Random random = new Random();
		this.setX(xRange * random.nextDouble());
		this.setY(yRange * random.nextDouble());
	}

	void doMove() {

	}

	void setScissors() {
		setImage(SCISSORS_IMAGE);
	}

	void setPaper() {
		setImage(PAPER_IMAGE);
	}

	void setRock() {
		setImage(ROCK_IMAGE);
	}
}
