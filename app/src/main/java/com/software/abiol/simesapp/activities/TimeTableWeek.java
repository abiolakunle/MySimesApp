package com.software.abiol.simesapp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.software.abiol.simesapp.Adapters.WeekdaysAdapter;
import com.software.abiol.simesapp.R;
import com.software.abiol.simesapp.utils.DrawerUtil;
import com.software.abiol.simesapp.utils.LetterImageView;

import java.util.List;

public class TimeTableWeek extends AppCompatActivity {

    private RecyclerView weekView;
    private String[] days_list;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table_week);

        //setActionBar
        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("My Time Table");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //call material drawer
        DrawerUtil.getMyDrawer(this, savedInstanceState, mToolbar);

        weekView = findViewById(R.id.week_view);

        days_list = getResources().getStringArray(R.array.Week);

        ;

/*
        days_list.add("MONDAY");
        days_list.add("TUESDAY");
        days_list.add("WEDNESDAY");
        days_list.add("THURSDAY");
        days_list.add("FRIDAY");
        days_list.add("SATURDAY");
*/

        WeekdaysAdapter weekdaysAdapter = new WeekdaysAdapter(days_list);
        weekView.setLayoutManager(new LinearLayoutManager(this));
        weekView.setAdapter(weekdaysAdapter);



        /*listView = findViewById(R.id.listView);
        setupListView();*/
    }










   /* private void setupListView(){

        String[] week = getResources().getStringArray(R.array.Week);

        WeekAdapter adapter = new WeekAdapter(this, R.layout.week_single_item, week);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }


    public class WeekAdapter extends ArrayAdapter{
        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};

        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource);
            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){

                holder = new ViewHolder();
                convertView = layoutInflater.inflate(resource, parent, false);
                holder.letterImageView = (LetterImageView) convertView.findViewById(R.id.week_letter_imageView);
                holder.day = (TextView) convertView.findViewById(R.id.week_day);
                convertView.setTag(holder);

            } else {

                holder = (ViewHolder)convertView.getTag();
            }

            holder.letterImageView.setOval(true);
            holder.letterImageView.setLetter(week[position].charAt(0));
            holder.day.setText(week[position]);

            return convertView;
        }

        class ViewHolder{
            private LetterImageView letterImageView;
            private TextView day;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home : {
                onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }

*/
}
