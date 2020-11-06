package com.sanchit.taskmanagment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sanchit.taskmanagment.adapters.ToDoListAdapter;
import com.sanchit.taskmanagment.dialogs.ListDialogFragment;
import com.sanchit.taskmanagment.interfaces.MainSendDataInterface;
import com.sanchit.taskmanagment.objects.ToDoList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This is the ListActivityFragment which holds the tasks and implements
 * ToDoListAdapter and its click listener
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */
public class ListActivityFragment extends Fragment implements ToDoListAdapter.ToDoListAdapterClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;

    private List<ToDoList> toDoLists;
    private ToDoListAdapter mAdapter;


    private DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    private boolean tabletMode = false;

    LinearLayout addButtonLayout;

    TextView nameTextView;
    TextView emailTextView;

    MainSendDataInterface mCallback;

    FirebaseDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflated = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = inflated.findViewById(R.id.recycler_view);

        nameTextView = inflated.findViewById(R.id.nameTv);
        emailTextView = inflated.findViewById(R.id.emailTv);

        toDoLists = new ArrayList<>();
        mAdapter = new ToDoListAdapter(getContext(), toDoLists, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);


        if (tabletMode) {
            mDrawerLayout = inflated.findViewById(R.id.drawer_layout);
            Toolbar toolbar = (Toolbar) inflated.findViewById(R.id.toolbar);
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, toolbar, 0, 0) {

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                }

                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                }
            };
        }


        addButtonLayout = inflated.findViewById(R.id.add_new_list_view);
        addButtonLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showEditDialog();
            }
        });


        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    emailTextView.setText(dataSnapshot.child("email").getValue().toString());
                    nameTextView.setText(dataSnapshot.child("name").getValue().toString());
                } catch (NullPointerException e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        return inflated;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a;

        if (context instanceof Activity) {
            a = (Activity) context;

            mCallback = (MainSendDataInterface) a;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("lists");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                toDoLists.clear();
                toDoLists = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.e(TAG, "onDataChange: value is " + postSnapshot.getKey() + " " + postSnapshot.child("name").getValue());
                    toDoLists.add(new ToDoList(postSnapshot.getKey().toString(), 0, Objects.requireNonNull(postSnapshot.child("name").getValue()).toString()));
                }
                mAdapter.switchData(toDoLists);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
    }


    private void showEditDialog() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new ListDialogFragment();
        dialogFragment.show(ft, "ListDialog");
    }


    @Override
    public void onClick(View view, ToDoList item) {
        mCallback.sendList(item);
    }
}
