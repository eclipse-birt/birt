/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public List<IFilterExprDefinition> getMappedFilterExprDefinitions(String dataSetExtId, String dataSourceExtId) {
		return Collections.emptyList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.model.api.filterExtension.interfaces.
	 * IODAFilterExprProvider#supportExtensionFilters()
	 */
	public boolean supportOdaExtensionFilters() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.filterExtension.IODAFilterExprProvider
	 * #supprtsODAFilterPushDown(java.lang.String, java.lang.String)
	 */
	public boolean supportODAFilterPushDown(String dataSourceExtId, String dataSetExtId) {
		return false;
	}

}
