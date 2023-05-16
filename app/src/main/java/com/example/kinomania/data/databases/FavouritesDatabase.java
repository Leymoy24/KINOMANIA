package com.example.kinomania.data.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavouritesDatabase extends SQLiteOpenHelper {

    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "CinemaDB";
    private static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String ITEM_TITLE = "itemTitle";
    public static String ITEM_ADDRESS = "itemaddress";
    public static String FAVOURITE_STATUS = "fStatus";
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + '('
            + KEY_ID + " TEXT," + ITEM_TITLE + " TEXT,"
            + ITEM_ADDRESS + " TEXT," +FAVOURITE_STATUS + " TEXT)";

    public FavouritesDatabase(Context context){
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //делаем пустую таблицу
    public void insertEmpty(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //вводим наши значения cv
        for(int x = 1; x < 101; x++){
            cv.put(KEY_ID, x);
            cv.put(FAVOURITE_STATUS, "0");
            db.insert(TABLE_NAME, null, cv);
        }
    }

    //добавляем данные в БД
    public void insertIntoDatabase(String item_title, String item_address, String id, String fav_status){
        SQLiteDatabase db;
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TITLE, item_title);
        cv.put(ITEM_ADDRESS, item_address);
        cv.put(KEY_ID, id);
        cv.put(FAVOURITE_STATUS, fav_status);
        db.insert(TABLE_NAME, null, cv);
        Log.i("Logcat", item_title + ", favstatus - " + fav_status + "- . " + cv);
    }

    // считываем все данные
    public Cursor read_all_data(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "select * from " + TABLE_NAME + " where " + KEY_ID + "=" + id +"";
        return db.rawQuery(sql, null, null);
    }

    //удаляем строку из БД
    public void remove_fav(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "UPDATE " + TABLE_NAME + " SET  " + FAVOURITE_STATUS + " ='0' WHERE " + KEY_ID + "=" + id + "";
        db.execSQL(sql);
        Log.i("Logcat", "remove " + id.toString());
    }

    // выбираем весь лист с любимыми кинотеатрами
    public Cursor select_all_favorite_list(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + FAVOURITE_STATUS + " ='1'";
        return db.rawQuery(sql, null, null);
    }

}
