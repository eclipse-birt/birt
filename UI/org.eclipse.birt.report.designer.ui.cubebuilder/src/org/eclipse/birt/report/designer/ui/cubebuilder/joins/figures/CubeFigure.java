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

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScalableLayeredPane;
import org.eclipse.draw2d.XYLayout;

/**
 * Report Editot root editpart.
 * 
 */
public class CubeFigure extends ScalableLayeredPane {

	public CubeFigure() {
		setLayoutManager(new XYLayout());
	}

	/*
	 * Do nothing
	 * 
	 * @see org.eclipse.draw2d.Figure#paintBorder(org.eclipse.draw2d.Graphics)
	 */
	protected void paintBorder(Graphics graphics) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure(Graphics graphics) {
		super.paintFigure(graphics);
		graphics.setForegroundColor(ReportColorConstants.MarginBorderColor);
	}

}
