package com.sanchit.taskmanagment.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sanchit.taskmanagment.R;
import com.sanchit.taskmanagment.objects.ToDoList;

import java.util.List;

/**
 * This the custom task adapter class which extends the RecyclerView
 * to hold each list
 *
 * @author Sanchit Vasdev
 * @version 1.0.0. 06/11/20
 */
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.MyViewHolder> {
    private Context context;
    private List<ToDoList> toDoList;
    ToDoListAdapterClickListener listAdapterClickListener;
    private boolean isWidgetAdapter = false;

    /**
     * Custom view holder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, description;
        private ToDoListAdapterClickListener mListener;

        /**
         * This is the ViewHolder function which sets the necessary view
         *
         * @param view     object of class View
         * @param listener interface for ToDoTaskClickListener
         */
        public MyViewHolder(View view, ToDoListAdapterClickListener listener) {
            super(view);
            name = view.findViewById(R.id.name);
            mListener = listener;
            view.setOnClickListener(this);
        }

        /**
         * Overriding onClick method of RecyclerView
         *
         * @param view object of class View
         */
        @Override
        public void onClick(View view) {
            Log.e("das", "onClick: " + getAdapterPosition() + " " + toDoList.size());
            mListener.onClick(view, toDoList.get(getAdapterPosition()));
        }
    }

    /**
     * TodoAdapter function,
     *
     * @param context  context of Adapter
     * @param cartList cartList of type List
     * @param listener interface for ToDoListAdapterClickListener
     */
    public ToDoListAdapter(Context context, List<ToDoList> cartList, ToDoListAdapterClickListener listener) {
        this.context = context;
        this.toDoList = cartList;
        listAdapterClickListener = listener;
    }

    /**
     * TodoAdapter function
     *
     * @param context         context of Adapter
     * @param cartList        cartList of type List
     * @param listener        interface for ToDoListAdapterClickListener
     * @param isWidgetAdapter boolean variable for widget
     */
    public ToDoListAdapter(Context context, List<ToDoList> cartList, ToDoListAdapterClickListener listener, Boolean isWidgetAdapter) {
        this.context = context;
        this.toDoList = cartList;
        listAdapterClickListener = listener;
        this.isWidgetAdapter = isWidgetAdapter;
    }

    /**
     * Overriding onCreateViewHolder function
     *
     * @param parent   object of type ViewGroup
     * @param viewType type of the view
     * @return returns a ViewHolder
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if (isWidgetAdapter) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tasklist_item, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.tasklist_widget_item, parent, false);
        }

        return new MyViewHolder(itemView, listAdapterClickListener);
    }

    /**
     * Overriding onBindViewHolder method
     *
     * @param holder   variable of type MyViewHolder
     * @param position position variable of type integer
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ToDoList item = toDoList.get(position);
        holder.name.setText(item.getName());
    }

    /**
     * Overriding getItemCount method
     *
     * @return taskList size
     */
    @Override
    public int getItemCount() {
        return toDoList.size();
    }

    /**
     * switchdata function
     *
     * @param cartList list of todocart
     */
    public void switchData(List<ToDoList> cartList) {
        this.toDoList = cartList;
    }

    /**
     * TodoListAdapterClickListener interface
     */
    public interface ToDoListAdapterClickListener {
        void onClick(View view, ToDoList position);
    }
}