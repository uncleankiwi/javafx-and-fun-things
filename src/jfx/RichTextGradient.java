package jfx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
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

	public static String colourText(String input, List<Color> palette, AlgorithmType algorithmType) {
		StringBuilder output = new StringBuilder();

		//if there's only one colour, just apply it to everything
		//still have to loop through it to replace whitespaces with <br /> and such though
		if (palette.size() == 1) {
			for (char c : input.toCharArray()) {
				String str = replaceWhiteSpace(String.valueOf(c));
				output.append(str);
			}
			output = new StringBuilder(applySpan(output.toString(), palette.get(0)));
		}
		else {
			//count non-space characters
			int nonSpaceChars = 0;
			Pattern pattern = Pattern.compile("[\\S]");	//i.e. non-whitespace character

			Matcher matcher = pattern.matcher(input);
			while(matcher.find()) nonSpaceChars++;

			//alternative for counting non-space characters:
//			String[] strings = input.split("");
//			for (String s : strings) {
//				if (pattern.matcher(s).matches()) nonSpaceChars++;
//			}
//			if (nonSpaceChars == 0) return input;

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
						Color color = getColor(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft, colorRight, algorithmType);
						gradient.add(color);
					}
				}
			}

			int k = 0;
			for (char c : input.toCharArray()) {
				String str = String.valueOf(c);
				if (pattern.matcher(str).matches()) {
					output.append(applySpan(str, gradient.get(k)));
					k++;
				}
				else {
					output.append(replaceWhiteSpace(str));
				}
			}
		}

		return output.toString();
	}

	private static Color getColor(double paletteSpacing, double paletteLeftPosition, double characterPosition,
								  Color colorLeft, Color colorRight, AlgorithmType algorithmType) {
		Color color = null;

		if (algorithmType == AlgorithmType.RGB) {
			double tempRed =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getRed(), colorRight.getRed());
			double tempGreen =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getGreen(), colorRight.getGreen());
			double tempBlue =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getBlue(), colorRight.getBlue());
			color = new Color(tempRed, tempGreen, tempBlue, 1f);
		}
		else if (algorithmType == AlgorithmType.HSB) {
			//jfx color hue: 0 - 360
			//jfx color saturation/brightness: 0 - 1
			//java.awt.Color: all 3 parameters are 0 - 1

			double tempHue =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getHue(), colorRight.getHue()) / 360;
			double tempSaturation =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getSaturation(), colorRight.getSaturation());
			double tempBrightness =
					calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, colorLeft.getBrightness(), colorRight.getBrightness());

			java.awt.Color awtColour = new java.awt.Color(
					java.awt.Color.HSBtoRGB((float) tempHue, (float) tempSaturation, (float) tempBrightness));

			float[] awtColourArray = awtColour.getRGBColorComponents(null);
			color = new Color(awtColourArray[0], awtColourArray[1], awtColourArray[2], 1f);
		}
		else if (algorithmType == AlgorithmType.CMYK) {
			double[] cmykLeft = RGBToCMYK(colorLeft);
			double[] cmykRight = RGBToCMYK(colorRight);
			double[] cmyk = new double[4];

			for (int i = 0; i < 4; i++){
				cmyk[i] = calcNewColourValue(paletteSpacing, paletteLeftPosition, characterPosition, cmykLeft[i], cmykRight[i]);
			}
			color = CMYKToRGB(cmyk);
		}

		return color;
	}

	private static double calcNewColourValue(double paletteSpacing, double paletteLeftPosition, double characterPosition,
											 double colourLeftVal, double colourRightVal) {
		return colourLeftVal * (paletteLeftPosition + paletteSpacing - characterPosition) / paletteSpacing
				+ colourRightVal * (characterPosition - paletteLeftPosition) / paletteSpacing;
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

	private static String replaceWhiteSpace(String str) {
		if (str.equals(" ")) return "&nbsp;";
		else if (str.equals("\n")) return "<br />";
		else return str;
	}

	//an approximation of cmyk. For more accurate results, use a colour space
	private static double[] RGBToCMYK(Color rgb) {
		double white = Math.max(Math.max(rgb.getRed(), rgb.getGreen()), rgb.getBlue());
		if (white == 0) {
			return new double[] {0, 0, 0, 1};
		}
		else {
			double cyan = (white - rgb.getRed()) / white;
			double magenta = (white - rgb.getGreen()) / white;
			double yellow = (white - rgb.getBlue()) / white;
			return new double[] {cyan, magenta, yellow, 1 - white};
		}
	}

	//again, another approximation of cmyk.
	private static Color CMYKToRGB(double[] cmyk) {
		double red = (1 - cmyk[0]) * (1 - cmyk[3]);
		double green = (1 - cmyk[1]) * (1 - cmyk[3]);
		double blue = (1 - cmyk[2]) * (1 - cmyk[3]);
		return new Color(red, green, blue, 1f);
	}
}

class RtgUI extends HBox {
	final TextArea txtInput = new TextArea();
	final HTMLEditor txtOutput = new HTMLEditor();
	final ColourList colourList = new ColourList();
	final RadioButton radRGB = new RadioButton("RGB");
	final RadioButton radHSB = new RadioButton("HSB");
	final RadioButton radCMYK = new RadioButton("CMYK");
	final ToggleGroup toggleGroup = new ToggleGroup();
	final Button btnColour = new Button("Colour!");
	private static final Insets INSETS = new Insets(5);
	private static final String CSS =
			"-fx-padding: 5px;" +
			"-fx-border-insets: 5px;" +
			"-fx-background-insets: 5px;";


	RtgUI () {
		this.setPadding(INSETS);

		txtInput.setPrefWidth(500);
		txtInput.setPrefHeight(250);
		txtInput.setStyle(CSS);

		txtOutput.setPrefWidth(500);
		txtOutput.setPrefHeight(300);
		txtOutput.setPadding(INSETS);
		txtOutput.setStyle(CSS);

		colourList.setStyle(CSS);
		radRGB.setToggleGroup(toggleGroup);
		radHSB.setToggleGroup(toggleGroup);
		radCMYK.setToggleGroup(toggleGroup);
		radRGB.setSelected(true);
		VBox radioGroup = new VBox();
		radioGroup.getChildren().addAll(radRGB, radHSB, radCMYK);
		radioGroup.setStyle(CSS);


		btnColour.setOnAction(event -> {
			List<Color> palette = colourList.getPalette();
			String input = txtInput.getText();
			AlgorithmType algorithmType = null;
			if (radRGB.isSelected()) algorithmType = AlgorithmType.RGB;
			else if (radHSB.isSelected()) algorithmType = AlgorithmType.HSB;
			else if (radCMYK.isSelected()) algorithmType = AlgorithmType.CMYK;

			txtOutput.setHtmlText(RichTextGradient.colourText(input, palette, algorithmType));
		});

		VBox rtgWrapperLeft = new VBox();
		VBox rtgWrapperRight = new VBox();
		rtgWrapperRight.setPadding(INSETS);

		rtgWrapperLeft.getChildren().addAll(txtInput, txtOutput);
		rtgWrapperRight.getChildren().addAll(colourList, radioGroup, btnColour);

		this.getChildren().addAll(rtgWrapperLeft, rtgWrapperRight);
	}
}

class ColourList extends VBox {
	final List<ColourItem> colourItems;

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
		//recalculate colourItem indices
		for (int i = 0; i < colourItems.size(); i++) {
			colourItems.get(i).setIndex(i);
		}

		//refresh parent pane's children
		this.getChildren().clear();
		this.getChildren().addAll(this.colourItems);
	}

}

class ColourItem extends HBox {
	final Button btnAddColour = new Button("+");
	final Button btnRemoveColour = new Button("-");
	final ColorPicker colourPicker = new ColorPicker();
	final ColourList parent;
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

	public void setIndex(int index) {
		this.index = index;
	}

}

enum AlgorithmType {
	RGB,
	HSB,
	CMYK
}