package com.software.abiol.simesapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.utils.DrawerUtil;

import java.util.HashMap;
import java.util.Map;

public class AddCourse extends AppCompatActivity {

    private EditText courseTitle, courseCode, courseUnit, courseLecturer;
    //private EditText courseProgram, courseClass;
    private Button addCourse;
    private Toolbar mToolbar;

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(this.getClass().getSimpleName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);

        courseTitle = findViewById(R.id.course_title);
        courseCode = findViewById(R.id.week_day);
        courseUnit = findViewById(R.id.course_unit);
        courseLecturer = findViewById(R.id.course_lecturer);

        /*courseProgram = findViewById(R.id.course_program);
        courseClass = findViewById(R.id.course_class);*/

        addCourse = findViewById(R.id.add_course);


        firebaseFirestore = FirebaseFirestore.getInstance();




        addCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            AddData();
            }
        });
    }

    private void AddData(){

        String course_title = courseTitle.getText().toString();
        String course_code = courseCode.getText().toString();
        String course_unit = courseUnit.getText().toString();
        String course_lecturer = courseLecturer.getText().toString();

        /*String course_program = courseProgram.getText().toString();
        String course_class = courseClass.getText().toString();*/


        Map<String, String> courseDetails = new HashMap<String, String>();
        courseDetails.put("course_code", course_code);
        courseDetails.put("course_unit", course_unit);
        courseDetails.put("course_lecturer" ,course_lecturer);

        firebaseFirestore.collection("courses").document(course_title).set(courseDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddCourse.this, "Course Added", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
