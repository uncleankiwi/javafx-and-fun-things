package pi_estimator;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EstimateNode extends Pane {
	public EstimateNode(Estimate estimate) {
		final Label label = new Label(estimate.toString() + "\nCloseness: " + estimate.closeness());

		setOnMouseEntered(event -> {
			this.getChildren().add(label);
			toFront();
		});

		setOnMouseExited(event -> this.getChildren().clear());
	}
}
