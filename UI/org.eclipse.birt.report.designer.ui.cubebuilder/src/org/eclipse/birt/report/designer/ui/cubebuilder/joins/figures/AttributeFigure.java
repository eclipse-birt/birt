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
