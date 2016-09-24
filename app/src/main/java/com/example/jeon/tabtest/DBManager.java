package com.example.jeon.tabtest;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-04-14.
 */
public class DBManager extends SQLiteOpenHelper {

    SQLiteDatabase diary;

    public DBManager(Context context) {
        super(context, "dodamDB.db", null, 1);

        diary = this.getWritableDatabase();
        createTable();

    }

    private void createInoculationTable(){
        String sql = "create table if not exists diary(imgurl TEXT , height TEXT , " +
                "weight TEXT, head TEXT , memo TEXT , date TEXT );";

    }


    private void createTable() {

        //쿼리준비
        String sql = "create table if not exists diary(imgurl TEXT , height TEXT , " +
                "weight TEXT, head TEXT , memo TEXT , date TEXT );";
        //쿼리 실행
        diary.execSQL(sql);

    }

    public void delete(String string) {
        diary = getWritableDatabase();
        String sql = "delete from diary where date = '" + string + "' ";
        diary.execSQL(sql);

        diary.close();

    }

    public void modify(String imgurl, String height, String weight, String head, String memo, String date) {
        diary = getWritableDatabase();
        String sql = "update diary set imgurl='" + imgurl + "', height='" + height + "', weight='" + weight + "', head='" + head + "', memo='" + memo + "' where date='" + date + "'";
        diary.execSQL(sql);

        diary.close();

    }

    public void insertDiary(String imgurl, String height, String weight, String head, String memo, String date) {
        //쿼리 준비
        diary = getWritableDatabase();

        String sql = "insert into diary values('" + imgurl + "','" + height + "'," +
                "'" + weight + "','" + head + "','" + memo + "','" + date + "');";
        //쿼리 실행하시오
        diary.execSQL(sql);
        diary.close();
    }

    public ArrayList<DataST> selectDB() {
        diary = getWritableDatabase();

        ArrayList<DataST> da = new ArrayList<DataST>();

        String sql = "select * from diary order by date desc";
        // String sql = "select * from diary limit '"+startnum+"','"+endnum+"' order by date desc";

        Cursor c = diary.rawQuery(sql, null);
        c.moveToFirst();

        while (c.isAfterLast() == false) {
            da.add(new DataST(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            c.moveToNext();
        }

        c.close();

        diary.close();
        return da;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        diary = getWritableDatabase();
        String sql = "drop table if exists diary";
        diary.execSQL(sql);
        createTable();
        diary.close();
    }

    public void createTable(String _query) {  //테이블을 생성
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public void insert(String _query) { //데이터 삽입
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(_query);
        db.close();
    }

    public int existsProfileData() {  //데이터가 존재하는지 확인
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from PROFILE", null);

        if (cursor.getCount() > 0) {    //데이터가 있으면 1, 없으면 2
            db.close();
            return 1;
        } else {
            db.close();
            return 2;
        }
    }

    public String[] getProfileData() { //DB의 데이터를 String 배열로 저장
        String[] data = new String[4];

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from PROFILE", null);

        while (cursor.moveToNext()) {
            data[0] = cursor.getString(1);
            data[1] = String.valueOf(cursor.getInt(2));
            data[2] = String.valueOf(cursor.getInt(3));
            data[3] = cursor.getString(4);
        }
        db.close();
        return data;
    }


    public ArrayList<RecordItem> getRecordDataType(int i, String day) { //타입별로 데이터 얻어오기

        ArrayList<RecordItem> data = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from BABY_RECORD where (_type=" + i + ") AND (day=" + day + ") order by _id desc", null);

        while (cursor.moveToNext()) {
            data.add(new RecordItem(cursor.getInt(0), cursor.getInt(1), String.valueOf(cursor.getInt(2)), String.valueOf(cursor.getInt(3)),
                    String.valueOf(cursor.getInt(4)), cursor.getString(5)));
        }
        db.close();
        return data;
    }

    public ArrayList<RecordItem> getRecordDataDay(String day) { //날짜별 데이터 얻어오기

        ArrayList<RecordItem> data = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from BABY_RECORD where day=" + day + " order by _id desc", null);

        while (cursor.moveToNext()) {
            data.add(new RecordItem(cursor.getInt(0), cursor.getInt(1), String.valueOf(cursor.getInt(2)), String.valueOf(cursor.getInt(3)),
                    String.valueOf(cursor.getInt(4)), cursor.getString(5)));
        }

        db.close();
        return data;
    }

    public void updateRecordData(int _id, int _type, int value, String day, String time) { //육아일일기록 데이터 수정
        SQLiteDatabase db = getWritableDatabase();
        switch (_type) {
            case 0:
            case 4:
                db.execSQL("UPDATE BABY_RECORD SET takes_time=" + value + ", day=" + day + ",time='" + time + "' WHERE _id=" + _id);
                break;
            case 1:
            case 2:
                db.execSQL("UPDATE BABY_RECORD SET amount=" + value + ", day=" + day + ",time='" + time + "' WHERE _id=" + _id);
                break;
            case 3:
                db.execSQL("UPDATE BABY_RECORD SET day=" + day + ",time='" + time + "' WHERE _id=" + _id);
                break;
        }

        db.close();
    }

    public void deleteRecordData(int _id) { //육아일일기록 데이터 삭제
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM BABY_RECORD WHERE _id=" + _id);
        db.close();
    }
}
