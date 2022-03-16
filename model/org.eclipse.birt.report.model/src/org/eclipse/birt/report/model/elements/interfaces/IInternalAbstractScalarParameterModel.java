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
 * The interface for AbstractScalarParameter element to store the constants.
 */
public interface IInternalAbstractScalarParameterModel {

	/**
	 * Name of the is-required property. If it is true, the user cannot enter "null"
	 * value.
	 *
	 */

	String IS_REQUIRED_PROP = "isRequired"; //$NON-NLS-1$

	/**
	 * Name of the DataSet property for a dynamic list.
	 */

	String DATASET_NAME_PROP = "dataSetName"; //$NON-NLS-1$

	/**
	 * Name of the Limited-list property
	 */
	String LIST_LIMIT_PROP = "listLimit"; //$NON-NLS-1$

	/**
	 * Name of the sortDirection property. Can be null, "asc" or "desc".
	 *
	 */

	String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the sortKey property. It indicates the sort key is value or the
	 * display text.
	 *
	 */

	String SORT_BY_PROP = "sortBy"; //$NON-NLS-1$

	/**
	 * Name of the parameter value type property.
	 */

	String VALUE_TYPE_PROP = "valueType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the expression by which the parameter
	 * query result sorts if the parameter is dynamic.
	 */
	String SORT_BY_COLUMN_PROP = "sortByColumn"; //$NON-NLS-1$

	/**
	 * Name of the default value property.
	 */

	String DEFAULT_VALUE_PROP = "defaultValue"; //$NON-NLS-1$

	/**
	 * Name of the choice property for a selection list.
	 */

	String SELECTION_LIST_PROP = "selectionList"; //$NON-NLS-1$

	/**
	 * Name of the value expression property for a dynamic list.
	 */

	String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the label expression property for a dynamic list.
	 */

	String LABEL_EXPR_PROP = "labelExpr"; //$NON-NLS-1$

	/**
	 * Name of the parameter data type property.
	 */

	String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the distinct property. If it is true, Engine checks duplicate values.
	 *
	 */

	String DISTINCT_PROP = "distinct"; //$NON-NLS-1$

	/**
	 * Name of the control type property.
	 */
	String CONTROL_TYPE_PROP = "controlType"; //$NON-NLS-1$

}
