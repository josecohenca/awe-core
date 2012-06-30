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

package org.amanzi.neo.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.amanzi.neo.nodeproperties.IGeneralNodeProperties;
import org.amanzi.neo.nodeproperties.impl.GeneralNodeProperties;
import org.amanzi.neo.nodetypes.INodeType;
import org.amanzi.neo.nodetypes.NodeTypeManager;
import org.amanzi.neo.nodetypes.NodeTypeUtils;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.exceptions.DuplicatedNodeException;
import org.amanzi.testing.AbstractIntegrationTest;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class NodeServiceIntegrationTest extends AbstractIntegrationTest {

    private enum TestRelatinshipType implements RelationshipType {
        TEST_REL;
    }

    private enum TestNodeType implements INodeType {
        TEST_NODE_TYPE1, TEST_NODE_TYPE2;

        @Override
        public String getId() {
            return NodeTypeUtils.getTypeId(this);
        }
    }

    private static final String[] CHILDREN_NAMES = new String[] {"child1", "child2", "child3"};

    private static final String CHILD_FOR_SEARCH = CHILDREN_NAMES[1];

    private static final String UNEXPECTED_CHILD = "unexpected child";

    private static final IGeneralNodeProperties GENERAL_NODE_PROPERTIES = new GeneralNodeProperties();

    private INodeService nodeService;

    @BeforeClass
    public static void setUpClass() throws IOException {
        AbstractIntegrationTest.setUpClass();

        NodeTypeManager.registerNodeType(TestNodeType.class);
    }

    @Before
    @Override
    public void setUp() {
        super.setUp();

        nodeService = new NodeService(getGraphDatabaseService(), GENERAL_NODE_PROPERTIES);
    }

    @Test
    public void testCheckGetAllNodes() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);

        Iterator<Node> result = nodeService.getChildren(parent);

        assertNotNull("result of search should not be null", result);

        @SuppressWarnings("unchecked")
        List<Node> resultList = IteratorUtils.toList(result);

        assertEquals("Unexpected size of children", CHILDREN_NAMES.length, resultList.size());

        for (Node node : resultList) {
            String name = (String)node.getProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty());

            assertTrue("name should exists in original children names", ArrayUtils.contains(CHILDREN_NAMES, name));
        }
    }

    @Test
    public void testCheckGetAllNodesWhenEmpty() throws Exception {
        Node parent = createNode();

        Iterator<Node> result = nodeService.getChildren(parent);

        assertFalse("It should not be any children", result.hasNext());
    }

    @Test
    public void testCheckGetAllChildrenWithOtherRelationship() throws Exception {
        Node parent = createNode();
        createChildren(TestRelatinshipType.TEST_REL, TestNodeType.TEST_NODE_TYPE1, parent);

        Iterator<Node> result = nodeService.getChildren(parent);

        assertFalse("It should not be any children", result.hasNext());
    }

    @Test
    public void testCheckGetAllNodesWithMixedRelTypes() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);
        createChildren(TestRelatinshipType.TEST_REL, TestNodeType.TEST_NODE_TYPE1, parent);

        Iterator<Node> result = nodeService.getChildren(parent);

        assertNotNull("result of search should not be null", result);

        @SuppressWarnings("unchecked")
        List<Node> resultList = IteratorUtils.toList(result);

        assertEquals("Unexpected size of childrent", CHILDREN_NAMES.length, resultList.size());

        for (Node node : resultList) {
            String name = (String)node.getProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty());

            assertTrue("name should exists in original children names", ArrayUtils.contains(CHILDREN_NAMES, name));
        }
    }

    @Test
    public void testCheckGetReferencedNode() throws Exception {
        assertEquals("unexpected referenced node", getGraphDatabaseService().getReferenceNode(), nodeService.getReferencedNode());
    }

    @Test
    public void testCheckGetChildByName() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);

        Node result = nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);

        assertNotNull("result of search should not be null", result);

        String name = (String)result.getProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty());

        assertEquals("name of found node is not the same as original", CHILD_FOR_SEARCH, name);
    }

    @Test
    public void testCheckGetChildByNameThatIsNotExists() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);

        Node result = nodeService.getChildByName(parent, UNEXPECTED_CHILD, TestNodeType.TEST_NODE_TYPE1);

        assertNull("Node cannot be found by this name", result);
    }

    @Test(expected = DuplicatedNodeException.class)
    public void testCheckDuplicatedNodeException() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);

        nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);
    }

    @Test
    public void testCheckGetNodeByNameWithoutChildren() throws Exception {
        Node parent = createNode();

        Node result = nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);

        assertNull("Node cannot be found by this name", result);
    }

    @Test
    public void testCheckGetNodeByNameWithChildrenByOtherRel() throws Exception {
        Node parent = createNode();
        createChildren(TestRelatinshipType.TEST_REL, TestNodeType.TEST_NODE_TYPE1, parent);

        Node result = nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);

        assertNull("Node cannot be found by this name", result);
    }

    @Test
    public void testCheckGetNodeByNameWithChildrenByMixedOtherRel() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);
        createChildren(TestRelatinshipType.TEST_REL, TestNodeType.TEST_NODE_TYPE1, parent);

        Node result = nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);

        assertNotNull("result of search should not be null", result);

        String name = (String)result.getProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty());

        assertEquals("name of found node is not the same as original", CHILD_FOR_SEARCH, name);
    }

    @Test
    public void testGetNameForNodeWithName() throws Exception {
        Node node = createNode(GENERAL_NODE_PROPERTIES.getNodeNameProperty(), GENERAL_NODE_PROPERTIES.getNodeNameProperty());

        String name = nodeService.getNodeName(node);

        assertEquals("unexpected name of node", GENERAL_NODE_PROPERTIES.getNodeNameProperty(), name);
    }

    @Test
    public void testGetNameForNodeWithType() throws Exception {
        Node node = createNode(GENERAL_NODE_PROPERTIES.getNodeTypeProperty(), TestNodeType.TEST_NODE_TYPE1.getId());

        INodeType type = nodeService.getNodeType(node);

        assertEquals("unexpected type of node", TestNodeType.TEST_NODE_TYPE1, type);
    }

    @Test
    public void testGetParent() throws Exception {
        Node parent = createNode();
        List<Node> children = createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);

        for (Node child : children) {
            Node result = nodeService.getParent(child);

            assertEquals("Unexpected parent of child", parent, result);
        }
    }

    @Test
    public void testGetParentWithoutParent() throws Exception {
        Node child = createNode();

        Node result = nodeService.getParent(child);

        assertNull("Parent should be null", result);
    }

    @Test(expected = DatabaseException.class)
    public void testGetParentWithDuplicatedParent() throws Exception {
        Node parent = createNode();
        Node anotherParent = createNode();

        List<Node> children = createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent,
                anotherParent);

        for (Node child : children) {
            nodeService.getParent(child);
        }
    }

    @Test
    public void testGetChildrenByNameWithDifferentNodeTypes() throws Exception {
        Node parent = createNode();
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE1, parent);
        createChildren(NodeService.NodeServiceRelationshipType.CHILD, TestNodeType.TEST_NODE_TYPE2, parent);

        Node result = nodeService.getChildByName(parent, CHILD_FOR_SEARCH, TestNodeType.TEST_NODE_TYPE1);

        assertNotNull("result of search should not be null", result);

        String name = (String)result.getProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty());

        assertEquals("name of found node is not the same as original", CHILD_FOR_SEARCH, name);
    }

    private List<Node> createChildren(RelationshipType relType, INodeType childNodeType, Node... parents) {
        List<Node> children = new ArrayList<Node>();

        Transaction tx = getGraphDatabaseService().beginTx();

        for (String name : CHILDREN_NAMES) {
            Node child = createNode(GENERAL_NODE_PROPERTIES.getNodeTypeProperty(), childNodeType.getId());
            child.setProperty(GENERAL_NODE_PROPERTIES.getNodeNameProperty(), name);

            for (Node parent : parents) {
                parent.createRelationshipTo(child, relType);
            }

            children.add(child);
        }

        tx.success();
        tx.finish();

        return children;
    }

}