package com.lyz.dewordhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lyz.dewordhelper.progress.TouchProgressView;


public class TrainingSettingsActivity extends AppCompatActivity implements TouchProgressView.OnProgressChangedListener {
    private static final String TAG = "Activity";
    int roundMax;
    TextView Progress;
    TouchProgressView proViewStyle;
    EditText MyProgress;
    EditText SetProgress;
    //int progress;
    String OpenChn="close";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_training_settings);
        initToolbar();
        init();
        SetProgress = (EditText) findViewById(R.id.myprogress);
        SetProgress.setText("");
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
        title.setText("训练设置");
    }
    private void init() {
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
    }
    public void onGenderClick(View v){
        if(SetProgress.getText().toString().equals(""))//LZ
            roundMax=Integer.parseInt(Progress.getText().toString());//LZ
        else//LZ
            roundMax=Integer.parseInt(SetProgress.getText().toString());//LZ
        //MyProgress = (EditText) findViewById(R.id.myprogress);
        //if(Integer.parseInt(MyProgress.getText().toString())==Integer.parseInt(Progress.getText().toString()))
        //roundMax=Integer.parseInt(Progress.getText().toString());
        //else
        //roundMax=Integer.parseInt(MyProgress.getText().toString());
        Intent intent=new Intent(this,GenderTrainingActivity.class);
        intent.putExtra("round",1);
        intent.putExtra("roundMax",roundMax);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        intent.putExtra("Einheit",getIntent().getStringExtra("Einheit"));
        intent.putExtra("OpenChn",OpenChn);
        startActivity(intent);

    }
    public void onPlClick(View v){
        if(SetProgress.getText().toString().equals(""))//LZ
            roundMax=Integer.parseInt(Progress.getText().toString());//LZ
        else//LZ
            roundMax=Integer.parseInt(SetProgress.getText().toString());//LZ
        //MyProgress = (EditText) findViewById(R.id.myprogress);
        //if(Integer.parseInt(MyProgress.getText().toString())==Integer.parseInt(Progress.getText().toString()))
        //roundMax=Integer.parseInt(Progress.getText().toString());
        //else
        //roundMax=Integer.parseInt(MyProgress.getText().toString());
        Intent intent=new Intent(this,PlTrainingActivity.class);
        intent.putExtra("round",1);
        intent.putExtra("roundMax",roundMax);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        intent.putExtra("Einheit",getIntent().getStringExtra("Einheit"));
        intent.putExtra("OpenChn",OpenChn);
        startActivity(intent);
    }
    public void onChnBtn(View v){

    }
}
