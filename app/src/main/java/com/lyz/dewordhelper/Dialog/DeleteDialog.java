package com.lyz.dewordhelper.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.R;

/**
 * Created by 61998 on 2017/2/25.
 */

public class DeleteDialog extends Dialog {
    private Button yes;
    private Button no;
    private TextView tv;

    int wordId;
    Word word;

    public DeleteDialog(Context context, int id){
        super(context);
        wordId=id;
        word=WordsAccess.getWordById(wordId);
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

        tv.setText("确定删除单词"+word.gender+" "+word.word+"吗？");
    }

    private void initEvent(){
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordsAccess.delete(wordId);
                Toast.makeText(getContext(),"单词删除成功",Toast.LENGTH_LONG).show();
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
