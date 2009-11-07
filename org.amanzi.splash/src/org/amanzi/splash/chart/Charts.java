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

package org.amanzi.splash.chart;

import java.awt.Color;
import java.awt.GradientPaint;

import org.amanzi.neo.core.NeoCorePlugin;
import org.amanzi.neo.core.database.exception.SplashDatabaseException;
import org.amanzi.neo.core.database.nodes.ChartItemNode;
import org.amanzi.neo.core.database.nodes.ChartNode;
import org.amanzi.neo.core.database.nodes.PieChartItemNode;
import org.amanzi.neo.core.database.nodes.PieChartNode;
import org.amanzi.neo.core.database.nodes.RubyProjectNode;
import org.amanzi.neo.core.database.nodes.SpreadsheetNode;
import org.amanzi.neo.core.database.services.AweProjectService;
import org.amanzi.splash.database.services.SpreadsheetService;
import org.amanzi.splash.swing.SplashTable;
import org.amanzi.splash.swing.SplashTableModel;
import org.amanzi.splash.ui.AbstractSplashEditor;
import org.amanzi.splash.ui.ChartEditorInput;
import org.amanzi.splash.ui.PieChartEditorInput;
import org.amanzi.splash.ui.SplashEditorInput;
import org.amanzi.splash.ui.SplashPlugin;
import org.amanzi.splash.utilities.NeoSplashUtil;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IElementFactory;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.Workbench;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 * TODO Purpose of 
 * <p>
 *
 * </p>
 * @author user
 * @since 1.0.0
 */
public class Charts implements IElementFactory{
    private static final String FACTORY_ID = Charts.class.getName();
    public static IEditorPart getActiveEditor(){
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getWorkbenchWindows()[0].getActivePage();
        return activePage.getActiveEditor();
    }

    @Override
    public IAdaptable createElement(IMemento memento) {
        return ChartEditorInput.createEditorInput(memento);
    }
    public static String getFactoryId() {
        return FACTORY_ID;
    }
    public static DefaultCategoryDataset createBarChartDataset(ChartNode chartNode) {
        DefaultCategoryDataset dataset= new DefaultCategoryDataset();
        for (ChartItemNode node : chartNode.getAllChartItems()) {
            dataset.addValue(Double.parseDouble((String)node.getValueNode().getValue()), node.getChartItemSeries(), (String)node.getCategoryNode().getValue());
        }
        return dataset;
    }
    /**
     * This method creates a chart.
     * @param dataset. dataset provides the data to be displayed in the chart. 
     * The parameter is provided by the 'createDataset()' method.
     * @return A chart.
     */
    public static JFreeChart createBarChart(DefaultCategoryDataset dataset) {

         // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            "",                         // chart title
            "",                         // domain axis label
            "Value",                    // range axis label
            dataset,                    // data
            PlotOrientation.VERTICAL,   // orientation
            true,                       // include legend
            true,                       // tooltips?
            false                       // URLs?
        );

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.blue,
                0.0f, 0.0f, new Color(0, 0, 64));
        renderer.setSeriesPaint(0, gp0);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        return chart;
    }
    /**
     * This method creates a chart.
     * 
     * @param dataset
     * dataset provides the data to be displayed in the chart. The
     * parameter is provided by the 'createDataset()' method.
     * @return A chart.
     */
    public static JFreeChart createPieChart(DefaultPieDataset dataset) {

        JFreeChart chart = ChartFactory
                .createPieChart3D(
                "",
                dataset, true, true, true);

        return chart;
    }
    
    public static  DefaultPieDataset createPieChartDataset(ChartNode chartNode) {
        DefaultPieDataset dataset  = new DefaultPieDataset();
        for (ChartItemNode node : chartNode.getAllChartItems()){
            dataset.setValue((String)node.getCategoryNode().getValue(), Double.parseDouble((String)node.getValueNode().getValue()));
        }
        return dataset;
    }
    public static void openChartEditor(final IEditorInput editorInput, final String editorId) {
        IWorkbench workbench = PlatformUI.getWorkbench();
        final IWorkbenchPage page = workbench.getWorkbenchWindows()[0].getActivePage();
            Display display = Display.getCurrent();
            if (display == null) {
                display = Display.getDefault();
            }
            display.syncExec(new Runnable() {

                @Override
                public void run() {
                    try {
                        NeoSplashUtil.logn("Try to open editor "+editorId);
                        page.openEditor(editorInput, editorId);
                    } catch (PartInitException e) {
                        NeoSplashUtil.logn(e.getMessage());
                    }
                }
            });
    }

}
