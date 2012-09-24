/* AWE - Amanzi Wireless Explorer
 * http://awe.amanzi.org
 * (C) 2008-2009, AmanziTel AB
 *
 * This library is provided under the terms of the Eclipse Public License
 * as described at http://www.eclipse.org/legal/epl-v10.html. Any use,
 * reproduction or distribution of the library constitutes recipient's
 * acceptance of this agreement.
 *
 * This library is distributed WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.amanzi.awe.chart.builder;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.List;

import org.amanzi.awe.chart.builder.dataset.dto.impl.CategoryDatasetContainer;
import org.amanzi.awe.charts.model.IChartModel;
import org.amanzi.awe.charts.model.IRangeAxis;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.Dataset;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public class CategoryChartBuilder
        extends
            AbstractMultiAxisChartBuilder<CategoryPlot, CategoryDatasetContainer, BarRenderer, LineAndShapeRenderer, CategoryAxis, NumberAxis> {

    private final static Color TRANSPERENT_COLOR = new Color(0, 0, 0, 0);

    private static final GradientPaint GRADIENT_PAINT = new GradientPaint(0.0f, 0.0f, Color.blue, 0.0f, 0.0f, new Color(0, 0, 64));

    private static final double ITEM_MARGIN = 0.2d;

    private static final double MAXIMUM_BAR_WIDTH = .04;

    private static final int MAXIMUM_CATEGORY_LABEL_WIDTH = 10;

    private static final int MAXIMUM_CATEGIRY_LABEL_LINES = 2;

    public CategoryChartBuilder(IChartModel model) {
        super(model);
    }

    @Override
    protected CategoryDatasetContainer createDataset(IChartModel model) {
        return new CategoryDatasetContainer(model);
    }

    @Override
    protected JFreeChart createDefaultChart() {
        return null;
    }

    @Override
    protected BarRenderer configMainRenderer() {
        BarRenderer renderer = new BarRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        renderer.setShadowVisible(false);
        renderer.setDrawBarOutline(false);

        renderer.setMaximumBarWidth(MAXIMUM_BAR_WIDTH);
        renderer.setSeriesPaint(0, GRADIENT_PAINT);
        renderer.setItemMargin(ITEM_MARGIN);
        return renderer;
    }

    @Override
    protected LineAndShapeRenderer configSubRenderer() {
        LineAndShapeRenderer renderer = new LineAndShapeRenderer();
        renderer.setBaseToolTipGenerator(new StandardCategoryToolTipGenerator());
        renderer.setItemMargin(ITEM_MARGIN);
        return renderer;
    }

    @Override
    protected NumberAxis configRangeAxis(IRangeAxis axis) {
        NumberAxis valueAxis = new NumberAxis(axis.getName());
        valueAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return valueAxis;
    }

    @Override
    protected CategoryAxis configDomainAxis(String domainAxisName) {
        CategoryAxis domainAxis = new CategoryAxis(domainAxisName);
        domainAxis.setMaximumCategoryLabelWidthRatio(MAXIMUM_CATEGORY_LABEL_WIDTH);
        domainAxis.setMaximumCategoryLabelLines(MAXIMUM_CATEGIRY_LABEL_LINES);

        return domainAxis;
    }

    @Override
    protected CategoryPlot plotSetup(Dataset dataset, CategoryAxis domainAxis, NumberAxis mainRangeAxis, BarRenderer mainRenderer) {
        CategoryDataset catDataset = (CategoryDataset)dataset;
        CategoryPlot plot = new CategoryPlot(catDataset, domainAxis, mainRangeAxis, mainRenderer);
        plot.setOrientation(getModel().getPlotOrientation());
        setVisibleColumns(catDataset, domainAxis);
        return plot;
    }

    /**
     * setup count of visible columns for domain axis
     * 
     * @param catDataset
     * @param domainAxis
     */
    @SuppressWarnings("unchecked")
    private void setVisibleColumns(CategoryDataset catDataset, CategoryAxis domainAxis) {
        List<Comparable< ? >> rowKey = catDataset.getColumnKeys();
        int middleElement = rowKey.size() / 2;

        int i = 0;
        for (Comparable< ? > cat : rowKey) {
            i++;
            if (i == middleElement || i == 1 || i == rowKey.size()) {
                domainAxis.setTickLabelPaint(cat, Color.black);
            } else {
                domainAxis.setTickLabelPaint(cat, TRANSPERENT_COLOR);
            }
        }

    }

    @Override
    protected void setSecondAxisForPlot(CategoryPlot plot, Dataset dataset, LineAndShapeRenderer subRenderer, NumberAxis secondAxis) {
        plot.setDataset(1, (CategoryDataset)dataset);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRangeAxis(1, secondAxis);
        plot.setRangeAxisLocation(1, AxisLocation.TOP_OR_LEFT);
        plot.setRenderer(1, subRenderer);
    }

    @Override
    protected JFreeChart finishUp(JFreeChart chart) {
        return new JFreeChart(getModel().getName(), getPlot());
    }
}