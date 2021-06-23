package pi_estimator;

import com.sun.javafx.charts.Legend;
import javafx.application.Application;
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
	VBox
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
	final int MAX_DIGITS = 6;
	final double ZOOM_FACTOR = 1.05;
	double yUpperBound;
	double yLowerBound;
	final double X_UPPER_BOUND = 1.1;
	final double X_LOWER_BOUND = -0.1;

	Map<Node, SeriesWrapper> nodeSeriesMap;
	Node selectedNode = null;
	final Label lblEstimateNode = new Label();	//labels an EstimateNode when it's moused-over
	final StackPane stackPane = new StackPane();	//holds chart and EstimateNode tooltip
	final NumberAxis xAxis = new NumberAxis();
	final NumberAxis yAxis = new NumberAxis();
	final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);

	RadioButton radPi = new RadioButton("Pi");
	RadioButton radE = new RadioButton("E");
	RadioButton radOther = new RadioButton("Other");
	TextField txtOther = new TextField();
	Spinner<Integer> spnDigits = new Spinner<>(1, 9, 6);

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		//initialize controls
		stage.setTitle("Irrational number estimator");
		yAxis.setLabel("Closeness of estimate");
		yAxis.setMinorTickVisible(false);
		xAxis.setLabel("Denominator of fraction estimating the number");
		xAxis.setTickMarkVisible(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setMinorTickVisible(false);
		xAxis.setAutoRanging(false);
		xAxis.setUpperBound(X_UPPER_BOUND);
		xAxis.setLowerBound(X_LOWER_BOUND);
		chart.setTitle("Closeness of estimates");
		lblEstimateNode.getStyleClass().add("chart-legend");
		stackPane.getChildren().add(chart);

		Label lblHints = new Label("Select icons in the legend to enable tooltips.\n" +
			"Scroll to zoom in and out. Right click to reset zoom.");

		HBox hBoxRadioButtons = new HBox();
		ToggleGroup toggleGroupNumber = new ToggleGroup();
		Label lblRadioButtons = new Label("Number to estimate");
		radPi.setToggleGroup(toggleGroupNumber);
		radE.setToggleGroup(toggleGroupNumber);
		radOther.setToggleGroup(toggleGroupNumber);
		radPi.setSelected(true);
		Button btnGraph = new Button("Graph");
		hBoxRadioButtons.getChildren().addAll(lblRadioButtons, radPi, radE, radOther, txtOther);
		btnGraph.setOnAction(event -> graph());

		HBox hBoxSpinner = new HBox();
		Label lblSpinner = new Label("Max digits to use");
		hBoxSpinner.getChildren().addAll(lblSpinner, spnDigits, btnGraph);

		VBox vBox = new VBox();
		vBox.getChildren().addAll(lblHints, hBoxRadioButtons, hBoxSpinner, stackPane);

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
			int zoomPolarity = event.getDeltaY() > 0 ? 1 : -1;

			double minY = chart.sceneToLocal(yAxis.localToScene(
				new Point2D(0, yAxis.getDisplayPosition(yAxis.getUpperBound())))).getY();
			double maxY = chart.sceneToLocal(yAxis.localToScene(
				new Point2D(0, yAxis.getDisplayPosition(yAxis.getLowerBound())))).getY();
			double mouseY = event.getY();
			double oldScaleMaxY = ((NumberAxis) chart.getYAxis()).getUpperBound();
			double oldScaleMinY = ((NumberAxis) chart.getYAxis()).getLowerBound();
			double deltaScaleY = (oldScaleMaxY - oldScaleMinY) * (ZOOM_FACTOR - 1);
			double newScaleMaxY = oldScaleMaxY - zoomPolarity * deltaScaleY * (mouseY - minY) / (maxY - minY);
			double newScaleMinY = oldScaleMinY + zoomPolarity * deltaScaleY * (maxY - mouseY) / (maxY - minY);

			((NumberAxis) chart.getYAxis()).setUpperBound(newScaleMaxY);
			((NumberAxis) chart.getYAxis()).setLowerBound(newScaleMinY);

			double minX = chart.sceneToLocal(xAxis.localToScene(
				new Point2D(xAxis.getDisplayPosition(xAxis.getLowerBound()), 0))).getX();
			double maxX = chart.sceneToLocal(xAxis.localToScene(
				new Point2D(xAxis.getDisplayPosition(xAxis.getUpperBound()), 0))).getX();
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
				((NumberAxis) chart.getXAxis()).setUpperBound(X_UPPER_BOUND);
				((NumberAxis) chart.getXAxis()).setLowerBound(X_LOWER_BOUND);
			}
		});
	}

	public void showEstimateNodeTooltip(Estimate estimate, SeriesWrapper seriesWrapper, MouseEvent event) {
		//setting label text and colour
		lblEstimateNode.setText(estimate.toString());
		if (seriesWrapper != null) lblEstimateNode.setStyle("-fx-border-color: " + seriesWrapper.getColour() + ";");

		stackPane.getChildren().add(lblEstimateNode);
		stackPane.applyCss();	//make it calculate width and height, otherwise they'll be 0
		stackPane.layout();

		lblEstimateNode.setLayoutX(event.getSceneX() - lblEstimateNode.getWidth() / 2);
		lblEstimateNode.setLayoutY(event.getSceneY() - 1.2 * lblEstimateNode.getHeight());
		lblEstimateNode.toFront();
	}

	public void graph() {
		//clearing things
		chart.getData().clear();
		chart.getYAxis().setAutoRanging(true);
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

				XYChart.Data<Number, Number> data =
					new XYChart.Data<>((
						(double) j / (bandEstimates.size() - 1)),
						currentEstimate.absoluteCloseness());
				data.setNode(new EstimateNode(currentEstimate, seriesWrapper, legendSymbol, this));

				series.getData().add(data);
			}
		}

		//resetting zoom
		chart.getYAxis().setAutoRanging(false);
		chart.getXAxis().setAutoRanging(false);
		yUpperBound = ((NumberAxis) chart.getYAxis()).getUpperBound();
		yLowerBound = ((NumberAxis) chart.getYAxis()).getLowerBound();
	}

	public void hideEstimateNodeTooltip() {
		stackPane.getChildren().remove(lblEstimateNode);
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
