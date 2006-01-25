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

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * Band used in a TableItem.
 * 
 * @version $Revision: 1.6 $ $Date: 2005/11/11 06:26:41 $
 */
public class TableBandDesign extends ReportElementDesign
{
	public static final int TABLE_DETAIL = 0;
	public static final int TABLE_HEADER = 1;
	public static final int TABLE_FOOTER = 2;
	public static final int GROUP_HEADER = 3;
	public static final int GROUP_FOOTER = 4;
	
	public static final int DEFAULT_BAND_LEVEL = -1;
	/*
	 * bandType is used to output the row type.
	 */
	private int bandType = TABLE_DETAIL;
	
	/*
	 * band level is used to output the row type when row is contains in the
	 * band and the band it in the group header or footer.
	 */
	private int bandLevel = DEFAULT_BAND_LEVEL;
	
	/**
	 * rows defined in this band. items are RowType.
	 */
	protected ArrayList rows = new ArrayList( );

	public int getBandLevel( )
	{
		return bandLevel;
	}
	
	public void setBandLevel( int bandLevel )
	{
		this.bandLevel = bandLevel;
	}
	/**
	 * get band type 
	 * @return the band type
	 */
	public int getBandType( )
	{
		return bandType;
	}
	
	/**
	 * set band type
	 * @param bandType the band type
	 */
	public void setBandType( int bandType )
	{
		this.bandType = bandType;
	}
	/**
	 * get the row number defined in this band.
	 * 
	 * @return row number
	 */
	public int getRowCount( )
	{
		return this.rows.size( );
	}

	/**
	 * add a row definition in this band.
	 * 
	 * @param row
	 *            row to be added.
	 */
	public void addRow( RowDesign row )
	{
		assert ( row != null );
		this.rows.add( row );
	}

	/**
	 * get row in this band.
	 * 
	 * @param index
	 *            row index
	 * @return row.
	 */
	public RowDesign getRow( int index )
	{
		assert ( index >= 0 && index < rows.size( ) );
		return (RowDesign) this.rows.get( index );
	}
}
