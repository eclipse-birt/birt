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

package org.eclipse.birt.report.data.oda.sampledb.ui.profile;

import java.util.Properties;

import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

public class SampleDbPropertyPage extends DataSourceEditorPage {

	private SampleDbSelectionPageHelper m_pageHelper;

	public SampleDbPropertyPage() {
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

	@Override
	protected DataSourceDesign collectDataSourceDesign(DataSourceDesign design) {
		design.setOdaExtensionId(OdaJdbcDriver.Constants.DATA_SOURCE_ID);
		try {
			design.setPublicProperties(DesignSessionUtil
					.createDataSourcePublicProperties(OdaJdbcDriver.Constants.DATA_SOURCE_ID, collectProperties()));
		} catch (OdaException e) {
		}
		return super.collectDataSourceDesign(design);
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
			m_pageHelper = new SampleDbSelectionPageHelper(this);

		m_pageHelper.createCustomControl(parent);
		m_pageHelper.initCustomControl(profileProps);
	}

}
