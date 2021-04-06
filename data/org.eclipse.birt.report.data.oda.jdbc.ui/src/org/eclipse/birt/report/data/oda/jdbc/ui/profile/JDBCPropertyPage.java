/*
 *************************************************************************
 * Copyright (c) 2005, 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.ui.util.bidi.profile.BidiSettingsSupport;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

/**
 * jdbc property page, used in editor dialog
 * 
 * @author Administrator
 *
 */
public class JDBCPropertyPage extends DataSourceEditorPage {

	private JDBCSelectionPageHelper m_pageHelper;

	// bidi_hcg: Bidi Object containing Bidi formats definitions
	private BidiSettingsSupport bidiSupport = new BidiSettingsSupport();

	public BidiSettingsSupport getBidiSupport() {
		return bidiSupport;
	}

	public JDBCPropertyPage() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceEditorPage#collectCustomProperties(java.util.Properties)
	 */
	public Properties collectCustomProperties(Properties profileProps) {
		if (m_pageHelper == null)
			return profileProps;

		return m_pageHelper.collectCustomProperties(profileProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceEditorPage#createAndInitCustomControl(org.eclipse.swt.widgets.
	 * Composite, java.util.Properties)
	 */
	protected void createAndInitCustomControl(Composite parent, Properties profileProps) {
		if (m_pageHelper == null)
			m_pageHelper = new JDBCSelectionPageHelper(this);

		Composite content = m_pageHelper.createCustomControl(parent);
		// bidi_hcg: add "Bidi Settings..." button to "Edit Data Source dialog"
		m_pageHelper.addBidiSettingsButton(content, profileProps);
		this.setPingButtonVisible(false);
		m_pageHelper.initCustomControl(profileProps);
		m_pageHelper.setResourceIdentifier(this.getHostResourceIdentifiers());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSourceEditorPageCore#refresh()
	 */
	public void refresh(Properties customConnectionProps) {
		m_pageHelper.initCustomControl(customConnectionProps);

		// enable/disable all controls on page in respect of the editable session state
		enableAllControls(getControl(), isSessionEditable());
		m_pageHelper.resetTestAndMngButton();
	}

}
