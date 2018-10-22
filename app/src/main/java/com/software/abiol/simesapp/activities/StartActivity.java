package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.software.abiol.simesapp.R;

public class StartActivity extends AppCompatActivity implements OnClickListener {

    private Button mRegBtn;
    private Button mLoginBtn;
    private TextView guestText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mRegBtn = (Button) findViewById(R.id.start_reg_btn);
        mLoginBtn = (Button) findViewById(R.id.start_login_btn);
        guestText = (TextView) findViewById(R.id.guest_text);

        guestText.setOnClickListener(this);
        mRegBtn.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

            if(v.getId() == R.id.start_reg_btn){
                Intent reg_intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(reg_intent);
            } else if (v.getId() == R.id.start_login_btn){
                Intent login_intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(login_intent);
            } else if(v.getId() == R.id.guest_text) {
                Intent guestIntent = new Intent(StartActivity.this, GuestPageActivity.class);
                startActivity(guestIntent);
            }

    }
}

