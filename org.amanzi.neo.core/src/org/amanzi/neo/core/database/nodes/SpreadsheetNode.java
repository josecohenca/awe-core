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
package org.amanzi.neo.core.database.nodes;

import java.util.Iterator;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.database.exception.SplashDatabaseException;
import org.amanzi.neo.core.database.exception.SplashDatabaseExceptionMessages;
import org.amanzi.neo.core.enums.SplashRelationshipTypes;
import org.neo4j.api.core.Direction;
import org.neo4j.api.core.Node;
import org.neo4j.api.core.Relationship;
import org.neo4j.api.core.ReturnableEvaluator;
import org.neo4j.api.core.StopEvaluator;
import org.neo4j.api.core.TraversalPosition;
import org.neo4j.api.core.Traverser;

/**
 * Wrapper class for Spreadsheet
 * 
 * @author Lagutko_N
 */

public class SpreadsheetNode extends AbstractNode {

	/*
	 * Type of this Node
	 */
	private static final String SPREADSHEET_NODE_TYPE = "spreadsheet";

	/**
	 * Constructor. Wraps a Node from database and sets type and name of Node
	 * 
	 * @param node
	 *            database node
	 */
	public SpreadsheetNode(Node node, String name) {
		super(node);
		setParameter(INeoConstants.PROPERTY_TYPE_NAME, SPREADSHEET_NODE_TYPE);
		setParameter(INeoConstants.PROPERTY_NAME_NAME, name);
	}

	/**
     * Constructor for wrapping existing Spreadsheet nodes. To reduce API confusion,
     * this constructor is private, and users should use the factory method instead.
     * @param node
     */
    private SpreadsheetNode(Node node) {
        super(node);
        if(!getParameter(INeoConstants.PROPERTY_TYPE_NAME).toString().equals(SPREADSHEET_NODE_TYPE)) throw new RuntimeException("Expected existing Spreadsheet Node, but got "+node.toString());
    }

    /**
     * Use factory method to ensure clear API different to normal constructor.
     *
     * @param node representing an existing Spreadsheet
     * @return SpreadsheetNode from existing Node
     */
    public static SpreadsheetNode fromNode(Node node) {
        return new SpreadsheetNode(node);
    }

	/**
	 * Sets name of Spreadsheet
	 * 
	 * @param newName
	 *            name of Spreadsheet
	 */

	public void setSpreadsheetName(String newName) {
		setParameter(INeoConstants.PROPERTY_NAME_NAME, newName);
	}

	/**
	 * Returns name of Spreadsheet
	 * 
	 * @return name of Spreadsheet
	 */
	public String getSpreadsheetName() {
		return (String) getParameter(INeoConstants.PROPERTY_NAME_NAME);
	}

	/**
	 * Adds a Row to Spreadsheet
	 * 
	 * @param row
	 *            row wrapper
	 */
	public void addRow(RowNode row) {
		addRelationship(SplashRelationshipTypes.ROW, row.getUnderlyingNode());
	}

	/**
	 * Adds chart to Spreadsheet
	 * 
	 * @param chart
	 * @deprecated
	 */
	public void addChart(ChartNode chart) {
		addRelationship(SplashRelationshipTypes.CHART, chart
				.getUnderlyingNode());
	}

	/**
	 * Add pie chart to Spreadsheet
	 * 
	 * @param chart
	 */
	public void addPieChart(PieChartNode chart) {
		addRelationship(SplashRelationshipTypes.PIE_CHART, chart
				.getUnderlyingNode());
	}

	/**
	 * Returns a Chart by given index
	 * 
	 * @param chartIndex
	 *            index of chart
	 * @return chart by index
	 * @throws SplashDatabaseException
	 *             if was founded more than one row by given index
	 */
	public ChartNode getChart(final String chartIndex)
			throws SplashDatabaseException {
		Iterator<ChartNode> iterator = new ChartIterator(chartIndex);

		if (iterator.hasNext()) {
			ChartNode result = iterator.next();
			if (iterator.hasNext()) {
				String message = SplashDatabaseExceptionMessages
						.getFormattedString(
								SplashDatabaseExceptionMessages.Not_Single_Chart_by_ID,
								chartIndex);
				throw new SplashDatabaseException(message);
			}
			return result;
		}

		return null;
	}

	/**
	 * Returns a Pie Chart by given index
	 * 
	 * @param chartIndex
	 *            index of chart
	 * @return chart by index
	 * @throws SplashDatabaseException
	 *             if was founded more than one row by given index
	 */
	public PieChartNode getPieChart(final String chartIndex)
			throws SplashDatabaseException {
		Iterator<PieChartNode> iterator = new PieChartIterator(chartIndex);

		if (iterator.hasNext()) {
			PieChartNode result = iterator.next();
			if (iterator.hasNext()) {
				String message = SplashDatabaseExceptionMessages
						.getFormattedString(
								SplashDatabaseExceptionMessages.Not_Single_Chart_by_ID,
								chartIndex);
				throw new SplashDatabaseException(message);
			}
			return result;
		}

		return null;
	}

	/**
	 * Returns a Row by given index
	 * 
	 * @param rowIndex
	 *            index of row
	 * @return row by index
	 * @throws SplashDatabaseException
	 *             if was founded more than one row by given index
	 */
	public RowNode getRow(final String rowIndex) throws SplashDatabaseException {
		Iterator<RowNode> iterator = new RowIterator(rowIndex);

		if (iterator.hasNext()) {
			RowNode result = iterator.next();
			if (iterator.hasNext()) {
				String message = SplashDatabaseExceptionMessages
						.getFormattedString(
								SplashDatabaseExceptionMessages.Not_Single_Row_by_ID,
								rowIndex);
				throw new SplashDatabaseException(message);
			}
			return result;
		}

		return null;
	}

	/**
	 * Returns a Column by given name
	 * 
	 * @param columnName
	 *            name of Column
	 * @return column by name
	 * @throws SplashDatabaseException
	 *             if was founded more than one column by given name
	 */
	public ColumnNode getColumn(String columnName)
			throws SplashDatabaseException {
		Iterator<ColumnNode> iterator = new ColumnInterator(columnName);

		if (iterator.hasNext()) {
			ColumnNode result = iterator.next();
			if (iterator.hasNext()) {
				String message = SplashDatabaseExceptionMessages
						.getFormattedString(
								SplashDatabaseExceptionMessages.Not_Single_Column_by_ID,
								columnName);
				throw new SplashDatabaseException(message);
			}
			return result;
		}

		return null;
	}

	public int getChartsCount() {
		Iterator<Relationship> iterator = node.getRelationships(
				SplashRelationshipTypes.CHART, Direction.OUTGOING).iterator();
		int result = 0;
		while (iterator.hasNext()) {
			result++;
		}
		return result;
	}

	public int getPieChartsCount() {
		Iterator<Relationship> iterator = node.getRelationships(
				SplashRelationshipTypes.PIE_CHART, Direction.OUTGOING)
				.iterator();
		int result = 0;
		while (iterator.hasNext()) {
			result++;
		}
		return result;
	}

	/**
	 * Returns a Cell by Column and Row
	 * 
	 * @param rowIndex
	 *            index of Row
	 * @param columnName
	 *            name of Column
	 * @return cell by Column and Row
	 * @throws SplashDatabaseException
	 *             if was founded more than one row, or more than one column, or
	 *             more than one cell
	 */
	public CellNode getCell(String rowIndex, String columnName)
			throws SplashDatabaseException {
		RowNode row = getRow(rowIndex);
		if (row != null) {
			return row.getCellByColumn(columnName);
		} else {
			return null;
		}
	}

	public Iterator<RowNode> getAllRows() {
		return new AllRowIterator();
	}

	/**
	 * Iterator that computes Rows by given Index
	 * 
	 * @author Lagutko_N
	 */
	private class RowIterator extends AbstractIterator<RowNode> {

		public RowIterator(final String rowIndex) {
			this.iterator = node.traverse(Traverser.Order.BREADTH_FIRST,
					StopEvaluator.DEPTH_ONE, new ReturnableEvaluator() {

						public boolean isReturnableNode(
								TraversalPosition position) {
							if (position.isStartNode()) {
								return false;
							}
							return position.lastRelationshipTraversed()
									.getEndNode()
									.getProperty(INeoConstants.PROPERTY_NAME_NAME).equals(rowIndex);
						}

					}, SplashRelationshipTypes.ROW, Direction.OUTGOING)
					.iterator();
		}

		@Override
		protected RowNode wrapNode(Node node) {
			return RowNode.fromNode(node);
		}
	}

	/**
	 * Iterator that computes Charts by given Index
	 * 
	 * @author amabdelsalam
	 */
	private class ChartIterator extends AbstractIterator<ChartNode> {

		public ChartIterator(final String chartIndex) {
			this.iterator = node.traverse(Traverser.Order.BREADTH_FIRST,
					StopEvaluator.DEPTH_ONE, new ReturnableEvaluator() {

						public boolean isReturnableNode(
								TraversalPosition position) {
							if (position.isStartNode()) {
								return false;
							}
							return position.lastRelationshipTraversed()
									.getEndNode().getProperty(
											ChartNode.CHART_INDEX).equals(
											chartIndex);
						}

					}, SplashRelationshipTypes.CHART, Direction.OUTGOING)
					.iterator();
		}

		@Override
		protected ChartNode wrapNode(Node node) {
			return new ChartNode(node);
		}
	}

	/**
	 * Iterator that computes Pie Charts by given Index
	 * 
	 * @author amabdelsalam
	 */
	private class PieChartIterator extends AbstractIterator<PieChartNode> {

		public PieChartIterator(final String chartIndex) {
			this.iterator = node.traverse(Traverser.Order.BREADTH_FIRST,
					StopEvaluator.DEPTH_ONE, new ReturnableEvaluator() {

						public boolean isReturnableNode(
								TraversalPosition position) {
							if (position.isStartNode()) {
								return false;
							}
							return position.lastRelationshipTraversed()
									.getEndNode().getProperty(
											PieChartNode.PIE_CHART_INDEX)
									.equals(chartIndex);
						}

					}, SplashRelationshipTypes.PIE_CHART, Direction.OUTGOING)
					.iterator();
		}

		@Override
		protected PieChartNode wrapNode(Node node) {
			return new PieChartNode(node);
		}
	}

	/**
	 * Iterator that computes all Rows in Spreadsheet
	 * 
	 * @author Lagutko_N
	 */
	private class AllRowIterator extends AbstractIterator<RowNode> {

		public AllRowIterator() {
			this.iterator = node.traverse(Traverser.Order.BREADTH_FIRST,
					StopEvaluator.DEPTH_ONE,
					ReturnableEvaluator.ALL_BUT_START_NODE,
					SplashRelationshipTypes.ROW, Direction.OUTGOING).iterator();
		}

		@Override
		protected RowNode wrapNode(Node node) {
			return RowNode.fromNode(node);
		}
	}

	/**
	 * Iterator that computes Columns by given Name
	 * 
	 * @author Lagutko_N
	 */

	private class ColumnInterator extends AbstractIterator<ColumnNode> {

		private static final int COLUMN_NODE_DEPTH = 3;

		public ColumnInterator(final String columnName) {

			this.iterator = node.traverse(
					Traverser.Order.DEPTH_FIRST,
					new StopEvaluator() {

						public boolean isStopNode(TraversalPosition position) {
							return position.depth() > COLUMN_NODE_DEPTH;
						}

					},
					new ReturnableEvaluator() {

						public boolean isReturnableNode(
								TraversalPosition position) {
							if (position.depth() == COLUMN_NODE_DEPTH) {
								return position.currentNode().getProperty(INeoConstants.PROPERTY_NAME_NAME).equals(columnName);
							}
							return false;
						}

					}, SplashRelationshipTypes.ROW, Direction.OUTGOING,
					SplashRelationshipTypes.ROW_CELL, Direction.OUTGOING,
					SplashRelationshipTypes.COLUMN_CELL, Direction.INCOMING)
					.iterator();

		}

		@Override
		protected ColumnNode wrapNode(Node node) {
			return ColumnNode.fromNode(node);
		}
	}

	public ChartNode getChartNode(String id) {
		ChartNode chart = null;
		try {
			chart = getChart(id);
		} catch (SplashDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
	}

	public PieChartNode getPieChartNode(String id) {
		PieChartNode chart = null;
		try {
			chart = getPieChart(id);
		} catch (SplashDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return chart;
	}

	/**
	 * Adds a Column to Spreadsheet
	 * 
	 * @param Column
	 *            wrapper
	 */
	public void addColumn(ColumnNode column) {
		addRelationship(SplashRelationshipTypes.COLUMN, column
				.getUnderlyingNode());
	}
	
	/**
	 * Returns a RubyProjectNode of this Spreadsheet
	 *
	 * @return RubyProjectNode of Spreadsheet
	 */
	public RubyProjectNode getSpreadsheetRootProject() {
	    Relationship relationship = getUnderlyingNode().getSingleRelationship(SplashRelationshipTypes.SPREADSHEET, Direction.INCOMING);
	    return RubyProjectNode.fromNode(relationship.getStartNode());
	}

}
