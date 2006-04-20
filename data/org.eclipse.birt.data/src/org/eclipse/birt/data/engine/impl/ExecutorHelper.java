/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public class ExecutorHelper implements IExecutorHelper
{
	//
	private Scriptable scope;	
	private Scriptable jsRowObject;

	//
	private IExecutorHelper parent;
	
	/**
	 * @param scope
	 */
	public ExecutorHelper( Scriptable scope )
	{
		this.scope = scope;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getScope()
	 */
	public Scriptable getScope()
	{
		return this.scope;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getParent()
	 */
	public IExecutorHelper getParent( )
	{
		return this.parent;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getJSRowObject()
	 */
	public Scriptable getJSRowObject( )
	{
		return jsRowObject;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#evaluate(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Object evaluate( IBaseExpression expr ) throws BirtException
	{
		return ExprEvaluateUtil.evaluateRawExpression2( expr, scope);
	}
	
	/**
	 * @param parent
	 */
	public void setParent( IExecutorHelper parent )
	{
		this.parent = parent;
	}
	
	/**
	 * @param jsRowObject
	 */
	public void setJSRowObject( Scriptable jsRowObject )
	{
		this.jsRowObject = jsRowObject;		
	}
	
}
