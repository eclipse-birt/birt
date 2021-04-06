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

package org.eclipse.birt.chart.ui.swt.interfaces;

/**
 * Used to store customized data for button which will be added into value
 * series composite
 */

public interface ISeriesButtonEntry {

	String getButtonId();

	String getPopupName();

	ITaskPopupSheet getPopupSheet();

	boolean isEnabled();
}
