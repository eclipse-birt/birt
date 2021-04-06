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

package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public abstract class BaseJSEvalHelper {

	protected Scriptable scope;
	protected ICubeQueryDefinition queryDefn;
	protected IBaseExpression expr;
	protected IBaseQueryResults outResults;
	private List jsObjectPopulators;
	protected ScriptContext cx;

	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param cx
	 * @param expr
	 * @throws DataException
	 */
	protected void init(IBaseQueryResults outResults, Scriptable parentScope, ICubeQueryDefinition queryDefn,
			ScriptContext cx, IBaseExpression expr) throws DataException {
		try {
			this.scope = ((IDataScriptEngine) (cx.getScriptEngine(IDataScriptEngine.ENGINE_NAME))).getJSContext(cx)
					.initStandardObjects();
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		this.scope.setParentScope(parentScope);
		this.queryDefn = queryDefn;
		this.expr = expr;
		this.outResults = outResults;
		this.cx = cx;
		this.jsObjectPopulators = new ArrayList();
		registerJSObjectPopulators();
		OLAPExpressionCompiler.compile(cx.newContext(this.scope), this.expr);
	}

	/**
	 * Overwrite this method if other Javascript objects are needed to registered.
	 * By default, the dimension Javascript object will be registered.
	 * 
	 * @throws DataException
	 */
	protected abstract void registerJSObjectPopulators() throws DataException;

	/**
	 * 
	 * @param populator
	 * @throws DataException
	 */
	protected void register(IJSObjectPopulator populator) throws DataException {
		populator.doInit();
		this.jsObjectPopulators.add(populator);
	}

	/**
	 * 
	 * @param resultRow
	 */
	protected void setData(Object resultRow) {
		for (Iterator i = jsObjectPopulators.iterator(); i.hasNext();) {
			IJSObjectPopulator populator = (IJSObjectPopulator) i.next();
			populator.setData(resultRow);
		}
	}

	/**
	 * clear all initialized javascript objects from the scope.
	 */
	public void close() {
		for (Iterator i = jsObjectPopulators.iterator(); i.hasNext();) {
			IJSObjectPopulator populator = (IJSObjectPopulator) i.next();
			populator.cleanUp();
		}
		jsObjectPopulators = null;
	}
}
