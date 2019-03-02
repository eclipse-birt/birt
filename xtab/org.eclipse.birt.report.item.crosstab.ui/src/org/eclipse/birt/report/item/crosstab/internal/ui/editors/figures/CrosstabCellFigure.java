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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.CellFigure;
import org.eclipse.draw2d.Graphics;

/**
 * Crosstab cell figure
 */
// NOTE if this class have bo code, will be remove
public class CrosstabCellFigure extends CellFigure
{

	protected void paintFigure( Graphics graphics )
	{
		super.paintFigure( graphics );

		if ( getBlankString( ) != null && getBlankString( ).length( ) > 0 )
		{
			graphics.setForegroundColor( ReportColorConstants.DarkShadowLineColor );
			drawBlankString( graphics, getBlankString( ) );
			graphics.restoreState( );
		}

	}
}
