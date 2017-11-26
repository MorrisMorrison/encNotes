/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.home;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeView;
import encNotes.dao.NoteDAO;
import encNotes.dao.NotebookDAO;
import java.io.File;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import encNotes.database.Database;
import encNotes.encryption.DBEncTools;
import encNotes.pojos.Note;
import encNotes.pojos.Notebook;
import encNotes.pojos.Tag;
import encNotes.timeutils.TimeUtils;
import java.time.LocalDateTime;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.web.HTMLEditor;


/**
 * FXML Database class
 *
 * @author mwlltr
 */
public class HomeController implements Initializable {
   
    @FXML
    private Parent topView;
    
    @FXML
    private JFXButton btnSave;
    
    @FXML
    private JFXButton btnAdd;
    
    @FXML
    private JFXButton btnDelete;
   
    @FXML
    private JFXTextField txtAdd;
    
    @FXML
    private JFXTextField txtNotename;
    
    @FXML
    private Label txtDate;
    
    @FXML
    private Label status;
    
    @FXML
    private HTMLEditor txtContent;
    
    @FXML
    private TreeView<String> notebooksTreeView;
    private TreeItem<String> root = new TreeItem<String>();
    
    @FXML
    private JFXTextField txtTags;
    
    //private final 
    
    Database database;
    String nodeName;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         Image image;
        ImageView imageView;
        File imageFile;
        
        //setup homeButton
        imageFile= new File("/home/mwlltr/workspace/Programming/encNotes/src/gui/images/icons8-Save Filled-20.png");
        image = new Image(imageFile.toURI().toString());
        imageView = new ImageView(image);
        btnSave.setGraphic(imageView);
        status.setText("Database opened.");
        notebooksTreeView.setRoot(root);
        Node rootIcon =  new ImageView(new Image(getClass().getResourceAsStream("../images/icons8-Book Shelf Filled-16.png")));
        root.setValue("Notebooks");
        root.setGraphic(rootIcon);
        this.nodeName="";
        this.database = new Database();
        this.database.insertCheckValue();
        txtDate.setText(this.database.getCurrentDateTimeGui());
        notebooksTreeView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<TreeItem>(){
             @Override
             public void onChanged(ListChangeListener.Change<? extends TreeItem> c) {
                 nodeName = notebooksTreeView.getSelectionModel().getSelectedItem().getValue();
                 //Note note = database.getNote(nodeName);
                 Note note = new NoteDAO().select(nodeName);
                 if (note == null){
                     txtNotename.setText("");
                     txtContent.setHtmlText("");
                     txtTags.setText("");
                     txtDate.setText(database.getCurrentDateTimeGui());
                 }else{
                 ArrayList<Tag> tags = (ArrayList<Tag>) note.getTags();
                 String tag_str ="";
                 for (Tag tag : tags){
                     tag_str+=tag.getName() + ",";
                 }
                 txtTags.setText(tag_str);
                 txtNotename.setText(note.getName());
                 txtContent.setHtmlText(note.getContent());
                 LocalDateTime last_changed = note.getLast_changed();
                 String date = last_changed.getDayOfMonth() + "." + last_changed.getMonthValue() + "." + last_changed.getYear();
                 txtDate.setText(date);

                 
                 }
                 
             }
        
        
        });
        
        NotebookDAO notebookDAO = new NotebookDAO();
        ArrayList<Notebook> notebooks = (ArrayList) notebookDAO.selectAll();
        for(Notebook notebook: notebooks){
            TreeItem<String> notebookTreeItem = new TreeItem<String>();
            notebookTreeItem.setValue(notebook.getName());
            Node notebookIcon =  new ImageView(new Image(getClass().getResourceAsStream("../images/icons8-Book Filled-16-orange.png")));
            notebookTreeItem.setGraphic(notebookIcon);
            
            
            
            root.getChildren().add(notebookTreeItem);
            
            NoteDAO noteDAO = new NoteDAO();
            ArrayList<Note> notes =(ArrayList) noteDAO.selectAll(notebook);
            for (Note note : notes){
                
                for (TreeItem<String> ti : root.getChildren()){
                if (ti.getValue().equals(notebook.getName())){
                    TreeItem<String> noteTreeItem = new TreeItem<String>();
                    noteTreeItem.setValue(note.getName());
                    Node noteIcon =  new ImageView(new Image(getClass().getResourceAsStream("../images/icons8-Page Filled-16.png")));
                    noteTreeItem.setGraphic(noteIcon);
                    ti.getChildren().add(noteTreeItem);
            }
            }
            
            
        }
            
            
        }
        root.setExpanded(true);
        
    }    
    @FXML
    public void btnAddClicked(ActionEvent e){
        
        String notebookName = txtAdd.getText();
        System.out.println(notebookName);
        
        Notebook notebook = new Notebook(notebookName);
        
        NotebookDAO notebookDAO = new NotebookDAO();
        notebookDAO.insert(notebook);
        
        
        TreeItem<String> notebookTreeItem = new TreeItem<String>();
        Node notebookIcon =  new ImageView(new Image(getClass().getResourceAsStream("../images/icons8-Book Filled-16-orange.png")));
        notebookTreeItem.setValue(notebookName);
        notebookTreeItem.setGraphic(notebookIcon);
        root.getChildren().add(notebookTreeItem);
        txtAdd.setText("");
        status.setText("Notebook " + notebookName + " added.");
    }
    
    @FXML
    public void btnSaveClicked(ActionEvent e){
        String noteName = txtNotename.getText();
        String tag = txtTags.getText();
        ArrayList<String> tagsAsString = this.seperateTags(tag);
        ArrayList<Tag> tags = new ArrayList<Tag>();
        for (String t : tagsAsString){
            tags.add(new Tag(t));
        }
        
        String content=txtContent.getHtmlText().toString();
        content = content.replaceAll("\'", "\"");
        String notebookName = getNodeName();
        TreeItem<String> noteTreeItem = new TreeItem<String>();
        Node noteIcon =  new ImageView(new Image(getClass().getResourceAsStream("../images/icons8-Page Filled-16.png")));
        noteTreeItem.setValue(noteName);
        noteTreeItem.setGraphic(noteIcon);
        
        for (TreeItem<String> ti : root.getChildren()){
            System.out.println(ti.getValue());
            System.out.println(notebookName);
            if (ti.getValue().equals(notebookName)){
                ti.getChildren().add(noteTreeItem);
            }
        }
        
        //this.database.addNote(noteName, content, notebookName);
        Notebook notebook = new NotebookDAO().select(notebookName);
        NoteDAO noteDAO = new NoteDAO();
        Note note = noteDAO.select(noteName);
        
        
        
        if (note == null){
            note = new Note(noteName, notebook, content, TimeUtils.getNow(), TimeUtils.getNow(), tags);
            noteDAO.insert(note);
        }else{
            note.setContent(content);
            note.setTags(tags);
            note.setLast_changed(TimeUtils.getNow());
            note.setName(noteName);
            noteDAO.update(note);
        }
        
        status.setText("Note " + noteName + " added.");
        unsetControls();
        
       
    }
    
    @FXML 
    public void myTreeClicked(ActionEvent e){
        System.out.println("Clicked.");
    }
    
    @FXML
    public void btnDeleteClicked(ActionEvent e){
        
        
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
            alert.setHeaderText("Are you sure?");
            alert.setTitle("Confirmation");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                    
                    NotebookDAO notebookDAO = new NotebookDAO();
                    if (notebookDAO.select(nodeName) == null){
                        new NoteDAO().delete(nodeName);
                    }else{
                        notebookDAO.delete(nodeName);
                    }
                    
                    
                    //this.database.deleteNotebook(nodeName);
                    //this.database.deleteNote(nodeName);
                    //this.database.deleteNotesTags(nodeName);

                    for (TreeItem<String> ti : root.getChildren()){
                        System.out.println(ti.getValue());
                        System.out.println(nodeName);
                        if (ti.getValue().equals(nodeName)){
                            root.getChildren().remove(ti);
                        }else{
                            for (TreeItem<String> tj : ti.getChildren()){
                               if (tj.getValue().equals(nodeName)){
                                   ti.getChildren().remove(tj);
                               }
                            }
                        }
                    }
            }

    }
    
    public void unsetControls(){
        this.txtNotename.setText("");
        this.txtTags.setText("");
        this.txtContent.setHtmlText("");
        
    }
    
    public String getNodeName(){
        return this.nodeName;
    }
    
    public ArrayList<String> seperateTags(String tag){
        ArrayList<String> tags = new ArrayList<String>();
        String singleTag ="";
        for (int i = 0; i < tag.length();i++){
            if (tag.charAt(i) != ','){
                singleTag += tag.charAt(i);
            }else{
                tags.add(singleTag);
                singleTag="";
            }
            if (i == tag.length()-1){
                tags.add(singleTag);
            }            
        }
        System.out.println(tags);
        return tags;
    }
    
}
