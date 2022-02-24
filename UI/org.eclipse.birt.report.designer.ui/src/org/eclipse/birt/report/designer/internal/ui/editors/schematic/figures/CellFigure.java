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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.widgets.Display;

/**
 * This is Cell figure for cell edit part.
 * 
 * 
 */
public class CellFigure extends ReportElementFigure {

	private String blankString;
	private boolean rtl; // bidi_hcg

	/**
	 * Constructor
	 */
	public CellFigure() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder(Graphics graphics) {
		// does nothing, table border layer paint it.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);

		if (blankString != null && blankString.length() > 0) {
			graphics.setForegroundColor(ReportColorConstants.ShadowLineColor);
			drawBlankString(graphics, blankString);
			graphics.restoreState();
		}

	}

	protected void drawBlankString(Graphics g, String s) {
		TextLayout tl = new TextLayout(Display.getCurrent());

		// bidi_hcg: Apply text direction
		tl.setOrientation(this.rtl ? SWT.RIGHT_TO_LEFT : SWT.LEFT_TO_RIGHT);

		tl.setText(s);
		Rectangle rc = tl.getBounds();

		int left = (getClientArea().width - rc.width) / 2;
		int top = (getClientArea().height - rc.height) / 2;

		g.drawText(s, getClientArea().x + left, getClientArea().y + top);
		tl.dispose();
	}

	/**
	 * @param blankString The blankString to set.
	 */
	public void setBlankString(String blankString) {
		this.blankString = blankString;
	}

	public String getBlankString() {
		return blankString;
	}

	/**
	 * @param rtl The RTL to flag to set.
	 */
	public void setDirectionRTL(boolean rtl) {
		this.rtl = rtl;
	}
}
