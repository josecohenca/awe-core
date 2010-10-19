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
import org.amanzi.neo.loader.core.preferences.DataLoadPreferences;

/**
 * <p>
 * Validator for network sector data
 * </p>
 * 
 * @author tsinkel_a
 * @since 1.0.0
 */
public class NetworkSectorDataValidator implements ILoaderInputValidator<CommonConfigData>{

    private String[] possibleFieldSepRegexes = new String[] {"\t", ",", ";"};

    @Override
    public IValidateResult validate(CommonConfigData data) {
        return ValidatorUtils.checkFileAndHeaders(data.getRoot(), 3, new String[]{DataLoadPreferences.NH_SECTOR,DataLoadPreferences.NH_LATITUDE}, possibleFieldSepRegexes);
    }

    @Override
    public void filter(CommonConfigData data) {
    }


}
