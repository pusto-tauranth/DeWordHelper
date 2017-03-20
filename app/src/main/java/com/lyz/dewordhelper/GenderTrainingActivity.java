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

public class GenderTrainingActivity extends AppCompatActivity {

    Word ques;
    int round;
    int roundMax;

    String ShowChn;
    String bookStr;
    String einheitStr;

    TextView wordTV;
    TextView plTV;
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
        setContentView(R.layout.activity_gender_training);

        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",0);
        bookStr=getIntent().getStringExtra("Book");
        einheitStr=getIntent().getStringExtra("Einheit");
        wordTV=(TextView)findViewById(R.id.word);
        plTV=(TextView)findViewById(R.id.pl);
        chnTV=(TextView)findViewById(R.id.chn);
        Round=(TextView)findViewById(R.id.round);
        RoundMax=(TextView)findViewById(R.id.roundmax);
        random=new Random();
        RoundMax.setText("/"+roundMax);

        ShowChn=getIntent().getStringExtra("OpenChn");
        if(ShowChn.equals("close"))
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
            plTV.setText(ques.plural);
            chnTV.setText(ques.chn);
            initToolbar();
        }else{
            Intent intent=new Intent(this,ReportActivity.class);
            intent.putExtra("Type","Gender");
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

        //ques.date=WordsAccess.timestamp("yyyy-MM-dd",0);

        if(Bool){
            ques.status=1;
            ques.trainingGender +=1;
        } else {
            ques.errorGender +=1;
            ques.trainingGender +=1;
            ques.status=-1;
        }
        WordsAccess.update(ques);
    }
    public void onGenderAClick(View arg0){
        // TODO Auto-generated method stub
        bar=(ProgressBar)findViewById(R.id.progressBar);
        bar.setProgress(100*(round-1)/roundMax);
        if(ques.gender.equals(((Button)arg0).getText())){
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
        trainingDialog.setWord(ques.gender+" "+ques.word+" "+ques.plural);
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
        if(bookStr.equals("All")){
            words=new Word[WordsAccess.getWordTotal(Word.TABLE,"")];
            for(int i=0;i<WordsAccess.getWordTotal(Word.TABLE,"");i++){
                words[i]=WordsAccess.getWordById(i);
            }
        }else{
            words=new Word[getListWordTotal(bookStr,einheitStr)];
            for(int i = 0; i<getListWordTotal(bookStr,einheitStr); i++) {
                words[i] = WordsAccess.getWordByListId(bookStr,einheitStr,i+1);
            }
        }
        wordWeightSum=0;
        for(int i=0;i<words.length;i++){
            wordWeightSum+=100-words[i].accuracyGender +30;//错误率权重为100-accuracyGender，为防止相差过大，每词计算权重时各再加一数
        }
    }
    public Word nextWord(){
        int stepWeightSum=0;
        int num=random.nextInt(wordWeightSum)+1;
        int i;
        for(i=0;i<words.length;i++){
            stepWeightSum+=100-words[i].accuracyGender +30;
            if(num<=stepWeightSum){
                break;
            }
        }
        return words[i];
    }
}