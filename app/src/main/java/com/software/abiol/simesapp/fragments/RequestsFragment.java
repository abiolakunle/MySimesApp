package com.software.abiol.simesapp.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.activities.StartActivity;
import com.software.abiol.simesapp.activities.UsersActivity;
import com.software.abiol.simesapp.models.Requests;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestsFragment extends Fragment {


    private View myMainView;
    private Button requestBlogBtn;
    private Button goToUPload, fetch, uploadMultiple, courseActivity, toCourses, timeTable;
    private FloatingActionButton seeUsersBtn;
    private RecyclerView myRequestList;

    private DatabaseReference friendsRequestReference, usersReference, friendsDatabaseRef, FriendsReqDatabaseRef;
    private DatabaseReference mRootRef;

    private FirebaseAuth mAuth;
    String online_user_id;

    public RequestsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myMainView = inflater.inflate(R.layout.fragment_requests, container, false);

        myRequestList = myMainView.findViewById(R.id.request_list);
        seeUsersBtn = myMainView.findViewById(R.id.see_users_btn);

        mAuth = FirebaseAuth.getInstance();
        online_user_id = mAuth.getCurrentUser().getUid();

        friendsRequestReference = FirebaseDatabase.getInstance().getReference().child("Friend_req").child(online_user_id);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        friendsDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friends");
        FriendsReqDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Friend_req");


        mRootRef = FirebaseDatabase.getInstance().getReference();

        myRequestList.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myRequestList.setLayoutManager(linearLayoutManager);

        seeUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usersIntent = new Intent(getContext(), UsersActivity.class);
                getContext().startActivity(usersIntent);
            }
        });

        return myMainView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    public void onStart() {

        super.onStart();


        FirebaseRecyclerOptions<Requests> options =
                new FirebaseRecyclerOptions.Builder<Requests>()
                        .setQuery(friendsRequestReference, Requests.class)
                        .build();

        FirebaseRecyclerAdapter firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Requests, RequestViewHolder>(options) {


            @Override
            public RequestsFragment.RequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.friend_request_all_users_layout, parent, false);

                return new RequestViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final RequestsFragment.RequestViewHolder requestViewHolder, final int position, @NonNull final Requests model) {


                final String list_user_id = getRef(position).getKey();

                DatabaseReference  get_type_ref = getRef(position).child("request_type").getRef();

                get_type_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()){
                            String request_type = dataSnapshot.getValue().toString();

                            if(request_type.equals("received")){

                                usersReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        final String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                                        final String status = dataSnapshot.child("status").getValue().toString();

                                        requestViewHolder.setUserName(userName);
                                        requestViewHolder.setThumb_user_image(userThumb);
                                        requestViewHolder.setUser_status(status);

                                        final String mCurrent_state = "req_received";

                                        requestViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                CharSequence options[] = new CharSequence[]{
                                                  "Accept Freind Request",
                                                  "Cancel Friend Request"
                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Friend Request options");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        if(position == 0){

                                                            if(mCurrent_state.equals("req_received")){

                                                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                                                Map friendsMap = new HashMap();
                                                                friendsMap.put("Friends/" + online_user_id + "/" + list_user_id + "/date", currentDate);
                                                                friendsMap.put("Friends/" + list_user_id + "/"  + online_user_id + "/date", currentDate);


                                                                friendsMap.put("Friend_req/" + online_user_id + "/" + list_user_id, null);
                                                                friendsMap.put("Friend_req/" + list_user_id + "/" + online_user_id, null);


                                                                mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                                                    @Override
                                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                                                        if(databaseError == null){

                                                                            Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
                                                                        } else {

                                                                            String error = databaseError.getMessage();

                                                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();


                                                                        }

                                                                    }
                                                                });

                                                            }

                                                        }

                                                        if(position == 1){

                                                            FriendsReqDatabaseRef.child(online_user_id).child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    FriendsReqDatabaseRef.child(list_user_id).child(online_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                                builder.show();

                                            }
                                        });


                                        if(dataSnapshot.hasChild("online")){

                                            String userOnline = dataSnapshot.child("online").getValue().toString();
                                            requestViewHolder.setUserOnline(userOnline);
                                        }


                                        friendsRequestReference.keepSynced(true);


                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            } else if(request_type.equals("sent")){
                                Button req_sent_btn = requestViewHolder.mView.findViewById(R.id.request_accept_btn);
                                req_sent_btn.setText("Request Sent");

                                requestViewHolder.mView.findViewById(R.id.request_decline_btn).setVisibility(View.INVISIBLE);

                                usersReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        final String userName = dataSnapshot.child("name").getValue().toString();
                                        final String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                                        final String status = dataSnapshot.child("status").getValue().toString();

                                        requestViewHolder.setUserName(userName);
                                        requestViewHolder.setThumb_user_image(userThumb);
                                        requestViewHolder.setUser_status(status);

                                        requestViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {


                                                CharSequence options[] = new CharSequence[]{
                                                        "Cancel Friend Request"

                                                };
                                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                                builder.setTitle("Friend Request Sent");

                                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {


                                                        if(position == 0){

                                                            FriendsReqDatabaseRef.child(online_user_id).child(list_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {

                                                                    FriendsReqDatabaseRef.child(list_user_id).child(online_user_id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {

                                                                            Toast.makeText(getContext(), "Request Cancelled", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });

                                                                }
                                                            });

                                                        }
                                                    }
                                                });
                                                builder.show();

                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });


                                    }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        };
        //not in tutorial added from user comments it made the user_layout show
        myRequestList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }



    public static class RequestViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public RequestViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUserName(String userName) {
            TextView userNameDisplay = mView.findViewById(R.id.request_profile_name);
            userNameDisplay.setText(userName);
        }

        public void setThumb_user_image(String userThumb) {

            CircleImageView thumb_image = (CircleImageView) mView.findViewById(R.id.request_profile_image);
            /*Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);*/
            Picasso.get().load(userThumb).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.default_avatar).into(thumb_image);

        }

        public void setUser_status(String status) {
            TextView user_status = mView.findViewById(R.id.request_profile_status);
            user_status.setText(status);
        }

        public void setUserOnline(String online) {
        }
    }

}
