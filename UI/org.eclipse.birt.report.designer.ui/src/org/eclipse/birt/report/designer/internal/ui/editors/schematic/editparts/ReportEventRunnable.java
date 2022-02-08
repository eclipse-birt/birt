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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.Map;

/**
 * Runnale to run the post model event.
 */

public abstract class ReportEventRunnable implements Runnable {
	private Object focus;
	private int type;
	private Map args;

	/**
	 * @param focus
	 * @param type
	 * @param args
	 */
	public ReportEventRunnable(Object focus, int type, Map args) {
		super();
		this.focus = focus;
		this.type = type;
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
	}

	/**
	 * @return
	 */
	public Map getArgs() {
		return args;
	}

	/**
	 * @param args
	 */
	public void setArgs(Map args) {
		this.args = args;
	}

	/**
	 * @return
	 */
	public Object getFocus() {
		return focus;
	}

	/**
	 * @param focus
	 */
	public void setFocus(Object focus) {
		this.focus = focus;
	}

	/**
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 */
	public void setType(int type) {
		this.type = type;
	}
}
