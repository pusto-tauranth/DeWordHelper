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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class TrainingSettingsActivity extends AppCompatActivity {
//public class TrainingSettingsActivity extends AppCompatActivity implements TouchProgressView.OnProgressChangedListener {
    //private static final String TAG = "Activity";
    int roundMax;
    private EditText etImageAdjustment = null;
    private SeekBar skbImageAdjustment = null;
    private TextView wordsNumber;

    private boolean notHandleAfterTextChangedEvent = false;
    private boolean changeFromSeekBar = false;
    private boolean changeFromEditText = false;
    Integer value=0;
    //TextView Progress;
    //TouchProgressView proViewStyle;
    EditText SetProgress;
    String OpenChn="close";//LZ
    //int progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_training_settings);
        //this.setTitle("训练设置");
        initToolbar();
        //init();
        SetProgress = (EditText) findViewById(R.id.mysetting);
        SetProgress.setText("0");
       Switch sc=(Switch)findViewById(R.id.chn1);//-----
        sc.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,boolean b){
                if(b){
                    Toast.makeText(getApplicationContext(),"显示汉语",Toast.LENGTH_SHORT).show();
                    OpenChn="open";
                }else{
                    Toast.makeText(getApplicationContext(),"不显示汉语",Toast.LENGTH_SHORT).show();
                    OpenChn="close";
                }
            }
        });//------LZ
        //联动设置
        etImageAdjustment = (EditText) findViewById(R.id.mysetting);
        etImageAdjustment.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO 自动生成的方法存根

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO 自动生成的方法存根

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO 自动生成的方法存根
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
                value =0;
                if (s.toString().trim().equals("")) {
                    notHandleAfterTextChangedEvent = true;
                    etImageAdjustment.setText("");
                } else {
                    value = Integer.parseInt(s.toString());
                }
                if (value != null) {
                    if (value < 0) {
                        value = 0;
                        //} else if (value > 100) {
                        //   value = 100;
                    }
                    notHandleAfterTextChangedEvent = true;
                    etImageAdjustment.setText(Integer.toString(value));
                    wordsNumber=(TextView)findViewById(R.id.outwordsnumber);
                    wordsNumber.setText(s.toString());
                    try {
                        if (!changeFromSeekBar) {
                            changeFromEditText = true;
                            skbImageAdjustment.setProgress(value);
                        } else {
                            changeFromSeekBar = false;
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(),
                                "writeyour number", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        skbImageAdjustment = (SeekBar) findViewById(R.id.wordsSeekBar);
        skbImageAdjustment
                .setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        // TODO 自动生成的方法存根
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        // TODO 自动生成的方法存根
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        // TODO 自动生成的方法存根
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
    public void initToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView title=(TextView)findViewById(R.id.tv_title);
        title.setText("训练设置");
    }

    /*private void init() {
        Progress = (TextView) findViewById(R.id.progress);
        proViewStyle = (TouchProgressView) findViewById(R.id.proview_style);
        proViewStyle.setOnProgressChangedListener(this);
        proViewStyle.setLineColor(R.color.colorAccent);
        proViewStyle.setLineHeight(20);
        proViewStyle.setProgress(50);
        proViewStyle.setPointColor(R.color.colorAccent);
        proViewStyle.setPointRadius(20);

    }
    @Override
    public void onProgressChanged(View v, int progress) {
        String name = "proViewStyle";
        Log.i(TAG, name + " 进度发生变化,progress== " + progress);
        Progress.setText(progress+"");
    }*/
    public void onGenderClick(View v){
        /*if(SetProgress.getText().toString().equals(""))//LZ
        roundMax=Integer.parseInt(Progress.getText().toString());//LZ
        else//LZ*/
        //roundMax=Integer.parseInt(SetProgress.getText().toString());//LZ
        roundMax=value;
        Intent intent=new Intent(this,GenderTrainingActivity.class);
        intent.putExtra("round",1);
        intent.putExtra("roundMax",roundMax);
        intent.putExtra("OpenChn",OpenChn);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        intent.putExtra("Einheit",getIntent().getStringExtra("Einheit"));
        startActivity(intent);
    }
    public void onPlClick(View v){
        Intent intent=new Intent(this,PlTrainingActivity.class);
        /*if(SetProgress.getText().toString().equals(""))//LZ
            roundMax=Integer.parseInt(Progress.getText().toString());//LZ
        else//LZ*/
           //roundMax=Integer.parseInt(SetProgress.getText().toString());//LZ
        roundMax=value;
        intent.putExtra("round",1);
        intent.putExtra("OpenChn",OpenChn);//LZ
        intent.putExtra("roundMax",roundMax);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        intent.putExtra("Einheit",getIntent().getStringExtra("Einheit"));
        startActivity(intent);
    }
    public void onChnBtn(View v){

    }
}
