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
        int changed_num;
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
             changed_num = db.update(Word.TABLE, values, Word.Key_status + " !=0", null);
        }
        else{
            changed_num = db.update(Word.TABLE_French, values, Word.Key_status + " !=0", null);
        }
        db.close();
        return changed_num;
    }

    public static void setTrainingTimes(int newNum,String type){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String WhereDate;
        String update;
        String insert;
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            WhereDate = " WHERE " + Word.Key_date_GermanStatistics + " = '" + timestamp + "'";
            if (type.equals("Gender")) {
                update = "update " + Word.TABLE_GermanStatistics + " set " + Word.Key_trainingGender_GermanStatistics + "=" + newNum + "+" + Word.Key_trainingGender_GermanStatistics + WhereDate;
                insert = "INSERT INTO " + Word.TABLE_GermanStatistics + " VALUES ('" + timestamp + "','" + 0 + "','" + newNum + "','" + 0 + "','" + 0 + "')";
            } else {
                update = "update " + Word.TABLE_GermanStatistics + " set " + Word.Key_trainingPlural_GermanStatistics + "=" + newNum + "+" + Word.Key_trainingPlural_GermanStatistics + WhereDate;
                insert = "INSERT INTO " + Word.TABLE_GermanStatistics + " VALUES ('" + timestamp + "','" + 0 + "','" + 0 + "','" + 0 + "','" + newNum + "')";
            }
            if (getWordTotal(Word.TABLE_GermanStatistics, WhereDate) == 0) {
                db.execSQL(insert);
            } else {
                db.execSQL(update);
            }
            db.close();
        }
        else{
            WhereDate = " WHERE " + Word.Key_date_FrenchStatistics + " = '" + timestamp + "'";
                update = "update " + Word.TABLE_FrenchStatistics + " set " + Word.Key_trainingGender_FrenchStatistics + "=" + newNum + "+" + Word.Key_trainingGender_FrenchStatistics + WhereDate;
                insert = "INSERT INTO " + Word.TABLE_FrenchStatistics + " VALUES ('" + timestamp + "','" + 0 + "','" + newNum + "','" +  "')";
            if (getWordTotal(Word.TABLE_FrenchStatistics, WhereDate) == 0) {
                db.execSQL(insert);
            } else {
                db.execSQL(update);
            }
            db.close();
        }
    }

    public static void setErrorTimes(String type){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String timestamp=WordsAccess.timestamp("yyyy-MM-dd",0);
        String whereError=" WHERE "+Word.Key_status+" = -1 ";
        String WhereDate;
        String update;
        String insert;
        if(WordsAccess.getSettingValueByName("language").equals("德语")){
            int newError=WordsAccess.getWordTotal(Word.TABLE,whereError);
            WhereDate=" WHERE "+Word.Key_date_GermanStatistics+" = '"+timestamp+"'";
            if(type.equals("Gender")){
                update="update "+Word.TABLE_GermanStatistics+" set "+Word.Key_errorGender_GermanStatistics +"="+newError+"+"+Word.Key_errorGender_GermanStatistics +WhereDate;
                insert="INSERT INTO "+Word.TABLE_GermanStatistics+" VALUES ('"+timestamp+"','"+newError+"','"+0+"','"+0+"','"+0+"')";
            }else{
                update="update "+Word.TABLE_GermanStatistics+" set "+Word.Key_errorPlural_GermanStatistics +"="+newError+"+"+Word.Key_errorPlural_GermanStatistics +WhereDate;
                insert="INSERT INTO "+Word.TABLE_GermanStatistics+" VALUES ('"+timestamp+"','"+0+"','"+0+"','"+newError+"','"+0+"')";
            }

            if(getWordTotal(Word.TABLE_GermanStatistics,WhereDate)==0){
                db.execSQL(insert);
            }else {
                db.execSQL(update);
            }
            db.close();
        }
        else{
            int newError=WordsAccess.getWordTotal(Word.TABLE_French,whereError);
            WhereDate=" WHERE "+Word.Key_date_FrenchStatistics+" = '"+timestamp+"'";
                update="update "+Word.TABLE_FrenchStatistics+" set "+Word.Key_errorGender_FrenchStatistics +"="+newError+"+"+Word.Key_errorGender_FrenchStatistics +WhereDate;
                insert="INSERT INTO "+Word.TABLE_FrenchStatistics+" VALUES ('"+timestamp+"','"+newError+"','"+"')";
            if(getWordTotal(Word.TABLE_FrenchStatistics,WhereDate)==0){
                db.execSQL(insert);
            }else {
                db.execSQL(update);
            }
            db.close();
        }
    }

    public static void setAccuracy(){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String update="update "+Word.TABLE+" set "+Word.Key_accuracyGender +
                "=100*("+Word.Key_trainingGender +"-"+Word.Key_errorGender +")/"+Word.Key_trainingGender +
                " " +
                " WHERE "+Word.Key_trainingGender +" !=0";
        db.execSQL(update);
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            String update2 = "update " + Word.TABLE + " set " + Word.Key_accuracyPlural +
                    "=100*(" + Word.Key_trainingPlural + "-" + Word.Key_errorPlural + ")/" + Word.Key_trainingPlural +
                    " " +
                    " WHERE " + Word.Key_trainingPlural + " !=0";
            db.execSQL(update2);
        }
        db.close();
    }

    public static Word getTrainingTimes(String timestamp){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery;
        Word word=new Word();
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
             selectQuery = "SELECT * " +
                    " FROM " + Word.TABLE_GermanStatistics
                    + " WHERE " +
                    Word.Key_date_GermanStatistics + " = ?";
            Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(timestamp)});
            if (cursor.moveToFirst()) {
                do {
                    word.date_GermanStatistics = cursor.getString(cursor.getColumnIndex(Word.Key_date_GermanStatistics));
                    word.trainingGender_GermanStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_trainingGender_GermanStatistics));
                    word.errorGender_GermanStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_errorGender_GermanStatistics));
                    word.trainingPlural_GermanStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_trainingPlural_GermanStatistics));
                    word.errorPlural_GermanStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_errorPlural_GermanStatistics));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        else{
            selectQuery = "SELECT * " +
                    " FROM " + Word.TABLE_FrenchStatistics
                    + " WHERE " +
                    Word.Key_date_FrenchStatistics + " = ?";
            Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(timestamp)});
            if (cursor.moveToFirst()) {
                do {
                    word.date_FrenchStatistics = cursor.getString(cursor.getColumnIndex(Word.Key_date_FrenchStatistics));
                    word.trainingGender_FrenchStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_trainingGender_FrenchStatistics));
                    word.errorGender_FrenchStatistics = cursor.getInt(cursor.getColumnIndex(Word.Key_errorGender_FrenchStatistics));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return word;
    }

    public static int insert(Word word,int book,int einheit){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        long word_ID=0;
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_book,book);
        values.put(Word.Key_unit,einheit);
        values.put(Word.Key_errorGender,0);
        values.put(Word.Key_status,0);
        values.put(Word.Key_trainingGender,0);
        values.put(Word.Key_mark,word.mark);
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            values.put(Word.Key_plural, word.plural);
            values.put(Word.Key_errorPlural, 0);
            values.put(Word.Key_trainingPlural, 0);
             word_ID = db.insert(Word.TABLE, null, values);
        }
        else{
            word_ID = db.insert(Word.TABLE_French, null, values);
        }
        db.close();
        return (int)word_ID;
    }

    public static void delete(int word_ID){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            db.delete(Word.TABLE, Word.Key_Id + " = ?", new String[]{String.valueOf(word_ID)});
        }
        else{
            db.delete(Word.TABLE_French, Word.Key_Id + " = ?", new String[]{String.valueOf(word_ID)});
        }
        db.close();
    }

    public static int update(Word word){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        ContentValues values=new ContentValues();
        int changed_num=0;
        values.put(Word.Key_gender,word.gender);
        values.put(Word.Key_word,word.word);
        values.put(Word.Key_chn,word.chn);
        values.put(Word.Key_errorGender,word.errorGender);
        values.put(Word.Key_status,word.status);
        values.put(Word.Key_trainingGender,word.trainingGender);
        values.put(Word.Key_mark,word.mark);
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            values.put(Word.Key_errorPlural, word.errorPlural);
            values.put(Word.Key_trainingPlural, word.trainingPlural);
            values.put(Word.Key_plural, word.plural);
            changed_num = db.update(Word.TABLE, values, Word.Key_Id + "=?", new String[]{String.valueOf(word.word_Id)});
        }
        else {
            changed_num = db.update(Word.TABLE_French, values, Word.Key_Id + "=?", new String[]{String.valueOf(word.word_Id)});
        }
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
    public static  int getListWordTotal(String Book,String Unit){
        int total=0;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="";
        //lz
        if(WordsAccess.getSettingValueByName("language").equals("德语"))
        { selectQuery="SELECT * FROM "+Word.TABLE+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_unit +" = "+Unit;}
        else
        { selectQuery="SELECT * FROM "+Word.TABLE_French+" WHERE "+Word.Key_book+" = "+Book+" AND "+Word.Key_unit +" = "+Unit;}
        //lz
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

    public static ArrayList<HashMap<String,String>> getWordList(String WHERE) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path, null);
        String selectQuery;
        ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
        if (WordsAccess.getSettingValueByName("language").equals("德语")) {
            selectQuery = "SELECT * " + " FROM " + Word.TABLE + " " + WHERE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                            cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                            cursor.getString(cursor.getColumnIndex(Word.Key_plural)) + "  " +
                            cursor.getString(cursor.getColumnIndex(Word.Key_chn)));
                    map.put("wordId", cursor.getString(cursor.getColumnIndex(Word.Key_Id)));
                    wordList.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }else{
            selectQuery = "SELECT * " + " FROM " + Word.TABLE_French + " " + WHERE;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                            cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                            cursor.getString(cursor.getColumnIndex(Word.Key_chn)));
                    map.put("wordId", cursor.getString(cursor.getColumnIndex(Word.Key_Id)));
                    wordList.add(map);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        }
        return wordList;
    }

        public static ArrayList<HashMap<String, String>> getLimitWordList (String WHERE,
        int lim, String type){
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path, null);
            String selectQuery;
            if (WordsAccess.getSettingValueByName("language").equals("德语")){
                selectQuery = "SELECT * " + " FROM " + Word.TABLE + " " + WHERE;
            }else{
                selectQuery = "SELECT * " + " FROM " + Word.TABLE_French + " " + WHERE;
            }
            ArrayList<HashMap<String, String>> wordList = new ArrayList<>();
            Cursor cursor = db.rawQuery(selectQuery, null);
            int t = 0;
            if (cursor.moveToFirst()) {
                do {
                    t += 1;
                    HashMap<String, String> map = new HashMap<>();
                    if (WordsAccess.getSettingValueByName("language").equals("德语")) {
                        if (type.equals("Gender")) {
                            map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_plural)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_chn)) + "    (正确率" +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_accuracyGender)) + "%)");
                        } else {
                            map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_plural)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_chn)) + "    (正确率" +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_accuracyPlural)) + "%)");
                        }
                    }
                    else{
                        if (type.equals("Gender")) {
                            map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_chn)) + "    (正确率" +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_accuracyGender)) + "%)");
                        } else {
                            map.put("All", cursor.getString(cursor.getColumnIndex(Word.Key_gender)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_word)) + "  " +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_chn)) + "    (正确率" +
                                    cursor.getString(cursor.getColumnIndex(Word.Key_accuracyPlural)) + "%)");
                        }
                    }
                    map.put("wordId", cursor.getString(cursor.getColumnIndex(Word.Key_Id)));
                    wordList.add(map);
                } while (cursor.moveToNext() && t < lim);
            }

            cursor.close();
            db.close();
            return wordList;
        }

    public static Word getWordById(int Id){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="";
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
             selectQuery = "SELECT * " +
                    " FROM " + Word.TABLE
                    + " WHERE " +
                    Word.Key_Id + " = ?";
        }
        if(WordsAccess.getSettingValueByName("language").equals("法语")) {
            selectQuery = "SELECT * " +
                    " FROM " + Word.TABLE_French
                    + " WHERE " +
                    Word.Key_Id + " = ?";
        }
        Word word=new Word();
        Cursor cursor=db.rawQuery(selectQuery,new String[]{String.valueOf(Id)});

        if(cursor.moveToFirst()){
            do{
                word.word_Id =cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
                word.gender=cursor.getString(cursor.getColumnIndex(Word.Key_gender));
                word.word=cursor.getString(cursor.getColumnIndex(Word.Key_word));
                word.chn=cursor.getString(cursor.getColumnIndex(Word.Key_chn));
                word.book=cursor.getInt(cursor.getColumnIndex(Word.Key_book));
                word.unit =cursor.getInt(cursor.getColumnIndex(Word.Key_unit));
                word.status=cursor.getInt(cursor.getColumnIndex(Word.Key_status));
                word.errorGender =cursor.getInt(cursor.getColumnIndex(Word.Key_errorGender));
                word.trainingGender =cursor.getInt(cursor.getColumnIndex(Word.Key_trainingGender));
                word.accuracyGender =cursor.getInt(cursor.getColumnIndex(Word.Key_accuracyGender));
                if(WordsAccess.getSettingValueByName("language").equals("德语")) {
                    word.plural =cursor.getString(cursor.getColumnIndex(Word.Key_plural));
                    word.errorPlural = cursor.getInt(cursor.getColumnIndex(Word.Key_errorPlural));
                    word.trainingPlural = cursor.getInt(cursor.getColumnIndex(Word.Key_trainingPlural));
                    word.accuracyPlural = cursor.getInt(cursor.getColumnIndex(Word.Key_accuracyPlural));
                }
                word.mark =cursor.getInt(cursor.getColumnIndex(Word.Key_mark));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return word;
    }

    public static Word getWordByListId(String Book,String Unit,int listId){
        Word word;
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="";
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
            if (Book.equals("Mark")) {
                selectQuery = "SELECT * FROM " + Word.TABLE + " WHERE " + Word.Key_mark + " = " + 1;
            } else if (!Unit.equals("All")) {
                selectQuery = "SELECT * FROM " + Word.TABLE + " WHERE " + Word.Key_book + " = " + Book + " AND " + Word.Key_unit + " = " + Unit;
            } else {
                selectQuery = "SELECT * FROM " + Word.TABLE + " WHERE " + Word.Key_book + " = " + Book;
            }
        }
        else {
            if (Book.equals("Mark")) {
                selectQuery = "SELECT * FROM " + Word.TABLE_French + " WHERE " + Word.Key_mark + " = " + 1;
            } else if (!Unit.equals("All")) {
                selectQuery = "SELECT * FROM " + Word.TABLE_French+ " WHERE " + Word.Key_book + " = " + Book + " AND " + Word.Key_unit + " = " + Unit;
            } else {
                selectQuery = "SELECT * FROM " + Word.TABLE_French+ " WHERE " + Word.Key_book + " = " + Book;
            }
        }
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
    public static Word getWordByAccuracyId(String type,int listId){
        Word word;
        String WHERE;
        if(type.equals("Gender")){
            WHERE=" WHERE " + Word.Key_errorGender + " != " + 0
                    +" ORDER BY "+Word.Key_accuracyGender + " ASC ";
        }else{
            WHERE= " WHERE " + Word.Key_errorPlural + " != " + 0
                    +" ORDER BY "+Word.Key_accuracyPlural + " ASC ";
        }
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery;
        if(WordsAccess.getSettingValueByName("language").equals("德语")) {
             selectQuery = "SELECT * FROM " + Word.TABLE + WHERE;
        }
        else{
            selectQuery = "SELECT * FROM " + Word.TABLE_French + WHERE;
        }
        Cursor cursor=db.rawQuery(selectQuery,null);
        int i=0;
        int id=0;
        if(cursor.moveToFirst()){
            do{
                i++;
                id=cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
            }while(cursor.moveToNext()&&i<listId);
        }
        cursor.close();
        db.close();
        //若训练量太少，改从下列选择
        if(i<listId){
            WHERE= " ORDER BY "+Word.Key_accuracyPlural + " ASC ";
            db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
            if(WordsAccess.getSettingValueByName("language").equals("德语")) {
                selectQuery = "SELECT * FROM " + Word.TABLE + WHERE;
            }
            else{
                selectQuery = "SELECT * FROM " + Word.TABLE_French + WHERE;
            }

            cursor=db.rawQuery(selectQuery,null);
            if(cursor.moveToFirst()){
                do{
                    i++;
                    id=cursor.getInt(cursor.getColumnIndex(Word.Key_Id));
                }while(cursor.moveToNext()&&i<listId);
            }
        }
        word=getWordById(id);
        cursor.close();
        db.close();
        return word;
    }

    public static String getSettingValueByName(String Name){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery ="SELECT * "+
                " FROM "+Word.TABLE_settings
                +" WHERE "+
                Word.Key_name_settings+" = ?";
        String value="";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{Name});

        if(cursor.moveToFirst()){
            do{
                value=cursor.getString(cursor.getColumnIndex(Word.Key_value_settings));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return value;
    }

    public static void setSettingValue(String name,String value){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String Where=" WHERE "+Word.Key_name_settings+" = '"+name+"'";
        String update;
        update="update "+Word.TABLE_settings+" set "+Word.Key_value_settings+" = '"+  value +"'"+Where;
        db.execSQL(update);
        db.close();
    }

    public static String getSettingsValueByName(String Name){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String selectQuery="SELECT * "+
                " FROM "+Word.TABLE_settings
                +" WHERE "+
                Word.Key_name_settings +" = ?";
        String Value="";
        Cursor cursor=db.rawQuery(selectQuery,new String[]{Name});

        if(cursor.moveToFirst()){
            do{
                Value=cursor.getString(cursor.getColumnIndex(Word.Key_value_settings));
            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return Value;
    }
    public static void setSettings(String Name,String Value){
        SQLiteDatabase db=SQLiteDatabase.openOrCreateDatabase(WordsHelper.DB_path,null);
        String update="update "+Word.TABLE_settings+" set "+Word.Key_value_settings +"='"+Value+
                "' WHERE "+Word.Key_name_settings+" ='"+Name+"'";
        db.execSQL(update);
        db.close();
    }
}
