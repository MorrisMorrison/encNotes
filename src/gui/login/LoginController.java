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
import com.jfoenix.controls.JFXTextField;
import encNotes.database.DatabaseUtils;
import encNotes.dbsetup.DatabaseSetup;
import encNotes.encryption.AES;
import encNotes.encryption.DBEncTools;
import gui.encNotes.MainViewController;
import gui.routes.Routes;
import java.io.File;
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
import static javafx.scene.AccessibleRole.BUTTON;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
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
    private JFXTextField txtPath;
    
    @FXML
    private JFXButton btnOpen;
    
    @FXML
    private JFXPasswordField passphrase;
    
    @FXML
    private JFXButton unlock;
    
    @FXML
    private JFXSpinner logInProgress;
    
    private String password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
        
        File imageFile;
        File database = new File("encNotes.db");
        String path = database.getAbsolutePath();
        Image image;
        ImageView imageView;
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Open-20.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        btnOpen.setGraphic(imageView);
        txtPath.setText(path);
        // hide progress spinner
        logInProgress.setVisible(false);
    }    
    
    @FXML
    private void btnUnlockClicked(ActionEvent event) throws IOException {
        String databasePath = txtPath.getText();
        
        this.password = passphrase.getText();
        if (this.password.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
            alert.setHeaderText("Please enter your secret passphrase!");
            alert.showAndWait();
            return;
        }
        DBEncTools.setPassword(this.password);
        DBEncTools dbEncTools = new DBEncTools();
        File dbFile = new File(databasePath);
        if (dbFile.exists() == true){
            if (dbEncTools.checkAuth() == true){
                dbEncTools.decryptDatabase();
            }else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "", ButtonType.OK);
                alert.setHeaderText("You entered the wrong passphrase!");
                alert.showAndWait();
                this.passphrase.setText("");
                return;
            }
        }
        DatabaseUtils.setDB_NAME(databasePath);
        // hide input fields
        passphrase.setVisible(false);
        unlock.setVisible(false);
        btnOpen.setVisible(false);
        txtPath.setVisible(false);
        // show progress spinner
        logInProgress.setVisible(true);
        
        // create a new stage
        Stage stage = new Stage();
        
        // create a new Transition
        PauseTransition pauseTransition = new PauseTransition();
        
        // set length of transition to 3 seconds
        pauseTransition.setDuration(Duration.seconds(3));
        
        // when transition has finished (after 3 seconds)
        pauseTransition.setOnFinished(ev-> {
            Parent root;
            try {
                
                // create mainview as parent root
                
                
                root = FXMLLoader.load(getClass().getResource(Routes.MAINVIEW));
                
                
                // create new decorator in this stage with root as parent
                // set custom decorator settings
                // first false value disables fullscreen
                // second false value disables maximize
                
                JFXDecorator decorator = new JFXDecorator(stage, root, false, false, true);
                
                
                
                
                decorator.setCustomMaximize(false);
                decorator.setBorder(Border.EMPTY);
                
                // create a new scene
                Scene scene = new Scene(decorator);
                
                //scene.getStylesheets().add(HospitalFX.class.getResource("/styles/styles.css").toExternalForm());
                
                
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                stage.setIconified(false);
                stage.show();
                
                // hide login stage
                unlock.getScene().getWindow().hide();
                
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
            });
        
        // start transition
        pauseTransition.play();
        
    }
    
    @FXML
    public void btnOpenClicked(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose database");
        Node source = (Node) e.getSource();
        Window stage = source.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null){
            txtPath.setText(file.getAbsolutePath());
            System.out.println(file.getPath().toString());
        }else{
            System.out.println("User cancelled.");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    

    }
    
    
