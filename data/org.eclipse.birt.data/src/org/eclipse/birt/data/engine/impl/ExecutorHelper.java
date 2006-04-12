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
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public class ExecutorHelper implements IExecutorHelper
{
	//
	private IExecutorHelper parent;
	private IResultIterator odiResult;
	private Scriptable scope;
	private ExprManager exprManager;
	
	public ExecutorHelper( IResultIterator it, Scriptable scope )
	{
		this.odiResult = it;
		this.scope = scope;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#evaluate(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public Object evaluate( IBaseExpression expr ) throws BirtException
	{
		return ExprEvaluateUtil.evaluateRawExpression2( expr, scope);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#setParent(org.eclipse.birt.data.engine.impl.IExecutorHelper)
	 */
	public void setParent( IExecutorHelper parent )
	{
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getParent()
	 */
	public IExecutorHelper getParent( )
	{
		return this.parent;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#setResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator)
	 */
	public void setResultIterator( IResultIterator it )
	{
		this.odiResult = it;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#setExprManager(org.eclipse.birt.data.engine.impl.ExprManager)
	 */
	public void setExprManager( ExprManager expr )
	{
		this.exprManager = expr;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getExprManager()
	 */
	public ExprManager getExprManager( )
	{
		return this.exprManager;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getScope()
	 */
	public Scriptable getScope()
	{
		return this.scope;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getOdiResult()
	 */
	public IResultIterator getOdiResult()
	{
		return this.odiResult;
	}
}
