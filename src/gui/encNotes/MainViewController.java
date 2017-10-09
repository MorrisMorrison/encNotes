/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.encNotes;

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
import javafx.event.EventType;
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
    private JFXDrawer sideDrawer;
    
    private VBox sidePane;
    private Node homeButton;
    private Node settingsButton;
    private Node trashButton;
    private Node exitButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Image image;
        ImageView imageView;
        File imageFile;
        

        
        try {
            this.sidePane = FXMLLoader.load(getClass().getResource(Routes.DRAWERVIEW));
            AnchorPane home = FXMLLoader.load(getClass().getResource(Routes.HOMEVIEW));
            AnchorPane settings = FXMLLoader.load(getClass().getResource(Routes.SETTINGSVIEW));
            
            setNode(home);
            sideDrawer.setSidePane(this.sidePane);
            sideDrawer.open();
            
            // setup reference to home button
            this.homeButton = getDrawerButton("homeMenu");
            
            // add event handler to home button
            this.homeButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent ev) -> {
                AnchorPane home_new;
                try {
                    home_new = FXMLLoader.load(getClass().getResource(Routes.HOMEVIEW));
                    setNode(home_new);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                unsetButtonsFocus();
                this.homeButton.setStyle("-fx-background-color: #009688;");
            });
            
            this.settingsButton = getDrawerButton("settingsMenu");
            this.settingsButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent ev) -> {
                setNode(settings);
                unsetButtonsFocus();
                this.settingsButton.setStyle("-fx-background-color: #009688;");
            });
            
                
            
            this.trashButton = getDrawerButton("trashMenu");
            this.trashButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent ev) -> {
                AnchorPane trash;
                try {
                    trash = FXMLLoader.load(getClass().getResource(Routes.TRASHVIEW));
                    setNode(trash);
                } catch (IOException ex) {
                    Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                unsetButtonsFocus();
                this.trashButton.setStyle("-fx-background-color: #009688;");
            });
            
            
            this.exitButton=getDrawerButton("exitMenu");
            this.exitButton.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent ev) -> {
                 Stage appStage = (Stage) ((Node) ev.getSource()).getScene().getWindow();
                                        appStage.close();
            });
            
            

        } catch (IOException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    private void setNode(Node node) {
        holderPane.getChildren().clear();
        holderPane.getChildren().add((Node) node);
    }
    
    private void unsetButtonsFocus(){
        Node homeButton = getDrawerButton("homeMenu");
        homeButton.setStyle("-fx-background-color: transparent;");
        Node settingsButton = getDrawerButton("settingsMenu");
        settingsButton.setStyle("-fx-background-color: transparent;");
        Node trashButton = getDrawerButton("trashMenu");
        trashButton.setStyle("-fx-background-color: transparent;");
    }
    
    private Node getDrawerButton(String accessibleText){
        for (Node node : this.sidePane.getChildren()) {
                if (node.getAccessibleText() != null) {
                        if (node.getAccessibleText().equals(accessibleText)){
                            return node;
                        }
                }
            }
        return null;
    }
    
}
