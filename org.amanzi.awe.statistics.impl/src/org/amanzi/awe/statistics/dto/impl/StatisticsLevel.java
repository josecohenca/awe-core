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

package org.amanzi.awe.statistics.dto.impl;

import org.amanzi.awe.statistics.dto.IStatisticsLevel;
import org.amanzi.awe.statistics.model.DimensionType;
import org.amanzi.neo.impl.dto.DataElement;
import org.neo4j.graphdb.Node;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class StatisticsLevel extends DataElement implements IStatisticsLevel {

    private final DimensionType dimension;

    public StatisticsLevel(final Node node, final DimensionType dimension) {
        super(node);

        this.dimension = dimension;
    }

    @Override
    public DimensionType getDimension() {
        return dimension;
    }

}
