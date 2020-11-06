package com.sanchit.taskmanagment;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanchit.taskmanagment.adapters.ToDoListAdapter;
import com.sanchit.taskmanagment.objects.ToDoList;
import com.sanchit.taskmanagment.widget.WidgetProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * WidgetConfigureActivity a
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */

public class WidgetConfigureActivity extends AppCompatActivity implements ToDoListAdapter.ToDoListAdapterClickListener {
    private static final String TAG = "WidgetConfigureActivity";

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    FirebaseDatabase database;
    private List<ToDoList> toDoLists;
    private ToDoListAdapter mAdapter;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.no_listsTV)
    TextView no_listsTV;


    private FirebaseAuth auth;

    /**
     * onCreate view of this activity
     *
     * @param savedInstanceState object of class Bundle saving the current instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_widget_configuration);
        ButterKnife.bind(this);

        setResult(RESULT_CANCELED);

        toolbar.setTitle(R.string.chooseList);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        toDoLists = new ArrayList<>();
        mAdapter = new ToDoListAdapter(getApplicationContext(), toDoLists, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        if (auth.getCurrentUser() != null) {
            loadData();
        } else {
            no_listsTV.setVisibility(View.VISIBLE);
            no_listsTV.setText(R.string.login_first);
        }
    }

    /**
     * main function to load data from firebase
     */
    private void loadData() {
        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("lists");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                toDoLists.clear();
                toDoLists = new ArrayList<>();
                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        Log.e(TAG, "onDataChange: value is " + postSnapshot.getKey() + " " + postSnapshot.child("name").getValue());
                        toDoLists.add(new ToDoList(postSnapshot.getKey().toString(), 0, Objects.requireNonNull(postSnapshot.child("name").getValue()).toString()));
                    }
                    mAdapter.switchData(toDoLists);
                    mAdapter.notifyDataSetChanged();
                } else {
                    no_listsTV.setVisibility(View.VISIBLE);
                    no_listsTV.setText(R.string.no_lists_available);
                }
            }

            /**
             * error handling method
             * @param databaseError object of class DatabaseError
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }


    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    /**
     * Display widget woth the bundle data for current list
     *
     * @param listId listid of type string
     */
    private void showAppWidget(ToDoList listId) {
        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        SharedPreferences prefs = getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Const.WidgetPrefs + mAppWidgetId, listId.getDocId());
        editor.putString(Const.WidgetNamePrefs + mAppWidgetId, listId.getName());
        editor.apply();

        Log.e(TAG, "showAppWidget: widget id configure " + mAppWidgetId);
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE, null, this, WidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId});
        sendBroadcast(intent);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    /**
     * Overring the onClick method
     *
     * @param view     object of class view
     * @param position position to create the widget
     */

    @Override
    public void onClick(View view, ToDoList position) {
        showAppWidget(position);
    }
}