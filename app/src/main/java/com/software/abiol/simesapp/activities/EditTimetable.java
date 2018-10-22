package com.software.abiol.simesapp.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mcsoft.timerangepickerdialog.RangeTimePickerDialog;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.utils.DrawerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javax.xml.xpath.XPathFactory.newInstance;

public class EditTimetable extends AppCompatActivity implements RangeTimePickerDialog.ISelectedTime, AdapterView.OnItemSelectedListener{


    private Button timeTableSetTime, submitTimetable;
    private TextView timetableDuration;
    List<String> timetableCourses;
    Spinner courseSpinner, timetableProgram, timetableClass, timetablePeriod, timeTableDay;
    private EditText timetableLecturer;

    String courseSelected;
    String programSelected;
    String classSelected;
    String periodSelected;
    String daySelected;

    private ProgressDialog progressDialog;

    FirebaseFirestore timetableFirestore;

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_timetable);

        //setActionBar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Modify Timetable");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);
        /*buildDrawer(savedInstanceState);*/


        timetableFirestore = FirebaseFirestore.getInstance();

        timetableCourses = new ArrayList<>();

        timeTableSetTime = findViewById(R.id.timetable_set_time);//button
        timetableDuration = findViewById(R.id.time_textview);//textView
        submitTimetable = findViewById(R.id.submit_timetable); //button

        //spinners
        timeTableDay = findViewById(R.id.timetable_day);
        timeTableDay.setOnItemSelectedListener(this);

        timetableProgram = findViewById(R.id.timetable_program);
        timetableProgram.setOnItemSelectedListener(this);

        timetableClass = findViewById(R.id.class_id);
        timetableClass.setOnItemSelectedListener(this);

        timetablePeriod = findViewById(R.id.timetable_period);
        timetablePeriod.setOnItemSelectedListener(this);

        //textView
        timetableLecturer = findViewById(R.id.course_lecturer);




        courseSpinner = findViewById(R.id.course_spinner);
        courseSpinner.setOnItemSelectedListener(this);

        final ArrayAdapter<String> courseSpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, timetableCourses);
        courseSpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseSpinner.setAdapter(courseSpinnerAdapt);



        final ArrayAdapter<String> progSpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.program));
        progSpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timetableProgram.setAdapter(progSpinnerAdapt);

        final ArrayAdapter<String> periodSpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.periods));
        periodSpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timetablePeriod.setAdapter(periodSpinnerAdapt);

        final ArrayAdapter<String> classSpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.class_id));
        classSpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timetableClass.setAdapter(classSpinnerAdapt);

        final ArrayAdapter<String> daySpinnerAdapt = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,  getResources().getStringArray(R.array.days));
        daySpinnerAdapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeTableDay.setAdapter(daySpinnerAdapt);



        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading... ");
        progressDialog.setMessage("Please wait while we load required data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();



        timetableFirestore.collection("courses").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc : task.getResult()){

                        timetableCourses.add(doc.getId());

                        Toast.makeText(EditTimetable.this, doc.getId(), Toast.LENGTH_SHORT);
                    }
                    //this gave me problems , it required to get the item selected from spinner
                    courseSpinnerAdapt.notifyDataSetChanged();
                    progressDialog.dismiss();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditTimetable.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



        setTimetableTime();





        submitTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                courseSelected = courseSpinner.getSelectedItem().toString();
                programSelected = timetableProgram.getSelectedItem().toString();
                classSelected = timetableClass.getSelectedItem().toString();
                periodSelected = timetablePeriod.getSelectedItem().toString();
                daySelected = timeTableDay.getSelectedItem().toString();


                String selectedCourse = courseSpinner.getSelectedItem().toString();
                Toast.makeText(EditTimetable.this, selectedCourse, Toast.LENGTH_SHORT).show();

                //transfer course data to timetable position
                timetableFirestore.collection("courses").document(courseSelected).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){

                            //get data from query without metadata
                            Map<String, Object> result  = task.getResult().getData();

                            timetableFirestore.document("/timetable/"+ classSelected + "_"+ programSelected + "/" + daySelected+ "/"+ periodSelected+ "/").set(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(EditTimetable.this, "Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }

                    }
                });


            }
        });






    }




    private void setTimetableTime() {

        timeTableSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Create an instance of the dialog fragment and show it
                RangeTimePickerDialog dialog = new RangeTimePickerDialog();
                dialog.newInstance(R.color.CyanWater, R.color.White, R.color.Yellow, R.color.colorPrimary, true);
                dialog.setRadiusDialog(20); // Set radius of dialog (default is 50)
                dialog.setIs24HourView(true); // Indicates if the format should be 24 hours
                dialog.setColorBackgroundHeader(R.color.colorPrimary); // Set Color of Background header dialog
                dialog.setColorTextButton(R.color.colorPrimaryDark); // Set Text color of button
                FragmentManager fragmentManager = getFragmentManager();
                dialog.show(fragmentManager, "");

            }
        });


    }

    @Override
    public void onSelectedTime(int hourStart, int minuteStart, int hourEnd, int minuteEnd) {

        timetableDuration.setText("Starts: "+hourStart+":"+ minuteStart+ " - Ends: "+hourEnd+":"+minuteEnd);

        Toast.makeText(this, "Start: "+hourStart+":"+minuteStart+"\nEnd: "+hourEnd+":"+minuteEnd, Toast.LENGTH_SHORT).show();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch(parent.getId()){
            case R.id.timetable_program:
                programSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(this, programSelected, Toast.LENGTH_SHORT).show();
                break;

            case R.id.class_id:
                classSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(this, classSelected, Toast.LENGTH_SHORT).show();
                break;

            case R.id.timetable_period:
                periodSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(this, periodSelected, Toast.LENGTH_SHORT).show();
                break;

            case R.id.course_spinner:
                courseSelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(this, courseSelected, Toast.LENGTH_SHORT).show();
                break;

            case R.id.timetable_day:
                daySelected = parent.getItemAtPosition(position).toString();
                Toast.makeText(this, daySelected, Toast.LENGTH_SHORT).show();

            default:
                Toast.makeText(this, "No item selected", Toast.LENGTH_SHORT).show();
                    break;
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /*private void pickTime(){

        // Create an instance of the dialog fragment and show it
        RangeTimePickerDialog dialog = new RangeTimePickerDialog();
        dialog.newInstance();
        dialog.setRadiusDialog(20); // Set radius of dialog (default is 50)
        dialog.setIs24HourView(true); // Indicates if the format should be 24 hours
        dialog.setColorBackgroundHeader(R.color.colorPrimary); // Set Color of Background header dialog
        dialog.setColorTextButton(R.color.colorPrimaryDark); // Set Text color of button
        FragmentManager fragmentManager = getFragmentManager();
        dialog.show(fragmentManager, "");

    }*/
}
