/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.encNotes;

import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */




public class DrawerViewController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private JFXButton home;
    
            
    @FXML
    private JFXButton settings;
                
    @FXML
    private JFXButton trash;
    
    @FXML
    private JFXButton exit;
    
                    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Image image;
        ImageView imageView;
        File imageFile;
        
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Zuhause-30.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        home.setGraphic(imageView);
        home.setStyle("-fx-background-color: #009688;");
        
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Einstellungen-30.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        settings.setGraphic(imageView);
        
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Müll-30.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        trash.setGraphic(imageView);
        
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Abmelden abgerundet-30.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        exit.setGraphic(imageView);
    }    
    
}
