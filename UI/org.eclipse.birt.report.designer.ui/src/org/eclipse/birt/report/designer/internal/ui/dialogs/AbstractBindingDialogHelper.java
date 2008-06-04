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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;

/**
 * 
 */

public abstract class AbstractBindingDialogHelper implements
		IBindingDialogHelper
{

	protected ReportItemHandle bindingHolder;
	protected ComputedColumnHandle binding;
	protected DataColumnBindingDialog dialog;
	private boolean isAggregate = false;
	protected ExpressionProvider expressionProvider;
	private Object itemContainer;

	public boolean isAggregate( )
	{
		return isAggregate;
	}

	public void setAggregate( boolean isAggregate )
	{
		this.isAggregate = isAggregate;
	}

	public ReportItemHandle getBindingHolder( )
	{
		return bindingHolder;
	}

	public void setBindingHolder( ReportItemHandle bindingHolder )
	{
		this.bindingHolder = bindingHolder;
	}

	public ComputedColumnHandle getBinding( )
	{
		return binding;
	}

	public void setBinding( ComputedColumnHandle binding )
	{
		this.binding = binding;
		if ( this.binding != null )
			setAggregate( this.binding.getAggregateFunction( ) != null
					&& !this.binding.getAggregateFunction( ).equals( "" ) ); //$NON-NLS-1$
	}

	public ComputedColumnHandle getBindingColumn( )
	{
		return this.binding;
	}

	public DataColumnBindingDialog getDialog( )
	{
		return dialog;
	}

	public void setDialog( DataColumnBindingDialog dialog )
	{
		this.dialog = dialog;
	}

	public ExpressionProvider getExpressionProvider( )
	{
		return expressionProvider;
	}

	public void setExpressionProvider( ExpressionProvider expressionProvider )
	{
		this.expressionProvider = expressionProvider;
	}

	public void setDataItemContainer( Object itemContainer )
	{
		this.itemContainer = itemContainer;
	}

	public Object getDataItemContainer( )
	{
		return this.itemContainer;
	}

	public boolean canProcessWithWarning( )
	{
		return true;
	}
}
