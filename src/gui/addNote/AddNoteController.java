/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.addNote;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class AddNoteController implements Initializable {

    
    @FXML
    private TextField txtName;
    
    @FXML
    private TextField txtTags;
    
    @FXML
    private TextArea txtContent;
    
    @FXML
    private Label lblDate;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        lblDate.setText(this.getCurrentDateTime());
    }    
    
    
    public void btnSaveClicked(ActionEvent e) throws IOException{
        String name;
        String date;
        String content;
        ArrayList<String> tags;
        
        
        
        
        
        
        // Switching Scene to Notes
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/notes/Notes.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene); 
        
        
    }
    
    public String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
        
        
}
