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
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

public class HivePropertyPage extends DataSourceEditorPage {
	private HiveSelectionPageHelper m_pageHelper;

	public HivePropertyPage() {
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
// do not need to rewrite this method
//	protected DataSourceDesign collectDataSourceDesign( DataSourceDesign design )
//	{
////		design.setOdaExtensionId( HiveDriver.DATA_SOURCE_ID );
////		try
////		{
////			design.setPublicProperties( DesignSessionUtil.createDataSourcePublicProperties( HiveDriver.DATA_SOURCE_ID,
////					collectProperties( ) ) );
////		}
////		catch ( OdaException e )
////		{
////		}
//		return super.collectDataSourceDesign( design );
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.profile.wizards.
	 * DataSourceEditorPage#createAndInitCustomControl(org.eclipse.swt.widgets.
	 * Composite, java.util.Properties)
	 */
	protected void createAndInitCustomControl(Composite parent, Properties profileProps) {
		if (m_pageHelper == null) {
			String odaDesignerId = UIManifestExplorer.getInstance().getOdaDesignerId(this.getOdaDataSourceId());
			m_pageHelper = new HiveSelectionPageHelper(this, odaDesignerId);
		}

		m_pageHelper.createCustomControl(parent);
		m_pageHelper.initCustomControl(profileProps);
		this.setPingButtonVisible(false);
	}

	@Override
	protected void refresh(Properties customConnectionProps) {
		if (m_pageHelper != null)
			m_pageHelper.initCustomControl(customConnectionProps);

		// enable/disable all controls on page based on the session editable state
		enableAllControls(getControl(), isSessionEditable());
		if (m_pageHelper != null)
			m_pageHelper.resetTestButton();
	}

}
