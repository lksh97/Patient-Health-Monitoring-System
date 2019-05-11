/*
Created By : Vaibhav Shukla
*/
package phms;

import ConnectionUtil.ConnectionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.stage.Stage;

public class FXMLPersonController implements Initializable{
    
    @FXML
    ObservableList<String> genderBoxList = FXCollections.observableArrayList("Male","Female","Others");

    @FXML
    public Label myLabel;
    
    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField mobile;

    @FXML
    private TextField occupation;

    @FXML
    private TextArea address;

    @FXML
    private Button next;
    
    @FXML
    private ComboBox<String> genderBox;
    
    @FXML
    private DatePicker date = new DatePicker();
        
    @FXML
    static File f;
    
    Stage dialogStage = new Stage();
    Scene scene;
       
    public Connection con = null;
    public PreparedStatement st = null;
    public ResultSet rs = null;
    public Statement statement = null;


    public FXMLPersonController() {
        con = ConnectionUtil.connectdb();
    }
    
    
    @FXML
    void Upload (ActionEvent event) throws IOException,SQLException {
        FileChooser fc = new FileChooser();
       // fc.getExtensionFilters().add(new ExtensionFilter("PDF Files", "*.pdf"));
        f = fc.showOpenDialog(null);
        if(f!=null)
            myLabel.setText(f.getName()); 
        else
            myLabel.setText("File Not Selected");
}  
    @FXML
    void move(ActionEvent event) throws IOException,SQLException {
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLMenu.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
    }
    
    @SuppressWarnings("empty-statement")
    public void goBack(ActionEvent event) throws Exception {
        if(validateFields()){
            String query = "INSERT INTO precord(FIRST, Last, Gender, Mobile, Occupation, Address, Medical_record, Appoint) VALUES (?,?,?,?,?,?,?,?)";            
            st = (PreparedStatement)con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
            st.setString(1, fname.getText());
            st.setString(2, lname.getText());
            String value;
            value = genderBox.getSelectionModel().getSelectedItem();
            st.setString(3, value);
            st.setString(4, mobile.getText());
            st.setString(5, occupation.getText());
            st.setString(6, address.getText());
            String filePath = f.getAbsolutePath();
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            st.setBinaryStream(7, inputStream);
            st.setString(8,((TextField)date.getEditor()).getText());
            st.executeUpdate();
            
            String ACCOUNT_SID =
            "ACb1e440ceb2b9299d3fcf4634714d024a";
            String AUTH_TOKEN =
            "87c3b383366f68950818d1cc0f2842c5";
            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
            Message message = Message
                .creator(new PhoneNumber("+91"+mobile.getText()), 
                        new PhoneNumber("+15163070018"), 
                        "\nWelcome to Vaibhav Hospital.\nWe will notify you on the date of your Appointment.")
                .create();
                        
            
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLMenu.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
            st.close();
            con.close();
              
        }
}
        private boolean validateFields(){
        if(occupation.getText().isEmpty() | lname.getText().isEmpty() | mobile.getText().isEmpty() | fname.getText().isEmpty() | address.getText().isEmpty() | genderBox.getSelectionModel().isEmpty() | date.getValue() == null | f==null){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("Please Enter into the fields");
        alert.setTitle("Validate Fields");
        alert.setHeaderText(null);
        alert.showAndWait();
        
        return false;
    }
       return true;          
}
    
    @FXML
    void logOut(ActionEvent event) throws IOException {
         Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        genderBox.setItems(genderBoxList);       
    }

     
     
    

}
