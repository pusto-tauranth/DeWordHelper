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

    private View pageQuantityGender, pageAccuracyGender, pageKeyGender,pageQuantityPlural, pageAccuracyPlural, pageKeyPlural;
    private List<View> pageList;
    private ViewPager mainViewPager;
    private MainPagerAdapter mainPagerAdapter;

    private LineChartView lineChart;
    String[] dates = getDates();

    int[] quantityGen;
    private List<PointValue> quanPointValuesGen = new ArrayList<>();
    private List<AxisValue> quanAxisXValuesGen = new ArrayList<>();

    float[] accuracyGen;
    private List<PointValue> accPointValuesGen = new ArrayList<>();
    private List<AxisValue> accAxisXValuesGen = new ArrayList<>();

    int[] quantityPl;
    private List<PointValue> quanPointValuesPl = new ArrayList<>();
    private List<AxisValue> quanAxisXValuesPl = new ArrayList<>();

    float[] accuracyPl;
    private List<PointValue> accPointValuesPl = new ArrayList<>();
    private List<AxisValue> accAxisXValuesPl = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wordsHelper = new WordsHelper(this);
        wordsHelper.openDatabase();
        wordsHelper.closeDatabase();

        quantityGen = getTrainingQuantityGen();
        accuracyGen = getTrainingAccuracyGen();
        quantityPl = getTrainingQuantityPl();
        accuracyPl = getTrainingAccuracyPl();
        LayoutInflater inflater = getLayoutInflater();
        pageKeyGender = inflater.inflate(R.layout.main_page_key, null);
        pageQuantityGender = inflater.inflate(R.layout.main_page_quantity, null);
        pageAccuracyGender = inflater.inflate(R.layout.main_page_accuracy, null);
        pageKeyPlural = inflater.inflate(R.layout.main_page_key, null);
        pageQuantityPlural = inflater.inflate(R.layout.main_page_quantity, null);
        pageAccuracyPlural = inflater.inflate(R.layout.main_page_accuracy, null);
        pageList = new ArrayList<>();
        pageList.add(pageKeyGender);
        pageList.add(pageQuantityGender);
        pageList.add(pageAccuracyGender);
        pageList.add(pageKeyPlural);
        pageList.add(pageQuantityPlural);
        pageList.add(pageAccuracyPlural);
        mainViewPager = (ViewPager) findViewById(R.id.pager);
        mainPagerAdapter = new MainPagerAdapter(pageList);
        mainViewPager.setAdapter(mainPagerAdapter);
        initTabLayout();

        getAxisXLabelsQuanGen();//获取x轴的标注
        getAxisPointsQuanGen();//获取坐标点
        initLineChartQuanGen();//初始化

        getAxisXLabelsAccGen();//获取x轴的标注
        getAxisPointsAccGen();//获取坐标点
        initLineChartAccGen();//初始化

        initKeyPageGen();

        getAxisXLabelsQuanPl();//获取x轴的标注
        getAxisPointsQuanPl();//获取坐标点
        initLineChartQuanPl();//初始化

        getAxisXLabelsAccPl();//获取x轴的标注
        getAxisPointsAccPl();//获取坐标点
        initLineChartAccPl();//初始化

        initKeyPagePl();
    }

    public void onTrainingClick(View v) {
        Intent intent = new Intent(this,BookSelectActivity.class);
        startActivity(intent);
    }

    public void onStockClick(View v) {
        Intent intent = new Intent(this, StockActivity.class);
        startActivity(intent);
    }

    public void onLanSelectClick(View v){
        Intent intent = new Intent(this,LanguageSelectActivity.class);
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

    public int[] getTrainingQuantityGen(){
        int[] num= new int[7];
        for(int i=0;i<7;i++){
            num[i]=WordsAccess.getTrainingTimes(dates[i]).trainingGender_2;
        }
        return num;
    }

    public int[] getTrainingQuantityPl(){
        int[] num= new int[7];
        for(int i=0;i<7;i++){
            num[i]=WordsAccess.getTrainingTimes(dates[i]).trainingPlural_2;
        }
        return num;
    }

    public float[] getTrainingAccuracyGen(){
        float[] num= new float[7];
        for(int i=0;i<7;i++){
            Word date=WordsAccess.getTrainingTimes(dates[i]);
            if(date.trainingGender_2 ==0){
                num[1]=0;
            }else{
                num[i]=(date.trainingGender_2 -date.errorGender_2)/(float)date.trainingGender_2 *100;
            }
        }
        return num;
    }
    public float[] getTrainingAccuracyPl(){
        float[] num= new float[7];
        for(int i=0;i<7;i++){
            Word date=WordsAccess.getTrainingTimes(dates[i]);
            if(date.trainingPlural_2 ==0){
                num[1]=0;
            }else{
                num[i]=(date.trainingPlural_2 -date.errorPlural_2)/(float)date.trainingPlural_2 *100;
            }
        }
        return num;
    }

    public void initTabLayout(){
        titleList = new ArrayList<>();
        titleList.add("词性\n常错");
        titleList.add("词性\n训练量");
        titleList.add("词性\n正确率");
        titleList.add("复数\n常错");
        titleList.add("复数\n训练量");
        titleList.add("复数\n正确率");
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

    public void initKeyPageGen() {
        ListView lv = (ListView) pageKeyGender.findViewById(R.id.keyList);
        ArrayList<HashMap<String, String>> myList;
        String WHERE = " WHERE " + Word.Key_errorGender + " != " + 0
                +" ORDER BY "+Word.Key_accuracyGender + " ASC ";
        myList = WordsAccess.getLimitWordList(WHERE,50,"Gender");
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
    public void initKeyPagePl() {
        ListView lv = (ListView) pageKeyPlural.findViewById(R.id.keyList);
        ArrayList<HashMap<String, String>> myList;
        String WHERE = " WHERE " + Word.Key_errorPlural + " != " + 0
                +" ORDER BY "+Word.Key_accuracyPlural + " ASC ";
        myList = WordsAccess.getLimitWordList(WHERE,50,"Plural");
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
    private void getAxisXLabelsQuanGen(){
        for (int i = 0; i < dates.length; i++) {
            quanAxisXValuesGen.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }
    private void getAxisXLabelsQuanPl(){
        for (int i = 0; i < dates.length; i++) {
            quanAxisXValuesPl.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }
    /**
     * 图表的每个点的显示
     */
    private void getAxisPointsQuanGen() {
        for (int i = 0; i < quantityGen.length; i++) {
            quanPointValuesGen.add(new PointValue(i, quantityGen[i]));
        }
    }
    private void getAxisPointsQuanPl() {
        for (int i = 0; i < quantityPl.length; i++) {
            quanPointValuesPl.add(new PointValue(i, quantityPl[i]));
        }
    }

    private void initLineChartQuanGen(){
        Line line = new Line(quanPointValuesGen).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
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
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValuesGen.length
        axisX.setValues(quanAxisXValuesGen);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("训练量");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        //lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
        lineChartData.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView) pageQuantityGender.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }
    private void initLineChartQuanPl(){
        Line line = new Line(quanPointValuesPl).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
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
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValuesGen.length
        axisX.setValues(quanAxisXValuesPl);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("训练量");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        //lineChartData.setAxisYLeft(axisY);  //Y轴设置在左边
        lineChartData.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView) pageQuantityPlural.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }

    /**
     * 设置X 轴的显示
     */
    private void getAxisXLabelsAccGen() {
        for (int i = 0; i < dates.length; i++) {
            accAxisXValuesGen.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }
    private void getAxisXLabelsAccPl() {
        for (int i = 0; i < dates.length; i++) {
            accAxisXValuesPl.add(new AxisValue(i).setLabel(dates[i].substring(5)));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPointsAccGen() {
        for (int i = 0; i < accuracyGen.length; i++) {
            accPointValuesGen.add(new PointValue(i, accuracyGen[i]));
        }
    }
    private void getAxisPointsAccPl() {
        for (int i = 0; i < accuracyPl.length; i++) {
            accPointValuesPl.add(new PointValue(i, accuracyPl[i]));
        }
    }

    private void initLineChartAccGen(){
        Line line = new Line(accPointValuesGen).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
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
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValuesGen.length
        axisX.setValues(accAxisXValuesGen);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("正确率（%）");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        lineChartData.setAxisYRight(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView) pageAccuracyGender.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }
    private void initLineChartAccPl(){
        Line line = new Line(accPointValuesPl).setColor(Color.parseColor("#ffb300"));  //折线的颜色（橙色）
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
        //axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=accAxisXValuesGen.length
        axisX.setValues(accAxisXValuesPl);  //填充X轴的坐标名称
        lineChartData.setAxisXBottom(axisX); //x 轴在底部
        //data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线

        Axis axisY = new Axis();  //Y轴
        axisY.setName("正确率（%）");//y轴标注
        axisY.setTextSize(15);//设置字体大小
        lineChartData.setAxisYRight(axisY);  //Y轴设置在左边
        //data.setAxisYRight(axisY);  //y轴设置在右边


        //设置行为属性，支持缩放、滑动以及平移
        lineChart =(LineChartView) pageAccuracyPlural.findViewById(R.id.line_chart);
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.VERTICAL);
        lineChart.setContainerScrollEnabled(false,null);
        lineChart.setLineChartData(lineChartData);
        lineChart.setVisibility(View.VISIBLE);
    }
}