package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.software.abiol.simesapp.fragments.NewsFragment;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.fragments.galleryFragment;
import com.software.abiol.simesapp.fragments.HomeFragment;
import com.software.abiol.simesapp.utils.DrawerUtil;


public class MySimesBlog extends AppCompatActivity {

    private Toolbar mToolbar;
    private FloatingActionButton addPostBtn;
    private BottomNavigationView mainBottomNav;

    private HomeFragment homeFragment;
    private NewsFragment newsFragment;
    private galleryFragment galleryFragment;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;

    private String active;

    String current_user_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_simes_blog);



        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("MySimesForum");

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);


        //change rule to remove true
        if(mAuth.getCurrentUser() != null){

            mainBottomNav = findViewById(R.id.mainBottomNav);

            //FRAGMENTS
            homeFragment = new HomeFragment();
            newsFragment = new NewsFragment();
            galleryFragment = new galleryFragment();
            //load home fragment on create
            replaceFragment(homeFragment);
            active = "homeFragment";




            mainBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.bottom_action_home:
                            replaceFragment(homeFragment);
                            active = "homeFragment";

                            return true;

                        /*case R.id.bottom_action_account:
                            replaceFragment(galleryFragment);
                            return true;*/

                        case R.id.bottom_action_notif:
                            replaceFragment(newsFragment);
                            active = "newsFragment";
                            return true;

                        default:
                            return false;

                    }


                }
            });


            addPostBtn = findViewById(R.id.add_post_btn);
            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent newPostIntent = new Intent(MySimesBlog.this, NewPostActivity.class);
                    newPostIntent.putExtra("active", active);
                    startActivity(newPostIntent);
                }
            });

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null) {
            sendToLogin();
        } else {
            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.isSuccessful()) {
                            Intent setupIntent = new Intent(MySimesBlog.this, SettingsActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }

                    } else {

                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MySimesBlog.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu_blog, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_logout_btn:
                logOut();
                return  true;

            case R.id.action_settings_btn:
                Intent settingsIntent = new Intent(MySimesBlog.this, SettingsActivity.class);
                startActivity(settingsIntent);

        }

        return false;

    }


    private void logOut(){

        mAuth.signOut();
        sendToLogin();

    }


    private void sendToLogin(){

        Intent mainIntent = new Intent(MySimesBlog.this, LoginActivity.class );
        startActivity(mainIntent);
        finish();

    }

    private void replaceFragment(Fragment fragment){

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commit();

    }

}
