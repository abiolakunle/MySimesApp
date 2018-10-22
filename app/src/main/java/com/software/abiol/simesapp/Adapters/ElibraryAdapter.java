package com.software.abiol.simesapp.Adapters;

import android.content.Context;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Ebook;
import com.software.abiol.simesapp.utils.GetTimeAgo;
import com.software.abiol.simesapp.utils.LetterImageView;


import java.util.ArrayList;
import java.util.List;

public class ElibraryAdapter extends RecyclerView.Adapter<ElibraryAdapter.ViewHolder> {

    Context context;
    List<Ebook> books = new ArrayList<>();

    public ElibraryAdapter(){

    }

    public ElibraryAdapter(List<Ebook> books ){
        this.books = books;

    }


    @NonNull
    @Override
    public ElibraryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ebook_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ElibraryAdapter.ViewHolder holder, final int position) {

        GetTimeAgo getTimeAgo = new GetTimeAgo();

        String type = books.get(position).getFile_type();
        String time = books.get(position).getTime_uploaded();
        String timeAgo = getTimeAgo.getTimeAgo(Long.parseLong(time), context);
        String by = books.get(position).getUploaded_by();
        String nameFile = books.get(position).getFile_name();
        String fileSize = books.get(position).getFile_size()+"Kb";

        holder.fileName.setText(nameFile);
        holder.fileSize.setText(fileSize);
        holder.fileDesc.setText("[Type: "+ type +"] [Uploaded By: " + by+"] [Time: "+ timeAgo+"]" );
        holder.letterImageView.setLetter(nameFile.charAt(0));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setType(Intent.ACTION_VIEW); // denotes that we are going to view something
                intent.setData(Uri.parse(books.get(position).getUrl()));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        private TextView fileName, fileSize, fileDesc;
        private LetterImageView letterImageView;



        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            fileName = mView.findViewById(R.id.name_of_file);
            fileSize = mView.findViewById(R.id.file_size);
            fileDesc = mView.findViewById(R.id.file_desc);
            letterImageView = mView.findViewById(R.id.lib_letter_imageView);


        }


    }
}
