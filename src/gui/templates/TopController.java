/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.templates;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class TopController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
    public void btnNewClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Trash");
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/addNote/AddNote.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene); 
        
    }
    
    public void btnSaveClicked(ActionEvent e){
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        System.out.println(appStage.getScene().toString());
    }
    
        public String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.ssss");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
    
}
