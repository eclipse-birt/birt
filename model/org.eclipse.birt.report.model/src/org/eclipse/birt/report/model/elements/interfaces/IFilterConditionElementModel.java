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
 * 
 */
public interface IFilterConditionElementModel
{

	/**
	 * Name of the filter operator member.
	 */

	String OPERATOR_PROP = "operator"; //$NON-NLS-1$

	/**
	 * Name of the filter expression member.
	 */

	String EXPR_PROP = "expr"; //$NON-NLS-1$

	/**
	 * Name of the filter value 1 expression member.
	 */

	String VALUE1_PROP = "value1"; //$NON-NLS-1$

	/**
	 * Name of the filter value 2 expression member.
	 */

	String VALUE2_PROP = "value2"; //$NON-NLS-1$

	/**
	 * Name of the filter target member.
	 */

	String FILTER_TARGET_PROP = "filterTarget"; //$NON-NLS-1$

	/**
	 * Name of the property that gives the member value.
	 */

	String MEMBER_PROP = "member"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether this filter is otpional or
	 * not.
	 */

	String IS_OPTIONAL_PROP = "isOptional"; //$NON-NLS-1$
}
