/*
Created By : Vaibhav Shukla
*/
package phms;


import ConnectionUtil.ConnectionUtil;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class FXMLCreateController implements Initializable{
    
    @FXML
    private TextField name;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Button create;

    Stage dialogStage = new Stage();
    Scene scene;
        
    public Connection con = null;
    public PreparedStatement st = null;
    public ResultSet rs = null;

    public FXMLCreateController() {
        con = ConnectionUtil.connectdb();
    }
 
    @FXML
    void createUser(ActionEvent event) throws SQLException {
    try{
           String query = "INSERT INTO login(username,password,name) VALUES (?,?,?)";
           st = (PreparedStatement)con.prepareStatement(query);
           st.setString(3, name.getText());
           st.setString(1, username.getText());
           st.setString(2, password.getText());
           st.executeUpdate(); 
           infoBox("Welcome : "+name.getText(),null,"Success" );
           Node node = (Node)event.getSource();
           dialogStage = (Stage) node.getScene().getWindow();
           dialogStage.close();
           scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")));
           dialogStage.setScene(scene);
           dialogStage.show();
       }
        catch(Exception e){
           e.printStackTrace();
       }
        finally{
            st.close();
    }
    }
    
    @FXML
    void goBack(ActionEvent event) {
    try{
           Node node = (Node)event.getSource();
           dialogStage = (Stage) node.getScene().getWindow();
           dialogStage.close();
           scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")));
           dialogStage.setScene(scene);
           dialogStage.show();
       }
        catch(Exception e){
           e.printStackTrace();
       }
    }
    
     public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
}

   
