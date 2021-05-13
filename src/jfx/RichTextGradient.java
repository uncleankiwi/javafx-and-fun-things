package jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/*
primaryStage
	scene
		rtgWrapper
			rtgWrapperLeft
				txtInput
				txtOutput	- textflow
			rtgWrapperRight
				lstColours
					btnAddColour
					btnRemoveColour
					colorPicker	- ColorPicker
				radRGB
				radHSB
				btnColour

*/

public class RichTextGradient extends Application {
	@Override
	public void start(Stage primaryStage) {
		RtgUI rtgUI = new RtgUI();
		Scene scene = new Scene(rtgUI, 700, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Rich text gradient");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}

class RtgUI extends HBox {


	RtgUI () {

	}
}