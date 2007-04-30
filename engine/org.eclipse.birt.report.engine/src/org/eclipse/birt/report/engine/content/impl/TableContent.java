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
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * 
 * the table content object which contains columns object and row objects
 * 
 */
public class TableContent extends AbstractContent implements ITableContent
{

	protected ArrayList columns = new ArrayList( );
	protected String caption;
	protected String captionKey;

	protected Boolean headerRepeat;

	public int getContentType( )
	{
		return TABLE_CONTENT;
	}

	public void setHeaderRepeat( boolean headerRepeat )
	{
		if (generateBy instanceof TableItemDesign)
		{
			if ( ( (TableItemDesign) generateBy ).isRepeatHeader( ) == headerRepeat )
			{
				this.headerRepeat = null;
				return;
			}
		}
		this.headerRepeat = Boolean.valueOf( headerRepeat );
	}

	public boolean isHeaderRepeat( )
	{
		if ( headerRepeat != null )
		{
			return headerRepeat.booleanValue( );
		}
		if ( generateBy instanceof TableItemDesign )
		{
			return ( (TableItemDesign) generateBy ).isRepeatHeader( );
		}

		return false;
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            the table deign
	 */
	public TableContent( IReportContent report )
	{
		super( report );
	}

	public Object accept( IContentVisitor visitor, Object value )
	{
		return visitor.visitTable( this, value );
	}

	/**
	 * @return Returns the caption.
	 */
	public String getCaption( )
	{
		if ( caption == null )
		{
			if ( generateBy instanceof TableItemDesign )
			{
				return ( (TableItemDesign) generateBy ).getCaption( );
			}
		}
		return caption;
	}

	/**
	 * @param caption
	 *            The caption to set.
	 */
	public void setCaption( String caption )
	{
		this.caption = caption;
	}

	public void setCaptionKey( String key )
	{
		this.captionKey = key;
	}

	public String getCaptionKey( )
	{
		if ( captionKey == null )
		{
			if ( generateBy instanceof TableItemDesign )
				return ( (TableItemDesign) generateBy ).getCaptionKey( );
		}
		return captionKey;
	}

	public int getColumnCount( )
	{
		return columns.size( );
	}

	public IColumn getColumn( int index )
	{
		return (IColumn) columns.get( index );
	}

	public void addColumn( IColumn column )
	{
		this.columns.add( column );
	}

	public ITableBandContent getHeader( )
	{
		return getTableBand( ITableBandContent.BAND_HEADER );
	}

	public ITableBandContent getFooter( )
	{
		return getTableBand( ITableBandContent.BAND_FOOTER );
	}

	protected ITableBandContent getTableBand( int type )
	{
		ITableBandContent tableBand;
		if ( children == null )
		{
			return null;
		}
		Iterator iter = children.iterator( );
		while ( iter.hasNext( ) )
		{
			Object child = iter.next( );
			if ( child instanceof ITableBandContent )
			{
				tableBand = (ITableBandContent) child;
				if ( tableBand.getBandType( ) == type )
				{
					return tableBand;
				}
			}
		}
		return null;
	}

	static final protected short FIELD_COLUMNS = 1000;
	static final protected short FIELD_CAPTION = 1001;
	static final protected short FIELD_CAPTIONKEY = 1002;
	static final protected short FIELD_HEADERREPEAT = 1003;

	protected void writeFields( DataOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( columns != null )
		{
			IOUtil.writeShort( out, FIELD_COLUMNS );
			Column column;
			IOUtil.writeInt( out, columns.size( ) );
			for ( int i = 0; i < columns.size( ); i++ )
			{
				column = (Column) columns.get( i );
				column.writeObject( out );
			}
		}
		if ( caption != null )
		{
			IOUtil.writeShort( out, FIELD_CAPTION );
			IOUtil.writeString( out, caption );
		}
		if ( captionKey != null )
		{
			IOUtil.writeShort( out, FIELD_CAPTIONKEY );
			IOUtil.writeString( out, captionKey );
		}
		if ( headerRepeat != null )
		{
			IOUtil.writeShort( out, FIELD_HEADERREPEAT );
			IOUtil.writeBool( out, headerRepeat.booleanValue( ) );
		}
	}

	public boolean needSave( )
	{
		return true;
	}

	protected void readField( int version, int filedId, DataInputStream in )
			throws IOException
	{
		switch ( filedId )
		{
			case FIELD_COLUMNS :
				int columnsSize = IOUtil.readInt( in );
				for ( int i = 0; i < columnsSize; i++ )
				{
					Column column = new Column( report );
					column.readObject( in );
					addColumn( column );
				}
				break;
			case FIELD_CAPTION :
				caption = IOUtil.readString( in );
				break;
			case FIELD_CAPTIONKEY :
				captionKey = IOUtil.readString( in );
				break;
			case FIELD_HEADERREPEAT :
				headerRepeat = Boolean.valueOf( IOUtil.readBool( in ) );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}

}
