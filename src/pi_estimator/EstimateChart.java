package pi_estimator;

import com.sun.javafx.charts.Legend;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/*
Draw estimates grouped by number of digits on a javafx chart.
Probably not ideal because it's too resource-intensive when fed
a high digit number; every estimate is a separate node.

The display also gets a little... funky at high digits due
to over-crowding. Looks best at <=6 digits.

A StackPane is needed; a Label cannot be directly added to EstimateNode,
because then the Label would belong to the Chart, which cuts off anything
that's outside it.

EstimateChart
	VBox
		hBoxHints
			lblHintTitle
			lblHints - application usage hints
		hBoxRadioButtons
			lblRadioButtons - number to estimate
			radPi
			radE
			radOther
			txtOther
		hBoxSpinner
			lblSpinner - max digits to use
			btnGraph
			spnDigits
		StackPane
			lblEstimateNode - Label: for estimate node tooltip
			Chart
 */
public class EstimateChart extends Application {
	final double ZOOM_FACTOR = 1.05;
	double yUpperBound;
	final double Y_LOWER_BOUND = 0;
	final double X_UPPER_BOUND = 1.1;
	final double X_LOWER_BOUND = -0.1;

	Map<Node, SeriesWrapper> nodeSeriesMap;
	Node selectedNode = null;
	final Label lblEstimateNode = new Label();	//labels an EstimateNode when it's moused-over
	final StackPane stackPane = new StackPane();	//holds chart and EstimateNode tooltip
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

	final Insets INSETS = new Insets(2, 5, 2, 5);
	final double SPACING = 2;

	final RadioButton radPi = new RadioButton("Pi\t");
	final RadioButton radE = new RadioButton("E\t");
	final RadioButton radOther = new RadioButton("Other\t");
	final TextField txtOther = new TextField();
	final Spinner<Integer> spnDigits = new Spinner<>(1, 9, 6);

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		//initialize controls
		stage.setTitle("Irrational number estimator");

		HBox hBoxHints = new HBox();
		hBoxHints.setPadding(INSETS);
		hBoxHints.setSpacing(SPACING);
		Text lblHintTitle = new Text("Tips:");
		Text lblHints = new Text("Select icons in the legend to enable tooltips.\n" +
			"Scroll to zoom in and out. Right click to reset zoom.");
		lblHints.autosize();
		hBoxHints.getChildren().addAll(lblHintTitle, lblHints);

		HBox hBoxRadioButtons = new HBox();
		hBoxRadioButtons.setPadding(INSETS);
		hBoxRadioButtons.setSpacing(SPACING);
		ToggleGroup toggleGroupNumber = new ToggleGroup();
		Text lblRadioButtons = new Text("Number to estimate:");
		radPi.setToggleGroup(toggleGroupNumber);
		radE.setToggleGroup(toggleGroupNumber);
		radOther.setToggleGroup(toggleGroupNumber);
		radPi.setSelected(true);
		txtOther.setPromptText("Enter a number");
		hBoxRadioButtons.getChildren().addAll(lblRadioButtons, radPi, radE, radOther, txtOther);

		HBox hBoxSpinner = new HBox();
		hBoxSpinner.setPadding(INSETS);
		hBoxSpinner.setSpacing(SPACING);
		Text lblSpinner = new Text("Max digits to use:");
		Button btnGraph = new Button("Graph");
		btnGraph.setOnAction(event -> graph());
		Button btnResetZoom = new Button("Reset zoom");
		btnResetZoom.setOnAction(event -> resetZoom());
		hBoxSpinner.getChildren().addAll(lblSpinner, spnDigits, btnGraph, btnResetZoom);

		yAxis.setLabel("Closeness of estimate");
		yAxis.setMinorTickVisible(false);
		xAxis.setLabel("Denominator of fraction estimating the number");
		xAxis.setTickMarkVisible(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setMinorTickVisible(false);
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
		xAxis.setUpperBound(X_UPPER_BOUND);
		xAxis.setLowerBound(X_LOWER_BOUND);
		chart.setTitle("Closeness of estimates");
		lblEstimateNode.getStyleClass().add("chart-legend");
		chart.setMinHeight(500);
		stackPane.getChildren().add(chart);

		VBox vBox = new VBox();
		vBox.setPadding(INSETS);
		vBox.setSpacing(SPACING);
		vBox.getChildren().addAll(hBoxHints, hBoxRadioButtons, hBoxSpinner, stackPane);

		//initializing chart - seems like css properties can't be read before this.
		//has to be done before putting nodes on each series, since each node also needs
		//to read the colour of the series it's on.
		chart.getStylesheets().add(Objects.requireNonNull(getClass().getResource("chart.css")).toExternalForm());
		Scene scene = new Scene(vBox, 800, 600);
		stage.setScene(scene);
		stage.show();

		//populate chart
		graph();

		//enabling zoom
		chart.setOnScroll(event -> {
			if (yAxis.isAutoRanging()) yAxis.setAutoRanging(false);
			int zoomPolarity = event.getDeltaY() > 0 ? 1 : -1;

			double minY = chart.sceneToLocal(yAxis.localToScene(
				new Point2D(0, yAxis.getDisplayPosition(yAxis.getUpperBound())))).getY();
			double maxY = chart.sceneToLocal(yAxis.localToScene(
				new Point2D(0, yAxis.getDisplayPosition(yAxis.getLowerBound())))).getY();
			double mouseY = event.getY();
			double oldScaleMaxY = yAxis.getUpperBound();
			double oldScaleMinY = yAxis.getLowerBound();
			double deltaScaleY = (oldScaleMaxY - oldScaleMinY) * (ZOOM_FACTOR - 1);
			double newScaleMaxY = oldScaleMaxY - zoomPolarity * deltaScaleY * (mouseY - minY) / (maxY - minY);
			double newScaleMinY = oldScaleMinY + zoomPolarity * deltaScaleY * (maxY - mouseY) / (maxY - minY);
			yAxis.setUpperBound(newScaleMaxY);
			yAxis.setLowerBound(newScaleMinY);

			double minX = chart.sceneToLocal(xAxis.localToScene(
				new Point2D(xAxis.getDisplayPosition(xAxis.getLowerBound()), 0))).getX();
			double maxX = chart.sceneToLocal(xAxis.localToScene(
				new Point2D(xAxis.getDisplayPosition(xAxis.getUpperBound()), 0))).getX();
			double mouseX = event.getX();
			double oldScaleMaxX = xAxis.getUpperBound();
			double oldScaleMinX = xAxis.getLowerBound();
			double deltaScaleX = (oldScaleMaxX - oldScaleMinX) * (ZOOM_FACTOR - 1);
			double newScaleMaxX = oldScaleMaxX - zoomPolarity * deltaScaleX * (maxX - mouseX) / (maxX - minX);
			double newScaleMinX = oldScaleMinX + zoomPolarity * deltaScaleX * (mouseX - minX) / (maxX - minX);

			xAxis.setUpperBound(newScaleMaxX);
			xAxis.setLowerBound(newScaleMinX);

		});
		chart.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.SECONDARY) {
				resetZoom();
			}
		});
	}

	private void resetZoom() {
		yAxis.setUpperBound(yUpperBound);
		yAxis.setLowerBound(Y_LOWER_BOUND);
		xAxis.setUpperBound(X_UPPER_BOUND);
		xAxis.setLowerBound(X_LOWER_BOUND);
	}

	public void showEstimateNodeTooltip(Estimate estimate, SeriesWrapper seriesWrapper, MouseEvent event) {
		//setting label text and colour
		lblEstimateNode.setText(estimate.toString());
		if (seriesWrapper != null) lblEstimateNode.setStyle("-fx-border-color: " + seriesWrapper.getColour() + ";");

		stackPane.getChildren().add(lblEstimateNode);
		stackPane.applyCss();	//make it calculate width and height, otherwise they'll be 0
		stackPane.layout();

		Point2D lblEstimateNodePos = stackPane.sceneToLocal(new Point2D(event.getSceneX() - lblEstimateNode.getWidth() / 2,
			event.getSceneY() - 1.2 * lblEstimateNode.getHeight()));

		lblEstimateNode.setLayoutX(lblEstimateNodePos.getX());
		lblEstimateNode.setLayoutY(lblEstimateNodePos.getY());
		lblEstimateNode.toFront();
	}

	public void graph() {
		//clearing things
		chart.getData().clear();
		selectedNode = null;

		//getting lists of estimate from the generator
		double referenceValue;
		if (radPi.isSelected()) referenceValue = Math.PI;
		else if (radE.isSelected()) referenceValue = Math.E;
		else {
			try {
				referenceValue = Double.parseDouble(txtOther.getText());
			}
			catch (NumberFormatException e) {
				return;
			}
		}
		int maxDigits = spnDigits.getValue();
		EstimateGenerator.populate(maxDigits, referenceValue);
		Map<Integer, List<Estimate>> estimates = EstimateGenerator.get();

		//graphing series
		for (int i = 1; i <= maxDigits; i++) {	//for each digit band
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(i + " digit" + (i != 1 ? "s" : ""));
			chart.getData().add(series);
		}

		//Making legend toggle graph opacity and tooltips.
		//When nothing is selected, all are opaque and without tooltips.
		//When a legend item is moused over or selected, then that graph is opaque with tooltips;
		//all others are translucent and without tooltips.
		chart.applyCss();
		chart.layout();
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
		//The highest closeness is also noted to adjust the y-axis scale.
		double highestCloseness = 0;
		for (int i = 1; i <= maxDigits; i++) {	//for each digit band
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
				if (currentEstimate.absoluteCloseness() > highestCloseness) highestCloseness = currentEstimate.absoluteCloseness();

				XYChart.Data<Number, Number> data =
					new XYChart.Data<>((
						(double) j / (bandEstimates.size() - 1)),
						currentEstimate.absoluteCloseness());
				data.setNode(new EstimateNode(currentEstimate, seriesWrapper, legendSymbol, this));

				series.getData().add(data);
			}
		}

		//resetting zoom. yUpperBound, the max value on the y-axis, has to be manually
		//calculated. yAxis.getUpperBound() seems to return wrong values.
		yUpperBound = Math.ceil(highestCloseness * 1.2);
		resetZoom();
	}

	public void hideEstimateNodeTooltip() {
		stackPane.getChildren().remove(lblEstimateNode);
	}

	//set node to selected, set others to background
	private void legendOnMouseEntered(Node node) {
		SeriesWrapper seriesWrapper = nodeSeriesMap.get(node);
		setSeriesToSelected(seriesWrapper);
	}

	//if nothing is selected, set all to idle
	//if something is selected, set to selected, set others to background
	private void legendOnOnMouseExited() {
		if (selectedNode == null) {
			setIdleAll();
		}
		else {
			SeriesWrapper seriesWrapper = nodeSeriesMap.get(selectedNode);
			setSeriesToSelected(seriesWrapper);
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
				setSeriesToSelected(seriesWrapper);
			}
		}
	}

	private void setIdleAll() {
		for (SeriesWrapper seriesWrapper : nodeSeriesMap.values()) {
			seriesWrapper.setIdle();
		}
	}

	//sets one series to selected and the remaining to background
	private void setSeriesToSelected(SeriesWrapper selectedWrapper) {
		for (SeriesWrapper seriesWrapper : nodeSeriesMap.values()) {
			if (seriesWrapper == selectedWrapper) seriesWrapper.setSelected();
			else seriesWrapper.setBackground();
		}
	}

}
