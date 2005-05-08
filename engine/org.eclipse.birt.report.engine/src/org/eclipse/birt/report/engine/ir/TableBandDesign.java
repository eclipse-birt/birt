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
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class TableBandDesign
{

	/**
	 * rows defined in this band. items are RowType.
	 */
	protected ArrayList rows = new ArrayList( );

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
