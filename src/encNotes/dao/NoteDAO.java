/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.dao;

import encNotes.pojos.Note;
import encNotes.database.DatabaseUtils;
import encNotes.pojos.Notebook;
import encNotes.pojos.Tag;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwlltr
 */
public class NoteDAO{
        
    private DatabaseUtils dbUtils;

    public NoteDAO( ) {
        this.dbUtils = DatabaseUtils.getInstance();
    }
    
    
    
    public int insert(Note note){
        // id, name, notebook_id, content, created, last_changed
        String sql ="INSERT INTO notes(name, content, notebook_id, created, last_changed, active) VALUES(?,?,?,?,?,?)";
        PreparedStatement pstmnt;
        int rows = 0;
        try {
            pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, note.getName());
            pstmnt.setString(2, note.getContent());
            pstmnt.setInt(3, note.getNotebook().getId());
            pstmnt.setString(4, note.getCreated().toString());
            pstmnt.setString(5, note.getLast_changed().toString());
            pstmnt.setBoolean(6, note.getActive());
            
            rows = pstmnt.executeUpdate();
            
            pstmnt.close();
            //note = this.select(note.getName());
            
            TagDAO tagDAO = new TagDAO();
            for(Tag tag : note.getTags()){
                tagDAO.referenceNote(note, tag);
            }
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rows > 0){
            return note.getId();
        }else{
            return -1;
        }
        
    }
    
    public Note select(int note_id){
        String sql = "SELECT * from notes WHERE id = ?";
        Note note = null;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, note_id);
            ResultSet rs = pstmnt.executeQuery();
            
            int id;
            String name;
            String content;
            int notebook_id;
            LocalDateTime created;
            LocalDateTime last_changed;
            boolean active;
            
            while (rs.next()){
                id = rs.getInt("id");
                name = rs.getString("name");
                content = rs.getString("content");
                notebook_id = rs.getInt("notebook_id");
                created = LocalDateTime.parse(rs.getString("created"));
                last_changed = LocalDateTime.parse(rs.getString("last_changed"));
                NotebookDAO notebookBean = new NotebookDAO();
                Notebook notebook = notebookBean.select(notebook_id);
                active = rs.getBoolean("active");

                TagDAO tagDAO = new TagDAO();
                ArrayList<Tag> tags = (ArrayList<Tag>) tagDAO.selectAll(id);

                note = new Note(id, name, notebook, content, created, last_changed, tags, active);
            }
            
            
            
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return note;
    }
    
    public Note select(String note_name){
        String sql = "SELECT * from notes WHERE name = ?";
        Note note = null;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, note_name);
            ResultSet rs = pstmnt.executeQuery();
            
            int id;
            String name;
            String content;
            int notebook_id;
            LocalDateTime created;
            LocalDateTime last_changed;
            boolean active;
            
            while (rs.next()){
                id = rs.getInt("id");
                name = rs.getString("name");
                content = rs.getString("content");
                notebook_id = rs.getInt("notebook_id");
                created = LocalDateTime.parse(rs.getString("created"));
                last_changed = LocalDateTime.parse(rs.getString("last_changed"));
                NotebookDAO notebookBean = new NotebookDAO();
                Notebook notebook = notebookBean.select(notebook_id);
                active = rs.getBoolean("active");
                
                TagDAO tagDAO = new TagDAO();
                ArrayList<Tag> tags = (ArrayList<Tag>) tagDAO.selectAll(id);

                note = new Note(id, name, notebook, content, created, last_changed, tags, active);
            }
            
            
            
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return note;
    }
    
    public boolean update(Note note){
        String sql = "UPDATE notes SET name=?, content = ?,  notebook_id =?, created=?, last_changed =?, active = ? WHERE id = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            
            pstmnt.setString(1, note.getName());
            pstmnt.setInt(3, note.getNotebook().getId());
            pstmnt.setString(2, note.getContent());
            pstmnt.setString(4, note.getCreated().toString());
            pstmnt.setString(5, note.getLast_changed().toString());
            pstmnt.setBoolean(6, note.getActive());
            pstmnt.setInt(7, note.getId());
            
            rows = pstmnt.executeUpdate();
            
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0 ){
            return true;
        }   else{
            return false;
        }
        
    }
    
    public boolean delete(int note_id){
        TagDAO tagDAO = new TagDAO();
        for (Tag tag : this.select(note_id).getTags()){
           tagDAO.unreferenceNote(this.select(note_id), tag);
        }
        
        
        String sql = "DELETE FROM notes WHERE id = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, note_id);
            rows = pstmnt.executeUpdate();
            
            
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0) {
            return true;
        }else{
            return false;
        }
        
    }
    
    public boolean delete(String note_name){
        TagDAO tagDAO = new TagDAO();
        Note note = this.select(note_name);
        for (Tag tag : note.getTags()){
           tagDAO.unreferenceNote(note, tag);
        }
        
        
        String sql = "DELETE FROM notes WHERE name = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, note_name);
            rows = pstmnt.executeUpdate();
            
            
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (rows > 0) {
            return true;
        }else{
            return false;
        }
        
    }
    
    public Collection<Note> selectAll(){
        String sql = "SELECT * from notes";
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();
            NotebookDAO notebookBean = new NotebookDAO();
            while(rs.next()){
                int id = rs.getInt("id");

                notes.add(this.select(id));
                
                pstmnt.close();
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notes;
    }
    
    public Collection<Note> selectAll(Notebook notebook){
        String sql = "SELECT * from notes WHERE notebook_id = ?";
        Note note = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, notebook.getId());
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");

                notes.add(this.select(id));
            }
            pstmnt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notes;
    }
    
    public Collection<Note> selectAll(boolean active){
        String sql = "SELECT * from notes WHERE active = ?";
        Note note = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setBoolean(1, active);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");
                notes.add(this.select(id));
            }
            pstmnt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notes;
    }
    
    public Collection<Note> selectAll(Notebook notebook, boolean active){
        String sql = "SELECT * from notes WHERE notebook_id = ? AND active = ?";
        Note note = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, notebook.getId());
            pstmnt.setBoolean(2, active);
            ResultSet rs = pstmnt.executeQuery();
            while(rs.next()){
                int id = rs.getInt("id");

                notes.add(this.select(id));
            }
            pstmnt.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notes;
    }
    
}
