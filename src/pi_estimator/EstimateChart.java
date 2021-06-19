package pi_estimator;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class EstimateChart extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {
		stage.setTitle("Pi estimator");

		final NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Closeness of estimate");
		final NumberAxis xAxis = new NumberAxis();
		xAxis.setLabel("Denominator");
		final LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
		chart.setTitle("Closeness of estimates of pi");



		//getting lists of estimate from the generator
		final int maxDigits = 4;
		EstimateGenerator.populate(maxDigits);
		List<Estimate> estimates = EstimateGenerator.get();
		List<Estimate> bestEstimates = EstimateGenerator.getBest();

		//get the number of points in each digit band's estimates
		//while also splitting each band into a separate list
		final int[] bandSize = new int[maxDigits];
		ArrayList<LinkedList<Integer>> closeness = new ArrayList<>();

		for (int i = 0; i < maxDigits; i++) {
			closeness.add(new LinkedList<>());
		}

		for (Estimate estimate : estimates) {
			int digits = estimate.digits() - 1;
			bandSize[digits]++;
			closeness.get(digits).add(estimate.absoluteCloseness());
		}

		//assign bands to separate series, then populate those series
		//while calculating x-axis value in-sito
		for (int i = 0; i < maxDigits; i++) {	//for each digit band
			XYChart.Series<Number, Number> series = new XYChart.Series<>();

			List<Integer> bandCloseness = closeness.get(i);
			for (int j = 0; j < bandCloseness.size(); j++) {	//for every different denominator
				series.getData().add(new XYChart.Data<>(
					(double) (j / (bandSize[i] - 1)), bandCloseness.get(j)));
			}

			chart.getData().add(series);
		}

		Scene scene = new Scene(chart, 800, 600);
		stage.setScene(scene);
		stage.show();
	}


}
