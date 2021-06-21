package pi_estimator;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EstimateNode extends Pane {
	public EstimateNode(Estimate estimate, String colour) {
		final Label label = new Label(estimate.toString());
		if (colour != null) {
			label.setStyle("-fx-border-color: " + colour + ";");
		}
		label.getStyleClass().addAll( "button");

		setOnMouseEntered(event -> {
			this.getChildren().add(label);
			toFront();
		});

		setOnMouseExited(event -> this.getChildren().clear());
	}
}
