package pi_estimator;

import javafx.application.Application;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

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

		//

		stage.show();
	}


}
