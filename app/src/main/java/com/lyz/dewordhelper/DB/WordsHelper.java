package com.lyz.dewordhelper.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.lyz.dewordhelper.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 61998 on 2017/2/22.
 */

public class WordsHelper{
    private final int Buffer_size=400000;
    public static final String DB_name="deutsch.db";
    public static final String Package_name="com.lyz.dewordhelper";
    public static final String DB_dir = "/data"+
            Environment.getDataDirectory().getAbsolutePath()+"/"+
            Package_name+"/";
    public static final String DB_path=DB_dir+DB_name;

    private SQLiteDatabase db;
    private Context context;

    public WordsHelper(Context context){
        this.context=context;
    }

    public void openDatabase(){
        this.db=this.openDatabase(DB_path);
    }

    private SQLiteDatabase openDatabase(String dbFile){
        try{
            if(!new File(dbFile).exists()){
                InputStream is=this.context.getResources().openRawResource(R.raw.deutsch);
                FileOutputStream fos=new FileOutputStream(dbFile);
                byte[] buffer=new byte[Buffer_size];
                int count=0;
                while((count=is.read(buffer))>0){
                    fos.write(buffer,0,count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(dbFile,null);
            return db;
        }catch (FileNotFoundException e){
            Log.e("Database", "File not found");
            e.printStackTrace();
        }catch (IOException e){
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.db.close();
    }
}

/*public class WordsHelper extends SQLiteOpenHelper {
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
}*/
