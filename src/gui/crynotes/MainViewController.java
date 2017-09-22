/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.crynotes;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import gui.routes.Routes;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class MainViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private AnchorPane holderPane;
    
    @FXML
    private JFXHamburger hamburger;
    
    @FXML
    private JFXDrawer sideDrawer;
    
    @FXML
    private JFXDrawer addNotebookDrawer;
    
    @FXML
    private AnchorPane addNotebookAnchor;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Image image;
        ImageView imageView;
        File imageFile;
        

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
        transition.setRate(-1);
                    transition.setRate(transition.getRate() * -1);
                    transition.play();
  
        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            transition.setRate(transition.getRate() * -1);
            transition.play();

            if (sideDrawer.isShown()) {
                sideDrawer.close();
            } else {
                sideDrawer.open();
            }

        });
        
        
        try {
            VBox sidePane = FXMLLoader.load(getClass().getResource(Routes.DRAWERVIEW));
            AnchorPane home = FXMLLoader.load(getClass().getResource(Routes.HOMEVIEW));
            AnchorPane settings = FXMLLoader.load(getClass().getResource(Routes.SETTINGSVIEW));
            AnchorPane trash = FXMLLoader.load(getClass().getResource(Routes.TRASHVIEW));
            setNode(home);
            sideDrawer.setSidePane(sidePane);
            sideDrawer.open();
            for (Node node : sidePane.getChildren()) {
                if (node.getAccessibleText() != null) {
                    node.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent ev) -> {
                        switch (node.getAccessibleText()) {
                            case "homeMenu":
                                //drawer.close();
                                setNode(home);
                                break;  
                            case "settingsMenu":
                                //drawer.close();
                                setNode(settings);
                                break; 
                            case "trashMenu":
                                //drawer.close();
                                setNode(trash);
                                break;
                            case "exitMenu":
                                        Stage appStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                                        appStage.close();
                                                       
                        }
                    });
                }

            }
        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
}
