package org.amanzi.splash.neo4j.database.services;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.amanzi.neo.core.service.NeoServiceProvider;
import org.amanzi.splash.neo4j.database.exception.SplashDatabaseException;
import org.amanzi.splash.neo4j.database.exception.SplashDatabaseExceptionMessages;
import org.amanzi.splash.neo4j.database.nodes.CellNode;
import org.amanzi.splash.neo4j.database.nodes.ChartItemNode;
import org.amanzi.splash.neo4j.database.nodes.ChartNode;
import org.amanzi.splash.neo4j.database.nodes.ColumnNode;
import org.amanzi.splash.neo4j.database.nodes.PieChartItemNode;
import org.amanzi.splash.neo4j.database.nodes.PieChartNode;
import org.amanzi.splash.neo4j.database.nodes.RootNode;
import org.amanzi.splash.neo4j.database.nodes.RowNode;
import org.amanzi.splash.neo4j.database.nodes.SpreadsheetNode;
import org.amanzi.splash.neo4j.swing.Cell;
import org.amanzi.splash.neo4j.ui.SplashPlugin;
import org.amanzi.splash.neo4j.utilities.NeoSplashUtil;
import org.neo4j.api.core.NeoService;
import org.neo4j.api.core.Transaction;

import com.eteks.openjeks.format.CellFormat;

/**
 * Service class for working with Neo4j-Spreadsheet
 * 
 * @author Lagutko_N 
 */

public class SpreadsheetService {
    
    /*
     * Default value of Cell Value
     */
    private static final String DEFAULT_VALUE = "";
    
    /*
     * Default value of Cell Definition 
     */
    private static final String DEFAULT_DEFINITION = "";
    
    /*
     * NeoService Provider
     */
    private NeoServiceProvider provider;
    
    /*
     * NeoService
     */
    protected NeoService neoService;
    
    /**
     * Constructor of Service.
     * 
     * Initializes NeoService and create a Root Element
     */
    public SpreadsheetService() {
        provider = NeoServiceProvider.getProvider();
        neoService = provider.getService();
    }
    
    /**
     * Searches for Spreadsheets by given name
     *
     * @param root root node of Spreadsheet
     * @param name name of Spreadsheet
     * @return founded Spreadsheet or null if Spreadsheet was not found
     */
    public SpreadsheetNode findSpreadsheet(RootNode root, String name) {
        SpreadsheetNode result = null;
        
        Transaction tx = neoService.beginTx();
        
        try {
            Iterator<SpreadsheetNode> spreadsheetIterator = root.getSpreadsheets();
            
            while (spreadsheetIterator.hasNext()) {
                SpreadsheetNode spreadsheet = spreadsheetIterator.next();
                
                if (spreadsheet.getSpreadsheetName().equals(name)) {
                    result = spreadsheet;
                    break;
                }            
            }
            tx.success();
        }
        finally {
            tx.finish();
        }
        
        return result;
    }
    
    /**
     * Creates a Spreadsheet by given name
     *
     * @param root root node for Spreadsheet
     * @param name name of Spreadsheet
     * @return create Spreadsheet
     * @throws SplashDatabaseException if Spreadsheet with given name already exists
     */
    
    public SpreadsheetNode createSpreadsheet(RootNode root, String name) throws SplashDatabaseException {
        if (findSpreadsheet(root, name) != null) {
            String message = SplashDatabaseExceptionMessages.getFormattedString(SplashDatabaseExceptionMessages.Duplicate_Spreadsheet, name);
            throw new SplashDatabaseException(message);
        }
        else {
            Transaction tx = neoService.beginTx();
            
            try {
                SpreadsheetNode spreadsheet = new SpreadsheetNode(neoService.createNode());
                
                spreadsheet.setSpreadsheetName(name);
                
                root.addSpreadsheet(spreadsheet);
                
                tx.success();
                
                return spreadsheet;
            }
            finally {
                tx.finish();                
            }
        }
    }
    
    /**
     * Creates a Chart in Spreadsheet by given ID 
     *
     *
     */
    public ChartNode createChart(SpreadsheetNode spreadsheet, String id) {
        Transaction tx = neoService.beginTx();
        
        try {        
            ChartNode chartNode = spreadsheet.getChart(id);
        
            if (chartNode == null) {
            	chartNode = new ChartNode(neoService.createNode());
            	chartNode.setChartIndex(id);
                spreadsheet.addChart(chartNode);
            }
            
            tx.success();
            
            return chartNode;
        }
        catch (SplashDatabaseException e) {
            tx.failure();
            String message = SplashDatabaseExceptionMessages.getFormattedString(SplashDatabaseExceptionMessages.Service_Method_Exception, "createChart");
            SplashPlugin.error(message, e);
            return null;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Creates a Chart in Spreadsheet by given ID 
     *
     *
     */
    public PieChartNode createPieChart(SpreadsheetNode spreadsheet, String id) {
        Transaction tx = neoService.beginTx();
        
        try {        
            PieChartNode chartNode = spreadsheet.getPieChart(id);
        
            if (chartNode == null) {
            	chartNode = new PieChartNode(neoService.createNode());
            	chartNode.setPieChartIndex(id);
                spreadsheet.addPieChart(chartNode);
            }
            
            tx.success();
            
            return chartNode;
        }
        catch (SplashDatabaseException e) {
            tx.failure();
            String message = SplashDatabaseExceptionMessages.getFormattedString(SplashDatabaseExceptionMessages.Service_Method_Exception, "createChart");
            SplashPlugin.error(message, e);
            return null;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Creates a Chart in Spreadsheet by given ID 
     *
     *
     */
    public ChartItemNode createChartItem(ChartNode chartNode, String id) throws SplashDatabaseException {
        Transaction tx = neoService.beginTx();
        
        try {        
            ChartItemNode ChartItemNode = chartNode.getChartItem(id);
        
            if (ChartItemNode == null) {
            	ChartItemNode = new ChartItemNode(neoService.createNode());
            	ChartItemNode.setChartItemIndex(id);
                chartNode.addChartItem(ChartItemNode);
            }
            
            tx.success();
            
            return ChartItemNode;
        }
        finally {
            tx.finish();
        }
    }
    /**
     * Creates a Pie Chart in Spreadsheet by given ID 
     *
     * 
     */
    public PieChartItemNode createPieChartItem(PieChartNode chartNode, String id) throws SplashDatabaseException {
        Transaction tx = neoService.beginTx();
        
        try {        
            PieChartItemNode ChartItemNode = chartNode.getPieChartItem(id);
        
            if (ChartItemNode == null) {
            	ChartItemNode = new PieChartItemNode(neoService.createNode());
            	ChartItemNode.setPieChartItemIndex(id);
                chartNode.addPieChartItem(ChartItemNode);
            }
            
            tx.success();
            
            return ChartItemNode;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Creates a Cell in Spreadsheet by given ID 
     *
     * @param spreadsheet spreadsheet
     * @param id id of Cell
     * @return created Cell
     */
    public CellNode createCell(SpreadsheetNode spreadsheet, CellID id) {
        Transaction tx = neoService.beginTx();
        
        try {        
            RowNode rowNode = spreadsheet.getRow(id.getRowName());
        
            if (rowNode == null) {
                rowNode = new RowNode(neoService.createNode());
                rowNode.setRowIndex(id.getRowName());
                spreadsheet.addRow(rowNode);
            }
            
            ColumnNode columnNode = spreadsheet.getColumn(id.getColumnName());;
            if (columnNode == null) {
                columnNode = new ColumnNode(neoService.createNode());
                columnNode.setColumnName(id.getColumnName());
            }
            
            CellNode cell = new CellNode(neoService.createNode());
            
            rowNode.addCell(cell);
            columnNode.addCell(cell);
            
            tx.success();
            
            return cell;
        }
        catch (SplashDatabaseException e) {
            tx.failure();
            String message = SplashDatabaseExceptionMessages.getFormattedString(SplashDatabaseExceptionMessages.Service_Method_Exception, "createCell");
            SplashPlugin.error(message, e);
            return null;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Updates Cell and Cell's References
     *
     * @param sheet Spreadsheet
     * @param cell Cell for update
     */
    public void updateCellWithReferences(SpreadsheetNode sheet, Cell cell) {
        //if Cell has no Script and have empty Value and Definition than delete it
        if (!cell.hasReference() && cell.getValue().equals(Cell.DEFAULT_VALUE)
                                 && cell.getDefinition().equals(Cell.DEFAULT_DEFINITION)) {
            if (deleteCell(sheet, new CellID(cell.getCellID()))) {
                return;
            }
        }
        
        CellNode updatedNode = updateCell(sheet, cell);
        
        Transaction tx = neoService.beginTx();
        
        try {
            List<String> rfdCellsIDs = NeoSplashUtil.findComplexCellIDs((String) cell.getDefinition());
            
            Iterator<CellNode> dependentCells = updatedNode.getReferencedNodes();
            
            ArrayList<CellNode> nodesToDelete = new ArrayList<CellNode>(0);
            
            while (dependentCells.hasNext()) {
                CellNode dependentCell = dependentCells.next();
                CellID id = new CellID(dependentCell.getRow().getRowIndex(), dependentCell.getColumn().getColumnName());
                
                if (!rfdCellsIDs.contains(id)) {
                    nodesToDelete.add(dependentCell);                    
                    rfdCellsIDs.remove(id);
                }
            }
            
            updatedNode.deleteReferenceFromNode(nodesToDelete);
            
            for (String ID : rfdCellsIDs){
                CellID id = new CellID(ID);
                                
                CellNode node = getCellNode(sheet, id);
                
                if (node == null) {                             
                    node = updateCell(sheet, new Cell(id.getRowIndex(), id.getColumnIndex(), DEFAULT_VALUE, DEFAULT_DEFINITION, new CellFormat()));
                }
                
                updatedNode.addDependedNode(node);                
            }
            
            tx.success();
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Updates only Cell values
     *
     * @param sheet spreadsheet
     * @param cell Cell for update
     * @return updated Cell
     */
    public CellNode updateCell(SpreadsheetNode sheet, Cell cell) {
        CellID id = new CellID(cell.getRow(), cell.getColumn());
        
        CellNode node = getCellNode(sheet, id);
        
        if (node == null) {
            node = createCell(sheet, id);                
        }
        
        Transaction tx = neoService.beginTx();
        
        try {
            node.setValue((String)cell.getValue());
            node.setDefinition((String)cell.getDefinition());
            
            if (cell.hasReference()) {
                node.setScriptURI(cell.getScriptURI());
            }
            
            CellFormat format = cell.getCellFormat();
            
            if (format != null) {
                node.setBackgroundColorB(format.getBackgroundColor().getBlue());
                node.setBackgroundColorG(format.getBackgroundColor().getGreen());
                node.setBackgroundColorR(format.getBackgroundColor().getRed());
                
                node.setFontColorB(format.getFontColor().getBlue());
                node.setFontColorG(format.getFontColor().getGreen());
                node.setFontColorR(format.getFontColor().getRed());
                
                node.setFontName(format.getFontName());
                node.setFontSize(format.getFontSize());
                node.setFontStyle(format.getFontStyle());
            }
            
            tx.success();
            
            return node;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Returns Cell by given ID
     *
     * @param sheet spreadsheet
     * @param id cell ID
     * @return converted Cell from Database
     */
    public Cell getCell(SpreadsheetNode sheet, CellID id) {
        CellNode node = getCellNode(sheet, id);
        
        if (node != null) {
            return convertNodeToCell(node, id.getRowName(), id.getColumnName());
        }
        
        return new Cell(id.getRowIndex(), id.getColumnIndex(), DEFAULT_DEFINITION, DEFAULT_VALUE, new CellFormat());
    }
    
    /**
     * Returns CellNode by given ID
     *
     * @param sheet spreadsheet
     * @param id id of Cell
     * @return CellNode by ID or null if Cell doesn't exists
     */
    private CellNode getCellNode(SpreadsheetNode sheet, CellID id) {
        Transaction tx = neoService.beginTx();
        
        try {            
            CellNode result = sheet.getCell(id.getRowName(), id.getColumnName());
            
            tx.success();
            
            return result;
        }
        catch (SplashDatabaseException e) {
            String message = SplashDatabaseExceptionMessages.getFormattedString(SplashDatabaseExceptionMessages.Service_Method_Exception, "getCellNode");
            SplashPlugin.error(message, e);            
        }
        finally {
            tx.finish();
        }
        
        return null;
    }
    
    /**
     * Returns ChartNode by given ID
     *
     * @param sheet spreadsheet
     * @param id id of Cell
     * @return CellNode by ID or null if Cell doesn't exists
     */
    private ChartNode getChartNode(SpreadsheetNode sheet, String id) {
        Transaction tx = neoService.beginTx();
        
        try {            
        	ChartNode result = sheet.getChartNode(id);
            
            tx.success();
            
            return result;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Returns Pie Chart Node by given ID
     *
     * @param sheet spreadsheet
     * 
     */
    private PieChartNode getPieChartNode(SpreadsheetNode sheet, String id) {
        Transaction tx = neoService.beginTx();
        
        try {            
        	PieChartNode result = sheet.getPieChartNode(id);
            
            tx.success();
            
            return result;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Converts CellNode to Cell
     *
     * @param node CellNode 
     * @return Cell
     */
    private Cell convertNodeToCell(CellNode node, String rowIndex, String columnName) {
        if (rowIndex == null) {
            RowNode row = node.getRow();
            rowIndex = row.getRowIndex();
        }        
        
        if (columnName == null) {
            ColumnNode column = node.getColumn();
            columnName = column.getColumnName();
        }
        
        CellID id = new CellID(rowIndex, columnName);
        
        CellFormat cellFormat = new CellFormat();
        
        Integer bgColorB = node.getBackgroundColorB();
        Integer bgColorG = node.getBackgroundColorG();
        Integer bgColorR = node.getBackgroundColorR();
        
        if ((bgColorB != null) && (bgColorG != null) && (bgColorR != null)) {
            Color color = new Color(bgColorR, bgColorR, bgColorB);
            cellFormat.setBackgroundColor(color);
        }
        
        Integer fontColorB = node.getFontColorB();
        Integer fontColorG = node.getFontColorG();
        Integer fontColorR = node.getFontColorR();
        
        if ((fontColorB != null) && (fontColorG != null) && (fontColorR != null)) {
            Color color = new Color(fontColorR, fontColorG, fontColorB);
            cellFormat.setFontColor(color);
        }
        
        cellFormat.setFontName(node.getFontName());
        cellFormat.setFontSize(node.getFontSize());
        cellFormat.setFontStyle(node.getFontStyle());
        cellFormat.setHorizontalAlignment(node.getHorizontalAlignment());
        cellFormat.setVerticalAlignment(node.getVerticalAlignment());
        
        String value = node.getValue();
        if (value == null) {
            value = DEFAULT_VALUE;
        }
        
        String definition = node.getDefinition();
        if (definition == null) {
            definition = DEFAULT_DEFINITION;
        }
        
        Cell result = new Cell(id.getRowIndex(), id.getColumnIndex(), definition, value, cellFormat);
        result.setScriptURI(node.getScriptURI());
        
        return result;
    }
    
    /**
     * Returns RFD Cells of Cell by given ID
     *
     * @param sheet Spreadsheet
     * @param cellID id of Cell
     * @return RFD cells of Cell
     */
    public ArrayList<Cell> getDependentCells(SpreadsheetNode sheet, CellID cellID) {
        CellNode currentNode = getCellNode(sheet, cellID);
        
        Iterator<CellNode> rfdNodes = currentNode.getDependedNodes();
        
        ArrayList<Cell> result = new ArrayList<Cell>(0);
        
        while (rfdNodes.hasNext()) {            
            result.add(convertNodeToCell(rfdNodes.next(), null, null));
        }
        
        return result;
    }
    
    /**
     * Returns RootNode for Spreadsheets
     *
     * @return root node
     */
    //TODO: this method must be rewritten to support specification from comments in #564 to support
    //Spreadsheets with same name in different Ruby and AWE projects
    public RootNode getRootNode() {
        Transaction tx = neoService.beginTx();
        try {
            
            RootNode root = new RootNode(neoService.getReferenceNode());
            tx.success();
            
            return root;
        }
        finally {
            tx.finish();
        }
    }
    
    /**
     * Deletes the Cell from Spreadsheet
     *
     * @param sheet Spreadsheet Node
     * @param id ID of Cell to delete
     * @return is Cell was successfully deleted
     */    
    public boolean deleteCell(SpreadsheetNode sheet, CellID id) {
        CellNode cell = getCellNode(sheet, id);
        
        if (cell != null) {
            //check if there are cells that are dependent on this cell
            if (cell.getDependedNodes().hasNext()) {
                //we can't delete Cell on which other Cell depends
                return false;
            }
            
            //delete Column if it has only this Cell
            ColumnNode column = cell.getColumn();
            if (column.getCellCount() == 1) {
                column.delete();
            }
            
            RowNode row = cell.getRow();
            
            cell.delete();
            
            //delete Row if it has no Cells
            if (row.getCellCount() == 0) {
                row.delete();
            }            
        }
        
        return true;
    }
    
    /**
     * Returns all Cells of Spreadsheet
     *
     * @param sheet Spreadsheet
     * @return all Cells of given Spreadsheet
     */
    
    public List<Cell> getAllCells(SpreadsheetNode sheet) {
        ArrayList<Cell> cellsList = new ArrayList<Cell>(0);
        
        Iterator<RowNode> rows = sheet.getAllRows();
        
        while (rows.hasNext()) {
            RowNode row = rows.next();
            String rowIndex = row.getRowIndex();
            
            Iterator<CellNode> cellsIterator = row.getAllCells();
            
            while (cellsIterator.hasNext()) {
                Cell cell = convertNodeToCell(cellsIterator.next(), rowIndex, null);
                cellsList.add(cell);
            }
        }
        
        return cellsList;
    }
    
    /**
     * Returns all Charts of Spreadsheet
     *
     * @param sheet Spreadsheet
     * @return all Cells of given Spreadsheet
     */
    
    public List<ChartItemNode> getAllChartItems(ChartNode chartNode) {
        ArrayList<ChartItemNode> chartItemsList = new ArrayList<ChartItemNode>(0);
        
        Iterator<ChartItemNode> chartItems = chartNode.getAllChartItems();
        
        while (chartItems.hasNext()) {
        	ChartItemNode chartItem = chartItems.next();
            String chartItemIndex = chartItem.getChartItemIndex();
            
            
            chartItemsList.add(chartItem);
        }
        
        return chartItemsList;
    }
    
    /**
     * Returns all Pie Charts of Spreadsheet
     *
     * @param sheet Spreadsheet
     * @return all Cells of given Spreadsheet
     */
    
    public List<PieChartItemNode> getAllPieChartItems(PieChartNode chartNode) {
        ArrayList<PieChartItemNode> chartItemsList = new ArrayList<PieChartItemNode>(0);
        
        Iterator<PieChartItemNode> chartItems = chartNode.getAllPieChartItems();
        
        while (chartItems.hasNext()) {
        	PieChartItemNode chartItem = chartItems.next();
            String chartItemIndex = chartItem.getPieChartItemIndex();
            
            
            chartItemsList.add(chartItem);
        }
        
        return chartItemsList;
    }

	

	
}
