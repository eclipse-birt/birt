/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/ 

package org.eclipse.birt.report.designer.internal.lib.editors.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;


/**
 * The figure show in the library editor when the seleection object form outline is not a visual element.
 * 
 */
public class EmptyFigure extends Figure
{
	private static final String EMPTYTEXT = "Library currently empty, add components from the palette or the menu to the library outline menu.";
	public EmptyFigure()
	{
		setBackgroundColor(ReportColorConstants.greyFillColor);
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );
		graphics.setBackgroundColor(ReportColorConstants.greyFillColor);
		
		Rectangle rect = getBounds();
		graphics.fillRectangle(rect);
		graphics.drawText(EMPTYTEXT, rect.x,rect.y);
	}
}
