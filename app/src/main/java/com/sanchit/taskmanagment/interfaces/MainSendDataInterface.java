package com.sanchit.taskmanagment.interfaces;

import com.sanchit.taskmanagment.objects.ToDoList;

/**
 * Interface for the main data
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */
public interface MainSendDataInterface {
    void sendList(ToDoList list);

    void sendListId(String listId);
}