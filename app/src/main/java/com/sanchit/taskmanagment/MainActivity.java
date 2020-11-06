package com.sanchit.taskmanagment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sanchit.taskmanagment.interfaces.MainSendDataInterface;
import com.sanchit.taskmanagment.objects.ToDoList;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Activity of the navigation menu
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */
public class MainActivity extends AppCompatActivity implements MainSendDataInterface {

    private final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    private ActionBarDrawerToggle mDrawerToggle;

    @BindView(R.id.imageViewProfile)
    ImageView imageView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    String currentList = "default";

    SharedPreferences preferences;

    FirebaseDatabase database;

    /**
     * onCreate view of this activity
     *
     * @param savedInstanceState object of class Bundle saving the current instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(Const.PREFS_NAME, MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(Const.PREF_DARK_THEME, false);

        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        MainActivityFragment fragment = new MainActivityFragment();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            fragment.setArguments(bundle);
            toolbar.setTitle(bundle.getString("name"));
        }
        replaceFragment(fragment);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0) {

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
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerToggle.syncState();
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        database = FirebaseDatabase.getInstance();

        DatabaseReference scoresRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()));
        scoresRef.keepSynced(true);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));

        Job myJob = dispatcher.newJobBuilder()
                .setService(BackgroundJobService.class)
                .setTag("unique-tag65464")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setReplaceCurrent(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(60, 300))
                .build();
        dispatcher.mustSchedule(myJob);
    }

    /**
     * Overriding the onActivityResult method
     *
     * @param requestCode taking requestCode from intent
     * @param resultCode  Code for getting the result
     * @param data        Getting user data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean useDarkTheme = preferences.getBoolean(Const.PREF_DARK_THEME, false);

        changeTheme(useDarkTheme);
    }

    /**
     * changeTheme of the app
     *
     * @param useDarkTheme boolean to check if user wants to change the theme
     */
    public void changeTheme(boolean useDarkTheme) {
        if (useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        } else {
            setTheme(R.style.AppTheme);
        }
        recreate();
    }

    /**
     * onPostCreate methof of this activity
     *
     * @param savedInstanceState object of class Bundle saving the current instance state
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    /**
     * method to load options menu
     *
     * @param menu menu object
     * @return returns if its created or not
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Function to check if the current Menu item is selected
     *
     * @param item an MenuType
     * @return returns a boolean value for the item
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.delete_list) {
            deleteList();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * delete List of todolist
     */
    private void deleteList() {

        DatabaseReference myRef = database.getReference("users").child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child("lists").child(currentList);
        myRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Log.d(TAG, "List successfully deleted!");
                MainActivityFragment fragment = new MainActivityFragment();
                replaceFragment(fragment);
            }
        });
    }

    /**
     * overriding onPrepareOptionsMenu , used to prepare the options menu
     *
     * @param menu takes the menu as parameter
     * @return returns object of its parent class
     */

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * methid to replace the fraction with corresponding required fragment
     *
     * @param fragment it returns a fragment
     */

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.contentFragment, fragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * function to sendList
     *
     * @param list takes a todolist
     */
    @Override
    public void sendList(ToDoList list) {
        mDrawerLayout.closeDrawer(Gravity.START, true);
        toolbar.setTitle(list.getName());

        currentList = list.getDocId();

        MainActivityFragment fragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putString("listKey", list.getDocId());
        bundle.putString("listName", list.getName());
        fragment.setArguments(bundle);
        replaceFragment(fragment);
    }

    /**
     * sets the currentListId to be used
     *
     * @param listId string id of the list
     */
    @Override
    public void sendListId(String listId) {
        currentList = listId;
    }
}
