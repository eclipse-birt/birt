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
package org.eclipse.birt.data.engine.impl;

import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */
public class GroupBindingColumn {
	//
	private int groupLevel;
	private String groupKey;
	private Map bindings;

	private static Logger logger = Logger.getLogger(GroupBindingColumn.class.getName());

	/**
	 * 
	 * @param bindings
	 * @param groupLevel
	 */
	public GroupBindingColumn(String groupKey, int groupLevel, Map bindings) {
		Object[] params = { groupKey, Integer.valueOf(groupLevel), bindings };
		logger.entering(GroupBindingColumn.class.getName(), "GroupBindingColumn", params);

		this.groupKey = groupKey;
		this.groupLevel = groupLevel;
		this.bindings = bindings;
		logger.exiting(GroupBindingColumn.class.getName(), "GroupBindingColumn");
	}

	/**
	 * 
	 * @return
	 */
	String getGroupKey() {
		return this.groupKey;
	}

	/**
	 * 
	 * @return
	 */
	public int getGroupLevel() {
		return this.groupLevel;
	}

	/**
	 * 
	 * @return
	 */
	public Set getColumnNames() {
		return this.bindings.keySet();
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws DataException
	 */
	public IBaseExpression getExpression(String name) throws DataException {
		if (this.bindings.containsKey(name))
			return ((IBinding) this.bindings.get(name)).getExpression();
		else
			return null;
	}

	public IBinding getBinding(String name) throws DataException {
		if (this.bindings.containsKey(name))
			return ((IBinding) this.bindings.get(name));
		else
			return null;
	}

}
