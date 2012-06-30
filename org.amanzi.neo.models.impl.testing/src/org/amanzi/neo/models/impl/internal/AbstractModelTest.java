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

package org.amanzi.neo.models.impl.internal;

import org.amanzi.neo.models.exceptions.DataInconsistencyException;
import org.amanzi.neo.models.exceptions.FatalException;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.nodetypes.INodeType;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.exceptions.PropertyNotFoundException;
import org.amanzi.testing.AbstractMockitoTest;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Node;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class AbstractModelTest extends AbstractMockitoTest {

    private static class TestModel extends AbstractModel {

        /**
         * @param nodeService
         */
        public TestModel(INodeService nodeService) {
            super(nodeService);
        }

        @Override
        public void finishUp() throws ModelException {
        }

    }

    private static final String TEST_NODE_NAME = "test name";

    private static final INodeType TEST_NODE_TYPE = new INodeType() {

        @Override
        public String getId() {
            return "test id";
        }
    };

    private INodeService nodeService;

    private AbstractModel model;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        nodeService = mock(INodeService.class);

        model = new TestModel(nodeService);

    }

    @Test
    public void testServiceActivityOnInitialize() throws Exception {
        Node rootNode = getNodeMock();

        model.initialize(rootNode);

        verify(nodeService).getNodeName(eq(rootNode));
        verify(nodeService).getNodeType(eq(rootNode));
        verify(nodeService).getParent(eq(rootNode));
    }

    @Test
    public void testCheckModelFieldsOnInitialize() throws Exception {
        Node rootNode = getNodeMock();
        Node parentNode = getNodeMock();

        when(nodeService.getNodeName(eq(rootNode))).thenReturn(TEST_NODE_NAME);
        when(nodeService.getNodeType(eq(rootNode))).thenReturn(TEST_NODE_TYPE);
        when(nodeService.getParent(eq(rootNode))).thenReturn(parentNode);

        model.initialize(rootNode);

        assertEquals("Unexpected initialized name", TEST_NODE_NAME, model.getName());
        assertEquals("Unexpected initialized type", TEST_NODE_TYPE, model.getType());
        assertEquals("Unexpected initialized parent node", parentNode, model.getParentNode());
        assertEquals("Unexpected initialized root node", rootNode, model.getRootNode());
    }

    @Test(expected = FatalException.class)
    public void testCheckFatalExceptionOnInitialize() throws Exception {
        Node rootNode = getNodeMock();

        when(nodeService.getNodeName(eq(rootNode))).thenThrow(new DatabaseException(new IllegalArgumentException()));

        model.initialize(rootNode);
    }

    @Test(expected = DataInconsistencyException.class)
    public void testCheckInconsistencyExceptionForNameOnInitialize() throws Exception {
        Node rootNode = getNodeMock();

        doThrow(new PropertyNotFoundException("Name", rootNode)).when(nodeService).getNodeName(rootNode);

        model.initialize(rootNode);
    }

    @Test(expected = DataInconsistencyException.class)
    public void testCheckInconsistencyExceptionForTypeOnInitialize() throws Exception {
        Node rootNode = getNodeMock();

        doThrow(new PropertyNotFoundException("Name", rootNode)).when(nodeService).getNodeName(rootNode);

        model.initialize(rootNode);
    }

    @Test(expected = DataInconsistencyException.class)
    public void testCheckInconsistencyExceptionForParentOnInitialize() throws Exception {
        Node rootNode = getNodeMock();

        doThrow(new PropertyNotFoundException("Parent", rootNode)).when(nodeService).getParent(rootNode);

        model.initialize(rootNode);
    }

}