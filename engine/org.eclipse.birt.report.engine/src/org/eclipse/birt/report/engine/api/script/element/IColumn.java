/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;

/**
 * Script wrapper of ColumnHandle.
 *
 */

public interface IColumn {
	/**
	 * Removes all hide rules that matches formatType.
	 * 
	 * @param rule
	 * @exception ScriptException
	 */

	void removeHideRule(IHideRule rule) throws ScriptException;

	/**
	 * Removes all hide rules
	 * 
	 * @throws ScriptException
	 */

	void removeHideRules() throws ScriptException;

	/**
	 * Returns array of hide rule expression
	 * 
	 * @return array of hide rule expression
	 */

	IHideRule[] getHideRules();

	/**
	 * Add HideRule
	 * 
	 * @param rule
	 * @throws ScriptException
	 */

	void addHideRule(IHideRule rule) throws ScriptException;
}
