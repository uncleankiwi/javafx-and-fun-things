package jfx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/*
primaryStage
	scene
		rtgWrapper
			rtgWrapperLeft
				txtInput
				txtOutput	- HTMLEditor
			rtgWrapperRight
				lstColours - ColourList
					colorItem - ColorItem
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

	public static String colourText(String input, List<Color> palette) {
		StringBuilder output = new StringBuilder();

		//if there's only one colour, just apply it to everything
		if (palette.size() == 1) {
			output = new StringBuilder(applySpan(input, palette.get(0)));
		}
		else {
			//count non-space characters
			int nonSpaceChars = 0;
			Pattern pattern = Pattern.compile("[\\S]");	//i.e. non-whitespace character
			String[] strings = input.split("");
			for (String s : strings) {
				if (pattern.matcher(s).matches()) nonSpaceChars++;
			}
			if (nonSpaceChars == 0) return input;

			//Create a gradient of colours:
			//1. Assign doubles to every character in input, ranging from 0 to 1
			//2. Do the same for every colour in palette
			//3. Compare those positions to calculate resulting gradient colours
			//This won't account for floating point errors. Instead, it'll just assign the first and last
			//characters to the first and last colours.
			List<Color> gradient = new ArrayList<>();
			if (nonSpaceChars <= palette.size()) gradient = palette;
			else {
				double letterSpacing = 1 / ((double) nonSpaceChars - 1);
				double paletteSpacing = 1 / ((double) palette.size() - 1);
				double paletteLeftPosition = 0;
				int paletteLeftIndex = 0;

				for (int i = 0; i < nonSpaceChars; i++) {
					//hard-assigning first and last characters to first and last colours
					if (i == 0) gradient.add(palette.get(0));
					else if (i == nonSpaceChars - 1) gradient.add(palette.get(palette.size() - 1));
					else {
						double characterPosition = i * letterSpacing;
						//check if moving to the next colour in the palette
						if (paletteLeftPosition + paletteSpacing < characterPosition) {
							paletteLeftPosition += paletteSpacing;
							paletteLeftIndex++;
						}

						//calculating a particular colour based on distance from two nearest colours
						Color colorLeft = palette.get(paletteLeftIndex);
						Color colorRight = palette.get(paletteLeftIndex + 1);
						double tempRed = colorLeft.getRed() * (paletteLeftPosition + paletteSpacing - characterPosition) / paletteSpacing
								+ colorRight.getRed() * (characterPosition - paletteLeftPosition) / paletteSpacing;
						double tempGreen = colorLeft.getGreen() * (paletteLeftPosition + paletteSpacing - characterPosition) / paletteSpacing
								+ colorRight.getGreen() * (characterPosition - paletteLeftPosition) / paletteSpacing;
						double tempBlue = colorLeft.getBlue() * (paletteLeftPosition + paletteSpacing - characterPosition) / paletteSpacing
								+ colorRight.getBlue() * (characterPosition - paletteLeftPosition) / paletteSpacing;
						Color color = new Color(tempRed, tempGreen, tempBlue, 1f);
						gradient.add(color);
					}

					//TODO: refactor into own method, add algorithm switching
				}
			}

			//apply gradient
			for (int i = 1; i <= nonSpaceChars; i++) {
				if (pattern.matcher(strings[i - 1]).matches()) {
					output.append(applySpan(strings[i - 1], gradient.get(i - 1)));
				}
				else {
					output.append(strings[i - 1]);
				}

			}
		}

		return output.toString();
	}

	private static String applySpan(String input, Color colour) {
		String output = "<span style=\"color: " + colourToHex(colour) + ";\">";
		output += input;
		output += "</span>";
		return output;
	}

	private static String doubleToHex(double d) {
		String string = Integer.toHexString((int) Math.round(d * 255));
		return string.length() == 1 ? "0" + string : string;
	}

	private static String colourToHex(Color colour) {
		return "#" + (
				doubleToHex(colour.getRed()) +
				doubleToHex(colour.getGreen()) +
				doubleToHex(colour.getBlue()) +
				doubleToHex(colour.getOpacity()))
				.toUpperCase();
	}
}

class RtgUI extends HBox {
	TextArea txtInput = new TextArea();
	HTMLEditor txtOutput = new HTMLEditor();
	ColourList colourList = new ColourList();
	RadioButton radRGB = new RadioButton("RGB");
	RadioButton radHSB = new RadioButton("HSB");
	ToggleGroup toggleGroup = new ToggleGroup();
	Button btnColour = new Button("Colour!");


	RtgUI () {
		txtInput.setPrefWidth(500);
		txtInput.setPrefHeight(250);

		txtOutput.setPrefWidth(500);
		txtOutput.setPrefHeight(250);

		radRGB.setToggleGroup(toggleGroup);
		radHSB.setToggleGroup(toggleGroup);

		btnColour.setOnAction(event -> {
			List<Color> palette = colourList.getPalette();
			String input = txtInput.getText();
			txtOutput.setHtmlText(RichTextGradient.colourText(input, palette));
		});

		VBox rtgWrapperLeft = new VBox();
		VBox rtgWrapperRight = new VBox();

		rtgWrapperLeft.getChildren().addAll(txtInput, txtOutput);
		rtgWrapperRight.getChildren().addAll(colourList, radRGB, radHSB, btnColour);

		this.getChildren().addAll(rtgWrapperLeft, rtgWrapperRight);
	}
}

class ColourList extends VBox {
	List<ColourItem> colourItems;

	ColourList() {
		colourItems = new ArrayList<>();

		addColour(0);
	}

	public List<Color> getPalette() {
		List<Color> colours = new ArrayList<>();
		colourItems.forEach(x -> colours.add(x.getColour()));
		return colours;
	}

	//adds it 1 after the index if the list isn't currently empty
	void addColour(int index) {
		if (this.colourItems.size() == 0) index = 0;
		else index++;

		ColourItem colourItem = new ColourItem(this, index);
		colourItems.add(index, colourItem);

		refresh();
	}

	//always leave at least 1 colour in the list
	void removeColour(int index) {
		if (this.colourItems.size() <= 1) return;
		else colourItems.remove(index);

		refresh();
	}

	private void refresh() {
		this.getChildren().clear();
		this.getChildren().addAll(this.colourItems);
	}

}

class ColourItem extends HBox {
	Button btnAddColour = new Button("+");
	Button btnRemoveColour = new Button("-");
	ColorPicker colourPicker = new ColorPicker();
	ColourList parent;
	Color colour;
	int index;

	ColourItem(ColourList parent, int index) {
		this.index = index;
		this.parent = parent;

		this.colour = Color.BLACK;
		colourPicker.setValue(this.colour);

		btnAddColour.setOnAction(event -> parent.addColour(this.index));
		btnRemoveColour.setOnAction(event -> parent.removeColour(this.index));
		colourPicker.setOnAction(event -> this.colour = colourPicker.getValue());

		this.getChildren().addAll(btnAddColour, btnRemoveColour, colourPicker);
	}

	public Color getColour() {
		return this.colour;
	}


}