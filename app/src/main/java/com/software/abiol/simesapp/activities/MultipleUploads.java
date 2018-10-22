package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.Adapters.UploadListAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MultipleUploads extends AppCompatActivity {

    private Button mSelectBtn, uploadBtn;
    private RecyclerView mUploadList;
    private static int RESULT_LOAD_IMAGE1 = 1;

    private List<String> fileNameList;
    private List<String> fileDoneList;
    private List<Double> progressList;
    private double progressStatus;
    private List<Integer> progVisibility;
    private UploadListAdapter uploadListAdapter;

    private StorageReference mStorage;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_uploads);

        mStorage = FirebaseStorage.getInstance().getReference();
        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        mSelectBtn = (Button) findViewById(R.id.select_btn);
        mUploadList = (RecyclerView) findViewById(R.id.upload_list);
        uploadBtn = findViewById(R.id.upload_btn);



        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        progressList =  new ArrayList<>();
        progVisibility = new ArrayList<>();

        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList, progressList, progVisibility);




        //Recycler view
        mUploadList.setLayoutManager(new LinearLayoutManager(this));
        mUploadList.setHasFixedSize(true);
        mUploadList.setAdapter(uploadListAdapter);

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //upload Button
                uploadToDb();
                uploadBtn.setEnabled(false);
                uploadBtn.setBackgroundColor(getResources().getColor(R.color.user_gray));
            }
        });

        mSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select Picture"), RESULT_LOAD_IMAGE1);
            }
        });


    }


    List<Uri> fileUri = new ArrayList<>();
    List<String> fileNameDatabase = new ArrayList<>();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE1 && resultCode == RESULT_OK){

            if(data.getClipData() != null){

                int totalItemSelected = data.getClipData().getItemCount();

                for(int i=0; i < totalItemSelected; i++){

                    fileUri.add(data.getClipData().getItemAt(i).getUri());
                    String fileName = getFileName(fileUri.get(i));
                    fileNameDatabase.add(fileName.substring(0, fileName.lastIndexOf('.')));


                    fileNameList.add(fileName);
                    fileDoneList.add("uploading");
                    progressList.add(i, 0.0);
                    progVisibility.add(i, View.INVISIBLE);
                    uploadListAdapter.notifyDataSetChanged();
                    //uploadListAdapter.notifyItemChanged(i);

                }
                //Toast.makeText(this, "You selected multiple files", Toast.LENGTH_LONG).show();
            } else if(data.getData() != null) {

                Toast.makeText(this, "You selected a single file" , Toast.LENGTH_LONG).show();
            }

        }
    }


    public void uploadToDb() {


        for (int i = 0; i < fileNameList.size(); i++) {
            final int finalI = i;
            StorageReference fileToUpload = mStorage.child("elibrary").child(fileNameList.get(i));

            fileToUpload.putFile(fileUri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    String url = taskSnapshot.getDownloadUrl().toString(); // return url of uploaded file
                    String fileSize = String.valueOf((taskSnapshot.getTotalByteCount()/(1024)));
                    String uploadedBy = firebaseUser.getDisplayName();
                    String user_id = firebaseUser.getUid();
                    String fileType = fileNameList.get(finalI).substring(fileNameList.get(finalI).lastIndexOf("."));
                    final String timeUploaded = System.currentTimeMillis() + "";

                    //store url in real time database
                    DatabaseReference reference = database.getReference(); // returns path to root

                    HashMap<String, String> fileDetails = new HashMap<>();
                    fileDetails.put("url", url);
                    fileDetails.put("file_name",fileNameList.get(finalI));
                    fileDetails.put("file_size", fileSize);
                    fileDetails.put("time_uploaded", timeUploaded);
                    fileDetails.put("uploaded_by", uploadedBy);
                    fileDetails.put("file_type", fileType);
                    fileDetails.put("user_id", user_id);


                    //reference.child("Uploads").child(fileNameDatabase.get(finalI)).setValue(url).
                    reference.child("elibrary").push().setValue(fileDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(MultipleUploads.this, "File successfully uploaded", Toast.LENGTH_LONG).show();
                                fileDoneList.remove(finalI);
                                fileDoneList.add(finalI, "done");
                                progressList.add(finalI, 0.0);
                                progVisibility.add(finalI, View.INVISIBLE);
                                uploadListAdapter.notifyDataSetChanged();
                                //uploadListAdapter.notifyItemChanged(finalI);

                            } else {
                                Toast.makeText(MultipleUploads.this, "File not successfully uploaded", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    //track progress of upload
                    progVisibility.add(finalI, View.VISIBLE);
                    double currentProgress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressList.add(finalI, currentProgress);
                    uploadListAdapter.notifyDataSetChanged();
                    //uploadListAdapter.notifyItemChanged(finalI);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(MultipleUploads.this, "File not successfully uploaded", Toast.LENGTH_LONG).show();
                    progVisibility.add(finalI, View.INVISIBLE);
                    uploadListAdapter.notifyDataSetChanged();
                    //uploadListAdapter.notifyItemChanged(finalI);

                }
            });

        }

    }



    public String getFileName(Uri uri){
        String result = null;
        if(uri.getScheme().equals("content")){
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);

            try{
                if(cursor != null && cursor.moveToFirst()){
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if(result == null){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut+1);
            }
        }
        return  result;
    }
}
