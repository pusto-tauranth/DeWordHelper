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
 * Created by 61998 on 2017/2/25.
 */

public class UpdateDialog extends Dialog {
    private EditText genderET;
    private EditText wordET;
    private EditText plET;
    private EditText chnET;
    private Button yes;
    private Button no;
    private int wordId;
    private Word word;

    public UpdateDialog(Context context,int id){
        super(context);
        wordId=id;
        word=WordsAccess.getWordById(wordId);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_update);

        initView();
        initEvent();
    }

    private void initView(){
        genderET=(EditText)findViewById(R.id.dialog_update_gender_et);
        wordET=(EditText)findViewById(R.id.dialog_update_word_et);
        plET=(EditText)findViewById(R.id.dialog_update_pl_et);
        chnET=(EditText)findViewById(R.id.dialog_update_chn_et);
        yes=(Button)findViewById(R.id.dialog_update_yes);
        no=(Button)findViewById(R.id.dialog_update_no);

        genderET.setText(word.gender);
        wordET.setText(word.word);
        plET.setText(word.plural);
        chnET.setText(word.chn);
    }

    private void initEvent(){
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word.gender=genderET.getText().toString();
                word.word=wordET.getText().toString();
                word.plural =plET.getText().toString();
                word.chn=chnET.getText().toString();
                WordsAccess.update(word);
                Intent intent = new Intent(getContext(),ReportActivity.class);
                intent.putExtra("Type","Die");
                getContext().startActivity(intent);
                Toast.makeText(getContext(),"单词编辑成功",Toast.LENGTH_LONG).show();
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
