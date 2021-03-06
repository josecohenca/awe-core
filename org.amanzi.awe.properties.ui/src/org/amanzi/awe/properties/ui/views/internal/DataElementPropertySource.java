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

package org.amanzi.awe.properties.ui.views.internal;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.amanzi.awe.properties.ui.messages.PropertiesViewMessages;
import org.amanzi.awe.ui.manager.AWEEventManager;
import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.IModel;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.models.internal.IDatasetModel;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
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

    private static final Logger LOGGER = Logger.getLogger(DataElementPropertySource.class);

    private final IDataElement dataElement;
    private final Map<String, Object> resetTableElement;
    private final IModel model;

    public DataElementPropertySource(final IDataElement dataElement) {
        this(dataElement, null);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public DataElementPropertySource(final IDataElement dataElement, final IModel model) {
        this.dataElement = dataElement;
        resetTableElement = new HashMap(dataElement.asMap());
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
        final Object object = dataElement.get(id.toString());

        if (object != null && object.getClass().isArray()) {
            return ArrayUtils.toString(object);
        }

        return object;
    }

    @Override
    public boolean isPropertySet(final Object id) {
        return !resetTableElement.get(id).equals(dataElement.get((String)id));
    }

    @Override
    public void resetPropertyValue(final Object id) {
        setPropertyValue(id, resetTableElement.get(id));
    }

    @Override
    public void setPropertyValue(final Object id, final Object value) {
        if (model instanceof IDatasetModel) {
            final IDatasetModel dataset = (IDatasetModel)model;
            try {
                if (!dataElement.get((String)id).getClass().equals(value.getClass())) {
                    showWarningDialog(PropertiesViewMessages.INCORRECT_PROPERTY_TYPE,
                            PropertiesViewMessages.INCORRECT_PROPERTY_TYPE_TEXT, value, dataElement.get((String)id), (String)id);

                    return;
                }

                dataset.updateProperty(dataElement, (String)id, value);
                dataElement.put((String)id, value);
                AWEEventManager.getManager().fireDataUpdatedEvent(null);
            } catch (final ModelException e) {
                LOGGER.error("can't update model  property", e);
            }
        }
    }

    /**
     * @param title
     * @param message
     * @param newValue
     * @param value
     * @param id
     */
    private void showWarningDialog(final String title, final String message, final Object newValue, final Object oldValue,
            final String id) {
        final String oldValueClass = oldValue != null ? oldValue.getClass().getSimpleName() : null;
        MessageDialog.openWarning(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), title, MessageFormat.format(
                message, id.toString(), newValue.toString(), newValue.getClass().getSimpleName(), oldValueClass));

    }
}
