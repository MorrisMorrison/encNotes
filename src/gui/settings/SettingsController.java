/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXToggleButton;
import encNotes.encryption.DBEncTools;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class SettingsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private JFXToggleButton btnEncrypt;
    
    @FXML
    private JFXPasswordField passphrase;

    @FXML
    private JFXPasswordField confirm;
    
    @FXML
    private JFXButton save;
    
    
    @FXML
    private JFXButton export;
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }


    @FXML
    public void btnEncryptClicked(ActionEvent e){
        if (btnEncrypt.isSelected()){
            passphrase.setDisable(false);
            confirm.setDisable(false);
            save.setDisable(false);
        }else{
            passphrase.setDisable(true);
            confirm.setDisable(true);
            save.setDisable(true);
        }
        
        
    }
    
    @FXML
    public void saveClicked(ActionEvent e){
        String newPassphrase = passphrase.getText();
        String confirmPassphrase = confirm.getText();
        if (newPassphrase.equals(confirmPassphrase)){
            Alert alert = new Alert(AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are you sure?");
            alert.setTitle("Confirmation");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                DBEncTools.setPassword(newPassphrase);
            }
            
        }
        
    }
    

    @FXML
    public void exportClicked(ActionEvent e){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Folder");
        Node source = (Node) e.getSource();
        Window stage = source.getScene().getWindow();
        File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null){
            System.out.println(selectedDirectory.getAbsolutePath());
            File database = new File("encNotes.db");
            try {
                File target = new File(selectedDirectory + "/" + database.getName());
                System.out.println(target.getAbsolutePath());
                Files.copy((Path) database.toPath(),(Path) target.toPath(), REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(SettingsController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            System.out.println("User cancelled.");
        }
    }
    
    
    
    
}
