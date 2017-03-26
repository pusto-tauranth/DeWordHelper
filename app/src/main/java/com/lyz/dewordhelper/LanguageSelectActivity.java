package com.lyz.dewordhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lyz.dewordhelper.DB.WordsAccess;

public class LanguageSelectActivity extends AppCompatActivity {
    String Language= WordsAccess.getSettingValueByName("language");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_language_select);
        initToolbar();
        final RadioButton radioButtonGerman =(RadioButton)findViewById(R.id.German);
        RadioButton radioButtonFrench =(RadioButton)findViewById(R.id.French);
        if(Language.equals("德语"))
        {
            radioButtonGerman.setChecked(true);
        }
        if(Language.equals("法语")){
            radioButtonFrench.setChecked(true);
        }
        RadioGroup group = (RadioGroup) this.findViewById(R.id.languageGroup);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int languageId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) LanguageSelectActivity.this.findViewById(radioButtonId);
                Language=rb.getText().toString();
                if(Language.equals("西班牙语")||Language.equals("葡萄牙语"))
                {
                    Toast.makeText(getApplicationContext(),"您所选语种正在努力开发中,系统将默认语言改为德语", Toast.LENGTH_LONG).show();
                    Language="德语";
                    radioButtonGerman.setChecked(true);
                }
                WordsAccess.setSettingValue("language", Language);
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
        finish();
    }
}