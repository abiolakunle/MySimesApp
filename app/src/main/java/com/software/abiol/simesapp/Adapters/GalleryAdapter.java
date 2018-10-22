package com.software.abiol.simesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.q42.android.scrollingimageview.ScrollingImageView;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.activities.CommentsActivity;
import com.software.abiol.simesapp.models.GalleryItem;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;

    List<GalleryItem> gallery_list;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;


    public GalleryAdapter(){

    }

    public GalleryAdapter(List<GalleryItem> gallery_list){

        this.gallery_list = gallery_list;

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_single_item, parent, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


        holder.setIsRecyclable(false);

        final String blogPostId = gallery_list.get(position).BlogPostId;
        final String currentUserId = firebaseAuth.getCurrentUser().getUid();

        String desc_data = gallery_list.get(position).getDesc();
        holder.description(desc_data);

        String image_url = gallery_list.get(position).getImage_url();
        String thumbUri = gallery_list.get(position).getImage_thumb();
        holder.setProfileImage(image_url, thumbUri);

        String user_id = gallery_list.get(position).getUser_id();
        //User Data will be retrieved here...
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    String userName = task.getResult().getString("name");
                    String userImage = task.getResult().getString("image");

                    holder.setUserData(userName, userImage);


                } else {

                    //Firebase Exception

                }

            }
        });

        try {
            long millisecond = gallery_list.get(position).getTimestamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(millisecond)).toString();
            holder.setTime(dateString);
        } catch (Exception e) {

            Toast.makeText(context, "Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

        }

        //Get Likes Count
        firebaseFirestore.collection("News/" + blogPostId + "/Likes").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int count = documentSnapshots.size();

                    holder.updateLikeCount(count);

                } else {

                    holder.updateLikeCount(0);

                }

            }
        });



        /*//Get Comment count
        firebaseFirestore.collection("News/" + blogPostId + "/Comments").addSnapshotListener( new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                if(!documentSnapshots.isEmpty()){

                    int Comment_count = documentSnapshots.size();

                    holder.commentCount(Comment_count);

                } else {

                    holder.commentCount(0);

                }

            }
        });*/


        //Get Likes
        firebaseFirestore.collection("News/" + blogPostId + "/Likes").document(currentUserId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                if(documentSnapshot.exists()){

                    holder.like.setImageDrawable(context.getDrawable(R.mipmap.action_like_accent));

                } else {

                    holder.like.setImageDrawable(context.getDrawable(R.mipmap.action_like_gray));

                }

            }
        });

        //Likes Feature
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firebaseFirestore.collection("News/" + blogPostId + "/Likes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if(!task.getResult().exists()){

                            Map<String, Object> likesMap = new HashMap<>();
                            likesMap.put("timestamp", FieldValue.serverTimestamp());

                            firebaseFirestore.collection("News/" + blogPostId + "/Likes").document(currentUserId).set(likesMap);

                        } else {

                            firebaseFirestore.collection("News/" + blogPostId + "/Likes").document(currentUserId).delete();

                        }

                    }
                });
            }
        });




        /*holder.blogCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });


        holder.commentText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent commentIntent = new Intent(context, CommentsActivity.class);
                commentIntent.putExtra("blog_post_id", blogPostId);
                context.startActivity(commentIntent);

            }
        });*/




    }


    @Override
    public int getItemCount() {
        return gallery_list.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        /*private ImageView scrollingImageView;*/
        private ImageView profileImage, like;
        private TextView userName;
        private TextView date, likeCount, description;


        public ViewHolder(View itemView) {
            super(itemView);


            mView = itemView;


            /*scrollingImageView = mView.findViewById(R.id.gallery_image);*/
            profileImage = mView.findViewById(R.id.gallery_user_image);
            userName = mView.findViewById(R.id.gallery_user_name);
            date = mView.findViewById(R.id.gallery_date);
            like = mView.findViewById(R.id.gallery_like_btn);
            likeCount = mView.findViewById(R.id.gallery_like_count);
            description = mView.findViewById(R.id.gallery_description);

        }

        public void description(String desc_data) {
            description.setText(desc_data);
        }

        public void setProfileImage(String downloadUri, String thumbUri) {

            final ConstraintLayout  constraintLayout = mView.findViewById(R.id.gallery_image);


            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(R.drawable.image_placeholder);

            Glide.with(context).applyDefaultRequestOptions(requestOptions).load(downloadUri).thumbnail(
                    Glide.with(context).load(thumbUri)
            ).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        constraintLayout.setBackground(resource);
                    }
                }
            });
        }


        public void setUserData(String userName, String userImage) {
            this.userName.setText(userName);

            RequestOptions placeholderOption = new RequestOptions();
            placeholderOption.placeholder(R.drawable.profile_placeholder);

            Glide.with(context).applyDefaultRequestOptions(placeholderOption).load(userImage).into(profileImage);

        }

        public void setTime(String dateString) {
            date.setText(dateString);
        }

        public void updateLikeCount(int count) {
            likeCount.setText(count + " Likes");
        }
    }

}
