package pi_estimator;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class EstimateNode extends Pane {

	public EstimateNode(Estimate estimate, SeriesWrapper seriesWrapper, Node legendSymbol) {
		final Label label = new Label(estimate.toString());
		if (seriesWrapper != null) label.setStyle("-fx-border-color: " + seriesWrapper.getColour() + ";");

		label.getStyleClass().addAll("chart-legend");

		setOnMouseEntered(event -> {
			//check if the series this node is on is currently selected
			if (EstimateChart.selectedNode == legendSymbol) {
				this.getChildren().add(label);
				applyCss();	//make it calculate width and height, otherwise they'll be 0
				layout();

				if (seriesWrapper != null) this.setStyle("-fx-background-color: " + seriesWrapper.getColour() + ";");
				label.setLayoutX(-label.getWidth() / 2);
				label.setLayoutY(-label.getHeight());
				toFront();
			}
			event.consume();
		});

		setOnMouseExited(event -> {
			this.getChildren().clear();
			this.setStyle("-fx-background-color: #ffffff00;");
			event.consume();
		});
	}
}
