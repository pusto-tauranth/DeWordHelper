package com.lyz.dewordhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.TrainingDialog;

import java.lang.reflect.Field;
import java.util.Random;

public class GenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int round;
    int roundMax;
    private TrainingDialog trainingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_training);
        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",0);
        this.setTitle("词性训练");
        TextView wordTV=(TextView)findViewById(R.id.word);
        TextView plTV=(TextView)findViewById(R.id.pl);
        TextView chnTV=(TextView)findViewById(R.id.chn);
        TextView insTV=(TextView)findViewById(R.id.inspection);
        Random random=new Random();
        int no=random.nextInt(464);
        WordsAccess access=new WordsAccess(this);
        ques = access.getWordById(no);
        wordTV.setText(ques.word);
        plTV.setText(ques.pl);
        chnTV.setText(ques.chn);
        insTV.setText(Integer.toString(round)+"  "+Integer.toString(roundMax));

    }

    public void onGenderAClick(View v){
        if(ques.gender.equals(((Button)v).getText())){
            trainingDialog=newTrainingDialog(R.style.trueDialog);
            trainingDialog.setTitle("Richtig");
            trainingDialog.show();
        }else{
            trainingDialog=newTrainingDialog(R.style.falseDialog);
            trainingDialog.setTitle("Falsch");
            trainingDialog.show();
        }
    }

    public void startNext(){
        if(round<roundMax){
            Intent intent=new Intent(this,GenderTrainingActivity.class);
            intent.putExtra("round",round+1);
            intent.putExtra("roundMax",roundMax);
            startActivity(intent);
            finish();
        }else{
            Intent intent=new Intent(this,ReportActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public TrainingDialog newTrainingDialog(@StyleRes int themResId){
        trainingDialog=new TrainingDialog(this,themResId);
        trainingDialog.setNextOnClickListener(new TrainingDialog.NextOnClickListener() {
            @Override
            public void onNextClick() {
                startNext();
            }
        });
        trainingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                startNext();
            }
        });
        trainingDialog.setWord(ques.gender+" "+ques.word+" "+ques.pl);
        trainingDialog.setChn(ques.chn);



        //ques.gender+" "+ques.word+" "+ques.pl+"\n"+ques.chn

        return trainingDialog;
    }
    /*public void onGenderAClick(View v){//设置View为Button时，会报错。
        if(ques.gender.equals(((Button)v).getText())){
            AlertDialog.Builder builder =newTrainingDialog(R.style.trueDialog);
            builder.setTitle("Richtig");
            builder.show();
        }
        else{
            AlertDialog.Builder builder =newTrainingDialog(R.style.falseAlertDialog);
            builder.setTitle("Falsch");
            builder.show();
        }
    }*/
}
