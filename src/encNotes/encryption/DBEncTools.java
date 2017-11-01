/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.encryption;

import encNotes.database.Database;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author mwlltr
 */
public class DBEncTools {
    
    private static String password;

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        DBEncTools.password = password;
    }

    public void copyDatabase(){
        
        File databaseFile = new File("encNotes.db");
        File encryptedFile = new File("encNotes_dec.db");
        
        try {
            Files.copy(databaseFile.toPath(), encryptedFile.toPath());
        } catch (IOException ex) {
            Logger.getLogger(DBEncTools.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void encryptDatabase(){
        // create new db object
        Database database = new Database();
        // get all tableNames
        ArrayList<String> tableNames = database.getAllTableNames();
        SecretKeySpec secretKey = AES.createSecretKey(this.password);
        ArrayList<String> sqlStrings = new ArrayList<String>();
        // for every table fetch all data
        for (String tableName : tableNames){
            if (!(tableName.equals("sqlite_sequence"))){
                try {
                    System.out.println("Tablename: " + tableName);
                    ArrayList<String> columnNames = database.getAllColumnNames(tableName);
                    ResultSet data = database.getAllData(tableName);
                    System.out.println("Data: " + data);
                    // get all column names of table
                    while (data.next()){
                        for (String columnName:columnNames){
                            if (!(columnName.equals("id"))){
                            int id = data.getInt("id");
                            String text = data.getString(columnName);
                            String encryptedText = AES.encrypt(text, secretKey);
                            String sql =  String.format("UPDATE %s SET %s='%s' WHERE id = %d;", tableName, columnName, encryptedText, id);
                            System.out.println(sql);
                            sqlStrings.add(sql);
                            }
                        }
                    }   
                } catch (SQLException ex) {
                    Logger.getLogger(DBEncTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        database.closeConnection();
        for (String sqlString : sqlStrings){
            database.executeUpdate(sqlString);
        }
        
    }
    
    public void decryptDatabase(){
          // create new db object
        Database database = new Database();
        // get all tableNames
        ArrayList<String> tableNames = database.getAllTableNames();
        SecretKeySpec secretKey = AES.createSecretKey(this.password);
        ArrayList<String> sqlStrings = new ArrayList<String>();
        // for every table fetch all data
        for (String tableName : tableNames){
            if (!(tableName.equals("sqlite_sequence"))){
                try {
                    System.out.println("Tablename: " + tableName);
                    ArrayList<String> columnNames = database.getAllColumnNames(tableName);
                    ResultSet data = database.getAllData(tableName);
                    System.out.println("Data: " + data);
                    // get all column names of table
                    while (data.next()){
                        for (String columnName:columnNames){
                            if (!(columnName.equals("id"))){
                            int id = data.getInt("id");
                            String text = data.getString(columnName);
                            String decryptedText = AES.decrypt(text, secretKey);
                            String sql =  String.format("UPDATE %s SET %s='%s' WHERE id = %d;", tableName, columnName, decryptedText, id);
                            System.out.println(sql);
                            sqlStrings.add(sql);
                            }
                        }
                    }   
                } catch (SQLException ex) {
                    Logger.getLogger(DBEncTools.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        database.closeConnection();
        for (String sqlString : sqlStrings){
            database.executeUpdate(sqlString);
        }
        
        
    }
    
    public boolean checkAuth(){
        Database database = new Database();
        String checkValue = database.getCheckValue();
        SecretKeySpec secretKey = AES.createSecretKey(this.password);
        if (AES.encrypt("ThIsIsMy1337UniQue<>//checkValue,.-!", secretKey).equals(checkValue)){
            return true;
        }else{
            return false;
        }
    }
}
