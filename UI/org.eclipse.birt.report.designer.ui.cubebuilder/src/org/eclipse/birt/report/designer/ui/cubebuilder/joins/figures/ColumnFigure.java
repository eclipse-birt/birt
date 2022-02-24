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

import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Display;

/**
 * The Figure representing a Column in a Table, on the Joins page of the Data
 * Set Wizard
 * 
 */
public class ColumnFigure extends Figure {

	// on windows, only one fontdata in array, not true for X-font
	private static final FontData fontData = Display.getCurrent().getSystemFont().getFontData()[0];

	protected final Font selectedFont = FontManager.getFont(fontData.getName(), fontData.getHeight(), SWT.BOLD);

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
		this.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
	}

	/**
	 * Sets the Font when the column is selected
	 */
	public void setSelectedFonts() {
		((IFigure) this.getChildren().get(0)).setFont(selectedFont);
		repaint();
	}

	/**
	 * Sets the Font when the Column is deselected
	 */
	public void setDeselectedFonts() {
		((IFigure) this.getChildren().get(0)).setFont(Display.getCurrent().getSystemFont());
		repaint();
	}
}
