package com.software.abiol.simesapp.activities;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.software.abiol.simesapp.R;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    //Instances of view elements
    private TextInputLayout mDisplayName, firstName, lastName;
    private TextInputLayout mEmail, phoneNumber;
    private TextInputLayout mPassword;
    private Button mCreateBtn;
    private TextView backTologin;
    private TextInputLayout mPasswordConfirm;
    private Toolbar mToolbar;
    private Spinner classSpinner, programSpinner;


    //firebase Database reference
    private DatabaseReference mDatabase;

    //Progress dialog
    ProgressDialog mRegProgress;

    //Firebase auth
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;

    //spinner results
    String programSelected;
    String classSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Toolbar Setup
        mToolbar = (Toolbar) findViewById(R.id.register_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //instance of progress dialog
        mRegProgress = new ProgressDialog(this);


        //instance of firebase authentication
        mAuth = FirebaseAuth.getInstance();


        //firebase firestore instance
        firebaseFirestore = FirebaseFirestore.getInstance();

        //instantiating views
        mDisplayName = (TextInputLayout) findViewById(R.id.register_display_name);
        firstName = (TextInputLayout) findViewById(R.id.first_name);
        lastName = (TextInputLayout) findViewById(R.id.last_name);
        phoneNumber = (TextInputLayout) findViewById(R.id.phone_number);
        mEmail = (TextInputLayout) findViewById(R.id.register_email);
        mPassword = (TextInputLayout) findViewById(R.id.reg_password);
        mPasswordConfirm = (TextInputLayout) findViewById(R.id.reg_password_confirm);
        mCreateBtn = (Button) findViewById(R.id.reg_create_btn);
        backTologin = findViewById(R.id.back_to_login);
        classSpinner = findViewById(R.id.class_spinner);
        programSpinner = findViewById(R.id.program_spinner);

        classSpinner.setPrompt("Class");
        classSpinner.setOnItemSelectedListener(this);

        programSpinner.setPrompt("Program");
        programSpinner.setOnItemSelectedListener(this);



        ArrayAdapter<String> classAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.class_id));
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);


        ArrayAdapter<String> programAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.program));
        programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        programSpinner.setAdapter(programAdapter);




        backTologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(registerIntent);
            }
        });


        //setting OnClickListener for create account button
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get strings from TextInputLayouts views
                String display_name = mDisplayName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();
                String password_confirm = mPasswordConfirm.getEditText().getText().toString();


                //Ensure the TextInput Layouts are not empty
                if(!TextUtils.isEmpty(display_name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)){
                    if(password.equals(password_confirm)){

                        mRegProgress.setTitle("Registering User");
                        mRegProgress.setMessage("Please wait while we create your account");
                        mRegProgress.setCanceledOnTouchOutside(false);
                        mRegProgress.show();

                        //call register method
                        register_user(display_name, email, password);

                    } else {
                        Toast.makeText(RegisterActivity.this, "Please ensure the passwords match", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    //Register method takes registration details and sends to firebase auth and creates user database
    private void  register_user(final String display_name, final String email, String password){
        //sends email and password to firebase auth
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                // adding diplay name manually
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(display_name)
                        .build();

                mAuth.getCurrentUser().updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    //user details
                                    String first_name = firstName.getEditText().getText().toString();
                                    String last_name = lastName.getEditText().getText().toString();
                                    String phone_number = phoneNumber.getEditText().getText().toString();
                                    //details from spinners
                                    classSelected = classSpinner.getSelectedItem().toString();
                                    programSelected = programSpinner.getSelectedItem().toString();

                                    //if registration is successful
                                    if (task.isSuccessful()) {

                                        //get users instance and  ID
                                        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                                        final String uid = current_user.getUid();


                                        //creates s database child "User" for user with user ID
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                                        //generates device token
                                        String device_token = FirebaseInstanceId.getInstance().getToken();



                                        //Stores other details of user
                                        final HashMap<String, String> userMap = new HashMap<>();
                                        userMap.put("display_name", display_name);
                                        userMap.put("name", display_name);
                                        userMap.put("first_name", first_name);
                                        userMap.put("last_name", last_name);
                                        userMap.put("status", "Hi there, I'm an Imyte");
                                        userMap.put("image", "default");
                                        userMap.put("thumb_image", "default");
                                        userMap.put("device_token", device_token);
                                        userMap.put("phone_number", phone_number);
                                        userMap.put("class", classSelected);
                                        userMap.put("program", programSelected);
                                        userMap.put("uid", uid);
                                        userMap.put("email", email);



                                        //adds user details to firebase database
                                        mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                //add user data to firestore
                                                firebaseFirestore.collection("Users").document(uid).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){

                                                            Toast.makeText(RegisterActivity.this, "Details updated", Toast.LENGTH_LONG).show();

                                                        }

                                                    }
                                                });

                                                if (task.isSuccessful()) {

                                                    mRegProgress.dismiss();

                                                    triggerRebirth(getBaseContext());

                                                    /*Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(mainIntent);*/

                                                    finish();
                                                }

                                            }
                                        });


                                    } else {

                                        mRegProgress.hide();
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }




                                }
                            }
                        });


            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()){

            case R.id.course_spinner:
                classSelected = parent.getItemAtPosition(position).toString();
                break;

            case R.id.program_spinner:
                programSelected = parent.getItemAtPosition(position).toString();
                break;

                default:
                    break;

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public static void triggerRebirth(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
        ComponentName componentName = intent.getComponent();
        Intent mainIntent = Intent.makeRestartActivityTask(componentName);
        context.startActivity(mainIntent);
        Runtime.getRuntime().exit(0);
    }
}


