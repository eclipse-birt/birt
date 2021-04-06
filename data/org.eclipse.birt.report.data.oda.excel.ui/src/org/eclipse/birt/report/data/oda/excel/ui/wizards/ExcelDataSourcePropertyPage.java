/*******************************************************************************
  * Copyright (c) 2012 Megha Nidhi Dahal and others.
  * All rights reserved. This program and the accompanying materials
  * are made available under the terms of the Eclipse Public License v1.0
  * which accompanies this distribution, and is available at
  * http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *    Megha Nidhi Dahal - initial API and implementation and/or initial documentation
  *    Actuate Corporation - added support of relative file path
  *******************************************************************************/

package org.eclipse.birt.report.data.oda.excel.ui.wizards;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

public class ExcelDataSourcePropertyPage extends DataSourceEditorPage {

	private ExcelDataSourcePageHelper pageHelper;

	public ExcelDataSourcePropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage
	 * #collectCustomProperties(java.util.Properties)
	 */
	public Properties collectCustomProperties(Properties profileProps) {
		/*
		 * Optionally assigns a custom designer state, for inclusion in the ODA design
		 * session response, using setResponseDesignerState( DesignerState customState
		 * );
		 */

		if (pageHelper == null)
			return profileProps;

		return pageHelper.collectCustomProperties(profileProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage
	 * #createAndInitCustomControl(org.eclipse.swt.widgets.Composite,
	 * java.util.Properties)
	 */
	protected void createAndInitCustomControl(Composite parent, Properties profileProps) {
		if (pageHelper == null)
			pageHelper = new ExcelDataSourcePageHelper(this);
		pageHelper.setResourceIdentifiers(getHostResourceIdentifiers());
		pageHelper.createCustomControl(parent);

		/*
		 * Optionally hides the Test Connection button, using setPingButtonVisible(
		 * false );
		 */

		/*
		 * Optionally restores the state of a previous design session. Obtains designer
		 * state, using getInitializationDesignerState();
		 */

		pageHelper.initCustomControl(profileProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage
	 * #refresh(java.util.Properties)
	 */
	protected void refresh(Properties customConnectionProps) {
		if (pageHelper != null)
			pageHelper.initCustomControl(customConnectionProps);

		// enable/disable all controls on page in respect of the editable session state
		enableAllControls(getControl(), isSessionEditable());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSourceEditorPageCore#createTestConnectionRunnable(org.eclipse.datatools.
	 * connectivity.IConnectionProfile)
	 */
	protected Runnable createTestConnectionRunnable(IConnectionProfile profile) {
		return pageHelper.createTestConnectionRunnable(profile);
	}

}
