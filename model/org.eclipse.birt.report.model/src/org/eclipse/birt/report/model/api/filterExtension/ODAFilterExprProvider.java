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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * ODAFilterExprProvider
 */
public class ODAFilterExprProvider implements IODAFilterExprProvider {
	private static ODAFilterExprProvider provider = new ODAFilterExprProvider();

	/**
	 * Returns the singleton instance.
	 *
	 * @return the instance
	 */

	public static ODAFilterExprProvider getInstance() {
		return provider;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IODAFilterExprProvider#getMappedFilterExprDefinitions(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public List<IFilterExprDefinition> getMappedFilterExprDefinitions(String dataSetExtId, String dataSourceExtId) {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IODAFilterExprProvider#supportExtensionFilters()
	 */
	@Override
	public boolean supportOdaExtensionFilters() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supprtsODAFilterPushDown(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean supportODAFilterPushDown(String dataSourceExtId, String dataSetExtId) {
		return false;
	}

}
