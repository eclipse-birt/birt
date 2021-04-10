
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public class ClassPathsEditorPage extends DataSourceEditorPage {
	private ClassPathsPageHelper helper;

	public ClassPathsEditorPage() {
		super();
		helper = new ClassPathsPageHelper(this.getHostResourceIdentifiers());
		this.setMessage(ClassPathsPageHelper.DEFAULT_MSG);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.
	 * DataSourceEditorPageCore#createTestConnectionRunnable(org.eclipse.datatools.
	 * connectivity.IConnectionProfile)
	 */
	@Override
	protected Runnable createTestConnectionRunnable(IConnectionProfile profile) {
		return helper.createTestConnectionRunnable(profile);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage
	 * #collectCustomProperties(java.util.Properties)
	 */
	@Override
	public Properties collectCustomProperties(Properties dataSourceProps) {
		return helper.collectCustomProperties(dataSourceProps);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceEditorPage
	 * #createAndInitCustomControl(org.eclipse.swt.widgets.Composite,
	 * java.util.Properties)
	 */
	@Override
	protected void createAndInitCustomControl(Composite parent, Properties profileProps) {
		if (helper.isPageInitialized())
			return;

		helper.setResourceIdentifiers(this.getHostResourceIdentifiers());
		helper.setInitialProperties(profileProps);
		helper.createPageCustomControl(parent);
	}

}
