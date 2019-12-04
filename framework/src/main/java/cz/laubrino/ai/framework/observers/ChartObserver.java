package cz.laubrino.ai.framework.observers;

import cz.laubrino.ai.framework.Observer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.ui.ApplicationFrame;
import org.jfree.chart.util.DefaultShadowGenerator;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author tomas.laubr on 3.12.2019.
 */
public class ChartObserver implements Observer {
    private volatile long episode;
    private Averaging testingSteps = new Averaging();
    private Averaging deltaQAveragining = new Averaging();
    private final String chartTitle;
    private ScheduledExecutorService scheduledExecutorService;

    private XYSeries testSuccessSeries = new XYSeries("Testing success");
    private XYSeries stepsNeededSeries = new XYSeries("Steps per testing episode");
    private XYSeries deltaQSeries = new XYSeries("ΔQ");

    public ChartObserver(String title) {
        chartTitle = title;
    }


    @Override
    public void qValueChanged(float deltaQ, int deltaQInPercent) {
        deltaQAveragining.add(deltaQInPercent);
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
        testSuccessSeries.add(episode, (int)((float)successEpisodes/allEpisodes*100));
    }

    @Override
    public void end() {
        scheduledExecutorService.shutdown();
        try {
            scheduledExecutorService.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        sample();
    }

    @Override
    public void start(StartConfiguration startConfiguration) {
        createChart(startConfiguration);

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(this::sample, 1, 1, TimeUnit.SECONDS);
    }

    private void createChart(StartConfiguration startConfiguration) {
        XYSeriesCollection testSuccessDataset = new XYSeriesCollection();
        testSuccessDataset.addSeries(testSuccessSeries);

        JFreeChart xyLineChart = ChartFactory.createXYLineChart(chartTitle, "Episodes", "Testing success [%]", testSuccessDataset);

        XYPlot xyPlot = xyLineChart.getXYPlot();
        xyPlot.setShadowGenerator(new DefaultShadowGenerator());

        ValueAxis episodesAxis = xyPlot.getDomainAxis();
        episodesAxis.setAutoRange(false);
        episodesAxis.setLowerBound(0);
        episodesAxis.setUpperBound(startConfiguration.getEpisodes());

        ValueAxis percentAxis = xyPlot.getRangeAxis();
        percentAxis.setAutoRange(false);
        percentAxis.setLowerBound(0);
        percentAxis.setUpperBound(105);

        NumberAxis stepsAxis = new NumberAxis("Steps per testing episode");
        XYSeriesCollection stepsNeededDataset = new XYSeriesCollection();
        stepsNeededDataset.addSeries(stepsNeededSeries);
        xyPlot.setDataset(1, stepsNeededDataset);
        xyPlot.setRangeAxis(1, stepsAxis);
        xyPlot.mapDatasetToRangeAxis(1, 1);
        StandardXYItemRenderer stepsRenderer = new StandardXYItemRenderer();
        xyPlot.setRenderer(1, stepsRenderer);
        stepsRenderer.setSeriesPaint(0, Color.BLACK);

        NumberAxis deltaQAxis = new NumberAxis("ΔQ [%]");
        XYSeriesCollection deltaQDataset = new XYSeriesCollection();
        deltaQDataset.addSeries(deltaQSeries);
        xyPlot.setDataset(2, deltaQDataset);
        xyPlot.setRangeAxis(2, deltaQAxis);
        xyPlot.mapDatasetToRangeAxis(2,2);

        StandardXYItemRenderer deltQRenderer = new StandardXYItemRenderer();
        xyPlot.setRenderer(2, deltQRenderer);
        deltQRenderer.setSeriesPaint(0, Color.ORANGE);

        xyPlot.getRenderer().setSeriesPaint(0, Color.decode("0x0036CC"));

        ChartPanel chartPanel = new ChartPanel(xyLineChart);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(false);
        chartPanel.setMaximumDrawHeight(2000);
        chartPanel.setMaximumDrawWidth(3000);

        ApplicationFrame applicationFrame = new ApplicationFrame(chartTitle);
        applicationFrame.setContentPane(chartPanel);
        applicationFrame.pack();
        applicationFrame.setVisible(true);
    }

    private void sample() {
        stepsNeededSeries.add(episode, (int)testingSteps.getAverageAndMarkReset());
        deltaQSeries.add(episode, (int)deltaQAveragining.getAverageAndMarkReset());
    }


}
