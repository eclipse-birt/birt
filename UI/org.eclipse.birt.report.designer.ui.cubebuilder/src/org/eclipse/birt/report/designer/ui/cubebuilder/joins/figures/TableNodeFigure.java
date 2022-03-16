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

import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * Figure for representing a Table object. This uses a ToolBarLayout to List all
 * the columns
 */
public class TableNodeFigure extends Panel {

	public TableNodeFigure(String name) {
		ToolbarLayout mainLayout = new ToolbarLayout();
		mainLayout.setStretchMinorAxis(true);
		mainLayout.setVertical(true);
		mainLayout.setSpacing(1);
		this.setLayoutManager(mainLayout);
		this.setOpaque(true);

	}

	private boolean isFact;

	public TableNodeFigure(String name, boolean isFact) {
		this(name);
		this.isFact = isFact;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	@Override
	protected boolean useLocalCoordinates() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	@Override
	public Dimension getMinimumSize(int wHint, int hHint) {

		return getPreferredSize();
	}

	/**
	 * Sets the color of the figure , when it is selected.
	 *
	 */
	public void setSelectedColors() {
		this.setOpaque(true);
		this.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		((TableBorderFigure) this.getBorder()).setSelectedColors(isFact);
	}

	/**
	 * Sets the color of the figure when it is deselected.
	 *
	 */
	public void setDeselectedColors() {
		this.setOpaque(true);
		this.setForegroundColor(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_FOREGROUND));
		((TableBorderFigure) this.getBorder()).setDeselectedColors(isFact);
	}

}
