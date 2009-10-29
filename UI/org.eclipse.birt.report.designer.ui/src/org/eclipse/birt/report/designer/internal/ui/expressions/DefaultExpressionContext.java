/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.expressions;

import java.util.List;

import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.expressions.IExpressionFilterSupport;

/**
 * DefaultExpressionContext
 */
public class DefaultExpressionContext implements
		IExpressionContext,
		IExpressionFilterSupport
{

	private Object contextObject;

	private List<ExpressionFilter> filters;

	public DefaultExpressionContext( Object contextObject )
	{
		this.contextObject = contextObject;
	}

	public Object getContextObject( )
	{
		return contextObject;
	}

	public void setFilters( List<ExpressionFilter> filters )
	{
		this.filters = filters;
	}

	public java.util.List<ExpressionFilter> getFilters( )
	{
		return filters;
	};
}
