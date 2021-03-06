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

public class PluralTrainingActivity extends AppCompatActivity {

    Word ques;
    int round;
    int roundMax;
    int errorTimes;
    String bookStr;
    String unitStr;
    String ShowChn;

    TextView genderTV;
    TextView wordTV;
    TextView chnTV;
    TextView Round;
    TextView RoundMax;

    Button otherPlBtn;

    int wordWeightSum;
    Random random;
    Word[] words;
    private TrainingDialog trainingDialog;
    private ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_plural_training);

        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",1);
        errorTimes=0;
        bookStr=getIntent().getStringExtra("Book");
        unitStr =getIntent().getStringExtra("Unit");
        genderTV=(TextView)findViewById(R.id.gender);
        wordTV=(TextView)findViewById(R.id.word);
        chnTV=(TextView)findViewById(R.id.chn);
        otherPlBtn=(Button)findViewById(R.id.pl_other);
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
            genderTV.setText(ques.gender);
            chnTV.setText(ques.chn);
            if(!ques.plural.equals("-")&&!ques.plural.equals("..")&&!ques.plural.equals("-e")&&!ques.plural.equals("..e")&&
                    !ques.plural.equals("-er")&&!ques.plural.equals("..er")&&!ques.plural.equals("-en")&&!ques.plural.equals("-n")&&
                    !ques.plural.equals("-nen")&&!ques.plural.equals("o.Pl.")){
                otherPlBtn.setText(ques.plural);
            }else{
                int no2=random.nextInt(12);
                switch(no2){
                    case 0:otherPlBtn.setText("-s");break;
                    case 1:otherPlBtn.setText("-es");break;
                    case 2:otherPlBtn.setText("-a");break;
                    case 3:otherPlBtn.setText("-sse");break;
                    case 4:otherPlBtn.setText("-s");break;
                    case 5:otherPlBtn.setText("-se");break;
                    case 6:otherPlBtn.setText("-s");break;
                    case 7:otherPlBtn.setText("...leute");break;
                    case 8:otherPlBtn.setText("-s");break;
                    case 9:otherPlBtn.setText("...sen");break;
                    case 10:otherPlBtn.setText("-s");break;
                    case 11:otherPlBtn.setText("-s");break;
                }
            }
            initToolbar();
        }else{
            Intent intent=new Intent(this,ReportActivity.class);
            intent.putExtra("Type","Plural");
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
            ques.trainingPlural +=1;
        } else {
            ques.errorPlural +=1;
            ques.trainingPlural +=1;
            ques.status=-1;
            errorTimes++;
        }
        WordsAccess.update(ques);
    }
    public void onPlAClick(View arg0) {
        String pl=((Button)arg0).getText().toString();
        bar=(ProgressBar)findViewById(R.id.progressBar2);
        bar.setProgress(100*(round-1)/roundMax);
        if(ques.plural.equals(pl)||
                (pl.equals(this.getResources().getString(R.string.en_etc))&&
                        (ques.plural.equals("-n")||ques.plural.equals("-en")||ques.plural.equals("-nen")))){
            trainingDialog=newTrainingDialog(R.style.trueDialog);
            trainingDialog.setTitle("True");
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
        wordWeightSum=0;
        if(bookStr.equals("Mark")){
            wordWeightSum= WordsAccess.getWordTotal("German"," WHERE "+ Word.Key_mark+"="+1);
        } else if(bookStr.equals("All")&&unitStr.equals("All")){
            wordWeightSum= WordsAccess.getWordTotal(Word.TABLE,"");
        }else if(bookStr.equals("Fallible")){
            words=new Word[50];
            for(int i = 0; i<50; i++) {
                words[i] = WordsAccess.getWordByAccuracyId("Plural",i+1);
                wordWeightSum+=(100-words[i].accuracyPlural +30);
            }
        } else if(unitStr.equals("All")){
            wordWeightSum= WordsAccess.getWordTotal(Word.TABLE," WHERE "+ Word.Key_book+"= "+bookStr);
        }else {
            words=new Word[getListWordTotal(bookStr, unitStr)];
            for(int i = 0; i<getListWordTotal(bookStr, unitStr); i++) {
                words[i] = WordsAccess.getWordByListId(bookStr, unitStr,i+1);
                wordWeightSum+=(100-words[i].accuracyPlural +30);
            }
        }
    }
    public Word nextWord() {
        int stepWeightSum = 0;
        int num = random.nextInt(wordWeightSum) + 1;
        int i;
        Word word;
        if (bookStr.equals("Mark")) {
            word = WordsAccess.getWordByListId(bookStr, unitStr, num);
        } else if (!unitStr.equals("All")) {
            for (i = 0; i < words.length; i++) {
                stepWeightSum += (100 - words[i].accuracyPlural + 30);
                if (num <= stepWeightSum) {
                    break;
                }
            }
            word = words[i];
        } else if (bookStr.equals("All")) {
            word = WordsAccess.getWordById(num);
        } else {
            word = WordsAccess.getWordByListId(bookStr, "All", num);
        }
        return word;
    }
}
