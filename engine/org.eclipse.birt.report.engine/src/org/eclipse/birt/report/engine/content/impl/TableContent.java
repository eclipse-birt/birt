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
 * @version $Revision: 1.9 $ $Date: 2005/11/22 19:25:39 $
 */
public class TableContent extends AbstractContent implements ITableContent
{

	private static final long serialVersionUID = 2267750727901854517L;
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
}