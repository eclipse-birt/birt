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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for scalar parameter element to store the constants.
 */
public interface IScalarParameterModel {

	/**
	 * Name of the conceal-value property.
	 */

	String CONCEAL_VALUE_PROP = "concealValue"; //$NON-NLS-1$

	/**
	 * Name of the allow-null property.
	 *
	 * @deprecated by {@link #IS_REQUIRED_PROP} since BIRT 2.2
	 */

	@Deprecated
	String ALLOW_NULL_PROP = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the allow-blank property.
	 *
	 * @deprecated by {@link #IS_REQUIRED_PROP} since BIRT 2.2
	 */

	@Deprecated
	String ALLOW_BLANK_PROP = "allowBlank"; //$NON-NLS-1$

	/**
	 * Name of the format property.
	 */

	String FORMAT_PROP = "format"; //$NON-NLS-1$

	/**
	 * Name of the alignment property.
	 */

	String ALIGNMENT_PROP = "alignment"; //$NON-NLS-1$

	/**
	 * Name of the muchMatch property for a selection list.
	 */

	String MUCH_MATCH_PROP = "mustMatch"; //$NON-NLS-1$

	/**
	 * Name of the fixedOrder property for a selection list.
	 */

	String FIXED_ORDER_PROP = "fixedOrder"; //$NON-NLS-1$

	/**
	 * The property name of the bound columns that bind the report element with the
	 * data set columns.
	 */

	String BOUND_DATA_COLUMNS_PROP = "boundDataColumns"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the type of this parameter: simple,
	 * multi-value, or ad-hoc.
	 */

	String PARAM_TYPE_PROP = "paramType"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the maximal number of of entries a report
	 * parameter pick list can have.
	 */

	String AUTO_SUGGEST_THRESHOLD_PROP = "autoSuggestThreshold"; //$NON-NLS-1$

	/**
	 * Name of the method that implements to return the default value list of the
	 * parameter.
	 */
	String GET_DEFAULT_VALUE_LIST_PROP = "getDefaultValueList"; //$NON-NLS-1$

	/**
	 * Name of the method that implements to return the selection value list of the
	 * parameter. This is meaningful for 'list' or 'combo' control type parameter.
	 */
	String GET_SELECTION_VALUE_LIST_PROP = "getSelectionValueList"; //$NON-NLS-1$
}
