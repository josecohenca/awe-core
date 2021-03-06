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

package org.amanzi.awe.correlation.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.amanzi.awe.correlation.exceptions.DuplicatedProxyException;
import org.amanzi.awe.correlation.model.CorrelationNodeTypes;
import org.amanzi.awe.correlation.nodeproperties.ICorrelationProperties;
import org.amanzi.awe.correlation.service.ICorrelationService;
import org.amanzi.neo.models.network.NetworkElementType;
import org.amanzi.neo.nodetypes.NodeTypeNotExistsException;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.exceptions.ServiceException;
import org.amanzi.neo.services.impl.internal.AbstractService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.Evaluator;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public class CorrelationService extends AbstractService implements ICorrelationService {

    public static enum CorrelationRelationshipType implements RelationshipType {
        CORRELATION, CORRELATED, PROXY;
    }

    private static final Logger LOGGER = Logger.getLogger(CorrelationService.class);

    private static final TraversalDescription INCOMING_PROXIES_TRAVERSAL = Traversal.description().depthFirst()
            .evaluator(Evaluators.excludeStartPosition()).relationships(CorrelationRelationshipType.PROXY, Direction.INCOMING);

    private static final TraversalDescription OUTGOUING_PROXIES_TRAVERSAL = Traversal.description().depthFirst()
            .evaluator(Evaluators.excludeStartPosition()).relationships(CorrelationRelationshipType.PROXY, Direction.OUTGOING);
    private final INodeService nodeSerivce;

    private final ICorrelationProperties correlationProperties;

    private final String CORRELATION_MODEL_NAME_FORMAT = "%s:%s@%s:%s";

    /**
     * @param nodeSerivce
     * @param generalNodeProeprties
     */
    public CorrelationService(final GraphDatabaseService graphDb, final INodeService nodeSerivce,
            final ICorrelationProperties correlationProperties) {
        super(graphDb, null);
        this.nodeSerivce = nodeSerivce;
        this.correlationProperties = correlationProperties;
    }

    @Override
    public Node createCorrelationModelNode(final Node networkRoot, final Node measurementRoot, final String correlationProperty,
            final String correlatedProperty) throws ServiceException {
        assert networkRoot != null;
        assert measurementRoot != null;
        assert !StringUtils.isEmpty(correlatedProperty);
        assert correlationProperty != null;
        assert !correlationProperty.isEmpty();

        String networkName = nodeSerivce.getNodeName(networkRoot);
        String measurementName = nodeSerivce.getNodeName(measurementRoot);

        String modelName = String.format(CORRELATION_MODEL_NAME_FORMAT, networkName, correlationProperty, measurementName,
                correlatedProperty);

        Node modelNode = nodeSerivce.createNode(networkRoot, CorrelationNodeTypes.CORRELATION_MODEL,
                CorrelationRelationshipType.CORRELATION, String.format(modelName, networkName, measurementName));

        nodeSerivce.updateProperty(modelNode, correlationProperties.getCorrelatedNodeProperty(), correlatedProperty);
        nodeSerivce.updateProperty(modelNode, correlationProperties.getCorrelationNodeProperty(), correlationProperty);

        nodeSerivce.linkNodes(measurementRoot, modelNode, CorrelationRelationshipType.CORRELATED);
        return modelNode;
    }

    @Override
    public Node createProxy(final Node rootNode, final Node sectorNode, final Node measurementNode, final String measuremntName)
            throws ServiceException {
        assert rootNode != null;
        assert sectorNode != null;
        assert !StringUtils.isEmpty(measuremntName);

        Node proxyNode = findProxy(sectorNode, measurementNode, measuremntName);

        if (proxyNode != null) {
            throw new DuplicatedProxyException(rootNode, sectorNode, measurementNode);
        }
        proxyNode = findSectorProxyForModel(sectorNode, measuremntName);
        if (proxyNode == null) {
            proxyNode = nodeSerivce.createNodeInChain(rootNode, CorrelationNodeTypes.PROXY);
            nodeSerivce.linkNodes(proxyNode, sectorNode, CorrelationRelationshipType.PROXY);
        }
        Relationship relationship = nodeSerivce.linkNodes(proxyNode, measurementNode, CorrelationRelationshipType.PROXY);
        nodeSerivce.updateProperty(relationship, correlationProperties.getCorrelatedModelNameProperty(), measuremntName);
        return proxyNode;
    }

    @Override
    public void deleteModel(final Node root) throws ServiceException, NodeTypeNotExistsException {
        assert root != null;
        assert nodeSerivce.getNodeType(root).equals(CorrelationNodeTypes.CORRELATION_MODEL);
        final Transaction tx = getGraphDb().beginTx();
        try {
            for (final Relationship rel : root.getRelationships(Direction.INCOMING)) {
                rel.delete();
            }
            deleteNode(root);
            tx.success();
        } catch (final Exception e) {
            tx.failure();
            throw new DatabaseException(e);
        } finally {
            tx.finish();
        }
    }

    /**
     * @param root
     * @throws NodeTypeNotExistsException
     * @throws ServiceException
     */
    private void deleteNode(final Node root) throws ServiceException, NodeTypeNotExistsException {
        if (!nodeSerivce.getNodeType(root).equals(CorrelationNodeTypes.CORRELATION_MODEL)
                || !nodeSerivce.getNodeType(root).equals(CorrelationNodeTypes.PROXY)) {
            return;
        }
        final List<Node> nextNode = new ArrayList<Node>();
        for (final Relationship rel : root.getRelationships(Direction.OUTGOING)) {
            nextNode.add(rel.getOtherNode(root));
            rel.delete();
        }
        for (final Relationship rel : root.getRelationships()) {
            rel.delete();
        }
        root.delete();
        for (final Node node : nextNode) {
            deleteNode(node);
        }
    }

    @Override
    public Iterator<Node> findAllNetworkCorrelations(final Node networkRoot) throws ServiceException {
        assert networkRoot != null;
        return nodeSerivce
                .getChildren(networkRoot, CorrelationNodeTypes.CORRELATION_MODEL, CorrelationRelationshipType.CORRELATION);
    }

    @Override
    public Node findCorrelationModelNode(final Node networkRoot, final Node measurementRoot, final String correlationProperty,
            final String correlatedProperty) throws ServiceException {
        assert networkRoot != null;
        assert measurementRoot != null;
        assert !StringUtils.isEmpty(correlatedProperty);
        assert correlationProperty != null;
        assert !correlationProperty.isEmpty();

        Iterator<Node> children = nodeSerivce.getChildren(networkRoot, CorrelationNodeTypes.CORRELATION_MODEL,
                CorrelationRelationshipType.CORRELATION);
        if (children == null || !children.hasNext()) {
            return null;
        }
        String networkName = nodeSerivce.getNodeName(networkRoot);
        String measuremerntName = nodeSerivce.getNodeName(measurementRoot);

        String modelName = String.format(CORRELATION_MODEL_NAME_FORMAT, networkName, correlationProperty, measuremerntName,
                correlatedProperty);

        while (children.hasNext()) {
            Node modelNode = children.next();
            if (nodeSerivce.getNodeName(modelNode).equals(modelName)) {
                return modelNode;
            }
        }
        return null;
    }

    private Node findMeasurementInProxy(final Node proxy, final Node measurementNode, final String measuremntName)
            throws ServiceException {
        Iterable<Relationship> rel = proxy.getRelationships(CorrelationRelationshipType.PROXY, Direction.OUTGOING);
        if (rel == null || !rel.iterator().hasNext()) {
            return null;
        }
        Iterator<Relationship> relationships = rel.iterator();

        while (relationships.hasNext()) {
            Relationship relation = relationships.next();
            if (relation.hasProperty(correlationProperties.getCorrelatedModelNameProperty())
                    && relation.getProperty(correlationProperties.getCorrelatedModelNameProperty()).equals(measuremntName)) {
                Node proxyMeasurement = relation.getOtherNode(proxy);
                if (proxyMeasurement.equals(measurementNode)) {
                    return proxyMeasurement;
                }
            }
        }
        return null;
    }

    @Override
    public Node findMeasurementModel(final Node correlation) throws ServiceException {
        assert correlation != null;

        return nodeSerivce.getParent(correlation, CorrelationRelationshipType.CORRELATED);
    }

    @Override
    public Node findProxy(final Node sectorNode, final Node measurementNode, final String measuremntName) throws ServiceException {
        Iterator<Node> proxies = findSectorProxies(sectorNode);
        Node proxyNode = null;
        while (proxies.hasNext()) {
            Node proxy = proxies.next();
            proxyNode = findMeasurementInProxy(proxy, measurementNode, measuremntName);
            if (proxyNode != null) {
                break;
            }
        }
        return proxyNode;
    }

    public Iterator<Node> findSectorProxies(final Node sectorNode) {
        assert sectorNode != null;
        return INCOMING_PROXIES_TRAVERSAL.traverse(sectorNode).nodes().iterator();
    }

    @Override
    public Node findSectorProxyForModel(final Node sectorNode, final String measuremntName) {
        Iterator<Node> proxies = findSectorProxies(sectorNode);
        Node proxyNode = null;
        while (proxies.hasNext()) {
            proxyNode = proxies.next();
            for (Relationship rel : proxyNode.getRelationships(Direction.OUTGOING, CorrelationRelationshipType.PROXY)) {
                Node otherNode = rel.getOtherNode(proxyNode);
                if (otherNode.equals(sectorNode)) {
                    continue;
                } else if (rel.getProperty(correlationProperties.getCorrelatedModelNameProperty()).equals(measuremntName)) {
                    return otherNode;
                }

            }
        }
        return null;
    }

    @Override
    public Iterator<Node> getMeasurementForProxy(final Node proxy, final String name) throws ServiceException {
        return OUTGOUING_PROXIES_TRAVERSAL.evaluator(new Evaluator() {

            @Override
            public Evaluation evaluate(final Path path) {
                Object property = path.lastRelationship().getProperty(correlationProperties.getCorrelatedModelNameProperty(), null);
                if (property != null && name.equals(property)) {
                    return Evaluation.INCLUDE_AND_CONTINUE;
                }
                return Evaluation.EXCLUDE_AND_CONTINUE;
            }
        }).traverse(proxy).nodes().iterator();
    }

    @Override
    public Node getSectorForProxy(final Node proxy) throws ServiceException {
        return searchNode(proxy);
    }

    private Node searchNode(final Node proxy) throws ServiceException {
        Iterable<Relationship> relations = proxy.getRelationships(Direction.OUTGOING, CorrelationRelationshipType.PROXY);
        if (proxy == null || !relations.iterator().hasNext()) {
            return null;
        }
        Node searchableNode;
        for (Relationship relation : relations) {
            searchableNode = relation.getOtherNode(proxy);
            try {
                if (nodeSerivce.getNodeType(searchableNode).equals(NetworkElementType.SECTOR)) {
                    return searchableNode;
                }
            } catch (NodeTypeNotExistsException e) {
                LOGGER.error("can't get type for node" + searchableNode);
                searchableNode = null;
            }
        }
        return null;
    }
}
