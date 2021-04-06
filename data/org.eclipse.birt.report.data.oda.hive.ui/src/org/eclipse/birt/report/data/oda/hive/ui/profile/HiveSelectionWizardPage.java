/*
 *************************************************************************
 * Copyright (c) 2005, 2012 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.hive.ui.profile;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.manifest.UIManifestExplorer;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * Hive data source wizard page implementation class.
 */
public class HiveSelectionWizardPage extends DataSourceWizardPage {
	private HiveSelectionPageHelper m_pageHelper;
	private Properties m_folderProperties;

	public HiveSelectionWizardPage(String pageName) {
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
		if (m_pageHelper == null) {
			String odaDesignerId = UIManifestExplorer.getInstance().getOdaDesignerId(this.getOdaDataSourceId());
			m_pageHelper = new HiveSelectionPageHelper(this, odaDesignerId);
		}

		m_pageHelper.createCustomControl(parent);
		// in case init was called before create
		m_pageHelper.initCustomControl(m_folderProperties);
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

	@Override
	public void refresh() {
		// enable/disable all controls on page based on the session editable state
		enableAllControls(getControl(), isSessionEditable());
		if (m_pageHelper != null)
			m_pageHelper.resetTestButton();
	}

}