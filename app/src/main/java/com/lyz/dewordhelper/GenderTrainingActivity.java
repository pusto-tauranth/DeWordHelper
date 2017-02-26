package com.lyz.dewordhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.TrainingDialog;

import java.util.Random;

public class GenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int no;
    int round;
    int roundMax;
    private TrainingDialog trainingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gender_training);
        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",0);
        TextView wordTV=(TextView)findViewById(R.id.word);
        TextView plTV=(TextView)findViewById(R.id.pl);
        TextView chnTV=(TextView)findViewById(R.id.chn);
        Random random=new Random();
        do{
            no=random.nextInt(464);
            ques = WordsAccess.getWordById(no);
        }while(ques.gender==null);//YCX
        ques = WordsAccess.getWordById(no);
        wordTV.setText(ques.word);
        plTV.setText(ques.pl);
        chnTV.setText(ques.chn);
        initToolbar();
    }

    public void initToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=(TextView)findViewById(R.id.tv_title);
        title.setText(ques.word);
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

        return trainingDialog;
    }
}
