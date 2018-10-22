package com.software.abiol.simesapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.software.abiol.simesapp.Adapters.ElibraryAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Ebook;

import com.software.abiol.simesapp.utils.DrawerUtil;

import java.util.ArrayList;
import java.util.List;

public class Elibrary extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar mToolbar;
    private List<Ebook> ebooks;
    private ElibraryAdapter elibraryAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_library);

        ebooks = new ArrayList<>();

        elibraryAdapter = new ElibraryAdapter(ebooks);


        //setActionBar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("E-library");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);

        recyclerView = findViewById(R.id.elibrary_recyclerView);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("elibrary").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                //called for individual items at the database reference
                Ebook book = dataSnapshot.getValue(Ebook.class);
                ebooks.add(book);
                elibraryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(elibraryAdapter);





    }
}
