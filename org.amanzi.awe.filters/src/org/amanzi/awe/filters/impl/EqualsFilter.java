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

package org.amanzi.awe.filters.impl;

import org.amanzi.awe.filters.impl.internal.AbstractFilter;
import org.amanzi.neo.dto.IDataElement;
import org.apache.commons.lang3.ObjectUtils;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class EqualsFilter<T extends Object> extends AbstractFilter<T> {

    private final T value;

    /**
     * @param propertyName
     */
    public EqualsFilter(final String propertyName, final T value) {
        super(propertyName);
        this.value = value;
    }

    @Override
    public boolean matches(final IDataElement element) {
        return ObjectUtils.equals(getElementValue(element), value);
    }

    @Override
    public boolean matches(final Object object) {
        return ObjectUtils.equals(object, value);
    }

}
