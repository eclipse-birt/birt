/*
 *************************************************************************
 * Copyright (c) 2009, 2010 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.ui.internal.datasource;

import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ui.profile.db.wizards.NewDbDataSourceWizard;

/**
 * Extends ODA UI framework wizard class for this custom ODA designer.
 *
 * @since 2.5.2
 */
public class CustomDbDataSourceWizard extends NewDbDataSourceWizard {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.datatools.connectivity.oda.design.internal.ui.profile.db.
	 * NewDbDataSourceWizardBase#setDataSourceDesignProperties(org.eclipse.datatools
	 * .connectivity.oda.design.DataSourceDesign, java.util.Properties)
	 */
	@Override
	protected void setDataSourceDesignProperties(DataSourceDesign newDesign, Properties customPropertyValues)
			throws OdaException {
		Properties dataSourceProps = DbProfilePropertyProvider.adaptToDataSourceProperties(customPropertyValues);

		super.setDataSourceDesignProperties(newDesign, dataSourceProps);
	}

}
