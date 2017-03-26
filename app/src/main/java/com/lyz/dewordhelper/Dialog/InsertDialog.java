package com.lyz.dewordhelper.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.R;
import com.lyz.dewordhelper.ReportActivity;

/**
 * Created by 61998 on 2017/2/24.
 */

public class InsertDialog extends Dialog {
    private EditText genderET;
    private EditText wordET;
    private EditText plET;
    private EditText chnET;
    private Button yes;
    private Button no;

    private int book;
    private int unit;

    public InsertDialog(Context context, int book, int unit){
        super(context);
        this.book=book;
        this.unit = unit;
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_insert);

        initView();
        initEvent();
    }

    private void initView(){
        genderET=(EditText)findViewById(R.id.dialog_insert_gender_et);
        wordET=(EditText)findViewById(R.id.dialog_insert_word_et);
        plET=(EditText)findViewById(R.id.dialog_insert_pl_et);
        chnET=(EditText)findViewById(R.id.dialog_insert_chn_et);
        yes=(Button)findViewById(R.id.dialog_insert_yes);
        no=(Button)findViewById(R.id.dialog_insert_no);
        if(WordsAccess.getSettingsValueByName("language").equals("法语")) {
            plET.setVisibility(View.GONE);
            findViewById(R.id.dialog_insert_pl_tv).setVisibility(View.GONE);
        }
    }

    private void initEvent(){
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word=new Word();
                word.gender=genderET.getText().toString();
                word.word=wordET.getText().toString();
                if(WordsAccess.getSettingsValueByName("language").equals("德语")){
                    word.plural =plET.getText().toString();
                }
                word.chn=chnET.getText().toString();
                word.mark=0;
                if(book==-1){//直接从收藏夹添加时，赋book=-1
                    word.mark=1;
                    book=-10;
                    unit=-10;
                }
                WordsAccess.insert(word,book, unit);
                Intent intent = new Intent(getContext(),ReportActivity.class);
                intent.putExtra("Type","Die");
                getContext().startActivity(intent);
                Toast.makeText(getContext(),"单词添加成功", Toast.LENGTH_LONG).show();
                dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
