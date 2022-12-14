package org.techtown.schedulerproject;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditMonthScheduleActivity extends AppCompatActivity {
    org.techtown.schedulerproject.MonthDBHelper monthDbHelper;
    SQLiteDatabase db;
    TextView dateView;
    Button datebutton, updatebutton;
    Switch markerchecker;
    EditText editcontents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editschedule);

        monthDbHelper =new org.techtown.schedulerproject.MonthDBHelper(this);
        try{
            db = monthDbHelper.getWritableDatabase();
        }catch (SQLException e){
            db = monthDbHelper.getReadableDatabase();
        }
        Intent intent = getIntent();

        TextView textView = findViewById(R.id.textView);
        textView.setText(intent.getStringExtra("id"));

        dateView = findViewById(R.id.editDateText);
        dateView.setText(intent.getStringExtra("date"));


        editcontents = findViewById(R.id.editcontents);
        editcontents.setText(intent.getStringExtra("contents"));
        markerchecker = findViewById(R.id.editMarkerSwitch);
        boolean mark;
        if (intent.getStringExtra("marker").equals("true")){
            markerchecker.setChecked(true);
        }


        datebutton = findViewById(R.id.editDateButton);
        datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(),"datePicker");
            }
        });


        updatebutton = findViewById(R.id.updateScheduleButton);
        updatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date, contents, marker;
                String errormsg = null;
                boolean errorcheker = false;

                /* ?????? ???????????? ?????? ????????? ??????????????? */
                date = dateView.getText().toString();
                Date checkdate = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    checkdate = sdf.parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(checkdate.before(new Date())){
                    errormsg = "#?????? ?????? ???????????????.\n";
                    errorcheker = true;
                }

                contents = editcontents.getText().toString();
                /* ????????? ?????? ????????? ????????? ????????? */
                if (contents.length()>100){
                    errormsg = "#?????? ?????? ?????? ????????????.\n#100??? ????????? ????????? ?????? ???????????????.";
                    errorcheker = true;
                }else if (contents==null || contents.length() < 1){
                    errormsg = "#????????? ?????? ??? ?????????.\n";
                    errorcheker = true;
                }

                /* ???????????? ?????? ?????? */
                if(markerchecker.isChecked()){
                    marker="true";
                }else{
                    marker="false";
                }


                if (errorcheker){
                    Toast.makeText(EditMonthScheduleActivity.this, errormsg, Toast.LENGTH_SHORT).show();
                    errormsg=null;
                    errorcheker=false;
                }else{
                    db.execSQL("UPDATE Monthcontacts SET contents='"+contents+"',to_date='"+date+"',marker='"+marker+"' WHERE _id = "+Integer.parseInt(textView.getText().toString())+";");
                    Toast.makeText(EditMonthScheduleActivity.this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditMonthScheduleActivity.this, MonthMainActivity.class);
                    startActivity(intent);

                    EditMonthScheduleActivity activity = EditMonthScheduleActivity.this;
                    activity.finish();
                }
            }
        });
        Button deleteButton = findViewById(R.id.deleteScheduleButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = Integer.parseInt(textView.getText().toString());
                db.execSQL("DELETE FROM Monthcontacts WHERE _id='"+id+"'");
                Toast.makeText(EditMonthScheduleActivity.this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditMonthScheduleActivity.this, MonthMainActivity.class);
                startActivity(intent);

                EditMonthScheduleActivity activity = EditMonthScheduleActivity.this;
                activity.finish();
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