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

package org.amanzi.neo.loader.ui.page.impl.network;

import java.util.List;

import org.amanzi.neo.loader.core.ILoader;
import org.amanzi.neo.loader.core.ISingleFileConfiguration;
import org.amanzi.neo.loader.ui.internal.Messages;
import org.amanzi.neo.loader.ui.page.ILoaderPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class LoadNetworkPage extends WizardPage implements ILoaderPage<ISingleFileConfiguration> {

    /**
     * @param pageName
     */
    protected LoadNetworkPage() {
        super(Messages.LoadNetworkPage_PageName);
    }

    @Override
    public void addLoader(final ILoader<ISingleFileConfiguration, ? > loader) {
        // TODO Auto-generated method stub

    }

    @Override
    public void createControl(final Composite parent) {
        // TODO Auto-generated method stub

    }

    @Override
    public List<ILoader<ISingleFileConfiguration, ? >> getLoaders() {
        // TODO Auto-generated method stub
        return null;
    }

}
