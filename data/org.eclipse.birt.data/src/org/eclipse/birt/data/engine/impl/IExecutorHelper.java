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
import org.mozilla.javascript.Scriptable;

/**
 * This interface define the behavior of an ExecutorHelper, which is used to help
 * its clients to evaluate expressions, especially ones like "row._outer". 
 */
public interface IExecutorHelper
{

	/**
	 * Return the Scope.
	 * 
	 * @return
	 */
	public Scriptable getScope( );

	/**
	 * Return the parent of this IExecutorHelper.
	 * @return
	 */
	public IExecutorHelper getParent( );
	
	/**
	 * Return the ExprManager instance.
	 * @return
	 */
	public Scriptable getJSRowObject( );
	
	/**
	 * Evaluate an Expression.
	 * 
	 * @param expr
	 * @return
	 * @throws BirtException
	 */
	public Object evaluate( IBaseExpression expr ) throws BirtException;

}