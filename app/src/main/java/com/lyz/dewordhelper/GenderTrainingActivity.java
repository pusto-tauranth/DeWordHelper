package com.lyz.dewordhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.Random;

public class GenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int round;
    int roundMax;

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
        Random random=new Random();
        int no=random.nextInt(464);
        WordsAccess access=new WordsAccess(this);
        ques = access.getWordById(no);
        wordTV.setText(ques.word);
        plTV.setText(ques.pl);
        chnTV.setText(ques.chn);

    }
    public void onGenderAClick(View v){     //设置View为Button时，会报错。
        if(ques.gender.equals(((Button)v).getText())){
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("Richtig");
            builder.setMessage(ques.gender+" "+ques.word+" "+ques.pl+" "+ques.chn);
            builder.setPositiveButton("Nächst", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(round<roundMax){
                        Intent intent=new Intent(GenderTrainingActivity.this,GenderTrainingActivity.class);
                        intent.putExtra("round",round+1);
                        intent.putExtra("roundMax",roundMax);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent=new Intent(GenderTrainingActivity.this,GenderTrainingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            builder.show();
        }
        else{
            AlertDialog.Builder builder =new AlertDialog.Builder(this);
            builder.setTitle("Falsch");
            builder.setMessage(ques.gender+" "+ques.word+" "+ques.pl+" "+ques.chn);
            builder.setPositiveButton("Nächst", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(round<roundMax){
                        Intent intent=new Intent(GenderTrainingActivity.this,GenderTrainingActivity.class);
                        intent.putExtra("round",round+1);
                        intent.putExtra("roundMax",roundMax);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent=new Intent(GenderTrainingActivity.this,ReportActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
            builder.show();
        }


    }
}
