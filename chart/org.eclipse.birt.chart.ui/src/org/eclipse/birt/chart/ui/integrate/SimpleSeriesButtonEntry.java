/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.integrate;

import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesButtonEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;

/**
 * 
 */

public class SimpleSeriesButtonEntry implements ISeriesButtonEntry {

	private final String buttonId;
	private final String popupName;
	private final ITaskPopupSheet popupSheet;
	private final boolean bEnabled;

	public SimpleSeriesButtonEntry(String buttonId, String popupName, ITaskPopupSheet popupSheet, boolean bEnabled) {
		this.buttonId = buttonId;
		this.popupName = popupName;
		this.popupSheet = popupSheet;
		this.bEnabled = bEnabled;
	}

	public String getButtonId() {
		return this.buttonId;
	}

	public String getPopupName() {
		return this.popupName;
	}

	public ITaskPopupSheet getPopupSheet() {
		return this.popupSheet;
	}

	public boolean isEnabled() {
		return this.bEnabled;
	}

}
