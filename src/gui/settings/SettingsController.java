/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.settings;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXToggleButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

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
    
    
}
