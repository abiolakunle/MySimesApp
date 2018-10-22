package com.software.abiol.simesapp.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.bl.ArrowDirection;
import com.daasuu.bl.BubbleLayout;
import com.github.library.bubbleview.BubbleLinearLayout;
import com.github.library.bubbleview.BubbleTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.software.abiol.simesapp.models.Messages;
import com.software.abiol.simesapp.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    private List<Messages> mMessageList;
    private DatabaseReference mUserDatabase;
    private Context context;

    private String current_user_id;
    private String image;
    private String name;
    private String dateString;


    public MessageAdapter(List<Messages> mMessageList) {

        this.mMessageList = mMessageList;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();


        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);

    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public TextView messageText;
        public CircleImageView profileImage, messageThumb;
        public TextView displayName, timeView;
        public ImageView messageImage;
        public BubbleLayout bubbleLayout;


        public MessageViewHolder(View view) {
            super(view);

            messageText = (TextView) view.findViewById(R.id.message_text_layout);
            messageImage = (ImageView) view.findViewById(R.id.message_image_layout);
            bubbleLayout = (BubbleLayout) view.findViewById(R.id.bubble_layout);
            messageThumb = view.findViewById(R.id.message_thumb);
            timeView = view.findViewById(R.id.time_view);


        }
    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        final Messages c = mMessageList.get(i);

        final String from_user = c.getFrom();
        final String message_type = c.getType();


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);


        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                name = dataSnapshot.child("name").getValue().toString();
                image = dataSnapshot.child("thumb_image").getValue().toString();

                long time = c.getTime();
                SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                sfd.format(time);
                dateString = DateFormat.format("MM/dd/yy HH:mm", new Date(time)).toString();


                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                current_user_id = mAuth.getCurrentUser().getUid();




                if (message_type.equals("text") && !c.getMessage().isEmpty()) {

                    if (from_user.equals(current_user_id)) {


                        Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(viewHolder.messageThumb);
                        viewHolder.bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                        viewHolder.bubbleLayout.setArrowPosition(50).setArrowHeight(16);
                        viewHolder.bubbleLayout.setArrowWidth(16);
                        viewHolder.bubbleLayout.setBubbleColor(Color.parseColor("#4FC3F7"));
                        viewHolder.bubbleLayout.setCornersRadius(24);
                        viewHolder.bubbleLayout.setStrokeWidth(0);
                        viewHolder.timeView.setText(dateString);
                        viewHolder.messageText.setText(c.getMessage());
                        viewHolder.messageImage.setVisibility(View.GONE);

                        viewHolder.messageText.setTextColor(context.getResources().getColor(R.color.white));


                    } else {


                        Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(viewHolder.messageThumb);
                        viewHolder.bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
                        viewHolder.bubbleLayout.setArrowPosition(50).setArrowHeight(16);
                        viewHolder.bubbleLayout.setArrowWidth(16);
                        viewHolder.bubbleLayout.setBubbleColor(Color.parseColor("#ffffff"));
                        viewHolder.bubbleLayout.setCornersRadius(24);
                        viewHolder.bubbleLayout.setStrokeWidth(0);
                        viewHolder.timeView.setText(dateString);
                        viewHolder.messageText.setText(c.getMessage());
                        viewHolder.messageImage.setVisibility(View.GONE);
                    }


                } else {

                    /*viewHolder.messageText.setVisibility(View.INVISIBLE);*/
                    if (!c.getMessage().isEmpty()) {

                        if (from_user.equals(current_user_id)) {


                            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(viewHolder.messageThumb);
                            viewHolder.bubbleLayout.setArrowDirection(ArrowDirection.RIGHT);
                            viewHolder.bubbleLayout.setArrowPosition(50).setArrowHeight(16);
                            viewHolder.bubbleLayout.setArrowWidth(16);
                            viewHolder.bubbleLayout.setBubbleColor(Color.parseColor("#4FC3F7"));
                            viewHolder.bubbleLayout.setCornersRadius(24);
                            viewHolder.bubbleLayout.setStrokeWidth(0);
                            viewHolder.messageImage.setVisibility(View.VISIBLE);
                            viewHolder.messageText.setVisibility(View.GONE);
                            viewHolder.timeView.setText(dateString);


                            Picasso.get().load(c.getMessage()).placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);


                        } else {


                            Picasso.get().load(image).placeholder(R.drawable.default_avatar).into(viewHolder.messageThumb);
                            viewHolder.bubbleLayout.setArrowDirection(ArrowDirection.LEFT);
                            viewHolder.bubbleLayout.setArrowPosition(50).setArrowHeight(16);
                            viewHolder.bubbleLayout.setArrowWidth(16);
                            viewHolder.bubbleLayout.setBubbleColor(Color.parseColor("#ffffff"));
                            viewHolder.bubbleLayout.setCornersRadius(24);
                            viewHolder.bubbleLayout.setStrokeWidth(0);
                            viewHolder.messageImage.setVisibility(View.VISIBLE);
                            viewHolder.messageText.setVisibility(View.GONE);
                            viewHolder.timeView.setText(dateString);


                            Picasso.get().load(c.getMessage()).placeholder(R.drawable.default_avatar).into(viewHolder.messageImage);

                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }


}


