package com.lyz.dewordhelper;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.WordsAccess;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitSelectActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_select);
        initToolbar();
        ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
        for (int i=1;i<=10;i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Unit","Einheit "+i);
            map.put("WordNum","总词量："+ WordsAccess.getListWordTotal(getIntent().getStringExtra("Book"),""+i));
            myList.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this,
                myList,
                R.layout.activity_stock_item,
                new String[]{"Unit", "WordNum"},
                new int[]{R.id.stock, R.id.wordNum});
        setListAdapter(mSchedule);
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
        title.setText("选择词库");
    }

    public void onAll(View v){
        Intent intent=new Intent(this,TrainingSettingsActivity.class);
        intent.putExtra("Unit","All");
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // TODO Auto-generated method stub
        super.onListItemClick(l, v, position, id);
        TextView unitTV=(TextView)v.findViewById(R.id.stock);
        String unitStr=unitTV.getText().toString().substring(8);
        Intent intent = new Intent(this,TrainingSettingsActivity.class);
        intent.putExtra("Unit",unitStr);
        intent.putExtra("Book",getIntent().getStringExtra("Book"));
        this.startActivity(intent);
    }
}
