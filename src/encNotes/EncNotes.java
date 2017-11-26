/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes;

import com.jfoenix.controls.JFXDecorator;
import encNotes.dao.DatabaseDAO;
import encNotes.dbsetup.DatabaseSetup;
import gui.routes.Routes;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author mwlltr
 */
public class EncNotes extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
        
        Parent root = FXMLLoader.load(getClass().getResource(Routes.LOGINVIEW));
        
        
        JFXDecorator decorator=new JFXDecorator(stage, root, false, false, true);
        decorator.setCustomMaximize(false);
        decorator.setBorder(Border.EMPTY);
        
        Scene scene = new Scene(decorator);
        //scene.getStylesheets().add(EncNotes.class.getResource("/styles/styles.css").toExternalForm());
        stage.initStyle(StageStyle.UNDECORATED);
        
        stage.setScene(scene);
        
        stage.setIconified(false);
        stage.show();

    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
