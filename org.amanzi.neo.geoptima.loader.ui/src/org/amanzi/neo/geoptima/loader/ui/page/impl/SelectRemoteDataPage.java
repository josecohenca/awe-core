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

package org.amanzi.neo.geoptima.loader.ui.page.impl;

import org.amanzi.awe.ui.view.widgets.AWEWidgetFactory;
import org.amanzi.awe.ui.view.widgets.TextWidget;
import org.amanzi.awe.ui.view.widgets.TextWidget.ITextChandedListener;
import org.amanzi.neo.geoptima.core.ui.messages.CoreMessages;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Vladislav_Kondratenko
 * @since 1.0.0
 */
public abstract class SelectRemoteDataPage extends AbstractLocationDataPage implements ITextChandedListener {

    private static final int MINIMAL_LABEL_WIDTH = 70;

    private TextWidget hostWidget;

    /**
     * @param pageName
     */
    public SelectRemoteDataPage(final String name) {
        super(name);
    }

    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        hostWidget = AWEWidgetFactory.getFactory().addTextWidget(this, SWT.BORDER, CoreMessages.host, getMainComposite(),
                MINIMAL_LABEL_WIDTH);

        hostWidget.setDefault(getDefaultHost());
    }

    protected abstract String getDefaultHost();

    @Override
    public IWizardPage getPreviousPage() {
        return getWizard().getPages()[0];
    }

    protected int getMinimalLabelWidth() {
        return MINIMAL_LABEL_WIDTH;
    }

    protected String getUrl() {
        return hostWidget.getText();
    }

}
