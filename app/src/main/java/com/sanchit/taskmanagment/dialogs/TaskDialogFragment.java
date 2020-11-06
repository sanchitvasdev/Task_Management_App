package com.sanchit.taskmanagment.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.sanchit.taskmanagment.MainActivity;
import com.sanchit.taskmanagment.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Task Dialog Fragment for the task name dialog box
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */
public class TaskDialogFragment extends DialogFragment {
    private final String TAG = MainActivity.class.getSimpleName();

    String listId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        listId = getArguments().getString("listId");
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.new_list_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        TextView titleView = mView.findViewById(R.id.dialogTitle);
        titleView.setText(R.string.new_item_title);
        userInputDialogEditText.setText(R.string.new_task_default_text);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(R.string.positiveDialogButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        String listName = userInputDialogEditText.getText().toString();
                        if (listName.isEmpty())
                            Toast.makeText(getActivity(), R.string.list_name_empty_alert, Toast.LENGTH_LONG).show();
                        else
                            addNewTask(listName);

                    }
                })

                .setNegativeButton(R.string.negativeDialogButton,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        return alertDialogBuilderUserInput.create();
    }


    private void addNewTask(final String taskName) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", taskName.toString());
        data.put("complete", "0");
        data.put("timestamp", ServerValue.TIMESTAMP);
        data.put("description", "");


        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("lists").child(listId).child("tasks").push();
        myRef.setValue(data);
    }
}
