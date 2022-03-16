/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

	@Override
	Insets getDefaultPaddingInsets() {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	@Override
	public Insets getInsets(IFigure figure) {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#getBorderInsets()
	 */
	@Override
	public Insets getBorderInsets() {
		return DEFAULTINSETS;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.
	 * BaseBorder#setPaddingInsets(org.eclipse.draw2d.geometry.Insets)
	 */
	@Override
	public void setPaddingInsets(Insets in) {
		// does nothing, ignore the padding.
	}

}
