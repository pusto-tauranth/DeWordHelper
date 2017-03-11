package com.lyz.dewordhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.lyz.dewordhelper.DB.WordsHelper;
import com.lyz.dewordhelper.Dialog.TrainingDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int no;
    int round;
    int roundMax;
    String bookStr;
    String einheitStr;
    String ShowChn;

    TextView wordTV;
    TextView plTV;
    TextView chnTV;

    Random random;
    private TrainingDialog trainingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gender_training);

        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",0);
        bookStr=getIntent().getStringExtra("Book");
        einheitStr=getIntent().getStringExtra("Einheit");
        wordTV=(TextView)findViewById(R.id.word);
        plTV=(TextView)findViewById(R.id.pl);
        chnTV=(TextView)findViewById(R.id.chn);
        random=new Random();

        startNext();



        ShowChn=getIntent().getStringExtra("OpenChn");
        if(ShowChn.equals("close"))//LZ
            chnTV.setVisibility(View.INVISIBLE);//LZ
        //LZ
            //LZ
    }

    public void startNext(){
        if(round<=roundMax){
            round++;
            if(bookStr.equals("All")){
                do{
                    no=random.nextInt(WordsAccess.getWordTotal(Word.TABLE,""));
                    ques = WordsAccess.getWordById(no);
                }while(ques.gender==null);//YCX
                ques = WordsAccess.getWordById(no);
            }else{
                no=random.nextInt(WordsAccess.getListWordTotal(bookStr,einheitStr));
                ques=WordsAccess.getWordByListId(bookStr,einheitStr,no);
            }
            wordTV.setText(ques.word);
            plTV.setText(ques.pl);
            chnTV.setText(ques.chn);
            initToolbar();
        }else{
            Intent intent=new Intent(this,ReportActivity.class);
            startActivity(intent);
            finish();
        }
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

    public void recording(boolean Bool){

        ques.date=WordsAccess.timestamp("yyyy-MM-dd",0);
        ques.status=1;
        if(Bool){

        } else {
            ques.errortimes+=1;
        }
        WordsAccess.update(ques);
    }
    public void onGenderAClick(View v){
        if(ques.gender.equals(((Button)v).getText())){
            trainingDialog=newTrainingDialog(R.style.trueDialog);
            trainingDialog.setTitle("Richtig");
            recording(true);
            trainingDialog.show();
        }else{
            trainingDialog=newTrainingDialog(R.style.falseDialog);
            trainingDialog.setTitle("Falsch");
            recording(false);
            trainingDialog.show();
        }
    }

    public TrainingDialog newTrainingDialog(@StyleRes int themResId){
        trainingDialog=new TrainingDialog(this,themResId);
        trainingDialog.setWord(ques.gender+" "+ques.word+" "+ques.pl);
        trainingDialog.setChn(ques.chn);
        trainingDialog.setNextOnClickListener(new TrainingDialog.NextOnClickListener() {
            @Override
            public void onNextClick() {
                startNext();
                trainingDialog.dismiss();
            }
        });
        trainingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                startNext();
                trainingDialog.dismiss();
            }
        });




        return trainingDialog;
    }
}