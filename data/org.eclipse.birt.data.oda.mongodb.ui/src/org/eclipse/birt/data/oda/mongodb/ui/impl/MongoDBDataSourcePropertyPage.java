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

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.design.ui.pages.impl.DefaultDataSourcePropertyPage;
import org.eclipse.swt.widgets.Composite;

public class MongoDBDataSourcePropertyPage extends DefaultDataSourcePropertyPage {

	private MongoDBDataSourcePageHelper pageHelper;

	public MongoDBDataSourcePropertyPage() {
		super();
	}

	protected void createAndInitCustomControl(Composite parent, Properties profileProps) {
		pageHelper = new MongoDBDataSourcePageHelper(this);
		pageHelper.createPageControls(parent);
		pageHelper.initPageControls(profileProps);
	}

	protected void refresh(Properties customConnectionProps) {
		if (pageHelper != null)
			pageHelper.initPageControls(customConnectionProps);

		// enable/disable all controls on page based on the editable session
		// state
		boolean isSessionEditable = isSessionEditable();
		enableAllControls(getControl(), isSessionEditable);
		if (isSessionEditable && pageHelper != null) {
			pageHelper.resetURIEditControlStatus();
			pageHelper.handleKerberosAuthenticationSelection();
		}
	}

	public Properties collectCustomProperties(Properties profileProps) {
		if (pageHelper == null)
			return profileProps;

		if (isSessionEditable()) {
			return pageHelper.collectCustomProperties(profileProps);
		}

		return profileProps;
	}

}
