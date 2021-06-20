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
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Objects;

//TODO hover values for selected chart


/*
Draws estimates grouped by number of digits on a javafx chart.
Probably not ideal because it's too resource-intensive when fed
a high digit number; every estimate is a separate node.

The display also gets a little... funky at high digits due
to over-crowding. Looks best at <=6 digits.
 */
public class EstimateChart extends Application {
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
		chart.setTitle("Closeness of estimates of pi");


		//getting lists of estimate from the generator
		final int maxDigits = 6;
		EstimateGenerator.populate(maxDigits);
		Map<Integer, List<Estimate>> estimates = EstimateGenerator.get();

		//assign bands to separate series, then populate those series
		//while calculating x-axis value in-sito
		for (int i = 1; i <= maxDigits; i++) {	//for each digit band
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.setName(i + " digit" + (i != 1 ? "s" : ""));
			List<Estimate> bandEstimates = estimates.get(i);

			for (int j = 0; j < bandEstimates.size(); j++) {	//for every different denominator
				series.getData().add(new XYChart.Data<>(
					((double) j / (bandEstimates.size() - 1)), bandEstimates.get(j).absoluteCloseness()));
			}

			chart.getData().add(series);
		}

		//making legend toggle graph opacity and tooltips
		for (Node node : chart.getChildrenUnmodifiable()){
			if (node instanceof Legend) {
				Legend legend = (Legend) node;

				//match each LegendItem to each series on chart
				for (Legend.LegendItem legendItem : legend.getItems()) {
					for (XYChart.Series<Number, Number> series : chart.getData()) {
						if (legendItem.getText().equals(series.getName())) {

							legendItem.getSymbol().setCursor(Cursor.HAND);
							legendItem.getSymbol().setOnMouseClicked(event -> {
								if (event.getButton() == MouseButton.PRIMARY) {

									//TODO toggle opacity and mouseover
									series.getNode().setVisible(!series.getNode().isVisible());
								}
							});
							break;
						}
					}
				}
			}
		}

		chart.getStylesheets().add(Objects.requireNonNull(getClass().getResource("chart.css")).toExternalForm());
		Scene scene = new Scene(chart, 800, 600);
		stage.setScene(scene);
		stage.show();
	}


}
