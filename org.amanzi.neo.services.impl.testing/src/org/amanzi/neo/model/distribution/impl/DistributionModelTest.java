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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import net.refractions.udig.ui.PlatformGIS;

import org.amanzi.log4j.LogStarter;
import org.amanzi.neo.model.distribution.IDistribution;
import org.amanzi.neo.model.distribution.IDistributionBar;
import org.amanzi.neo.model.distribution.IDistributionalModel;
import org.amanzi.neo.model.distribution.IRange;
import org.amanzi.neo.services.AbstractNeoServiceTest;
import org.amanzi.neo.services.AbstractService;
import org.amanzi.neo.services.DatasetService;
import org.amanzi.neo.services.DistributionService;
import org.amanzi.neo.services.NeoServiceFactory;
import org.amanzi.neo.services.DistributionService.DistributionNodeTypes;
import org.amanzi.neo.services.NetworkService.NetworkElementNodeType;
import org.amanzi.neo.services.enums.INodeType;
import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.filters.Filter;
import org.amanzi.neo.services.model.IDataElement;
import org.amanzi.neo.services.model.impl.DataElement;
import org.amanzi.neo.services.model.impl.DriveModel.DriveNodeTypes;
import org.amanzi.neo.services.model.impl.DriveModel.DriveRelationshipTypes;
import org.apache.commons.lang3.StringUtils;
import org.geotools.brewer.color.BrewerPalette;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PropertyContainer;

/**
 * Tests on Distribution Model
 * 
 * @author lagutko_n
 * @since 1.0.0
 */
public class DistributionModelTest extends AbstractNeoServiceTest {

	private static final String DEFAULT_DISTRIBUTION_NAME = "distribution";

	private static final String DEFAULT_MODEL_NAME = "mocked network";

	private static final int NUMBER_OF_DISTRIBUTION_BARS = 5;

	private static final String DISTRIBUTION_BAR_NAME_PREFIX = "distribution_bar_";

	private static final Color[] DISTRIBUTION_BAR_COLORS = new Color[] {
			Color.BLACK, Color.WHITE, Color.RED, null, Color.CYAN };

	private static final INodeType DISTRIBUTION_NODE_TYPE = NetworkElementNodeType.SECTOR;

	private static final int NUMBER_OF_NODES_TO_ANALYSE = 40;

	private static final int[] BAR_COUNT = new int[] { 10, 9, 8, 7, 6 };

	private static final String DISTRIBUTION_PROPERTY_NAME = "property";

	private static final int PALETTE_INDEX = PlatformGIS.getColorBrewer()
			.getPalettes().length / 2;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clearDb();
		initializeDb();
		new LogStarter().earlyStartup();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		stopDb();
		clearDb();
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToCreatesDistributionWithoutAnalyzedModel() throws Exception {
		new DistributionModel(null, getDistributionType());
		new DistributionModel(null, getNode());
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToCreateDistributionWithoutDistributionType()
			throws Exception {
		IDistributionalModel model = getDistributionalModel();

		new DistributionModel(model, (IDistribution<?>) null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToCreateDistributionWithoutRootNode() throws Exception {
		IDistributionalModel model = getDistributionalModel();

		new DistributionModel(model, (Node) null);
	}

	@Test
	public void checkActionsWithExistingDistributionModel() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		IDistribution<?> distribution = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distribution);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		new DistributionModel(model, distribution);

		verify(service).findRootAggregationNode(parentDistribution,
				distribution);
		verify(service, never()).createRootAggregationNode(any(Node.class),
				any(IDistribution.class));
	}

	@Test
	public void checkPropertiesForExistingDistributionModel() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(true);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		assertEquals("Incorrect name of Distribution",
				DISTRIBUTION_PROPERTY_NAME + " - " + DEFAULT_DISTRIBUTION_NAME,
				distribution.getName());
		assertNotNull("Root Node cannot be null", distribution.getRootNode());
		assertEquals("Incorrect distribution type", distributionType,
				distribution.getDistributionType());
		assertEquals("Incorrect Type of Distribution",
				DistributionNodeTypes.ROOT_AGGREGATION, distribution.getType());

		BrewerPalette palette = PlatformGIS.getColorBrewer().getPalettes()[PALETTE_INDEX];
		assertEquals("Incorrect Palette for Distribution", palette,
				distribution.getPalette());
	}

	@Test
	public void checkActionsWithNewDistributionModel() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		IDistribution<?> distribution = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				false, distribution);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		new DistributionModel(model, distribution);

		verify(service).findRootAggregationNode(parentDistribution,
				distribution);
		verify(service).createRootAggregationNode(parentDistribution,
				distribution);
	}

	@Test
	public void checkPropertiesForNewDistributionModel() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(false);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		assertEquals("Incorrect name of Distribution",
				DISTRIBUTION_PROPERTY_NAME + " - " + DEFAULT_DISTRIBUTION_NAME,
				distribution.getName());
		assertNotNull("Root Node cannot be null", distribution.getRootNode());
		assertEquals("Incorrect distribution type", distributionType,
				distribution.getDistributionType());
		assertEquals("Incorrect Type of Distribution",
				DistributionNodeTypes.ROOT_AGGREGATION, distribution.getType());
		assertNull("Incorrect initial Palette", distribution.getPalette());
	}

	@Test
	public void checkNoWorkWithAnalyzedModelIfDistriubtionAlreadyExist()
			throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		distribution.getDistributionBars();

		verify(model).getRootNode();
		verifyNoMoreInteractions(model);
	}

	@Test
	public void checkServiceActionsWithExistingDatabase() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.getDistributionBars();

		verify(service).findAggregationBars(rootAggregation);

		verify(service, never()).createAggregationBarNode(any(Node.class),
				any(IDistributionBar.class));
	}

	@Test
	public void checkDistributionBarsSizeWithExistingDatabase()
			throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> distributionBars = distribution
				.getDistributionBars();

		assertNotNull("List of distribution bars cannot be null",
				distributionBars);
		assertEquals("Unexpected size of Bars", NUMBER_OF_DISTRIBUTION_BARS,
				distributionBars.size());

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			assertNotNull("Distribution bar cannot be null",
					distributionBars.get(i));
		}
	}

	@Test
	public void checkRootOfDistributionBarsExistingDatabase() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		List<IDistributionBar> distributionBars = distribution
				.getDistributionBars();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			IDataElement rootElement = distributionBars.get(i).getRootElement();
			Node rootNode = ((DataElement) rootElement).getNode();

			assertEquals("Incorrect Root node of Distribution bar",
					distributionBarNodes.get(i), rootNode);
		}
	}

	@Test
	public void checkPropertiesOfDistributionBarsExistingDatabase()
			throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> distributionBars = distribution
				.getDistributionBars();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			IDistributionBar distributionBar = distributionBars.get(i);

			Color color = DISTRIBUTION_BAR_COLORS[i];
			if (color == null) {
				color = DistributionBar.DEFUALT_COLOR;
			}

			assertEquals("Incorrect Color of Bar", color,
					distributionBar.getColor());
			assertEquals("Incorrect count of Bar", i,
					distributionBar.getCount());
			assertEquals("Incorrect Name of Bar", DISTRIBUTION_BAR_NAME_PREFIX
					+ i, distributionBar.getName());
		}
	}

	@Test
	public void checkNumberOfCreatedDistributionBars() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		List<IDistributionBar> distributionBars = distribution
				.getDistributionBars();

		assertNotNull("List of Bars cannot be null", distributionBars);
		assertEquals("Incorrect size of Bars", NUMBER_OF_DISTRIBUTION_BARS,
				distributionBars.size());

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			assertNotNull("Distribution bar cannot be null",
					distributionBars.get(i));
		}
	}

	@Test
	public void checkPropertiesOfCreatedBars() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> distributionBars = distribution
				.getDistributionBars();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			IDistributionBar distributionBar = distributionBars.get(i);

			Color color = DISTRIBUTION_BAR_COLORS[i];
			if (color == null) {
				color = DistributionBar.DEFUALT_COLOR;
			}

			assertEquals("Incorrect Color of Bar", color,
					distributionBar.getColor());
			assertEquals("Incorrect Name of Bar", DISTRIBUTION_BAR_NAME_PREFIX
					+ i, distributionBar.getName());
		}
	}

	@Test
	public void checkCreateActionOfService() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.getDistributionBars();

		verify(service, atLeast(NUMBER_OF_DISTRIBUTION_BARS))
				.createAggregationBarNode(eq(distribution.getRootNode()),
						any(IDistributionBar.class));

		verify(service, never()).findAggregationBars(any(Node.class));
	}

	@Test
	public void checkModelActivity() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.getDistributionBars();

		verify(model).getAllElementsByType(DISTRIBUTION_NODE_TYPE);
		verify(distributionType).init();
	}

	@Test
	public void checkSerivceActivity() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.getDistributionBars();

		verify(service, atLeast(NUMBER_OF_NODES_TO_ANALYSE)).createAggregation(
				any(Node.class), any(Node.class));
	}

	@Test
	public void checkBarCounts() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> bars = distribution.getDistributionBars();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			assertEquals("Incorrect count in Bar", BAR_COUNT[i], bars.get(i)
					.getCount());
		}
	}

	@Test
	public void checkReloadedDistribution() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);

		Object property = mock(Object.class);
		Set<Object> objects = new HashSet<Object>(2);
		objects.add(property);
		when(
				model.getAllProperties(distributionType.getNodeType(),
						distributionType.getPropertyName()))
				.thenReturn(objects);
		when(
				model.getPropertyValueCount(distributionType.getNodeType(),
						distributionType.getPropertyName(), property))
				.thenReturn(2);

		List<IDistributionBar> newBars = newDistribution.getDistributionBars();

		DistributionModel reloadedDistribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> reloadedBars = reloadedDistribution
				.getDistributionBars();

		assertEquals("Invalid name of reloaded Distribution",
				newDistribution.getName(), reloadedDistribution.getName());
		assertEquals("Invalid size of bars in reloaded Distribution",
				newBars.size(), reloadedBars.size());

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			IDistributionBar newBar = newBars.get(i);
			IDistributionBar reloadedBar = reloadedBars.get(i);

			assertEquals("Incorrect name of reloaded distribution",
					newBar.getName(), reloadedBar.getName());
			assertEquals("Incorrect color of reloaded distribution",
					newBar.getColor(), reloadedBar.getColor());
			assertEquals("Incorrect count of reloaded distribution",
					newBar.getCount(), reloadedBar.getCount());
			assertEquals("Incorrect root element of reloaded distribution",
					newBar.getRootElement(), reloadedBar.getRootElement());
		}
	}

	@Test
	public void checkPropertiesOfBarUpdated() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		List<IDistributionBar> distributionBars = newDistribution
				.getDistributionBars();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			IDistributionBar distributionBar = distributionBars.get(i);

			verify(service).updateDistributionBar(eq(rootAggregation),
					eq(distributionBar));
		}
	}

	@Test
	public void checkModelCountChanged() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.getDistributionBars();

		verify(service).updateDistributionModelCount(eq(rootAggregation),
				any(Integer.class));
	}

	@Test
	public void checkModelActionsOnEnablingCurrent() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.setCurrent(true);

		verify(service).setCurrentDistributionModel(parentDistribution,
				rootAggregation);
	}

	@Test
	public void checkModelActionsOnDisablingCurrent() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.setCurrent(false);

		verify(service).setCurrentDistributionModel(parentDistribution, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void trySetNullForLeftColor() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.setLeftColor(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void trySetNullForRightColor() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.setRightColor(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void trySetNullForSelectedColor() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.setMiddleColor(null);
	}

	@Test
	public void checkServiceActivityOnFinishUp() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);
		newDistribution.finishUp();

		verify(service).updateSelectedBarColors(eq(rootAggregation),
				any(Color.class), any(Color.class), any(Color.class));
	}

	@Test
	public void checkDefaultSelectionColors() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				false, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel newDistribution = new DistributionModel(model,
				distributionType);

		assertEquals("Unexpected default left color",
				DistributionModel.DEFAULT_LEFT_COLOR,
				newDistribution.getLeftColor());
		assertEquals("Unexpected default right color",
				DistributionModel.DEFAULT_RIGHT_COLOR,
				newDistribution.getRightColor());

		assertEquals("Unexpected default selected color",
				DistributionModel.DEFAULT_MIDDLE_COLOR,
				newDistribution.getMiddleColor());
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToUpdateBarWithoutBar() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.updateBar(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToUpdateBarWithoutName() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		DistributionBar bar = new DistributionBar(distribution);
		bar.setName(null);

		distribution.updateBar(bar);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToUpdateBarWithEmptyName() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		DistributionBar bar = new DistributionBar(distribution);
		bar.setName(StringUtils.EMPTY);

		distribution.updateBar(bar);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToUpdateBarWithNullRootElement() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		DistributionBar bar = new DistributionBar(distribution);
		bar.setName(DISTRIBUTION_BAR_NAME_PREFIX);
		bar.setRootElement(null);

		distribution.updateBar(bar);
	}

	@Test(expected = IllegalArgumentException.class)
	public void tryToUpdateBarWithNullNegativeCount() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);

		DistributionBar bar = new DistributionBar(distribution);
		bar.setName(DISTRIBUTION_BAR_NAME_PREFIX);
		bar.setRootElement(mock(IDataElement.class));
		bar.setCount(-1);

		distribution.updateBar(bar);
	}

	@Test
	public void checkServiceActivityOnUpdate() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);
		IDistributionBar bar = getDistributionBar();

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		distribution.updateBar(bar);

		verify(service).updateDistributionBar(eq(rootAggregation), eq(bar));
	}

	@Test
	public void checkServiceActivityOnUpdatePalette() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		BrewerPalette palette = PlatformGIS.getColorBrewer().getPalettes()[0];
		distribution.setPalette(palette);
		distribution.finishUp();

		verify(service).updatePalette(eq(rootAggregation), eq(palette));
	}

	@Test
	public void checkServiceActivityOnNullPalette() throws Exception {
		Node parentDistribution = getNode();
		Node rootAggregation = getNode(DistributionNodeTypes.ROOT_AGGREGATION);
		List<Node> distributionBarNodes = getDistributionBarNodes();
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, distributionBarNodes,
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);

		DistributionModel distribution = new DistributionModel(model,
				distributionType);
		BrewerPalette palette = PlatformGIS.getColorBrewer().getPalettes()[0];
		distribution.setPalette(null);
		distribution.finishUp();

		verify(service, never())
				.updatePalette(eq(rootAggregation), eq(palette));
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkDistributionModelConstructorWithNullModel() {
		Node node = mock(Node.class);
		new DistributionModel(null, node);
	}

	@Test
	public void checkSettersAndGetters() throws AWEException {
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(true);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);
		DistributionModel distributionModel = new DistributionModel(model,
				distributionType);
		Color color = mock(Color.class);
		int defaultCount = 0;

		distributionModel.setRightColor(color);
		distributionModel.setLeftColor(color);
		distributionModel.setMiddleColor(color);
		when(distributionModel.getAnalyzedModel()).thenReturn(model);

		Assert.assertEquals(defaultCount, distributionModel.getBarCount());
		Assert.assertEquals(model, distributionModel.getAnalyzedModel());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void checkGetBarColorForAggregatedElement() throws AWEException {
		PropertyContainer propertyContainer = mock(PropertyContainer.class);
		DataElement dataElement = new DataElement(propertyContainer);
		when(dataElement.get(AbstractService.TYPE)).thenReturn(
				DriveNodeTypes.MP);
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(true);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		DatasetService datasetService = mock(DatasetService.class);
		DistributionModel.datasetService = datasetService;
		Iterable iterable = mock(Iterable.class);

		when(
				datasetService.getFirstRelationTraverser(dataElement.getNode(),
						DriveRelationshipTypes.LOCATION, Direction.INCOMING))
				.thenReturn(iterable);

		iterable = datasetService.getFirstRelationTraverser(
				dataElement.getNode(), DriveRelationshipTypes.LOCATION,
				Direction.INCOMING);

		Iterator iterator = mock(Iterator.class);
		when(iterable.iterator()).thenReturn(iterator);

		iterator = iterable.iterator();

		when(iterator.next()).thenReturn(parentDistribution);

		IDistributionalModel model = getDistributionalModel(parentDistribution);
		DistributionModel distributionModel = new DistributionModel(model,
				distributionType);

		Assert.assertNull(distributionModel
				.getBarColorForAggregatedElement(dataElement));
		when(
				service.findBarByAggregatedNode(parentDistribution,
						rootAggregation.getId()))
				.thenReturn(parentDistribution);

		Assert.assertNull(distributionModel
				.getBarColorForAggregatedElement(dataElement));
	}

	@Test(expected = IllegalArgumentException.class)
	public void checkGetBarColorForAggregatedElementWithNull()
			throws AWEException {
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(true);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		IDistributionalModel model = getDistributionalModel(parentDistribution);
		DistributionModel distributionModel = new DistributionModel(model,
				distributionType);
		distributionModel.getBarColorForAggregatedElement(null);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void checkGetBarColorForAggregatedElementElseCase()
			throws AWEException {
		PropertyContainer propertyContainer = mock(PropertyContainer.class);
		DataElement dataElement = new DataElement(propertyContainer);
		when(dataElement.get(AbstractService.TYPE))
				.thenReturn(DriveNodeTypes.M);
		Node parentDistribution = getNode();
		Node rootAggregation = getDistributionRootNode(true);
		IDistribution<?> distributionType = getDistributionType();
		DistributionService service = getDistributionService(
				parentDistribution, rootAggregation, getDistributionBarNodes(),
				true, distributionType);
		DistributionModel.distributionService = service;
		DatasetService datasetService = mock(DatasetService.class);
		DistributionModel.datasetService = datasetService;
		Iterable iterable = mock(Iterable.class);

		when(
				datasetService.getFirstRelationTraverser(dataElement.getNode(),
						DriveRelationshipTypes.LOCATION, Direction.INCOMING))
				.thenReturn(iterable);

		iterable = datasetService.getFirstRelationTraverser(
				dataElement.getNode(), DriveRelationshipTypes.LOCATION,
				Direction.INCOMING);

		Iterator iterator = mock(Iterator.class);
		when(iterable.iterator()).thenReturn(iterator);

		iterator = iterable.iterator();

		when(iterator.next()).thenReturn(parentDistribution);

		IDistributionalModel model = getDistributionalModel(parentDistribution);
		DistributionModel distributionModel = new DistributionModel(model,
				distributionType);

		Assert.assertNull(distributionModel
				.getBarColorForAggregatedElement(dataElement));
	}

	/**
	 * Returns mocked list of Distribution Bars
	 * 
	 * @return
	 */
	private List<Node> getDistributionBarNodes() {
		ArrayList<Node> result = new ArrayList<Node>();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			result.add(getDistributionBarNode(i));
		}

		return result;
	}

	/**
	 * Returns mocked Aggregation Bar
	 * 
	 * @return
	 */
	private Node getDistributionBarNode(int index) {
		Node result = getNode(DistributionNodeTypes.AGGREGATION_BAR);

		when(result.getProperty(DistributionService.BAR_COLOR, null))
				.thenReturn(getColorArray(DISTRIBUTION_BAR_COLORS[index]));
		when(result.getProperty(DistributionService.COUNT, null)).thenReturn(
				index);
		when(result.getProperty(AbstractService.NAME, null)).thenReturn(
				DISTRIBUTION_BAR_NAME_PREFIX + index);

		return result;
	}

	/**
	 * Creates mocked Distribution Service for testing constructor
	 * 
	 * @param distributionModelRoot
	 * @param rootAggregationNode
	 * @param shouldFind
	 * @return
	 */
	private DistributionService getDistributionService(
			Node distributionModelRoot, Node rootAggregationNode,
			List<Node> distributionBars, boolean shouldFind,
			IDistribution<?> distirbution) throws AWEException {
	
		NeoServiceFactory factory = mock(NeoServiceFactory.class);
		when(factory.getInstance()).thenReturn(null);
		
		
		DistributionService service = mock(DistributionService.class);

		Node aggregationForFind = null;
		if (shouldFind) {
			aggregationForFind = rootAggregationNode;
		}
		when(
				service.findRootAggregationNode(distributionModelRoot,
						distirbution)).thenReturn(aggregationForFind);

		Iterable<Node> distributionBarNodes = new ArrayList<Node>();
		if (shouldFind) {
			distributionBarNodes = distributionBars;
		}
		when(service.findAggregationBars(rootAggregationNode)).thenReturn(
				distributionBarNodes);

		if (!shouldFind) {
			when(
					service.createRootAggregationNode(distributionModelRoot,
							distirbution)).thenReturn(rootAggregationNode);

			for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
				when(
						service.createAggregationBarNode(
								eq(rootAggregationNode),
								any(IDistributionBar.class))).thenReturn(
						distributionBars.get(i));
			}
		}

		return service;
	}

	/**
	 * Creates mocked root of aggregation
	 * 
	 * @return
	 */
	private Node getDistributionRootNode(boolean shouldExist) {
		Node result = getNode(DistributionNodeTypes.ROOT_AGGREGATION);

		BrewerPalette palette = PlatformGIS.getColorBrewer().getPalettes()[PALETTE_INDEX];
		if (shouldExist) {
			when(result.getProperty(DistributionService.PALETTE, null))
					.thenReturn(palette.getName());
		}

		return result;
	}

	/**
	 * Creates mocked Node
	 * 
	 * @return
	 */
	private Node getNode(INodeType nodeType) {
		Node result = mock(Node.class);

		if (nodeType != null) {
			when(result.getProperty(AbstractService.TYPE, StringUtils.EMPTY))
					.thenReturn(nodeType.getId());
		}
		when(result.getProperty(DistributionService.COUNT, 0)).thenReturn(
				NUMBER_OF_DISTRIBUTION_BARS);
		when(result.getProperty(DistributionService.PROPERTY_NAME)).thenReturn(
				DISTRIBUTION_PROPERTY_NAME);
		when(result.getProperty(DistributionService.NAME)).thenReturn(
				DEFAULT_DISTRIBUTION_NAME);

		return result;
	}

	/**
	 * Creates mocked Node
	 * 
	 * @return
	 */
	private Node getNode() {
		return getNode(null);
	}

	/**
	 * Create mocked Distributional Model with default Node
	 * 
	 * @return
	 */
	private IDistributionalModel getDistributionalModel() {
		Node node = getNode();
		return getDistributionalModel(node);
	}

	/**
	 * Create mocked Distributional Model
	 * 
	 * @return
	 */
	private IDistributionalModel getDistributionalModel(Node rootNode) {
		IDistributionalModel result = mock(IDistributionalModel.class);

		when(result.getRootNode()).thenReturn(rootNode);
		when(result.getName()).thenReturn(DEFAULT_MODEL_NAME);

		Iterable<IDataElement> analysedNodes = getAnalysedNodes();
		when(result.getAllElementsByType(DISTRIBUTION_NODE_TYPE)).thenReturn(
				analysedNodes);

		return result;
	}

	/**
	 * Creates mocked Range
	 * 
	 * @param index
	 * @return
	 */
	private IRange createRange(int index) {
		IRange result = mock(IRange.class);

		when(result.getName()).thenReturn(DISTRIBUTION_BAR_NAME_PREFIX + index);
		when(result.getColor()).thenReturn(DISTRIBUTION_BAR_COLORS[index]);

		Filter filter = new Filter();
		filter.setExpression(DISTRIBUTION_NODE_TYPE,
				DISTRIBUTION_PROPERTY_NAME, index);
		when(result.getFilter()).thenReturn(filter);

		return result;
	}

	/**
	 * Creates list of mocked Ranges
	 * 
	 * @return
	 */
	private List<IRange> getRanges() {
		ArrayList<IRange> result = new ArrayList<IRange>();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			result.add(createRange(i));
		}

		return result;
	}

	/**
	 * Creates mocked Distribution Type
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private IDistribution getDistributionType() {
		IDistribution result = mock(IDistribution.class);

		when(result.getName()).thenReturn(DEFAULT_DISTRIBUTION_NAME);

		List<IRange> range = getRanges();
		when(result.getRanges()).thenReturn(range);

		when(result.getNodeType()).thenReturn(DISTRIBUTION_NODE_TYPE);
		when(result.getCount()).thenReturn(NUMBER_OF_NODES_TO_ANALYSE);
		when(result.getPropertyName()).thenReturn(DISTRIBUTION_PROPERTY_NAME);

		return result;
	}

	/**
	 * Creates Iterable of Analysed Nodes
	 * 
	 * @return
	 */
	private Iterable<IDataElement> getAnalysedNodes() {
		List<IDataElement> result = new ArrayList<IDataElement>();

		for (int i = 0; i < NUMBER_OF_DISTRIBUTION_BARS; i++) {
			for (int j = 0; j < BAR_COUNT[i]; j++) {
				Node node = getNode(DISTRIBUTION_NODE_TYPE);

				when(node.hasProperty(DISTRIBUTION_PROPERTY_NAME)).thenReturn(
						true);
				when(node.getProperty(DISTRIBUTION_PROPERTY_NAME))
						.thenReturn(i);

				DataElement element = new DataElement(node);
				result.add(element);
			}
		}

		return result;
	}

	private IDistributionBar getDistributionBar() {
		IDistributionBar result = mock(IDistributionBar.class);

		when(result.getName()).thenReturn(DISTRIBUTION_BAR_NAME_PREFIX);
		when(result.getRootElement()).thenReturn(
				new DataElement(new HashMap<String, Object>()));

		return result;
	}

}