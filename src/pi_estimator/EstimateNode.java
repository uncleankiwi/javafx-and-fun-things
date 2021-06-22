package pi_estimator;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class EstimateNode extends Pane {
	private boolean positionInitialized = false;
	double labelX;
	double labelY;

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
				applyCss();	//make it calculate width and height, otherwise they'll be 0
				layout();
				label.setLayoutX(-label.getWidth() / 2);
				label.setLayoutY(-label.getHeight());
				toFront();
			}
		});

		setOnMouseExited(event -> this.getChildren().clear());
	}
}
