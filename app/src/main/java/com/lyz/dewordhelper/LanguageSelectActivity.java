package com.lyz.dewordhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class LanguageSelectActivity extends AppCompatActivity {
    TextView tv =null;
    String Language="德语";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);
        initToolbar();
        tv = (TextView) this.findViewById(R.id.decision);
        tv.setText("您选择的语言是:"+"德语");
        RadioGroup group = (RadioGroup) this.findViewById(R.id.languageGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int languageId) {
                // TODO Auto-generated method stub
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) LanguageSelectActivity.this.findViewById(radioButtonId);
                tv.setText("您选择的语言是：" + rb.getText());
                Language=""+rb.getText();
            }
        });
    }

    public void initToolbar() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("选择语言");
    }

    public void onEnsureClick(View v){
       // Intent intent= new Intent(LanguageSelectActivity.this,BookSelectActivity.class);
       // intent.putExtra("Language",Language);
        //tartActivity(intent);
        finish();
    }
}