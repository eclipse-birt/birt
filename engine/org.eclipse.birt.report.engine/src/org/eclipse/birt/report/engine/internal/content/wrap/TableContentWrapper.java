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

package org.eclipse.birt.report.engine.internal.content.wrap;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;

/**
 * 
 * the table content object which contains columns object and row objects
 * 
 * @version $Revision: 1.13 $ $Date: 2006/01/20 14:55:38 $
 */
public class TableContentWrapper extends AbstractContentWrapper
		implements
			ITableContent
{

	protected ITableContent tableContent;

	protected TableBandContentWrapper footer;
	protected TableBandContentWrapper body;
	protected TableBandContentWrapper header;

	/**
	 * constructor
	 * 
	 * @param item
	 *            the table deign
	 */
	public TableContentWrapper( ITableContent content )
	{
		super( content );
		tableContent = content;
	}

	public void accept( IContentVisitor visitor, Object value )
	{
		visitor.visitTable( this, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#addColumn(org.eclipse.birt.report.engine.content.IColumn)
	 */
	public void addColumn( IColumn column )
	{
		tableContent.addColumn( column );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getBody()
	 */
	public ITableBandContent getBody( )
	{
		if ( body == null )
		{
			body = new TableBandContentWrapper( tableContent.getBody( ) );
		}
		return body;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getCaption()
	 */
	public String getCaption( )
	{
		return tableContent.getCaption( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getColumn(int)
	 */
	public IColumn getColumn( int index )
	{
		return tableContent.getColumn( index );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getColumnCount()
	 */
	public int getColumnCount( )
	{
		return tableContent.getColumnCount( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getFooter()
	 */
	public ITableBandContent getFooter( )
	{
		if ( footer == null )
		{
			footer = new TableBandContentWrapper( tableContent.getBody( ) );
		}
		return footer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#getHeader()
	 */
	public ITableBandContent getHeader( )
	{
		if ( header == null )
		{
			header = new TableBandContentWrapper( tableContent.getBody( ) );
		}
		return header;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.content.ITableContent#isHeaderRepeat()
	 */
	public boolean isHeaderRepeat( )
	{
		return tableContent.isHeaderRepeat( );
	}

}