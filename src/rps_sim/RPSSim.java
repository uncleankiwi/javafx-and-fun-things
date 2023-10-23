package rps_sim;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/*
	hBoxWrapper
		Sim
		vBoxButtons
			btnRandomize
			btnStart

 */
public class RPSSim extends Application {
	public static void main(String[] args) {
		launch();
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		VBox vBoxButtons = new VBox();
		Button btnRandomize = new Button("Randomize");
		Button btnStart = new Button("Start");
		vBoxButtons.getChildren().addAll(btnRandomize, btnStart);
		btnRandomize.setOnAction(x -> arena.init());
		btnStart.setOnAction(x -> arena.init());


		HBox hBoxWrapper = new HBox();


		hBoxWrapper.getChildren().addAll(arena, vBoxButtons);
		Scene scene = new Scene(hBoxWrapper);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private final Arena arena = new Arena();
}
