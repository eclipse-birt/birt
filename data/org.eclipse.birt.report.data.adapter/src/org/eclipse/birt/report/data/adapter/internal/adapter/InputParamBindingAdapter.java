/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.internal.adapter;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.report.model.api.ParamBindingHandle;

/**
 * Adaptor for Input Parameter Binding
 */
public class InputParamBindingAdapter extends InputParameterBinding
{
	/**
	 * Constructs instance based on Model ParamBindingHandle 
	 */
	public InputParamBindingAdapter( ParamBindingHandle modelHandle )
	{
		this( modelHandle.getParamName(),  modelHandle.getExpression() );
	}
	
	/**
	 * Constructs instance based on param name and expression 
	 */
	public InputParamBindingAdapter( String paramName, String bindingExpr )
	{
		super( paramName, 
				new ExpressionAdapter( bindingExpr, DataType.ANY_TYPE ) );
	}
}
