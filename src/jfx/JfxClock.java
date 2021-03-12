package jfx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Calendar;

public class JfxClock extends Application {
	@Override
	public void start(Stage primaryStage) {
		ClockWrapper clockwrapper = new ClockWrapper();
		Scene scene = new Scene(clockwrapper, 300, 325);
		primaryStage.setScene(scene);
		primaryStage.setTitle("A3Q1");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}

class ClockWrapper extends VBox {
	public AnalogPane analog = new AnalogPane();
	public DigitalPane digital = new DigitalPane();
	ClockWrapper(){
		this.setStyle("-fx-background-color: #eeeeee");
		this.getChildren().addAll(analog, digital);
	}
}

abstract class Clock extends Pane {
	protected Calendar cal;
	protected double hour;
	protected double minute;
	protected double second;
	protected double millisecond;
	private final KeyFrame timerKeyframe = new KeyFrame(Duration.millis(100), event -> updateClock());
	protected Timeline timer = new Timeline(timerKeyframe);

	Clock(){
		timer.setCycleCount(Timeline.INDEFINITE);
		timer.play();
	}

	abstract public void updateClock();
}

class DigitalPane extends Clock {
	private final Text digitalTime;

	DigitalPane(){
		digitalTime = new Text(0, 25, "");
		digitalTime.setFont(Font.font(14));
		updateClock();
		digitalTime.setX((300 - digitalTime.getBoundsInLocal().getWidth()) / 2);
		this.getChildren().add(digitalTime);
	}

	@Override
	public void updateClock() {
		cal = Calendar.getInstance();
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		millisecond = cal.get(Calendar.MILLISECOND);
		second = cal.get(Calendar.SECOND);

		digitalTime.setText(String.format("%02.0f", hour) + ":" +
				String.format("%02.0f", minute) + ":" +
				String.format("%02.0f", second));
	}
}

class AnalogPane extends Clock {
	private static final int CLOCK_RADIUS = 125;
	private static final int SMALL_MARKING = 7;
	private static final int LARGE_MARKING = 14;
	private static final int NUMBERING = 24;
	private static final int HOUR_HAND = 60; //green
	private static final int MINUTE_HAND = 80; //blue
	private static final int SECOND_HAND = 100; //red
	private static final int CLOCK_X = 150;
	private static final int CLOCK_Y = 150;
	private final Line hourhand;
	private final Line minutehand;
	private final Line secondhand;

	AnalogPane(){
		Circle frame = new Circle(CLOCK_X, CLOCK_Y, CLOCK_RADIUS, Color.WHITE);
		frame.setStroke(Color.BLACK);
		this.getChildren().add(frame);

		//creating the clock face
		Line[] smallmarking = new Line[12 * 4];
		Line[] largemarking = new Line[12];
		Text[] numbering = new Text[12];
		for (int i = 1; i <=12; i++) {
			for (int k = 1; k <= 4; k++) {
				smallmarking[(i - 1) * 4 + k - 1] = new Line(
						getX(CLOCK_RADIUS - SMALL_MARKING, 6 * k + 30 * (i - 1)),
						getY(CLOCK_RADIUS - SMALL_MARKING, 6 * k + 30 * (i - 1)),
						getX(CLOCK_RADIUS, 6 * k + 30 * (i - 1)),
						getY(CLOCK_RADIUS, 6 * k + 30 * (i - 1)));
				this.getChildren().add(smallmarking[(i - 1) * 4 + k - 1]);
			}
			largemarking[i - 1] = new Line(
					getX(CLOCK_RADIUS - LARGE_MARKING, 30 * i),
					getY(CLOCK_RADIUS - LARGE_MARKING, 30 * i),
					getX(CLOCK_RADIUS, 30 * i),
					getY(CLOCK_RADIUS, 30 * i));
			this.getChildren().add(largemarking[i - 1]);

			numbering[i - 1] = new Text(Integer.toString(i));
			numbering[i - 1].setX(
					getX(CLOCK_RADIUS - NUMBERING, 30 * i) -
							numbering[i - 1].getBoundsInLocal().getWidth() / 2);
			numbering[i - 1].setY(
					getY(CLOCK_RADIUS - NUMBERING, 30 * i) +
							numbering[i - 1].getBoundsInLocal().getHeight() / 3);
			this.getChildren().add(numbering[i - 1]);
		}

		//creating the hands
		hourhand = new Line(
				CLOCK_X,
				CLOCK_Y,
				getX(HOUR_HAND, 0),
				getY(HOUR_HAND, 0));
		hourhand.setStroke(Color.LIGHTGREEN);
		hourhand.setStrokeWidth(5);
		minutehand = new Line(
				CLOCK_X,
				CLOCK_Y,
				getX(MINUTE_HAND, 0),
				getY(MINUTE_HAND, 0));
		minutehand.setStroke(Color.SLATEBLUE);
		minutehand.setStrokeWidth(3);
		secondhand = new Line(
				CLOCK_X,
				CLOCK_Y,
				getX(SECOND_HAND, 0),
				getY(SECOND_HAND, 0));
		secondhand.setStroke(Color.INDIANRED);

		updateClock();
		this.getChildren().addAll(hourhand, minutehand, secondhand);
	}

	private static double getX(double dist, double deg) {
		//gets an x coordinate of a point that is dist from CLOCK_X, CLOCK_Y at angle deg
		return CLOCK_X + dist * Math.sin(Math.toRadians(deg));
	}

	private static double getY(double dist, double deg) {
		//gets an y coordinate of a point that is dist from CLOCK_X, CLOCK_Y at angle deg
		return CLOCK_Y - dist * Math.cos(Math.toRadians(deg));
	}
	private static double hourToDeg(double hour, double minute) {
		return hour / 12 * 360 + minute / (12 * 60) * 360;
	}
	private static double minuteToDeg(double minute, double second) {
		return minute / 60 * 360 + second / (60 * 60) * 360;
	}
	private static double secondToDeg(double second, double millisecond) {
		return second / 60 * 360 + millisecond / (60 * 1000) * 360;
	}

	@Override
	public void updateClock() {
		cal = Calendar.getInstance();
		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
		millisecond = cal.get(Calendar.MILLISECOND);
		second = cal.get(Calendar.SECOND);

		hourhand.setEndX(getX(HOUR_HAND, hourToDeg(hour, minute)));
		hourhand.setEndY(getY(HOUR_HAND, hourToDeg(hour, minute)));
		minutehand.setEndX(getX(MINUTE_HAND, minuteToDeg(minute, second)));
		minutehand.setEndY(getY(MINUTE_HAND, minuteToDeg(minute, second)));
		secondhand.setEndX(getX(SECOND_HAND, secondToDeg(second, millisecond)));
		secondhand.setEndY(getY(SECOND_HAND, secondToDeg(second, millisecond)));
	}
}