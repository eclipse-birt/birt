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

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class AttributeFigure extends ColumnFigure {
	/**
	 * Sets the background and foreground color when the Column is selected.
	 */
	public void setSelectedColors() {
		this.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		this.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_SELECTION));
	}

	/**
	 * Sets the background and foreground color when the Column is deselected.
	 */
	public void setDeselectedColors() {
		this.setBackgroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		this.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
	}

}
