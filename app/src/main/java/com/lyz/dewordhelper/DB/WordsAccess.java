package com.lyz.dewordhelper.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 61998 on 2017/2/22.
 */

public class WordsAccess {

    //private WordsHelper wordsHelper;

    //public WordsAccess(Context context){
        //wordsHelper=new WordsHelper(context);
    //}

    public static int insert(Word word,int book,int einheit){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_pl,word.pl);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_book,book);
        values.put(Word.Key_einheit,einheit);
        long word_ID=db.insert(Word.TABLE,null,values);

        db.close();
        return (int)word_ID;
    }

    public static void delete(int word_ID){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        db.delete(Word.TABLE,Word.Key_Id +" = ?", new String[]{String.valueOf(word_ID)});
        db.close();
    }

    public static void update(Word word){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_pl,word.pl);
        values.put(Word.Key_chn,word.chn);

        db.update(Word.TABLE,values,Word.Key_Id +"=?",new String[]{String.valueOf(word.word_Id)});
        db.close();
    }

    public static ArrayList<HashMap<String,String>> getWordList(String WHERE){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT "+
                Word.Key_Id +","+
                Word.Key_gender+","+
                Word.Key_word+","+
                Word.Key_pl+","+
                Word.Key_chn+","+
                Word.Key_book +","+
                Word.Key_einheit+" FROM "+Word.TABLE+" "+WHERE;
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

    public static Word getWordById(int Id){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT "+
                Word.Key_Id +","+
                Word.Key_gender+","+
                Word.Key_word+","+
                Word.Key_pl+","+
                Word.Key_chn+","+
                Word.Key_book +","+//LZ
                Word.Key_einheit+ " FROM "+Word.TABLE
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
                word.einheit=cursor.getInt(cursor.getColumnIndex(Word.Key_einheit));//LZ
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return word;
    }
}
