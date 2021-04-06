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
package org.eclipse.birt.data.engine.script;

/**
 * This class control the current pass level of filter.
 */
public class FilterPassController {
	public static final int FIRST_PASS = 1;
	public static final int SECOND_PASS = 2;
	public static final int DEFAULT_PASS = 0;
	public static final int DEFAULT_ROW_COUNT = -1;

	private boolean forceReset = false;
	private int passLevel = DEFAULT_PASS;

	private int rowCount = DEFAULT_ROW_COUNT;
	private int secondPassRowCount = 0;

	public int getPassLevel() {
		return passLevel;
	}

	public void setPassLevel(int i) {
		passLevel = i;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int i) {
		rowCount = i;
	}

	public void setForceReset(boolean b) {
		forceReset = b;
	}

	public boolean getForceReset() {
		return forceReset;
	}

	public int getSecondPassRowCount() {
		return secondPassRowCount;
	}

	public void setSecondPassRowCount(int row) {
		secondPassRowCount = row;
	}
}
