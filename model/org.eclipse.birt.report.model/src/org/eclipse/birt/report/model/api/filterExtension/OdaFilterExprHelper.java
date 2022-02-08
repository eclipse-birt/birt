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
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;

/**
 * OdaFilterExprHelper
 */

public class OdaFilterExprHelper extends OdaFilterExprHelperImpl {

	/**
	 * Returns the list of IFilterExprDefinition. The list contains both of ODA
	 * extension provider registered filter definitions, and BIRT predefined filter
	 * definitions. If under OS BIRT, the list will only contain the
	 * IFilterExprDefinition instance which represent the BIRT predefined ones.
	 * 
	 * @param odaDatasetExtensionId    oda datasource extension id.
	 * @param odaDataSourceExtensionId oda dataset extension id.
	 * @param filterType               the filter type
	 * @return List of IFilterExprDefinition instance.
	 */

	public static List<IFilterExprDefinition> getMappedFilterExprDefinitions(String dataSetExtId,
			String dataSourceExtId, int filterType) {
		return birtFilterExprDefList;
	}

	/**
	 * Return the IFilterExprDefinition instance based on the passed in BIRT
	 * predefined Filter expression name. The returned IFilterExprDefinition will
	 * provide the information that mapped to a corresponding ODA extension Filter
	 * if there is one. For OS BIRT, the returned IFilterExprDefinition will not
	 * have any map information to the ODA extension filters.
	 * 
	 * @param birtFilterExprId the BIRT predefined fitler expression id.
	 * @param datasetExtId     ODA dataset extension id. Null if is for OS BIRT.
	 * @param datasourceExtId  ODA datasource extension id. Null if is for OS BIRT.
	 * @return Instance of IFilterExprDefinition. IFilterExprDefinition instance
	 *         based on the passed in filter expression id.
	 */
	public static IFilterExprDefinition getFilterExpressionDefn(String birtFilterExprId, String datasetExtId,
			String datasourceExtId) {

		if (!birtPredefinedFilterConstants.contains(birtFilterExprId))
			throw new IllegalArgumentException("The Birt filter expression Id is not valid.");

		List feds = birtFilterExprDefList;
		if (feds.size() > 0) {
			for (int i = 0; i < feds.size(); i++) {
				IFilterExprDefinition fed = (IFilterExprDefinition) feds.get(i);
				if (fed.getBirtFilterExprId().equals(birtFilterExprId))
					return fed;
			}
		}
		return ODAProviderFactory.getInstance().createFilterExprDefinition(birtFilterExprId);
	}

	/**
	 * Indicates if support the ODA extension filter expressions.
	 * 
	 * @return true if support, false if not.
	 */

	public static boolean supportOdaExtensionFilters() {
		return false;
	}

	/**
	 * Indicates if the given data source and data set support the ODA extension
	 * Filters.
	 * 
	 * @param dataSourceExtId the extension id of the data source
	 * @param dataSetExtId    the extension id of the data set
	 * @return true if supported, false, if not supported.
	 */
	public static boolean supportODAFilterPushDown(String dataSourceExtId, String dataSetExtId) {
		return false;
	}
}
