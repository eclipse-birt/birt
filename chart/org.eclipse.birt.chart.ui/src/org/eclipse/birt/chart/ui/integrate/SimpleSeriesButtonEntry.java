/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

	@Override
	public String getButtonId() {
		return this.buttonId;
	}

	@Override
	public String getPopupName() {
		return this.popupName;
	}

	@Override
	public ITaskPopupSheet getPopupSheet() {
		return this.popupSheet;
	}

	@Override
	public boolean isEnabled() {
		return this.bEnabled;
	}

}
