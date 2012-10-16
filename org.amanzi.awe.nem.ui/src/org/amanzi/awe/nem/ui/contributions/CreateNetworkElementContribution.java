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

package org.amanzi.awe.nem.ui.contributions;

import java.util.Arrays;
import java.util.Collection;

import org.amanzi.awe.nem.managers.structure.NetworkStructureManager;
import org.amanzi.awe.nem.ui.utils.MenuUtils;
import org.amanzi.awe.nem.ui.wizard.NetworkElementCreationWizard;
import org.amanzi.awe.views.treeview.provider.ITreeItem;
import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.network.INetworkModel;
import org.amanzi.neo.nodetypes.INodeType;
import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public class CreateNetworkElementContribution extends AbstractNetworkMenuContribution {

    private static final Logger LOGGER = Logger.getLogger(CreateNetworkElementContribution.class);

    @Override
    public void fill(Menu menu, int index) {
        ITreeItem< ? , ? > item = getSelectedItem(LOGGER);

        if (item == null) {
            return;
        }

        final INetworkModel model = MenuUtils.getModelFromTreeItem(item);
        final IDataElement element = MenuUtils.getElementFromTreeItem(item);
        INodeType type = MenuUtils.getType(model, element);

        Collection<INodeType> types = NetworkStructureManager.getInstance().getUnderlineElements(type,
                Arrays.asList(model.getNetworkStructure()));
        if (LOGGER.isDebugEnabled()) {
            log(LOGGER, "Underline types " + Arrays.toString(types.toArray()), LoggerStatus.INFO);
        }
        for (final INodeType newType : types) {
            MenuItem menuItem = new MenuItem(menu, SWT.CHECK, index);
            menuItem.setText(newType.getId());
            menuItem.addSelectionListener(new SelectionAdapter() {
                public void widgetSelected(SelectionEvent e) {
                    openWizard(model, element, newType);
                }
            });
        }

    }

    @Override
    protected IWizard getWizard(INetworkModel model, IDataElement root, INodeType type) {
        return new NetworkElementCreationWizard(model, root, type);
    }

}
