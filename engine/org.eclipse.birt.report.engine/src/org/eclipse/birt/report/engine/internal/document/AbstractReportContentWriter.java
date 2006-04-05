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

package org.eclipse.birt.report.engine.internal.document;

import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;

/**
 * writer used to create the content stream.
 *
 * @version $Revision:$ $Date:$
 */
abstract public class AbstractReportContentWriter
		implements
			IReportContentWriter
{

	protected static Logger logger = Logger
			.getLogger( IReportContentWriter.class.getName( ) );

	public long writeFullContent( IContent content ) throws IOException
	{
		long offset = getOffset();
		new ContentWriterVisitor( ).write( content, this );
		return offset;
	}

	/**
	 * use to writer the content into the disk.
	 * 
	 * @version $Revision: 1.9 $ $Date: 2006/02/20 05:28:16 $
	 */
	private class ContentWriterVisitor extends ContentVisitorAdapter
	{

		public void write( IContent content, IReportContentWriter writer )
		{
			visit( content, writer );
		}

		public void visitContent( IContent content, Object value )
		{
			IReportContentWriter writer = (IReportContentWriter) value;
			try
			{
				writer.writeContent( content );
				Iterator iter = content.getChildren( ).iterator( );
				while ( iter.hasNext( ) )
				{
					IContent child = (IContent) iter.next( );
					visitContent( child, value );
				}
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "write content failed" );
			}
		}

		public void visitPage( IPageContent page, Object value )
		{
			IReportContentWriter writer = (IReportContentWriter) value;
			try
			{
				writer.writeContent( page );
				// output all the page header
				Iterator iter = page.getHeader( ).iterator( );
				while ( iter.hasNext( ) )
				{
					IContent content = (IContent) iter.next( );
					visitContent( content, value );
				}
				// output all the page footer
				iter = page.getFooter( ).iterator( );
				while ( iter.hasNext( ) )
				{
					IContent content = (IContent) iter.next( );
					visitContent( content, value );
				}
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "write content failed" );
			}

		}

		public void visitTable( ITableContent table, Object value )
		{
			IReportContentWriter writer = (IReportContentWriter) value;
			try
			{
				writer.writeContent( table );
				ITableBandContent header = table.getHeader( );
				if ( header != null )
				{
					visitContent( header, value );
				}
				ITableBandContent footer = table.getFooter( );
				if ( footer != null )
				{
					visitContent( footer, value );
				}
				ITableBandContent body = table.getBody( );
				if ( body != null )
				{
					visitContent( body, value );
				}
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "write content failed" );
			}
		}
	}

}
