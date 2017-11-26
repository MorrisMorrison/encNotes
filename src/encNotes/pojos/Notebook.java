/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.pojos;

/**
 *
 * @author mwlltr
 */
public class Notebook {
    private int id;
    private String name;
    private boolean active;
    
    public Notebook(int id, String name, boolean active) {
        this.id = id;
        this.name = name;
        this.active = active;
    }

    public Notebook(String notebookName, boolean active) {
        this.name = notebookName;
        this.active = active;
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

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    
    
}
