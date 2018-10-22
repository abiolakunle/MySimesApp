package com.software.abiol.simesapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daasuu.bl.BubbleLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.software.abiol.simesapp.models.Comments;
import com.software.abiol.simesapp.R;

import java.util.List;

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.ViewHolder> {

    public List<Comments> commentsList;
    public Context context;

    private FirebaseFirestore mFirestore;


    public CommentsRecyclerAdapter(List<Comments> commentsList){
        mFirestore = FirebaseFirestore.getInstance();
        this.commentsList = commentsList;

    }

    @Override
    public CommentsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_list_item, parent, false);
        context = parent.getContext();
        return new CommentsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentsRecyclerAdapter.ViewHolder holder, int position) {

        final String blogPostId = commentsList.get(position).BlogPostId;

        holder.setIsRecyclable(true);

        String commentMessage = commentsList.get(position).getMessage();
        holder.setComment_message(commentMessage);

        String idUser = commentsList.get(position).getUser_id();
        mFirestore.collection("Users").document(idUser).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                String imageUrl = documentSnapshot.get("image").toString();
                String nameReturned = documentSnapshot.get("name").toString();
                holder.setName(nameReturned);
                holder.setImage(imageUrl);

            }
        });





    }


    @Override
    public int getItemCount() {

        if(commentsList != null) {

            return commentsList.size();

        } else {

            return 0;

        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView comment_message;
        private TextView comment_name;
        private ImageView commentImage;
        private BubbleLayout bubbleLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;


        }

        public void setComment_message(String message){

            comment_message = mView.findViewById(R.id.comment_message);
            comment_message.setText(message);

        }


        public  void setName(String name) {
            comment_name = mView.findViewById(R.id.comment_username);
            comment_name.setText(name);
        }

        public void setImage(String imageUrl) {
            commentImage = mView.findViewById(R.id.comment_image);
            RequestOptions requestOptions  = RequestOptions.placeholderOf(R.drawable.profile_placeholder);
            Glide.with(context).load(imageUrl).apply(requestOptions).into(commentImage);
        }
    }

}
