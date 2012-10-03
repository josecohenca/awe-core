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

package org.amanzi.awe.ui.view.widgets;

import org.amanzi.awe.ui.AWEUIPlugin;
import org.amanzi.awe.ui.view.widgets.CheckBoxWidget.ICheckBoxSelected;
import org.amanzi.awe.ui.view.widgets.DateTimeWidget.ITimeChangedListener;
import org.amanzi.awe.ui.view.widgets.DriveComboWidget.IDriveSelectionListener;
import org.amanzi.awe.ui.view.widgets.PropertyComboWidget.IPropertySelectionListener;
import org.amanzi.awe.ui.view.widgets.internal.AbstractAWEWidget;
import org.amanzi.neo.providers.IDriveModelProvider;
import org.amanzi.neo.providers.IProjectModelProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public final class AWEWidgetFactory {

    private static class WizardFactoryHolder {
        private static volatile AWEWidgetFactory instance = new AWEWidgetFactory();
    }

    private final IProjectModelProvider projectModelProvider;

    private final IDriveModelProvider driveModelProvider;

    private AWEWidgetFactory() {
        this.projectModelProvider = AWEUIPlugin.getDefault().getProjectModelProvider();
        this.driveModelProvider = AWEUIPlugin.getDefault().getDriveModelProvider();
    }

    public static AWEWidgetFactory getFactory() {
        return WizardFactoryHolder.instance;
    }

    public DriveComboWidget addDriveComboWidget(final IDriveSelectionListener listener, final String labelText,
            final Composite parent, final int minimalLabelWidth) {
        return initializeWidget(new DriveComboWidget(parent, listener, labelText, projectModelProvider, driveModelProvider,
                minimalLabelWidth));
    }

    public PropertyComboWidget addPropertyComboWidget(final IPropertySelectionListener listener, final String labelText,
            final Composite parent, final int minimalLabelWidth) {
        return initializeWidget(new PropertyComboWidget(parent, listener, labelText, minimalLabelWidth));
    }

    public DateTimeWidget addPeriodWidget(final ITimeChangedListener listener, final String label, final Composite parent,
            final int minimalLabelWidth) {
        return initializeWidget(new DateTimeWidget(parent, listener, label, minimalLabelWidth));
    }

    public CheckBoxWidget addCheckBoxWidget(final ICheckBoxSelected listener, final String label, final Composite parent) {
        return initializeWidget(new CheckBoxWidget(parent, listener, label));
    }

    public <T extends AbstractAWEWidget< ? , ? >> T initializeWidget(final T widget) {
        widget.initializeWidget();
        return widget;
    }

}
