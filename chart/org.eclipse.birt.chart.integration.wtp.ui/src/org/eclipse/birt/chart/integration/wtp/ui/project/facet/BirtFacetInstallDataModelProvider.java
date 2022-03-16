/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.integration.wtp.ui.project.facet;

import org.eclipse.birt.chart.integration.wtp.ui.ChartWTPUIPlugin;
import org.eclipse.jst.j2ee.web.project.facet.WebFacetInstallDataModelProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

/**
 * Implement DataModel provider for birt project facet
 *
 */
public class BirtFacetInstallDataModelProvider extends WebFacetInstallDataModelProvider {

	/**
	 * Create IDataModel
	 *
	 * @see org.eclipse.wst.common.componentcore.datamodel.FacetInstallDataModelProvider#create()
	 */
	@Override
	public Object create() {
		IDataModel dataModel = (IDataModel) super.create();

		// Add facet id of birt runtime
		dataModel.setProperty("IFacetDataModelProperties.FACET_ID", //$NON-NLS-1$
				ChartWTPUIPlugin.RUNTIME_FACET_ID);

		return dataModel;
	}
}
