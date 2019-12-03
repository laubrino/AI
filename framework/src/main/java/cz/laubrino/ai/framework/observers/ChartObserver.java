package cz.laubrino.ai.framework.observers;

import cz.laubrino.ai.framework.Observer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tomas.laubr on 3.12.2019.
 */
public class ChartObserver implements Observer {
    private volatile long episode;
    private Averaging testingSteps;
    private ScheduledExecutorService scheduledExecutorService;

    private XYSeries testSuccessSeries = new XYSeries("Testing success");
    private XYSeries stepsNeededSeries = new XYSeries("Steps performed");

    public ChartObserver(String title) {
        ApplicationFrame applicationFrame = new ApplicationFrame(title);
        ChartPanel chartPanel = (ChartPanel) createPanel();
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        chartPanel.setMaximumDrawHeight(2000);
        chartPanel.setMaximumDrawWidth(3000);
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);


        testingSteps = new Averaging();

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sample, 1, 1, TimeUnit.SECONDS);

    }


    @Override
    public void qValueChanged(float deltaQ, int deltaQInPercent) {

    }

    @Override
    public void learningEpisodeFinished(long episode, long steps) {
        this.episode = episode;
    }

    @Override
    public void testingEpisodeFinished(boolean success, long steps) {
        testingSteps.add(steps);
    }

    @Override
    public void testingBatchFinished(int successEpisodes, int allEpisodes) {
        testSuccessSeries.add(episode, successEpisodes);
    }

    @Override
    public void end() {
    }

    private JFreeChart createChart() {
        XYSeriesCollection testSuccessDataset = new XYSeriesCollection();
        testSuccessDataset.addSeries(testSuccessSeries);

        XYSeriesCollection stepsNeededDataset = new XYSeriesCollection();
        stepsNeededDataset.addSeries(stepsNeededSeries);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart("Rubik's Cube RL", "Episodes", "Testing success [%]", testSuccessDataset);

        XYPlot xyPlot = xyLineChart.getXYPlot();

        ValueAxis episodesAxis = xyPlot.getDomainAxis();
        episodesAxis.setAutoRange(false);
        episodesAxis.setLowerBound(0);
        episodesAxis.setUpperBound(1_000_000);

        ValueAxis percentAxis = xyPlot.getRangeAxis();
        percentAxis.setAutoRange(false);
        percentAxis.setLowerBound(0);
        percentAxis.setUpperBound(105);

        NumberAxis stepsAxis = new NumberAxis("Steps performed");
        xyPlot.setDataset(1, stepsNeededDataset);
        xyPlot.setRangeAxis(1, stepsAxis);
        xyPlot.mapDatasetToRangeAxis(1, 1);

        StandardXYItemRenderer stepsRenderer = new StandardXYItemRenderer();
        xyPlot.setRenderer(1, stepsRenderer);

        ChartUtils.applyCurrentTheme(xyLineChart);

        // colors after applyCurrentTheme() !
        xyPlot.getRenderer().setSeriesPaint(0, Color.decode("0x0036CC"));
        stepsRenderer.setSeriesPaint(0, Color.ORANGE);

        return xyLineChart;

//
//        JFreeChart var1 = ChartFactory.createTimeSeriesChart("Multiple Axis Demo 1", "Time of Day", "Primary Range Axis", var0, true, true, false);
//        var1.addSubtitle(new TextTitle("Four datasets and four range axes."));
//        XYPlot var2 = (XYPlot)var1.getPlot();
//        var2.setOrientation(PlotOrientation.VERTICAL);
//        var2.setDomainPannable(true);
//        var2.setRangePannable(true);
//        NumberAxis var3 = new NumberAxis("Range Axis 2");
//        var3.setAutoRangeIncludesZero(false);
//        var2.setRangeAxis(1, var3);
//        var2.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_LEFT);
//        XYDataset var4 = createDataset("Series 2", 1000.0D, new Minute(), 170);
//        var2.setDataset(1, var4);
//        var2.mapDatasetToRangeAxis(1, 1);
//        StandardXYItemRenderer var5 = new StandardXYItemRenderer();
//        var2.setRenderer(1, var5);
//        NumberAxis var6 = new NumberAxis("Range Axis 3");
//        var2.setRangeAxis(2, var6);
//        XYDataset var7 = createDataset("Series 3", 10000.0D, new Minute(), 170);
//        var2.setDataset(2, var7);
//        var2.mapDatasetToRangeAxis(2, 2);
//        StandardXYItemRenderer var8 = new StandardXYItemRenderer();
//        var2.setRenderer(2, var8);
//        NumberAxis var9 = new NumberAxis("Range Axis 4");
//        var2.setRangeAxis(3, var9);
//        XYDataset var10 = createDataset("Series 4", 25.0D, new Minute(), 200);
//        var2.setDataset(3, var10);
//        var2.mapDatasetToRangeAxis(3, 3);
//        StandardXYItemRenderer var11 = new StandardXYItemRenderer();
//        var2.setRenderer(3, var11);
//        ChartUtils.applyCurrentTheme(var1);
//        var2.getRenderer().setSeriesPaint(0, Color.black);
//        var5.setSeriesPaint(0, Color.red);
//        var3.setLabelPaint(Color.red);
//        var3.setTickLabelPaint(Color.red);
//        var8.setSeriesPaint(0, Color.blue);
//        var6.setLabelPaint(Color.blue);
//        var6.setTickLabelPaint(Color.blue);
//        var11.setSeriesPaint(0, Color.green);
//        var9.setLabelPaint(Color.green);
//        var9.setTickLabelPaint(Color.green);
//        return var1;
    }

    public JPanel createPanel() {
        JFreeChart jFreeChart = createChart();
        ChartPanel chartPanel = new ChartPanel(jFreeChart);
        chartPanel.setMouseWheelEnabled(true);
        return chartPanel;
    }

    void sample() {
        stepsNeededSeries.add(episode, (int)testingSteps.getAverageAndMarkReset());


//        long episodesPerSecond = episode - previousEpisode;
//        previousEpisode = episode;
//        System.out.format("Episode %,d (%,d/s), testing success %d%% (%d steps needed)%n",
//                episode, episodesPerSecond, (int)testingPercent.getAverageAndMarkReset(), (int)testingSteps.getAverageAndMarkReset());
    }


}
