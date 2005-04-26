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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;

/**
 * CellEditorLocator for label.
 *  
 */
final public class LabelCellEditorLocator implements CellEditorLocator
{

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
	public LabelCellEditorLocator( Figure l )
	{
		setLabel( l );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.tools.CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
	 */
	public void relocate( CellEditor celleditor )
	{
		Text text = (Text) celleditor.getControl( );

		Rectangle rect = ( (LabelFigure) figure ).getEditorArea( );
		figure.translateToAbsolute( rect );

		int xOffset = 0;
		int wOffset = 0;
		int yOffset = 0;
		int hOffset = 0;

		if ( SWT.getPlatform( ).equalsIgnoreCase( "gtk" ) ) { //$NON-NLS-1$
			xOffset = GTK_X_OFFSET;
			wOffset = GTK_W_OFFSET;
		}
		else if ( SWT.getPlatform( ).equalsIgnoreCase( "carbon" ) ) { //$NON-NLS-1$
			xOffset = MAC_X_OFFSET;
			wOffset = MAC_W_OFFSET;
			yOffset = MAC_Y_OFFSET;
			hOffset = MAC_H_OFFSET;
		}
		else
		{
			xOffset = WIN_X_OFFSET;
			wOffset = WIN_W_OFFSET;
		}

		text.setBounds( rect.x + xOffset, rect.y + yOffset, rect.width
				+ wOffset, rect.height + hOffset );
	}

	/**
	 * Returns the figure.
	 */
	protected Figure getLabel( )
	{
		return figure;
	}

	/**
	 * Sets the figure.
	 * 
	 * @param l
	 *            The figure to set
	 */
	protected void setLabel( Figure l )
	{
		this.figure = l;
	}

}