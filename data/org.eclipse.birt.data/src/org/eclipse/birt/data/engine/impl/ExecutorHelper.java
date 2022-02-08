/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.logging.Logger;

import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public class ExecutorHelper implements IExecutorHelper {
	//
	private Scriptable scriptable;

	//
	private IExecutorHelper parent;

	private static Logger logger = Logger.getLogger(ExecutorHelper.class.getName());

	/**
	 * @param scope
	 */
	public ExecutorHelper(IExecutorHelper parent) {
		logger.entering(ExecutorHelper.class.getName(), "ExecutorHelper", parent);
		this.parent = parent;
		logger.exiting(ExecutorHelper.class.getName(), "ExecutorHelper");
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getParent()
	 */
	public IExecutorHelper getParent() {
		return this.parent;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IExecutorHelper#getJSRowObject()
	 */
	public Scriptable getScriptable() {
		return scriptable;
	}

	/**
	 * @param jsRowObject
	 */
	public void setScriptable(Scriptable scriptable) {
		this.scriptable = scriptable;
	}

}
