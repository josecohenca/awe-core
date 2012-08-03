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

package org.amanzi.awe.statistics.entities.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.amanzi.awe.statistics.entities.IAggregatedStatisticsEntity;
import org.amanzi.awe.statistics.service.StatisticsService;
import org.amanzi.neo.services.enums.INodeType;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.exceptions.DuplicateNodeNameException;
import org.amanzi.neo.services.exceptions.IllegalNodeDataException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;

/**
 * <p>
 * contains flagged methods
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public abstract class AbstractStorageEntity<T extends IAggregatedStatisticsEntity> extends AbstractFlaggedEntity {

    protected Map<String, T> children;
    private static final Logger LOGGER = Logger.getLogger(AbstractStorageEntity.class);

    /**
     * instantiation
     * 
     * @param parent
     * @param current
     * @param type
     */
    AbstractStorageEntity(Node parent, Node current, INodeType type) {
        super(parent, current, type);
    }

    /**
     * @param nodeType
     */
    AbstractStorageEntity(INodeType nodeType) {
        super(nodeType);
    }

    /**
     * try to find S_ROW node by timestamp ;
     * 
     * @param timestamp
     * @return
     * @throws DatabaseException
     * @throws IllegalNodeDataException
     */
    public T findChildByName(String name) {
        if ((name == null) || StringUtils.isEmpty(name)) {
            LOGGER.error("name element is null.");
            throw new IllegalArgumentException("timestamp element is null");
        }
        loadChildrenIfNecessary();
        return children.get(name);
    }

    /**
     * return all Statistics Rows
     * 
     * @return
     */
    public Collection<T> getAllChild() {
        loadChildrenIfNecessary();
        return children.values();
    }

    /**
     * Loads rows if necessary
     */
    protected void loadChildrenIfNecessary() {
        if (children == null) {
            children = new HashMap<String, T>();
            Iterable<Node> rowsNodes = statisticService.getChildrenChainTraverser(rootNode);
            if (rowsNodes == null) {
                return;
            }
            for (Node rowNode : rowsNodes) {
                String name = (String)statisticService.getNodeProperty(rowNode, StatisticsService.NAME);
                children.put(name, instantiateChild(rootNode, rowNode));
            }
        }
    }

    /**
     * create child and add to the CHILD->NEXT chain
     * 
     * @param name
     * @param entityType
     * @return
     * @throws DuplicateNodeNameException
     * @throws IllegalNodeDataException
     * @throws DatabaseException
     */
    protected T createChildWithName(String name, INodeType entityType) throws DuplicateNodeNameException, DatabaseException,
            IllegalNodeDataException {
        if (StringUtils.isEmpty(name) || entityType == null) {
            LOGGER.error("incorrect arguments values");
            throw new IllegalArgumentException("incorrect arguments values");
        }
        loadChildrenIfNecessary();
        if (children.containsKey(name)) {
            LOGGER.error("child with name." + name + "is already exists");
            throw new DuplicateNodeNameException();
        }
        Node entity = statisticService.createEntity(rootNode, entityType, name, Boolean.FALSE);
        T newChild = instantiateChild(rootNode, entity);
        children.put(name, newChild);
        return newChild;
    }

    /**
     * @param rootNode
     * @param rowNode
     * @return
     */
    protected abstract T instantiateChild(Node rootNode, Node rowNode);
}
