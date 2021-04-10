/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

/**
 * A border class for Table/Grid.
 */

public class TableBorder extends SectionBorder {

	/**
	 * Table/Grid uses Zero as default padding insets.
	 */
	private static final Insets DEFAULTINSETS = new Insets(0, 0, 0, 0);

	Insets getDefaultPaddingInsets() {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#getBorderInsets()
	 */
	public Insets getBorderInsets() {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	public void setPaddingInsets(Insets in) {
		// does nothing, ignore the padding.
	}

}