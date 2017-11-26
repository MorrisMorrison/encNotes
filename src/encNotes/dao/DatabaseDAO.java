/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.dao;

import encNotes.database.DatabaseUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwlltr
 */
public class DatabaseDAO {
    
    public ArrayList<String> getAllTableNames(){
         ArrayList<String> tableNames = new ArrayList<String>();
        
        ResultSet rs = null;
        
        try {
            Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();
            String sql = String.format("SELECT name FROM sqlite_master WHERE type='table';");
            rs = statement.executeQuery(sql);
            String name="";
            while (rs.next()){
                name=rs.getString("name");
                tableNames.add(name);
            }
            statement.close();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tableNames;
    }
        
    public ArrayList<String> getAllColumnNames(String table_name){
         ArrayList<String> columnNames = new ArrayList<String>();
       
        ResultSet rs = null;
        
        try {
            Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();
            String sql = String.format("PRAGMA table_info(%s);", table_name);
            rs = statement.executeQuery(sql);
            String name ="";
            while (rs.next()){
                name = rs.getString("name");
                columnNames.add(name);
            }
            rs.close();
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
       return columnNames; 
    }
    
    public ResultSet getAllData(String table_name){
         ResultSet rs = null;
        
        try {
            Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();
            String sql = String.format("SELECT * from %s;", table_name);
            rs = statement.executeQuery(sql);
            //rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return rs;
    }
    
    public void executeUpdate(String sql){
        try {
            Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();
            statement.executeUpdate(sql);
            statement.close();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
            public void insertCheckValue(){
        try{
            Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();
            
            String sql = "SELECT * from checkPassword;";
            ResultSet result = statement.executeQuery(sql);
            if (!result.isBeforeFirst()){
                sql = "INSERT INTO checkPassword (name) VALUES ('ThIsIsMy1337UniQue<>//checkValue,.-!');";
            }
            statement.executeUpdate(sql);
        } catch (Exception e) {
           System.err.println( e.getClass().getName() + ": " + e.getMessage() );
           System.exit(0);
        }
    }
        
         public String getCheckValue(){
            String name = "";
            try{
                Statement statement = DatabaseUtils.getInstance().getConnection().createStatement();

                String sql = "SELECT * from checkPassword;";

                ResultSet result = statement.executeQuery(sql);

                while(result.next()){
                    name = result.getString("name");
                }
            } catch (Exception e) {
               System.err.println( e.getClass().getName() + ": " + e.getMessage() );
               System.exit(0);
            }
        return name;
    }

}
