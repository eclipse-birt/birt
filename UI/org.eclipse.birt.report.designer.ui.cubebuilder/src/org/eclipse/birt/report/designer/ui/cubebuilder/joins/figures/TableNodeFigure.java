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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LabeledBorder;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * Figure for representing a Table object. This uses a ToolBarLayout to List all
 * the columns
 */
public class TableNodeFigure extends Panel
{

	private LabeledBorder frameBorder;
	Figure tableContainer;

	public TableNodeFigure( String name )
	{
		ToolbarLayout mainLayout = new ToolbarLayout( );
		mainLayout.setStretchMinorAxis( true );
		mainLayout.setVertical( true );
		mainLayout.setSpacing( 1 );
		this.setLayoutManager( mainLayout );
		this.setOpaque( true );

	}

	private boolean isFact;

	public TableNodeFigure( String name, boolean isFact )
	{
		this( name );
		this.isFact = isFact;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#useLocalCoordinates()
	 */
	protected boolean useLocalCoordinates( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize( int wHint, int hHint )
	{

		return getPreferredSize( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.IFigure#getMinimumSize(int, int)
	 */
	public IFigure getContents( )
	{

		return tableContainer;
	}

	/**
	 * Sets the color of the figure , when it is selected.
	 * 
	 */
	public void setSelectedColors( )
	{
		this.setOpaque( true );
		this.setForegroundColor( Display.getCurrent( )
				.getSystemColor( SWT.COLOR_LIST_FOREGROUND ) );
		( (TableBorderFigure) this.getBorder( ) ).setSelectedColors( isFact );
	}

	/**
	 * Sets the color of the figure when it is deselected.
	 * 
	 */
	public void setDeselectedColors( )
	{
		this.setOpaque( true );
		this.setForegroundColor( Display.getCurrent( )
				.getSystemColor( SWT.COLOR_LIST_FOREGROUND ) );
		( (TableBorderFigure) this.getBorder( ) ).setDeselectedColors( isFact );
	}

	/**
	 * @return The Name of the Table to be represented
	 */
	public String getName( )
	{
		return frameBorder.getLabel( );
	}

	/**
	 * @param The
	 *            Name of the table to be displayed
	 */
	public void setName( String string )
	{
		frameBorder.setLabel( string );
	}

}