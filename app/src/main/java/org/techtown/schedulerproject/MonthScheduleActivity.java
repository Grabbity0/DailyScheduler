package org.techtown.schedulerproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MonthScheduleActivity extends AppCompatActivity {
    MonthDBHelper monthDbHelper;
    SQLiteDatabase db;
    TextView dateView;
    Button datebutton, savebutton;
    Switch markerchecker;
    EditText setcontents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        monthDbHelper =new MonthDBHelper(this);
        try{
            db = monthDbHelper.getWritableDatabase();
        }catch (SQLException e){
            db = monthDbHelper.getReadableDatabase();
        }


        dateView = findViewById(R.id.setDateText);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String now =sdf.format(new Date());
        dateView.setText(now);

        setcontents = findViewById(R.id.setcontents);
        markerchecker = findViewById(R.id.setMarkerSwitch);


        datebutton = findViewById(R.id.setDateButton);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });


        savebutton = findViewById(R.id.saveScheduleButton);
        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date, contents, marker;
                String errormsg = null;
                boolean errorcheker = false;

                /* 현재 날짜보다 앞의 날짜를 지정한다면 */
                date = dateView.getText().toString();
                Date checkdate = null;
                try {
                    checkdate = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(checkdate.before(new Date())){
                    errormsg = "#옳지 않은 날짜입니다.\n";
                    errorcheker = true;
                }

                contents = setcontents.getText().toString();
                /* 내용이 너무 길거나 내용이 없다면 */
                if (contents.length()>100){
                    errormsg = "#글자 수가 너무 많습니다.\n#100자 이하의 글자만 저장 가능합니다.";
                    errorcheker = true;
                }else if (contents==null || contents.length() < 1){
                    errormsg = "#내용을 입력 해 주세요.\n";
                    errorcheker = true;
                }

                /* 중요표시 체크 여부 */
                if(markerchecker.isChecked()){
                    marker="true";
                }else{
                    marker="false";
                }


                if (errorcheker){
                    Toast.makeText(MonthScheduleActivity.this, errormsg, Toast.LENGTH_SHORT).show();
                    errormsg=null;
                    errorcheker=false;
                }else{
                    db.execSQL("INSERT INTO Monthcontacts VALUES (null, '"+contents+"','"+date+"','"+marker+"');");
                    Toast.makeText(MonthScheduleActivity.this, "항목이 추가되었습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MonthScheduleActivity.this, MonthMainActivity.class);
                    startActivity(intent);

                    MonthScheduleActivity activity = MonthScheduleActivity.this;
                    activity.finish();
                }
            }
        });


    }
    public void processDatePickerResult(int year, int month, int day) {
        String yearString = Integer.toString(year);
        String monthString = Integer.toString(month+1);
        String dayString = Integer.toString(day);
        String dateString = yearString+"-"+monthString+"-"+dayString;
        dateView.setText(dateString);
        Toast.makeText(this, "Set Date : "+dateString, Toast.LENGTH_SHORT).show();
    }
}