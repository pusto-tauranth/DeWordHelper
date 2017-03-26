package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.Dialog.InsertDialog;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitManageActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_manage);
        initToolbar();
        initList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    public void initToolbar(){
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnitManageActivity.this.finish();
            }
        });
        TextView title=(TextView)findViewById(R.id.tv_title);
        String titleStr;
        titleStr="Buch "+getIntent().getStringExtra("Book");
        if(WordsAccess.getSettingsValueByName("language").equals("法语")){
            titleStr="Reflets " + getIntent().getStringExtra("Book");
        }
        title.setText(titleStr);
    }

    public void initList(){
        ArrayList<HashMap<String, String>> stockList = new ArrayList<HashMap<String, String>>();
        if( WordsAccess.getSettingValueByName("language").equals("德语")) {
            for (int i = 1; i <= 10; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Unit", "Einheit " + i);
                map.put("WordNum", "总词量：" + WordsAccess.getListWordTotal(getIntent().getStringExtra("Book"), "" + i));
                stockList.add(map);
            }
        }
        if( WordsAccess.getSettingValueByName("language").equals("法语"))
        {
            for (int i = 0; i <= 26; i++) {
                HashMap<String, String> map = new HashMap<>();
                map.put("Unit", "épisode " + i);
                map.put("WordNum", "总词量：" + WordsAccess.getListWordTotal(getIntent().getStringExtra("Book"), "" + i));
                stockList.add(map);
            }
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this,
                stockList,
                R.layout.activity_stock_item,
                new String[]{"Unit","WordNum"},
                new int[]{R.id.stock, R.id.wordNum});
        setListAdapter(mSchedule);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialog insertDialog=new InsertDialog(UnitManageActivity.this,-10,-10);
                insertDialog.show();
            }
        });
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        TextView unitTV=(TextView)v.findViewById(R.id.stock);
        String unitStr=unitTV.getText().toString().substring(8);

        Intent intent = new Intent(this,StockDetailActivity.class);
        intent.putExtra("Unit",unitStr);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        startActivity(intent);
    }
}


