/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.templates;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class LeftController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    protected JFXButton btnNotebooks;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    
    
    
     public void btnHomeClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Home");
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/home/Home.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene);  

    }
    
    
    public void btnNotebooksClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Notebooks");
        btnNotebooks.setText("clicked");
        /***
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/notebooks/Notebooks.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        
        appStage.setScene(notebooksScene);  
        ***/
        
    }
    
    public void btnNotesClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Notes");
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/notes/Notes.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene);  
        
    }
    
    public void btnSettingsClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Settings");
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/settings/Settings.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene);  
    }
    
    public void btnTrashClicked(ActionEvent e) throws IOException{
        System.out.println("Clicked Trash");
        Parent notebooksParent = FXMLLoader.load(getClass().getResource("/gui/trash/Trash.fxml"));
        Scene notebooksScene = new Scene(notebooksParent);
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.setScene(notebooksScene);  
    }
    
    public void btnExitClicked(ActionEvent e){
        Stage appStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        appStage.close();
    }
    
    
}
