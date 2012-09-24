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

package org.amanzi.awe.catalog.neo.selection;

import java.util.Set;

import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.render.IGISModel.ILocationElement;
import org.amanzi.neo.models.render.IRenderableModel;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class Selection implements ISelection {

    private final IRenderableModel model;

    private final Set<IDataElement> elements;

    private final Set<ILocationElement> locations;

    /**
     * 
     */
    public Selection(IRenderableModel model, Set<IDataElement> selectedElements, Set<ILocationElement> selectedLocations) {
        this.model = model;
        this.elements = selectedElements;
        this.locations = selectedLocations;
    }

    @Override
    public IRenderableModel getModel() {
        return model;
    }

    @Override
    public Set<IDataElement> getSelectedElements() {
        return elements;
    }

    @Override
    public Set<ILocationElement> getSelectedLocations() {
        return locations;
    }

}