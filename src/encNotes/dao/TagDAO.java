/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.dao;

import encNotes.database.DatabaseUtils;
import encNotes.pojos.Note;
import encNotes.pojos.Notebook;
import encNotes.pojos.Tag;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwlltr
 */
public class TagDAO {
    
      private DatabaseUtils dbUtils;

    public TagDAO( ) {
        this.dbUtils = DatabaseUtils.getInstance();
    }
    
    
    
    public int insert(Tag tag){
        // id, name, notebook_id, content, created, last_changed
        String sql ="INSERT into tags VALUES(?,?)";
        PreparedStatement pstmnt;
        int rows = 0;
        try {
            pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, tag.getId());
            pstmnt.setString(2, tag.getName());

            rows = pstmnt.executeUpdate();
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (rows > 0){
            return tag.getId();
        }else{
            return -1;
        }
        
    }
    
    public Tag select(int tag_id){
        String sql = "SELECT * from tags WHERE id = ?";
        Tag tag = null;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, tag_id);
            ResultSet rs = pstmnt.executeQuery();

            rs.next();
            int id = rs.getInt("id");
            String name = rs.getString("name");
            
            sql = "SELECT * FROM notesTags WHERE id = ?";
            pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, tag_id);
            
            rs = pstmnt.executeQuery();
            NoteDAO noteDAO = new NoteDAO();
            ArrayList<Note> notes = new ArrayList<Note>();
            
            while (rs.next()){
                Note note = noteDAO.select(rs.getInt("note_id"));
                notes.add(note);
            }
            
            tag = new Tag(id, name, notes);
            
            rs.close();
            pstmnt.close();
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tag;
    }
    
    public boolean update(Tag tag){
        String sql = "UPDATE tags SET name=? WHERE id =?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            
            pstmnt.setString(1, tag.getName());
            pstmnt.setInt(2, tag.getId());

            
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
    
    public boolean delete(int tag_id){
        String sql = "DELETE FROM tags WHERE id = ?";
        int rows = 0;
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            pstmnt.setInt(1, tag_id);
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
    
    public Collection<Tag> selectAll(){
        String sql = "SELECT * from tags";
        Tag tag = null;
        ArrayList<Tag> tags = new ArrayList<Tag>();
        try {
            PreparedStatement pstmnt = this.dbUtils.getConnection().prepareStatement(sql);
            ResultSet rs = pstmnt.executeQuery();
           
            while(rs.next()){
                int id = rs.getInt("id");
                tags.add(this.select(id));                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(NoteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return tags;
    }
    
    
}
