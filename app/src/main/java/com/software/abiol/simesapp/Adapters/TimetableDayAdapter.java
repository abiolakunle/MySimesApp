package com.software.abiol.simesapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.TimetableDayModel;
import com.software.abiol.simesapp.utils.LetterImageView;

import java.util.List;

public class TimetableDayAdapter extends RecyclerView.Adapter<TimetableDayAdapter.ViewHolder>{

    List<TimetableDayModel> timetableDayModels;



    public TimetableDayAdapter(List<TimetableDayModel> timetableDayModels) {

        this.timetableDayModels = timetableDayModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_single_course, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.courseTitle.setText(timetableDayModels.get(position).getCourse_title());
        holder.courseCode.setText(timetableDayModels.get(position).getCourse_code());
        holder.courseUnit.setText(timetableDayModels.get(position).getCourse_unit()+ " Unit(s)");
        holder.letterImageView.setLetter(timetableDayModels.get(position).getCourse_title().charAt(0));



    }

    @Override
    public int getItemCount() {
        return timetableDayModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView courseTitle, courseCode, courseUnit;
        private LetterImageView letterImageView;



        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            courseTitle = mView.findViewById(R.id.course_title);
            courseCode = mView.findViewById(R.id.week_day);
            courseUnit = mView.findViewById(R.id.course_unit);
            letterImageView = mView.findViewById(R.id.week_letter_imageView);
            letterImageView.setOval(true);

        }
    }


}
