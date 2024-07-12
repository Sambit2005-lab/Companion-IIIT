package com.codexnovas.companioniiit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.codexnovas.companioniiit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


            public class change_your_password extends AppCompatActivity {
                private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText;
                private Button saveButton;
                private FirebaseAuth mAuth;
                private ProgressDialog progressDialog;



                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    EdgeToEdge.enable(this);
                    setContentView(R.layout.activity_change_your_password);




                        mAuth = FirebaseAuth.getInstance();

                        oldPasswordEditText = findViewById(R.id.old_password_edit);
                        newPasswordEditText = findViewById(R.id.new_password_edit);
                        confirmPasswordEditText = findViewById(R.id.confirm_password_edit);
                        saveButton = findViewById(R.id.save_button);

                        saveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                changePassword();
                            }
                        });
                    }

                    private void changePassword () {
                        String oldPassword = oldPasswordEditText.getText().toString();
                        String newPassword = newPasswordEditText.getText().toString();
                        String confirmPassword = confirmPasswordEditText.getText().toString();

                        if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!newPassword.equals(confirmPassword)) {
                            Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Changing password...");
                        progressDialog.show();

                        final FirebaseUser user = mAuth.getCurrentUser();
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), oldPassword);

                        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Toast.makeText(change_your_password.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Toast.makeText(change_your_password.this, "Password change failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(change_your_password.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
