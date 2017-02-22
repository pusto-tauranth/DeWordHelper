package com.lyz.dewordhelper.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 61998 on 2017/2/22.
 */

public class WordsHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="deutsch.sqlite";

    public WordsHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据表
        String CREATE_TABLE_STUDENT="CREATE TABLE "+ Word.TABLE+"("
                + Word.Key_Id +" TEXT PRIMARY KEY ,"
                + Word.Key_gender+" TEXT, "
                + Word.Key_word+" INTEGER, "
                + Word.Key_pl+" TEXT)";
        db.execSQL(CREATE_TABLE_STUDENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //如果旧表存在，删除，所以数据将会消失
        db.execSQL("DROP TABLE IF EXISTS "+ Word.TABLE);

        //再次创建表
        onCreate(db);
    }
}
