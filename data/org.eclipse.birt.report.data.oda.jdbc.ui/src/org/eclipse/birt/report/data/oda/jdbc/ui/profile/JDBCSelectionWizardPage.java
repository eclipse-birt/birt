/*
 *************************************************************************
 * Copyright (c) 2005, 2006 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile.BidiSettingsSupport;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * jdbc selection page
 * 
 */
public class JDBCSelectionWizardPage extends DataSourceWizardPage {
	/**
	 * helper object
	 */
	private JDBCSelectionPageHelper m_pageHelper;
	private Properties m_folderProperties;
	private BidiSettingsSupport bidiSupport = new BidiSettingsSupport(); // bidi_hcg

	public BidiSettingsSupport getBidiSupport() {
		return bidiSupport;
	}

	public JDBCSelectionWizardPage(String pageName) {
		super(pageName);
		// page title is specified in extension manifest
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceWizardPage#createPageCustomControl(org.eclipse.swt.widgets.
	 * Composite)
	 */
	public void createPageCustomControl(Composite parent) {
		if (m_pageHelper == null)
			m_pageHelper = new JDBCSelectionPageHelper(this);
		Composite content = m_pageHelper.createCustomControl(parent);
		m_pageHelper.initCustomControl(m_folderProperties); // in case init was called before create
		// bidi_hcg: add "Bidi Settings..." button to "Edit Data Source dialog"
		m_pageHelper.addBidiSettingsButton(content, m_folderProperties);
		m_pageHelper.setResourceIdentifier(this.getHostResourceIdentifiers());
		this.setPingButtonVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceWizardPage#initPageCustomControl(java.util.Properties)
	 */
	public void setInitialProperties(Properties dataSourceProps) {
		m_folderProperties = dataSourceProps;
		if (m_pageHelper == null)
			return; // ignore, wait till createPageCustomControl to initialize
		m_pageHelper.initCustomControl(m_folderProperties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceWizardPage#collectCustomProperties()
	 */
	public Properties collectCustomProperties() {
		if (m_pageHelper != null)
			return m_pageHelper.collectCustomProperties(m_folderProperties);

		return (m_folderProperties != null) ? m_folderProperties : new Properties();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		getControl().setFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage
	 * #refresh()
	 */
	public void refresh() {
		// enable/disable all controls on page in respect of the editable session state
		enableAllControls(getControl(), isSessionEditable());
		if (m_pageHelper != null) {
			m_pageHelper.resetTestAndMngButton();
		}
	}

}
