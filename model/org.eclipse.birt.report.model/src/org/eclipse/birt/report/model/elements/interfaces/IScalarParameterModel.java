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
	
	public static final String VALUE_TYPE_PROP = "valueType";  //$NON-NLS-1$
	
	/**
	 * Name of the conceal-value property.
	 */

	public static final String CONCEAL_VALUE_PROP = "concealValue"; //$NON-NLS-1$

	/**
	 * Name of the allow-null property.
	 */

	public static final String ALLOW_NULL_PROP = "allowNull"; //$NON-NLS-1$

	/**
	 * Name of the allow-blank property.
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
	 * Name of the display prompt property.
	 */

	public static final String DISPLAY_PROMPT_PROP = "displayPrompt"; //$NON-NLS-1$ 
}
