/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.filterExtension;

import java.util.List;

import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * 
 */

public interface IODAFilterExprProvider {

	/**
	 * Only returns the filter expression which can be mapped between BIRT defined
	 * and ODA filter extension provider.
	 * 
	 * @param odaDatasetExtensionId    oda datasource extension id.
	 * @param odaDataSourceExtensionId oda dataset extension id.
	 * @return List of IFilterExprDefinition instance.
	 */
	public List<IFilterExprDefinition> getMappedFilterExprDefinitions(String odaDatasetExtensionId,
			String odaDataSourceExtensionId);

	/**
	 * Indicates if currently under the environment support the ODA extension
	 * Filters.
	 * 
	 * @return true if supported, false, if not supported.
	 */
	public boolean supportOdaExtensionFilters();

	/**
	 * Indicates if the given data source and data set support the ODA extension
	 * Filters.
	 * 
	 * @param dataSourceExtId the extension id of the data source
	 * @param dataSetExtId    the extension id of the data set
	 * @return true if supported, false, if not supported.
	 */
	public boolean supportODAFilterPushDown(String dataSourceExtId, String dataSetExtId);

}
