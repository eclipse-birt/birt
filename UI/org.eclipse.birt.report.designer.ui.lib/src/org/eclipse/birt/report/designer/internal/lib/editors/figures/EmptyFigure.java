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

package org.eclipse.birt.report.designer.internal.lib.editors.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * The figure show in the library editor when the seleection object form outline
 * is not a visual element.
 *
 */
public class EmptyFigure extends LabelFigure {

	// private String text;

	public EmptyFigure() {
		setOpaque(true);
//		setBackgroundColor( ReportColorConstants.greyFillColor );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.
	 * LabelFigure#getPreferredSize(int, int)
	 */
	@Override
	public Dimension getPreferredSize(int wHint, int hHint) {
		if (prefSize != null) {
			return prefSize;
		}
		return getSize();
	}
}
