/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Represents the utility class to help compute level of operator.
 */

public class OperatorUtil {

	/**
	 * If operater is null, not null, true , false , then level is zero. Delete
	 * value1 and value2 in design file
	 */
	public static final int OPERATOR_LEVEL_ZERO = 0;

	/**
	 * If operater is >, >= , = , < , <= , like , top , bottom , any , then level is
	 * one. Set value1, delete value2 in design file
	 */
	public static final int OPERATOR_LEVEL_ONE = 1;

	/**
	 * If operater is between , not between , then level is two. Set value1 and
	 * value2 in design file.
	 */
	public static final int OPERATOR_LEVEL_TWO = 2;

	/**
	 * If operator is not in the choice list, then level is fail.
	 */
	public static final int OPERATOR_LEVEL_NOT_EXIST = -1;

	/**
	 * Computes the level of operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, If
	 * operator is one of follows:
	 * <ul>
	 * <li><code>MAP_OPERATOR_NULL</code>
	 * <li><code>MAP_OPERATOR_NOT_NULL</code>
	 * <li><code>MAP_OPERATOR_TRUE</code>
	 * <li><code>MAP_OPERATOR_FALSE</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_ZERO<code>;
	 * 
	 * If operator is one of follows:
	 * <ul>
	 * <li><code>MAP_OPERATOR_EQ</code>
	 * <li><code>MAP_OPERATOR_NE</code>
	 * <li><code>MAP_OPERATOR_LT</code>
	 * <li><code>MAP_OPERATOR_LE</code>
	 * <li><code>MAP_OPERATOR_GE</code>
	 * <li><code>MAP_OPERATOR_GT</code>
	 * <li><code>MAP_OPERATOR_LIKE</code>
	 * <li><code>MAP_OPERATOR_TOP_N</code>
	 * <li><code>MAP_OPERATOR_BOTTOM_N</code>
	 * <li><code>MAP_OPERATOR_NOT_LIKE</code>
	 * <li><code>MAP_OPERATOR_NOT_MATCH</code>
	 * <li><code>MAP_OPERATOR_ANY</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_ONE<code>;
	 * 
	 * If operator is one of follows:
	 * <ul>
	 * <li><code>MAP_OPERATOR_BETWEEN</code>
	 * <li><code>MAP_OPERATOR_NOT_BETWEEN</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_TWO<code>;
	 * 
	 * If operator is not in the choice list, return
	 * <code>OPERATOR_LEVEL_NOT_EXIST<code>.
	 * 
	 * @param operator the operator to compute.
	 * @return level of operator.
	 *
	 */
	public static int computeStyleRuleOperatorLevel(String operator) {
		if ((DesignChoiceConstants.MAP_OPERATOR_TRUE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_FALSE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NULL.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NOT_NULL.equals(operator))) {
			return OPERATOR_LEVEL_ZERO;
		} else if ((DesignChoiceConstants.MAP_OPERATOR_EQ.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_GE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_LT.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_LE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_GT.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_LIKE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_TOP_N.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_BOTTOM_N.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NOT_LIKE.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NOT_MATCH.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_ANY.equals(operator))) {
			return OPERATOR_LEVEL_ONE;
		} else if ((DesignChoiceConstants.MAP_OPERATOR_BETWEEN.equals(operator))
				|| (DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN.equals(operator))) {
			return OPERATOR_LEVEL_TWO;
		}
		return OPERATOR_LEVEL_NOT_EXIST;
	}

	/**
	 * Computes the level of operator. The allowed values are defined in
	 * {@link org.eclipse.birt.report.model.api.elements.DesignChoiceConstants}, If
	 * operator is one of follows:
	 * <ul>
	 * <li><code>FILTER_OPERATOR_NULL</code>
	 * <li><code>FILTER_OPERATOR_NOT_NULL</code>
	 * <li><code>FILTER_OPERATOR_TRUE</code>
	 * <li><code>FILTER_OPERATOR_FALSE</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_ZERO<code>;
	 * 
	 * If operator is one of follows:
	 * <ul>
	 * <li><code>FILTER_OPERATOR_EQ</code>
	 * <li><code>FILTER_OPERATOR_NE</code>
	 * <li><code>FILTER_OPERATOR_LT</code>
	 * <li><code>FILTER_OPERATOR_LE</code>
	 * <li><code>FILTER_OPERATOR_GE</code>
	 * <li><code>FILTER_OPERATOR_GT</code>
	 * <li><code>FILTER_OPERATOR_LIKE</code>
	 * <li><code>FILTER_OPERATOR_TOP_N</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_N</code>
	 * <li><code>FILTER_OPERATOR_TOP_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_BOTTOM_PERCENT</code>
	 * <li><code>FILTER_OPERATOR_ANY</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_ONE<code>;
	 * 
	 * If operator is one of follows:
	 * <ul>
	 * <li><code>FILTER_OPERATOR_BETWEEN</code>
	 * <li><code>FILTER_OPERATOR_NOT_BETWEEN</code>
	 * </ul>
	 * return <code>OPERATOR_LEVEL_TWO<code>;
	 * 
	 * If operator is not in the choice list, return
	 * <code>OPERATOR_LEVEL_NOT_EXIST<code>.
	 * 
	 * @param operator the operator to compute.
	 * @return level of operator.
	 */
	public static int computeFilterOperatorLevel(String operator) {
		if ((DesignChoiceConstants.FILTER_OPERATOR_TRUE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_FALSE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_NULL.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL.equals(operator))) {
			return OPERATOR_LEVEL_ZERO;
		} else if ((DesignChoiceConstants.FILTER_OPERATOR_EQ.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_GE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_NE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_LT.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_LE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_GT.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_LIKE.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_TOP_N.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_ANY.equals(operator))) {
			return OPERATOR_LEVEL_ONE;
		} else if ((DesignChoiceConstants.FILTER_OPERATOR_BETWEEN.equals(operator))
				|| (DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN.equals(operator))) {
			return OPERATOR_LEVEL_TWO;
		}
		return OPERATOR_LEVEL_NOT_EXIST;
	}
}
