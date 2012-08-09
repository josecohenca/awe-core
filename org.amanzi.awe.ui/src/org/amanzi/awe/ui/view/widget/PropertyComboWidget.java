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

package org.amanzi.awe.ui.view.widget;

import java.util.Collection;

import org.amanzi.awe.ui.view.widget.PropertyComboWidget.IPropertySelectionListener;
import org.amanzi.awe.ui.view.widget.internal.AbstractComboWidget;
import org.amanzi.neo.models.measurement.IMeasurementModel;
import org.amanzi.neo.models.statistics.IPropertyStatisticalModel;
import org.amanzi.neo.models.statistics.IPropertyStatisticsModel;
import org.amanzi.neo.nodetypes.INodeType;
import org.apache.commons.collections.CollectionUtils;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 *
 * </p>
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class PropertyComboWidget extends AbstractComboWidget<String, IPropertySelectionListener> {

    public interface IPropertySelectionListener extends AbstractComboWidget.IComboSelectionListener {

        void onPropertySelected(String property);

    }

    private IPropertyStatisticsModel propertyModel;

    private INodeType nodeType;

    /**
     * @param parent
     * @param label
     */
    protected PropertyComboWidget(final Composite parent, final String label) {
        super(parent, label);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Collection<String> getItems() {
        if (propertyModel != null) {
            if (nodeType == null) {
                return propertyModel.getPropertyNames();
            } else {
                return propertyModel.getPropertyNames(nodeType);
            }
        }
        return CollectionUtils.EMPTY_COLLECTION;
    }

    @Override
    protected String getItemName(final String item) {
        return item;
    }

    public void setModel(final IMeasurementModel model) {
        if (model != null) {
            this.propertyModel = model.getPropertyStatistics();
            this.nodeType = model.getMainMeasurementNodeType();

            fillCombo();
        }
    }

    public void setModel(final IPropertyStatisticalModel model, final INodeType nodeType) {
        this.propertyModel = model.getPropertyStatistics();
        this.nodeType = nodeType;
    }

    public void setModel(final IPropertyStatisticalModel model) {
        this.propertyModel = model.getPropertyStatistics();
    }

    @Override
    protected void fireListener(final IPropertySelectionListener listener, final String selectedItem) {
        listener.onPropertySelected(selectedItem);
    }

}