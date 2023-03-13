/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.odadatasource.wizards;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;

/**
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.datatools.connectivity.oda.design.ui
 *             org.eclipse.datatools.connectivity.oda.design.ui } .
 */

@Deprecated
public abstract class DefaultExtendedDataSourceWizard extends AbstractDataSourceConnectionWizard {

	/**
	 * @param title
	 */
	public DefaultExtendedDataSourceWizard(String title) {
		super(title);
	}

	/**
	 *
	 */
	public DefaultExtendedDataSourceWizard() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.odadatasource.wizards.
	 * AbstractDataSourceConnectionWizard#createDataSource(org.eclipse.birt.model.
	 * api.ReportDesignHandle)
	 */
	@Override
	public DataSourceHandle createDataSource(ModuleHandle handle) {
		// String modelExtension = null;
		String dataSourceType = getConfigurationElement().getAttribute("id"); //$NON-NLS-1$
		// if(Utility.doesDataSourceModelExtensionExist(dataSourceType))
		// {
		// modelExtension = dataSourceType;
		// }
		// OdaDataSourceHandle dsHandle = handle.getDataSources( )
		// .getElementHandle( )
		// .getElementFactory( )
		// .newOdaDataSource( Messages.getString("datasource.new.defaultName"),
		// dataSourceType); //$NON-NLS-1$
		OdaDataSourceHandle dsHandle = DesignElementFactory
				.getInstance(handle.getDataSources().getElementHandle().getModuleHandle())
				.newOdaDataSource(Messages.getString("datasource.new.defaultName"), //$NON-NLS-1$
						dataSourceType);
		return dsHandle;
	}
}
