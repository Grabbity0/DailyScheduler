package org.techtown.schedulerproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MemoSettingActivity extends AppCompatActivity {
    TimePicker timePicker;
    EditText _subj;
    EditText _contents;
    Button complete;
    Button cancel;

    String hour;
    String minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_settings);

        timePicker = findViewById(R.id.timePicker);
        _subj = findViewById(R.id.editText);
        _contents = findViewById(R.id.editText2);
        complete = findViewById(R.id.button);
        cancel = findViewById(R.id.button2);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateHour = new SimpleDateFormat("HH");
        SimpleDateFormat dateMinute = new SimpleDateFormat("mm");
        String getHour = dateHour.format(date);
        String getMinute = dateMinute.format(date);

        timePicker.setHour(Integer.parseInt(getHour));
        timePicker.setMinute(Integer.parseInt(getMinute));

        Intent getIntent = getIntent();

        if(getIntent.getStringExtra("time") != null) {
            String getTime = getIntent.getStringExtra("time");
            String[] splitTime = getTime.split(":");

            timePicker.setHour(Integer.parseInt(splitTime[0]));
            timePicker.setMinute(Integer.parseInt(splitTime[1]));

            _subj.setText(getIntent.getStringExtra("subj"));
            _contents.setText(getIntent.getStringExtra("cont"));
        }

        hour = Integer.toString(timePicker.getHour()); // 안바꾸면 null값 반환
        minute = Integer.toString(timePicker.getMinute());

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int _hour, int _minute) {
                hour = Integer.toString(_hour); // 안바꾸면 null값 반환
                minute = Integer.toString(_minute); // 안바꾸면 null값 반환
            }
        });


        Intent intent = new Intent(getApplicationContext(), DailyMainActivity.class);

        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subj = _subj.getText().toString();
                String contents = _contents.getText().toString();
                String lastTime = getIntent.getStringExtra("time");
                if(hour != null && minute != null && subj.length() != 0) {
                    intent.putExtra("lastTime", lastTime);
                    intent.putExtra("hour", hour);
                    intent.putExtra("minute", minute);
                    intent.putExtra("subj", subj);
                    intent.putExtra("contents", contents);
                    if(getIntent.getStringExtra("time") == null) {
                        setResult(1001, intent);
                    }
                    else{
                        setResult(1002, intent);
                    }
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"시, 분, 제목은 필수 입력입니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }




}
