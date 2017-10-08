/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encNotes.note;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 *
 * @author mwlltr
 */
public class Note {
    int id;
    String name;
    String content;
    String notebookName;
    String created;
    String lastChanged;
    ArrayList<String> tags;
    
    public Note(int id, String name, String content, String notebookName, String created, String lastChanged, ArrayList<String> tags){
        this.id = id;
        this.name = name;
        this.content = content;
        this.notebookName = notebookName;
        this.created = created;
        this.lastChanged=lastChanged;
        this.tags = tags;
    }
    
    
    
    public void deleteTag(String tagName){
        String tag ="";
        for (ListIterator li = this.tags.listIterator(0); li.hasNext();){
            tag = (String) li.next();
            if (tag.toString().compareTo(tagName) == 0){
            tags.remove(tag);
            }           
        }
    }
    
    public ArrayList<String> getTags(){
        return this.tags;
    }
    
    public void addTag(String tagName){
        tags.add(tagName);
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotebookName() {
        return notebookName;
    }

    public void setNotebook(String notebookName) {
        this.notebookName = notebookName;
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

    @Override
    public String toString() {
        return "Note{" + "id=" + id + ", name=" + name + ", content=" + content + ", notebookName=" + notebookName + ", created=" + created + ", lastChanged=" + lastChanged + ", tags=" + tags + '}';
    }
    
    
    
    
    
    
}
