package pi_estimator;

import com.sun.javafx.charts.Legend;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO add radiobuttons for selecting other irrational numbers?
//TODO determine if serieswrapper still needs state attribute

/*
Draws estimates grouped by number of digits on a javafx chart.
Probably not ideal because it's too resource-intensive when fed
a high digit number; every estimate is a separate node.

The display also gets a little... funky at high digits due
to over-crowding. Looks best at <=6 digits.

A StackPane is needed; a Label cannot be directly added to EstimateNode,
because then the Label would belong to the Chart, which cuts off anything
that's outside it.

EstimateChart
	StackPane
		Label - for estimate node tooltip
		Chart
 */
public class EstimateChart extends Application {
	final int MAX_DIGITS = 6;
	final double ZOOM_FACTOR = 1.05;
	double yUpperBound;
	double yLowerBound;
	double xUpperBound;
	double xLowerBound;

	Map<Node, SeriesWrapper> nodeSeriesMap;
	Node selectedNode = null;
	final Label estimateNodeLabel = new Label();	//labels an EstimateNode when it's moused-over
	final StackPane stackPane = new StackPane();	//holds chart and EstimateNode tooltip

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Pi estimator");

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Closeness of estimate");
		yAxis.setMinorTickVisible(false);
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Denominator of fraction estimating pi");
		xAxis.setTickMarkVisible(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setMinorTickVisible(false);
		xAxis.setAutoRanging(false);
		xAxis.setUpperBound(1.1);
		xAxis.setLowerBound(-0.1);
		final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
		chart.setTitle("Closeness of estimates of pi\n" +
			"(Select icons in the legend to enable tooltips)");
		estimateNodeLabel.getStyleClass().add("chart-legend");
		stackPane.getChildren().add(chart);

		//getting lists of estimate from the generator
		EstimateGenerator.populate(MAX_DIGITS, Math.PI);
		Map<Integer, List<Estimate>> estimates = EstimateGenerator.get();

		//initializing chart - seems like css properties can't be read before this.
		//has to be done before putting nodes on each series, since each node also needs
		//to read the colour of the series it's on.
		for (int i = 1; i <= MAX_DIGITS; i++) {	//for each digit band
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(i + " digit" + (i != 1 ? "s" : ""));
			chart.getData().add(series);
		}
		chart.getStylesheets().add(Objects.requireNonNull(getClass().getResource("chart.css")).toExternalForm());
		Scene scene = new Scene(stackPane, 800, 600);
		stage.setScene(scene);
		stage.show();

		//Making legend toggle graph opacity and tooltips.
		//When nothing is selected, all are opaque and without tooltips.
		//When a legend item is moused over or selected, then that graph is opaque with tooltips;
		//all others are translucent and without tooltips.
		for (Node node : chart.getChildrenUnmodifiable()){
			if (node instanceof Legend) {
				Legend legend = (Legend) node;
				nodeSeriesMap = new HashMap<>();

				//match each LegendItem to each series on chart
				for (Legend.LegendItem legendItem : legend.getItems()) {

					for (XYChart.Series<Number, Number> series : chart.getData()) {
						if (legendItem.getText().equals(series.getName())) {
							nodeSeriesMap.put(legendItem.getSymbol(), new SeriesWrapper(series, legendItem.getSymbol()));
							legendItem.getSymbol().setCursor(Cursor.HAND);
							legendItem.getSymbol().setOnMouseEntered(event -> legendOnMouseEntered(legendItem.getSymbol()));
							legendItem.getSymbol().setOnMouseExited(event -> legendOnOnMouseExited());
							legendItem.getSymbol().setOnMouseClicked(event -> legendOnMouseClicked(event, legendItem.getSymbol()));
							break;
						}
					}
				}
			}
		}

		//Assign bands to separate series, then populate those series
		//while calculating x-axis value in situ.
		//It also assigns a label that pops up.
		//Each EstimateNode also needs to know its SeriesWrapper so that it can share its colour.
		for (int i = 1; i <= MAX_DIGITS; i++) {	//for each digit band
			List<Estimate> bandEstimates = estimates.get(i);
			XYChart.Series<Number, Number> series = chart.getData().get(i - 1);

			//getting serieswrapper by finding legend symbol with series name
			SeriesWrapper seriesWrapper = null;
			Node legendSymbol = null;
			for (Map.Entry<Node, SeriesWrapper> entry: nodeSeriesMap.entrySet()) {
				if (series.getName().equals(entry.getValue().getName())) {
					seriesWrapper = entry.getValue();
					legendSymbol = entry.getKey();
					break;
				}
			}

			for (int j = 0; j < bandEstimates.size(); j++) {	//for every different denominator
				Estimate currentEstimate = bandEstimates.get(j);

				XYChart.Data<Number, Number> data =
					new XYChart.Data<>((
						(double) j / (bandEstimates.size() - 1)),
						currentEstimate.absoluteCloseness());
				data.setNode(new EstimateNode(currentEstimate, seriesWrapper, legendSymbol, this));

				series.getData().add(data);
			}
		}

		//enabling zoom. needs to know chart y-value range, so happens after populating with data.
		//right click resets zoom.
		chart.getYAxis().setAutoRanging(false);
		yUpperBound = ((NumberAxis) chart.getYAxis()).getUpperBound();
		yLowerBound = ((NumberAxis) chart.getYAxis()).getLowerBound();
		xUpperBound = ((NumberAxis) chart.getXAxis()).getUpperBound();
		xLowerBound = ((NumberAxis) chart.getXAxis()).getLowerBound();
		chart.setOnScroll(event -> {
			int zoomPolarity = event.getDeltaY() > 0 ? 1 : -1;

			double minY = chart.getLayoutBounds().getMinY();
			double maxY = chart.getLayoutBounds().getMaxY();
			double mouseY = event.getY();
			double oldScaleMaxY = ((NumberAxis) chart.getYAxis()).getUpperBound();
			double oldScaleMinY = ((NumberAxis) chart.getYAxis()).getLowerBound();
			double deltaScaleY = (oldScaleMaxY - oldScaleMinY) * (ZOOM_FACTOR - 1);
			double newScaleMaxY = oldScaleMaxY - zoomPolarity * deltaScaleY * (mouseY - minY) / (maxY - minY);
			double newScaleMinY = oldScaleMinY + zoomPolarity * deltaScaleY * (maxY - mouseY) / (maxY - minY);

			((NumberAxis) chart.getYAxis()).setUpperBound(newScaleMaxY);
			((NumberAxis) chart.getYAxis()).setLowerBound(newScaleMinY);

			double minX = chart.getLayoutBounds().getMinX();
			double maxX = chart.getLayoutBounds().getMaxX();
			double mouseX = event.getX();
			double oldScaleMaxX = ((NumberAxis) chart.getXAxis()).getUpperBound();
			double oldScaleMinX = ((NumberAxis) chart.getXAxis()).getLowerBound();
			double deltaScaleX = (oldScaleMaxX - oldScaleMinX) * (ZOOM_FACTOR - 1);
			double newScaleMaxX = oldScaleMaxX - zoomPolarity * deltaScaleX * (maxX - mouseX) / (maxX - minX);
			double newScaleMinX = oldScaleMinX + zoomPolarity * deltaScaleX * (mouseX - minX) / (maxX - minX);

			((NumberAxis) chart.getXAxis()).setUpperBound(newScaleMaxX);
			((NumberAxis) chart.getXAxis()).setLowerBound(newScaleMinX);

		});
		chart.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				((NumberAxis) chart.getYAxis()).setUpperBound(yUpperBound);
				((NumberAxis) chart.getYAxis()).setLowerBound(yLowerBound);
				((NumberAxis) chart.getXAxis()).setUpperBound(xUpperBound);
				((NumberAxis) chart.getXAxis()).setLowerBound(xLowerBound);
			}
		});
	}

	public void showEstimateNodeTooltip(Estimate estimate, SeriesWrapper seriesWrapper, MouseEvent event) {
		//setting label text and colour
		estimateNodeLabel.setText(estimate.toString());
		if (seriesWrapper != null) estimateNodeLabel.setStyle("-fx-border-color: " + seriesWrapper.getColour() + ";");

		stackPane.getChildren().add(estimateNodeLabel);
		stackPane.applyCss();	//make it calculate width and height, otherwise they'll be 0
		stackPane.layout();

		estimateNodeLabel.setLayoutX(event.getSceneX() - estimateNodeLabel.getWidth() / 2);
		estimateNodeLabel.setLayoutY(event.getSceneY() - 1.2 * estimateNodeLabel.getHeight());
		estimateNodeLabel.toFront();
	}

	public void hideEstimateNodeTooltip() {
		stackPane.getChildren().remove(estimateNodeLabel);
	}

	//set node to selected, set others to background
	private void legendOnMouseEntered(Node node) {
		SeriesWrapper seriesWrapper = nodeSeriesMap.get(node);
		setOneToSelected(seriesWrapper);
	}

	//if nothing is selected, set all to idle
	//if something is selected, set to selected, set others to background
	private void legendOnOnMouseExited() {
		if (selectedNode == null) {
			setIdleAll();
		}
		else {
			SeriesWrapper seriesWrapper = nodeSeriesMap.get(selectedNode);
			setOneToSelected(seriesWrapper);
		}
	}

	//if already selected, set all to idle
	//if not selected, set to selected, and set others to background
	private void legendOnMouseClicked(MouseEvent event, Node node) {
		if (event.getButton() == MouseButton.PRIMARY) {
			//if this is selected, deselect it
			if (selectedNode == node) {
				setIdleAll();
				selectedNode = null;
			}
			//if a different node was selected, deselect that, and select this
			else {
				selectedNode = node;
				SeriesWrapper seriesWrapper = nodeSeriesMap.get(node);
				setOneToSelected(seriesWrapper);
			}
		}
	}

	private void setIdleAll() {
		for (SeriesWrapper seriesWrapper : nodeSeriesMap.values()) {
			seriesWrapper.setIdle();
		}
	}

	//sets one series to selected and the remaining to background
	private void setOneToSelected(SeriesWrapper selectedWrapper) {
		for (SeriesWrapper seriesWrapper : nodeSeriesMap.values()) {
			if (seriesWrapper == selectedWrapper) seriesWrapper.setSelected();
			else seriesWrapper.setBackground();
		}
	}

}
