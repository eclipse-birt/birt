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
import java.util.ArrayList;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

/**
 * 
 * the table content object which contains columns object and row objects
 * 
 * @version $Revision: 1.10 $ $Date: 2005/11/25 02:14:05 $
 */
public class TableContent extends AbstractContent implements ITableContent
{

	protected ArrayList columns = new ArrayList( );
	protected String caption;
	protected String captionKey;

	protected boolean headerRepeat;

	/**
	 * constructor. use by serialize and deserialize
	 */
	public TableContent( )
	{
	}

	public int getContentType( )
	{
		return TABLE_CONTENT;
	}

	public void setHeaderRepeat( boolean headerRepeat )
	{
		this.headerRepeat = headerRepeat;
	}

	public boolean isHeaderRepeat( )
	{
		return headerRepeat;
	}

	/**
	 * constructor
	 * 
	 * @param item
	 *            the table deign
	 */
	public TableContent( ReportContent report )
	{
		super( report );
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitTable( this, value );
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

	public ITableBandContent getBody( )
	{
		return getTableBand( ITableBandContent.BAND_BODY );
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
		for ( int i = 0; i < children.size( ); i++ )
		{
			tableBand = (ITableBandContent) children.get( i );
			if ( tableBand.getType( ) == type )
			{
				return tableBand;
			}
		}
		return null;
	}
	
	static final protected int FIELD_COLUMNS = 1000;
	static final protected int FIELD_CAPTION = 1001;
	static final protected int FIELD_CAPTIONKEY = 1002;
	static final protected int FIELD_HEADERREPEAT = 1003;
	
	
	protected void writeFields( ObjectOutputStream out ) throws IOException
	{
		super.writeFields( out );
		if ( columns != null )
		{
			out.writeInt( FIELD_COLUMNS );
			Column column;
			out.writeInt( columns.size() );
			for ( int i=0;i<columns.size();i++ )
			{
				column = ( Column ) columns.get( i );
				column.writeContent( out );
			}
		}
		if ( caption != null )
		{
			out.writeInt( FIELD_CAPTION );
			out.writeUTF( caption );
		}
		if ( captionKey != null )
		{
			out.writeInt( FIELD_CAPTIONKEY );
			out.writeUTF( captionKey );
		}
		out.writeInt( FIELD_HEADERREPEAT );
		out.writeBoolean( headerRepeat );
	}

	protected void readField( int version, int filedId, ObjectInputStream in )
			throws IOException, ClassNotFoundException
	{
		switch ( filedId )
		{
			case FIELD_COLUMNS :
				int columnsSize = in.readInt( );
				for ( int i=0;i<columnsSize;i++ ) {
					Column column = new Column();
					column.readContent( in );
					addColumn( column );
				}
				break;
			case FIELD_CAPTION :
				caption = in.readUTF( );
				break;
			case FIELD_CAPTIONKEY :
				captionKey = in.readUTF( );
				break;
			case FIELD_HEADERREPEAT :
				headerRepeat = in.readBoolean( );
				break;
			default :
				super.readField( version, filedId, in );
		}
	}
}