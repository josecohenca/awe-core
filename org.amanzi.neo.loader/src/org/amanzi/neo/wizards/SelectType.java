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

package org.amanzi.neo.wizards;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.amanzi.neo.core.enums.INodeType;
import org.amanzi.neo.core.enums.NodeTypes;
import org.amanzi.neo.core.utils.AbstractDialog;
import org.amanzi.neo.services.DatasetService;
import org.amanzi.neo.services.NeoServiceFactory;
import org.apache.commons.lang.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * <p>
 * Select new type
 * </p>.
 *
 * @author TsAr
 * @since 1.0.0
 */
public class SelectType extends AbstractDialog<String> {
    
    /** The Constant MIN_FIELD_WIDTH. */
    private static final int MIN_FIELD_WIDTH = 50;
    
    /** The shell. */
    private Shell shell;
    
    /** The t new node. */
    private Combo tNewNode;
    
    /** The b ok. */
    private Button bOk;
    
    /** The b cancel. */
    private Button bCancel;
    
    /** The possible types. */
    private final Map<String,INodeType> possibleTypes=new LinkedHashMap<String, INodeType>();
    
    /** The service. */
    private DatasetService service;
    
    /** The node type. */
    protected String nodeType="";

    /**
     * Instantiates a new select type.
     *
     * @param parent the parent
     * @param title the title
     * @param possibleTypes the possible types
     */
    public SelectType(Shell parent, String title,List<INodeType> possibleTypes) {
        super(parent, title);
        this.possibleTypes.clear();
        service=NeoServiceFactory.getInstance().getDatasetService();
        
        for (INodeType type:possibleTypes){
            this.possibleTypes.put(type.getId(),type);
        }
    }

    /**
     * Creates the contents.
     *
     * @param shell the shell
     */
    @Override
    protected void createContents(Shell shell) {
        this.shell = shell;

        shell.setImage(NodeTypes.DATASET.getImage());
        shell.setLayout(new GridLayout(2, true));

        Label label = new Label(shell, SWT.NONE);
        GridData layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        layoutData.minimumWidth = MIN_FIELD_WIDTH;

        label.setText("Node type");
        label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
        tNewNode = new Combo(shell, SWT.BORDER);
        layoutData = new GridData(SWT.FILL, SWT.CENTER, true, false);
        layoutData.minimumWidth = MIN_FIELD_WIDTH;
        tNewNode.setLayoutData(layoutData);
        tNewNode.setItems(getPossibleItems());
        bOk = new Button(shell, SWT.PUSH);
        bOk.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        bOk.setText("OK");
        bCancel = new Button(shell, SWT.PUSH);
        bCancel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
        bCancel.setText("Cancel");
        bOk.setEnabled(false);
        addListeners();
    }


    /**
     * Adds the listeners.
     */
    private void addListeners() {
        bOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                status = nodeType;
                shell.close();
            }
        });
        bCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                status = null;
                shell.close();
            }
        });
        tNewNode.addModifyListener(new ModifyListener() {
            
            @Override
            public void modifyText(ModifyEvent e) {
                nodeType=tNewNode.getText().trim();
               checkTypeName(); 
            }
        });
    }


    /**
     * Check type name.
     */
    protected void checkTypeName() {
        if (StringUtils.isEmpty(nodeType)){
            bOk.setEnabled(false);
        }
        if (possibleTypes.keySet().contains(tNewNode.getText())){
            bOk.setEnabled(true);
            return;
        }
        bOk.setEnabled(service.getNodeType(tNewNode.getText())==null);
    }


    /**
     * Gets the possible items.
     *
     * @return the possible items
     */
    private String[] getPossibleItems() {
        return possibleTypes.keySet().toArray(new String[0]);
    }

}
