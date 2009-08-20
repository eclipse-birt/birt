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

package org.eclipse.birt.report.designer.internal.ui.script;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.expressions.AbstractExpressionConverter;

/**
 * JSExpressionConverter
 */
public class JSExpressionConverter extends AbstractExpressionConverter
{

	public String getBindingExpression( String bindingName )
	{
		return ExpressionUtil.createJSRowExpression( bindingName );
	}

	public String getCubeBindingExpression( String bindingName )
	{
		return ExpressionUtil.createJSDataExpression( bindingName );
	}

	public String getDimensionExpression( String dimensionName,
			String levelName, String attributeName )
	{
		if ( attributeName == null )
		{
			return ExpressionUtil.createJSDimensionExpression( dimensionName,
					levelName );
		}
		return ExpressionUtil.createJSDimensionExpression( dimensionName,
				levelName,
				attributeName );
	}

	public String getMeasureExpression( String measureName )
	{
		return ExpressionUtil.createJSMeasureExpression( measureName );
	}

	public String getParameterExpression( String paramName )
	{
		return ExpressionUtil.createJSParameterExpression( paramName );
	}

	public String getBinding( String expression )
	{
		try
		{
			return ExpressionUtil.getColumnBindingName( expression );
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
		}
		return null;
	}

	public String getResultSetColumnExpression( String columnName )
	{
		// TODO Auto-generated method stub
		return ExpressionUtil.createJSDataSetRowExpression( columnName );
	}
}
