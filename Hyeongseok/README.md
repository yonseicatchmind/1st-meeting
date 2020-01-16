Main.java
=========
```java
package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	
			 Parent root = FXMLLoader.load(getClass().getResource("Sample.fxml"));
			    Scene scene = new Scene(root);
			    primaryStage.setScene(scene);
			    primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
```
SampleController.java
=====================
```java
package application;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
public class SampleController implements Initializable {
	//>>>>>>>>>>>>>>>>>>>>>>>Other variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private GraphicsContext gcB,gcF;
    private boolean drawline = false,drawoval = false,drawrectangle = false,erase = false,freedesign = true;
    double startX, startY, lastX,lastY,oldX,oldY;
    double hg;
    //>>>>>>>>>>>>>>>>>>>>>>>FXML Variables<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML private RadioButton strokeRB,fillRB;
    @FXML private ColorPicker colorPick;
    @FXML private Canvas canvas;
    @FXML private Button rectButton,lineButton,ovlButton,pencButton;
    @FXML private Slider sizeSlider;
    //////////////////////////////////////////////////////////////////////////////
    @FXML
    private void onMousePressedListener(MouseEvent e){
        this.startX = e.getX();
        this.startY = e.getY();
        this.oldX = e.getX();
        this.oldY = e.getY();
    }
    @FXML
    private void onMouseDraggedListener(MouseEvent e){
        this.lastX = e.getX();
        this.lastY = e.getY();
        if(drawrectangle)
            drawRectEffect();
        if(drawoval)
            drawOvalEffect();
        if(drawline)
            drawLineEffect();
        if(freedesign)
            freeDrawing();
    }
    @FXML
    private void onMouseReleaseListener(MouseEvent e){
        if(drawrectangle)
            drawRect();
        if(drawoval)
            drawOval();
        if(drawline)
            drawLine();
    }
    @FXML
    private void onMouseExitedListener(MouseEvent event)
    {
        System.out.println("No puedes dibujar fuera del canvas");
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>Draw methods<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    private void drawOval()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());
        if(fillRB.isSelected()){
            gcB.setFill(colorPick.getValue());
            gcB.fillOval(startX, startY, wh, hg);
        }else{
            gcB.setStroke(colorPick.getValue());
            gcB.strokeOval(startX, startY, wh, hg);
        }
    }
    private void drawRect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcB.setLineWidth(sizeSlider.getValue());
        if(fillRB.isSelected()){
            gcB.setFill(colorPick.getValue());
            gcB.fillRect(startX, startY, wh, hg);
        }else{
            gcB.setStroke(colorPick.getValue());
            gcB.strokeRect(startX, startY, wh, hg);
        }
    }
    private void drawLine()
    {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.strokeLine(startX, startY, lastX, lastY);
    }
    private void freeDrawing()
    {
        gcB.setLineWidth(sizeSlider.getValue());
        gcB.setStroke(colorPick.getValue());
        gcB.strokeLine(oldX, oldY, lastX, lastY);
        oldX = lastX;
        oldY = lastY;
    }
    //////////////////////////////////////////////////////////////////////
    //>>>>>>>>>>>>>>>>>>>>>>>>>>Draw effects methods<<<<<<<<<<<<<<<<<<<<<<<
    private void drawOvalEffect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());
        if(fillRB.isSelected()){
            gcF.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gcF.setFill(colorPick.getValue());
            gcF.fillOval(startX, startY, wh, hg);
        }else{
            gcF.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeOval(startX, startY, wh, hg );
        }
       }
    private void drawRectEffect()
    {
        double wh = lastX - startX;
        double hg = lastY - startY;
        gcF.setLineWidth(sizeSlider.getValue());
        if(fillRB.isSelected()){
            gcF.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gcF.setFill(colorPick.getValue());
            gcF.fillRect(startX, startY, wh, hg);
        }else{
            gcF.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            gcF.setStroke(colorPick.getValue());
            gcF.strokeRect(startX, startY, wh, hg );
        }
    }
    private void drawLineEffect()
    {
        gcF.setLineWidth(sizeSlider.getValue());
        gcF.setStroke(colorPick.getValue());
        gcF.clearRect(0, 0, canvas.getWidth() , canvas.getHeight());
        gcF.strokeLine(startX, startY, lastX, lastY);
    }
    ///////////////////////////////////////////////////////////////////////
    @FXML 
    private void clearCanvas(ActionEvent e)
    {
        gcB.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gcF.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    //>>>>>>>>>>>>>>>>>>>>>Buttons control<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @FXML
    private void setOvalAsCurrentShape(ActionEvent e)
    {
        drawline = false;
        drawoval = true;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }
     @FXML
    private void setLineAsCurrentShape(ActionEvent e)
    {
        drawline = true;
        drawoval = false;
        drawrectangle = false;
        freedesign = false;
        erase = false;
    }
     @FXML
    private void setRectangleAsCurrentShape(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        freedesign = false;
        erase=false;
        drawrectangle = true;
    }
    @FXML
    private void setErase(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        drawrectangle = false;    
        erase = true;
        freedesign= false;
    }
    @FXML
    private void setFreeDesign(ActionEvent e)
    {
        drawline = false;
        drawoval = false;
        drawrectangle = false;    
        erase = false;
        freedesign = true;
    }
    //////////////////////////////////////////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gcB = canvas.getGraphicsContext2D();
        gcF = canvas.getGraphicsContext2D();
        sizeSlider.setMin(1);
        sizeSlider.setMax(50);
        //////////////////////////////////
        Image imageRect = new Image(getClass().getResourceAsStream("Stop-32.png"));
        ImageView icR = new ImageView(imageRect);
        icR.setFitWidth(32);
        icR.setFitHeight(32);
        rectButton.setGraphic(icR);  
        Image imageLinea = new Image(getClass().getResourceAsStream("Ruler-32.png"));
        ImageView icLin = new ImageView(imageLinea);
        icLin.setFitWidth(32);
        icLin.setFitHeight(32);
        lineButton.setGraphic(icLin);
        Image imageOvalo = new Image(getClass().getResourceAsStream("Chart-32.png"));
        ImageView icOval = new ImageView(imageOvalo);
        icOval.setFitWidth(32);
        icOval.setFitHeight(32);
        ovlButton.setGraphic(icOval);
        Image imageLapiz = new Image(getClass().getResourceAsStream("Pencil-32.png"));
        ImageView icLapiz = new ImageView(imageLapiz);
        icLapiz.setFitWidth(32);
        icLapiz.setFitHeight(32);
        pencButton.setGraphic(icLapiz);
    }
}
```

Sample.fxml
===========
```fxml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.Cursor?>
<?import javafx.scene.canvas.Canvas?>
<?import javafx.scene.control.Button?>
<?import javafx.scene.control.ColorPicker?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.control.MenuButton?>
<?import javafx.scene.control.MenuItem?>
<?import javafx.scene.control.PasswordField?>
<?import javafx.scene.control.RadioButton?>
<?import javafx.scene.control.TextArea?>
<?import javafx.scene.control.TextField?>
<?import javafx.scene.control.ToggleButton?>
<?import javafx.scene.control.ToolBar?>
<?import javafx.scene.effect.ColorAdjust?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.HBox?>
<?import javafx.scene.shape.Rectangle?>
<?import javafx.scene.text.Font?>
<AnchorPane fx:id="result" prefHeight="800.0" prefWidth="1280.0" xmlns="http://javafx.com/javafx/8.0.171" xmlns:fx="http://javafx.com/fxml/1" fx:controller="application.SampleController">
   <children>
      <TextField fx:id="Answer" layoutX="549.0" layoutY="14.0" prefHeight="30.0" prefWidth="320.0" />
      <TextField fx:id="Talk" layoutX="247.0" layoutY="749.0" prefHeight="29.0" prefWidth="1019.0" />
      <Canvas fx:id="canvas" height="423.0" layoutX="247.0" layoutY="69.0" onMouseDragged="#onMouseDraggedListener" onMouseExited="#onMouseExitedListener" onMousePressed="#onMousePressedListener" onMouseReleased="#onMouseReleaseListener" width="1019.0" />
      <HBox layoutX="247.0" layoutY="69.0" prefHeight="45.0" prefWidth="896.0">
         <children>
            <ToolBar cacheHint="SPEED" maxHeight="-Infinity" maxWidth="-Infinity" prefHeight="43.0" prefWidth="969.0" snapToPixel="false">
              <items>
                <Button fx:id="pencButton" mnemonicParsing="false" onAction="#setFreeDesign" prefWidth="80.0" text="Pencil">
                     <font>
                        <Font name="Cambria Math" size="15.0" />
                     </font>
                  </Button>
                  <Button fx:id="rectButton" mnemonicParsing="false" text="Button" />
                  <Button fx:id="lineButton" mnemonicParsing="false" text="Button" />
                  <Button fx:id="ovlButton" mnemonicParsing="false" text="Button" />
                  <Button fx:id="eraser" mnemonicParsing="false" onAction="#setErase" prefWidth="80.0" text="Eraser" />
                  <MenuButton mnemonicParsing="false" prefWidth="80.0" text="MenuButton">
                    <items>
                      <MenuItem mnemonicParsing="false" text="12pt" />
                      <MenuItem mnemonicParsing="false" text="14pt" />
                        <MenuItem mnemonicParsing="false" text="Unspecified Action" />
                        <MenuItem mnemonicParsing="false" text="Unspecified Action" />
                        <MenuItem mnemonicParsing="false" text="Unspecified Action" />
                        <MenuItem mnemonicParsing="false" text="Unspecified Action" />
                    </items>
                  </MenuButton>
                  <ColorPicker fx:id="colorpick" />
                  <RadioButton fx:id="fillRB" mnemonicParsing="false" text="RadioButton" />
                  <RadioButton fx:id="strokeRB" mnemonicParsing="false" text="RadioButton" />
              </items>
            </ToolBar>
         </children>
      </HBox>
      <TextArea fx:id="TalkBoard" layoutX="247.0" layoutY="502.0" prefHeight="230.0" prefWidth="1019.0" />
      <TextArea fx:id="RankList" layoutX="14.0" layoutY="548.0" prefHeight="230.0" prefWidth="197.0" />
      <Label layoutX="14.0" layoutY="502.0" prefHeight="43.0" prefWidth="197.0" text="Rank" />
      <ListView layoutX="15.0" layoutY="314.0" prefHeight="210.0" prefWidth="197.0" />
      <Label layoutX="14.0" layoutY="267.0" text="People List" textOverrun="CLIP" />
      <Rectangle arcWidth="5.0" blendMode="ADD" fill="BLUE" height="191.0" layoutX="13.0" layoutY="69.0" smooth="false" stroke="BLACK" strokeLineCap="ROUND" strokeLineJoin="ROUND" strokeMiterLimit="0.0" strokeType="INSIDE" width="197.0">
         <cursor>
            <Cursor fx:constant="CROSSHAIR" />
         </cursor>
      </Rectangle>
      <PasswordField fx:id="ID" layoutX="27.0" layoutY="85.0" />
      <ToggleButton fx:id="Loginout" layoutX="27.0" layoutY="165.0" mnemonicParsing="false" prefHeight="29.0" prefWidth="172.0" text="LogIn/Out" />
      <ListView fx:id="Score" layoutX="26.0" layoutY="124.0" prefHeight="29.0" prefWidth="172.0" />
   </children>
   <effect>
      <ColorAdjust />
   </effect>
</AnchorPane>
```
