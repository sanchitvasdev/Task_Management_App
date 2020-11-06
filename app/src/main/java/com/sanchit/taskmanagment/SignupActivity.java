package com.sanchit.taskmanagment;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Sign up activity to add user to app's database
 *
 * @author Sanchit Vasdev
 * @version 1.0.0, 06/11/20
 */
public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText inputEmail;

    @BindView(R.id.name)
    EditText inputName;

    @BindView(R.id.password)
    EditText inputPassword;
    @BindView(R.id.sign_in_button)
    Button btnSignIn;
    @BindView(R.id.sign_up_button)
    Button btnSignUp;
    @BindView(R.id.btn_reset_password)
    Button btnReset;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private FirebaseAuth auth;

    @Override
    /**
     * onCreate view of this activity
     *
     * @param savedInstanceState object of class Bundle saving the current instance state
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);


        auth = FirebaseAuth.getInstance();

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String name = inputName.getText().toString();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_email, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), R.string.enter_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), R.string.password_short, Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    System.out.println(task);
                                    Toast.makeText(SignupActivity.this, getResources().getString(R.string.authentication_failed) + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    registerUserDatabase(email, name);
                                }
                            }
                        });

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

    }

    /**
     * main function handling the registration of the user
     *
     * @param email email of the user
     * @param name  name of the user
     */
    private void registerUserDatabase(String email, String name) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users").child(auth.getUid());

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("name", name);
        myRef.setValue(user);
        myRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * onResume element function of the activity
     */
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}
