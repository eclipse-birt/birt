/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
 *******************************************************************************/
package org.eclipse.birt.data.oda.pojo.ui.impl.dialogs;

import java.util.Properties;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSourceWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;

/**
 * POJO DataSourceWizardPage to define the data source class paths
 *
 */

public class ClassPathsWizardPage extends DataSourceWizardPage {
	private Properties properties;
	private ClassPathsPageHelper helper;

	public ClassPathsWizardPage(String pageName) {
		super(pageName);
		this.setMessage(ClassPathsPageHelper.DEFAULT_MSG);
		helper = new ClassPathsPageHelper(this.getHostResourceIdentifiers());
		helper.setWizardPage(this);
	}

	public ClassPathsWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
		this.setMessage(ClassPathsPageHelper.DEFAULT_MSG);
		helper = new ClassPathsPageHelper(this.getHostResourceIdentifiers());
		helper.setWizardPage(this);
	}

	@Override
	public Properties collectCustomProperties() {
		if (properties == null) {
			properties = new Properties();
		}

		return helper.collectCustomProperties(properties);
	}

	@Override
	public void setInitialProperties(Properties dataSourceProps) {
		properties = dataSourceProps;
		if (properties == null) {
			properties = new Properties();
		}

		if (helper == null) {
			return; // ignore, wait till createPageCustomControl to initialize
		}
		helper.setInitialProperties(properties);
	}

	@Override
	public void refresh() {
		if (helper != null) {
			helper.refresh();
		}
	}

	@Override
	protected Runnable createTestConnectionRunnable(final IConnectionProfile profile) {
		return helper.createTestConnectionRunnable(profile);
	}

	@Override
	public void createPageCustomControl(Composite parent) {
		helper.setResourceIdentifiers(this.getHostResourceIdentifiers());
		helper.createPageCustomControl(parent);
	}

}
