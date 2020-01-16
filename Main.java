package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			AnchorPane root = (AnchorPane)FXMLLoader.load(getClass().getResource("Sample.fxml"));
			Scene scene = new Scene(root,1200,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			

	        Canvas canvas = new Canvas(800, 300);
	        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
	        initDraw(graphicsContext);
	         
	        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, 
	                new EventHandler<MouseEvent>(){
	 
	            @Override
	            public void handle(MouseEvent event) {
	                graphicsContext.beginPath();
	                graphicsContext.moveTo(event.getX(), event.getY());
	                graphicsContext.stroke();
	            }
	        });
	         
	        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, 
	                new EventHandler<MouseEvent>(){
	 
	            @Override
	            public void handle(MouseEvent event) {
	                graphicsContext.lineTo(event.getX(), event.getY());
	                graphicsContext.stroke();
	            }
	        });
	 
	        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, 
	                new EventHandler<MouseEvent>(){
	 
	            @Override
	            public void handle(MouseEvent event) {
	 
	            }
	        });
	 
	    //    StackPane root1 = new StackPane();
	       root.getChildren().add(canvas);
	       canvas.setLayoutX(300);
	       canvas.setLayoutY(170);
	   //     Scene scene1 = new Scene(root1, 400, 400);
	   //     primaryStage.setTitle("java-buddy.blogspot.com");
	   //     primaryStage.setScene(scene);
	    //    primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
public static void main(String[] args) {
		launch(args);

}
     
    private void initDraw(GraphicsContext gc){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();
         
        gc.setFill(Color.LIGHTGRAY);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);
 
        gc.fill();
        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle
         
        gc.setFill(Color.RED);
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
         
    }
     
}
