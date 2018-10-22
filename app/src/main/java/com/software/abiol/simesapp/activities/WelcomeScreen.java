package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.software.abiol.simesapp.R;

public class WelcomeScreen extends AppCompatActivity {

    private FirebaseUser mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome_screen);

        mAuth = FirebaseAuth.getInstance().getCurrentUser();

        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }



    private class LogoLauncher extends  Thread{
        public void run(){
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            Intent continueIntent;
            if(mAuth == null) {
                continueIntent = new Intent(WelcomeScreen.this, StartActivity.class);
                startActivity(continueIntent);
            } else {
                continueIntent = new Intent(WelcomeScreen.this, MainActivity.class);
                startActivity(continueIntent);
            }
            WelcomeScreen.this.finish();
        }
    }
}

