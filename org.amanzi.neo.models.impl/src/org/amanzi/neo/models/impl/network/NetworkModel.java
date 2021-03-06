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

package org.amanzi.neo.models.impl.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.amanzi.awe.filters.IFilter;
import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.impl.dto.DataElement;
import org.amanzi.neo.impl.dto.SectorElement;
import org.amanzi.neo.impl.dto.SiteElement;
import org.amanzi.neo.impl.util.IDataElementIterator;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.models.exceptions.ParameterInconsistencyException;
import org.amanzi.neo.models.impl.internal.AbstractDatasetModel;
import org.amanzi.neo.models.network.INetworkModel;
import org.amanzi.neo.models.network.NetworkElementType;
import org.amanzi.neo.models.render.IGISModel.ILocationElement;
import org.amanzi.neo.nodeproperties.IGeneralNodeProperties;
import org.amanzi.neo.nodeproperties.IGeoNodeProperties;
import org.amanzi.neo.nodeproperties.INetworkNodeProperties;
import org.amanzi.neo.nodetypes.INodeType;
import org.amanzi.neo.nodetypes.NodeTypeManager;
import org.amanzi.neo.nodetypes.NodeTypeNotExistsException;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.ServiceException;
import org.amanzi.neo.services.impl.NodeService.NodeServiceRelationshipType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;

import com.vividsolutions.jts.geom.Envelope;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class NetworkModel extends AbstractDatasetModel implements INetworkModel {

    // TODO: LN: 12.09.2012, duplicates AbstractMeasurementModel.ElementLocationIterator
    private final class ElementLocationIterator implements IDataElementIterator<ILocationElement> {

        private final Iterator<IDataElement> dataElements;

        private ILocationElement nextElement;

        private boolean moveToNext;

        private final Set<ILocationElement> locationElements = new HashSet<ILocationElement>();

        public ElementLocationIterator(final Iterable<IDataElement> dataElements) {
            this.dataElements = dataElements.iterator();

            moveToNext = true;
        }

        @Override
        public boolean hasNext() {
            if (moveToNext) {
                nextElement = moveToNext();
            }
            return nextElement != null;
        }

        private ILocationElement moveToNext() {
            try {
                ILocationElement element = null;
                while (dataElements.hasNext() && element == null) {
                    final IDataElement dataElement = dataElements.next();

                    if (dataElement.getNodeType().equals(NetworkElementType.SITE)) {
                        element = getLocationElement(((DataElement)dataElement).getNode());
                    } else if (dataElement.getNodeType() instanceof NetworkElementType) {
                        if (NetworkElementType.compare(NetworkElementType.SITE, (NetworkElementType)dataElement.getNodeType()) < 0) {
                            IDataElement tempElement = dataElement;
                            while (!tempElement.getNodeType().equals(NetworkElementType.SITE)) {
                                tempElement = NetworkModel.this.getParentElement(tempElement);
                            }

                            element = getLocationElement(((DataElement)tempElement).getNode());
                        }

                    }
                }

                if (element != null) {
                    locationElements.add(element);
                }

                return element;
            } catch (final ModelException e) {
                LOGGER.error(e);
                return null;
            } finally {
                moveToNext = false;
            }
        }

        @Override
        public ILocationElement next() {
            if (moveToNext) {
                nextElement = moveToNext();
            }

            moveToNext = true;
            return nextElement;
        }

        @Override
        public void remove() {
            dataElements.remove();
        }

        @Override
        public Iterable<ILocationElement> toIterable() {
            return new Iterable<ILocationElement>() {

                @Override
                public Iterator<ILocationElement> iterator() {
                    return ElementLocationIterator.this;
                }
            };
        }

    }

    public enum NetworkRelationshipTypes implements RelationshipType {
        SYNONYMS;
    }

    private static final Logger LOGGER = Logger.getLogger(NetworkModel.class);

    private static final String SYNONYMS_KEY_FORMAT = "%s.%s";

    private final INetworkNodeProperties networkNodeProperties;

    private final LRUMap elementLocationsIteratorCache = new LRUMap(5);

    private List<INodeType> structure = new ArrayList<INodeType>() {
        /** long serialVersionUID field */
        private static final long serialVersionUID = 7149098047373556881L;

        {
            add(NetworkElementType.NETWORK);
        }
    };

    private Map<String, String> synonyms;

    private Node synonymNode;

    /**
     * @param nodeService
     * @param generalNodeProperties
     */
    public NetworkModel(final INodeService nodeService, final IGeneralNodeProperties generalNodeProperties,
            final IGeoNodeProperties geoNodeProperties, final INetworkNodeProperties networkNodeProperties) {
        super(nodeService, generalNodeProperties, geoNodeProperties);
        this.networkNodeProperties = networkNodeProperties;
    }

    protected DataElement createDefaultElement(final INetworkElementType elementType, final IDataElement parent, final String name,
            final Map<String, Object> properties) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("createDefaultElement", elementType, parent, name, properties));
        }

        DataElement result = null;

        try {
            final Node parentNode = ((DataElement)parent).getNode();
            final Node node = getNodeService().createNode(parentNode, elementType, NodeServiceRelationshipType.CHILD, name,
                    properties);

            updateNetworkStructure(parent, elementType);

            getIndexModel().index(elementType, node, getGeneralNodeProperties().getNodeNameProperty(), name);

            getPropertyStatisticsModel().indexElement(elementType, removeIgnoredProperties(properties));

            result = new DataElement(node);
            result.setName(name);
            result.setNodeType(elementType);
        } catch (final ServiceException e) {
            processException("An error occured on creating new Network Element", e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("createDefaultElement"));
        }

        return result;
    }

    @Override
    public IDataElement createElement(final INetworkElementType elementType, final IDataElement parent, final String name,
            final Map<String, Object> properties) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("createElement", elementType, parent, name, properties));
        }

        // validate input
        if (elementType == null) {
            throw new ParameterInconsistencyException("elementType");
        }

        if (parent == null) {
            throw new ParameterInconsistencyException("parent");
        }
        if (StringUtils.isEmpty(name)) {
            throw new ParameterInconsistencyException("name", name);
        }
        if (properties == null) {
            throw new ParameterInconsistencyException("properties", null);
        }

        IDataElement result = null;

        if (elementType.equals(NetworkElementType.SECTOR)) {
            final Integer ci = (Integer)properties.get(networkNodeProperties.getCIProperty());
            final Integer lac = (Integer)properties.get(networkNodeProperties.getLACProperty());
            result = createSector(parent, name, lac, ci, properties);
        } else if (elementType.equals(NetworkElementType.SITE)) {
            final Double lat = (Double)properties.get(getGeoNodeProperties().getLatitudeProperty());
            final Double lon = (Double)properties.get(getGeoNodeProperties().getLongitudeProperty());

            result = createSite(parent, name, lat, lon, properties);
        } else {
            result = createDefaultElement(elementType, parent, name, properties);
        }
        updateSynonyms(elementType, properties);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("createElement"));
        }

        return result;
    }

    protected IDataElement createSector(final IDataElement parent, final String name, final Integer lac, final Integer ci,
            final Map<String, Object> properties) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("createSector", parent, name, lac, ci, properties));
        }

        final DataElement result = createDefaultElement(NetworkElementType.SECTOR, parent, name, properties);

        if (result != null) {
            if (ci != null) {
                getIndexModel().index(NetworkElementType.SECTOR, result.getNode(), networkNodeProperties.getCIProperty(), ci);
            }
            if (lac != null) {
                getIndexModel().index(NetworkElementType.SECTOR, result.getNode(), networkNodeProperties.getLACProperty(), lac);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("createSector"));
        }

        return result;
    }

    protected IDataElement createSite(final IDataElement parent, final String name, final Double latitude, final Double longitude,
            final Map<String, Object> properties) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("createSite", parent, name, latitude, longitude, properties));
        }

        // validate input
        if (latitude == null) {
            throw new ParameterInconsistencyException(getGeoNodeProperties().getLatitudeProperty());
        }
        if (longitude == null) {
            throw new ParameterInconsistencyException(getGeoNodeProperties().getLongitudeProperty());
        }

        final DataElement result = createDefaultElement(NetworkElementType.SITE, parent, name, properties);

        if (result != null) {
            getIndexModel().indexInMultiProperty(NetworkElementType.SITE, result.getNode(), Double.class,
                    getGeoNodeProperties().getLatitudeProperty(), getGeoNodeProperties().getLongitudeProperty());
            updateLocation(latitude, longitude);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("createSite"));
        }

        return result;
    }

    @Override
    public IDataElement findElement(final INetworkElementType elementType, final String elementName) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("findElement", elementType, elementName));
        }

        // validate input
        if (elementType == null) {
            throw new ParameterInconsistencyException("elementType");
        }

        if (StringUtils.isEmpty(elementName)) {
            throw new ParameterInconsistencyException("elementName", elementName);
        }

        IDataElement result = null;

        final Node elementNode = getIndexModel().getSingleNode(elementType, getGeneralNodeProperties().getNodeNameProperty(),
                elementName);

        if (elementNode != null) {
            result = new DataElement(elementNode);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("findElement"));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IDataElement findSector(final String sectorName, final Integer ci, final Integer lac) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("findSector", sectorName, ci, lac));
        }

        // validate input
        if (StringUtils.isEmpty(sectorName) && ci == null && lac == null) {
            throw new ParameterInconsistencyException(getGeneralNodeProperties().getNodeNameProperty(), sectorName);
        }
        if (ci == null && StringUtils.isEmpty(sectorName)) {
            throw new ParameterInconsistencyException(networkNodeProperties.getCIProperty(), ci);
        }

        IDataElement result = null;

        if (!StringUtils.isEmpty(sectorName)) {
            result = findElement(NetworkElementType.SECTOR, sectorName);
        }
        if (result == null && ci != null) {
            final List<Node> ciList = getNodeListFromIndex(NetworkElementType.SECTOR, networkNodeProperties.getCIProperty(), ci);
            List<Node> resultList = null;

            if (lac != null) {
                final List<Node> lacNodes = getNodeListFromIndex(NetworkElementType.SECTOR, networkNodeProperties.getLACProperty(),
                        lac);

                resultList = new ArrayList<Node>(CollectionUtils.intersection(ciList, lacNodes));
            }

            if (resultList != null && !resultList.isEmpty()) {
                result = new DataElement(resultList.get(0));
            } else if (!ciList.isEmpty() && lac == null) {
                result = new DataElement(ciList.get(0));
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("findElement"));
        }
        return result;
    }

    @Override
    public void finishUp() throws ModelException {
        LOGGER.info("Finishing up model <" + getName() + ">");
        try {
            initializeSynonyms();
            getNodeService().updateProperties(synonymNode, synonyms);
            getNodeService().updateProperty(getRootNode(), networkNodeProperties.getStuctureProperty(),
                    structureToStringArrayFormat());
        } catch (final ServiceException e) {
            processException("can't set structure properties", e);
        }
        super.finishUp();
    }

    @Override
    public Iterable<IDataElement> getAllElementsByType(final INodeType nodeType) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("getAllElementsByType", nodeType));
        }

        final Iterable<IDataElement> result = new DataElementIterator(getIndexModel().getAllNodes(nodeType)).toIterable();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("getAllElementsByType"));
        }

        return result;
    }

    @Override
    public ILocationElement getElementLocation(final IDataElement dataElement) throws ModelException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getStartLogStatement("getElelemntLocation", dataElement));
        }

        ILocationElement location = null;

        final Node elementNode = ((DataElement)dataElement).getNode();

        try {
            if (dataElement instanceof ISiteElement) {
                location = (ISiteElement)dataElement;
            } else if (dataElement.getNodeType().equals(NetworkElementType.SITE)) {
                location = getLocationElement(elementNode);
            } else if (dataElement.getNodeType().equals(NetworkElementType.SECTOR)) {
                location = getLocationElement(getParent(elementNode));
            }
        } catch (final ServiceException e) {
            processException("Error on computing Location element", e);
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(getFinishLogStatement("getElementLocation"));
        }

        return location;
    }

    @Override
    public Iterable<ILocationElement> getElements(final Envelope bound) throws ModelException {
        final Double[] min = new Double[] {bound.getMinY(), bound.getMinX()};
        final Double[] max = new Double[] {bound.getMaxY(), bound.getMaxX()};

        final Iterator<Node> nodeIterator = getIndexModel().getNodes(NetworkElementType.SITE, Double.class, min, max,
                getGeoNodeProperties().getLatitudeProperty(), getGeoNodeProperties().getLongitudeProperty());

        return new LocationIterator(nodeIterator).toIterable();
    }

    @Override
    public Iterable<ILocationElement> getElements(final Envelope bound, final IFilter filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterable<ILocationElement> getElementsLocations(final Iterable<IDataElement> dataElements) {
        Iterable<ILocationElement> result = (Iterable<ILocationElement>)elementLocationsIteratorCache.get(dataElements);

        if (result == null) {
            result = new ElementLocationIterator(dataElements).toIterable();

            elementLocationsIteratorCache.put(dataElements, result);
        } else {
            result = new ElementLocationIterator(dataElements).toIterable();
        }

        return result;
    }

    @Override
    protected ILocationElement getLocationElement(final Node node) {
        final SiteElement site = new SiteElement(node);
        site.setNodeType(NetworkElementType.SITE);

        try {
            site.setLatitude((Double)getNodeService().getNodeProperty(node, getGeoNodeProperties().getLatitudeProperty(), null,
                    true));
            site.setLongitude((Double)getNodeService().getNodeProperty(node, getGeoNodeProperties().getLongitudeProperty(), null,
                    true));

            final Iterator<Node> sectorNodes = getNodeService().getChildren(node, NetworkElementType.SECTOR,
                    NodeServiceRelationshipType.CHILD);
            while (sectorNodes.hasNext()) {
                site.addSector(getSectorElement(sectorNodes.next()));
            }

        } catch (final ServiceException e) {
            LOGGER.error("Unable to create a SiteElement from node", e);

            return null;
        }

        return site;
    }

    @Override
    protected INodeType getModelType() {
        return NetworkElementType.NETWORK;
    }

    @Override
    public INodeType[] getNetworkStructure() {
        return structure.toArray(new INodeType[structure.size()]);
    }

    private List<Node> getNodeListFromIndex(final INodeType nodeType, final String propertyName, final Object value)
            throws ModelException {
        final List<Node> result = new ArrayList<Node>();

        CollectionUtils.addAll(result, getIndexModel().getNodes(nodeType, propertyName, value));

        return result;
    }

    @Override
    public int getRenderableElementCount() {
        return getPropertyStatistics().getCount(NetworkElementType.SITE);
    }

    private ISectorElement getSectorElement(final Node node) throws ServiceException {
        final SectorElement element = new SectorElement(node);
        element.setNodeType(NetworkElementType.SECTOR);
        element.setName(getNodeService().getNodeName(node));
        element.setAzimuth((Double)getNodeService().getNodeProperty(node, networkNodeProperties.getAzimuthProperty(), null, false));
        element.setBeamwidth((Double)getNodeService().getNodeProperty(node, networkNodeProperties.getBeamwidthProperty(), null,
                false));

        return element;
    }

    @Override
    public Map<String, String> getSynonyms() {
        return synonyms;
    }

    @Override
    public void initialize(final Node rootNode) throws ModelException {
        super.initialize(rootNode);
        initializeNetworkStructure();
        initializeSynonyms();

    }

    /**
     * @param rootNode
     * @throws NodeTypeNotExistsException
     */
    public void initializeNetworkStructure() {
        structure.clear();
        final String[] structure = (String[])getRootNode().getProperty(networkNodeProperties.getStuctureProperty());
        for (final String element : structure) {
            INodeType type;
            try {
                type = NodeTypeManager.getInstance().getType(element);
            } catch (final NodeTypeNotExistsException e) {
                LOGGER.error("can't find node type " + element, e);
                continue;
            }
            this.structure.add(type);
        }

    }

    /**
     * @param node
     * @throws ModelException
     */
    private void initializeSynonyms() throws ModelException {
        if (synonymNode != null) {
            return;
        }
        Iterator<Node> nodes;
        try {
            nodes = getNodeService().getChildren(getRootNode(), NetworkElementType.SYNONYMS, NetworkRelationshipTypes.SYNONYMS);

            if (nodes.hasNext()) {
                synonymNode = nodes.next();
            } else {
                synonymNode = getNodeService().createNode(getRootNode(), NetworkElementType.SYNONYMS,
                        NetworkRelationshipTypes.SYNONYMS);
            }
        } catch (ServiceException e) {
            processException("can't get children from node " + getRootNode(), e);
        }
        synonyms = new HashMap<String, String>();
        if (synonymNode == null) {
            return;
        }
        for (String key : synonymNode.getPropertyKeys()) {
            if (key.equals(getGeneralNodeProperties().getNodeTypeProperty())) {
                continue;
            }
            synonyms.put(key, (String)synonymNode.getProperty(key));
        }
    }

    @Override
    protected boolean isInAppropriatedProperty(final String propertyName) {
        return !getGeoNodeProperties().getLatitudeProperty().equals(propertyName)
                && !getGeoNodeProperties().getLongitudeProperty().equals(propertyName);
    }

    protected Map<String, Object> removeIgnoredProperties(final Map<String, Object> properties) {
        properties.remove(getGeoNodeProperties().getLatitudeProperty());
        properties.remove(getGeoNodeProperties().getLongitudeProperty());

        return properties;
    }

    @Override
    public IDataElement replaceChild(final IDataElement child, final IDataElement newParent) throws ModelException {
        // TODO Auto-generated method stub
        return null;
    }

    public void setStructure(final List<INodeType> structure) throws ModelException {
        this.structure = structure;
        try {
            getNodeService().updateProperty(getRootNode(), networkNodeProperties.getStuctureProperty(),
                    structureToStringArrayFormat());
        } catch (final ServiceException e) {
            processException("can't setStructure to network", e);
        }

    }

    private String[] structureToStringArrayFormat() {
        final String[] structure = new String[this.structure.size()];
        int i = 0;
        for (final INodeType type : this.structure) {
            structure[i] = type.getId();
            i++;
        }
        return structure;
    }

    @Override
    protected void updateIndexModel(final IDataElement element, final String propertyName, final Object propertyValue)
            throws ModelException {
        if (element.getNodeType().equals(NetworkElementType.SECTOR)) {
            updateSectorIndex(element, propertyName, propertyValue);
        } else if (element.getNodeType().equals(NetworkElementType.SITE)) {
            updateSiteIndex(element, propertyName, propertyValue);
        } else if (getGeneralNodeProperties().getNodeNameProperty().equals(propertyName)) {
            getIndexModel().updateIndex(element.getNodeType(), ((DataElement)element).getNode(),
                    getGeneralNodeProperties().getNodeNameProperty(), element.get(propertyName), propertyValue);
        }

    }

    /**
     * @param parent
     * @param elementType
     * @throws ServiceException
     */
    private void updateNetworkStructure(final IDataElement parent, final INetworkElementType elementType) throws ServiceException {
        INodeType parentType;
        try {
            parentType = getNodeService().getNodeType(((DataElement)parent).getNode());
        } catch (final ServiceException e) {
            throw e;
        } catch (final NodeTypeNotExistsException e) {
            LOGGER.error("can't get parent node type", e);
            return;

        }
        final INodeType currentType = elementType;

        final int parentIndex = structure.indexOf(parentType);
        final int currentIndex = structure.indexOf(currentType);

        if (currentIndex < 0) {
            structure.add(currentType);
            return;
        } else if (currentIndex > 0 && parentIndex >= 0) {
            return;
        }

        final int lastIndex = structure.indexOf(NetworkElementType.SITE);
        if (parentIndex < lastIndex) {
            structure.add(parentIndex, currentType);
        }
    }

    /**
     * @param element
     * @param propertyName
     * @param propertyValue
     * @throws ModelException
     */
    private void updateSectorIndex(final IDataElement element, final String propertyName, final Object propertyValue)
            throws ModelException {
        final DataElement sectorElement = (DataElement)element;
        final Node sectorNode = sectorElement.getNode();
        if (propertyName.equals(networkNodeProperties.getCIProperty())) {
            getIndexModel().updateIndex(NetworkElementType.SECTOR, sectorNode, networkNodeProperties.getCIProperty(),
                    sectorElement.get(networkNodeProperties.getCIProperty()), propertyValue);
        } else if (propertyName.equals(networkNodeProperties.getLACProperty())) {
            getIndexModel().updateIndex(NetworkElementType.SECTOR, sectorNode, networkNodeProperties.getLACProperty(),
                    sectorElement.get(networkNodeProperties.getLACProperty()), propertyValue);
        }

    }

    /**
     * @param element
     * @param propertyName
     * @param propertyValue
     * @throws ModelException
     */
    private void updateSiteIndex(final IDataElement element, final String propertyName, final Object propertyValue)
            throws ModelException {
        final Double lat = (Double)element.get(getGeoNodeProperties().getLatitudeProperty());
        final Double lon = (Double)element.get(getGeoNodeProperties().getLongitudeProperty());
        final Node siteNode = ((DataElement)element).getNode();
        getIndexModel().indexInMultiProperty(NetworkElementType.SITE, siteNode, Double.class,
                getGeoNodeProperties().getLatitudeProperty(), getGeoNodeProperties().getLongitudeProperty());
        if (getGeoNodeProperties().getLatitudeProperty().equals(propertyName)) {
            updateLocation((Double)propertyValue, lon);
        } else if (getGeoNodeProperties().getLongitudeProperty().equals(propertyName)) {
            updateLocation(lat, (Double)propertyValue);
        }

    }

    /**
     * @param elementType
     * @param properties
     * @throws ParameterInconsistencyException
     */
    private void updateSynonyms(final INetworkElementType elementType, final Map<String, Object> properties)
            throws ParameterInconsistencyException {
        if (synonyms == null) {
            return;
        }
        // validate input
        if (elementType == null) {
            throw new ParameterInconsistencyException("elementType");
        }
        if (properties == null) {
            throw new ParameterInconsistencyException("properties", null);
        }
        String typeId = elementType.getId();
        for (String property : properties.keySet()) {
            String key = String.format(SYNONYMS_KEY_FORMAT, typeId, property);
            if (!synonyms.containsKey(key) && !property.equals(getGeneralNodeProperties().getNodeTypeProperty())) {
                if (property.equals(getGeneralNodeProperties().getNodeNameProperty())) {
                    synonyms.put(key, elementType.getId().toUpperCase());
                } else {
                    synonyms.put(key, property);
                }
            }
        }

    }

    @Override
    public void updateSynonyms(final Map<String, Object> synonymnsMap) throws ModelException {
        assert synonymnsMap != null;
        initializeSynonyms();
        for (Entry<String, Object> synonym : synonymnsMap.entrySet()) {
            synonyms.put(synonym.getKey(), (String)synonym.getValue());
        }
    }
}
