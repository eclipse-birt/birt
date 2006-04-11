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
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * This interface define the behavior of an ExecutorHelper, which is used to help
 * its clients to evaluate expressions, especially ones like "row._outer". 
 */
public interface IExecutorHelper
{
	/**
	 * Evaluate an Expression.
	 * 
	 * @param expr
	 * @return
	 * @throws BirtException
	 */
	public abstract Object evaluate( IBaseExpression expr )
			throws BirtException;

	/**
	 * Set the parent of this IExecutorHelper. If "row._outer" is found in an expression,
	 * we need to use its parent IExecutorHelper instance to evaluate the value. If an 
	 * IEvaluatorHelper do not have parent, that means it is not a nested or sub query.
	 * 
	 * @param parent
	 */
	public abstract void setParent( IExecutorHelper parent );

	/**
	 * Return the parent of this IExecutorHelper.
	 * @return
	 */
	public abstract IExecutorHelper getParent( );

	/**
	 * Set the org.eclipse.birt.data.engine.odi.IResultIterator instance used by the 
	 * IEvaluatorHelper instance to evaluate the value. 
	 * @param it
	 */
	public abstract void setResultIterator( IResultIterator it );

	/**
	 * Set the ExprManager instance of this IEvaluatorHelper instance. The ExprManager
	 * instance is used by JSResultRow to create its parent JSResultRow instance once 
	 * necessary.
	 * 
	 * @param expr
	 */
	public abstract void setExprManager( ExprManager expr );

	/**
	 * Return the ExprManager instance.
	 * @return
	 */
	public abstract ExprManager getExprManager( );

	/**
	 * Return the Scope.
	 * 
	 * @return
	 */
	public abstract Scriptable getScope( );

	/**
	 * Return the ResultIterator instance.
	 * @return
	 */
	public abstract IResultIterator getOdiResult( );

}