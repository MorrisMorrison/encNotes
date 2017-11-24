/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.pojos;

import java.time.LocalDateTime;

/**
 *
 * @author mwlltr
 */
public class Note {
    private int id;
    private String name; 
    private Notebook notebook;
    private String content;
    private LocalDateTime created;
    private LocalDateTime last_changed;

    public Note(int id, String name, Notebook notebook, String content, LocalDateTime created, LocalDateTime last_changed) {
        this.id = id;
        this.name = name;
        this.notebook = notebook;
        this.content = content;
        this.created = created;
        this.last_changed = last_changed;
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

    public Notebook getNotebook() {
        return notebook;
    }

    public void setNotebook(Notebook notebook) {
        this.notebook = notebook;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getLast_changed() {
        return last_changed;
    }

    public void setLast_changed(LocalDateTime last_changed) {
        this.last_changed = last_changed;
    }

    
    
    
}
