/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.expressions;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * IExpressionConverter
 */
public interface IExpressionConverter
{

	/**
	 * Returns the binding expression by given name.
	 * 
	 * @param bindingName
	 * @return
	 */
	String getBindingExpression( String bindingName );

	/**
	 * Returns the parameter expression by given name.
	 * 
	 * @param paramName
	 * @return
	 */
	String getParameterExpression( String paramName );

	/**
	 * Returns the cube binding expression by given name.
	 * 
	 * @param bindingName
	 * @return
	 */
	String getCubeBindingExpression( String bindingName );

	/**
	 * Returns the dimension/level/attribute expression for given names.
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	String getDimensionExpression( String dimensionName, String levelName,
			String attributeName );

	/**
	 * Returns the measure expression for given name.
	 * 
	 * @param measureName
	 * @return
	 */
	String getMeasureExpression( String measureName );

	/**
	 * Returns the first binding that found referenced in the given expression.
	 * 
	 * @param expression
	 * @return
	 */
	String getBinding( String expression );

	/**
	 * Returns the result set column expression by given column name.
	 * 
	 * @param bindingName
	 * @return
	 */
	String getResultSetColumnExpression( String columnName );

	/**
	 * Returns the expression as the representation for the given constant value
	 * and type.
	 * 
	 * @param value
	 *            The constant value string.
	 * @param dataType
	 *            The type constants defined as
	 *            {@link DesignChoiceConstants#CHOICE_COLUMN_DATA_TYPE}
	 * @return
	 */
	String getConstantExpression( String value, String dataType );
}