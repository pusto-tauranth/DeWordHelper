package com.lyz.dewordhelper.DB;

/**
 * Created by 61998 on 2017/2/22.
 */

public class Word {
    public static final String TABLE = "Version1";//表名

    //列名
    public static final String Key_Id="rowid";
    public static final String Key_gender="gender";
    public static final String Key_word="word";
    public static final String Key_pl="conjugation";
    public static final String Key_chn="meaning";

    //属性
    public int word_Id;
    public String gender;
    public String word;
    public String pl;
    public String chn;
}
