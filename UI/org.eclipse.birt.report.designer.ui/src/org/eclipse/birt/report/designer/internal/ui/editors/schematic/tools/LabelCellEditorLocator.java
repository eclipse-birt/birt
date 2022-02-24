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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

/**
 * CellEditorLocator for label.
 * 
 */
final public class LabelCellEditorLocator implements CellEditorLocator {

	private static Dimension MINSIZE = new Dimension(20, 8);
	private Figure figure;

	private static int WIN_X_OFFSET = -4;

	private static int WIN_W_OFFSET = 5;

	private static int GTK_X_OFFSET = 0;

	private static int GTK_W_OFFSET = 0;

	private static int MAC_X_OFFSET = -3;

	private static int MAC_W_OFFSET = 9;

	private static int MAC_Y_OFFSET = -3;

	private static int MAC_H_OFFSET = 6;

	/**
	 * Constructor
	 * 
	 * @param l
	 */
	public LabelCellEditorLocator(Figure l) {
		setLabel(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.
	 * CellEditor)
	 */
	public void relocate(CellEditor celleditor) {
		Text text = (Text) celleditor.getControl();

		Rectangle rect = ((LabelFigure) figure).getEditorArea();
		figure.translateToAbsolute(rect);

		int xOffset = 0;
		int wOffset = 0;
		int yOffset = 0;
		int hOffset = 0;

		if (SWT.getPlatform().equalsIgnoreCase("gtk")) { //$NON-NLS-1$
			xOffset = GTK_X_OFFSET;
			wOffset = GTK_W_OFFSET;
		} else if (SWT.getPlatform().equalsIgnoreCase("carbon")) { //$NON-NLS-1$
			xOffset = MAC_X_OFFSET;
			wOffset = MAC_W_OFFSET;
			yOffset = MAC_Y_OFFSET;
			hOffset = MAC_H_OFFSET;
		} else {
			xOffset = WIN_X_OFFSET;
			wOffset = WIN_W_OFFSET;
		}

		boolean isInline = DesignChoiceConstants.DISPLAY_INLINE.equals(((LabelFigure) figure).getDisplay());
		if (isInline) {
			org.eclipse.swt.graphics.Rectangle trim = text.computeTrim(0, 0, 0, 0);
			rect.translate(trim.x, trim.y);
			rect.width += trim.width;
			rect.height += trim.height;
		}
		int width = rect.width + wOffset;
		if (width < MINSIZE.width) {
			width = MINSIZE.width;
		}
		int height = rect.height + hOffset;
		if (height < MINSIZE.height) {
			height = MINSIZE.height;
		}
		text.setBounds(rect.x + xOffset, rect.y + yOffset, width, height);
	}

	/**
	 * Returns the figure.
	 */
	protected Figure getLabel() {
		return figure;
	}

	/**
	 * Sets the figure.
	 * 
	 * @param l The figure to set
	 */
	protected void setLabel(Figure l) {
		this.figure = l;
	}

}
