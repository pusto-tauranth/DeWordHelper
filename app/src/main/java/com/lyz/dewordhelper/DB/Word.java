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
    public static final String Key_mark ="mark";//收藏单词

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
    public int mark;

    public static final String TABLE_French= "French";//表名


    //Table_GermanStatistic
    public static final String TABLE_GermanStatistics= "Germanstatistics";
    public static final String Key_date_GermanStatistics ="date";
    public static final String Key_trainingGender_GermanStatistics ="trainingGender";
    public static final String Key_errorGender_GermanStatistics ="errorGender";
    public static final String Key_trainingPlural_GermanStatistics ="trainingPlural";
    public static final String Key_errorPlural_GermanStatistics ="errorPlural";

    public String date_GermanStatistics;
    public int trainingGender_GermanStatistics;
    public int errorGender_GermanStatistics;
    public int trainingPlural_GermanStatistics;
    public int errorPlural_GermanStatistics;

    //Table_FrenchStatistic
    public static final String TABLE_FrenchStatistics= "Frenchstatistics";
    public static final String Key_date_FrenchStatistics ="date";
    public static final String Key_trainingGender_FrenchStatistics ="trainingGender";
    public static final String Key_errorGender_FrenchStatistics ="errorGender";

    public String date_FrenchStatistics;
    public int trainingGender_FrenchStatistics;
    public int errorGender_FrenchStatistics;

    //TABLE_settings
    public static final String TABLE_settings= "settings";
    public static final String Key_name_settings= "name";
    public static final String Key_value_settings= "value";
}
