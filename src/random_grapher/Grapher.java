package random_grapher;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*

UI
------
vBoxWrapper
	hBoxSettings
		vBoxDistributionSettings			vBoxGraphSettings
			See below.							See Below.
	chart


Distribution settings
------------------------
distribution:							uniform/linear/sigmoid bell curve/normal/normal truncated
discrete/continuous						o		o		o					o		o
distribution uniformity below mode		-		o		o					-		-
distribution uniformity above mode		-		o		o					-		-
min value								o		o		o					-		o
mode value								-		o		o					o		o
max value								o		o		o					-		o
test									o		o		o					o		o

Graph settings
------------------
sample size
bucket size (if continuous)
 */

@SuppressWarnings("SpellCheckingInspection")
public class Grapher extends Application {
	final VBox vBoxWrapper;

	final HBox hBoxSettings;

	final VBox vBoxDistributionSettings;

	final VBox vBoxGraphSettings;

	final NumberAxis xAxis;
	final NumberAxis yAxis;
	final LineChart<Number,Number> chart;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage stage) {




		Scene scene = new Scene(vBoxWrapper, 800, 600);
		stage.setScene(scene);
		stage.show();
	}

	Grapher() {
		vBoxWrapper = new VBox();

		hBoxSettings = new HBox();

		vBoxDistributionSettings = new VBox();

		vBoxGraphSettings = new VBox();

		xAxis = new NumberAxis();
		yAxis = new NumberAxis();
		chart = new LineChart<>(xAxis,yAxis);
	}
}
