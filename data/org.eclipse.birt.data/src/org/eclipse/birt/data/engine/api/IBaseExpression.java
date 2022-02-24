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
package org.eclipse.birt.data.engine.api;

/**
 * Base type to represent a generic data expression used in a report design. An
 * expression has an optional return data type. Each expression can also be
 * associated with a handle, which is used by the Data Engine to store the
 * compiled evaluation plan for the expression.
 */
public interface IBaseExpression {
	/**
	 * The string value which stands for overall group.
	 * 
	 * @deprecated
	 */
	public static final String GROUP_OVERALL = "Total.OVERALL";

	/**
	 * Gets the data type of the expression. Acceptable return values are those
	 * enumeration constants defined in the
	 * <code>org.eclipse.birt.core.data.DataType</code> class. If the result data
	 * type of the expression is not known, return <code>UNKNOWN_TYPE</code>.
	 * 
	 * @see org.eclipse.birt.core.data.DataType
	 */
	public abstract int getDataType();

	/**
	 * Returns the handle associated with the expression.
	 * 
	 * @return the expression execution handle.
	 */
	public abstract Object getHandle();

	/**
	 * Associates the expression with the provided handle.
	 */
	public abstract void setHandle(Object handle);

	/**
	 * Set the group name this expession belongs to.
	 * 
	 * @param name
	 * @deprecated
	 */
	public void setGroupName(String name);

	/**
	 * The group on which this expression should be evaluated.
	 * 
	 * @return
	 * @deprecated
	 */
	public String getGroupName();

	/**
	 * Return scriptId of the expression. The value of script id will be used to
	 * determine by which script engine the script should be evaluated against.
	 */
	public String getScriptId();

	/**
	 * Set the script id of the expression. For javascript expression, the id could
	 * be "javascript".
	 */
	public void setScriptId(String scriptId);

	/**
	 * Get scopeId of this expression.
	 * 
	 * @return
	 */
	public String getScopeId();

	/**
	 * Set scopeId for this expression.
	 * 
	 * @param scopeId
	 */
	public void setScopeId(String scopeId);

}
