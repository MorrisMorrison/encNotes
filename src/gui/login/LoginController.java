/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import gui.routes.Routes;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Border;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class LoginController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lblMain;
    
    @FXML
    private Label lblExp;
    
    @FXML
    private JFXPasswordField passphrase;
    
    @FXML
    private JFXButton unlock;
    
    @FXML
    private JFXSpinner logInProgress;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        logInProgress.setVisible(false);
    }    
    
    @FXML
    private void btnUnlockClicked(ActionEvent event) throws IOException {
        passphrase.setVisible(false);
        unlock.setVisible(false);
        logInProgress.setVisible(true);
        Stage stage = new Stage();
        PauseTransition pauseTransition = new PauseTransition();
        System.out.println("Complte one");
        pauseTransition.setDuration(Duration.seconds(3));
            pauseTransition.setOnFinished(ev-> {
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource(Routes.MAINVIEW));
                JFXDecorator decorator = new JFXDecorator(stage, root, false, false, true);
        decorator.setCustomMaximize(false);
        decorator.setBorder(Border.EMPTY);

                Scene scene = new Scene(decorator);
                //scene.getStylesheets().add(HospitalFX.class.getResource("/styles/styles.css").toExternalForm());
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.setIconified(false);
                stage.show();
                unlock.getScene().getWindow().hide();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
            });
            System.out.println("Complte two");

        pauseTransition.play();
        System.out.println("Complte one");
        
        
        
    }

    }
    
    
