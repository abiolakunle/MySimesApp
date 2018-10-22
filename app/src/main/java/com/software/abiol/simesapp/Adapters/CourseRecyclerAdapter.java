package com.software.abiol.simesapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.models.Course;
import com.software.abiol.simesapp.utils.LetterImageView;

import java.util.List;

public class CourseRecyclerAdapter extends RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder> {

    public List<Course> courses;

    public CourseRecyclerAdapter(List<Course> courses){
        this.courses = courses;

    }


    @NonNull
    @Override
    public CourseRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_course_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseRecyclerAdapter.ViewHolder holder, int position) {


        holder.courseUnit.setText(courses.get(position).getCourse_unit());
        holder.courseTitle.setText(courses.get(position).getCourse_title());
        holder.courseCode.setText(courses.get(position).getCourse_code());
        holder.letterImageView.setLetter(courses.get(position).getCourse_title().charAt(0));
    }

    @Override
    public int getItemCount() {
        return courses.size();
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
