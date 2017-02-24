package com.lyz.dewordhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;

public class WordDetailActivity extends AppCompatActivity {

    Word word;
    WordsAccess wordsAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        wordsAccess = new WordsAccess(this);
        Intent intent = getIntent();
        String Position = intent.getStringExtra("WordPosition");
        String Location = intent.getStringExtra("WordListLocation");
        int PosInt= Integer.parseInt(Position)+1;
        int LocInt = Integer.parseInt(Location)+1;
        int ID=0,i;

             for(i=1;i<464/*可以LocInt*大约的每个List的词数*/;i++) {
                 word = wordsAccess.getWordById(i);
                 if (word.einheit <LocInt) ID++;
                 else break;
            }
        int Id=ID+PosInt;
        word=wordsAccess.getWordById(Id);
        TextView wordTV=(TextView)findViewById(R.id.word);
        TextView plTV=(TextView)findViewById(R.id.pl);
        TextView chnTV=(TextView)findViewById(R.id.chn);
        wordTV.setText(word.word+Id);
        plTV.setText(word.pl);
        chnTV.setText(word.chn);
    }

}
