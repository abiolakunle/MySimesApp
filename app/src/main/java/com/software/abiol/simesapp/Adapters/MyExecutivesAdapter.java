package com.software.abiol.simesapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Executives;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyExecutivesAdapter extends RecyclerView.Adapter<MyExecutivesAdapter.ViewHolder> {

    private List<Executives> list;
    private Context context;


    public MyExecutivesAdapter(List<Executives> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyExecutivesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        context = parent.getContext();

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_grid_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyExecutivesAdapter.ViewHolder holder, int position) {

        String[] positionArray = context.getResources().getStringArray(R.array.posts);
        String first_name = list.get(position).getFirst_name();
        String last_name = list.get(position).getLast_name();
        String image = list.get(position).getImage();
        String post = list.get(position).getPosition();
        String phoneNumber = list.get(position).getPhone_number();

        Glide.with(context).load(image).into(holder.executiveImage);

        holder.executiveName.setText(first_name+" "+last_name);
        holder.executivePosition.setText(positionArray[Integer.valueOf(post)]);
        holder.executivePhone.setText(phoneNumber);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        private CircleImageView executiveImage;
        private TextView executiveName;
        private TextView executivePosition;
        private TextView executivePhone;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            executivePosition = mView.findViewById(R.id.executive_position);
            executiveName = mView.findViewById(R.id.executive_name);
            executivePhone = mView.findViewById(R.id.executive_phone);
            executiveImage = mView.findViewById(R.id.executive_image);


        }
    }
}
