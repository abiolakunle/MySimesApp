package com.software.abiol.simesapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.software.abiol.simesapp.Adapters.CourseRecyclerAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Course;
import com.software.abiol.simesapp.utils.DrawerUtil;

import java.util.ArrayList;
import java.util.List;


public class AllCoursesActivity extends AppCompatActivity {

    private static final String TAG = "MESSAGE";
    private RecyclerView courseRecyclerView;
    private List<Course> courses;
    private Toolbar mToolbar;

    private FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Courses");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);


        courseRecyclerView = findViewById(R.id.courses_recycler);

        fireStore = FirebaseFirestore.getInstance();

        courses = new ArrayList<>();

        final CourseRecyclerAdapter courseRecyclerAdapter = new CourseRecyclerAdapter(courses);
        courseRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        courseRecyclerView.setAdapter(courseRecyclerAdapter);


        fireStore.collection("courses").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){

                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Course  newCourses =  doc.getDocument().toObject(Course.class);
                        courses.add(newCourses);

                        courseRecyclerAdapter.notifyDataSetChanged();

                    }
                }

            }
        });


    }
}
