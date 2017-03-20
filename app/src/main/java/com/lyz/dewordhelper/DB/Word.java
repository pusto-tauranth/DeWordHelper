package com.lyz.dewordhelper.DB;

/**
 * Created by 61998 on 2017/2/22.
 */

public class Word {
    public static final String TABLE = "German";//表名

    //列名
    public static final String Key_Id="id";
    public static final String Key_gender="gender";
    public static final String Key_word="word";
    public static final String Key_plural ="plural";
    public static final String Key_chn="chinese";
    public static final String Key_book ="book";
    public static final String Key_unit ="unit";
    public static final String Key_status="status";//记录该次训练中是否答错
    public static final String Key_errorGender ="errorGender";
    public static final String Key_trainingGender ="trainingGender";//记录总训练次数
    public static final String Key_accuracyGender ="accuracyGender";
    public static final String Key_errorPlural ="errorPlural";
    public static final String Key_trainingPlural ="trainingPlural";//记录总训练次数
    public static final String Key_accuracyPlural ="accuracyPlural";

    //属性
    public int word_Id;
    public String gender;
    public String word;
    public String plural;
    public String chn;
    public int unit;
    public int book;
    public long status;
    public int errorGender;
    public int trainingGender;
    public int accuracyGender;
    public int errorPlural;
    public int trainingPlural;
    public int accuracyPlural;
    //Table_2
    public static final String TABLE_2= "statistics";
    public static final String Key_date_2 ="date";
    public static final String Key_trainingGender_2 ="trainingGender";
    public static final String Key_errorGender_2 ="errorGender";
    public static final String Key_trainingPlural_2 ="trainingPlural";
    public static final String Key_errorPlural_2 ="errorPlural";
    public String date_2;
    public int trainingGender_2;
    public int errorGender_2;
    public int trainingPlural_2;
    public int errorPlural_2;
}
