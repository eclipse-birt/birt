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

package org.eclipse.birt.report.engine.internal.document.v3;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.AbstractContent;
import org.eclipse.birt.report.engine.content.impl.AutoTextContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ListBandContent;
import org.eclipse.birt.report.engine.content.impl.ListContent;
import org.eclipse.birt.report.engine.content.impl.ListGroupContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TableGroupContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;

/**
 * read the content from the content stream.
 * 
 */
public class ReportContentReaderV3
{

	protected static Logger logger = Logger
			.getLogger( ReportContentReaderV3.class.getName( ) );

	protected ReportContent reportContent;
	protected RAInputStream stream;
	protected int version = -1;

	protected final static int INDEX_ENTRY_SIZE_V0 = 40;
	protected final static int INDEX_ENTRY_SIZE_V1 = 24;

	protected final static int VERSION_0 = 0;
	protected final static int VERSION_1 = 1;
	protected final static int VERSION_SIZE = 4;

	/**
	 * the current offset of the stream.
	 */
	protected long offset;

	protected boolean isEmpty = false;

	public ReportContentReaderV3( ReportContent reportContent,
			RAInputStream stream ) throws IOException
	{
		this.reportContent = reportContent;
		this.stream = stream;
		long length = stream.length( );
		if ( this.stream.length( ) >= 4 )
		{
			stream.seek( 0 );
			int iVersion = stream.readInt( );
			if ( -1 == iVersion )
			{
				version = VERSION_0;
			}
			else
			{
				version = iVersion;
				if ( version == VERSION_1 && length == 4 )
				{
					isEmpty = true;
				}
			}
		}
		else
		{
			throw new IOException( "unrecognized stream version!" );
		}
	}

	public boolean isEmpty( )
	{
		return isEmpty;
	}

	public void close( )
	{
		if ( stream != null )
		{
			try
			{
				stream.close( );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "Failed to close the reader", ex );
			}
			stream = null;
		}
	}

	/**
	 * read the content object from the input stream.
	 * 
	 * @param oi
	 *            the input stream.
	 * @return the object read out.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected IContent readObject( long offset ) throws IOException
	{
		if ( VERSION_0 == version )
		{
			stream.seek( offset );
		}
		else if ( VERSION_1 == version )
		{
			stream.seek( VERSION_SIZE + offset );
		}
		else
		{
			throw new IOException( "unrecognized stream version!" );
		}

		int size = stream.readInt( );
		byte[] buffer = new byte[size];
		stream.readFully( buffer, 0, size );
		DataInputStream oi = new DataInputStream( new ByteArrayInputStream(
				buffer ) );
		AbstractContent object = null;
		int contentType = IOUtil.readInt( oi );
		switch ( contentType )
		{
			case IContent.CELL_CONTENT :
				object = new CellContent( reportContent );
				break;
			case IContent.CONTAINER_CONTENT :
				object = new ContainerContent( reportContent );
				break;
			case IContent.DATA_CONTENT :
				object = new DataContent( reportContent );
				break;
			case IContent.FOREIGN_CONTENT :
				object = new ForeignContent( reportContent );
				break;
			case IContent.IMAGE_CONTENT :
				object = new ImageContent( reportContent );
				break;
			case IContent.LABEL_CONTENT :
				object = new LabelContent( reportContent );
				break;
			case IContent.PAGE_CONTENT :
				object = new PageContent( reportContent );
				break;
			case IContent.ROW_CONTENT :
				object = new RowContent( reportContent );
				break;
			case IContent.TABLE_BAND_CONTENT :
				object = new TableBandContent( reportContent );
				break;
			case IContent.TABLE_CONTENT :
				object = new TableContent( reportContent );
				break;
			case IContent.TEXT_CONTENT :
				object = new TextContent( reportContent );
				break;
			case IContent.AUTOTEXT_CONTENT :
				object = new AutoTextContent( reportContent );
				break;
			case IContent.LIST_CONTENT :
				object = new ListContent( reportContent );
				break;
			case IContent.LIST_BAND_CONTENT :
				object = new ListBandContent( reportContent );
				break;
			case IContent.LIST_GROUP_CONTENT :
				object = new ListGroupContent( reportContent );
				break;
			case IContent.TABLE_GROUP_CONTENT :
				object = new TableGroupContent( reportContent );
				break;
			default :
				// Not expected
				throw new IOException( "Found invalid contentType"
						+ contentType + " at object offset " + offset );
		}
		object.setVersion( version );
		object.readContent( oi );
		return object;
	}

	/**
	 * read the content object out from the input stream in the curretn offset.
	 * After call this methods, the offset is position to the next element in
	 * pre-depth order.
	 * 
	 * The content's parent is loaded in this time.
	 * 
	 * @return the object read out.
	 */
	public IContent readContent( ) throws IOException
	{
		long index = offset;

		// load the content from the stream
		IContent content = readContent( index );

		// try to locate the next element
		DocumentExtension docExt = (DocumentExtension) content
				.getExtension( IContent.DOCUMENT_EXTENSION );
		// the next element is its child if exits
		if ( docExt.getFirstChild( ) != -1 )
		{
			offset = docExt.getFirstChild( );
			return content;
		}
		// otherise use it's sibling if exists
		if ( docExt.getNext( ) != -1 )
		{
			offset = docExt.getNext( );
			return content;
		}
		// or use the parent's sibling if exits
		docExt = readDocumentExtension( docExt.getParent( ) );
		while ( docExt != null )
		{

			if ( docExt.getNext( ) != -1 )
			{
				offset = docExt.getNext( );
				return content;
			}
			docExt = readDocumentExtension( docExt.getParent( ) );
		}

		offset = -1;

		return content;
	}

	public IContent readContent( long index ) throws IOException
	{

		if ( VERSION_0 == version )
		{
			return readContentV0( index );
		}
		else if ( VERSION_1 == version )
		{
			return readContentV1( index );
		}
		else
		{
			throw new IOException( "unrecognized stream version!" );
		}
	}

	private IContent readContentV0( long index ) throws IOException
	{
		if ( index >= stream.length( ) || index < 0 )
		{
			throw new IOException( "Invalid content offset:" + index );
		}
		DocumentExtension docExt = readDocumentExtensionV0( index );
		IContent content = readObject( index + INDEX_ENTRY_SIZE_V0 );
		if ( content != null )
		{
			content.setExtension( IContent.DOCUMENT_EXTENSION, docExt );
		}
		return content;
	}

	private IContent readContentV1( long index ) throws IOException
	{
		DocumentExtension docExt = readDocumentExtensionV1( index );
		IContent content = readObject( index + INDEX_ENTRY_SIZE_V1 );
		if ( content != null )
		{
			content.setExtension( IContent.DOCUMENT_EXTENSION, docExt );
		}
		return content;
	}

	private DocumentExtension readDocumentExtension( long index )
			throws IOException
	{
		if ( VERSION_0 == version )
		{
			return readDocumentExtensionV0( index );
		}
		else if ( VERSION_1 == version )
		{
			return readDocumentExtensionV1( index );
		}
		else
		{
			throw new IOException( "unrecognized stream version!" );
		}
	}

	private DocumentExtension readDocumentExtensionV0( long index )
			throws IOException
	{
		stream.seek( index );
		index = stream.readLong( );
		long parent = stream.readLong( );
		long previous = stream.readLong( );
		long next = stream.readLong( );
		long child = stream.readLong( );
		DocumentExtension docExt = new DocumentExtension( index );
		docExt.setParent( parent );
		docExt.setPrevious( previous );
		docExt.setNext( next );
		docExt.setFirstChild( child );
		return docExt;
	}

	private DocumentExtension readDocumentExtensionV1( long index )
			throws IOException
	{
		stream.seek( VERSION_SIZE + index );
		long parent = stream.readLong( );
		long next = stream.readLong( );
		long child = stream.readLong( );
		DocumentExtension docExt = new DocumentExtension( index );
		docExt.setParent( parent );
		docExt.setNext( next );
		docExt.setFirstChild( child );
		return docExt;
	}

	/**
	 * get the current offset.
	 * 
	 * The current offset is changed by set of readContent.
	 * 
	 * @return
	 */
	public long getOffset( )
	{
		return offset;
	}

	/**
	 * set the current offset. The offset must pints to a valid content.
	 * 
	 * @param offset
	 */
	public void setOffset( long offset )
	{
		this.offset = offset;
	}
}
