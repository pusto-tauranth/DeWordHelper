package com.lyz.dewordhelper.DB;

import android.content.ContentValues;
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

    public static void setTrainingTimes(int newNum,String type){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String WhereDate=" WHERE "+Word.Key_date_2+" = '"+timestamp+"'";

        String update;
        String insert;
        if(type.equals("Gender")){
            update="update "+Word.TABLE_2+" set "+Word.Key_trainingGender_2 +"="+newNum+"+"+Word.Key_trainingGender_2 +WhereDate;
            insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+0+"','"+newNum+"','"+0+"','"+0+"')";
        }else{
            update="update "+Word.TABLE_2+" set "+Word.Key_trainingPlural_2 +"="+newNum+"+"+Word.Key_trainingPlural_2 +WhereDate;
            insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+0+"','"+0+"','"+0+"','"+newNum+"')";
        }
        if(getWordTotal(Word.TABLE_2,WhereDate)==0){
            db.execSQL(insert);
        }else {
            db.execSQL(update);
        }
        db.close();
    }

    public static void setErrorTimes(String type){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String whereError=" WHERE "+Word.Key_status+" = -1 ";
        int newError=WordsAccess.getWordTotal(Word.TABLE,whereError);

        String WhereDate=" WHERE "+Word.Key_date_2+" = '"+timestamp+"'";
        String update;
        String insert;
        if(type.equals("Gender")){
            update="update "+Word.TABLE_2+" set "+Word.Key_errorGender_2 +"="+newError+"+"+Word.Key_errorGender_2 +WhereDate;
            insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+newError+"','"+0+"','"+0+"','"+0+"')";
        }else{
            update="update "+Word.TABLE_2+" set "+Word.Key_errorPlural_2 +"="+newError+"+"+Word.Key_errorPlural_2 +WhereDate;
            insert="INSERT INTO "+Word.TABLE_2+" VALUES ('"+timestamp+"','"+0+"','"+0+"','"+newError+"','"+0+"')";
        }

        if(getWordTotal(Word.TABLE_2,WhereDate)==0){
            db.execSQL(insert);
        }else {
            db.execSQL(update);
        }
        db.close();
    }

    public static void setAccuracy(){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String update="update "+Word.TABLE+" set "+Word.Key_accuracyGender +
                "=100*("+Word.Key_trainingGender +"-"+Word.Key_errorGender +")/"+Word.Key_trainingGender +
                " " +
                " WHERE "+Word.Key_trainingGender +" !=0";
        db.execSQL(update);
        String update2="update "+Word.TABLE+" set "+Word.Key_accuracyPlural +
                "=100*("+Word.Key_trainingPlural +"-"+Word.Key_errorPlural +")/"+Word.Key_trainingPlural +
                " " +
                " WHERE "+Word.Key_trainingPlural +" !=0";
        db.execSQL(update2);
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
                word.trainingGender_2 =cursor.getInt(cursor.getColumnIndex(Word.Key_trainingGender_2));
                word.errorGender_2 =cursor.getInt(cursor.getColumnIndex(Word.Key_errorGender_2));
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
        values.put(Word.Key_plural,word.plural);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_book,book);
        values.put(Word.Key_unit,einheit);
        values.put(Word.Key_errorGender,0);
        values.put(Word.Key_status,0);
        values.put(Word.Key_trainingGender,0);
        values.put(Word.Key_errorPlural,0);
        values.put(Word.Key_trainingPlural,0);
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
        values.put(Word.Key_plural,word.plural);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_errorGender,word.errorGender);
        values.put(Word.Key_status,word.status);
        values.put(Word.Key_trainingGender,word.trainingGender);
        values.put(Word.Key_errorPlural,word.errorPlural);
        values.put(Word.Key_status,word.status);
        values.put(Word.Key_trainingPlural,word.trainingPlural);
        int changed_num=db.update(Word.TABLE,values,Word.Key_Id +"=?",new String[]{String.valueOf(word.word_Id)});
        db.close();
        return changed_num;
    }

    public static  int getWordTotal(String table,String Where){//返回符合where条件的记录数
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
        String selectQuery="SELECT * FROM "+Word.TABLE+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_unit +" = "+Einheit;
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
                        cursor.getString(cursor.getColumnIndex(Word.Key_plural))+"  "+
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
                        cursor.getString(cursor.getColumnIndex(Word.Key_plural))+"  "+
                        cursor.getString(cursor.getColumnIndex(Word.Key_chn))+"(正确率"+
                        cursor.getString(cursor.getColumnIndex(Word.Key_accuracyGender))+"%)");
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
                " FROM "+Word.TABLE
                +" WHERE "+
                Word.Key_Id +" = ?";
        Word word=new Word();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});

        if(cursor.moveToFirst()){
            do{
                word.word_Id =cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
                word.gender=cursor.getString(cursor.getColumnIndex(Word.Key_gender));
                word.word=cursor.getString(cursor.getColumnIndex(Word.Key_word));
                word.plural =cursor.getString(cursor.getColumnIndex(Word.Key_plural));
                word.chn=cursor.getString(cursor.getColumnIndex(Word.Key_chn));
                word.book=cursor.getInt(cursor.getColumnIndex(Word.Key_book));
                word.unit =cursor.getInt(cursor.getColumnIndex(Word.Key_unit));
                word.status=cursor.getInt(cursor.getColumnIndex(Word.Key_status));
                word.errorGender =cursor.getInt(cursor.getColumnIndex(Word.Key_errorGender));
                word.trainingGender =cursor.getInt(cursor.getColumnIndex(Word.Key_trainingGender));
                word.accuracyGender =cursor.getInt(cursor.getColumnIndex(Word.Key_accuracyGender));
                word.errorPlural =cursor.getInt(cursor.getColumnIndex(Word.Key_errorPlural));
                word.trainingPlural =cursor.getInt(cursor.getColumnIndex(Word.Key_trainingPlural));
                word.accuracyPlural =cursor.getInt(cursor.getColumnIndex(Word.Key_accuracyPlural));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return word;
    }

    public static Word getWordByListId(String Book,String Einheit,int listId){
        Word word;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * FROM "+Word.TABLE+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_unit +" = "+Einheit;
        System.out.println(selectQuery);
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
