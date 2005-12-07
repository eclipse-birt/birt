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

package org.eclipse.birt.report.engine.content.impl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.ir.CellDesign;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.9 $ $Date: 2005/11/17 16:50:44 $
 */
public class CellContent extends AbstractContent implements ICellContent
{

	/**
	 * row span
	 */
	protected int rowSpan = -1;

	/**
	 * col span, if equals to 1, then get it from the design.
	 */
	protected int colSpan = -1;
	
	/**
	 * column id, if equals to 0, get it from the design
	 */
	protected int column = -1;

	/**
	 * constructor use by serialize and deserialize
	 */
	public CellContent( )
	{
	}
	
	public int getContentType( )
	{
		return CELL_CONTENT;
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            cell design item
	 */
	public CellContent( ReportContent report )
	{
		super( report );
	}

	/**
	 * @return Returns the rowSpan.
	 */
	public int getRowSpan( )
	{
		return this.rowSpan;
	}

	/**
	 * 
	 * @return the column span
	 */
	public int getColSpan( )
	{
		return colSpan;
	}

	/**
	 * 
	 * @return the column number
	 */
	public int getColumn( )
	{
		return column;
	}
	
	public int getRow( )
	{
		if ( parent != null && parent instanceof IRowContent )
		{
			return ( (IRowContent) parent ).getRowID( );
		}
		return 0;
	}

	public void setDrop( String drop )
	{
		if ( generateBy instanceof CellDesign )
			( (CellDesign) generateBy ).setDrop( drop );
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitCell( this, value );
	}

	/**
	 * @param rowSpan
	 *            The rowSpan to set.
	 */
	public void setRowSpan( int rowSpan )
	{
		this.rowSpan = rowSpan;
	}

	public void setColSpan( int colSpan )
	{
		this.colSpan = colSpan;
	}
	
	public void setColumn( int column )
	{
		this.column = column;
	}
	
	static final protected int FIELD_ROW_SPAN = 100;
	static final protected int FIELD_COL_SPAN = 101;
	static final protected int FIELD_COLUMN = 102;
		
	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( rowSpan != -1 )
		{
			out.writeInt( FIELD_ROW_SPAN );
			out.writeInt( rowSpan );
		}
		if ( colSpan != -1 )
		{
			out.writeInt( FIELD_COL_SPAN );
			out.writeInt( colSpan );
		}
		if ( column != -1 )
		{
			out.writeInt( FIELD_COLUMN );
			out.writeInt( column );
		}
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_ROW_SPAN :
				rowSpan = in.readInt( );
				break;
			case FIELD_COL_SPAN :
				colSpan = in.readInt( );
				break;
			case FIELD_COLUMN :
				column = in.readInt( );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}