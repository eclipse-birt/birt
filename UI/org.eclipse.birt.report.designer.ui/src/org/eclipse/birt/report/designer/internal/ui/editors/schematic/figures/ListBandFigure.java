/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.layout.ListBandLayout;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;

/**
 * List Band figure
 * 
 */
public class ListBandFigure extends Figure implements ReportShowFigure {

	private IFigure contend;

	private IFigure controlFigure;

	private boolean state = true;

	/**
	 * Constructor
	 */
	public ListBandFigure() {
		setLayoutManager(new ListBandLayout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * ReportShowFigure#getContent()
	 */
	public IFigure getContent() {
		return contend;
	}

	/**
	 * @return control figure
	 */
	public IFigure getControlFigure() {
		return controlFigure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * ReportShowFigure#setShowing(boolean)
	 */
	public void setShowing(boolean bool) {
		state = bool;
		getParent().getParent().invalidateTree();
		getUpdateManager().addInvalidFigure(getParent().getParent());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * ReportShowFigure#setShowing(boolean)
	 */
	public boolean isControlShowing() {
		return state;
	}

	public void setContend(IFigure contend) {
		this.contend = contend;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#containsPoint(int, int)
	 */
	public boolean containsPoint(int x, int y) {
		return getContent().containsPoint(x, y) || getControlFigure().containsPoint(x, y);
	}

	/**
	 * Set control figure.
	 * 
	 * @param controlFigure
	 */
	public void setControlFigure(IFigure controlFigure) {
		this.controlFigure = controlFigure;
	}
}
