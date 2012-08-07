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

package org.amanzi.neo.loader.ui.page.widgets.impl;

import java.io.File;
import java.util.List;

import org.amanzi.neo.loader.ui.page.impl.internal.AbstractLoaderPage;
import org.amanzi.neo.loader.ui.page.widgets.impl.ResourceSelectorWidget.IResourceSelectorListener;
import org.amanzi.neo.loader.ui.page.widgets.impl.SelectDriveResourcesWidget.ISelectDriveResourceListener;
import org.amanzi.neo.loader.ui.page.widgets.internal.AbstractPageWidget;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * TODO Purpose of
 * <p>
 * </p>
 * 
 * @author Nikolay Lagutko (nikolay.lagutko@amanzitel.com)
 * @since 1.0.0
 */
public class SelectDriveResourcesWidget extends AbstractPageWidget<Composite, ISelectDriveResourceListener>
implements
IResourceSelectorListener {

	public interface ISelectDriveResourceListener extends AbstractPageWidget.IPageEventListener {

		void onResourcesSelected(List<File> files);

	}

	private ResourceSelectorWidget resourceSelector;

	private final ISelectDriveResourceListener listener;

	/**
	 * @param isEnabled
	 * @param parent
	 * @param listener
	 * @param projectModelProvider
	 */
	protected SelectDriveResourcesWidget(final Composite parent, final ISelectDriveResourceListener listener) {
		super(true, parent, listener, null);
		this.listener = listener;
	}

	@Override
	protected Composite createWidget(final Composite parent, final int style) {
		Composite panel = new Composite(parent, style);
		panel.setLayoutData(getGroupLayoutData());
		panel.setLayout(new GridLayout(1, false));

		resourceSelector = WizardFactory.getInstance().addDirectorySelector(getPanel(panel), this);
		WizardFactory.getInstance().addDriveDataFileSelector(getPanel(panel), listener);

		return panel;
	}

	private Composite getPanel(final Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		panel.setLayout(new GridLayout(3, false));

		return panel;
	}

	protected Object getGroupLayoutData() {
		return new GridData(SWT.FILL, SWT.CENTER, true, false, AbstractLoaderPage.NUMBER_OF_COLUMNS, 1);
	}

	@Override
	protected int getStyle() {
		return SWT.NONE;
	}

	@Override
	public void onResourceChanged() {
		resourceSelector.getFileName();
	}

}
