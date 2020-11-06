
package com.sanchit.taskmanagment.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sanchit.taskmanagment.R;
import com.sanchit.taskmanagment.objects.ListTask;

import java.util.List;

/**
 * This is a custom list adapter class which extends the RecyclerView to
 * hold each list
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 6/11/20
 */
public class ListTaskAdapter extends RecyclerView.Adapter<ListTaskAdapter.MyViewHolder> {
    private Context context;
    private List<ListTask> taskList;
    ToDoTaskClickListener toDoTaskClickListener;

    /**
     * Custom view holder
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        public TextView name, description;
        public RadioButton radioButton;
        private ToDoTaskClickListener mListener;

        /**
         * This is the ViewHolder function which sets the necessary view
         *
         * @param view          object of class View
         * @param clickListener interface for ToDoTaskClickListener
         */
        public MyViewHolder(View view, ToDoTaskClickListener clickListener) {
            super(view);
            name = view.findViewById(R.id.name);
            radioButton = view.findViewById(R.id.radio_button);
            mListener = clickListener;
            view.setOnClickListener(this);
            radioButton.setOnCheckedChangeListener(this);
        }

        /**
         * Overriding onClick method of RecyclerView
         *
         * @param view object of class View
         */
        @Override
        public void onClick(View view) {
            Log.e("das", "onClick: " + getAdapterPosition() + " " + taskList.size());
            mListener.onClick(view, taskList.get(getAdapterPosition()));
        }

        /**
         * Overriding onClick method of RecyclerView
         *
         * @param compoundButton compoundButton type variable
         * @param b              boolean variable
         */
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            radioButton.setChecked(false);
            mListener.onChecked(taskList.get(getAdapterPosition()));
        }
    }

    /**
     * ListTaskAdapter function,
     *
     * @param context       context of Adapter
     * @param cartList      cartList of type List
     * @param clickListener interface for ToDoTaskClickListener
     */
    public ListTaskAdapter(Context context, List<ListTask> cartList, ToDoTaskClickListener clickListener) {
        this.context = context;
        this.taskList = cartList;
        toDoTaskClickListener = clickListener;
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
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);

        return new MyViewHolder(itemView, toDoTaskClickListener);
    }

    /**
     * Overriding onBindViewHolder method
     *
     * @param holder   variable of type MyViewHolder
     * @param position position variable of type integer
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ListTask item = taskList.get(position);
        holder.name.setText(item.getName());
    }

    /**
     * Overriding getItemCount method
     *
     * @return taskList size
     */
    @Override
    public int getItemCount() {
        return taskList.size();
    }

    /**
     * Function to remove item from list
     *
     * @param position object of type ListTask
     */
    public void removeItem(ListTask position) {
        taskList.remove(position);
        notifyDataSetChanged();
    }

    /**
     * function to remove item from list
     *
     * @param position object of type integer
     */
    public void removeItem(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * function to restore item
     *
     * @param item     object of type ListTask
     * @param position object of type integer
     */
    public void restoreItem(ListTask item, int position) {
        taskList.add(position, item);
        notifyItemInserted(position);
    }

    /**
     * TodoTaskClickListener interface
     */
    public interface ToDoTaskClickListener {
        void onClick(View view, ListTask position);

        void onChecked(ListTask position);
    }
}