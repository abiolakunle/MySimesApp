package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Executives;

import java.util.Map;

public class JoinExecutives extends AppCompatActivity {

    private Spinner executivePost;
    private EditText executiveCode;
    private Button addExecutiveBtn;


    private String[] code;
    private String[] post;
    private String selectedPostCode;
    private String selectedPosition;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_executives);

        executivePost = findViewById(R.id.executive_post);
        executiveCode = findViewById(R.id.executive_code);
        addExecutiveBtn = findViewById(R.id.add_executive_btn);

        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        ArrayAdapter<String> posts = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.posts));
        posts.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        executivePost.setAdapter(posts);

        code = getResources().getStringArray(R.array.code);

        executivePost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        selectedPostCode = code[0];
                        break;
                    case 1:
                        selectedPostCode = code[1];
                        break;
                    case 2:
                        selectedPostCode = code[2];
                        break;
                    case 3:
                        selectedPostCode = code[3];
                        break;
                    case 4:
                        selectedPostCode = code[4];
                        break;
                    case 5:
                        selectedPostCode = code[5];
                        break;
                    case 6:
                        selectedPostCode = code[6];
                        break;
                    case 7:
                        selectedPostCode = code[7];
                        break;
                    case 8:
                        selectedPostCode = code[8];
                        break;
                    case 9:
                        selectedPostCode = code[9];
                        break;
                    case 10:
                        selectedPostCode = code[10];
                        break;
                    case 11:
                        selectedPostCode = code[11];
                        break;
                    default:
                        break;

                }


                selectedPosition = String.valueOf(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addExecutiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(selectedPostCode.equals(executiveCode.getText().toString())){
                    activate();

                } else {
                    Toast.makeText(JoinExecutives.this, "Code does not match the selected position, Please check field and retry", Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    private void activate() {

        firebaseFirestore.collection("Users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    //get data from query without metadata
                    Map<String, Object> result  = task.getResult().getData();
                    result.put("position", selectedPosition);

                    firebaseFirestore.document("executives/"+selectedPosition).set(result).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(JoinExecutives.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent executivesIntent = new Intent(JoinExecutives.this, MyExecutives.class);
                                startActivity(executivesIntent);
                            }
                        }
                    });

                }

            }
        });
    }
}
