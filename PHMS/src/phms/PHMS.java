/*
Created By : Vaibhav Shukla
*/
package phms;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PHMS extends Application {
    public static Stage stage;
    @Override
    public void start(Stage stage) throws Exception {
        this.stage=stage;
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        stage.setTitle("Patient Health Monitoring System");
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        PHMS.stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
