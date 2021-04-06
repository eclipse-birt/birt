/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public static final String IS_REQUIRED_PROP = "isRequired"; //$NON-NLS-1$

	/**
	 * Name of the DataSet property for a dynamic list.
	 */

	public static final String DATASET_NAME_PROP = "dataSetName"; //$NON-NLS-1$

	/**
	 * Name of the Limited-list property
	 */
	public static final String LIST_LIMIT_PROP = "listLimit"; //$NON-NLS-1$

	/**
	 * Name of the sortDirection property. Can be null, "asc" or "desc".
	 * 
	 */

	public static final String SORT_DIRECTION_PROP = "sortDirection"; //$NON-NLS-1$

	/**
	 * Name of the sortKey property. It indicates the sort key is value or the
	 * display text.
	 * 
	 */

	public static final String SORT_BY_PROP = "sortBy"; //$NON-NLS-1$

	/**
	 * Name of the parameter value type property.
	 */

	public static final String VALUE_TYPE_PROP = "valueType"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates the expression by which the parameter
	 * query result sorts if the parameter is dynamic.
	 */
	public static final String SORT_BY_COLUMN_PROP = "sortByColumn"; //$NON-NLS-1$

	/**
	 * Name of the default value property.
	 */

	public static final String DEFAULT_VALUE_PROP = "defaultValue"; //$NON-NLS-1$

	/**
	 * Name of the choice property for a selection list.
	 */

	public static final String SELECTION_LIST_PROP = "selectionList"; //$NON-NLS-1$

	/**
	 * Name of the value expression property for a dynamic list.
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$

	/**
	 * Name of the label expression property for a dynamic list.
	 */

	public static final String LABEL_EXPR_PROP = "labelExpr"; //$NON-NLS-1$

	/**
	 * Name of the parameter data type property.
	 */

	public static final String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the distinct property. If it is true, Engine checks duplicate values.
	 * 
	 */

	public static final String DISTINCT_PROP = "distinct"; //$NON-NLS-1$

	/**
	 * Name of the control type property.
	 */
	public static final String CONTROL_TYPE_PROP = "controlType"; //$NON-NLS-1$

}
