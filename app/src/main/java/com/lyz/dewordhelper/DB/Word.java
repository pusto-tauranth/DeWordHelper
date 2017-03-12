package com.lyz.dewordhelper.DB;

/**
 * Created by 61998 on 2017/2/22.
 */

public class Word {
    public static final String TABLE = "Test";//表名

    //列名
    public static final String Key_Id="rowid";
    public static final String Key_gender="gender";
    public static final String Key_word="word";
    public static final String Key_pl="conjugation";
    public static final String Key_chn="meaning";
    public static final String Key_book ="book";
    public static final String Key_einheit ="Einheit";//LZ
    public static final String Key_errortimes ="errortimes";
    public static final String Key_easy_archive ="easy_archive";
    public static final String Key_collection ="collection";
    public static final String Key_date ="date";//newly added
    public static final String Key_status="status";


    //属性
    public int word_Id;
    public String gender;
    public String word;
    public String pl;
    public String chn;
    public int einheit;//LZ
    public int book;
    public int errortimes;
    public int easy_archive;
    public int collection;
    public String date;
    public long status;

    //Table_2
    public static final String TABLE_2= "statistics";
    public static final String Key_date_2 ="date";
    public static final String Key_training_2 ="training";
    public String date_2;
    public int training_2;
}
