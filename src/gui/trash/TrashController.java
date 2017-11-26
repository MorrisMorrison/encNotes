/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.trash;

import com.jfoenix.controls.JFXButton;
import encNotes.dao.NoteDAO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import encNotes.pojos.Note;
import java.util.ArrayList;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
    
    @FXML
    private JFXButton btnDelete;
    
    @FXML
    private JFXButton btnRestore;
    
    
    private ObservableList<Note> tableData;
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        System.out.println("INITIALIZE TRASH");
        ArrayList<Note> notes = (ArrayList<Note>) new NoteDAO().selectAll(false);
        
        tableData = FXCollections.observableArrayList();
        
        for(Note note : notes){
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
            return new ReadOnlyObjectWrapper(p.getValue().getNotebook().getName());
            }
         });
         
         clmTags.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getTags());
            }
         });
         
         clmLastChanged.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getLast_changed());
            }
         });
         
         clmCreated.setCellValueFactory(new Callback<CellDataFeatures<Note, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<Note, String> p) {
            return new ReadOnlyObjectWrapper(p.getValue().getCreated());
            }
         });

        
        
    }   
    
    
    
    @FXML
    public void btnDeleteClicked(ActionEvent e){
        Note note = tblTrash.getSelectionModel().getSelectedItem();
        System.out.println(note.getName());
        new NoteDAO().delete(note.getId());
        tableData.remove(note);
    }

    @FXML
    public void btnRestoreClicked(ActionEvent e){
        Note note = tblTrash.getSelectionModel().getSelectedItem();
        note.setActive(true);
        new NoteDAO().update(note);
        tableData.remove(note);
        //database.addNotebook(note.getNotebookName(), "root");        
    }
    
}
