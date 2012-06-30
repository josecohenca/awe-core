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

package org.amanzi.neo.impl.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.amanzi.neo.dto.IDataElement;
import org.neo4j.graphdb.Node;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class DataElement implements IDataElement {

    private Node node;

    private final Map<String, Object> properties = new HashMap<String, Object>();

    public DataElement() {

    }

    public DataElement(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node == null ? properties.toString() : node.toString();
    }

    @Override
    public boolean equals(Object anotherObject) {
        if (node == null) {
            return super.equals(anotherObject);
        } else {
            if (anotherObject instanceof DataElement) {
                DataElement anotherElement = (DataElement)anotherObject;

                return node.equals(anotherElement.getNode());
            }
        }

        return false;
    }

    @Override
    public int hashCode() {
        return node == null ? super.hashCode() : node.hashCode();
    }

    @Override
    public Object get(String header) {
        return properties.get(header);
    }

    @Override
    public Object put(String key, Object value) {
        return properties.put(key, value);
    }

    @Override
    public Set<String> keySet() {
        return properties.keySet();
    }
}