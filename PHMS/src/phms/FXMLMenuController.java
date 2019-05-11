/*
Created By : Vaibhav Shukla
*/
package phms;

import ConnectionUtil.ConnectionUtil;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;




public class FXMLMenuController implements Initializable{

    @FXML
    TableView<ModelTable> table;
    
    @FXML
    TableColumn<ModelTable, Integer> Id;
    
    @FXML
    TableColumn<ModelTable, String> FIRST;
    
    @FXML
    TableColumn<ModelTable, String> Last;
    
    @FXML
    TableColumn<ModelTable, String> Gender;
    
    @FXML
    TableColumn<ModelTable, String> Mobile;
    
    @FXML
    TableColumn<ModelTable, String> Occupation;
    
    @FXML
    TableColumn<ModelTable, String> Address;
    
    @FXML
    private TextField searchF;
    
    Stage dialogStage = new Stage();
    Scene scene;
    
    @FXML
    ObservableList<ModelTable> obList = FXCollections.observableArrayList();
    
    @FXML
    FilteredList<ModelTable> filteredData = new FilteredList<>(obList, e->true);
    
    public Connection con = null;
    public PreparedStatement st = null;
    public ResultSet rs = null;
    
    public FXMLMenuController() { 
        con = ConnectionUtil.connectdb();
    }
   
    @FXML
    void search(KeyEvent event) throws IOException{
                searchF.textProperty().addListener((observableValue, oldValue, newValue) ->{
                filteredData.setPredicate((Predicate<? super ModelTable>) obList->{
                    if(newValue == null || newValue.isEmpty()){
                        return true;
                    }
                    String lowerCaseFilter = newValue.toLowerCase();
                    
                    String s = ""+obList.getId();
                    if(s.contains(newValue)){
                        return true;
                    }
                    else if(obList.getFirst().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }else if(obList.getLast().toLowerCase().contains(lowerCaseFilter)){
                        return true;
                    }
                    return false;
                });
            });
            SortedList<ModelTable> sortedData = new SortedList<>(filteredData);
            sortedData.comparatorProperty().bind(table.comparatorProperty());
            table.setItems(sortedData);
    }
    
    @FXML
    void addUser(ActionEvent event) throws IOException {
        Node node = (Node)event.getSource();
        dialogStage = (Stage) node.getScene().getWindow();
        dialogStage.close();
        scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLPersonal.fxml")));
        dialogStage.setScene(scene);
        dialogStage.show();
    }
    @FXML
    void refresh(ActionEvent event) throws Exception {
        loadData();
    }
    
    @FXML
    void upUser(ActionEvent event) throws IOException, Exception {
        loadData();
    }
   
    
    @FXML
    void logOut(ActionEvent  event) throws IOException {
         Node node = (Node)event.getSource();
            dialogStage = (Stage) node.getScene().getWindow();
            dialogStage.close();
            scene = new Scene(FXMLLoader.load(getClass().getResource("FXMLDocument.fxml")));
            dialogStage.setScene(scene);
            dialogStage.show();
    }
            
    public void loadData() throws Exception{
        obList.clear();
        try{
            rs = con.createStatement().executeQuery("select * from precord");
            while(rs.next()){
                obList.add(new ModelTable(rs.getInt("Id"),rs.getString("FIRST"), rs.getString("Last"), rs.getString("Gender"),
                rs.getString("Mobile"), rs.getString("Occupation"), rs.getString("Address")));
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {            
                rs.close();
            } catch (SQLException ex) {
                Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        Id.setCellValueFactory(new PropertyValueFactory<>("id"));
        FIRST.setCellValueFactory(new PropertyValueFactory<>("first"));
        Last.setCellValueFactory(new PropertyValueFactory<>("last"));
        Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        Mobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        Occupation.setCellValueFactory(new PropertyValueFactory<>("occupation"));
        Address.setCellValueFactory(new PropertyValueFactory<>("address"));
        
        table.setItems(obList);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources){
        try {
            loadData();
        } catch (Exception ex) {
            Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @FXML
    void notify(ActionEvent event) throws SQLException {
        String ACCOUNT_SID =
            "xxxxxxx";
        String AUTH_TOKEN =
            "xxxxxx";
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        Date date = new Date();
        String dt = "\""+dateFormat.format(date)+"\"";
        DateFormat dkFormat = new SimpleDateFormat("HH");
        Date dk = new Date();
        int dd = Integer.parseInt(dkFormat.format(dk));
      
        String query = "select * from precord where Appoint="+dt;
        st = (PreparedStatement)con.prepareStatement(query);
        rs = st.executeQuery();
        
        while(rs.next()){
            dd+=1;
            Message message = Message
            .creator(new PhoneNumber("+91"+rs.getString("Mobile")),
            new PhoneNumber("+15163070018"),
            "Hi "+rs.getString("First")+",\n Today is your appointment with Dr. Vaibhav.\n You can visit him at "+dd+":00 hrs.")
            .create();
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Notified all the patients who have appointment today.");
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    
    @FXML
    private void displaySelected(javafx.scene.input.MouseEvent event) throws Exception {
        if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
            if(table.getSelectionModel().getSelectedItem()==null){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Entry Not Selected");
                alert.setTitle("Select Entry");
                alert.setHeaderText(null);
                alert.showAndWait();       
            }
            else{
                FXMLLoader Loader = new FXMLLoader();
                Loader.setLocation(getClass().getResource("FXMLEdit.fxml"));
                
                try {
                    Loader.load();
                } catch (IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                FXMLEditController editController = Loader.getController();
                editController.setData(table.getSelectionModel().getSelectedItem().getId(), ""+table.getSelectionModel().getSelectedItem().getFirst(),
                        ""+table.getSelectionModel().getSelectedItem().getLast(),""+table.getSelectionModel().getSelectedItem().getGender(),
                        ""+table.getSelectionModel().getSelectedItem().getMobile(),""+table.getSelectionModel().getSelectedItem().getOccupation(),
                        table.getSelectionModel().getSelectedItem().getAddress()+"");
                Parent p = Loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(p));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
                stage.setOnCloseRequest((e)->{
                try {
                    upUser(new ActionEvent());
                } catch (IOException ex) {
                    Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
                }   catch (Exception ex) {
                        Logger.getLogger(FXMLMenuController.class.getName()).log(Level.SEVERE, null, ex);
                    }
    });       
    }
        }
    }
    
}

 
