package pi_estimator;

import javafx.scene.chart.XYChart;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Holds a JavaFx Series node, its original colour, its translucent colour,
as well as methods to set it to different states:
	1. idle (original colour, no tooltip) - when no legendItem is selected
	2. selected (original colour, tooltip) - when selected or moused-over
	3. background (translucent colour, no tooltip) - when something else is selected/moused-over
 */
public class SeriesWrapper {
	private final XYChart.Series<Number, Number> series;
	private State state;		//is this node fully visible/translucent/etc
	private String colour;
	private String fadedColour;

	public SeriesWrapper(XYChart.Series<Number, Number> series) {
		this.state = State.IDLE;
		this.series = series;
		extractColour(series.nodeProperty().get().toString());
	}

	public void setIdle() {
		state = State.IDLE;
		setColour();
	}

	public void setSelected() {
		state = State.SELECTED;
		series.getNode().toFront();
		setColour();
	}

	public void setBackground() {
		state = State.BACKGROUND;
		setFadedColour();
	}

	//Extract colour from node properties e.g. stroke=0x9a42c8ff
	//and put it into the colour attribute
	private void extractColour(String css) {
		Pattern pattern = Pattern.compile("(?<=stroke=0x)[0-9a-z]{8}");
		Matcher matcher = pattern.matcher(css);
		if (matcher.find())	colour = "#" + matcher.group(0);
		System.out.println(colour);
		fadedColour = colour.substring(0, colour.length() - 2) + "33";
	}

	private void setColour() {
		series.getNode().setStyle("-fx-stroke: " + colour + ";");
	}

	private void setFadedColour() {
		series.getNode().setStyle("-fx-stroke: " + fadedColour + ";");
	}

	private enum State {
		IDLE,
		SELECTED,
		BACKGROUND
	}


}