/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notesng.controller;
import notesng.dbsetup.DatabaseSetup;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import notesng.note.Note;
import notesng.notebook.Notebook;
import org.sqlite.JDBC;
/**
 *
 * @author mwlltr
 */
public class Controller {
    Connection con = null;
    Statement statement = null;
    ArrayList<Notebook> notebooks;
    
    public static void main(String args[]){
        Controller myController = new Controller();
        System.out.println(myController.getCurrentDateTime());
    }
    
    // Default Constructor
    // Initializes all tables and notebooks list
    public Controller(){
        DatabaseSetup setup = new DatabaseSetup();
        setup.start();
        notebooks = new ArrayList<Notebook>();
        this.initNotebooks();
        this.initNotes();
    }
    
    // Database interaction
    
    // Create connection to the database
    // SQLite creates a new DB-file it can't find it under the given path
    public void connectToDatabase(){
        try{
            Class.forName("org.sqlite.JDBC");
             this.con = DriverManager.getConnection("jdbc:sqlite:crynotes.db");
        }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
        }
        System.out.println("Connection opened...");
    }
    
    // Close connection and statement
    public void closeConnection(){
        try {
            this.statement.close();
            this.con.close();
            
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
        System.out.println("Connection closed...");
    }
   
    // add a note to the database
    // also adds a note to notebook object in notebooks list
    public void addNote(String noteName,String content, String notebookName){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    noteName = this.hyphenString(noteName);
                    content = this.hyphenString(content);
                    notebookName = this.hyphenString(notebookName);
                    String created = this.getCurrentDateTime();
                    created = this.hyphenString(created);
                    String lastChanged=created;
                    String sql = String.format("INSERT INTO notes (name, content, notebook, created, lastChanged) VALUES (%s, %s, %s, %s, %s);", noteName, content, notebookName, created, lastChanged);
                    System.out.println(sql);
                    this.statement.executeUpdate(sql);
                    this.closeConnection();
                    
                    //this.addNoteToNotebook(noteName, notebookName);
                    

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // add a notebook to the database
    // also adds a notebook to notebooks list
    public void addNotebook(String notebookName,String parent){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    notebookName = this.hyphenString(notebookName);
                    parent = this.hyphenString(parent);
                    String created = this.getCurrentDateTime();
                    created = this.hyphenString(created);
                    String lastChanged=created;
                    String sql = String.format("INSERT INTO notebooks(name, parent, created, lastChanged) VALUES (%s, %s, %s, %s);", notebookName, parent, created, lastChanged);
                    System.out.println(sql);
                    this.statement.execute(sql);
                    this.closeConnection();
                    
                    //this.notebooks.add(this.getNotebook(notebookName));

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // add a tag to the database
    public void addTag(String noteName, String tagName){
        //add note and tag to noteTag table to represent their relationship
        this.addNotesTag(noteName, tagName);
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    tagName = this.hyphenString(tagName);
                    String sql = String.format("INSERT INTO tags (name) VALUES (%s);", tagName);
                    System.out.println(sql);
                    this.statement.executeUpdate(sql);
                    this.closeConnection();
                    this.addTagToNote(noteName, tagName);
                    
            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // adds a notename and a tagname to notesTag table
    // gets called automatically when a addTag is called
    public void addNotesTag(String noteName,String tagName){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    noteName = this.hyphenString(noteName);
                    tagName = this.hyphenString(tagName);
                    String sql = String.format("INSERT INTO notesTags (noteName, tagName) VALUES (%s, %s);", noteName, tagName);
                    System.out.println(sql);
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // adds deleted notes to trashNotes table
    // gets called automatically when deleteNote is called
    public void addTrashNotes(String noteName,String content, String notebook, String created){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    noteName = this.hyphenString(noteName);
                    content = this.hyphenString(content);
                    notebook = this.hyphenString(notebook);
                    created = this.hyphenString(created);
                    String deleted = this.getCurrentDateTime();
                    deleted = this.hyphenString(deleted);
                    String sql = String.format("INSERT INTO trashNotes (noteName, content, notebook, created, deleted) VALUES (%s, %s, %s, %s, %s);", noteName, content, notebook, created, deleted);
                    System.out.println(sql);
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // adds deleted notebook to trashNotebooks table
    // gets called automatically when deleteNotebook is called
    public void addTrashNotebooks(String notebookName,String parent, String created, String deleted){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    notebookName = this.hyphenString(notebookName);
                    parent = this.hyphenString(parent);
                    created = this.hyphenString(created);
                    deleted = this.hyphenString(deleted);
                    String sql = String.format("INSERT INTO trashNotebooks (notebookName,parent, created, deleted) VALUES (%s, %s, %s, %s);", notebookName, parent, created, deleted);
                    System.out.println(sql);
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }System.out.println("Values inserted...");
    }
    
    // deletes a note from database and from noteslist of workbook object in workbooks list
    public void deleteNote(String noteName){
        Note note = null;
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("DELETE from notes WHERE name = %s;", noteName);
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
            
            //note = this.getNote(noteName);
            //this.deleteNoteFromNotebook(note.getName(), note.getNotebookName());
            //this.addTrashNotes(note.getName(), note.getContent(),note.getNotebookName(), note.getCreated());
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void deleteNotebook(String notebookName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            notebookName = this.hyphenString(notebookName);
            String sql = String.format("DELETE from notebooks WHERE name = %s;", notebookName);
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
            this.deleteNotebookFromNotebooks(notebookName);
           
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // deletes all tags from both tables tags and notesTags    
    public void deleteTag(String tagName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            tagName = this.hyphenString(tagName);
            String sql = String.format("DELETE from tags WHERE name = %s;", tagName);
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            sql = String.format("DELETE from notesTags WHERE tagName = %s,", tagName);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // deletes a note stores in trashNotes table
    public void deleteTrashNotes(String noteName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("DELETE from trashNotes WHERE notesName = %s;", noteName);
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
        
    // deletes a notebook stored in trashNotebooks table
    public void deleteTrashNotebooks(String notebookName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            notebookName = this.hyphenString(notebookName);
            String sql = String.format("DELETE from trashNotebooks WHERE workbookName = %s;", notebookName);
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // creates a new note object with information from database and returns it
    public Note getNote(String noteName){
        ResultSet rs = null;
        Note note = null;
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("SELECT * FROM notes WHERE name = %s;", noteName);
            rs = this.statement.executeQuery(sql);
            
            int id=0;
            String name="";
            String content="";
            String notebook="";
            String created="";
            String lastChanged="";
            
            while (rs.next()){
                id = rs.getInt("id");
                name=rs.getString("name");
                content = rs.getString("content");
                notebook = rs.getString("notebook");
                created = rs.getString("created");
                lastChanged = rs.getString("lastChanged");
                
            }
            note = new Note(id, name, content, notebook, created, lastChanged);
            
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }return note;
    }
    
    public ArrayList<Note> getNotes(){
        Notebook notebook = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        for (ListIterator li = this.notebooks.listIterator(0); li.hasNext();){
            notebook = (Notebook) li.next();
            notes.addAll(notebook.getNotes());
        }
        return notes;
    }
    
    // Creates a new notebook object with information from database and returns it
    public Notebook getNotebook(String notebookName){
        ResultSet rs = null;
        Notebook notebook = null;
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            notebookName = this.hyphenString(notebookName);
            String sql = String.format("SELECT * FROM notebooks WHERE name = %s;", notebookName);
            rs = this.statement.executeQuery(sql);
            
            int id=0;
            String name="";
            String parent="";
            String created="";
            String lastChanged="";
            
            while (rs.next()){
                id = rs.getInt("id");
                name=rs.getString("name");
                parent = rs.getString("parent");
                created = rs.getString("created");
                lastChanged = rs.getString("lastChanged");
                
            }
            notebook = new Notebook(id, name, parent, created, lastChanged);
            
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }return notebook;
    }
    
    public ArrayList<String> getNotebookNames(){
        ArrayList<String> notebookNames = new ArrayList<String>();
        
        for (Notebook nb : notebooks){
            notebookNames.add(nb.getName());
        }
        
        return notebookNames;
    }
    
    // get a list of all notebooks
    public ArrayList<Notebook> getNotebooks(){
        return this.notebooks;
    }
    
    // get a list of all notes of a specific notebook
    public ArrayList<Note> getNotesOfNotebook(String notebookName){
        return this.getNotebookOfNotebooks(notebookName).getNotes();
    }
    
    // get a list of all tags of a specific note
    public ArrayList<String> getTagsOfNote(String noteName){
        return this.getNoteOfNotebooks(noteName).getTags();
    }
    
    // search through all notes and return a list of notes containing tagName
    public ArrayList<Note> getNotesOfTag(String tagName){
        Notebook notebook = null; 
        Note note = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        String tag = "";
        for (ListIterator lia = this.notebooks.listIterator(0); lia.hasNext();){
            notebook = (Notebook) lia.next();
            for (ListIterator lib = notebook.getNotes().listIterator(0); lib.hasNext();){
                note = (Note) lib.next();
                for (ListIterator lic = note.getTags().listIterator(0); lic.hasNext();){
                    tag = (String) lic.next();
                    if (tag.toString().compareTo(tagName)==0){
                        notes.add(note);
                    }
                }           
            }
        }
        return notes;
    }
    
    //Support Functions
    
    // adds a notebook object to notebooks list
    public void addNotebookToNotebooks(String notebookName){
        Notebook notebook = this.getNotebook(notebookName);
        this.notebooks.add(notebook);  
    }
    
    // searches for a notebook in notebookslist and adds a new note to it
    public void addNoteToNotebook(String noteName, String notebookName){
        this.notebooks.get(this.getIndexOfNotebooks(notebookName)).addNote(this.getNote(noteName));
    }
    
    // searches for a existing note in all workbooks and deletes this note
    public void deleteNoteFromNotebook(String noteName, String notebookName){
        this.notebooks.get(this.getIndexOfNotebooks(notebookName)).deleteNote(this.getNoteOfNotebooks(noteName));
    }
    
    // searches for notebook in notebooks list and deletes it
    public void deleteNotebookFromNotebooks(String notebookName){
        // Notebook object or index can ne parsed
        this.notebooks.remove(this.getIndexOfNotebooks(notebookName));
    }
    
    // searches for a tag in 
    public void deleteTagFromNote(String tagName, String noteName){
        this.getNoteOfNotebooks(noteName).deleteTag(tagName);
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            tagName = this.hyphenString(tagName);
            String sql = String.format("DELETE from notesTags WHERE noteName = %s,", noteName);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // searches for a notebook in notebooks list and returns its index
    public int getIndexOfNotebooks(String notebookName){
        int id = this.notebooks.indexOf(this.getNotebookOfNotebooks(notebookName));
        return id;
    }
    
    // searches for a note in a notebook in notebooks list and adds a tag to it
    public void addTagToNote(String noteName, String tagName){
        String notebookName = this.getNote(noteName).getNotebookName();
        int id = this.notebooks.indexOf(this.getNotebook(notebookName));
        this.notebooks.get(id).getNote(noteName).addTag(tagName);       
    }
    
    // returns note object of notebooks list
    public Note getNoteOfNotebooks(String noteName){
        Notebook notebook = null;
        Note note = null;
        for (ListIterator li = this.notebooks.listIterator(0); li.hasNext();){
            notebook = (Notebook) li.next();
            if (notebook.getNote(noteName).getName().compareTo(noteName)==0){
               note =notebook.getNote(noteName);
               return note;
            }           
        }
        return note;
    }
    
    // returns notebook object from notebooks list
    public Notebook getNotebookOfNotebooks(String notebookName){
        Notebook notebook = null;
        
        for (ListIterator li = this.notebooks.listIterator(0); li.hasNext();){
            notebook = (Notebook) li.next();
            if (notebook.getName().compareTo(notebookName) == 0){
               return notebook; 
            }           
        }
        return notebook;
    }
    
    // get current date and time as String
    public String getCurrentDateTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.ssss");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
   
    public String getCurrentDateTimeGui(){
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy");
        Date date = new Date();
        String now = dateFormat.format(date);
        return now;
    }
    
    public String hyphenString(String text){
        String newtext = "'" + text + "'";
        return newtext;
    }
    
    public void initNotebooks(){
        ResultSet rs = null;
        Notebook notebook = null;
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = String.format("SELECT * FROM notebooks");
            rs = this.statement.executeQuery(sql);
            
            String name="";
            while (rs.next()){
                name=rs.getString("name"); 
                notebook = this.getNotebook(name);
                this.notebooks.add(notebook);
            }
            
            
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void initNotes(){
         ResultSet rs = null;
        Note note = null;
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = String.format("SELECT * FROM notes");
            rs = this.statement.executeQuery(sql);
            
            String name="";
            String notebook="";
            while (rs.next()){
                name=rs.getString("name"); 
                note = this.getNote(name);
                notebook = rs.getString("notebook");
                for (Notebook nb : this.notebooks){
                    if (nb.getName().equals(notebook)){
                        System.out.println(nb.getName());
                        System.out.println(notebook);
                        
                        nb.addNote(note);
                    }
                }
            }
            
            
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
