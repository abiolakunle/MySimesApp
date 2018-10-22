package com.software.abiol.simesapp.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.software.abiol.simesapp.Adapters.MyExecutivesAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.activities.EditTimetable;
import com.software.abiol.simesapp.models.Executives;

import java.util.ArrayList;
import java.util.List;

public class MyExecutives extends AppCompatActivity {

    private List<Executives> executivesList;

    private RecyclerView executivesView;

    private FirebaseFirestore mFirestore;

    private MyExecutivesAdapter myExecutivesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_executives);

        executivesView = findViewById(R.id.executive_recycler);

        mFirestore = FirebaseFirestore.getInstance();

        executivesList = new ArrayList<>();

        getExecutives();

        myExecutivesAdapter = new MyExecutivesAdapter(executivesList);
        executivesView.setAdapter(myExecutivesAdapter);
        executivesView.setLayoutManager(new GridLayoutManager(this, 2));



    }

    private void getExecutives() {

        mFirestore.collection("executives").orderBy("position").addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                for(DocumentChange doc: documentSnapshots.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){

                        Executives executives = doc.getDocument().toObject(Executives.class);

                        executivesList.add(executives);

                        myExecutivesAdapter.notifyDataSetChanged();


                    }
                }

            }
        });

    }
}
