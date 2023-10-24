package rps_sim;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

import java.util.*;

@SuppressWarnings("FieldCanBeLocal")
class Arena extends Pane {
	private final Image ROCK_IMAGE;
	private final Image PAPER_IMAGE;
	private final Image SCISSORS_IMAGE;
	private static final int TOKEN_COUNT = 50;
	private final Set<Token> tokens;
	static final double WIDTH = 400;
	static final double HEIGHT = 600;
	final Set<Token> rockTokens;
	final Set<Token> paperTokens;
	final Set<Token> scissorsTokens;
	private boolean running;
	private Timer timer;
	private static final long DELAY = 300;

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
		rockTokens = new HashSet<>();
		paperTokens = new HashSet<>();
		scissorsTokens = new HashSet<>();
		running = false;
	}

	void init() {
		Type[] typeArr = Type.values();
		int index = 0;
		for (Token token : tokens) {
			token.randomPos();
			token.setType(typeArr[index]);

			if (index == typeArr.length - 1) {
				index = 0;
			}
			else {
				index++;
			}
		}
	}

	void startStop() {
		running = !running;
		if (running) {
			timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					for (Token t : tokens) {
						t.doMove();
						t.doConsume();
					}
				}
			};
			timer.scheduleAtFixedRate(task, DELAY, DELAY);
		}
		else {
			timer.cancel();
		}
	}

	private Image loadImage(String url) {
		return new Image(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(url)));
	}





	
}
