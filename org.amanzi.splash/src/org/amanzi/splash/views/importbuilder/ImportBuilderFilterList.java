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


package org.amanzi.splash.views.importbuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Class that plays the role of the domain model in the ImportBuilderTableViewer
 * In real life, this class would access a persistent store of some kind.
 * 
 */

public class ImportBuilderFilterList {

	private final int COUNT = 100;
	//private Vector filters = new Vector(COUNT);
	private Set changeListeners = new HashSet();

	ArrayList<ImportBuilderFilter> filters = new ArrayList<ImportBuilderFilter>();
	ArrayList<String> filter_headings = new ArrayList<String>();
	
	// Combo box choices
	static final String[] FILTER_HEADINGS_ARRAY = { "?", "Nancy", "Larry", "Joe" };
	
	/**
	 * Constructor
	 */
	public ImportBuilderFilterList() {
		super();
		this.initData();
	}
	
	/*
	 * Initialize the table data.
	 * Create COUNT filters and add them them to the 
	 * collection of filters
	 */
	private void initData() {
		ImportBuilderFilter filter;
		//filter_headings.add("Message Type");
		//filter_headings.add("Heading2");
		//filter_headings.add("Heading3");
		//for (int i = 0; i < 3; i++) {
			//filter = new ImportBuilderFilter(filter_headings.get(i),"Text" + i);
			//filters.add(filter);
		//}
		
		
	}

	/**
	 * Return the array of owners   
	 */
	public String[] getHeadingsList() {
		
		String[] ret = new String[filter_headings.size()];
		for (int i=0;i<filter_headings.size();i++) ret[i] = filter_headings.get(i);
		return ret;	    
		
	}
	
	public void addHeadingToList(String heading){
		filter_headings.add(heading);
	}
	
	public void setFilterHeading(int index, String heading){
		filter_headings.set(index, heading);
	}
	
	/**
	 * Return the collection of filters
	 */
	public ArrayList getFilters() {
		return filters;
	}
	
	/**
	 * Add a new filter to the collection of filters
	 */
	public void addFilter() {
		ImportBuilderFilter filter = new ImportBuilderFilter("Filter Heading", "Filter Text");
		filters.add(filters.size(), filter);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IImportBuilderFilterListViewer) iterator.next()).addFilter(filter);
	}
	
	/**
	 * Add a new filter to the collection of filters
	 */
	public void addFilter(String heading, String text) {
		ImportBuilderFilter filter = new ImportBuilderFilter(heading, text);
		filters.add(filters.size(), filter);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IImportBuilderFilterListViewer) iterator.next()).addFilter(filter);
		
		
	}

	/**
	 * @param filter
	 */
	public void removeFilter(ImportBuilderFilter filter) {
		filters.remove(filter);
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IImportBuilderFilterListViewer) iterator.next()).removeFilter(filter);
	}

	/**
	 * @param filter
	 */
	public void filterChanged(ImportBuilderFilter filter) {
		Iterator iterator = changeListeners.iterator();
		while (iterator.hasNext())
			((IImportBuilderFilterListViewer) iterator.next()).updateFilter(filter);
	}

	/**
	 * @param viewer
	 */
	public void removeChangeListener(IImportBuilderFilterListViewer viewer) {
		changeListeners.remove(viewer);
	}

	/**
	 * @param viewer
	 */
	public void addChangeListener(IImportBuilderFilterListViewer viewer) {
		changeListeners.add(viewer);
	}
	
	public String getFilterRubyCode(){
		String code = "";
		code += "sheet_importer = SheetImporter.create.filter_and { \n";
		for (int i=0;i<filters.size();i++){
			code += "match '" + filters.get(i).getFilterHeading() + "', '"+filters.get(i).getFilterText()+"' \n";
		}
		code += "} \n";
		
		return code;
	}


}
