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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.CellComputedStyle;
import org.eclipse.birt.report.engine.ir.CellDesign;

/**
 * 
 * cell content object Implement IContentContainer interface the content of cell
 * can be any report item
 * 
 * @version $Revision: 1.13 $ $Date: 2006/01/20 14:55:38 $
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
	 * Flag indicading if this cell is the start of a group.
	 */
	protected boolean isStartOfGroup = false;

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

	public IStyle getComputedStyle( )
	{
		if ( computedStyle == null )
		{
			computedStyle = new CellComputedStyle( this );
		}
		return computedStyle;
	}

	static final protected int FIELD_ROW_SPAN = 100;
	static final protected int FIELD_COL_SPAN = 101;
	static final protected int FIELD_COLUMN = 102;
	static final protected int FIELD_START_OF_GROUP = 103;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( rowSpan != -1 )
		{
			IOUtil.writeInt( out, FIELD_ROW_SPAN );
			IOUtil.writeInt( out, rowSpan );
		}
		if ( colSpan != -1 )
		{
			IOUtil.writeInt( out, FIELD_COL_SPAN );
			IOUtil.writeInt( out, colSpan );
		}
		if ( column != -1 )
		{
			IOUtil.writeInt( out, FIELD_COLUMN );
			IOUtil.writeInt( out, column );
		}
		if ( isStartOfGroup )
		{
			IOUtil.writeInt( out, FIELD_START_OF_GROUP );
			IOUtil.writeBool( out, isStartOfGroup );
		}
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_ROW_SPAN :
				rowSpan = IOUtil.readInt( in );
				break;
			case FIELD_COL_SPAN :
				colSpan = IOUtil.readInt( in );
				break;
			case FIELD_COLUMN :
				column = IOUtil.readInt( in );
				break;
			case FIELD_START_OF_GROUP :
				isStartOfGroup = IOUtil.readBool( in );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

	public boolean isStartOfGroup( )
	{
		return isStartOfGroup;
	}

	public void setStartOfGroup( boolean isStartOfGroup )
	{
		this.isStartOfGroup = isStartOfGroup;
	}
}