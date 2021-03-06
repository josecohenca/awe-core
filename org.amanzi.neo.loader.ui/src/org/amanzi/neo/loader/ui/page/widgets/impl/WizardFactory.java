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

import java.util.List;

import org.amanzi.neo.loader.core.ILoader;
import org.amanzi.neo.loader.core.internal.IConfiguration;
import org.amanzi.neo.loader.core.internal.LoaderCorePlugin;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectDriveNameWidget.ISelectDriveListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectDriveResourcesWidget.ISelectDriveResourceListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectLoaderWidget.ISelectLoaderListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectNetworkNameWidget.ISelectNetworkListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.internal.DriveDataFileSelector;
import org.amanzi.neo.loader.ui.page.widgets.internal.AbstractPageWidget;
import org.amanzi.neo.providers.IDriveModelProvider;
import org.amanzi.neo.providers.INetworkModelProvider;
import org.amanzi.neo.providers.IProjectModelProvider;
import org.apache.commons.io.filefilter.IOFileFilter;
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
public final class WizardFactory {

    private static class WizardFactoryHolder {
        private static volatile WizardFactory instance = new WizardFactory();
    }

    public static WizardFactory getInstance() {
        return WizardFactoryHolder.instance;
    }

    protected static <T extends AbstractPageWidget< ? , ? >> T initializeWidget(final T widget) {
        widget.initializeWidget();
        return widget;
    }

    private final IProjectModelProvider projectModelProvider;

    private final INetworkModelProvider networkModelProvider;

    private final IDriveModelProvider driveModelProvider;

    private WizardFactory() {
        projectModelProvider = LoaderCorePlugin.getInstance().getProjectModelProvider();
        networkModelProvider = LoaderCorePlugin.getInstance().getNetworkModelProvider();
        driveModelProvider = LoaderCorePlugin.getInstance().getDriveModelProvider();
    }

    public SelectDriveNameWidget addDatasetNameSelectorForDrive(final Composite parent, final ISelectDriveListener listener) {
        return initializeWidget(new SelectDriveNameWidget(parent, listener, projectModelProvider, driveModelProvider));
    }

    public SelectNetworkNameWidget addDatasetNameSelectorForNetwork(final Composite parent, final ISelectNetworkListener listener,
            final boolean isEditable, final boolean isEnabled) {
        return initializeWidget(new SelectNetworkNameWidget(parent, listener, isEditable, isEnabled, projectModelProvider,
                networkModelProvider));
    }

    public DriveDataFileSelector addDriveDataFileSelector(final Composite parent, final ISelectDriveResourceListener listener) {
        return initializeWidget(new DriveDataFileSelector(parent, listener));
    }

    public SelectDriveResourcesWidget addDriveResourceSelector(final Composite parent, final ISelectDriveResourceListener listener,
            final IOFileFilter filter) {
        return initializeWidget(new SelectDriveResourcesWidget(parent, listener, filter));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T extends IConfiguration> SelectLoaderWidget<T> addLoaderSelector(final Composite parent,
            final ISelectLoaderListener listener, final List<ILoader<T, ? >> loaders) {
        return initializeWidget(new SelectLoaderWidget(true, parent, listener, loaders, projectModelProvider));
    }

}
