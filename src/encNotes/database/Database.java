/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.database;
import encNotes.dbsetup.DatabaseSetup;
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
import encNotes.note.Note;
import encNotes.notebook.Notebook;
import org.sqlite.*;
/**
 *
 * @author mwlltr
 */



public class Database {
    Connection con = null;
    Statement statement = null;
    ArrayList<Notebook> notebooks;
    private static String databasePath;
    
    public static void setDatabasePath(String path){
        databasePath = path;
    }
    
    public static void main(String args[]){
        Database myController = new Database();

    }
    
    // Default Constructor
    // Initializes all tables and notebooks list
    public Database(){
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
            String connectionString = "jdbc:sqlite:" + databasePath;
             this.con = DriverManager.getConnection(connectionString);
        }catch(Exception e){
         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
         System.exit(0);
        }

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

    }
   
    // add a note to the database
    // if a note is added that already exists
    // notedata is updated
    public void addNote(String noteName,String content, String notebookName){
        try {
                    
                    Note note = null;
                    boolean exists;
                    Notebook notebook = this.getNotebook(notebookName);
                    
                    // if a user selects a note, edits it and saves his changes
                    // the given vaviable notebookName contains the actual name of the note,
                    // because the homeController always passes the name of the selected node.
                    
                    // we need to check if the given variable notebookName is really the name of a notebook
                    // therefore we simply check if this notebook exists
                    
                    // if id is 0
                    // the notebook doesn't exists
                    // so the given variable in notebookName is the name of the selected note
                    if (notebook.getId() == 0){
                        exists = true;
                        // create a note of the given noteName (in this case from notebookName variable)
                        // to get the name of the note and the actual notebookName before we change it
                        note = this.getNote(notebookName);
                    }else{
                        exists=false;
                    }
                    
                    
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    notebookName = this.hyphenString(notebookName);
                    noteName = this.hyphenString(noteName);
                    content = this.hyphenString(content);
                    String created = this.getCurrentDateTime();
                    created = this.hyphenString(created);
                    String lastChanged=created;
                    String sql="";
                    
                    // if note doesnt exist already
                    // a new note is added
                    if (exists==false){
                        sql = String.format("INSERT INTO notes (name, content, notebook, created, lastChanged) VALUES (%s, %s, %s, %s, %s);", noteName, content, notebookName, created, lastChanged);
                    }else{
                    // if note exists
                    // note is updated
                        sql = String.format("UPDATE notes SET name = %s, content =%s, notebook=%s, lastChanged = %s WHERE name =%s", noteName, content, this.hyphenString(note.getNotebookName()), lastChanged, this.hyphenString(note.getName()));
                    }
                    
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
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
               
                    this.statement.execute(sql);
                    this.closeConnection();
                    
                    //this.notebooks.add(this.getNotebook(notebookName));

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
    }
    
    // add a tag to the database
    public void addTag(String noteName, String tagName){
        //add note and tag to noteTag table to represent their relationship
        //this.addNotesTag(noteName, tagName);
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    tagName = this.hyphenString(tagName);
                    String sql = String.format("INSERT INTO tags (name) VALUES (%s);", tagName);
                    
                    this.statement.executeUpdate(sql);
                    this.closeConnection();
                    this.addTagToNote(noteName, tagName);
                    
            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
    }
    

    
    // add a tag to the database
    // if a tag is added that already exists
    // only the notename is updated
    public void addNotesTag(String noteName,String tagName, String notebookName){
        try {
                    Notebook notebook = this.getNotebook(notebookName);
                    
                    
                    // if a user selects a note, edits it and saves his changes
                    // the given vaviable notebookName contains the actual name of the note,
                    // because the homeController always passes the name of the selected node.
                    
                    // we need to check if the given variable notebookName is really the name of a notebook
                    // therefore we simply check if this notebook exists
                    
                    
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    String sql;  
                    
                    // if notebook id isn't 0
                    // the user selected a notebook
                    // and new tags need to be added
                    if(notebook.getId() != 0){

                            noteName = this.hyphenString(noteName);
                            tagName = this.hyphenString(tagName);
                            sql = String.format("INSERT INTO notesTags (noteName, tagName) VALUES (%s, %s);", noteName, tagName);
                            this.statement.executeUpdate(sql);
                    
                    // if user didn't select a notebook
                    // tags need to be updated
                    }else{
                        
                        notebookName = this.hyphenString(notebookName);
                        noteName = this.hyphenString(noteName);
                        tagName = this.hyphenString(tagName);
                        sql = String.format("UPDATE notesTags SET noteName = %s WHERE noteName = %s AND tagName = %s;", noteName, notebookName, tagName);
                         System.out.println("SQL: " + sql);
                        this.statement.executeUpdate(sql);
                        
                     }
                    this.closeConnection();
            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
    }
    
    // adds deleted notes to trashNotes table
    // gets called automatically when deleteNote is called
    public void addTrashNotes(String noteName,String content, String tags, String notebook, String created, String lastChanged){
        try {
                    this.connectToDatabase();
                    this.statement = this.con.createStatement();
                    noteName = this.hyphenString(noteName);
                    content = this.hyphenString(content);
                    tags = this.hyphenString(tags);
                    notebook = this.hyphenString(notebook);
                    created = this.hyphenString(created);
                    lastChanged = this.hyphenString(lastChanged);
                    String deleted = this.getCurrentDateTime();
                    deleted = this.hyphenString(deleted);
                    String sql = String.format("INSERT INTO trashNotes (noteName, content, tags, notebook, created, lastChanged, deleted) VALUES (%s, %s, %s, %s, %s, %s, %s);", noteName, content, tags, notebook, created, lastChanged, deleted);
                   
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
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
                   
                    this.statement.executeUpdate(sql);
                    this.closeConnection();

            } catch (Exception e) {
                   System.err.println( e.getClass().getName() + ": " + e.getMessage() );
                   System.exit(0);
                }
    }
    
    // deletes a note from database and from noteslist of workbook object in workbooks list
    public void deleteNote(String noteName){
        Note note = null;
        try {
            note = this.getNote(noteName);
            //this.deleteNoteFromNotebook(note.getName(), note.getNotebookName());
            this.addTrashNotes(note.getName(), note.getContent(), note.getTags().toString(), note.getNotebookName(), note.getCreated(), note.getLastChanged());
            
            
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("DELETE from notes WHERE name = %s;", noteName);
            
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    public void deleteNotebook(String notebookName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            notebookName = this.hyphenString(notebookName);
            String sql = String.format("DELETE from notebooks WHERE name = %s;", notebookName);
          
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
            //this.deleteNotebookFromNotebooks(notebookName);
           
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // deletes all tags from both tables tags and notesTags    
    public void deleteTag(String tagName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            tagName = this.hyphenString(tagName);
            String sql = String.format("DELETE from tags WHERE name = %s;", tagName);
       
            this.statement.executeUpdate(sql);
            sql = String.format("DELETE from notesTags WHERE tagName = %s,", tagName);
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    // deletes a note stores in trashNotes table
    public void deleteTrashNotes(String noteName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("DELETE from trashNotes WHERE noteName = %s;", noteName);
            
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    
        
    // deletes a notebook stored in trashNotebooks table
    public void deleteTrashNotebooks(String notebookName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            notebookName = this.hyphenString(notebookName);
            String sql = String.format("DELETE from trashNotebooks WHERE notebookName = %s;", notebookName);
        
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
     // deletes a notebook stored in trashNotebooks table
    public void deleteNotesTags(String noteName){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("DELETE from notesTags WHERE noteName = %s;", noteName);
        
            this.statement.executeUpdate(sql);
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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
            
            // select all tags of this notes and add them to an ArrayList seperately
            ArrayList<String> tags = new ArrayList<String>();
            sql =   String.format("SELECT * FROM notesTags WHERE noteName = %s", noteName);
            System.out.println(sql);
            rs= this.statement.executeQuery(sql);
            while(rs.next()){
                tags.add(rs.getString("tagName"));
            }
            
            note = new Note(id, name, content, notebook, created, lastChanged, tags);
            
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return note;
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
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public ArrayList<Note> getDeletedNotes(){
        ArrayList<Note> notes = new ArrayList<Note>();
        ResultSet rs = null;
        
         try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "SELECT * FROM trashNotes";
        
            rs = this.statement.executeQuery(sql);
            
            
            while(rs.next()){
                int id = rs.getInt("id");
                String noteName = rs.getString("noteName");
                
                String tag = rs.getString("tags");
                
                ArrayList<String> tags = this.seperateTags(tag);
                String content = rs.getString("content");
                String notebook = rs.getString("notebook");
                String created = rs.getString("created");
                String lastChanged = rs.getString("lastChanged");
                
                Note note = new Note(id, noteName, content, notebook, created, lastChanged, tags);
                notes.add(note);
            }
            
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return notes;
    }
    
    public Note getDeletedNote(String noteName){
        ResultSet rs = null;
        Note note = null;
         try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            noteName = this.hyphenString(noteName);
            String sql = String.format("SELECT * FROM trashNotes WHERE noteName = %s", noteName);

            rs = this.statement.executeQuery(sql);
            
            
            while(rs.next()){
                int id = rs.getInt("id");
                
                String tag = rs.getString("tags");
                
                ArrayList<String> tags = this.seperateTags(tag);
                String content = rs.getString("content");
                String notebook = rs.getString("notebook");
                String created = rs.getString("created");
                String lastChanged = rs.getString("lastChanged");
                
                note = new Note(id, noteName, content, notebook, created, lastChanged, tags);
            }
            
            this.statement.close();           
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return note;
    }
    
    // get a string of tags seperated by commas
    // split them and add single tags to an ArrayList
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

        return tags;
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
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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
            
            
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
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

                        
                        nb.addNote(note);
                    }
                }
            }
            
            
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public ArrayList<String> getAllTableNames(){
        
        ArrayList<String> tableNames = new ArrayList<String>();
        
        ResultSet rs = null;
        
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = String.format("SELECT name FROM sqlite_master WHERE type='table';");
            rs = this.statement.executeQuery(sql);
            String name="";
            while (rs.next()){
                name=rs.getString("name");
                tableNames.add(name);
            }
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableNames;
        
    }
    
    public ArrayList<String> getAllColumnNames(String tableName){
       
        
        
        ArrayList<String> columnNames = new ArrayList<String>();
       
        ResultSet rs = null;
        
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = String.format("PRAGMA table_info(%s);", tableName);
            rs = this.statement.executeQuery(sql);
            String name ="";
            while (rs.next()){
                name = rs.getString("name");
                columnNames.add(name);
            }
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
       return columnNames; 
    }
    
        public ResultSet getAllData(String tableName){
        
        
        ResultSet rs = null;
        
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = String.format("SELECT * from %s;", tableName);
            rs = this.statement.executeQuery(sql);
            //this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
        
    }
        
        public void executeUpdate(String sql){
            
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        }
        
        public void insertCheckValue(){
        try{
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            
            String sql = "SELECT * from checkPassword;";
            ResultSet result = this.statement.executeQuery(sql);
            if (!result.isBeforeFirst()){
                sql = "INSERT INTO checkPassword (name) VALUES ('ThIsIsMy1337UniQue<>//checkValue,.-!');";
            }
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
    }
        
         public String getCheckValue(){
            String name = "";
            try{
                this.connectToDatabase();
                this.statement = this.con.createStatement();

                String sql = "SELECT * from checkPassword;";

                ResultSet result = this.statement.executeQuery(sql);

                while(result.next()){
                    name = result.getString("name");
                }
                this.closeConnection();
            } catch (Exception e) {
               System.err.println( e.getClass().getName() + ": " + e.getMessage() );
               System.exit(0);
            }
        return name;
    }
}
