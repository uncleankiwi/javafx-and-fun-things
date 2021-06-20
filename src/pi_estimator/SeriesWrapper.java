package pi_estimator;

import javafx.scene.Node;

/*
Holds a JavaFx Series node, its original colour, its translucent colour,
as well as methods to set it to different states:
	1. idle (original colour, no tooltip) - when no legendItem is selected
	2. selected (original colour, tooltip) - when selected or moused-over
	3. background (translucent colour, no tooltip) - when something else is selected/moused-over
 */
public class SeriesWrapper {
	private final Node node;	//the node of a series on a Chart
	private State state;		//is this node fully visible/translucent/etc

	public SeriesWrapper(Node node) {
		this.node = node;
		this.state = State.IDLE;
	}

	public void setIdle() {
		this.state = State.IDLE;
		this.node.setVisible(true);
	}

	public void setSelected() {
		this.state = State.SELECTED;
		this.node.setVisible(true);
	}

	public void setBackground() {
		this.state = State.BACKGROUND;
		this.node.setVisible(false);
	}

	private enum State {
		IDLE,
		SELECTED,
		BACKGROUND
	}


}
