package com.software.abiol.simesapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.software.abiol.simesapp.R;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    public List<String> fileNameList;
    public List<String> fileDoneList;
    public List<Double> progressList;
    public double progressStatus;
    public List<Integer> progVisibility;
/*    public List<String> mechanical_list = new ArrayList<>(fileNameList.size());
    public List<String> mechanical_uri = new ArrayList<>(fileNameList.size());
    public List<String> electrical_list = new ArrayList<>(fileNameList.size());
    public List<String> maintenance_list = new ArrayList<>(fileNameList.size());
    public List<String> mathematics_list = new ArrayList<>(fileNameList.size());*/
    Context context;


    public UploadListAdapter(List<String> fileNameList, List<String> fileDoneList, List<Double> progressList, List<Integer> progVisibility){

        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;
        this.progressList = progressList;
        this.progressStatus = progressStatus;
        this.progVisibility = progVisibility;


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.upload_list_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String fileName = fileNameList.get(position);
        holder.fileNameView.setText(fileName);


        holder.progressBar.setVisibility(progVisibility.get(position));
        holder.progressBar.setProgress(progressList.get(position).intValue());


        String fileDone = fileDoneList.get(position);

        if(fileDone.equals("uploading")){

            holder.fileDoneView.setImageResource(android.R.drawable.checkbox_off_background);
        } else {
            holder.fileDoneView.setImageResource(android.R.drawable.checkbox_on_background);
        }

/*        holder.mechanical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mechanical_list.contains(fileName)) {
                    mechanical_list.add(position, fileName);
                    mechanical_uri.add(fileDoneList.get(position));
                    Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.electrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!electrical_list.contains(fileName)) {
                    electrical_list.add(position, fileName);
                    Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.maintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maintenance_list.set(position, fileName);
                Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
            }
        });

        holder.mathematics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mathematics_list.set(position, fileName);
                Toast.makeText(context, fileName, Toast.LENGTH_SHORT).show();
            }
        });*/



        holder.itemSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.listTags.getVisibility() ==  View.VISIBLE) {

                    holder.listTags.setVisibility(View.GONE);

                } else {

                    holder.listTags.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView fileNameView;
        public ImageView fileDoneView;
        private ConstraintLayout itemSelected;
        private LinearLayout listTags;
        private CheckBox mechanical, electrical, maintenance, mathematics;
        private ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            itemSelected = (ConstraintLayout) mView.findViewById(R.id.item_selected);
            listTags = (LinearLayout) mView.findViewById(R.id.list_tags);
            fileNameView = (TextView) mView.findViewById(R.id.upload_filename);
            fileDoneView = (ImageView) mView.findViewById(R.id.upload_loading);
            progressBar = (ProgressBar) mView.findViewById(R.id.upload_progress);


            mechanical = mView.findViewById(R.id.mechanical);
            electrical = mView.findViewById(R.id.electrical);
            maintenance = mView.findViewById(R.id.maintenance);
            mathematics = mView.findViewById(R.id.mathematics);


        }
    }
}
