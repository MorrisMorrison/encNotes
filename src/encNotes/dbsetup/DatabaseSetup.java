/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.dbsetup;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author mwlltr
 */
public class DatabaseSetup {
Connection con;
Statement statement;
    
    public DatabaseSetup( ){
    }
    
    public void start(){
        this.setupDatabase();
    }
    
    
    // Database Setup
    
    // Create connection to the database
    // SQLite creates a new DB-file it can't find it under the given path
    public void connectToDatabase(){
        try{
        Class.forName("org.sqlite.JDBC");
             this.con = DriverManager.getConnection("jdbc:sqlite:encNotes.db");
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
    
        // Create a table to store notes
    public void createTableNotes(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS notes " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " name  TEXT    NOT NULL    UNIQUE," +
                    " content  TEXT    NOT NULL," +
                    " notebook   TEXT NOT NULL," +
                    " created   TEXT NOT NULL," +
                    " lastChanged   TEXT NOT NULL)"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }
    
    // Create a table to store notebooks
    public void createTableNotebooks(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS notebooks " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " name  TEXT    NOT NULL    UNIQUE," +
                    " parent  TEXT    NOT NULL," +
                    " created   TEXT NOT NULL," +
                    " lastChanged   TEXT NOT NULL)"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }

    // Create a table to connect notes to tags
    public void createTableNotesTags(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS notesTags " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " noteName  TEXT    NOT NULL," +
                    " tagName  TEXT    NOT NULL);"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }
    
    // Create a table to store tags in
    public void createTableTags(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS tags " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " name  TEXT    NOT NULL    UNIQUE);"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }
    
    // Create a table to store deleted notes to be able to restore them
    public void createTableTrashNotes(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS trashNotes " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " noteName  TEXT    NOT NULL    UNIQUE," +
                    " tags  TEXT    NOT NULL," +
                    " content  TEXT    NOT NULL," +
                    " notebook   TEXT NOT NULL," +
                    " created   TEXT NOT NULL," +
                    " lastChanged   TEXT NOT NULL," +
                    " deleted   TEXT    NOT NULL);"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }
    
    // Create a table to store deleted notebooks to be able to restore them
    public void createTableTrashNotebooks(){
        try {
            this.connectToDatabase();
            this.statement = this.con.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS trashNotebooks " +
                    "(id INTEGER PRIMARY KEY    AUTOINCREMENT," +
                    " notebookName  TEXT    NOT NULL    UNIQUE," +
                    " parent  TEXT    NOT NULL," +
                    " created   TEXT NOT NULL," +
                    " deleted   TEXT NOT NULL);"
                    ; 
            System.out.println(sql);
            this.statement.executeUpdate(sql);
            this.closeConnection();
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }System.out.println("Table Snippet created...");
    }
    
    // Setup all tables
    public void setupDatabase(){
        this.createTableNotes();
        this.createTableNotebooks();
        this.createTableNotesTags();
        this.createTableTags();
        this.createTableTrashNotes();
        //this.createTableTrashNotebooks();
    }
    
}
