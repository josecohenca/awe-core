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

package org.amanzi.neo.models.distribution;

import java.util.List;

import org.amanzi.neo.nodetypes.INodeType;

/**
 * Type of Distribution
 * 
 * @author lagutko_n
 * @since 1.0.0
 */
public interface IDistribution<T extends IRange> {

    /**
     * Type of Chart
     * 
     * @author lagutko_n
     * @since 1.0.0
     */
    public enum ChartType {
        /*
         * Chart shows Counts of property for each bar
         */
        COUNTS("Counts"),

        /*
         * Chart shows Percents of count of property for each bar
         */
        PERCENTS("Percents"),

        /*
         * Chart show Logarithmic count of property for each bar
         */
        LOGARITHMIC("Logarithmic counts"),

        /*
         * Chart show CDF of this property
         */
        CDF("CDF Chart");

        /*
         * title of this type of chart
         */
        private String title;

        /**
         * Constructor
         * 
         * @param title
         */
        private ChartType(String title) {
            this.title = title;
        }

        /**
         * Returns title of this type of Chart
         * 
         * @return
         */
        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return getTitle();
        }

        /**
         * Searches corresponding ChartType enum by it's title
         * 
         * @param title
         * @return
         */
        public static ChartType findByTitle(String title) {
            for (ChartType singleType : values()) {
                if (singleType.title.equals(title)) {
                    return singleType;
                }
            }

            return null;
        }

        /**
         * Returns Default ChartType Default ChartType is COUNTS
         * 
         * @return
         */
        public static ChartType getDefault() {
            return COUNTS;
        }
    }

    /**
     * Type of Distribution Selection
     * 
     * @author gerzog
     * @since 1.0.0
     */
    public enum Select {
        MIN, MAX, AVERAGE, EXISTS, UNIQUE, FIRST;
    }

    /**
     * Returns name of this Distribution
     * 
     * @return
     */
    String getName();

    /**
     * Returns list of Ranges of this Distribution
     * 
     * @return
     */
    List<T> getRanges();

    /**
     * Type of Node to Analyze
     * 
     * @return
     */
    INodeType getNodeType();

    /**
     * Returns total number of nodes to analyse
     * 
     * @return
     */
    int getCount();

    /**
     * Initializes current distribution
     */
    void init();

    /**
     * Returns possible Selects
     * 
     * @return
     */
    Select[] getPossibleSelects();

    /**
     * Sets type of Selection
     * 
     * @param select
     */
    void setSelect(Select select);

    /**
     * Returns name of Analyzed Property
     * 
     * @return
     */
    String getPropertyName();

    /**
     * Is it possible to changes colors of this Distribution
     * 
     * @return
     */
    boolean canChangeColors();

    /**
     * Set possibility to change colors of this Distribution
     * 
     * @param canChangeColor
     */
    void setCanChangeColors(boolean canChangeColor);

}