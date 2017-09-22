/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notesng.notebook;

import java.util.ArrayList;
import java.util.ListIterator;
import javax.swing.tree.DefaultMutableTreeNode;
import notesng.note.Note;

/**
 *
 * @author mwlltr
 */
public class Notebook {
    int id;
    String name;
    String parent;
    String created;
    String lastChanged;
    ArrayList<Note> notes;
    
    public Notebook(int id, String name, String parent, String created, String lastChanged){
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.created = created;
        this.lastChanged=lastChanged;
        notes = new ArrayList<Note>();
    }
    
    public void addNote(Note note){
        this.notes.add(note);
    }
    
    public void deleteNote(Note note){
        this.notes.remove(note);
    }
    
    public ArrayList<Note> getNotes(){
        return this.notes;
    }
    
    public Note getNote(String noteName){
        Note note = null;
        for (ListIterator li = this.notes.listIterator(0); li.hasNext();){
            note = (Note) li.next();
            if (note.getName().compareTo(noteName) == 0){
               return note; 
            }           
        }
        return note;
    }
    
    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(String lastChanged) {
        this.lastChanged = lastChanged;
    }


    

}
