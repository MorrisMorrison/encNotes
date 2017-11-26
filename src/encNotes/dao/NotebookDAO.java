/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.dao;

import encNotes.database.DatabaseUtils;
import encNotes.pojos.Note;
import encNotes.pojos.Notebook;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwlltr
 */
public class NotebookDAO {
        
    private DatabaseUtils dbUtils;

    public NotebookDAO( ) {
        this.dbUtils = DatabaseUtils.getInstance();
    }
    
    
    
    public int insert(Notebook notebook){
        // id, name, notebook_id, content, created, last_changed
        String sql ="INSERT INTO notebooks(name) VALUES(?)";
        PreparedStatement pstmnt;
        int rows = 0;
        try {
            pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, notebook.getName());

            
            rows = pstmnt.executeUpdate();
            
            pstmnt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rows > 0){
            return notebook.getId();
        }else{
            return -1;
        }
        
    }
    
    public Notebook select(int notebook_id){
        String sql = "SELECT * from notebooks WHERE id = ?";
        Notebook notebook = null;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, notebook_id);
            ResultSet rs = pstmnt.executeQuery();

            rs.next();
            int id = rs.getInt("id");
            String name = rs.getString("name");
            
            notebook = new Notebook(id, name);
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notebook;
    }
    
    public Notebook select(String notebook_name){
        String sql = "SELECT * from notebooks WHERE name = ?";
        Notebook notebook = null;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, notebook_name);
            ResultSet rs = pstmnt.executeQuery();
            while (rs.next()){
                 int id = rs.getInt("id");
                String name = rs.getString("name");
                notebook = new Notebook(id, name);
            }
           
            
            
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notebook;
    }
    
    public boolean update(Notebook notebook){
        String sql = "UPDATE notebooks SET name=? WHERE id =?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            
            pstmnt.setString(1, notebook.getName());
            pstmnt.setInt(2, notebook.getId());

            
            rows = pstmnt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0 ){
            return true;
        }   else{
            return false;
        }
        
    }
    
    public boolean delete(int notebook_id){
        NoteDAO noteDAO = new NoteDAO();
        ArrayList<Note> notes = (ArrayList) noteDAO.selectAll(this.select(notebook_id));
        
        for(Note note : notes){
            if(note.getNotebook().getId() == notebook_id){
                noteDAO.delete(note.getId());
            }
        }
        
        String sql = "DELETE FROM notebooks WHERE id = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, notebook_id);
            rows = pstmnt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0) {
            return true;
        }else{
            return false;
        }
        
    }
    
     public boolean delete(String notebook_name){
        NoteDAO noteDAO = new NoteDAO();
        ArrayList<Note> notes = (ArrayList) noteDAO.selectAll(this.select(notebook_name));
        
        for(Note note : notes){
            if(note.getNotebook().getName().equals(notebook_name)){
                noteDAO.delete(note.getId());
            }
        }
        
        String sql = "DELETE FROM notebooks WHERE name = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, notebook_name);
            rows = pstmnt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0) {
            return true;
        }else{
            return false;
        }
        
    }
    
    public Collection<Notebook> selectAll(){
        String sql = "SELECT * from notebooks";
        Note note = null;
        ArrayList<Notebook> notebooks = new ArrayList<Notebook>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();
           
            while(rs.next()){
                int id = rs.getInt("id");
                notebooks.add(this.select(id));                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notebooks;
    }
    
}
