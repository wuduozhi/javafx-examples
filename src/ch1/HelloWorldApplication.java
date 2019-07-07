package ch1;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class HelloWorldApplication  extends Application{
    public HelloWorldApplication() {
        super();
    }

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("Inside init() method! Perform necessary initializations here.");
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Inside stop() method! Destroy resources. Perform Cleanup.");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("I am wuduozhi");
        label.setAlignment(Pos.CENTER);
        primaryStage.setScene(new Scene(label,300,400));
        primaryStage.setTitle("Hello World,JavaFX");
        primaryStage.show();
    }

    /*
    	If we include the main() method then we need to call Application.launch()
    	for launching the JavaFX application
    */
    public static void main(String[] args) {
        launch(args);
    }
}
