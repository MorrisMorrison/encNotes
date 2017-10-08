/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trash;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import encNotes.database.Database;
import encNotes.note.Note;
import java.util.ArrayList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author mwlltr
 */
public class TrashController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private TableView<Note> tblTrash;
    
    @FXML
    private TableColumn<Note,String> clmID;
    
    @FXML
    private TableColumn clmNoteName;
    
    @FXML
    private TableColumn clmNotebook;
    
    @FXML
    private TableColumn clmTags;
    
    @FXML
    private TableColumn clmCreated;
    
    @FXML
    private TableColumn clmLastChanged;
    
    
    
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        Database database = new Database();
        ArrayList<Note> notes = database.getDeletedNotes();
        
        ObservableList<Note> tableData = FXCollections.observableArrayList();
        
        for(Note note : notes){
            System.out.println(note.toString());
            tableData.add(note);
        }
        
        tblTrash.setEditable(false);
        tblTrash.setItems(tableData);
        
        
         clmID.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getId());
            }
         });
         
         clmNoteName.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getName());
            }
         });
         
         clmNotebook.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getNotebookName());
            }
         });
         
         clmTags.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getTags());
            }
         });
         
         clmLastChanged.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getLastChanged());
            }
         });
         
         clmCreated.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getCreated());
            }
         });

        
        
    }   

    
}
