package org.techtown.schedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MonthMainActivity extends AppCompatActivity {
    MonthDBHelper monthDbHelper;
    SQLiteDatabase db;
    RecyclerView recyclerView;
    ArrayList<MonthSchedules> scheduleslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView textView = findViewById(R.id.todayView);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        textView.setText(sdf.format(new Date()));

        recyclerView = findViewById(R.id.recyclerview_main);
        scheduleslist = new ArrayList<>();

        monthDbHelper = new MonthDBHelper(this);
        try{
            db = monthDbHelper.getWritableDatabase();
        }catch (SQLException e){
            db = monthDbHelper.getReadableDatabase();
        }
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM Monthcontacts ORDER BY marker DESC, to_date ASC;", null);

        String id;
        String contents;
        String date;
        String marker;

        while (cursor.moveToNext()){
            id = cursor.getString(0);
            contents = cursor.getString(1);
            date = cursor.getString(2);
            marker = cursor.getString(3);

            if(marker.equals("true")){
                scheduleslist.add(new MonthSchedules(contents, date, R.drawable.blue_round_star ,id));
            }else if(marker.equals("false")){
                scheduleslist.add(new MonthSchedules(contents, date, R.drawable.round_star ,id));
            }
        }
        //scheduleslist.add(new Schedules("contents", "date", R.drawable.feathercolor ,"id"));
        MonthAdapter adapter = new MonthAdapter(scheduleslist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Button addSchedule = findViewById(R.id.insetButton);
        addSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MonthMainActivity.this, MonthScheduleActivity.class);
                startActivity(intent);
                MonthMainActivity activity = MonthMainActivity.this;
                activity.finish();
            }
        });
        Button deleteAll = findViewById(R.id.allDeleteButton);
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("DELETE FROM Monthcontacts;");
                scheduleslist.clear();
                adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);
            }
        });

        adapter.setClickListener(new MonthAdapter.OnClickListener() {
            @Override
            public void onClicked(MonthAdapter.ScheduleViewHolder viewHolder, View view, int position) {
                //Toast.makeText(MainActivity.this, adapter.getItem(position).getDataid(), Toast.LENGTH_SHORT).show();
                MonthSchedules date = adapter.getItem(position);
                Intent intent = new Intent(MonthMainActivity.this, EditMonthScheduleActivity.class);
                intent.putExtra("id", date.getDataid());
                intent.putExtra("contents", date.getContents());
                intent.putExtra("date", date.getDate());
                String mk;
                if (date.getMarker() == R.drawable.blue_round_star){
                    mk="true";
                }else{mk="false";}
                intent.putExtra("marker", mk);
                startActivity(intent);
                MonthMainActivity activity = MonthMainActivity.this;
                activity.finish();
            }
        });
    }

    public void goFirstActivity(View view){
        Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }
}