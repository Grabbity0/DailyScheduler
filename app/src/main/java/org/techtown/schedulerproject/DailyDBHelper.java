package org.techtown.schedulerproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DailyDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "memo.db";
    private static final int DATABASE_VERSION = 2;

    public DailyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS DateMemo(time text PRIMARY KEY, subj text, contents text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS DateMemo");
        onCreate(db);
    }

    // 데이터 입력
    public void Insert(String time, String subj, String contents) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DateMemo VALUES('" + time + "', '" + subj + "', '" + contents + "');");
        db.close();
    }

    // 데이터 수정
    public void UpdateLast(String time, String subj, String contents, String lastTime) {
        Log.d("d", time + subj + contents);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DateMemo SET time = '" + time + "', subj = '" + subj + "', contents = '" + contents + "' where time = '" + lastTime + "'");
        db.close();
        Log.d("d", "업데이트 완료");
    }

    public void Update(String time, String subj, String contents) {
        Log.d("d", time + subj + contents);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DateMemo SET time = '" + time + "', subj = '" + subj + "', contents = '" + contents + "' where time = '" + time + "'");
        db.close();
        Log.d("d", "업데이트 완료");
    }

    // 데이터 삭제
    public void Delete(String time) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DateMemo WHERE time = '" + time + "'");
        db.close();
    }

    public void DeleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DateMemo");
        db.close();
    }

}
