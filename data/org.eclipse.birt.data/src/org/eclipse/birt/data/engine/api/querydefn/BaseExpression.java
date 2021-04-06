/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
	public Object getHandle() {
		return this.handle;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setHandle(java.lang.Object)
	 */
	public void setHandle(Object handle) {
		this.handle = handle;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setGroupName(String)
	 */
	public void setGroupName(String name) {
		if (name != null && name.trim().length() != 0)
			this.groupName = name;
		else
			this.groupName = GROUP_OVERALL;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getGroupName()
	 */
	public String getGroupName() {
		return this.groupName;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#getGroupName()
	 */
	public String getScriptId() {
		return this.scriptId;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IBaseExpression#setScriptId(String)
	 */
	public void setScriptId(String scriptId) {
		this.scriptId = scriptId;
	}

	public String getScopeId() {
		return this.scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}
}
