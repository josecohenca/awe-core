package org.amanzi.neo.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.amanzi.neo.services.exceptions.AWEException;
import org.amanzi.neo.services.exceptions.DuplicateNodeNameException;
import org.amanzi.neo.services.exceptions.IllegalNodeDataException;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class ProjectServiceTest {
	private static Logger LOGGER = Logger.getLogger(ProjectServiceTest.class);

	private static ProjectService projectService;
	private static GraphDatabaseService graphDb;
	private static final String prName = "Project";
	private static final String databasePath = System.getProperty("user.home")
			+ File.separator + ".amanzi" + File.separator + "test";
	private static Transaction tx;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		graphDb = new EmbeddedGraphDatabase(databasePath);
		LOGGER.info("Database created in folder " + databasePath);
		projectService = new ProjectService(graphDb);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (graphDb != null) {
			graphDb.shutdown();
			LOGGER.info("Database shut down");
		}
		deleteFolder(new File(databasePath));

	}

	@Test
	public void testCreateProject() {
		LOGGER.info("testCreateProject() started");
		try {
			// check that the node is created, it has the defined name
			Node project = projectService.createProject(prName);

			Assert.assertNotNull(project);
			Assert.assertEquals(prName,
					project.getProperty(NewAbstractService.PROPERTY_NAME_NAME, null));
			// check that type is 'project'
			// check that it's attached to the reference node and relation type
			// is
			// correct
			assertProjectRelatedToRefNode(project);
		} catch (AWEException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("testCreateProject() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testCreateProjectEmptyName() throws AWEException {
		LOGGER.info("testCreateProjectEmptyName() started");
		projectService.createProject("");
		LOGGER.info("testCreateProjectEmptyName() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testCreateProjectNullName() throws AWEException {
		LOGGER.info("testCreateProjectNullName() started");
		projectService.createProject(null);
		LOGGER.info("testCreateProjectNullName() finished");
	}

	@Test(expected = DuplicateNodeNameException.class)
	public void testCreateProjectExisting() throws AWEException {
		LOGGER.info("testCreateProjectExisting() started");
		projectService.createProject(prName);
		LOGGER.info("testCreateProjectExisting() finished");
	}

	@Test
	public void testFindProject() {
		LOGGER.info("testFindProject() started");
		try {
			// check that the node with defined name is found, of project type
			// and connected to ref node
			Node project = projectService.createProject(prName + "1");

			// create a node of another type with the same name
			tx = graphDb.beginTx();
			graphDb.createNode().setProperty(NewAbstractService.PROPERTY_NAME_NAME,
					prName + "1");
			tx.success();
			tx.finish();

			Node found = projectService.findProject(prName + "1");
			Assert.assertEquals(prName + "1",
					found.getProperty(NewAbstractService.PROPERTY_NAME_NAME, null));
			Assert.assertEquals(project, found);
			assertProjectRelatedToRefNode(found);
		} catch (AWEException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("testFindProject() finished");
	}

	@Test
	public void testFindProjectNotFound() {
		LOGGER.info("testFindProjectNotFound() started");
		try {
			// null is returned if not found
			Node found = projectService.findProject(prName + "2");
			Assert.assertNull(found);
		} catch (AWEException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("testFindProjectNotFound() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testFindProjectEmptyName() throws AWEException {
		LOGGER.info("testFindProjectEmptyName() started");
		projectService.findProject("");
		LOGGER.info("testFindProjectEmptyName() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testFindProjectNullName() throws AWEException {
		LOGGER.info("testFindProjectNullName() started");
		projectService.findProject(null);
		LOGGER.info("testFindProjectNullName() finished");
	}

	@Test
	public void testFindAllProjects() {
		LOGGER.info("testFindAllProjects() started");
		// check the number, names and type of returned nodes
		int count = 0;
		List<String> projectNames = new ArrayList<String>();
		projectNames.add(prName);
		projectNames.add(prName + "1");

		for (Node node : projectService.findAllProjects()) {
			count++;
			Assert.assertTrue(projectNames.contains(node.getProperty(
					NewAbstractService.PROPERTY_NAME_NAME, null)));
			assertProjectRelatedToRefNode(node);
		}
		Assert.assertTrue(2 == count);
		LOGGER.info("testFindAllProjects() finished");
	}

	@Test
	public void testGetProject() {
		LOGGER.info("testGetProject() started");
		try {
			// check that the node is created, it has the defined name
			// check that type is 'project'
			// check that it's attached to the reference node and relation type
			// is
			// correct
			Node got = projectService.getProject(prName + "2");
			Assert.assertNotNull(got);
			Assert.assertEquals(prName + "2",
					got.getProperty(NewAbstractService.PROPERTY_NAME_NAME, null));
			assertProjectRelatedToRefNode(got);
		} catch (AWEException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("testGetProject() finished");
	}

	@Test
	public void testGetProjectNotRecreated() {
		LOGGER.info("testGetProjectNotRecreated() started");
		try {
			// check that the returned node is not recreated if already exists
			Node project = projectService.createProject(prName + "3");
			Node got = projectService.getProject(prName + "3");
			Assert.assertEquals(project, got);
		} catch (AWEException e) {
			LOGGER.error(e.getMessage(), e);
		}
		LOGGER.info("testGetProjectNotRecreated() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testGetProjectEmptyName() throws AWEException {
		LOGGER.info("testGetProjectEmptyName() started");
		projectService.getProject("");
		LOGGER.info("testGetProjectEmptyName() finished");
	}

	@Test(expected = IllegalNodeDataException.class)
	public void testGetProjectNullName() throws AWEException {
		LOGGER.info("testGetProjectNullName() started");
		projectService.getProject(null);
		LOGGER.info("testGetProjectNullName() finished");
	}

	private void assertProjectRelatedToRefNode(Node node) {
		Assert.assertEquals(ProjectService.ProjectNodeType.PROJECT.getId(),
				node.getProperty(NewAbstractService.PROPERTY_TYPE_NAME, null));
		Node refNode = graphDb.getReferenceNode();
		for (Relationship rel : node.getRelationships(
				ProjectService.ProjectRelationshipType.PROJECT,
				Direction.INCOMING)) {
			Assert.assertTrue(rel.getOtherNode(node).equals(refNode));
		}
	}

	// TODO: use a common method instead of this one
	private static void deleteFolder(File path) {
		if (path.exists()) {
			for (File file : path.listFiles()) {
				if (file.isDirectory()) {
					deleteFolder(file);
				} else {
					file.delete();
				}
			}
			path.delete();
		}
	}

}
