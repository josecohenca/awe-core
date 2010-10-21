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

package org.amanzi.neo.loader.ui.wizards;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.amanzi.neo.core.INeoConstants;
import org.amanzi.neo.core.NeoCorePlugin;
import org.amanzi.neo.core.service.NeoServiceProvider;
import org.amanzi.neo.loader.core.CommonConfigData;
import org.amanzi.neo.loader.ui.NeoLoaderPluginMessages;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Traverser;

/**
 * <p>
 * Main page of
 * </p>
 * 
 * @author tsinkel_a
 * @since 1.0.0
 */
public class LoadDatasetMainPage extends LoaderPage<CommonConfigData> {
    /** String ASC_PAT_FILE field */
    private static final String ASC_PAT_FILE = ".*_(\\d{6})_.*";
    private static final String FMT_PAT_FILE = ".*(\\d{4}-\\d{2}-\\d{2}).*";
    private static final String CSV_PAT_FILE = ".*(\\d{2}/\\d{2}/\\d{4}).*";

    /*
     * Minimum height of Shell
     */
    private static final int MINIMUM_HEIGHT = 400;

    /*
     * Minimum width of Shell
     */
    private static final int MINIMUM_WIDTH = 600;

    /*
     * Dataset field width
     */
    private static final int DATASET_WIDTH = 150;

    /*
     * Layout for One column and Fixed Width
     */
    private final static GridLayout layoutOneColumnNotFixedWidth = new GridLayout(1, false);

    private static final int MAX_NEMO_LINE_READ = 50;

    /*
     * Shell of this Dialog
     */
    private Shell dialogShell;

    /*
     * Button for FileDialog
     */
    private Button browseDialogButton;

    /*
     * Button for adding to load files
     */
    private Button addFilesToLoaded;

    /*
     * Button for removing from load files
     */
    private Button removeFilesFromLoaded;

    /*
     * List for files to choose
     */
    private List folderFilesList;

    /*
     * List for files to load
     */
    private List filesToLoadList;

    /*
     * Cancel button
     */
    private Button cancelButton;

    /*
     * Load button
     */
    private Button loadButton;
    /**
     * file data
     */
    private Calendar workData = null;
    private boolean applyToAll = false;
    /*
     * Maps for storing name of file and path to file
     */
    private final HashMap<String, String> folderFiles = new HashMap<String, String>();
    private final Map<String, String> loadedFiles = new LinkedHashMap<String, String>();

    private Button addAllFilesToLoaded;

    private Button removeAllFilesFromLoaded;

    private Combo cDataset;
    private String datasetName;
    /*
     * Default directory for file dialogs
     */
    private static String defaultDirectory = null;
    /**
     * wizard page if tems dialog was created from import wizard page
     */
    private WizardPage wizardPage = null;

    private final LinkedHashMap<String, Node> dataset = new LinkedHashMap<String, Node>();

    private Label ldataset;
    private boolean addToSelect = false;

    private Node rootNode;
    private Combo cLoaders;

    public LoadDatasetMainPage() {
        super("mainDatasetPage");
        setTitle(NeoLoaderPluginMessages.TemsImportWizard_PAGE_DESCR);
        rootNode = null;
    }

    @Override
    public void createControl(Composite parent) {
        createControlForDialog(parent);

        setControl(parent);
    }

    public void createControlForDialog(Composite parent) {
        GridLayout layout = layoutOneColumnNotFixedWidth;
        parent.setLayout(layout);
        parent.setLayoutData(new GridData(SWT.FILL));
        loadButton = new Button(parent, SWT.NONE);
        cancelButton = loadButton;
        createSelectFileGroup(parent);
        cancelButton.moveBelow(null);
        cancelButton.setVisible(false);
    }

    /**
     * Creates group for selecting files to load
     * 
     * @param parent
     */

    private void createSelectFileGroup(Composite parent) {
        createDatasetRow(parent);
        Group group = new Group(parent, SWT.NONE);
        group.setLayout(new GridLayout(3, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        createFolderSelectionComposite(group);
        createManipulationComposite(group);
        createFileToLoadComposite(group);
        Composite panel = new Composite(group, SWT.NONE);
        panel.setLayout(new FormLayout());
        GridData data = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        panel.setLayoutData(data);

        Label ldataset = new Label(panel, SWT.NONE);
        ldataset.setText(NeoLoaderPluginMessages.NetworkSiteImportWizard_DATA_TYPE);
        cLoaders = new Combo(panel, SWT.NONE);
        FormData dLabel = new FormData();
        dLabel.left = new FormAttachment(0, 5);
        dLabel.top = new FormAttachment(cDataset, 5, SWT.CENTER);
        ldataset.setLayoutData(dLabel);

        FormData dCombo = new FormData();
        dCombo.left = new FormAttachment(ldataset, 5);
        dCombo.top = new FormAttachment(0, 2);
        dCombo.width = DATASET_WIDTH;
        cLoaders.setLayoutData(dCombo);
    }

    /**
     * Creates List for files to load
     * 
     * @param parent
     */

    private void createFileToLoadComposite(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(layoutOneColumnNotFixedWidth);
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        filesToLoadList = createSelectionList(panel, NeoLoaderPluginMessages.DriveDialog_FilesToLoadListLabel);
    }

    /**
     * Creates a List with Label
     * 
     * @param parent parent Composite
     * @param label test of Label
     * @return created List
     */

    private List createSelectionList(Composite parent, String label) {
        Label listLabel = new Label(parent, SWT.NONE);
        listLabel.setText(label);
        listLabel.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false));

        List list = new List(parent, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        gridData.minimumWidth = 150;
        list.setLayoutData(gridData);

        return list;
    }

    /**
     * Creates Buttons for manipulations
     * 
     * @param parent
     */

    private void createManipulationComposite(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(layoutOneColumnNotFixedWidth);
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

        Composite choosePanel = new Composite(panel, SWT.NONE);
        choosePanel.setLayout(layoutOneColumnNotFixedWidth);
        choosePanel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        browseDialogButton = createChooseButton(choosePanel, NeoLoaderPluginMessages.DriveDialog_BrowseButtonText, SWT.TOP);

        Composite actionPanel = new Composite(panel, SWT.NONE);
        actionPanel.setLayout(layoutOneColumnNotFixedWidth);
        actionPanel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

        addFilesToLoaded = createChooseButton(actionPanel, NeoLoaderPluginMessages.DriveDialog_AddButtonText, SWT.CENTER);
        addAllFilesToLoaded = createChooseButton(actionPanel, NeoLoaderPluginMessages.DriveDialog_AddAllButtonText, SWT.CENTER);
        removeFilesFromLoaded = createChooseButton(actionPanel, NeoLoaderPluginMessages.DriveDialog_RemoveButtonText, SWT.CENTER);
        removeAllFilesFromLoaded = createChooseButton(actionPanel, NeoLoaderPluginMessages.DriveDialog_RemoveAllButtonText, SWT.CENTER);
    }

    /**
     * Create button for manipulation
     * 
     * @param parent parent Composite
     * @param label label of Button
     * @param position position of Button
     * @return created Button
     */

    private Button createChooseButton(Composite parent, String label, int position) {
        Button button = new Button(parent, SWT.NONE);
        button.setText(label);
        button.setLayoutData(new GridData(SWT.FILL, position, true, true));

        return button;
    }

    /**
     * Creates group for selecting
     * 
     * @param parent
     */
    private void createDatasetRow(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(new FormLayout());
        GridData data = new GridData(SWT.FILL, SWT.BOTTOM, true, false);
        panel.setLayoutData(data);

        ldataset = new Label(panel, SWT.NONE);
        ldataset.setText(NeoLoaderPluginMessages.DriveDialog_DatasetLabel);
        cDataset = new Combo(panel, SWT.NONE);
        FormData dLabel = new FormData();
        dLabel.left = new FormAttachment(0, 5);
        dLabel.top = new FormAttachment(cDataset, 5, SWT.CENTER);
        ldataset.setLayoutData(dLabel);

        FormData dCombo = new FormData();
        dCombo.left = new FormAttachment(ldataset, 5);
        dCombo.top = new FormAttachment(0, 2);
        dCombo.width = DATASET_WIDTH;
        cDataset.setLayoutData(dCombo);

        // TODO: Check if the following line is needed
        // Transaction tx = NeoServiceProvider.getProvider().getService().beginTx();
        Traverser allDatasetTraverser = NeoCorePlugin.getDefault().getProjectService().getAllDatasetTraverser(NeoServiceProvider.getProvider().getService().getReferenceNode());
        for (Node node : allDatasetTraverser) {
            dataset.put((String)node.getProperty(INeoConstants.PROPERTY_NAME_NAME), node);
        }
        String[] items = dataset.keySet().toArray(new String[0]);
        Arrays.sort(items);
        cDataset.setItems(items);
        cDataset.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                changeDatasetSelection();
            }
        });
        cDataset.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                changeDatasetSelection();
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                widgetSelected(e);
            }
        });
    }

    /**
     * Creates List for choosing Files
     * 
     * @param parent
     */

    private void createFolderSelectionComposite(Composite parent) {
        Composite panel = new Composite(parent, SWT.NONE);
        panel.setLayout(layoutOneColumnNotFixedWidth);
        panel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        folderFilesList = createSelectionList(panel, NeoLoaderPluginMessages.DriveDialog_FilesToChooseListLabel);
    }

    @Override
    protected boolean validateConfigData(CommonConfigData configurationData) {
        return false;
    }

    /**
     * change dataset selection
     */
    protected void changeDatasetSelection() {
        try {
            Node datasetNode = dataset.get(cDataset.getText());
            if (datasetNode != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
