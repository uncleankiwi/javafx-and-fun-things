package rps_sim;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("FieldCanBeLocal")
class Arena extends Pane {
	private final Image ROCK_IMAGE;
	private final Image PAPER_IMAGE;
	private final Image SCISSORS_IMAGE;
	private static final int TOKEN_COUNT = 50;
	private final Set<Token> tokens;
	static final double WIDTH = 400;
	static final double HEIGHT = 600;

	Arena() {
		ROCK_IMAGE = loadImage("rps_sim/rock.png");
		PAPER_IMAGE = loadImage("rps_sim/paper.png");
		SCISSORS_IMAGE = loadImage("rps_sim/scissors.png");
		tokens = new HashSet<>();
		for (int i = 0; i < TOKEN_COUNT; i++) {
			Token t = new Token(ROCK_IMAGE, PAPER_IMAGE, SCISSORS_IMAGE, this);
			tokens.add(t);
			getChildren().add(t);
		}
		setMinWidth(WIDTH);
		setMinHeight(HEIGHT);
	}

	void init() {
		int type = 1;
		for (Token token : tokens) {
			token.randomPos();

			switch (type) {
				case 1:
					token.setRock();
					break;
				case 2:
					token.setPaper();
					break;
				default:
					token.setScissors();
			}

			if (type == 3) {
				type = 1;
			}
			else {
				type++;
			}
		}


	}

	private Image loadImage(String url) {
		return new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(url)));
	}





	
}
