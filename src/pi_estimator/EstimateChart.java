package pi_estimator;

import com.sun.javafx.charts.Legend;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO avoid cutting off labels outside chart boundaries
//TODO zoom?
//TODO determine if serieswrapper still needs state attribute

/*
Draws estimates grouped by number of digits on a javafx chart.
Probably not ideal because it's too resource-intensive when fed
a high digit number; every estimate is a separate node.

The display also gets a little... funky at high digits due
to over-crowding. Looks best at <=6 digits.
 */
public class EstimateChart extends Application {
	static final int MAX_DIGITS = 6;

	static Map<Node, SeriesWrapper> nodeSeriesMap;
	static Node selectedNode = null;

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

		//getting lists of estimate from the generator
		EstimateGenerator.populate(MAX_DIGITS);
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
		Scene scene = new Scene(chart, 800, 600);
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
		//while calculating x-axis value in situ
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
				data.setNode(new EstimateNode(currentEstimate, seriesWrapper, legendSymbol));

				series.getData().add(data);
			}
		}
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
