/*
Created By : Vaibhav Shukla
*/
package phms;

import ConnectionUtil.ConnectionUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;  
import java.util.Date;  


public class FXMLEditController implements Initializable {
    
    @FXML
    private int Iid;
    
    @FXML
    ObservableList<String> genderBoxList = FXCollections.observableArrayList("Male","Female","Others");

    @FXML
    private TextField fname;

    @FXML
    private TextField lname;

    @FXML
    private TextField mobile;

    @FXML
    private TextField occupation;

    @FXML
    private ComboBox<String> genderBox;

    @FXML
    private TextArea address;
    
    @FXML
    private Label location;
    
    @FXML
    static File f;
    
    @FXML
    private DatePicker date = new DatePicker();  
    
    Stage dialogStage = new Stage();
    Scene scene;
        
    public Connection con = null;
    public PreparedStatement st = null;
    public ResultSet rs = null;
    public int check1, check2;

    public FXMLEditController() {
        con = ConnectionUtil.connectdb();
    }
    
    
    @FXML
    void Upload (ActionEvent event) throws IOException,SQLException {
        FileChooser fc = new FileChooser();
        f = fc.showOpenDialog(null);
        if(f!=null){
            location.setText(f.getName()); 
            String query = "UPDATE precord set Medical_record=? WHERE ID="+Iid;
            st = (PreparedStatement)con.prepareStatement(query);
            String filePath = f.getAbsolutePath();
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            st.setBinaryStream(1, inputStream);
            check2 = st.executeUpdate();
            inputStream.close();
            st.close();
        }
        else
            location.setText("File Not Selected");
} 
    
    @FXML
    @SuppressWarnings("empty-statement")
    void save(ActionEvent event) throws IOException,SQLException{
        if(validateFields()){
            String query = "UPDATE precord set FIRST=?, Last=?, Gender=?, Mobile=?, Occupation=?, Address=?, Appoint=? WHERE ID="+Iid;
            st = (PreparedStatement)con.prepareStatement(query);
            st.setString(1, fname.getText());
            st.setString(2, lname.getText());
            String value;
            value = genderBox.getSelectionModel().getSelectedItem();
            st.setString(3,value);
            st.setString(4, mobile.getText());
            st.setString(5, occupation.getText());
            st.setString(6, address.getText());
            st.setString(7,((TextField)date.getEditor()).getText());
            check1 = st.executeUpdate(); 
            infoBox("User Details has been updated.",null,"Success" );

            st.close();
        }
    }
    
    private boolean validateFields(){
        if(occupation.getText().isEmpty() | lname.getText().isEmpty() | mobile.getText().isEmpty() | fname.getText().isEmpty() | address.getText().isEmpty() | genderBox.getSelectionModel().isEmpty() | date.getValue() == null){
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
    void back(ActionEvent event) {
        if(check1>0||check2>0)
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Details has been Changed.\nkindly close the window to exit.");
            alert.setTitle("Change Alert");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
        else{
            Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
        }
    }
    
    @FXML
    void mRecord(ActionEvent event) throws IOException,SQLException{
        String query = "SELECT Medical_record FROM precord WHERE id="+Iid;
        st = (PreparedStatement)con.prepareStatement(query);
        rs = st.executeQuery();
        f = new File("Medical_Record");
        FileOutputStream output = new FileOutputStream(f);
        InputStream input = null;
        while (rs.next()) {
            input = rs.getBinaryStream("Medical_record");
            byte[] buffer = new byte[1024];
            while (input.read(buffer) > 0) {
                output.write(buffer);
            }
        }
        output.close();
        input.close();
        location.setText("File saved to "+f.getAbsolutePath());
        st.close();
        try {
            if (rs != null) {
                rs.close();
            }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    
    
    @FXML
     public void setData(int id, String first, String last, String gen, String mob, String occ, String add) throws Exception {
        Iid=id;
        fname.setText(first);
        lname.setText(last);
        genderBox.getSelectionModel().select(gen);
        mobile.setText(mob);
        occupation.setText(occ);
        address.setText(add);
        String query = "SELECT * FROM precord WHERE id="+Iid;
        st = (PreparedStatement)con.prepareStatement(query);
        rs = st.executeQuery();  
        if(!rs.next()) {
        } else {
            String s = rs.getString("Appoint");
            Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(s);
            java.sql.Date sqlDate = new java.sql.Date(date1.getTime());
            //java.sql.Date stk = new java.sql.Date(date1);
            date.setValue(sqlDate.toLocalDate());
        }
     }
    
    @Override
    public void initialize(URL location, ResourceBundle resources){   
        genderBox.setItems(genderBoxList);  
    }  
    
}
