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

package org.amanzi.awe.views.properties.views.internal;

import java.util.ArrayList;
import java.util.List;

import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.IModel;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class DataElementPropertySource implements IPropertySource {

    private final IDataElement dataElement;

    private final IModel model;

    public DataElementPropertySource(final IDataElement dataElement) {
        this(dataElement, null);
    }

    public DataElementPropertySource(final IDataElement dataElement, final IModel model) {
        this.dataElement = dataElement;
        this.model = model;
    }

    @Override
    public Object getEditableValue() {
        return null;
    }

    @Override
    public IPropertyDescriptor[] getPropertyDescriptors() {
        final List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();

        propertyDescriptors.add(new DataElementPropertyDescriptor(DataElementPropertyDescriptor.ID_PROPERTY));
        for (final String propertyName : dataElement.keySet()) {
            propertyDescriptors.add(new DataElementPropertyDescriptor(propertyName));
        }

        return propertyDescriptors.toArray(new IPropertyDescriptor[propertyDescriptors.size()]);
    }

    @Override
    public Object getPropertyValue(final Object id) {
        if (id.equals(DataElementPropertyDescriptor.ID_PROPERTY)) {
            return dataElement.getId();
        }
        return dataElement.get(id.toString());
    }

    @Override
    public boolean isPropertySet(final Object id) {
        return dataElement.contains(id.toString());
    }

    @Override
    public void resetPropertyValue(final Object id) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setPropertyValue(final Object id, final Object value) {
        // TODO Auto-generated method stub

    }

}