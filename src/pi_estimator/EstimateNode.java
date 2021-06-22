package pi_estimator;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class EstimateNode extends Pane {

	public EstimateNode(Estimate estimate, SeriesWrapper seriesWrapper, Node legendSymbol, EstimateChart estimateChart) {
		setOnMouseEntered(event -> {
			//Check if the series this node is on is currently selected.
			//If so, then make this data point visible, then get EstimateChart to show a tooltip.
			if (estimateChart.selectedNode == legendSymbol) {
				if (seriesWrapper != null) this.setStyle("-fx-background-color: " + seriesWrapper.getColour() + ";");
				estimateChart.showEstimateNodeTooltip(estimate, seriesWrapper, event);
			}
			event.consume();
		});

		setOnMouseExited(event -> {
			this.setStyle("-fx-background-color: #ffffff00;");
			estimateChart.hideEstimateNodeTooltip();
			event.consume();
		});
	}
}
