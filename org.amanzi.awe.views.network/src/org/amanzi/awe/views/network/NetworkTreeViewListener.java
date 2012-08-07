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

package org.amanzi.awe.views.network;

import org.amanzi.awe.ui.events.impl.ShowInViewEvent;
import org.amanzi.awe.ui.listener.AbstractShowViewListener;
import org.amanzi.awe.views.network.view.NetworkTreeView;

/**
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public class NetworkTreeViewListener extends AbstractShowViewListener {

    /**
     * @param requiredView
     */
    protected NetworkTreeViewListener() {
        super(NetworkTreeView.NETWORK_TREE_VIEW_ID);
    }

    @Override
    protected void handleEventInView(ShowInViewEvent showInView) {
        ((NetworkTreeView)getView()).selectDataElement(showInView.getElement());
    }

}