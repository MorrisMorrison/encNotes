/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwlltr
 */
public class DatabaseUtils {
    
    public static String PREFIX="jdbc:sqlite:";
    public static String DB_NAME="encNotes.db";
    public static String URL = PREFIX + DB_NAME;
    public static String DB_USER ="postgres";
    public static String DB_PASSWORD ="postgres";
    
    private static DatabaseUtils dbUtils = new DatabaseUtils();
    
    private Connection con;

    private DatabaseUtils() {
            // initializes a connection
            // connection only gets created 1 time
            try {
                Class.forName("org.sqlite.JDBC");
                this.con = DriverManager.getConnection(URL);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Connection getConnection(){
        return this.con;
    }
           
    public void closeConnection(){
        try {
            this.con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DatabaseUtils getInstance(){
        return dbUtils;
    }
    
    
    
}
