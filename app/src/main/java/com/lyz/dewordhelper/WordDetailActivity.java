package com.lyz.dewordhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.DeleteDialog;
import com.lyz.dewordhelper.Dialog.InsertDialog;
import com.lyz.dewordhelper.Dialog.UpdateDialog;

public class WordDetailActivity extends AppCompatActivity {

    Word word;
    int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        Id=Integer.parseInt(getIntent().getStringExtra("WordIdStr"));
        word=WordsAccess.getWordById(Id);
        setTitle(word.gender+" "+word.word);
        TextView genderTV=(TextView)findViewById(R.id.gender);
        TextView wordTV=(TextView)findViewById(R.id.word);
        TextView plTV=(TextView)findViewById(R.id.pl);
        TextView chnTV=(TextView)findViewById(R.id.chn);
        genderTV.setText(word.gender);
        wordTV.setText(word.word);
        plTV.setText(word.pl);
        chnTV.setText(word.chn);
    }

    public void onInsertClick(View v){
        InsertDialog insertDialog=new InsertDialog(this,word.book,word.einheit);
        insertDialog.show();
    }

    public void onUpdateClick(View v){
        UpdateDialog updateDialog=new UpdateDialog(this,Id);
        updateDialog.show();
    }

    public void onDeleteClick(View v){
        DeleteDialog deleteDialog=new DeleteDialog(this,Id);
        deleteDialog.show();
    }

}
