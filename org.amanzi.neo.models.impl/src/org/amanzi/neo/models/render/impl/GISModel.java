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

package org.amanzi.neo.models.render.impl;

import org.amanzi.neo.dto.IDataElement;
import org.amanzi.neo.models.exceptions.ModelException;
import org.amanzi.neo.models.impl.internal.AbstractNamedModel;
import org.amanzi.neo.models.impl.internal.util.CRSWrapper;
import org.amanzi.neo.models.render.IGISModel;
import org.amanzi.neo.models.render.IRenderableModel;
import org.amanzi.neo.nodeproperties.IGeneralNodeProperties;
import org.amanzi.neo.nodeproperties.IGeoNodeProperties;
import org.amanzi.neo.nodetypes.INodeType;
import org.amanzi.neo.services.INodeService;
import org.amanzi.neo.services.exceptions.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.neo4j.graphdb.Node;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Envelope;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class GISModel extends AbstractNamedModel implements IGISModel {

    private static final Logger LOGGER = Logger.getLogger(GISModel.class);

    private double minLatitude = Double.MAX_VALUE;;

    private double maxLatitude = -Double.MAX_VALUE;

    private double minLongitude = Double.MAX_VALUE;

    private double maxLongitude = -Double.MAX_VALUE;

    private final IGeoNodeProperties geoNodeProperties;

    private IRenderableModel sourceModel;

    private CoordinateReferenceSystem crs;

    private String crsCode;

    private boolean updateCoordinates = false;

    /**
     * @param nodeService
     * @param generalNodeProperties
     */
    public GISModel(final INodeService nodeService, final IGeneralNodeProperties generalNodeProperties,
            final IGeoNodeProperties geoNodeProperites) {
        super(nodeService, generalNodeProperties);

        geoNodeProperties = geoNodeProperites;
    }

    @Override
    public boolean canRender() {
        return sourceModel.getRenderableElementCount() > 0;
    }

    @Override
    public boolean canResolve(final Class< ? > clazz) {
        return clazz.isAssignableFrom(sourceModel.getClass());
    }

    @Override
    protected Node createNode(final Node parentNode, final INodeType nodeType, final String name) throws ServiceException {
        return getNodeService().createNode(parentNode, nodeType, GISRelationType.GIS, name);
    }

    @Override
    public void finishUp() throws ModelException {
        LOGGER.info("Finishing up model <" + getName() + ">");
        try {
            if (updateCoordinates) {
                getNodeService().updateProperty(getRootNode(), geoNodeProperties.getMaxLatitudeProperty(), maxLatitude);
                getNodeService().updateProperty(getRootNode(), geoNodeProperties.getMaxLongitudeProperty(), maxLongitude);
                getNodeService().updateProperty(getRootNode(), geoNodeProperties.getMinLatitudeProperty(), minLatitude);
                getNodeService().updateProperty(getRootNode(), geoNodeProperties.getMinLongitudeProperty(), minLongitude);
                getNodeService().updateProperty(getRootNode(), geoNodeProperties.getCRSProperty(), getCRSCode());
            }
        } catch (final ServiceException e) {
            processException("Error on updating GIS Model", e);
        }
    }

    @Override
    public Iterable<IDataElement> getAllElementsByType(final INodeType nodeType) throws ModelException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReferencedEnvelope getBounds() {
        final double updatedMinLongitude = minLongitude - (maxLongitude - minLongitude) * 0.05;
        final double updatedMaxLongitude = maxLongitude + (maxLongitude - minLongitude) * 0.05;
        final double updatedMinLatitude = minLatitude - (maxLatitude - minLatitude) * 0.05;
        final double updatedMaxLatitude = maxLatitude + (maxLatitude - minLatitude) * 0.05;
        return new ReferencedEnvelope(updatedMinLongitude, updatedMaxLongitude, updatedMinLatitude, updatedMaxLatitude, crs);
    }

    @Override
    public int getCount() {
        return sourceModel.getRenderableElementCount();
    }

    @Override
    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    protected String getCRSCode() {
        return crsCode;
    }

    @Override
    public Iterable<ILocationElement> getElements(final Envelope bound) throws ModelException {
        return sourceModel.getElements(bound);
    }

    /**
     * @return Returns the maxLatitude.
     */
    @Override
    public double getMaxLatitude() {
        return maxLatitude;
    }

    /**
     * @return Returns the maxLongitude.
     */
    @Override
    public double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * @return Returns the minLatitude.
     */
    @Override
    public double getMinLatitude() {
        return minLatitude;
    }

    /**
     * @return Returns the minLongitude.
     */
    @Override
    public double getMinLongitude() {
        return minLongitude;
    }

    @Override
    protected INodeType getModelType() {
        return GISNodeType.GIS;
    }

    @Override
    protected Node getParent(final Node rootNode) throws ServiceException {
        return getNodeService().getParent(rootNode, GISRelationType.GIS);
    }

    @Override
    public IRenderableModel getSourceModel() {
        return sourceModel;
    }

    @Override
    public INodeType getSourceType() {
        return sourceModel.getType();
    }

    @Override
    public INodeType getType() {
        return GISNodeType.GIS;
    }

    @Override
    public void initialize(final Node rootNode) throws ModelException {
        super.initialize(rootNode);

        try {
            maxLatitude = getNodeService().getNodeProperty(rootNode, geoNodeProperties.getMaxLatitudeProperty(), -Double.MAX_VALUE,
                    false);
            minLatitude = getNodeService().getNodeProperty(rootNode, geoNodeProperties.getMinLatitudeProperty(), Double.MAX_VALUE,
                    false);
            maxLongitude = getNodeService().getNodeProperty(rootNode, geoNodeProperties.getMaxLongitudeProperty(),
                    -Double.MAX_VALUE, false);
            minLongitude = getNodeService().getNodeProperty(rootNode, geoNodeProperties.getMinLongitudeProperty(),
                    Double.MAX_VALUE, false);

            final String crsCodeValue = getNodeService().getNodeProperty(rootNode, geoNodeProperties.getCRSProperty(),
                    StringUtils.EMPTY, false);
            setCRS(crsCodeValue);

            updateCoordinates = false;
        } catch (final ServiceException e) {
            processException("Cannot get GIS-related properties from Node", e);
        }
    }

    /**
     * @param crsCode The crsCode to set.
     */
    public void setCRS(final CoordinateReferenceSystem crs) {
        this.crs = crs;
        this.crsCode = CRSWrapper.fromCRS(crs).getEpsg();
    }

    protected void setCRS(final String crsCode) {
        if (!StringUtils.isEmpty(crsCode)) {
            try {
                this.crsCode = crsCode;
                crs = CRS.decode(crsCode);
            } catch (final FactoryException e) {
                LOGGER.error("Cannot determinate CRS", e);
            }
        }
    }

    @Override
    public void setSourceModel(final IRenderableModel sourceModel) {
        this.sourceModel = sourceModel;
    }

    @Override
    public void updateBounds(final double latitude, final double longitude) {
        minLatitude = Math.min(latitude, minLatitude);
        maxLatitude = Math.max(latitude, maxLatitude);
        minLongitude = Math.min(longitude, minLongitude);
        maxLongitude = Math.max(longitude, maxLongitude);

        if (getCRS() == null) {
            final CRSWrapper wrapper = CRSWrapper.fromLocation(latitude, longitude, getName());
            setCRS(wrapper.getEpsg());
        }

        updateCoordinates = true;
    }
}
