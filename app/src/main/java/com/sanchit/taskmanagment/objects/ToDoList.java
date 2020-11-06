package com.sanchit.taskmanagment.objects;

/**
 * class for ToDoList object
 *
 * @author Sanchit Vasdev
 * @version 1.0.0. 06/11/20
 */

public class ToDoList {
    String docId;
    int id;
    String name;

    public ToDoList(String docId, int id, String name) {
        this.docId = docId;
        this.id = id;
        this.name = name;
    }

    public String getDocId() {
        return docId;
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

}
