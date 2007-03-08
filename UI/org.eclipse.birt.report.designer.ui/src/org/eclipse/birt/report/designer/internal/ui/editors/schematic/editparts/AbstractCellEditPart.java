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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutCell;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Abstract class for the cell editpart
 */
public abstract class AbstractCellEditPart extends ReportElementEditPart implements
		ITableLayoutCell
{

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public AbstractCellEditPart( Object model )
	{
		super( model );
	}

	/*
	 * Gets the paint layer (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getLayer(java.lang.Object)
	 */
	public IFigure getLayer( Object key )
	{
		if ( getParent( ) instanceof AbstractTableEditPart )
		{
			return ( (AbstractTableEditPart) getParent( ) ).getLayer( key );
		}
		return super.getLayer( key );
	}

	/**
	 * Gets the edit part bounds
	 * 
	 * @return the edit part bounds
	 */
	public Rectangle getBounds( )
	{
		return getFigure( ).getBounds( );
	}
}
