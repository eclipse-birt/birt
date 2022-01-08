/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.model.api.core.IStructure;

/**
 * Represents the design of an SortCondition in the scripting environment
 * 
 */

public interface ISortCondition {

	/**
	 * Returns sort direction.
	 * 
	 * @return sort direction.
	 */

	public String getDirection();

	/**
	 * Sets sort direction.
	 * <ul>
	 * <li>asc
	 * <li>desc
	 * </ul>
	 * 
	 * @param direction
	 * @throws ScriptException
	 */

	public void setDirection(String direction) throws ScriptException;

	/**
	 * Returns sort key
	 * 
	 * @return sort key
	 */

	public String getKey();

	/**
	 * Sets sort key.
	 * 
	 * @param key
	 * @throws ScriptException
	 */

	public void setKey(String key) throws ScriptException;

	/**
	 * Returns structure.
	 * 
	 * @return structure
	 */

	public IStructure getStructure();
}
