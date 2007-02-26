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
 * The interface for scalar parameter element to store the constants.
 */
public interface IScalarParameterModel
{

	/**
	 * Name of the default value property.
	 */

	public static final String DEFAULT_VALUE_PROP = "defaultValue"; //$NON-NLS-1$ 

	/**
	 * Name of the parameter data type property.
	 */

	public static final String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the parameter value type property.
	 */

	public static final String VALUE_TYPE_PROP = "valueType"; //$NON-NLS-1$

	/**
	 * Name of the conceal-value property.
	 */

	public static final String CONCEAL_VALUE_PROP = "concealValue"; //$NON-NLS-1$

	/**
	 * Name of the allow-null property.
	 * 
	 * @deprecated by {@link #IS_REQUIRED_PROP} since BIRT 2.2
	 */

	public static final String ALLOW_NULL_PROP = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the allow-blank property.
	 * 
	 * @deprecated by {@link #IS_REQUIRED_PROP} since BIRT 2.2
	 */

	public static final String ALLOW_BLANK_PROP = "allowBlank"; //$NON-NLS-1$

	/**
	 * Name of the format property.
	 */

	public static final String FORMAT_PROP = "format"; //$NON-NLS-1$

	/**
	 * Name of the control type property.
	 */

	public static final String CONTROL_TYPE_PROP = "controlType"; //$NON-NLS-1$ 

	/**
	 * Name of the alignment property.
	 */

	public static final String ALIGNMENT_PROP = "alignment"; //$NON-NLS-1$ 

	/**
	 * Name of the DataSet property for a dynamic list.
	 */

	public static final String DATASET_NAME_PROP = "dataSetName"; //$NON-NLS-1$ 

	/**
	 * Name of the value expression property for a dynamic list.
	 */

	public static final String VALUE_EXPR_PROP = "valueExpr"; //$NON-NLS-1$ 

	/**
	 * Name of the label expression property for a dynamic list.
	 */

	public static final String LABEL_EXPR_PROP = "labelExpr"; //$NON-NLS-1$ 

	/**
	 * Name of the muchMatch property for a selection list.
	 */

	public static final String MUCH_MATCH_PROP = "mustMatch"; //$NON-NLS-1$ 

	/**
	 * Name of the fixedOrder property for a selection list.
	 */

	public static final String FIXED_ORDER_PROP = "fixedOrder"; //$NON-NLS-1$ 

	/**
	 * Name of the choice property for a selection list.
	 */

	public static final String SELECTION_LIST_PROP = "selectionList"; //$NON-NLS-1$ 

	/**
	 * Name of the prompt text property
	 */
	public static final String PROMPT_TEXT_PROP = "promptText"; //$NON-NLS-1$

	/**
	 * Name of the prompt text ID property. This property contains the message
	 * ID used to localize property prompt text ID.
	 */

	public static final String PROMPT_TEXT_ID_PROP = "promptTextID"; //$NON-NLS-1$

	/**
	 * Name of the Limited-list property
	 */
	public static final String LIST_LIMIT_PROP = "listLimit"; //$NON-NLS-1$

	/**
	 * The property name of the bound columns that bind the report element with
	 * the data set columns.
	 */

	public static final String BOUND_DATA_COLUMNS_PROP = "boundDataColumns"; //$NON-NLS-1$

	/**
	 * Name of the is-required property. If it is true, the user cannot enter
	 * "null" value.
	 * 
	 */

	public static final String IS_REQUIRED_PROP = "isRequired"; //$NON-NLS-1$

	/**
	 * Name of the distinct property. If it is true, Engine checks duplicate
	 * values.
	 * 
	 */

	public static final String DISTINCT_PROP = "distinct"; //$NON-NLS-1$

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

}
