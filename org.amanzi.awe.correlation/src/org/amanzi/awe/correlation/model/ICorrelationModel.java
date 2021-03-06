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

package org.amanzi.awe.correlation.model;

import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.IModel;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.models.measurement.IMeasurementModel;
import org.amanzi.neo.models.network.INetworkModel;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
// TODO KV: add comments
public interface ICorrelationModel extends IModel {

    Iterable<IProxyElement> findAllProxies() throws ModelException;

    /**
     * @return
     */
    Integer getCorrelatedMCount();

    String getCorrelatedProperty();

    String getCorrelationProperty();

    Long getEndTime();

    /**
     * @return
     */
    IMeasurementModel getMeasurementModel();

    INetworkModel getNetworModel();

    int getProxiesCount();

    IProxyElement getProxy(IDataElement sector, IDataElement correlatedElement) throws ModelException;

    Long getStartTime();

    /**
     * @return
     */
    Integer getTotalMCount();

    /**
     * @return
     */
    Integer getTotalSectorsCount();
}
