/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public class ExprManager {
	private List bindingExprs;
	private Map autoBindingExprMap;

	// TODO enhance me. The auto binding should be done at preparation phrase rather
	// than
	// execution phrase.
	private Map autoBindingMap;

	private int entryLevel;
	private IBaseQueryDefinition baseQueryDefn;
	private ScriptContext context;
	public final static int OVERALL_GROUP = 0;

	// private Context cx;

	/**
	 * An exprManager object is to manipulate all available column bindings for
	 * specified query definition.
	 */
	public ExprManager(IBaseQueryDefinition baseQueryDefn, ScriptContext cx) {
		bindingExprs = new ArrayList();
		autoBindingExprMap = new HashMap();
		entryLevel = OVERALL_GROUP;
		this.baseQueryDefn = baseQueryDefn;
		this.autoBindingMap = new HashMap();
		this.context = cx;
	}

	/**
	 * @param resultsExprMap
	 * @param groupLevel
	 */
	public void addBindingExpr(String groupKey, Map resultsExprMap, int groupLevel) {
		if (resultsExprMap == null)
			return;

		bindingExprs.add(new GroupBindingColumn(groupKey, groupLevel, resultsExprMap));
	}

	/**
	 * @param name
	 * @param baseExpr
	 */
	void addAutoBindingExpr(String name, IBaseExpression baseExpr) {
		autoBindingExprMap.put(name, baseExpr);
		this.autoBindingMap.put(name, new Binding(name, baseExpr));
	}

	/**
	 * @param name
	 * @return expression for specified name
	 * @throws DataException
	 */
	public IBaseExpression getExpr(String name) throws DataException {
		IBaseExpression baseExpr = getBindingExpr(name);
		if (baseExpr == null)
			baseExpr = getAutoBindingExpr(name);

		return baseExpr;
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public IBinding getBinding(String name) throws DataException {
		for (int i = 0; i < bindingExprs.size(); i++) {
			GroupBindingColumn gcb = (GroupBindingColumn) bindingExprs.get(i);
			if (entryLevel != OVERALL_GROUP) {
				if (gcb.getGroupLevel() > entryLevel)
					continue;
			}
			if (gcb.getBinding(name) != null)
				return gcb.getBinding(name);
		}

		if (this.autoBindingMap.containsKey(name)) {
			return (IBinding) this.autoBindingMap.get(name);
		}

		return null;

	}

	/**
	 * @param name
	 * @return
	 * @throws DataException
	 */
	private IBaseExpression getBindingExpr(String name) throws DataException {
		for (int i = 0; i < bindingExprs.size(); i++) {
			GroupBindingColumn gcb = (GroupBindingColumn) bindingExprs.get(i);
			if (entryLevel != OVERALL_GROUP) {
				if (gcb.getGroupLevel() > entryLevel)
					continue;
			}
			Object o = gcb.getExpression(name);
			if (o != null)
				return (IBaseExpression) o;
		}
		return null;
	}

	/**
	 * @param name
	 * @return auto binding expression for specified name
	 */
	IScriptExpression getAutoBindingExpr(String name) {
		return (IScriptExpression) this.autoBindingExprMap.get(name);
	}

	/**
	 * TODO: remove me
	 * 
	 * @return
	 */
	public List getBindingExprs() {
		return this.bindingExprs;
	}

	/**
	 * TODO: remove me
	 * 
	 * @return
	 */
	public Map getAutoBindingExprMap() {
		return this.autoBindingExprMap;
	}

	/**
	 * TODO: remove me
	 * 
	 * Set the entry group level of the expr manager. The column bindings of groups
	 * with group level greater than the given key will not be visible to outside.
	 * 
	 * @param i
	 */
	void setEntryGroupLevel(int i) {
		this.entryLevel = i;
	}

	/**
	 * @throws DataException
	 */
	public void validateColumnBinding() throws DataException {
		ExprManagerUtil.validateColumnBinding(this, baseQueryDefn, context);
	}

}
