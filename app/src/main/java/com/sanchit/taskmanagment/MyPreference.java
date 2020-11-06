package com.sanchit.taskmanagment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;

import androidx.annotation.NonNull;

import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

/**
 * custom preference file
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */

public class MyPreference extends Preference {
    TextView signOutTv;
    TextView nameTv;
    ImageView imageViewProfile;
    FirebaseDatabase database;
    Context context;

    public MyPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        signOutTv = view.findViewById(R.id.signOutButtonTv);
        nameTv = view.findViewById(R.id.nameTv);


        database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameTv.setText(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        signOutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context.getApplicationContext()));
                dispatcher.cancelAll();

                SharedPreferences preferences = getContext().getSharedPreferences(Const.PREFS_NAME, getContext().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();

                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getContext(), LoginActivity.class); // Your list's Intent
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getContext().startActivity(i);
            }
        });
    }

}