package com.lyz.dewordhelper.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.R;
import com.lyz.dewordhelper.ReportActivity;

/**
 * Created by 61998 on 2017/2/25.
 */

public class MarkDialog extends Dialog {
    private Button yes;
    private Button no;
    private TextView tv;

    int wordId;
    Word word;

    public MarkDialog(Context context, int id){
        super(context);
        wordId=id;
        word= WordsAccess.getWordById(wordId);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);

        initView();
        initEvent();

    }

    private void initView(){
        tv=(TextView)findViewById(R.id.dialog_delete_tv);
        yes=(Button)findViewById(R.id.dialog_delete_yes);
        no=(Button)findViewById(R.id.dialog_delete_no);

        tv.setText("确定收藏单词"+word.gender+" "+word.word+"吗？");
        if(word.mark==1){
            tv.setText("确定取消收藏单词"+word.gender+" "+word.word+"吗？");
        }
    }

    private void initEvent(){
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Word word= WordsAccess.getWordById(wordId);
                if(word.mark==1){
                    word.mark=0;
                    WordsAccess.update(word);
                    Toast.makeText(getContext(),"已成功取消收藏", Toast.LENGTH_LONG).show();
                }else{
                    word.mark=1;
                    WordsAccess.update(word);
                    Toast.makeText(getContext(),"已成功收藏", Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(getContext(),ReportActivity.class);
                intent.putExtra("Type","Die");
                getContext().startActivity(intent);
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