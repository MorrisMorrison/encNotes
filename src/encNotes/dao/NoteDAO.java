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
        String sql ="INSERT into notes (name, notebook_id, content, created,last_changed) VALUES(?,?,?,?,?)";
        PreparedStatement pstmnt;
        int rows = 0;
        try {
            pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setString(1, note.getName());
            pstmnt.setInt(2, note.getNotebook().getId());
            pstmnt.setString(3, note.getCreated().toString());
            pstmnt.setString(4, note.getLast_changed().toString());
            
            rows = pstmnt.executeUpdate();
            
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

            rs.next();
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String content = rs.getString("content");
            int notebook_id = rs.getInt("notebook_id");
            LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
            LocalDateTime last_changed = rs.getTimestamp("last_changed").toLocalDateTime();
            
            NotebookDAO notebookBean = new NotebookDAO();
            Notebook notebook = notebookBean.select(notebook_id);
            
            TagDAO tagDAO = new TagDAO();
            ArrayList<Tag> tags = (ArrayList<Tag>) tagDAO.selectAll(id);
            
            note = new Note(id, name, notebook, content, created, last_changed, tags);
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return note;
    }
    
    public boolean update(Note note){
        String sql = "UPDATE notes SET name=?, notebook_id =?, content = ?, created=?, last_changed =? WHERE id = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            
            pstmnt.setString(1, note.getName());
            pstmnt.setInt(2, note.getNotebook().getId());
            pstmnt.setString(3, note.getContent());
            pstmnt.setString(4, note.getCreated().toString());
            pstmnt.setString(5, note.getLast_changed().toString());
            
            pstmnt.setInt(6, note.getId());
            
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
        Note note = null;
        ArrayList<Note> notes = new ArrayList<Note>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();
            NotebookDAO notebookBean = new NotebookDAO();
            while(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String content = rs.getString("content");
                int notebook_id = rs.getInt("notebook_id");
                LocalDateTime created = rs.getTimestamp("created").toLocalDateTime();
                LocalDateTime last_changed = rs.getTimestamp("last_changed").toLocalDateTime();
                Notebook notebook = notebookBean.select(notebook_id);
                TagDAO tagDAO = new TagDAO();
                ArrayList<Tag> tags = (ArrayList<Tag>) tagDAO.selectAll(id);
                
                note = new Note(id, name, notebook, content, created, last_changed, tags);
                notes.add(note);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return notes;
    }
    
}
