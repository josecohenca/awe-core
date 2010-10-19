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

package org.amanzi.neo.loader.ui.validators;

import org.amanzi.neo.loader.core.CommonConfigData;
import org.amanzi.neo.loader.core.ILoaderInputValidator;
import org.amanzi.neo.loader.core.IValidateResult;

/**
 * <p>
 *Neighbour loader
 * </p>
 * @author tsinkel_a
 * @since 1.0.0
 */
public class NeighbourDataValidator implements ILoaderInputValidator<CommonConfigData>{

    @Override
    public IValidateResult validate(CommonConfigData data) {
        return null;
    }

    @Override
    public void filter(CommonConfigData data) {
    }

}
