package com.software.abiol.simesapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.utils.DrawerUtil;

import java.util.HashMap;

public class UploadToLibraryActivity extends AppCompatActivity {

    private Button selectFile, upload, cancelButton;
    private ProgressBar progressBar;
    TextView notification;
    TextView progressLabel, sizeLabel;

    Uri pdfUri; //urls are Url meant for local storage

    private FirebaseStorage storage; // used for uploading files E.g Pdf
    private FirebaseDatabase database; //Used to store urls of uploaded files
    private StorageTask storageTask;
    private FirebaseUser firebaseUser;


    /*private ProgressDialog progressDialog;*/
    private  String fileName;

    private  Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_to_library);

        //setActionBar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Upload to E-Library");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);


        storage = FirebaseStorage.getInstance(); // returns object of firebase storage
        database = FirebaseDatabase.getInstance(); // returns instance of firebase database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        selectFile = findViewById(R.id.selectFile);
        upload = findViewById(R.id.upload);
        notification = findViewById(R.id.notification);
        progressBar = findViewById(R.id.progressBarTest);
        cancelButton = findViewById(R.id.cancel_button);
        progressLabel = findViewById(R.id.progress_label);
        sizeLabel = findViewById(R.id.size_label);



   /*     //for tags......
        final String[] select_qualification = {
                "Select Qualification", "10th / Below", "12th", "Diploma", "UG",
                "PG", "Phd"};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        ArrayList<StateVO> listVOs = new ArrayList<>();

        for (int i = 0; i < select_qualification.length; i++) {
            StateVO stateVO = new StateVO();
            stateVO.setTitle(select_qualification[i]);
            stateVO.setSelected(false);
            listVOs.add(stateVO);
        }
        MyAdapter myAdapter = new MyAdapter(UploadToLibraryActivity.this, 0,
                listVOs);
        spinner.setAdapter(myAdapter);


        ArrayList pubKey = new ArrayList();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            pubKey= extras.getStringArrayList("checkbox_state");
            //The key argument here must match that used in your adapter
        }

        StateVO stateVO = (StateVO) pubKey.get(1);
        Toast.makeText(this, stateVO.getTitle(), Toast.LENGTH_SHORT).show();*/







        selectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(UploadToLibraryActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    selectPdf();
                } else {

                    ActivityCompat.requestPermissions(UploadToLibraryActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(pdfUri != null) {

                    uploadControl();
                } else {


                    Toast.makeText(UploadToLibraryActivity.this, "please select a file", Toast.LENGTH_LONG).show();
                }

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pdfUri != null) {
                    storageTask.cancel();
                    progressLabel.setText("0");
                    sizeLabel.setText("0%");
                }

            }
        });




    }

    private void uploadControl() {
        String btnText = upload.getText().toString();

        if(btnText.equals("Start Upload")){
            uploadFile(pdfUri);
            upload.setText("Pause Upload");

        } else if(btnText.equals("Pause Upload") && storageTask.isInProgress()){
            storageTask.pause();
            upload.setText("Resume");

        } else if(btnText.equals("Resume") && storageTask.isPaused()){
            storageTask.resume();
            upload.setText("Pause Upload");
        }
    }

    private void uploadFile(Uri pdfUri) {


        progressBar.setVisibility(View.VISIBLE);

        final String fileNameStorage = fileName;
        final String timeUploaded = System.currentTimeMillis() + ""; //substring(0, fileName.lastIndexOf('.'));

        StorageReference storageReference = storage.getReference(); // returns path ,root



        storageTask = storageReference.child("elibrary").child(fileNameStorage).putFile(pdfUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                String url = taskSnapshot.getDownloadUrl().toString(); // return url of uploaded file
                String fileSize = String.valueOf((taskSnapshot.getTotalByteCount()/(1024)));
                String uploadedBy = firebaseUser.getDisplayName();
                String user_id = firebaseUser.getUid();
                String timeUploaded = System.currentTimeMillis() + ""; //substring(0, fileName.lastIndexOf('.'));
                String fileType = fileNameStorage.substring(fileNameStorage.lastIndexOf("."));

                //store url in real time database
                DatabaseReference reference = database.getReference(); // returns path to root

                HashMap<String, String> fileDetails = new HashMap<>();
                fileDetails.put("url", url);
                fileDetails.put("file_name",fileNameStorage);
                fileDetails.put("file_size", fileSize);
                fileDetails.put("time_uploaded", timeUploaded);
                fileDetails.put("uploaded_by", uploadedBy);
                fileDetails.put("file_type", fileType);
                fileDetails.put("user_id", user_id);



                reference.child("elibrary").push().setValue(fileDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {



                        if(task.isSuccessful()){

                            Toast.makeText(UploadToLibraryActivity.this, "File successfully uploaded", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(UploadToLibraryActivity.this, "File not successfully uploaded", Toast.LENGTH_LONG).show();
                        }
                        //progressDialog.dismiss();
                        progressBar.setVisibility(View.INVISIBLE);

                        Intent elibraryIntent = new Intent(UploadToLibraryActivity.this, Elibrary.class);
                        startActivity(elibraryIntent);
                        UploadToLibraryActivity.this.finish();


                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(UploadToLibraryActivity.this, "File not successfully uploaded", Toast.LENGTH_LONG).show();
                //progressDialog.dismiss();
                progressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                //track progress of upload
                int currentProgress = (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                //progressDialog.setProgress(currentProgress);
                progressBar.setProgress(currentProgress);
                String progressText = taskSnapshot.getBytesTransferred()/(1024) + "KB / " + taskSnapshot.getTotalByteCount()/(1024) + " KB";


                progressLabel.setText(progressText);
                sizeLabel.setText(currentProgress+ "%");

            }
        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            selectPdf();
        } else {

            Toast.makeText(this, "Please provide permission", Toast.LENGTH_LONG).show();;
        }

    }

    private void selectPdf() {
        //To offer user to select file using fil manager

        //we will be using an intent

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT); // to fetch file
        startActivityForResult(intent, 86);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //check weather user has selected file or not
        if(requestCode == 86 && resultCode == RESULT_OK && data != null){
            fileName = getFileName(data.getData());
            pdfUri = data.getData(); //returns uri of selected file
            notification.setText("You selected: " + fileName);

        } else {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_LONG).show();

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
