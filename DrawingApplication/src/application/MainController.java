package application;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;

import java.awt.MouseInfo;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

public class MainController implements Initializable{
	
	
	@FXML
	private HBox hBox;
	@FXML
	private VBox vBox;
	//Contains HBox
	@FXML
	private ColorPicker colorSelector;
	//GUI Element - user picks color
	@FXML
	private ComboBox<String> shapeSelector;
	//GUI Element - user picks brush shape
	@FXML
	private ComboBox<String> brushSizeSelector;
	//GUI Element - user picks brush size
	@FXML
	private CheckBox eraseToggle;
	//GUI Element - user picks if erasing or not
	@FXML
	private Button saveButton;
	@FXML
	private Canvas canvas = new Canvas();
	//Canvas on which the shapes are drawn

	ObservableList<String> shapeList = FXCollections.observableArrayList("Circle","Square","Triangle","Hexagon");
	ObservableList<String> sizeList = FXCollections.observableArrayList("1","2","3","4","5","10","20","50","100");
	//Initializing the Lists with their elements
	
	public static Color brushColor = Color.BLACK;
	public static String brushShape = "Circle";
	public static boolean erase = false;
	public static String brushSize = "5";
	public static double mouseX;
	public static double mouseY;
	//Variables used by GUI elements and drawOnClick function
	
	
	@FXML
	public void setBrushSize(ActionEvent event) {
		brushSize = brushSizeSelector.getValue();
	}@FXML
	public void setColor(ActionEvent event) {
		brushColor = colorSelector.getValue();
	}@FXML
	public void setShape(ActionEvent event) {
		brushShape = shapeSelector.getValue();
	}@FXML
	public void toggleErase(ActionEvent event) {
		if(eraseToggle.isSelected()) {
			erase = true;
		}
		else {
			erase = false;
		}
	}
	
	
	@FXML
	public void drawOnClick(MouseEvent event) {
		GraphicsContext context = canvas.getGraphicsContext2D();
		double size = Integer.parseInt(brushSize);
		Color usedColor;
		if(erase == true) usedColor = Color.WHITE;
		else usedColor = brushColor;
		context.setFill(usedColor);
		mouseX = event.getX();
		mouseY = event.getY();
		switch(MainController.brushShape) {
		case "Circle":
			context.fillOval(mouseX-size*5,mouseY-size*5,size*10,size*10);
			break;
		case "Square":
			context.fillRect(mouseX-size*5, mouseY-size*5, size*10, size*10);
			break;
		case "Triangle":
			context.fillPolygon(new double[] {mouseX,mouseX+size*5,mouseX-size*5}, new double[] {mouseY+size*10*Math.sqrt(3)/3, mouseY-(size*10*Math.sqrt(3)/6), mouseY-(size*10*Math.sqrt(3)/6)}, 3);
			break;
		case "Hexagon":
			context.fillPolygon(new double[] {mouseX-size*10*Math.sqrt(3)/3,mouseX-size*10,mouseX-size*10*Math.sqrt(3)/3,mouseX+size*10*Math.sqrt(3)/3,mouseX+size*10,mouseX+size*10*Math.sqrt(3)/3},
					new double[] {mouseY+size*10,mouseY,mouseY-size*10,mouseY-size*10,mouseY,mouseY+size*10},
					6);
			break;
		}
	}

	
	@FXML
	public void saveDrawing(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(((Node)event.getSource()).getScene().getWindow());
		if(file != null) {
			try {
				WritableImage writableimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
				canvas.snapshot(null, writableimage);
				RenderedImage renderedimage = SwingFXUtils.fromFXImage(writableimage, null);
				ImageIO.write(renderedimage,"png",file);
			} catch(IOException ex) {
				Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		shapeSelector.setItems(shapeList);
		brushSizeSelector.setItems(sizeList);
	}
}