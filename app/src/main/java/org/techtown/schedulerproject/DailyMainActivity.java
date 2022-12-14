package org.techtown.schedulerproject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;

import android.app.ActivityOptions;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import me.bastanfar.semicirclearcprogressbar.SemiCircleArcProgressBar;


public class DailyMainActivity extends AppCompatActivity {

    //DB
    DailyDBHelper dailyDbHelper;
    SQLiteDatabase db;

    RecyclerView recyclerView;
    //Handler
    final Handler handler = new Handler(Looper.getMainLooper());

    //Time
    TextClock textClock;

    //Progressbar
    SemiCircleArcProgressBar progressBar;

    //Fab
    private boolean fabMain_status = false;
    private FloatingActionButton fabMain;
    private FloatingActionButton fabCreate;
    private FloatingActionButton fabRemoveAll;

    //Intent
    ActivityResultLauncher<Intent> activityResultLauncher;

    TextView textView7;
    TextView textView8;

    String hour;
    String minute;
    String subj;
    String contents;
    String lastTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_main);

        textClock = findViewById(R.id.textClock);

        progressBar = findViewById(R.id.progressBar);

        recyclerView = findViewById(R.id.recyclerview);

        textView7 = findViewById(R.id.textView7);
        textView8 = findViewById(R.id.textView8);

        fabMain = findViewById(R.id.fabMain);
        fabCreate = findViewById(R.id.fabCreate);
        fabRemoveAll = findViewById(R.id.fabRemove);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    String getTime = dateFormat.format(date);
                    String time = getTime.replace(":", "");
                    String[] _getTime = getTime.split(":");
                    int nowTime = Integer.parseInt(time);
                    int _nowTime = (Integer.parseInt(_getTime[0]) * 60) + Integer.parseInt(_getTime[1]);

                    if(nowTime >= 0 && nowTime < 600){
                        textView8.setText("?????? ??? ????????? ????????????.");
                    }
                    else if(nowTime >= 600 && nowTime < 1200){
                        textView8.setText("?????? ???????????????. ????????? ?????? ????????????.");
                    }
                    else if(nowTime >= 1200 && nowTime < 1800){
                        textView8.setText("????????? ???????????????. ????????? ??? ???????????????.");
                    }
                    else{
                        textView8.setText("?????? ?????? ?????? ??????????????????.");
                    }

                    db = dailyDbHelper.getReadableDatabase();

                    if(db != null) {
                        Cursor cursor = db.rawQuery("SELECT * FROM DateMemo WHERE CAST(REPLACE(time, ':', '') AS INT) > " + nowTime + " ORDER BY time", null);
                        cursor.moveToFirst();
                        if (cursor != null && cursor.moveToFirst()) {
                            String t = cursor.getString(0);
                            String s = cursor.getString(1);
                            String[] _t = t.split(":");
                            int dbTime = (Integer.parseInt(_t[0]) * 60) + Integer.parseInt(_t[1]);
                            textView7.setText((dbTime - (_nowTime)) + "??? ?????? \n?????? ????????? '" + s + "'???????????????.");
                        }
                        else{
                            textView7.setText("?????? ????????? ???????????? ????????????.");
                        }
                    }

                    progressBar.setPercent(nowTime/ 24);
                    handler.postDelayed(this,1000);
                }
            }, 0,1000);
        }


        dailyDbHelper = new DailyDBHelper(this);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode() == 1001){
                Intent intent = result.getData();
                hour = intent.getStringExtra("hour");
                minute = intent.getStringExtra("minute");
                subj = intent.getStringExtra("subj");
                contents = intent.getStringExtra("contents");

                if(Integer.parseInt(hour) < 10){
                    hour = "0" + hour;
                }
                if(Integer.parseInt(minute) < 10){
                    minute = "0" + minute;
                }
                db = dailyDbHelper.getReadableDatabase();

                Cursor cursor = db.rawQuery("SELECT * FROM DateMemo ORDER BY time", null);
                int count = 0;
                while (cursor.moveToNext()) {
                    String t = cursor.getString(0);
                    if(t.equals(hour + ":" + minute)){
                        count++;
                    }
                }
                if(count == 0) {
                    dailyDbHelper.Insert(hour + ":" + minute, subj, contents);
                }
                else{
                    Intent cintent = new Intent(getApplicationContext(), OverLapPop.class);
                    activityResultLauncher.launch(cintent);
                }
            }
            else if(result.getResultCode() == 1002){
                Intent intent = result.getData();
                hour = intent.getStringExtra("hour");
                minute = intent.getStringExtra("minute");
                subj = intent.getStringExtra("subj");
                contents = intent.getStringExtra("contents");
                lastTime = intent.getStringExtra("lastTime");

                if(Integer.parseInt(hour) < 10){
                    hour = "0" + hour;
                }
                if(Integer.parseInt(minute) < 10){
                    minute = "0" + minute;
                }

                db = dailyDbHelper.getReadableDatabase();

                Cursor cursor = db.rawQuery("SELECT * FROM DateMemo WHERE time != '" + lastTime + "' ORDER BY time", null);
                int count = 0;
                while (cursor.moveToNext()) {
                    String t = cursor.getString(0);
                    if(t.equals(hour + ":" + minute)){
                        count++;
                    }
                }
                if(count == 0) {
                    dailyDbHelper.UpdateLast(hour + ":" + minute, subj, contents, lastTime);
                    Toast.makeText(DailyMainActivity.this, "?????? ??????", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent cintent = new Intent(getApplicationContext(), OverLapPop.class);
                    cintent.putExtra("lastTime", lastTime);
                    activityResultLauncher.launch(cintent);
                }
            }
            else if(result.getResultCode() == 1111){
                Intent intent = result.getData();
                String time = intent.getStringExtra("last");
                if(time != null){
                    dailyDbHelper.Delete(time);
                }
                dailyDbHelper.Update(hour + ":" + minute, subj, contents);
                Toast.makeText(DailyMainActivity.this, "?????????????????????.", Toast.LENGTH_SHORT).show();
            }
            else if(result.getResultCode() == 2222){
                dailyDbHelper.DeleteAll();
                Toast.makeText(DailyMainActivity.this, "????????? ????????? ??????.", Toast.LENGTH_SHORT).show();
            }
            else if(result.getResultCode() == 3333){
                Intent intent = result.getData();
                String time = intent.getStringExtra("time");
                dailyDbHelper.Delete(time);
                Toast.makeText(DailyMainActivity.this, "?????? ???????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
            }
            else if(result.getResultCode() == 0){
                Toast.makeText(DailyMainActivity.this, "?????????????????????.", Toast.LENGTH_SHORT).show();
            }
        });




        // ??????????????? ?????? ??????
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFab();

            }
        });
        // ?????? ????????? ?????? ??????
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MemoSettingActivity.class);
                activityResultLauncher.launch(intent);
            }
        });

        // ?????? ?????? ????????? ?????? ??????
        fabRemoveAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RemoveAllPop.class);
                activityResultLauncher.launch(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        db = dailyDbHelper.getReadableDatabase();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        MemoAdapter adapter = new MemoAdapter();

        if(db != null) {
            Cursor cursor = db.rawQuery("SELECT * FROM DateMemo ORDER BY time", null);
                while (cursor.moveToNext()) {
                    String t = cursor.getString(0);
                    String s = cursor.getString(1);
                    String c = cursor.getString(2);

                    adapter.addItem(new Memo(t, s, c));
                }

            recyclerView.setAdapter(adapter);
                Log.d("d", "?????????????????? ?????????");
        }

        adapter.setOnItemClickListener(new OnMemoItemClickListener() {
            @Override
            public void onItemClick(MemoAdapter.ViewHolder holder, View view, int position) {
                Memo item = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), MemoSettingActivity.class);
                intent.putExtra("time",item.getTime());
                intent.putExtra("subj",item.getSubject());
                intent.putExtra("cont",item.getContent());
                activityResultLauncher.launch(intent);
            }
        });

        adapter.setOnItemLongClickListener(new OnMemoItemLongClickListener() {
            @Override
            public void onItemLongClick(MemoAdapter.ViewHolder holder, View view, int position) {
                Memo item = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), RemoveOnePop.class);
                intent.putExtra("time", item.getTime());
                activityResultLauncher.launch(intent);
            }
        });
    }


    // ????????? ?????? ?????? ????????? ??????????????? ??????
    public void toggleFab() {
        if(fabMain_status) {
            // ????????? ?????? ?????? ??????
            // ??????????????? ??????
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCreate, "translationY", 0f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabRemoveAll, "translationY", 0f);
            fe_animation.start();
            // ?????? ????????? ????????? ??????
            fabMain.setImageResource(R.drawable.ic_add_foreground);

        }else {
            // ????????? ?????? ?????? ??????
            ObjectAnimator fc_animation = ObjectAnimator.ofFloat(fabCreate, "translationY", -400f);
            fc_animation.start();
            ObjectAnimator fe_animation = ObjectAnimator.ofFloat(fabRemoveAll, "translationY", -200f);
            fe_animation.start();
            // ?????? ????????? ????????? ??????
            fabMain.setImageResource(R.drawable.ic_add_background);
        }
        // ????????? ?????? ?????? ??????
        fabMain_status = !fabMain_status;
    }


    public void goFirstActivity(View view){
        Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this).toBundle();
        startActivity(intent, bundle);
    }

}