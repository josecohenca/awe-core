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
package org.amanzi.neo.services.node2node;

import org.amanzi.neo.services.DatasetService;
import org.amanzi.neo.services.INeoConstants;
import org.amanzi.neo.services.NeoServiceFactory;
import org.amanzi.neo.services.enums.NodeTypes;
import org.amanzi.neo.services.utils.Utils;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * Model of node to node relation
 * 
 * @author Kasnitskij_V
 * @since 1.0.0
 */
public class NodeToNodeRelationModel {
    // node-root
	private Node rootNode;
	// type of node to node relation
	private INodeToNodeType type;
	// node to node relation service
	private NodeToNodeRelationService node2nodeRelationService;
	// dataset service
	private DatasetService datasetService;
	private long countProxy;
	private long countRelation;
	private String proxyIndexKey;
	
	/**
	 * constructor of this class
	 * @param rootModelNode rootModel-node
	 * @param type type of relation
	 * @param name name of model
	 */
	public NodeToNodeRelationModel(Node rootModelNode, INodeToNodeType type, String name) {
		datasetService = NeoServiceFactory.getInstance().getDatasetService();
		node2nodeRelationService = NeoServiceFactory.getInstance().getNodeToNodeRelationService();
		
		this.type = type;
		this.rootNode = node2nodeRelationService.getNodeToNodeRelationsRoot(rootModelNode, type, name);
		countProxy=node2nodeRelationService.getCountProxy(rootNode);
		countRelation=node2nodeRelationService.getCountRelation(rootNode);
		proxyIndexKey = Utils.getLuceneIndexKeyByProperty(rootNode, INeoConstants.PROPERTY_NAME_NAME, NodeTypes.PROXY);
	}
	
	/**
	 * method to add relation between serving node and dependent node
	 * with some parameters to relation
	 *
	 * @param servingNode
	 * @param dependentNode
	 * @param parameters
	 */
	public Relationship getRelation(Node servingNode, Node dependentNode) {
	    Relationship result = node2nodeRelationService.getRelation(rootNode,proxyIndexKey,servingNode,dependentNode);
	    countProxy=node2nodeRelationService.getCountProxy(rootNode);
	    countRelation=node2nodeRelationService.getCountRelation(rootNode);
	    return result;
	}
	
	/**
	 * Cleares Model
	 * 
	 * @param deleteRootNode is Root Node should be deleted
	 */
	public void clear(boolean deleteRootNode) {
		node2nodeRelationService.clearNodeToNodeStructure(rootNode, new String[] {proxyIndexKey}, deleteRootNode);
	}

	public long getProxyCount() {
	    return countProxy;
	}
    public long getRelationCount() {
        return countRelation;
    }
}
