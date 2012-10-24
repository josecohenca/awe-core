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

package org.amanzi.awe.correlation.service;

import org.amanzi.neo.services.IService;
import org.amanzi.neo.services.exceptions.ServiceException;
import org.neo4j.graphdb.Node;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public interface ICorrelationService extends IService {

    Node createCorrelationModelNode(Node networkRoot, Node measurementRoot, String correlationProperty, String correlatedProperty)
            throws ServiceException;

    Node createProxy(Node rootNode, Node sectorNode, Node measurementNode, String measuremntName) throws ServiceException;

    Node findCorrelationModelNode(Node networkRoot, Node measurementRoot, String correlationProperty, String correlatedProperty)
            throws ServiceException;

    Node findProxy(Node sectorNode, Node measurementNode, String measuremntName) throws ServiceException;

    Node getMeasurementForProxy(Node proxy) throws ServiceException;

    Node getSectorForProxy(Node proxy) throws ServiceException;
}
