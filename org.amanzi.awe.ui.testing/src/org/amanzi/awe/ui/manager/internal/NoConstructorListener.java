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

package org.amanzi.awe.ui.manager.internal;

import org.amanzi.awe.ui.events.IEvent;
import org.amanzi.awe.ui.listener.IAWEEventListenter;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public final class NoConstructorListener implements IAWEEventListenter {

    /**
     * 
     */
    private NoConstructorListener() {
    }

    @Override
    public void onEvent(final IEvent event) {
    }

    public static String convertToString() {
        return new NoConstructorListener().toString();
    }

    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }

}
