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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.editparts.GridLayer;

/**
 * Paint the grid
 *  
 */
public class TableGridLayer extends GridLayer
{

	private TableEditPart source;

	/**
	 * Constructor
	 * @param rows
	 * @param cells
	 */
	public TableGridLayer( TableEditPart source )
	{
		super( );
		this.source = source;
	}

	/**
	 * @return rows
	 */
	public List getRows( )
	{
		return source.getRows( );
	}

	/**
	 * @return columns
	 */
	public List getColumns( )
	{
		return source.getColumns( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.GridLayer#paintGrid(org.eclipse.draw2d.Graphics)
	 */
	protected void paintGrid( Graphics g )
	{

		Collections.sort( getRows( ), new NumberComparable( ) );
		Collections.sort( getColumns( ), new NumberComparable( ) );

		if ( !getColumns( ).isEmpty( ) )
		{

		}

		if ( !getRows( ).isEmpty( ) )
		{
		}

	}

	protected void drawRows( Graphics g )
	{
		Rectangle clip = g.getClip( Rectangle.SINGLETON );
		List rows = getRows( );
		int size = rows.size( );
		int height = 0;
		for ( int i = 0; i < size; i++ )
		{
			height = height + getRowHeight( rows.get( i ) );
			if ( height < clip.y + clip.height )
			{
				g.drawLine( clip.x, height, clip.x + clip.width, height );
			}
		}

	}

	protected void drawColumns( Graphics g )
	{
		Rectangle clip = g.getClip( Rectangle.SINGLETON );
		List columns = getColumns( );
		int size = columns.size( );
		int width = 0;
		for ( int i = 0; i < size; i++ )
		{
			width = width + getColumnWidth( columns.get( i ) );
			if ( width < clip.x + clip.width )
			{
				g.drawLine( width, clip.y, width, clip.y + clip.height );

			}
		}

	}

	private int getRowHeight( Object row )
	{
		return TableUtil.caleVisualHeight( source, row );
	}

	private int getColumnWidth( Object column )
	{
		return TableUtil.caleVisualWidth( source, column );
	}

	/**
	 * Sorter to be used to sort the rows with row number
	 * 
	 */
	public static class NumberComparable implements Comparator
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare( Object o1, Object o2 )
		{
			//TODO: sort the row with row number
			return 0;
		}

	}
}