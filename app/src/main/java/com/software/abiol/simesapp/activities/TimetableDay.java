package com.software.abiol.simesapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.software.abiol.simesapp.Adapters.TimetableDayAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.TimetableDayModel;
import com.software.abiol.simesapp.utils.DrawerUtil;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;


public class TimetableDay extends AppCompatActivity {


    private RecyclerView dayRecycler;
    private List<TimetableDayModel> dayData;

    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private Toolbar mToolbar;

    private String userClass;
    private String userProgram;
    private String day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_day);

        day = getIntent().getStringExtra("day");


        //setActionBar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(day);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);

        dayData = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        String current_user_id = firebaseUser.getUid();


        dayRecycler = findViewById(R.id.timetable_day_recycler);
        dayRecycler.setLayoutManager(new LinearLayoutManager(this));

        final TimetableDayAdapter timetableDayAdapter = new TimetableDayAdapter(dayData);
        dayRecycler.setAdapter(timetableDayAdapter);




        firestore.document("Users/"+current_user_id+"/").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                userClass = documentSnapshot.getString("class");
                userProgram = documentSnapshot.getString("program");



                firestore.collection("/timetable/"+userClass+"_"+userProgram+"/"+day.toLowerCase()+"/")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        //Log.d("DOPE", document.getId() + " => " + document.getData());
                                        TimetableDayModel timetableDayModel = document.toObject(TimetableDayModel.class);

                                        dayData.add(timetableDayModel);
                                        timetableDayAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    Toast.makeText(TimetableDay.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });









        /*firestore.collection("/timetable/full_time/nd1/monday/period1/").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Log.d("TOPE", task.getResult().getDocuments().get(0).getData().get("data").toString());

                dayData.add((TimetableDayModel) task.getResult().getDocuments().get(0).getData().get("data"));

                timetableDayAdapter.notifyDataSetChanged();
            }
        });*/




            /*firestore.collection("/timetable/full_time/nd1/monday/period/").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {

                        String string = doc.getDocument().getData().toString();
                        Log.d("TOPE", doc.getDocument().toObject(TimetableDayModel.class).toString());

                        *//*TimetableDayModel timetableDayModel = doc.getDocument().toObject(TimetableDayModel.class);

                        dayData.add(timetableDayModel);


                        timetableDayAdapter.notifyDataSetChanged();*//*
                    }

                }
            });*/











    }
}
