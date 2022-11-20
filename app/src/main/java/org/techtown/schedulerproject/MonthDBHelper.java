package org.techtown.schedulerproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MonthDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MonthScheduler.db";
    private static final int DATABASE_VERSION = 2;

    /* 세번째 인수 factory 표준 cursor를 사용할 셩우 null로 지정 */
    public MonthDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Monthcontacts (_id INTEGER PRIMARY KEY AUTOINCREMENT, contents text, to_date date, marker text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Monthcontacts");
        onCreate(sqLiteDatabase);
    }
}