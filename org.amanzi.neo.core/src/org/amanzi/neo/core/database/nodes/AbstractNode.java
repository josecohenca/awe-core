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
package org.amanzi.neo.core.database.nodes;

import org.amanzi.neo.core.INeoConstants;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.RelationshipType;

/**
 * Abstract wrapper of Neo Database Node
 * 
 * @author Lagutko_N
 */

public abstract class AbstractNode {

	/*
	 * Wrapped Node
	 */
	protected Node node;

	/**
	 * Constructor of abstract Wrapper
	 * 
	 * @param node
	 *            wrapped Node
	 */

	public AbstractNode(Node node) {
		this.node = node;
	}

	/**
	 * Constructor of abstract Wrapper
	 * 
	 * @param node
	 *            wrapped Node
	 * @param nodeName
	 *            node name
	 * @param nodeType
	 *            node type
	 */
	public AbstractNode(Node node, String nodeName, String nodeType) {
		this(node);
		setParameter(INeoConstants.PROPERTY_NAME_NAME, nodeName);
		setParameter(INeoConstants.PROPERTY_TYPE_NAME, nodeType);
	}

	/**
	 * Method that creates a relationship to other node
	 * 
	 * @param type
	 *            type of Relationship
	 * @param node
	 *            related node
	 * @return created Relationship
	 */

	protected Relationship addRelationship(RelationshipType type, Node node) {
		return this.node.createRelationshipTo(node, type);
	}

	/**
	 * Method that creates a relationship to other node
	 * 
	 * @param type
	 *            type of Relationship
	 * @param node
	 *            wrapper node
	 */

	protected void addRelationship(RelationshipType type, AbstractNode node) {
		this.node.createRelationshipTo(node.getUnderlyingNode(), type);
	}

	/**
	 * Returns wrapped node
	 * 
	 * @return Database Node
	 */

	public Node getUnderlyingNode() {
		return node;
	}

	/**
	 * Sets the parameter of Node
	 * 
	 * @param name
	 *            Name of Parameter
	 * @param value
	 *            Value of Parameter
	 */

	protected void setParameter(String name, Object value) {
		//Lagutko, 6.10.2009, if Node already has this value than wouldn't change it
		//this fix will resolve problem with deadlocks on read
	    if (node.hasProperty(name)) {
	        Object previousValue = node.getProperty(name);
	        if (previousValue.equals(value)) {
	            return;
	        }
	    }
		node.setProperty(name, value);
	}

	/**
	 * Returns cached value of Parameter of get it from Node
	 * 
	 * @param name
	 *            parameter name
	 * @return value of parameter
	 */

	protected Object getParameter(String name) {
		Object value = null;

		if (node.hasProperty(name)) {
			value = node.getProperty(name);
		}

		return value;
	}

	/**
	 * Returns Name of this Node
	 * 
	 * @return
	 */

	public String getName() {
		return (String) getParameter(INeoConstants.PROPERTY_NAME_NAME);
	}

	/**
	 * Deletes this Node
	 * 
	 */
	public void delete() {
		node.delete();
	}
}
