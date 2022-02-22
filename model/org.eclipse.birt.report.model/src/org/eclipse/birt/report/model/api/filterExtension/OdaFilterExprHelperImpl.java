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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;
import org.eclipse.birt.report.model.extension.oda.ODAProviderFactory;

/**
 * OdaFilterExprHelper
 */

class OdaFilterExprHelperImpl {

	/**
	 * The constant for static filter type
	 */
	public static int STATIC_FILTER = 0;
	/**
	 * The constant for dynamic filter type
	 */
	public static int DYNAMIC_FILTER = 1;

	/**
	 * BIRT predefined filter expression id.
	 */
	public static Set<String> birtPredefinedFilterConstants = new HashSet<>();

	/**
	 * The list contains the BIRT predefined filter definitions.
	 */
	protected static List<IFilterExprDefinition> birtFilterExprDefList = new ArrayList<>();

	/**
	 * The flag to initialize the birt predefined filter operators.
	 */
	private static boolean initBirtExpr = false;

	static {
		// for different class loader. There may be multiple thread issues.
		if (!initBirtExpr) {
			synchronized (OdaFilterExprHelperImpl.class) {
				if (!initBirtExpr) {
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_EQ);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_EQ);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_BETWEEN);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_BETWEEN);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_FALSE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_FALSE);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_GE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_GE);

					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_GT);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_GT);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_IN);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_IN);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_LE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_LE);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_LIKE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_LIKE);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_LT);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_LT);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_MATCH);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_MATCH);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NE);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NOT_IN);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NOT_IN);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_NULL);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_NULL);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_TOP_N);
					addToList(DesignChoiceConstants.FILTER_OPERATOR_TOP_N);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT);

					addToList(DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT);
					birtPredefinedFilterConstants.add(DesignChoiceConstants.FILTER_OPERATOR_TRUE);

					addToList(DesignChoiceConstants.FILTER_OPERATOR_TRUE);

					initBirtExpr = true;
				}
			}
		}
	}

	private static void addToList(String key) {
		IFilterExprDefinition fed = ODAProviderFactory.getInstance().createFilterExprDefinition(key);
		birtFilterExprDefList.add(fed);
	}
}
