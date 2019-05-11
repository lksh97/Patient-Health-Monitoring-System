/*
Created By : Vaibhav Shukla
*/
package phms;

import ConnectionUtil.ConnectionUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {
    
   @FXML
    private JFXTextField userr;

    @FXML
    private JFXPasswordField password;

    @FXML
    private JFXButton login;

    @FXML
    private JFXButton sign;
    
    
    Stage dialogStage = new Stage();
    Scene scene;
        
    public Connection con = null;
    public PreparedStatement st = null;
    public ResultSet rs = null;
    
    public FXMLDocumentController() {
        con = ConnectionUtil.connectdb();
    }
    
      @FXML
    void pas(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            password.requestFocus();
        }
    }
    
    @FXML
    void log(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            try{
           String query = "select username,password from login where username=? and password=?";
           st = (PreparedStatement)con.prepareStatement(query);
           st.setString(1, userr.getText());
           st.setString(2, password.getText());
           rs = (ResultSet)st.executeQuery();      
           if(!rs.next()){
                infoBox("Please enter correct Email and Password", null, "Failed");
            }else{
                
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLMenu.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();
            }
           st.close();
           rs.close();
       }
       catch(Exception e){
           e.printStackTrace();
       }
        }
    }


    @FXML
    void makeLogin(ActionEvent event) {
        try{
           String query = "select username,password from login where username=? and password=?";
           st = (PreparedStatement)con.prepareStatement(query);
           st.setString(1, userr.getText());
           st.setString(2, password.getText());
           rs = (ResultSet)st.executeQuery();      
           if(!rs.next()){
                infoBox("Please enter correct Email and Password", null, "Failed");
            }else{
                
                Node node = (Node)event.getSource();
                dialogStage = (Stage) node.getScene().getWindow();
                dialogStage.close();
                scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLMenu.fxml")));
                dialogStage.setScene(scene);
                dialogStage.show();
            }
           st.close();
           rs.close();
       }
       catch(Exception e){
           e.printStackTrace();
       }
    }
 

    @FXML
    void newUser(ActionEvent event){
        try{
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLUser.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
        }
        catch(Exception e){
           e.printStackTrace();
       }
    }
    
 
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    
}
