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

package org.amanzi.awe.distribution.model.bar.impl;

import java.awt.Color;

import org.amanzi.awe.distribution.model.bar.IDistributionBar;
import org.amanzi.neo.impl.dto.SourcedElement;
import org.neo4j.graphdb.Node;

/**
 * TODO Purpose of
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class DistributionBar extends SourcedElement implements IDistributionBar {

    private int count;

    /**
     * @param node
     * @param collectFunction
     */
    public DistributionBar(final Node node, final ICollectFunction collectFunction) {
        super(node, collectFunction);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Color getColor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setColor(final Color color) {
        // TODO Auto-generated method stub

    }

    @Override
    public int getCount() {
        return count;
    }

    public void setCount(final int count) {
        this.count = count;
    }

}