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

package org.amanzi.neo.model.distribution.impl;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.refractions.udig.ui.PlatformGIS;

import org.amanzi.neo.model.distribution.IDistribution;
import org.amanzi.neo.model.distribution.IDistributionBar;
import org.amanzi.neo.model.distribution.IDistributionModel;
import org.amanzi.neo.model.distribution.IDistributionalModel;
import org.amanzi.neo.model.distribution.IRange;
import org.amanzi.neo.services.AbstractService;
import org.amanzi.neo.services.DatasetService;
import org.amanzi.neo.services.DistributionService;
import org.amanzi.neo.services.DistributionService.DistributionNodeTypes;
import org.amanzi.neo.services.NeoServiceFactory;
import org.amanzi.neo.services.NodeTypeManager;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.exceptions.DatabaseException;
import org.amanzi.neo.services.model.IDataElement;
import org.amanzi.neo.services.model.impl.AbstractModel;
import org.amanzi.neo.services.model.impl.DataElement;
import org.amanzi.neo.services.model.impl.DriveModel.DriveNodeTypes;
import org.amanzi.neo.services.model.impl.DriveModel.DriveRelationshipTypes;
import org.amanzi.neo.services.utils.Pair;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.geotools.brewer.color.BrewerPalette;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;

/**
 * Model for Distribution
 * 
 * @author lagutko_n
 * @since 1.0.0
 */
public class DistributionModel extends AbstractModel implements IDistributionModel {

    private static final Logger LOGGER = Logger.getLogger(DistributionModel.class);

    /**
     * Default color for Left Bar
     */
    static final Color DEFAULT_LEFT_COLOR = Color.RED;

    /**
     * Default color for Right Bar
     */
    static final Color DEFAULT_RIGHT_COLOR = Color.GREEN;

    /**
     * Default color for Selected Bar
     */
    static final Color DEFAULT_MIDDLE_COLOR = Color.RED;

    /*
     * Distribution Service
     */
    static DistributionService distributionService = NeoServiceFactory.getInstance().getDistributionService();
    static DatasetService datasetService = NeoServiceFactory.getInstance().getDatasetService();
    /*
     * Analyzed Model
     */
    private IDistributionalModel analyzedModel;

    /*
     * Type of Distribution
     */
    private IDistribution< ? > distributionType;

    /*
     * Is Distribution Structure already exists in DB
     */
    private boolean isExist = false;

    /*
     * Number
     */
    private int count;

    /*
     * Color of left Bar from Selected Bar
     */
    private Color leftBarColor = DEFAULT_LEFT_COLOR;

    /*
     * Color of right Bar from Selected Bar
     */
    private Color rightBarColor = DEFAULT_RIGHT_COLOR;

    /*
     * Color of selected Bar
     */
    private Color selectedBarColor = DEFAULT_MIDDLE_COLOR;

    /*
     * Number of Bars
     */
    private int barCount = 0;

    /*
     * Palette of this Distribution
     */
    private BrewerPalette palette;

    /**
     * Returns
     * 
     * @param parentNode
     * @param distributionName
     * @throws DatabaseException
     */
    public DistributionModel(IDistributionalModel analyzedModel, IDistribution< ? > distributionType) throws AWEException {
        super(DistributionNodeTypes.ROOT_AGGREGATION);

        LOGGER.debug("start new DistributionModel()");

        // validate input
        if (analyzedModel == null) {
            LOGGER.error("Analyzed Model cannot be null");
            throw new IllegalArgumentException("Analyzed Model cannot be null");
        }
        if (distributionType == null) {
            LOGGER.error("Distribution Type cannot be null");
            throw new IllegalArgumentException("Distribution Type cannot be null");
        }

        // try to find in DB
        rootNode = distributionService.findRootAggregationNode(analyzedModel.getRootNode(), distributionType);
        if (rootNode == null) {
            LOGGER.info("Creating new Distribution Structure for <" + analyzedModel + ", " + distributionType + ">");

            rootNode = distributionService.createRootAggregationNode(analyzedModel.getRootNode(), distributionType);
        } else {
            isExist = true;
            LOGGER.info("Distribution Found for <" + analyzedModel + ", " + distributionType + ">");
        }

        // initialize other fields
        this.analyzedModel = analyzedModel;
        this.distributionType = distributionType;

        initParameters(rootNode);

        LOGGER.debug("finish new DistributionModel()");
    }

    public DistributionModel(IDistributionalModel analyzedModel, Node rootNode) {
        super(DistributionNodeTypes.ROOT_AGGREGATION);

        LOGGER.debug("start new DistributionModel()");

        // validate input
        if (analyzedModel == null) {
            LOGGER.error("Analyzed Model cannot be null");
            throw new IllegalArgumentException("Analyzed Model cannot be null");
        }
        if (rootNode == null) {
            LOGGER.error("Root Node cannot be null");
            throw new IllegalArgumentException("Root Node cannot be null");
        }

        isExist = true;
        this.rootNode = rootNode;
        this.analyzedModel = analyzedModel;
        initParameters(rootNode);

        LOGGER.debug("finish new DistributionModel()");

    }

    private void initParameters(Node rootNode) {
        this.name = rootNode.getProperty(DistributionService.PROPERTY_NAME) + " - "
                + rootNode.getProperty(DistributionService.NAME);
        this.nodeType = NodeTypeManager.getType(DistributionService.getNodeType(rootNode));
        this.count = (Integer)rootNode.getProperty(DistributionService.COUNT, 0);

        // initialize selection colors
        this.leftBarColor = getColor(rootNode, DistributionService.LEFT_COLOR, DEFAULT_LEFT_COLOR);
        this.rightBarColor = getColor(rootNode, DistributionService.RIGHT_COLOR, DEFAULT_RIGHT_COLOR);
        this.selectedBarColor = getColor(rootNode, DistributionService.SELECTED_COLOR, DEFAULT_MIDDLE_COLOR);

        String paletteName = (String)rootNode.getProperty(DistributionService.PALETTE, null);
        if (!StringUtils.isEmpty(paletteName)) {
            this.palette = PlatformGIS.getColorBrewer().getPalette(paletteName);
        }

    }

    @Override
    public IDistribution< ? > getDistributionType() {
        return distributionType;
    }

    @Override
    public List<IDistributionBar> getDistributionBars() throws AWEException {
        return getDistributionBars(new NullProgressMonitor());
    }

    /**
     * Creates Distirubtion Bar from Node
     * 
     * @param barNode
     * @return
     */
    private DistributionBar createDistributionBar(Node barNode) {
        // create root element
        DataElement rootElement = new DataElement(barNode);
        // create distribution bar
        DistributionBar distributionBar = new DistributionBar(rootElement, this);

        // load properties
        Integer count = (Integer)rootElement.get(DistributionService.COUNT);
        if (count != null) {
            distributionBar.setCount(count);
        }
        Color color = getColor(rootElement, DistributionService.BAR_COLOR);
        if (color != null) {
            distributionBar.setColor(color);
        }
        String name = (String)rootElement.get(AbstractService.NAME);
        distributionBar.setName(name);

        return distributionBar;
    }

    /**
     * Converts Color property from Node to Color Object
     * 
     * @param rootElement bar element
     * @return
     */
    private Color getColor(DataElement rootElement, String colorProperty) {
        return getColor(rootElement.getNode(), colorProperty, null);
    }

    /**
     * Converts Color property from Node to Color object
     * 
     * @param node
     * @param colorProperty
     * @return
     */
    private Color getColor(Node node, String colorProperty, Color defaultColor) {
        int[] colorArray = (int[])node.getProperty(colorProperty, null);

        if (colorArray != null) {
            return new Color(colorArray[0], colorArray[1], colorArray[2]);
        } else {
            return defaultColor;
        }
    }

    /**
     * Creates Distribution Database structure
     */
    private List<IDistributionBar> createDistribution(IProgressMonitor monitor) throws AWEException {
        int totalElementsCount = 0;
        for (Object property : analyzedModel.getAllProperties(distributionType.getNodeType(), distributionType.getPropertyName())) {
            totalElementsCount += analyzedModel.getPropertyValueCount(distributionType.getNodeType(),
                    distributionType.getPropertyName(), property);
        }
        monitor.beginTask("Creating Distribution <" + getName() + "> in Database", totalElementsCount);
        distributionType.init();
        List<Pair<IRange, DistributionBar>> distributionConditions = createDistributionBars();
        try {

            // iterate through all analysed data
            for (IDataElement element : analyzedModel.getAllElementsByType(distributionType.getNodeType())) {
                // iterate through conditions
                for (Pair<IRange, DistributionBar> condition : distributionConditions) {
                    IRange range = condition.getLeft();

                    // check element
                    if (range.getFilter().check(element)) {
                        DistributionBar bar = condition.getRight();
                        Node barNode = ((DataElement)bar.getRootElement()).getNode();
                        Node sourceNode = ((DataElement)element).getNode();

                        // create aggregation link
                        distributionService.createAggregation(barNode, sourceNode);

                        // increase count in bar
                        bar.setCount(bar.getCount() + 1);
                    }
                }
                monitor.worked(1);

            }
        } catch (Exception e) {
            LOGGER.error("Exception on creating aggregation links", e);
            throw new DatabaseException(e);
        }

        monitor.done();

        // create result
        List<IDistributionBar> result = new ArrayList<IDistributionBar>();
        for (Pair<IRange, DistributionBar> condition : distributionConditions) {
            IDistributionBar distributionBar = condition.getRight();

            // update properties in DB
            distributionService.updateDistributionBar(getRootNode(), distributionBar);

            // add to result
            result.add(distributionBar);
        }

        return result;
    }

    /**
     * Creates list of Distribution Bars from Ranges
     * 
     * @return
     */
    private List<Pair<IRange, DistributionBar>> createDistributionBars() throws AWEException {
        ArrayList<Pair<IRange, DistributionBar>> result = new ArrayList<Pair<IRange, DistributionBar>>();

        for (IRange range : distributionType.getRanges()) {
            DistributionBar bar = createDistributionBar(range);

            result.add(new Pair<IRange, DistributionBar>(range, bar));
        }

        // update count of bars for model
        distributionService.updateDistributionModelCount(getRootNode(), result.size());

        return result;
    }

    /**
     * Creates single Distribution
     * 
     * @param range
     * @return
     */
    private DistributionBar createDistributionBar(IRange range) throws AWEException {
        DistributionBar distributionBar = new DistributionBar(this);
        if (range.getColor() != null) {
            distributionBar.setColor(range.getColor());
        }

        distributionBar.setName(range.getName());

        Node barNode = distributionService.createAggregationBarNode(getRootNode(), distributionBar);

        distributionBar.setRootElement(new DataElement(barNode));

        return distributionBar;
    }

    /**
     * Loads Distribution from Database
     * 
     * @return
     */
    private List<IDistributionBar> loadDistribution(IProgressMonitor monitor) {
        List<IDistributionBar> distributionBars = new ArrayList<IDistributionBar>();

        monitor.beginTask("Loading Distribution <" + getName() + "> from Database", count);

        for (Node distributionBarNode : distributionService.findAggregationBars(getRootNode())) {
            distributionBars.add(createDistributionBar(distributionBarNode));
            monitor.worked(1);
        }

        monitor.done();

        LOGGER.info("Loaded <" + distributionBars.size() + "> distribution bars from Database");

        return distributionBars;
    }

    @Override
    public void updateBar(IDistributionBar bar) throws AWEException {
        LOGGER.debug("start updateBar(<" + bar + ">)");

        // check input
        if (bar == null) {
            LOGGER.error("Input bar cannot be null");
            throw new IllegalArgumentException("Input bar cannot be null");
        }
        if (StringUtils.isEmpty(bar.getName())) {
            LOGGER.error("New name of Bar cannot be null or empty");
            throw new IllegalArgumentException("New name of Bar cannot be null or empty");
        }
        if (bar.getCount() < 0) {
            LOGGER.error("New count of Bar cannot be less than zero");
            throw new IllegalArgumentException("New count of Bar cannot be less than zero");
        }
        if (bar.getRootElement() == null) {
            LOGGER.error("New rootElement of Bar cannot be null");
            throw new IllegalArgumentException("New rootElement of Bar cannot be null");
        }

        // update bar
        distributionService.updateDistributionBar(getRootNode(), bar);

        LOGGER.debug("finish updateBar()");
    }

    @Override
    public List<IDistributionBar> getDistributionBars(IProgressMonitor monitor) throws AWEException {
        LOGGER.debug("start getDistributionBars()");

        List<IDistributionBar> result = null;

        if (!isExist) {
            LOGGER.info("No Distribution <" + getName() + ">. Create new one");
            result = createDistribution(monitor);
            isExist = true;
        } else {
            LOGGER.info("Load Distribution <" + getName() + "> from Database");
            result = loadDistribution(monitor);
        }

        LOGGER.debug("finish getDistributionBars()");

        barCount = result.size();

        return result;
    }

    @Override
    public void setCurrent(boolean isCurrent) throws AWEException {
        LOGGER.debug("start setCurrent(<" + isCurrent + ">)");

        Node rootAggrNode = getRootNode();
        if (isCurrent) {
            LOGGER.info("Enabling Distribution <" + getName() + "> for Model <" + analyzedModel.getName() + ">");
        } else {
            LOGGER.info("Disabling Distribution <" + getName() + "> for Model <" + analyzedModel.getName() + ">");
            rootAggrNode = null;
        }

        distributionService.setCurrentDistributionModel(analyzedModel.getRootNode(), rootAggrNode);

        LOGGER.debug("finish setCurrent()");
    }

    @Override
    public Color getRightColor() {
        return rightBarColor;
    }

    @Override
    public void setRightColor(Color rightBarColor) {
        if (rightBarColor == null) {
            LOGGER.error("Cannot set null for rightBarColor");
            throw new IllegalArgumentException("Cannot set null for rightBarColor");
        }

        this.rightBarColor = rightBarColor;
    }

    @Override
    public Color getLeftColor() {
        return leftBarColor;
    }

    @Override
    public void setLeftColor(Color leftBarColor) {
        if (leftBarColor == null) {
            LOGGER.error("Cannot set null for leftBarColor");
            throw new IllegalArgumentException("Cannot set null for leftBarColor");
        }

        this.leftBarColor = leftBarColor;
    }

    @Override
    public Color getMiddleColor() {
        return selectedBarColor;
    }

    @Override
    public void setMiddleColor(Color selectedBarColor) {
        if (selectedBarColor == null) {
            LOGGER.error("Cannot set null for selectedBarColor");
            throw new IllegalArgumentException("Cannot set null for selectedBarColor");
        }

        this.selectedBarColor = selectedBarColor;
    }

    @Override
    public void finishUp() throws AWEException {
        distributionService.updateSelectedBarColors(rootNode, leftBarColor, rightBarColor, selectedBarColor);
        if (palette != null) {
            distributionService.updatePalette(rootNode, palette);
        }

        super.finishUp();
    }

    @Override
    public int getBarCount() {
        return barCount;
    }

    @Override
    public BrewerPalette getPalette() {
        return palette;
    }

    @Override
    public void setPalette(BrewerPalette palette) {
        this.palette = palette;
    }

    @Override
    public IDistributionalModel getAnalyzedModel() {
        return analyzedModel;
    }

    @Override
    public Color getBarColorForAggregatedElement(IDataElement element) {
        if (element == null) {
            LOGGER.error("<getBarForAggregatedElement(" + element + ")>e lement cannot be null");
            throw new IllegalArgumentException("<getBarForAggregatedElement(" + element + ")>e lement cannot be null");
        }
        Node elementNode;
        if (NodeTypeManager.getType(element).equals(DriveNodeTypes.MP)) {
            elementNode = datasetService
                    .getFirstRelationTraverser(((DataElement)element).getNode(), DriveRelationshipTypes.LOCATION,
                            Direction.INCOMING).iterator().next();
        } else {
            elementNode = ((DataElement)element).getNode();
        }
        Node foundBar = distributionService.findBarByAggregatedNode(elementNode, rootNode.getId());
        if (foundBar == null) {
            LOGGER.error("<getBarForAggregatedElement(" + element + ")>e lement cannot get distribution bars");
            return null;
        }
        Color color = getColor(new DataElement(foundBar), DistributionService.BAR_COLOR);
        return color;
    }
}