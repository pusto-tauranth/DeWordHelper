package com.lyz.dewordhelper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.TrainingDialog;

import java.util.Random;

import static com.lyz.dewordhelper.DB.WordsAccess.getListWordTotal;

public class FrenchGenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int round;
    int roundMax;
    int errorTimes;

    String ShowChn;
    String bookStr;
    String unitStr;

    TextView wordTV;
    TextView chnTV;
    TextView Round;
    TextView RoundMax;

    int wordWeightSum;
    Random random;
    Word[] words;

    private TrainingDialog trainingDialog;
    private ProgressBar bar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_french_gender_training);

        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",1);
        errorTimes=0;
        bookStr=getIntent().getStringExtra("Book");
        unitStr =getIntent().getStringExtra("Unit");
        wordTV=(TextView)findViewById(R.id.word);
        chnTV=(TextView)findViewById(R.id.chn);
        Round=(TextView)findViewById(R.id.round);
        RoundMax=(TextView)findViewById(R.id.roundmax);
        random=new Random();
        RoundMax.setText("/"+roundMax);

        ShowChn=getIntent().getStringExtra("OpenChn");
        if(ShowChn.equals("off"))
            chnTV.setVisibility(View.INVISIBLE);

        initWords();
        startNext();
    }

    public void startNext(){
        if(round<=roundMax){
            Round.setText("progress："+round);
            round++;
            ques=nextWord();
            wordTV.setText(ques.word);
            chnTV.setText(ques.chn);
            initToolbar();
        }else{
            Intent intent=new Intent(this,ReportActivity.class);
            intent.putExtra("Type","Gender");
            intent.putExtra("RoundMax",roundMax);
            intent.putExtra("ErrorTimes",errorTimes);
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
        if(Bool){
            ques.status=1;
            ques.trainingGender +=1;
        } else {
            ques.errorGender +=1;
            ques.trainingGender +=1;
            ques.status=-1;
            errorTimes++;
        }
        WordsAccess.update(ques);
    }
    public void onGenderAClick(View arg0){
        bar=(ProgressBar)findViewById(R.id.progressBar);
        bar.setProgress(100*(round-1)/roundMax);
        if(ques.gender.equals(((Button)arg0).getText())||ques.gender.equals("la/le")){
            trainingDialog=newTrainingDialog(R.style.trueDialog);
            trainingDialog.setTitle("Right");
            recording(true);
            trainingDialog.show();
        }else{
            trainingDialog=newTrainingDialog(R.style.falseDialog);
            trainingDialog.setTitle("False");
            recording(false);
            trainingDialog.show();
        }
    }

    public TrainingDialog newTrainingDialog(@StyleRes int themResId){
        trainingDialog=new TrainingDialog(this,themResId);
        trainingDialog.setWord(ques.gender+" "+ques.word);
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

    public void initWords(){
        wordWeightSum=0;
        if(bookStr.equals("Mark")){
            wordWeightSum= WordsAccess.getWordTotal("French"," WHERE "+ Word.Key_mark+"="+1);
        } else if(bookStr.equals("All")&&unitStr.equals("All")){
            wordWeightSum= WordsAccess.getWordTotal(Word.TABLE_French,"");
        }else if(bookStr.equals("Fallible")){
            words=new Word[50];
            for(int i = 0; i<50; i++) {
                words[i] = WordsAccess.getWordByAccuracyId("Gender",i+1);
                wordWeightSum+=(100-words[i].accuracyGender +30);
            }
        } else if(unitStr.equals("All")){
            wordWeightSum= WordsAccess.getWordTotal(Word.TABLE_French," WHERE "+ Word.Key_book+"= "+bookStr);
        }else {
            words=new Word[getListWordTotal(bookStr, unitStr)];
            for(int i = 0; i<getListWordTotal(bookStr, unitStr); i++) {
                words[i] = WordsAccess.getWordByListId(bookStr, unitStr,i+1);
                wordWeightSum+=(100-words[i].accuracyGender +30);
            }
        }
    }
    public Word nextWord(){
        int stepWeightSum=0;
        int num=random.nextInt(wordWeightSum)+1;
        int i;
        Word word;
        if(bookStr.equals("Mark")){
            word= WordsAccess.getWordByListId(bookStr,unitStr,num);
        } else if(!unitStr.equals("All")){
            for(i=0;i<words.length;i++){
                stepWeightSum+=(100-words[i].accuracyGender+30);
                if(num<=stepWeightSum){
                    break;
                }
            }
            word= words[i];
        }else if(bookStr.equals("All")){
            word= WordsAccess.getWordById(num);
        }else{
            word= WordsAccess.getWordByListId(bookStr,"All",num);
        }
        return word;
    }
}