package com.software.abiol.simesapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.activities.EditTimetable;
import com.software.abiol.simesapp.activities.TimetableDay;
import com.software.abiol.simesapp.utils.LetterImageView;

import java.util.List;

public class WeekdaysAdapter extends RecyclerView.Adapter<WeekdaysAdapter.ViewHolder> {

    private String[] days;
    private Context context;


    public WeekdaysAdapter(String[] days){
        this.days = days;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        context = parent.getContext();
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.week_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.letterImageView.setLetter(days[position].charAt(0));
        holder.dayView.setText(days[position]);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent dayTimetableIntent = new Intent(context, TimetableDay.class);
                dayTimetableIntent.putExtra("day",days[position]);
                context.startActivity(dayTimetableIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return days.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;

        LetterImageView letterImageView;
        TextView dayView;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            letterImageView = mView.findViewById(R.id.week_letter_imageView);
            dayView = mView.findViewById(R.id.week_day);
        }
    }
}
