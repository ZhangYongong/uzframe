package com.ughen.util.excl;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 统计图
 *
 * @Author:Yonghong Zhang
 * @Date: 13:49 2019/1/25
 */
public class SummaryGraphUtils {

    /**
     * 提供静态方法：获取报表图形1：饼状图
     *
     * @param title 标题
     * @param datas 数据
     */
    public static void createPieChart(String title, Map<String, Double> datas, OutputStream out) throws Exception {
        try {
            Font font = new Font("新宋体", Font.BOLD, 15);
            //如果不使用Font,中文将显示不出来
            DefaultPieDataset pds = new DefaultPieDataset();
            //获取迭代器：
            Set<Map.Entry<String, Double>> set = datas.entrySet();
            Iterator iterator = (Iterator) set.iterator();
            Map.Entry entry = null;
            while (iterator.hasNext()) {
                entry = (Map.Entry) iterator.next();
                pds.setValue(entry.getKey().toString(), Double.parseDouble(entry.getValue().toString()));
            }
            /**
             * 生成一个饼图的图表
             * 分别是:显示图表的标题、需要提供对应图表的DateSet对象、是否显示图例、是否生成贴士以及是否生成URL链接
             */
            JFreeChart chart = ChartFactory.createPieChart(title, pds, true, false, true);
            //设置图片标题的字体
            chart.getTitle().setFont(font);
            //得到图块,准备设置标签的字体
            PiePlot plot = (PiePlot) chart.getPlot();
            //设置标签字体
            plot.setLabelFont(font);
            //设置图例项目字体
            chart.getLegend().setItemFont(font);
            /**
             * 设置开始角度(弧度计算)
             *
             * 度    0°    30°        45°        60°        90°        120°    135°    150°    180°    270°    360°
             * 弧度    0    π/6        π/4        π/3        π/2        2π/3    3π/4    5π/6    π        3π/2    2π
             */
            plot.setStartAngle(new Float(3.14f / 2f));
            //设置plot的前景色透明度
            plot.setForegroundAlpha(0.7f);
            //设置plot的背景色透明度
            plot.setBackgroundAlpha(0.0f);
            //设置标签生成器(默认{0})
            //{0}:key {1}:value {2}:百分比 {3}:sum
            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}/{2}"));
            //将内存中的图片写到本地硬盘
            ChartUtilities.writeChartAsJPEG(out, chart, 800, 400, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    /**
     * 提供静态方法：获取报表图形2：柱状图
     *
     * @param title  标题
     * @param datas  数据
     * @param type   分类
     * @param danwei 柱状图的数量单位
     */
    public static void createHistogram(String title, Map<String, Map<String, Double>> datas, String type, String danwei, OutputStream out) throws Exception {
        try {
            Font font = new Font("宋体", Font.BOLD, 20);
            //种类数据集
            DefaultCategoryDataset ds = new DefaultCategoryDataset();
            //获取迭代器：
            Set<Map.Entry<String, Map<String, Double>>> set1 = datas.entrySet();
            //第一次迭代
            Iterator iterator1 = (Iterator) set1.iterator();
            Iterator iterator2 = null;
            HashMap<String, Double> map = null;
            Set<Map.Entry<String, Double>> set2 = null;
            Map.Entry entry1 = null;
            Map.Entry entry2 = null;
            while (iterator1.hasNext()) {
                //遍历分类
                entry1 = (Map.Entry) iterator1.next();
                //得到每次分类的详细信息
                map = (HashMap<String, Double>) entry1.getValue();
                //获取键值对集合
                set2 = map.entrySet();
                //再次迭代遍历
                iterator2 = set2.iterator();
                while (iterator2.hasNext()) {
                    entry2 = (Map.Entry) iterator2.next();
                    ds.setValue(Double.parseDouble(entry2.getValue().toString()),//每次统计数量
                            entry2.getKey().toString(),                         //名称
                            entry1.getKey().toString());                        //分类
                }
            }
            JFreeChart chart = ChartFactory.createBarChart(title, type, danwei, ds, PlotOrientation.VERTICAL, true, true, true);
            //设置整个图片的标题字体
            chart.getTitle().setFont(font);
            //设置提示条字体
            font = new Font("宋体", Font.BOLD, 15);
            chart.getLegend().setItemFont(font);
            //得到绘图区
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            //得到绘图区的域轴(横轴),设置标签的字体
            plot.getDomainAxis().setLabelFont(font);
            //设置横轴标签项字体
            plot.getDomainAxis().setTickLabelFont(font);
            //设置范围轴(纵轴)字体
            plot.getRangeAxis().setLabelFont(font);
            //存储成图片
            plot.setForegroundAlpha(1.0f);
            ChartUtilities.writeChartAsJPEG(out, chart, 800, 400, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

//    public static void main(String[] args) {
//
//
//        Map<String, Map<String, Double>> datas = new HashMap<String, Map<String, Double>>();
//
//        Map<String, Double> map1 = new HashMap<String, Double>();
//        Map<String, Double> map2 = new HashMap<String, Double>();
//        Map<String, Double> map3 = new HashMap<String, Double>();
//        Map<String, Double> map4 = new HashMap<String, Double>();
//
//        //设置第一期的投票信息
//        map1.put("a", (double) 1000);
//        map1.put("b", (double) 700);
//        map1.put("c", (double) 600);
//        map1.put("d", (double) 400);
//
//        //设置第二期的投票信息
//        map2.put("a", (double) 1300);
//        map2.put("b", (double) 900);
//        map2.put("c", (double) 800);
//        map2.put("d", (double) 500);
//        //设置第三期的投票信息
//        map2.put("a", (double) 2000);
//        map3.put("b", (double) 1700);
//        map3.put("c", (double) 1000);
//        map3.put("d", (double) 1000);
//        //设置第四期的投票信息
//        map4.put("a", (double) 3000);
//        map4.put("b", (double) 2500);
//        map4.put("c", (double) 1600);
//        map4.put("d", (double) 1400);
//        //压入数据
//        datas.put("一", map1);
//        datas.put("二", map2);
//        datas.put("三", map3);
//        // datas.put("四", map4);
//        OutputStream out = null;
//        try {
//            out = new FileOutputStream("E://a.jpg");
//            createHistogram("柱状图统计结果", datas, "柱状图", "数量单位", out);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    /**
     * 提供静态方法：获取报表图形3：折线图
     *
     * @param title  标题
     * @param datas  数据
     * @param type   分类
     * @param danwei 柱状图的数量单位
     */
    public static void createPolyline(String title, Map<String, Map<String, Double>> datas, String type, String danwei, OutputStream out) throws IOException {
        //种类数据集
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Font font = new Font("宋体", Font.BOLD, 20);
        //获取迭代器：
        Set<Map.Entry<String, Map<String, Double>>> set1 = datas.entrySet();    //总数据
        Iterator iterator1 = (Iterator) set1.iterator();                        //第一次迭代
        Iterator iterator2 = null;
        HashMap<String, Double> map = null;
        Set<Map.Entry<String, Double>> set2 = null;
        Map.Entry entry1 = null;
        Map.Entry entry2 = null;
        while (iterator1.hasNext()) {//4种分类
            entry1 = (Map.Entry) iterator1.next();                    //遍历分类
            map = (HashMap<String, Double>) entry1.getValue();//得到每次分类的详细信息
            set2 = map.entrySet();                               //获取键值对集合
            iterator2 = set2.iterator();                        //再次迭代遍历
            while (iterator2.hasNext()) {//4-4
                entry2 = (Map.Entry) iterator2.next();
                ds.setValue(Double.parseDouble(entry2.getValue().toString()),//每次统计数量
                        entry2.getKey().toString(),                         //名称
                        entry1.getKey().toString());                        //分类
            }
        }
        //创建折线图,折线图分水平显示和垂直显示两种
        JFreeChart chart = ChartFactory.createLineChart(title, type, danwei, ds,//2D折线图
                PlotOrientation.VERTICAL,
                true, true, true);
//            JFreeChart chart = ChartFactory.createLineChart3D(title, type, danwei, ds,//3D折线图
//                    PlotOrientation.VERTICAL,
//                    true, true, false);
        //设置整个图片的标题字体
        chart.getTitle().setFont(font);
        //设置提示条字体
        font = new Font("宋体", Font.BOLD, 15);
        chart.getLegend().setItemFont(font);
        //得到绘图区
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        //得到绘图区的域轴(横轴),设置标签的字体
        plot.getDomainAxis().setLabelFont(font);
        //设置横轴标签项字体
        plot.getDomainAxis().setTickLabelFont(font);
        //设置范围轴(纵轴)字体
        font = new Font("宋体", Font.BOLD, 18);
        plot.getRangeAxis().setLabelFont(font);
        plot.setForegroundAlpha(1.0f);
        //存储成图片
        ChartUtilities.writeChartAsJPEG(out, chart, 800, 400, null);
    }

    public static void main(String[] args) {

        Map<String, Map<String, Double>> datas = new HashMap<String, Map<String, Double>>();
        Map<String, Double> map1 = new HashMap<String, Double>();
        Map<String, Double> map2 = new HashMap<String, Double>();
        Map<String, Double> map3 = new HashMap<String, Double>();
        Map<String, Double> map4 = new HashMap<String, Double>();
        //设置第一期的投票信息
        map1.put("a", (double) 700);
        map1.put("b", (double) 1000);
        map1.put("c", (double) 600);
        map1.put("d", (double) 400);
        //设置第二期的投票信息
        map2.put("a", (double) 900);
        map2.put("b", (double) 2000);
        map2.put("c", (double) 800);
        map2.put("d", (double) 500);
        //设置第三期的投票信息
        map3.put("a", (double) 1700);
        map3.put("b", (double) 1000);
        map3.put("c", (double) 1400);
        map3.put("d", (double) 1000);
        //设置第四期的投票信息
        map4.put("a", (double) 2500);
        map4.put("b", (double) 3000);
        map4.put("c", (double) 1600);
        map4.put("d", (double) 1400);
        //压入数据
        datas.put("一", map1);
        datas.put("二", map2);
        datas.put("三", map3);
        datas.put("四", map4);
        OutputStream out = null;
        try {
            out = new FileOutputStream("E://a.jpg");
            createPolyline("柱状图统计结果", datas, "柱状图", "数量单位", out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}