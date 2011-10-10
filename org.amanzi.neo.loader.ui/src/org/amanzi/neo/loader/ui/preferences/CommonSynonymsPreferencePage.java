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

package org.amanzi.neo.loader.ui.preferences;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.amanzi.neo.loader.core.preferences.DataLoadPreferenceManager;
import org.amanzi.neo.services.NewDatasetService.DatasetTypes;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * @author Vladislav_Kondratenko
 */
public class CommonSynonymsPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

    private final static int DATASET_PARAMETER_COLUMN = 0;
    private List<Map<String, String[]>> newSynonyms = new LinkedList<Map<String, String[]>>();
    private static List<SynonymsContainer> previousSynonymslist = new LinkedList<SynonymsContainer>();
    private final static int SYNONYM_COLUMN = 2;
    private String DEF_TITLE;
    private int numberSelectedColumn;
    private TableItem item;
    private List<SynonymsContainer> synonymContainer = new LinkedList<SynonymsContainer>();
    private List<TextCellEditor> editors = new LinkedList<TextCellEditor>();
    private final SynonymsContainer[] EMPTY_SYNONYMS_CONTAINER = new SynonymsContainer[0];
    private Map<String, String[]> synonymsMap;

    private class SynonymLabelProvider extends ColumnLabelProvider {

        private int columnNumber;

        public SynonymLabelProvider(int columnNumber) {
            this.columnNumber = columnNumber;
        }

        @Override
        public String getText(Object element) {
            if (element instanceof SynonymsContainer)
                switch (columnNumber) {
                case DATASET_PARAMETER_COLUMN:
                    return ((SynonymsContainer)element).getKey();
                case SYNONYM_COLUMN:
                    return ((SynonymsContainer)element).getValue();
                }
            return "";
        }
    }

    private class TableContentProvider implements IStructuredContentProvider {

        @Override
        public void dispose() {
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        }

        @Override
        public Object[] getElements(Object inputElement) {
            if (!synonymContainer.isEmpty()) {
                previousSynonymslist.clear();
                for (SynonymsContainer synonym : synonymContainer) {
                    if (!previousSynonymslist.contains(synonym)) {
                        previousSynonymslist.add(synonym);
                    }
                }
            }

            return previousSynonymslist.toArray(EMPTY_SYNONYMS_CONTAINER);
        }
    }

    private class SynonymsContainer {

        /**
         * 
         */
        public SynonymsContainer(String key, String[] value) {
            super();
            this.key = key;
            this.value = value;
        }

        /**
         * @return Returns the key.
         */
        public String getKey() {
            return key;
        }

        /**
         * @param key The key to set.
         */
        public void setKey(String key) {
            this.key = key;
        }

        /**
         * @param value The value to set.
         */
        public void setValue(String[] value) {
            this.value = value;
        }

        /**
         * @return Returns the value.
         */
        public String getValue() {
            String result = "";
            for (String val : value) {
                result += val + ",";
            }
            return result;
        }

        private String key;
        private String[] value;

    }

    private class TextCellEditorSupport extends EditingSupport {

        private TextCellEditor editor;

        private int columnNumber;

        /**
         * @param viewer
         */
        public TextCellEditorSupport(TableViewer viewer, int columnNumber) {
            super(viewer);
            editor = new TextCellEditor(viewer.getTable());
            editors.add(editor);
            this.columnNumber = columnNumber;
        }

        @Override
        protected CellEditor getCellEditor(Object element) {
            switch (columnNumber) {
            case DATASET_PARAMETER_COLUMN:
                editor.setValue(((SynonymsContainer)element).getKey());
                break;
            case SYNONYM_COLUMN: {
                editor.setValue(((SynonymsContainer)element).getValue());
                break;
            }
            }
            return editor;
        }

        @Override
        protected boolean canEdit(Object element) {
            return true;
        }

        @Override
        protected Object getValue(Object element) {
            switch (columnNumber) {
            case DATASET_PARAMETER_COLUMN: {
                return ((SynonymsContainer)element).getKey();
            }
            case SYNONYM_COLUMN: {
                return ((SynonymsContainer)element).getValue();
            }
            default: {
                return StringUtils.EMPTY;
            }
            }
        }

        @Override
        protected void setValue(Object element, Object value) {
            switch (columnNumber) {
            case DATASET_PARAMETER_COLUMN:
                ((SynonymsContainer)element).setKey(value.toString());
                editor.setValue(((SynonymsContainer)element).getKey());
                break;
            case SYNONYM_COLUMN: {
                ((SynonymsContainer)element).setValue(value.toString().split(","));
                Map<String, String[]> newSyn = new HashMap<String, String[]>();
                newSyn.put(((SynonymsContainer)element).getKey(), ((SynonymsContainer)element).getValue().toString().split(","));
                newSynonyms.add(newSyn);
                editor.setValue(((SynonymsContainer)element).getValue());
                break;
            }
            }
        }
    }

    private Combo cDatasetTypes;

    private TableViewer tableViewer;

    @Override
    protected Control createContents(Composite parent) {
        DEF_TITLE = getTitle();
        setTitle(DEF_TITLE + " : not select Dataset Type");

        setImageDescriptor(null);

        Group mainFrame = new Group(parent, SWT.NONE);

        GridLayout mainLayout = new GridLayout(1, false);
        mainFrame.setLayout(mainLayout);

        Label lDriveType = new Label(mainFrame, SWT.NONE);
        lDriveType.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        lDriveType.setText("Select Dataset Type");

        cDatasetTypes = new Combo(mainFrame, SWT.DROP_DOWN);
        cDatasetTypes.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        cDatasetTypes.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {

                updateTable(DatasetTypes.valueOf(cDatasetTypes.getText()));
                setTitle(DEF_TITLE + " : not select Synonyms Type");
                Table table = tableViewer.getTable();
                table.setVisible(true);
                table.setHeaderVisible(true);
                table.setLinesVisible(true);
                table.setEnabled(true);

            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
        cDatasetTypes.setItems(prepareDatasetType());
        createTable(mainFrame);

        return mainFrame;
    }

    /**
     * @return
     */
    private String[] prepareDatasetType() {
        String[] datasetTypesArray = new String[DatasetTypes.values().length];
        int count = 0;
        for (DatasetTypes type : DatasetTypes.values()) {
            datasetTypesArray[count] = type.name();
            count++;
        }
        return datasetTypesArray;
    }

    private void updateTable(DatasetTypes datasetType) {
        // Collections.sort(currentParameters);
        loadMappings(false, DatasetTypes.valueOf(cDatasetTypes.getText()));
        tableViewer.refresh();
        tableViewer.getTable().setVisible(true);
        tableViewer.getTable().setEnabled(true);
        tableViewer.getTable().setHeaderVisible(true);
        tableViewer.getTable().setLinesVisible(true);
    }

    private void createTable(Composite mainFrame) {
        tableViewer = new TableViewer(mainFrame, SWT.FULL_SELECTION | SWT.BORDER);
        Table table = tableViewer.getTable();
        TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
        TableColumn column = viewerColumn.getColumn();
        column.setText("Real name");
        column.setWidth(100);
        column.setResizable(true);
        viewerColumn.setEditingSupport(new TextCellEditorSupport(tableViewer, DATASET_PARAMETER_COLUMN));
        viewerColumn.setLabelProvider(new SynonymLabelProvider(DATASET_PARAMETER_COLUMN));
        viewerColumn = new TableViewerColumn(tableViewer, SWT.LEFT);
        column = viewerColumn.getColumn();
        column.setText("Synonym");
        column.setWidth(100);
        column.setResizable(true);
        viewerColumn.setLabelProvider(new SynonymLabelProvider(SYNONYM_COLUMN));
        viewerColumn.setEditingSupport(new TextCellEditorSupport(tableViewer, SYNONYM_COLUMN));
        tableViewer.setContentProvider(new TableContentProvider());

        GridData layoutData = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
        layoutData.grabExcessVerticalSpace = true;
        layoutData.grabExcessHorizontalSpace = true;
        tableViewer.getControl().setLayoutData(layoutData);
        tableViewer.setInput("");
        table.setVisible(false);
        table.setHeaderVisible(false);
        table.setLinesVisible(false);
        table.setEnabled(false);
        tableViewer.refresh();
        tableViewer.getControl().addMouseListener(new MouseListener() {
            @Override
            public void mouseUp(MouseEvent e) {
            }

            @Override
            public void mouseDown(MouseEvent e) {
                Table table = (Table)e.widget;
                item = table.getItem(new Point(e.x, e.y));

                if (item == null || item.getText().equals("")) {
                    numberSelectedColumn = -1;
                    return;
                }

                for (int i = 0; i < table.getColumnCount(); i++) {

                    Rectangle bounds = item.getBounds(i);
                    if (bounds.contains(e.x, e.y)) {
                        numberSelectedColumn = i;

                    }
                }

            }

            @Override
            public void mouseDoubleClick(MouseEvent e) {
            }
        });
        hookContextMenu();
    }

    /**
     * Creates a popup menu
     */
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                if (numberSelectedColumn == DATASET_PARAMETER_COLUMN)
                    fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(tableViewer.getControl());
        tableViewer.getControl().setMenu(menu);

    }

    /**
     * @param manager
     */
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(new Action("Delete") {
            @Override
            public void run() {
                tableViewer.refresh();
            }
        });
    }

    protected static DataLoadPreferenceManager preferenceManager;

    private void loadMappings(boolean isDefault, DatasetTypes synonymsType) {
        if (isDefault) {
            DataLoadPreferenceManager.intializeDefault();
        } else {
            synonymContainer.clear();
            if (synonymsMap != null) {
                synonymsMap.clear();
            }
            synonymsMap = preferenceManager.getSynonyms(DatasetTypes.valueOf(cDatasetTypes.getText()));
            for (String key : synonymsMap.keySet()) {
                synonymContainer.add(new SynonymsContainer(key, synonymsMap.get(key)));
            }
        }
    }

    @Override
    public void init(IWorkbench workbench) {
        preferenceManager = new DataLoadPreferenceManager();
    }

    @Override
    protected void performApply() {

        savePreferences();

        super.performApply();
    }

    /**
     *
     */
    private void savePreferences() {
        for (Map<String, String[]> container : newSynonyms) {
            preferenceManager.updateSynonyms(DatasetTypes.valueOf(cDatasetTypes.getText()), container);
        }
    }

    @Override
    public boolean performOk() {
        savePreferences();

        return super.performOk();
    }
}