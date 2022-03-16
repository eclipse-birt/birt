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

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;

/**
 * @deprecated As of BIRT 2.1, replaced by
 *             {@link org.eclipse.datatools.connectivity.oda.design.ui
 *             org.eclipse.datatools.connectivity.oda.design.ui } .
 *
 * @version $Revision: 1.2 $ $Date: 2006/03/30 04:36:07 $
 */

@Deprecated
public abstract class DefaultExtendedDataSetWizard extends AbstractDataSetWizard {

	/**
	 * @param title
	 */
	public DefaultExtendedDataSetWizard(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 */
	public DefaultExtendedDataSetWizard() {
		super();
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.ui.odadatasource.wizards.
	 * AbstractDataSetWizard#createDataSet(org.eclipse.birt.model.api.
	 * ReportDesignHandle)
	 */
	@Override
	public DataSetHandle createDataSet(ModuleHandle handle) {
		String dataSetType = getConfigurationElement().getAttribute("id"); //$NON-NLS-1$

		// TODO: "New data_set_default_display_name" will be better
		String newName = "New Data Set"; //$NON-NLS-1$
		// OdaDataSetHandle dataSetHandle = handle.getDataSets( )
		// .getElementHandle( )
		// .getElementFactory( )
		// .newOdaDataSet( newName, dataSetType ); //$NON-NLS-1$
		OdaDataSetHandle dataSetHandle = DesignElementFactory
				.getInstance(handle.getDataSets().getElementHandle().getModuleHandle())
				.newOdaDataSet(newName, dataSetType);
		return dataSetHandle;
	}
}
