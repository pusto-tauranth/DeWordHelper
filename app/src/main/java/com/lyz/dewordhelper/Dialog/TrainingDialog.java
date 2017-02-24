package com.lyz.dewordhelper.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyz.dewordhelper.GenderTrainingActivity;
import com.lyz.dewordhelper.R;
import com.lyz.dewordhelper.ReportActivity;

/**
 * Created by 61998 on 2017/2/24.
 */

public class TrainingDialog extends Dialog {
    private Button next;
    private TextView titleTV;
    private TextView wordTV;
    private TextView chnTV;
    private String titleStr;
    private String wordStr;
    private String chnStr;

    private NextOnClickListener nextOnClickListener;

    public void setNextOnClickListener(NextOnClickListener nextOnClickListener){
        this.nextOnClickListener=nextOnClickListener;
    }

    public TrainingDialog(Context context,@StyleRes int themResId){
        super(context, themResId);
    }

    @Override
    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_training);

        //setCanceledOnTouchOutside(false);

        initView();
        initData();
        initEvent();
    }

    private void initEvent(){
       next.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               nextOnClickListener.onNextClick();
           }
       });
    }
    private void initData(){
        titleTV.setText(titleStr);
        wordTV.setText(wordStr);
        chnTV.setText(chnStr);
    }

    private void initView(){
        next=(Button)findViewById(R.id.dialog_training_next);
        titleTV=(TextView)findViewById(R.id.dialog_training_title);
        wordTV=(TextView)findViewById(R.id.dialog_training_word);
        chnTV=(TextView)findViewById(R.id.dialog_training_chn);
    }

    public void setTitle(String title){
        titleStr=title;
    }

    public void setWord(String word){
       wordStr=word;
    }

    public void setChn(String chn){
        chnStr=chn;
    }

    public interface NextOnClickListener{
        public void onNextClick();
    }
}
