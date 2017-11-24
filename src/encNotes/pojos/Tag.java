/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.pojos;

import java.util.Collection;

/**
 *
 * @author mwlltr
 */
public class Tag {
    private int id;
    private String name;
    private Collection<Note> notes;

    public Tag(int id, String name, Collection<Note> notes) {
        this.id = id;
        this.name = name;
        this.notes = notes;
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

    public Collection<Note> getNotes() {
        return notes;
    }

    public void setNotes(Collection<Note> notes) {
        this.notes = notes;
    }
    
    
    
}
