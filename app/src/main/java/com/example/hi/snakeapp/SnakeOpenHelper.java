package com.example.hi.snakeapp;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by user on 7/5/2017.
 */

public class SnakeOpenHelper extends SQLiteOpenHelper {

    static private final String TABLE_NAME = "tbl_snake_score";
    static private final String TABLE_COLUMN_1 = "name";
    static private final String TABLE_COLUMN_2 = "score";

    /*    static private final int VERSION = 1;
        private static final String DATABASE_NAME = "snake.db";*/
    final String CREATE_TABLE = "create table " + TABLE_NAME + " (id integer primary key autoincrement, " + TABLE_COLUMN_1 + " text, " + TABLE_COLUMN_2 + " integer);";

    public SnakeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w("TASK", "Upgrading from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        sqLiteDatabase.execSQL(CREATE_TABLE);
       // onCreate(sqLiteDatabase);
    }
}
