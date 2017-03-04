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

public class PlTrainingActivity extends AppCompatActivity {

    Word ques;
    int no;
    int round;
    int roundMax;
    String bookStr;
    String einheitStr;
    String ShowChn;

    TextView genderTV;
    TextView wordTV;
    TextView chnTV;

    Button otherPlBtn;

    Random random;
    private TrainingDialog trainingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_pl_training);

        round=getIntent().getIntExtra("round",0);
        roundMax=getIntent().getIntExtra("roundMax",0);
        bookStr=getIntent().getStringExtra("Book");
        einheitStr=getIntent().getStringExtra("Einheit");
        genderTV=(TextView)findViewById(R.id.gender);
        wordTV=(TextView)findViewById(R.id.word);
        chnTV=(TextView)findViewById(R.id.chn);
        otherPlBtn=(Button)findViewById(R.id.pl_other);
        random=new Random();

        ShowChn=getIntent().getStringExtra("OpenChn");
        if(ShowChn.equals("close"))//LZ
            chnTV.setVisibility(View.INVISIBLE);//LZ

        startNext();
    }

    public void startNext(){
        if(round<=roundMax){
            round++;
            if(bookStr.equals("All")){
                do{
                    no=random.nextInt(WordsAccess.getWordTotal())+1;
                    ques = WordsAccess.getWordById(no);
                }while(ques.gender==null);//YCX
                ques = WordsAccess.getWordById(no);
            }else{
                no=random.nextInt(WordsAccess.getListWordTotal(bookStr,einheitStr));
                ques=WordsAccess.getWordByListId(bookStr,einheitStr,no);
            }
            wordTV.setText(ques.word);
            genderTV.setText(ques.gender);
            chnTV.setText(ques.chn);
            if(!ques.pl.equals("-")&&!ques.pl.equals("..")&&!ques.pl.equals("-e")&&!ques.pl.equals("..e")&&
                    !ques.pl.equals("-er")&&!ques.pl.equals("..er")&&!ques.pl.equals("-en")&&!ques.pl.equals("-n")&&
                    !ques.pl.equals("-nen")&&!ques.pl.equals("o.Pl.")){
                otherPlBtn.setText(ques.pl);
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
        title.setText(ques.gender+" "+ques.word);
    }

    public void onPlAClick(View v) {
        String pl=((Button)v).getText().toString();
        System.out.println(pl);
        System.out.println(ques.pl);
        if(ques.pl.equals(pl)||
                (pl.equals(this.getResources().getString(R.string.en_etc))&&
                        (ques.pl.equals("-n")||ques.pl.equals("-en")||ques.pl.equals("-nen")))){
            trainingDialog=newTrainingDialog(R.style.trueDialog);
            trainingDialog.setTitle("Richtig");
            trainingDialog.show();
        }else{
            trainingDialog=newTrainingDialog(R.style.falseDialog);
            trainingDialog.setTitle("Falsch");
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
