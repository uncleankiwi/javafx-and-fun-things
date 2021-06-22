package pi_estimator;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EstimateNode extends Pane {
	public EstimateNode(Estimate estimate, SeriesWrapper seriesWrapper, Node legendSymbol) {
		final Label label = new Label(estimate.toString());
		if (seriesWrapper != null) {
			label.setStyle("-fx-border-color: " + seriesWrapper.getColour() + ";");
		}
		label.getStyleClass().addAll("button");

		setOnMouseEntered(event -> {
			//check if the series this node is on is currently selected
			if (EstimateChart.selectedNode == legendSymbol) {
				this.getChildren().add(label);
				toFront();
			}
		});

		setOnMouseExited(event -> this.getChildren().clear());
	}
}
