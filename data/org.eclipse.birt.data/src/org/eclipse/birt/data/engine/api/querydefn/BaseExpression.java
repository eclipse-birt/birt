/*
 *************************************************************************
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
 *
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IBaseExpression;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IBaseExpression} interface.
 */
public abstract class BaseExpression implements IBaseExpression {
	protected int dataType;
	protected Object handle;
	private String groupName = GROUP_OVERALL;

	// default script id
	public final static String javaScriptId = "javascript";
	public final static String constantId = "constant";

	private String scriptId = javaScriptId;
	private String scopeId = null;

	/**
	 * Constructs an instance with unknown data type
	 */
	public BaseExpression() {
		this.dataType = DataType.UNKNOWN_TYPE;
	}

	/**
	 * Constructs an instance with specified data type
	 */
	public BaseExpression(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getDataType()
	 */
	@Override
	public int getDataType() {
		return this.dataType;
	}

	/**
	 * Sets the data type of the expression
	 */
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getHandle()
	 */
	@Override
	public Object getHandle() {
		return this.handle;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setHandle(java.lang.Object)
	 */
	@Override
	public void setHandle(Object handle) {
		this.handle = handle;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setGroupName(String)
	 */
	@Override
	public void setGroupName(String name) {
		if (name != null && name.trim().length() != 0) {
			this.groupName = name;
		} else {
			this.groupName = GROUP_OVERALL;
		}
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getGroupName()
	 */
	@Override
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getGroupName()
	 */
	@Override
	public String getScriptId() {
		return this.scriptId;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setScriptId(String)
	 */
	@Override
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	@Override
	public String getScopeId() {
		return this.scopeId;
	}

	@Override
	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}
}
