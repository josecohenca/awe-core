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

package org.amanzi.awe.distribution.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.amanzi.awe.distribution.model.DistributionNodeType;
import org.amanzi.awe.distribution.model.type.IDistributionType;
import org.amanzi.awe.distribution.properties.IDistributionNodeProperties;
import org.amanzi.awe.distribution.service.IDistributionService;
import org.amanzi.neo.nodeproperties.IGeneralNodeProperties;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.exceptions.DuplicatedNodeException;
import org.amanzi.neo.services.exceptions.ServiceException;
import org.amanzi.neo.services.impl.internal.AbstractService;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.RelationshipType;
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
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class DistributionService extends AbstractService implements IDistributionService {

    public static enum DistributionRelationshipType implements RelationshipType {
        DISTRIBUTION, AGGREGATED;
    }

    private static final TraversalDescription BAR_BY_SOURCE_TRAVERSAL = Traversal.description().breadthFirst()
            .relationships(DistributionRelationshipType.AGGREGATED, Direction.INCOMING).evaluator(Evaluators.atDepth(1));

    private final class DistributionBarSourceEvaluator implements Evaluator {

        private final Node rootNode;

        public DistributionBarSourceEvaluator(final Node rootNode) {
            this.rootNode = rootNode;
        }

        @Override
        public Evaluation evaluate(final Path path) {
            boolean includes = false;

            try {
                includes = nodeService.getChainParent(path.endNode()).equals(rootNode);
            } catch (final ServiceException e) {
                // TODO: LN: 8.12.2012, handle error
            }

            return Evaluation.ofIncludes(includes);
        }
    }

    private final IDistributionNodeProperties distributionNodeProperties;

    private final INodeService nodeService;

    /**
     * @param graphDb
     * @param generalNodeProperties
     */
    public DistributionService(final GraphDatabaseService graphDb, final IGeneralNodeProperties generalNodeProperties,
            final IDistributionNodeProperties distributionNodeProperties, final INodeService nodeService) {
        super(graphDb, generalNodeProperties);

        this.nodeService = nodeService;
        this.distributionNodeProperties = distributionNodeProperties;
    }

    @Override
    public Node findDistributionNode(final Node rootNode, final IDistributionType< ? > distributionType) throws ServiceException {
        assert rootNode != null;
        assert distributionType != null;

        final Evaluator nameEvaluator = new PropertyEvaluator(getGeneralNodeProperties().getNodeNameProperty(),
                distributionType.getName());
        final Evaluator typeEvaluator = new PropertyEvaluator(distributionNodeProperties.getDistributionNodeType(),
                distributionType.getNodeType().getId());
        final Evaluator propertyEvaluator = new PropertyEvaluator(distributionNodeProperties.getDistributionPropertyName(),
                distributionType.getPropertyName());
        final Evaluator selectEvaluator = new PropertyEvaluator(distributionNodeProperties.getDistributionSelect(),
                distributionType.getSelect().name());

        final Iterator<Node> nodeIterator = nodeService
                .getChildrenTraversal(DistributionNodeType.DISTRIBUTION_ROOT, DistributionRelationshipType.DISTRIBUTION)
                .evaluator(nameEvaluator).evaluator(typeEvaluator).evaluator(propertyEvaluator).evaluator(selectEvaluator)
                .traverse(rootNode).nodes().iterator();

        Node result = null;

        if (nodeIterator.hasNext()) {
            result = nodeIterator.next();

            if (nodeIterator.hasNext()) {
                throw new DuplicatedNodeException("distributionType", distributionType);
            }
        }

        return result;
    }

    @Override
    public Node createDistributionNode(final Node rootNode, final IDistributionType< ? > distributionType) throws ServiceException {
        assert rootNode != null;
        assert distributionType != null;

        Node result = null;

        try {
            final Map<String, Object> properties = new HashMap<String, Object>();

            properties.put(getGeneralNodeProperties().getNodeNameProperty(), distributionType.getName());
            properties.put(distributionNodeProperties.getDistributionPropertyName(), distributionType.getPropertyName());
            properties.put(distributionNodeProperties.getDistributionNodeType(), distributionType.getNodeType().getId());
            properties.put(distributionNodeProperties.getDistributionSelect(), distributionType.getSelect().name());

            result = nodeService.createNode(rootNode, DistributionNodeType.DISTRIBUTION_ROOT,
                    DistributionRelationshipType.DISTRIBUTION, properties);
        } catch (final Exception e) {
            throw new DatabaseException(e);
        }

        return result;
    }

    @Override
    public Node getCurrentDistribution(final Node rootNode) throws ServiceException {
        assert rootNode != null;

        final Long currentDistributionNodeId = nodeService.getNodeProperty(rootNode,
                distributionNodeProperties.getCurrentDistributionProperty(), null, false);

        Node result = null;

        try {
            if (currentDistributionNodeId != null) {
                result = getGraphDb().getNodeById(currentDistributionNodeId);
            }
        } catch (final Exception e) {
            throw new DatabaseException(e);
        }

        return result;
    }

    @Override
    public void setCurrentDistribution(final Node rootNode, final Node currentDistributionNode) throws ServiceException {
        assert rootNode != null;
        assert currentDistributionNode != null;

        nodeService.updateProperty(rootNode, distributionNodeProperties.getCurrentDistributionProperty(),
                currentDistributionNode.getId());
    }

    @Override
    public Node createDistributionBarNode(final Node rootNode, final String name, final int[] color) throws ServiceException {
        assert rootNode != null;
        assert name != null;

        if (findDistributionBarNode(rootNode, name) != null) {
            throw new DuplicatedNodeException(getGeneralNodeProperties().getNodeNameProperty(), name);
        }

        final Map<String, Object> properties = new HashMap<String, Object>();
        if (color != null) {
            properties.put(distributionNodeProperties.getBarColor(), color);
        }
        final Node result = nodeService.createNodeInChain(rootNode, DistributionNodeType.DISTRIBUTION_BAR, name, properties);

        return result;
    }

    protected Node findDistributionBarNode(final Node rootNode, final String name) throws ServiceException {
        return nodeService.getChildInChainByName(rootNode, name, DistributionNodeType.DISTRIBUTION_BAR);
    }

    @Override
    public Node findDistributionBar(final Node rootNode, final Node sourceNode) throws ServiceException {
        assert rootNode != null;
        assert sourceNode != null;

        final Iterator<Node> nodeIterator = BAR_BY_SOURCE_TRAVERSAL.evaluator(new DistributionBarSourceEvaluator(rootNode))
                .traverse(sourceNode).nodes().iterator();

        Node result = null;

        if (nodeIterator.hasNext()) {
            result = nodeIterator.next();

            if (nodeIterator.hasNext()) {
                throw new DuplicatedNodeException("parent", rootNode);
            }
        }

        return result;
    }
}
