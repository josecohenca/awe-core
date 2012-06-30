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

import java.awt.Color;

import org.amanzi.neo.dto.IDataElement;

/**
 * Interface that represents Distribution Bar
 * 
 * @author lagutko_n
 * @since 1.0.0
 */
public interface IDistributionBar extends Comparable<IDistributionBar> {

    /**
     * Returns Color of this Bar
     * 
     * @return
     */
    Color getColor();

    /**
     * Set Color for this bar
     * 
     * @param color
     */
    void setColor(Color color);

    /**
     * Number of Data Elements in this Bar
     * 
     * @return
     */
    int getCount();

    /**
     * Name of this Bar
     * 
     * @return
     */
    String getName();

    /**
     * Root Element of Bar
     * 
     * @return
     */
    IDataElement getRootElement();

    /**
     * Returns Distribution
     * 
     * @return
     */
    IDistributionModel getDistribution();

}