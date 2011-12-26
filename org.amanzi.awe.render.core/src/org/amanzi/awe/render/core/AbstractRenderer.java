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

package org.amanzi.awe.render.core;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.IOException;

import net.refractions.udig.catalog.IGeoResource;
import net.refractions.udig.project.ILayer;
import net.refractions.udig.project.internal.render.impl.RendererImpl;
import net.refractions.udig.project.render.RenderException;

import org.amanzi.awe.console.AweConsolePlugin;
import org.amanzi.awe.models.catalog.neo.GeoResource;
import org.amanzi.awe.neostyle.BaseNeoStyle;
import org.amanzi.neo.model.distribution.IDistributionBar;
import org.amanzi.neo.model.distribution.IDistributionalModel;
import org.amanzi.neo.model.distribution.impl.DistributionManager;
import org.amanzi.neo.model.distribution.impl.DistributionModel;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.model.IDataElement;
import org.amanzi.neo.services.model.IRenderableModel;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author grigoreva_a
 * @since 1.0.0
 */
public abstract class AbstractRenderer extends RendererImpl {
    private static Logger LOGGER = Logger.getLogger(AbstractRenderer.class);
    protected static BaseNeoStyle style;
    protected IRenderableModel model;
    private MathTransform transform_d2w;
    private MathTransform transform_w2d;
    protected DistributionManager distributionManager = DistributionManager.getManager();
    protected DistributionModel currentDistributionModel = null;
    private AbstractRendererStyles commonStyle = initDefaultRendererStyle();

    protected abstract AbstractRendererStyles initDefaultRendererStyle();

    public void setScaling(Envelope bounds_transformed, Envelope data_bounds, final IProgressMonitor monitor, long count) {
        double dataScaled = (bounds_transformed.getHeight() * bounds_transformed.getWidth())
                / (data_bounds.getHeight() * data_bounds.getWidth());

        double countScaled = dataScaled * count / 2;
        setDrawLabel(countScaled);
        if (countScaled < commonStyle.getMaxElementsFull()) {
            commonStyle.setScale(Scale.LARGE);
        } else if (countScaled > commonStyle.getMaxElementsLite()) {
            commonStyle.setScale(Scale.SMALL);
        } else {
            commonStyle.setScale(Scale.MEDIUM);
        }
        if (commonStyle.getScale().equals(Scale.LARGE) && commonStyle.isScaleSymbols()) {
            int largeSectorsSize = commonStyle.getLargeElementSize();
            largeSectorsSize *= Math.sqrt(commonStyle.getMaxElementsFull()) / (3 * Math.sqrt(countScaled));
            largeSectorsSize = Math.min(largeSectorsSize, commonStyle.getMaxSymbolSize());
            commonStyle.setLargeElementSize(largeSectorsSize);
        }
        bounds_transformed.expandBy(0.75 * (bounds_transformed.getHeight() + bounds_transformed.getWidth()));
    }

    /**
     * set requirement to draw labels
     * 
     * @param countScaled
     */
    protected abstract void setDrawLabel(double countScaled);

    public static final String BLACKBOARD_NODE_LIST = "org.amanzi.awe.tool.star.StarTool.nodes";

    @Override
    public void render(Graphics2D destination, IProgressMonitor monitor) throws RenderException {
        ILayer layer = getContext().getLayer();
        IGeoResource resource = layer.findGeoResource(GeoResource.class);
        // c+v
        layer.getMap().getBlackboard().get(BLACKBOARD_NODE_LIST);
        if (resource != null) {
            try {
                renderGeoResource(destination, resource, monitor);
            } catch (AWEException e) {
                LOGGER.error("Could not render resource.", e);
            }
        }
    }

    /**
     * @param destination
     * @param resource
     * @param monitor
     * @throws AWEException
     */
    private void renderGeoResource(Graphics2D destination, IGeoResource resource, IProgressMonitor monitor) throws RenderException,
            AWEException {

        if (monitor == null) {
            monitor = new NullProgressMonitor();
        }
        // TODO: Get size from info (???)
        monitor.beginTask("render network sites and sectors: " + resource.getIdentifier(), IProgressMonitor.UNKNOWN);

        try {

            setStyle(destination);
            // find a resource to render
            model = resource.resolve(getResolvedClass(), monitor);
            initCurrentDistribution();
            // get rendering bounds and zoom
            setCrsTransforms(resource.getInfo(null).getCRS());
            Envelope bounds_transformed = getTransformedBounds();
            Envelope data_bounds = model.getBounds();
            Long count;
            if (bounds_transformed == null) {
                commonStyle.setScale(Scale.MEDIUM);
            } else if (data_bounds != null && data_bounds.getHeight() > 0 && data_bounds.getWidth() > 0) {
                count = getRenderableElementCount(model);
                setScaling(bounds_transformed, data_bounds, monitor, count);
            }
            // TODO: selection

            renderElements(destination, bounds_transformed, data_bounds, monitor);

        } catch (IOException e) {
            LOGGER.error("Could not relosve resource.", e);
            throw new RenderException(e);
        } catch (TransformException e) {
            LOGGER.error("Could not transform bounds.", e);
            throw new RenderException(e);
        } catch (FactoryException e) {
            LOGGER.error("Could not set CRS transforms.", e);
            throw new RenderException(e);
        }
    }

    /**
     * render elements from current model
     * 
     * @param destination
     * @throws TransformException
     * @throws AWEException
     * @throws NoninvertibleTransformException
     */
    private void renderElements(Graphics2D destination, Envelope bounds_transformed, Envelope data_bounds, IProgressMonitor monitor)
            throws NoninvertibleTransformException, AWEException, TransformException {
        for (IDataElement element : model.getElements(data_bounds)) {
            Coordinate location = model.getCoordinate(element);
            if (location == null) {
                continue;
            }
            Coordinate world_location = new Coordinate();
            if (bounds_transformed != null && !bounds_transformed.contains(location)) {
                continue; // Don't draw points outside viewport
            }
            try {
                JTS.transform(location, world_location, transform_d2w);
            } catch (Exception e) {
                JTS.transform(location, world_location, transform_w2d.inverse());
            }

            java.awt.Point p = getContext().worldToPixel(world_location);

            renderElement(destination, p, element, model);

            monitor.worked(1);
            // count++;
            if (monitor.isCanceled())
                break;
        }
    }

    /**
     * return renderable element count
     * 
     * @param model2
     */
    protected abstract long getRenderableElementCount(IRenderableModel model);

    /**
     * render element based on latitude and longitude values;
     * 
     * @param destination
     * @param point
     * @param element
     */
    protected abstract void renderCoordinateElement(Graphics2D destination, Point point, IDataElement element);

    /**
     * generate required shape element
     * 
     * @param shape -shape of feature element
     * @param destination
     * @param size -current s
     * @param isFill- is need to feel shape
     */
    protected void drawElement(RenderShape shape, Graphics2D destination, Point point, IDataElement element, boolean isFill) {
        int size = getSize();
        int x = point.x - size;
        int y = point.y - size;
        Color color = commonStyle.changeColor(getColor(element), commonStyle.getAlpha());
        switch (shape) {
        case OVAL:
            drawOval(destination, isFill, x, y, size, color);
            break;
        case RECTUNGLE:
            drawRect(destination, isFill, x, y, size, color);
        default:
            break;
        }
    }

    /**
     * return default element size depends of scale
     * 
     * @return
     */
    private int getSize() {
        switch (commonStyle.getScale()) {
        case MEDIUM:
            return commonStyle.getMediumElementSize() / 2;
        case LARGE:
            return commonStyle.getLargeElementSize() / 2;
        default:
            break;
        }
        return 1;
    }

    /**
     * draw oval
     * 
     * @param destination
     * @param isFill
     * @param x
     * @param y
     * @param size
     * @param color
     */
    protected void drawOval(Graphics2D destination, boolean isFill, int x, int y, int size, Color color) {
        destination.setColor(commonStyle.getBorderColor());
        destination.drawOval(x, y, size, size);
        if (isFill) {
            destination.setColor(color);
            destination.fillOval(x, y, size, size);
        }

    }

    /**
     * draw rectungle
     * 
     * @param destination
     * @param isFill
     * @param x
     * @param y
     * @param size
     * @param color
     */
    protected void drawRect(Graphics2D destination, boolean isFill, int x, int y, int size, Color color) {
        destination.setColor(commonStyle.getBorderColor());
        destination.drawRect(x, y, size, size);
        if (isFill) {
            destination.setColor(color);
            destination.fillRect(x, y, size, size);
        }
    }

    /**
     * get color from distribution ... if distribution not exist then return default border color
     * 
     * @param element
     * @return
     */
    protected Color getColor(IDataElement element) {
        if (currentDistributionModel == null) {
            return getDefaultColor(element);
        }
        IDistributionBar bar = currentDistributionModel.getBarForAggregatedElement(element);
        if (bar == null || bar.getColor() == null) {
            LOGGER.info(" <getColor(IDataElement element)> Cann't find bar of distribution " + currentDistributionModel + " for "
                    + element + " element");
            return getDefaultColor(element);
        }
        return bar.getColor();
    }

    /**
     * return default color
     * 
     * @param element
     * @return
     */
    protected Color getDefaultColor(IDataElement element) {
        switch (commonStyle.getScale()) {
        case MEDIUM:
        case SMALL:
            return commonStyle.getBorderColor();
        case LARGE:
            return getDefaultFillColorByElement(element);
        default:
            return null;
        }
    }

    /**
     * return default fill color for element;
     */
    protected abstract Color getDefaultFillColorByElement(IDataElement element);

    protected void initCurrentDistribution() {
        if (model instanceof IDistributionalModel) {
            currentDistributionModel = distributionManager.getCurrent((IDistributionalModel)model);
        }
    }

    /**
     * set default style to destination
     */
    protected void setStyle(Graphics2D destination) {
        if (commonStyle.isAntialiazing()) {
            destination.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            destination.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }
    }

    /**
     * @param destination
     * @param point
     * @param element
     */
    protected void renderElement(Graphics2D destination, Point point, IDataElement element, IRenderableModel model) {
        destination.setColor(Color.BLACK);
        destination.drawOval(point.x - 1, point.y - 1, 2, 2);
    }

    @Override
    public void render(IProgressMonitor monitor) throws RenderException {
        Graphics2D g = getContext().getImage().createGraphics();
        render(g, monitor);
    }

    private void setCrsTransforms(CoordinateReferenceSystem dataCrs) throws FactoryException {
        boolean lenient = true; // needs to be lenient to work on uDIG 1.1
                                // (otherwise we get error:
                                // bursa wolf parameters required
        CoordinateReferenceSystem worldCrs = context.getCRS();
        this.transform_d2w = CRS.findMathTransform(dataCrs, worldCrs, lenient);
        this.transform_w2d = CRS.findMathTransform(worldCrs, dataCrs, lenient); // could
                                                                                // use
                                                                                // transform_d2w.inverse()
                                                                                // also
    }

    private Envelope getTransformedBounds() throws TransformException {
        ReferencedEnvelope bounds = getRenderBounds();
        if (bounds == null) {
            bounds = this.context.getViewportModel().getBounds();
        }
        Envelope bounds_transformed = null;
        if (bounds != null && transform_w2d != null) {
            bounds_transformed = JTS.transform(bounds, transform_w2d);
        }
        return bounds_transformed;
    }

    /**
     * gets average count of geoNeo.getCount() from all resources in map
     * 
     * @param data_bounds
     * @return average count
     */
    protected double getAverageDensity(IProgressMonitor monitor) {
        double result = 0;
        long count = 0;
        try {
            for (ILayer layer : getContext().getMap().getMapLayers()) {
                Class< ? extends IRenderableModel> classToResolve = getResolvedClass();
                if (layer.getGeoResource().canResolve(classToResolve)) {
                    IRenderableModel resource = layer.getGeoResource().resolve(classToResolve, monitor);
                    Envelope dbounds = resource.getBounds();
                    if (dbounds != null) {
                        result += calculateResult(dbounds, resource);
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while try to return average density, return 0", e);
            AweConsolePlugin.error("Error while try to return average density, return 0 ");
            return 0;
        }
        return result / (double)count;
    }

    /**
     * return class which can be resolved with georesource
     * 
     * @return
     */
    protected abstract Class< ? extends IRenderableModel> getResolvedClass();

    /**
     * calculate average between necessary nodes count and size
     * 
     * @param dbounds
     * @param resource
     * @return
     */
    protected double calculateResult(Envelope dbounds, IRenderableModel resource) {
        return 0d;
    }
}