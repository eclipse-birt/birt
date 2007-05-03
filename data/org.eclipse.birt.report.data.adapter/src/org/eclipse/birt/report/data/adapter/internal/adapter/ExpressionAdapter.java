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

import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;

public class ExpressionAdapter extends ScriptExpression
{
	
	/**
	 * Constructs an expression with provided text and return data type
	 * Data type is defined as Dte enumeration value
	 */
	public ExpressionAdapter( String exprText, int returnType )
	{
		super( exprText, returnType );
	}
	
	/**
	 * Constructs an expression with provided text and return data type
	 * Data type is defined as a Model data type string
	 */
	public ExpressionAdapter( String exprText, String returnType )
	{
		super( exprText, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(returnType) );
	}
	
	/**
	 * Constructs an expression based on Model computed column handle
	 */
	public ExpressionAdapter( ComputedColumnHandle ccHandle )
	{
		super( ccHandle.getExpression(), 
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( ccHandle.getDataType() ) );
	}
	
}
