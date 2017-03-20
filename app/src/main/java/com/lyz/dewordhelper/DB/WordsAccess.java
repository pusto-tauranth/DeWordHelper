package com.lyz.dewordhelper.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by 61998 on 2017/2/22.
 */

public class WordsAccess {

    public static String timestamp(String format,int offset){/*recommend:"yyyy-MM-dd";offset=-1,昨天；0，当前；1：明天*/
        Date date=new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(date);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, offset);
        date = calendar.getTime();

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static int statusReset(){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);

        Word word= new Word();
        word.status=0;
        ContentValues values=new ContentValues();
        values.put(Word.Key_status,word.status);
        int changed_num=db.update(Word.TABLE,values,Word.Key_status + " !=0",null);
        db.close();
        return changed_num;
    }

    public static void setTrainingTimes(int new_num){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String WhereDate=" WHERE "+Word.Key_date_2+" = '"+timestamp+"'";

        String update="update "+Word.TABLE_2+" set "+Word.Key_training_2+"="+new_num+"+"+Word.Key_training_2+WhereDate;
        String insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+new_num+"','"+ 0+"')";

        if(getWordTotal(Word.TABLE_2,WhereDate)==0){
            db.execSQL(insert);
        }else {
            db.execSQL(update);
        }
        db.close();
    }

    public static void setErrorTimes(){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String WHERE0=" WHERE "+Word.Key_status+" = -1 ";
        int newError=WordsAccess.getWordTotal(Word.TABLE,WHERE0);

        String WhereDate=" WHERE "+Word.Key_date_2+" = '"+timestamp+"'";

        String update="update "+Word.TABLE_2+" set "+Word.Key_errortimes_2+"="+newError+"+"+Word.Key_errortimes_2+WhereDate;
        String insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+0+"','"+newError+"')";

        if(getWordTotal(Word.TABLE_2,WhereDate)==0){
            db.execSQL(insert);
        }else {
            db.execSQL(update);
        }
        db.close();
    }

    public static void setAccuracy(){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String update="update "+Word.TABLE+" set "+Word.Key_accuracy+
                "=("+Word.Key_training+"-"+Word.Key_errortimes+")/"+Word.Key_training+
                " " +
                " WHERE "+Word.Key_training+" !=0";
        db.execSQL(update);
        db.close();
    }

    public static Word getTrainingTimes(String timestamp){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * "+
                " FROM "+Word.TABLE_2
                +" WHERE "+
                Word.Key_date_2 +" = ?";
        Word word=new Word();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(timestamp)});

        if(cursor.moveToFirst()){
            do{
                word.date_2=cursor.getString(cursor.getColumnIndex(Word.Key_date_2));
                word.training_2=cursor.getInt(cursor.getColumnIndex(Word.Key_training_2));
                word.errortimes_2=cursor.getInt(cursor.getColumnIndex(Word.Key_errortimes_2));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return word;
    }

    public static int insert(Word word,int book,int einheit){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_pl,word.pl);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_book,book);
        values.put(Word.Key_einheit,einheit);
        values.put(Word.Key_errortimes,0);
        values.put(Word.Key_status,0);
        values.put(Word.Key_training,0);
        long word_ID=db.insert(Word.TABLE,null,values);

        db.close();
        return (int)word_ID;
    }

    public static void delete(int word_ID){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        db.delete(Word.TABLE,Word.Key_Id +" = ?", new String[]{String.valueOf(word_ID)});
        db.close();
    }

    public static int update(Word word){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_pl,word.pl);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_errortimes,word.errortimes);
        values.put(Word.Key_date,word.date);
        values.put(Word.Key_status,word.status);
        values.put(Word.Key_training,word.training);
        int changed_num=db.update(Word.TABLE,values,Word.Key_Id +"=?",new String[]{String.valueOf(word.word_Id)});
        db.close();
        return changed_num;
    }

    public static  int getWordTotal(String table,String Where){//返回符合where限定的记录数
        int total=0;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * FROM "+table+Where;
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                total++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return total;
    }
    public static  int getListWordTotal(String Book,String Einheit){
        int total=0;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * FROM "+Word.TABLE+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_einheit+" = "+Einheit;
        Cursor cursor=db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                total++;
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return total;
    }

    public static ArrayList<HashMap<String,String>> getWordList(String WHERE){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * "+ " FROM "+Word.TABLE+" "+WHERE;
        ArrayList<HashMap<String,String>> wordList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                HashMap<String,String> map=new HashMap<>();
                map.put("All",cursor.getString(cursor.getColumnIndex(Word.Key_gender))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_word))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_pl))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_chn)));
                map.put("wordId",cursor.getString(cursor.getColumnIndex(Word.Key_Id)));
                wordList.add(map);
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return wordList;
    }

    public static ArrayList<HashMap<String,String>> getLimitWordList(String WHERE,int lim){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * "+ " FROM "+Word.TABLE+" "+WHERE;
        ArrayList<HashMap<String,String>> wordList=new ArrayList<>();
        Cursor cursor=db.rawQuery(selectQuery,null);
        int t=0;
        if(cursor.moveToFirst()){
            do{
                t+=1;
                HashMap<String,String> map=new HashMap<>();
                map.put("All",cursor.getString(cursor.getColumnIndex(Word.Key_gender))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_word))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_pl))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_chn))+"(正确率"+
                        cursor.getString(cursor.getColumnIndex(Word.Key_accuracy))+"%)");
                map.put("wordId",cursor.getString(cursor.getColumnIndex(Word.Key_Id)));
                wordList.add(map);
            }while(cursor.moveToNext()&&t<lim);
        }

        cursor.close();
        db.close();
        return wordList;
    }

    public static Word getWordById(int Id){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * "+
                /*Word.Key_Id +","+
                Word.Key_gender+","+
                Word.Key_word+","+
                Word.Key_pl+","+
                Word.Key_chn+","+
                Word.Key_book +","+//LZ
                Word.Key_errortimes+","+
                Word.Key_date+","+      //newly added
                Word.Key_einheit+ */" FROM "+Word.TABLE
                +" WHERE "+
                Word.Key_Id +" = ?";
        Word word=new Word();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});

        if(cursor.moveToFirst()){
            do{
                word.word_Id =cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
                word.gender=cursor.getString(cursor.getColumnIndex(Word.Key_gender));
                word.word=cursor.getString(cursor.getColumnIndex(Word.Key_word));
                word.pl=cursor.getString(cursor.getColumnIndex(Word.Key_pl));
                word.chn=cursor.getString(cursor.getColumnIndex(Word.Key_chn));
                word.book=cursor.getInt(cursor.getColumnIndex(Word.Key_book));
                word.einheit=cursor.getInt(cursor.getColumnIndex(Word.Key_einheit));
                word.status=cursor.getInt(cursor.getColumnIndex(Word.Key_status));
                word.errortimes=cursor.getInt(cursor.getColumnIndex(Word.Key_errortimes));
                word.training=cursor.getInt(cursor.getColumnIndex(Word.Key_training));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return word;
    }

    public static Word getWordByListId(String Book,String Einheit,int listId){
        Word word;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * FROM "+Word.TABLE+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_einheit+" = "+Einheit;
        Cursor cursor=db.rawQuery(selectQuery,null);
        int i=0;
        int id=0;
        if(cursor.moveToFirst()){
            do{
                i++;
                id=cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
            }while(cursor.moveToNext()&&i<listId);
        }
        word=getWordById(id);
        cursor.close();
        db.close();
        return word;
    }
}
