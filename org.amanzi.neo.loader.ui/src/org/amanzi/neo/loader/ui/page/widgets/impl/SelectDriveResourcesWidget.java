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

package org.amanzi.neo.loader.ui.page.widgets.impl;

import java.io.File;
import java.util.Collection;

import org.amanzi.awe.ui.view.widgets.AWEWidgetFactory;
import org.amanzi.awe.ui.view.widgets.ResourceSelectorWidget;
import org.amanzi.awe.ui.view.widgets.ResourceSelectorWidget.IResourceSelectorListener;
import org.amanzi.neo.loader.ui.page.impl.internal.AbstractLoaderPage;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectDriveResourcesWidget.ISelectDriveResourceListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.internal.DriveDataFileSelector;
import org.amanzi.neo.loader.ui.page.widgets.internal.AbstractPageWidget;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
// TODO: LN: 10.10.2012, refactor to use widgets from org.amanzi.awe.ui
public class SelectDriveResourcesWidget extends AbstractPageWidget<Composite, ISelectDriveResourceListener>
        implements
            IResourceSelectorListener {

    public interface ISelectDriveResourceListener extends AbstractPageWidget.IPageEventListener {

        void onDirectorySelected(String directoryName);

        void onResourcesSelected(Collection<File> files);

    }

    private ResourceSelectorWidget resourceSelector;

    private final ISelectDriveResourceListener listener;

    private DriveDataFileSelector driveDataSelector;

    private final IOFileFilter filter;

    /**
     * @param isEnabled
     * @param parent
     * @param listener
     * @param projectModelProvider
     */
    protected SelectDriveResourcesWidget(final Composite parent, final ISelectDriveResourceListener listener,
            final IOFileFilter filter) {
        super(true, parent, listener, null);
        this.listener = listener;
        this.filter = filter;
    }

    @Override
    protected Composite createWidget(final Composite parent, final int style) {
        final Composite panel = new Composite(parent, style);
        panel.setLayoutData(getGroupLayoutData());
        panel.setLayout(new GridLayout(1, false));

        resourceSelector = AWEWidgetFactory.getFactory().addDirectorySelector(getPanel(panel), this);
        driveDataSelector = WizardFactory.getInstance().addDriveDataFileSelector(getPanel(panel), listener);
        driveDataSelector.setFileFilter(filter);

        return panel;
    }

    protected Object getGroupLayoutData() {
        // TODO: LN: 10.10.2012, make a factory for LayoutData
        return new GridData(SWT.FILL, SWT.CENTER, true, false, AbstractLoaderPage.NUMBER_OF_COLUMNS, 1);
    }

    private Composite getPanel(final Composite parent) {
        final Composite panel = new Composite(parent, SWT.NONE);
        // TODO: LN: 10.10.2012, make a factory for Layouts
        panel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        // TODO: LN: 10.10.2012, make a factory for LayoutData
        panel.setLayout(new GridLayout(3, false));

        return panel;
    }

    @Override
    protected int getStyle() {
        return SWT.NONE;
    }

    @Override
    public void onResourceChanged() {
        final String directoryName = resourceSelector.getFileName();

        for (final ISelectDriveResourceListener listener : getListeners()) {
            listener.onDirectorySelected(directoryName);
        }

        driveDataSelector.setFiles(new File(directoryName));
    }

    public void updateFilter(final IOFileFilter filter) {
        driveDataSelector.setFileFilter(filter);
    }

}
