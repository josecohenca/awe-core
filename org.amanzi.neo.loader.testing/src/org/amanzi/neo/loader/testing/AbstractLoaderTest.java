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
package org.amanzi.neo.loader.testing;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.NeoCorePlugin;
import org.neo4j.api.core.EmbeddedNeo;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.Transaction;
import org.neo4j.util.index.LuceneIndexService;

/**
 * Abstract class for testing neo loaders.
 * 
 * @author Shcharbatsevich_A
 * @since 1.0.0
 */
public abstract class AbstractLoaderTest {
	
	public static final String BUNDLE_KEY_EMPTY = "empty";
	public static final String BUNDLE_KEY_CORRECT = "correct";
	public static final String BUNDLE_KEY_WRONG = "wrong";
	public static final String BUNDLE_KEY_TIME = "time";
	public static final String BUNDLE_KEY_SEPERATOR = ",";
	public static final String BUNDLE_KEY_TRUE = "1";
	
	private static EmbeddedNeo neo = null;
	private static final String DATABASE_NAME = "neo_test";
	private static final String USER_HOME = "user.home";
	private static final String AMANZI_STR = ".amanzi";
	
	public static final int IGNORE = 0;
	public static final int ONLY_ONE = 1;
	public static final int MANY = 2;
	
	private List<Node> visitedNodes;
	
	/**
	 * Gets neo service.
	 * @return
	 */
	public static EmbeddedNeo getNeo(){
		if (neo == null){
			neo = new EmbeddedNeo(NeoTestPlugin.getDefault().getDatabaseLocationWithCheck());
		}
		return neo;
	}
	
	/**
	 * Finish actions.
	 */
	public static void doFinish(){
		if(neo!=null){
			neo.shutdown();
			neo = null;
		}
		clearDbDirectory();
	}
	
	/**
	 * Initialize Lucene index.
	 * @return LuceneIndexService
	 */
	protected static LuceneIndexService initIndex(){
		return new LuceneIndexService(getNeo());
	}
	
	/**
	 * Initialize project service.
	 */
	protected static void initProjectService(){
		NeoCorePlugin.getDefault().initProjectService(getNeo());
	}

	/**
	 * Delete data base directory.
	 */
	protected static void clearDbDirectory() {
		File dir = new File(System.getProperty(USER_HOME));
		if(dir.exists() && dir.isDirectory()){
			dir = new File(dir,AMANZI_STR);
			if(dir.exists() && dir.isDirectory()){
				dir = new File(dir,DATABASE_NAME);
				if(dir.exists()){
					if(dir.isDirectory()){
						clearDirectory(dir);
					}
					dir.delete();
				}
			}
		}
	}
	
	/**
	 * Clear directory.
	 * @param directory File (for clear)
	 */
	protected static void clearDirectory(File directory){
		if(directory.exists()){
			for(File file : directory.listFiles()){
				if(file.isDirectory()){
					clearDirectory(file);
				}
				file.delete();
			}
		}
	}
	
	/**
	 * Gets property from resource bungle.
	 * @param aBundleName String (bundle)
	 * @param aKey String (key)
	 * @return String (property)
	 */
	protected static String getProperty(String aBundleName, String aKey){
		return ResourceBundle.getBundle(aBundleName).getString(aKey);
	}
	
	/**
	 * Gets file name for build data base.
	 * @param aKey String (test key)
	 * @return String
	 */
	protected String getDbName(String aKey){
		return getProperty("test_loader.db_name."+aKey);
	}
	
	/**
	 * Gets directory name where saved file for build data base 
	 * @return String
	 */
	protected String getFileDirectory(){
		return getProperty("test_loader.common.file_directory");
	}
	
	/**
	 * Gets property from default resource bungle.
	 * @param aKey String (key)
	 * @return String (property)
	 */
	protected String getProperty(String aKey){
		return getProperty(getDefaultBungleName(), aKey);
	}
	
	/**
	 * Gets name of default resource bungle.
	 * @return String 
	 */
	protected String getDefaultBungleName(){
		return this.getClass().getName();
	}
	
	/**
	 * Convert string to list by BUNDLE_KEY_SEPERATOR
	 * @param aString
	 * @return List<String>
	 */
	protected static List<String> parceStringToList(String aString){
		if(aString==null||aString.length()==0){
			return new ArrayList<String>(0);
		}
		String[] splited = aString.split(BUNDLE_KEY_SEPERATOR);
		return Arrays.asList(splited);
	}
	
	/**
	 * Convert string to boolean value (true - equals BUNDLE_KEY_TRUE).
	 * @param aString
	 * @return boolean
	 */
	protected static boolean parceStringToBoolean(String aString){
		return aString!=null&&aString.equals(BUNDLE_KEY_TRUE);
	}
	
	/**
	 * Convert string to int value.
	 * @param aString
	 * @return boolean
	 */
	protected static int parceStringToInt(String aString){
		if(aString == null||aString.length()==0){
			return -1;
		}
		return Integer.parseInt(aString);
	}
	
	/**
	 * Convert string to long value.
	 * @param aString
	 * @return boolean
	 */
	protected static long parceStringToLong(String aString){
		if(aString == null||aString.length()==0){
			return 0;
		}
		return Long.parseLong(aString);
	}
	
	/**
	 * Check structure of loader.
	 * @param loader 
	 */
    protected void assertLoader(Object loader){
    	Transaction transacation = getNeo().beginTx();
    	try{    	
	    	Node root = getNeo().getReferenceNode();
	    	if(root==null){
	    		return;
	    	}
	    	visitedNodes = new ArrayList<Node>();
	    	assertNode(root);
	    	transacation.success();
	    }
    	finally{
    		transacation.finish();
    	}
    }
    
    /**
     * Check node with all links (recursive).
     * @param aNode Node
     */
    protected void assertNode(Node aNode){
    	visitedNodes.add(aNode);
    	Node referenceNode = getNeo().getReferenceNode();
		boolean isReference = aNode.equals(referenceNode);
    	String typeA = isReference?"root":(String) aNode.getProperty(INeoConstants.PROPERTY_TYPE_NAME);
    	List<String> necessaryLinks = parceStringToList(getProperty("test_loader.necessary_links.type_"+typeA));
    	List<String> ignoreLinks = parceStringToList(getProperty("test_loader.common_ignore_links"));
    	HashMap<String, Integer> checkedLinks = new HashMap<String, Integer>();
    	for(Relationship link : aNode.getRelationships()){
    		String linkType = link.getType().name();
    		if(ignoreLinks.contains(linkType)){
    			continue;
    		}
    		boolean isOut = link.getStartNode().equals(aNode);
			String postfix = isOut?"out":"in";
			Node linked = link.getOtherNode(aNode);
			isReference = linked.equals(referenceNode);
			String typeB = isReference?"root":(String)linked.getProperty(INeoConstants.PROPERTY_TYPE_NAME);
			String currKey = typeA+"."+linkType+"."+typeB+"."+postfix;
			int result;
			try {
				result = parceStringToInt(getProperty("test_loader.link_key." + currKey));
			} catch (MissingResourceException e) {
				throw new AssertionError("Wrong link <" + currKey + ">.");
			}
			String linkKey = linkType+"_"+postfix+"_"+typeB;
			if(result==ONLY_ONE){
				Integer before = checkedLinks.get(linkKey);
				assertTrue("More then one link, key <"+currKey+">.",before==null);
			}
			if(result==IGNORE){
				continue;
			}
			necessaryLinks.remove(linkKey);
			Integer before = checkedLinks.get(linkKey);
			if(before == null){
				before = 0;
			}
			checkedLinks.put(linkKey, before++);
			if (!visitedNodes.contains(linked)) {
				assertNode(linked);
			}
    	}
    	assertTrue("Node <"+typeA+"> has no necessary links: "
    			+Arrays.toString(necessaryLinks.toArray())+"."
    			,necessaryLinks.isEmpty());
    }
	
    /**
     * Check time of load data.
     * @param loadTime long (time for check)
     * @param aTestKey String (test key)
     */
	protected void assertLoadTime(long loadTime, String aTestKey) {
		long maxTime = parceStringToLong(getProperty("test_loader.max_time."+aTestKey));
		String message = String.format("Load time(ms) = %d, max time(ms) = %d", loadTime, maxTime);
		assertTrue(message, loadTime <= maxTime);
	}

}
