package com.lyz.dewordhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lyz.dewordhelper.DB.WordsAccess;


public class TrainingSettingsActivity extends AppCompatActivity {
    int roundMax;
    private EditText etImageAdjustment = null;
    private SeekBar skbImageAdjustment = null;

    private boolean notHandleAfterTextChangedEvent = false;
    private boolean changeFromSeekBar = false;
    private boolean changeFromEditText = false;
    Integer value = 0;
    EditText SetProgress;
    String OpenChn= WordsAccess.getSettingsValueByName("chinese");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_training_settings);
        initToolbar();
        value= Integer.parseInt(WordsAccess.getSettingsValueByName("quantity"));
        Button pluralBtn=(Button)findViewById(R.id.plural);
        if(WordsAccess.getSettingsValueByName("language").equals("法语")){
            pluralBtn.setVisibility(View.GONE);
        }
        SetProgress = (EditText) findViewById(R.id.wordEditText);
        SetProgress.setText(String.valueOf(value));
        Switch switcher=(Switch)findViewById(R.id.chn1);
        if(OpenChn.equals("on")){
            switcher.setChecked(true);
        }else{
            switcher.setChecked(false);
        }
        switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getApplicationContext(), "显示汉语", Toast.LENGTH_SHORT).show();
                    OpenChn = "on";
                } else {
                    Toast.makeText(getApplicationContext(), "不显示汉语", Toast.LENGTH_SHORT).show();
                    OpenChn = "off";
                }
                WordsAccess.setSettings("chinese",OpenChn);
            }
        });
        //联动设置
        etImageAdjustment = (EditText) findViewById(R.id.wordEditText);
        etImageAdjustment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (notHandleAfterTextChangedEvent) {
                    notHandleAfterTextChangedEvent = false;

                    // 光标置最后
                    CharSequence text = etImageAdjustment.getText();
                    if (text instanceof Spannable) {
                        Spannable spanText = (Spannable) text;
                        Selection.setSelection(spanText, text.length());
                    }
                    return;
                }
                if (s == null) {
                    return;
                }
                value = 0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent = true;
                    etImageAdjustment.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (value != null) {
                    if (value < 0) {
                        value = 0;
                    } else if (value > 1000) {
                        value = 1000;
                    }
                    notHandleAfterTextChangedEvent = true;
                    etImageAdjustment.setText(Integer.toString(value));
                    try {
                        if (!changeFromSeekBar) {
                            changeFromEditText = true;
                            skbImageAdjustment.setProgress(value);
                        } else {
                            changeFromSeekBar = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(),
                                "请输入您希望的数值", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        skbImageAdjustment = (SeekBar) findViewById(R.id.wordsSeekBar);
        skbImageAdjustment.setProgress(value);
        skbImageAdjustment
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        if (fromUser) {
                            if (!changeFromEditText) {
                                changeFromSeekBar = true;
                                etImageAdjustment.setText(Integer.toString(progress));
                            } else {
                                changeFromEditText = false;
                            }
                        }
                    }

                });
    }//--------联动设置
    public void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("训练设置");
    }

    public void onGenderClick(View v) {
        if(getIntent().getStringExtra("WordNum").equals("0")){
            Toast.makeText(getApplicationContext(),"此词库中没有单词！", Toast.LENGTH_SHORT).show();
        }else {
            roundMax = value;
            WordsAccess.setSettings("quantity", String.valueOf(roundMax));
            WordsAccess.statusReset();
            Intent intent = null;
            if (WordsAccess.getSettingValueByName("language").equals("德语")) {
                intent = new Intent(this, GenderTrainingActivity.class);
            }
            if (WordsAccess.getSettingValueByName("language").equals("法语")) {
                intent = new Intent(this, FrenchGenderTrainingActivity.class);
            }
            intent.putExtra("round", 1);
            intent.putExtra("roundMax", roundMax);
            intent.putExtra("OpenChn", OpenChn);
            intent.putExtra("Book", getIntent().getStringExtra("Book"));
            intent.putExtra("Unit", getIntent().getStringExtra("Unit"));
            intent.putExtra("WordNum",getIntent().getStringExtra("WordNum"));
            if (roundMax == 0) {
                Toast.makeText(getApplicationContext(), "请正确设置训练词数", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(intent);
            }
        }
    }

    public void onPlClick(View v) {
        if (getIntent().getStringExtra("WordNum").equals("0")) {
            Toast.makeText(getApplicationContext(), "此词库中没有单词！", Toast.LENGTH_SHORT).show();
        } else {
            if (WordsAccess.getSettingValueByName("language").equals("德语")) {
                Intent intent = new Intent(this, PluralTrainingActivity.class);
                roundMax = value;
                WordsAccess.setSettings("quantity", String.valueOf(roundMax));
                WordsAccess.statusReset();
                intent.putExtra("round", 1);
                intent.putExtra("OpenChn", OpenChn);
                intent.putExtra("roundMax", roundMax);
                intent.putExtra("Book", getIntent().getStringExtra("Book"));
                intent.putExtra("Unit", getIntent().getStringExtra("Unit"));
                intent.putExtra("WordNum",getIntent().getStringExtra("WordNum"));
                if (roundMax == 0) {
                    Toast.makeText(getApplicationContext(), "请正确设置训练词数", Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(intent);
                }
            }
            if (WordsAccess.getSettingValueByName("language").equals("法语")) {
                Toast.makeText(getApplicationContext(), "法语未开放复数训练", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
