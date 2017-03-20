package com.lyz.dewordhelper;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.lyz.dewordhelper.DB.Word;
import com.lyz.dewordhelper.DB.WordsAccess;
import com.lyz.dewordhelper.DB.WordsHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    public WordsHelper wordsHelper;

    private List<String> titleList;
    private TabLayout tabLayout;

    private View pageQuantity, pageAccuracy, pageKey;
    private List<View> pageList;
    private ViewPager mainViewPager;
    private MainPagerAdapter mainPagerAdapter;

    private LineChartView lineChart;
    String[] dates = getDates();

    int[] quantity;
    private List<PointValue> quanPointValues = new ArrayList<>();
    private List<AxisValue> quanAxisXValues = new ArrayList<>();

    float[] accuracy;
    private List<PointValue> accPointValues = new ArrayList<>();
    private List<AxisValue> accAxisXValues = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordsHelper = new WordsHelper(this);
        wordsHelper.openDatabase();
        wordsHelper.closeDatabase();

        quantity = getTrainingQuantity();
        accuracy = getTrainingAccuracy();
        LayoutInflater inflater = getLayoutInflater();
        pageKey = inflater.inflate(R.layout.main_page_key, null);
        pageQuantity = inflater.inflate(R.layout.main_page_quantity, null);
        pageAccuracy = inflater.inflate(R.layout.main_page_accuracy, null);
        pageList = new ArrayList<>();
        pageList.add(pageKey);
        pageList.add(pageQuantity);
        pageList.add(pageAccuracy);
        mainViewPager = (ViewPager) findViewById(R.id.pager);
        mainPagerAdapter = new MainPagerAdapter(pageList);
        mainViewPager.setAdapter(mainPagerAdapter);
        initTabLayout();

        getAxisXLabelsQuan();//获取x轴的标注
        getAxisPointsQuan();//获取坐标点
        initLineChartQuan();//初始化

        getAxisXLabelsAcc();//获取x轴的标注
        getAxisPointsAcc();//获取坐标点
        initLineChartAcc();//初始化

        initKeyPage();
    }

    public void onTrainingClick(View v) {
        Intent intent = new Intent(this, StockSelectActivity.class);
        startActivity(intent);
    }

    public void onStockClick(View v) {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }

    public String[] getDates() {
        String[] timestamp = new String[7];
        int offset = 0;
        for (int i = 6; i >=0; i--) {
            timestamp[i] = WordsAccess.timestamp("yyyy-MM-dd", offset);
            offset -= 1;
        }
        return timestamp;
    }

    public int[] getTrainingQuantity(){
        int[] num= new int[7];
        for(int i=0;i<7;i++){
            num[i]=WordsAccess.getTrainingTimes(dates[i]).training_2;
        }
        return num;
    }

    public float[] getTrainingAccuracy(){
        float[] num= new float[7];
        for(int i=0;i<7;i++){
            Word date=WordsAccess.getTrainingTimes(dates[i]);
            if(date.training_2==0){
                num[1]=0;
            }else{
                num[i]=(date.training_2-date.errortimes_2)/(float)date.training_2*100;
            }
        }
        return num;
    }

    public void initTabLayout(){
        titleList = new ArrayList<>();
        titleList.add("常错词");
        titleList.add("最近训练量");
        titleList.add("最近正确率");
        tabLayout = (TabLayout)findViewById(R.id.main_tab);
        //设置tab的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //把TabLayout和ViewPager关联起来
        tabLayout.setupWithViewPager(mainViewPager);
        //添加tab选项卡
        for (int i = 0;i< titleList.size(); i++) {
            tabLayout.getTabAt(i).setText(titleList.get(i));
        }
    }

    public void initKeyPage() {
        ListView lv = (ListView) pageKey.findViewById(R.id.keyList);
        ArrayList<HashMap<String, String>> myList;
        String WHERE = " WHERE " + Word.Key_errortimes + " != " + 0
                +" ORDER BY "+Word.Key_accuracy + " ASC ";
        myList = WordsAccess.getLimitWordList(WHERE,50);
        SimpleAdapter listAdapter = new SimpleAdapter(this,
                myList,
                R.layout.activity_stock_detail_item,
                new String[]{"All", "wordId"},
                new int[]{R.id.All, R.id.wordId});
        lv.setAdapter(listAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView idTV = (TextView) view.findViewById(R.id.wordId);
                String wordIdStr = idTV.getText().toString();
                Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
                intent.putExtra("WordIdStr", wordIdStr);
                startActivity(intent);
            }
        });
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLabelsQuan(){
        for (int i = 0; i < dates.length; i++) {
            quanAxisXValues.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPointsQuan() {
        for (int i = 0; i < quantity.length; i++) {
            quanPointValues.add(new PointValue(i, quantity[i]));
        }
    }

    private void initLineChartQuan(){
        Line line = new Line(quanPointValues).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("");  //表格名称
        axisX.setTextSize(15);//设置字体大小
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValues.length
        axisX.setValues(quanAxisXValues);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("训练量");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        //lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
        lineChartData.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView)pageQuantity.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLabelsAcc() {
        for (int i = 0; i < dates.length; i++) {
            accAxisXValues.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPointsAcc() {
        for (int i = 0; i < accuracy.length; i++) {
            accPointValues.add(new PointValue(i, accuracy[i]));
        }
    }

    private void initLineChartAcc(){
        Line line = new Line(accPointValues).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
        List<Line> lines = new ArrayList<>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.DIAMOND）
        line.setCubic(false);//曲线是否平滑，即是曲线还是折线
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
        //line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示（每个数据点都是个大的圆点）
        lines.add(line);
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X坐标轴字体是斜的显示还是直的，true是斜的显示
        axisX.setTextColor(Color.BLACK);  //设置字体颜色
        axisX.setName("");  //表格名称
        axisX.setTextSize(15);//设置字体大小
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValues.length
        axisX.setValues(accAxisXValues);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("正确率（%）");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        lineChartData.setAxisYRight(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView)pageAccuracy.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }
}