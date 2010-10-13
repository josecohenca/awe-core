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

package org.amanzi.awe.report.grid.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.amanzi.awe.report.charts.ChartType;
import org.amanzi.awe.statistic.CallTimePeriods;
import org.amanzi.awe.statistics.database.entity.Statistics;
import org.amanzi.awe.statistics.database.entity.StatisticsCell;
import org.amanzi.awe.statistics.database.entity.StatisticsGroup;
import org.amanzi.awe.statistics.database.entity.StatisticsRow;
import org.amanzi.neo.core.utils.Pair;
import org.jfree.data.general.DefaultValueDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Utility class to create charts, datasets and other JFreeChart related components
 * 
 * @author Pechko_E
 * @since 1.0.0
 */
public class ChartUtilities {
    public static TimeSeriesCollection[] createChartDataset(Statistics stat, String siteName, String kpiName,
            String timeAggregation, ChartType type) {
        final Map<String, StatisticsGroup> groups = stat.getGroups();
        final StatisticsGroup statisticsGroup = groups.get(siteName);
        final TimeSeriesCollection timeSeriesCollection = new TimeSeriesCollection();
        final TimeSeries timeSeries = new TimeSeries(kpiName);
        final TimeSeriesCollection tresholdCollection = new TimeSeriesCollection();
        final TimeSeries thresholdTimeSeries = new TimeSeries("Average");
        double threshold = 0;
        for (Entry<String, StatisticsRow> entry : statisticsGroup.getRows().entrySet()) {
            StatisticsRow row = entry.getValue();
            if (!row.getName().equalsIgnoreCase("total")) {
                RegularTimePeriod period = null;
                GregorianCalendar calendar = new GregorianCalendar();
                calendar.setTimeInMillis(row.getPeriod());
                if (CallTimePeriods.HOURLY.getId().equalsIgnoreCase(timeAggregation)) {
                    period = new Hour(calendar.getTime());
                } else if (CallTimePeriods.DAILY.getId().equalsIgnoreCase(timeAggregation)) {
                    period = new Day(calendar.getTime());
                }
                final StatisticsCell cell = row.getCellByKey(kpiName);
                if (cell != null) {
                    timeSeries.add(period, cell.getValue().doubleValue());
                }
                thresholdTimeSeries.add(period, threshold);
            } else {
                threshold = row.getCellByKey(kpiName).getValue().doubleValue();
            }
        }
        tresholdCollection.addSeries(thresholdTimeSeries);
        timeSeriesCollection.addSeries(timeSeries);
        return new TimeSeriesCollection[] {tresholdCollection, timeSeriesCollection};
    }

    public static Map<String, Map<String, TimeSeries[]>> createChartDatasets(Statistics stat, String timeAggregation, ChartType type) {
        final Map<String, StatisticsGroup> groups = stat.getGroups();
        // site -> KPI -> datasets
        Map<String, Map<String, TimeSeries[]>> result = new HashMap<String, Map<String, TimeSeries[]>>(groups.size());
        for (StatisticsGroup group : groups.values()) {
            String key = group.getGroupName();
            final Map<String, StatisticsRow> rows = group.getRows();
            final Map<String, Double> thresholds = new HashMap<String, Double>();
            final HashMap<String, TimeSeries[]> dsPerKPI = new HashMap<String, TimeSeries[]>(rows.size());
            result.put(key, dsPerKPI);
            for (Entry<String, StatisticsRow> entry : rows.entrySet()) {
                StatisticsRow row = entry.getValue();

                if (!row.getName().equalsIgnoreCase("total")) {
                    RegularTimePeriod period = null;
                    GregorianCalendar calendar = new GregorianCalendar();
                    calendar.setTimeInMillis(row.getPeriod());
                    if (CallTimePeriods.HOURLY.getId().equalsIgnoreCase(timeAggregation)) {
                        period = new Hour(calendar.getTime());
                    } else if (CallTimePeriods.DAILY.getId().equalsIgnoreCase(timeAggregation)) {
                        period = new Day(calendar.getTime());
                    }

                    Map<String, StatisticsCell> cells = row.getCells();
                    for (StatisticsCell cell : cells.values()) {
                        final String kpiName = cell.getName();
                        TimeSeries[] ts = dsPerKPI.get(kpiName);
                        ts[1].add(period, cell.getValue().doubleValue());
                        ts[0].add(period, thresholds.get(kpiName));

                    }
                } else {
                    final Map<String, StatisticsCell> cells = row.getCells();
                    for (StatisticsCell cell : cells.values()) {
                        String kpiName = cell.getName();
                        double threshold = cell.getValue().doubleValue();
                        thresholds.put(kpiName, threshold);
                        TimeSeries timeSeries = new TimeSeries(kpiName);
                        TimeSeries thresholdTimeSeries = new TimeSeries("Average (" + new DecimalFormat("#0.0").format(threshold) + ")");
                        dsPerKPI.put(kpiName, new TimeSeries[] {thresholdTimeSeries, timeSeries});

                    }
                }
            }
            result.put(key, dsPerKPI);
        }
        return result;
    }

    public static DefaultValueDataset createDialChartDataset(Statistics stat, String siteName, String kpiName) {
        StatisticsGroup group = stat.getGroupByKey(siteName);
        final StatisticsRow row = group.getRowByKey("total");
        final StatisticsCell cell = row.getCellByKey(kpiName);
        DefaultValueDataset ds = new DefaultValueDataset();
        ds.setValue(cell.getValue().doubleValue());
        return ds;
    }
    public static  List<Pair<String,DefaultValueDataset>> createDialChartDatasets(Statistics stat, String kpiName) {
        final Map<String, StatisticsGroup> groups = stat.getGroups();
        List<Pair<String,DefaultValueDataset>> datasets=new ArrayList<Pair<String,DefaultValueDataset>>(groups.size());
        for (StatisticsGroup group :groups.values()){
            final StatisticsRow row = group.getRowByKey("total");
            final StatisticsCell cell = row.getCellByKey(kpiName);
            DefaultValueDataset ds = new DefaultValueDataset();
            ds.setValue(cell.getValue().doubleValue());
            datasets.add(new Pair<String,DefaultValueDataset>(group.getGroupName(),ds));
        }
        Collections.sort(datasets, new Comparator<Pair<String,DefaultValueDataset>>(){

            @Override
            public int compare(Pair<String,DefaultValueDataset> o1, Pair<String,DefaultValueDataset> o2) {
                final double o1val = o2.r().getValue().doubleValue();
                final double o2val = o1.r().getValue().doubleValue();
                if (o1val>o2val){
                    return 1;
                }else  if (o1val<o2val){
                    return 0;
                }
                return 0;
            }});
        return datasets;
    }
}