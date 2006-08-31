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
 * @version $Revision: 1.3 $ $Date: 2006/08/28 04:25:10 $
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

	/**
	 * the current offset of the stream.
	 */
	protected long offset;

	public ReportContentReaderV3( ReportContent reportContent,
			RAInputStream stream ) throws IOException
	{
		this.reportContent = reportContent;
		this.stream = stream;
		if( this.stream.length( ) >= 4 )
		{
			stream.seek( 0 );
			int iVersion = stream.readInt( );
			if( -1 == iVersion)
			{
				version = VERSION_0;
			}
			else
			{
				version = iVersion;
			}
		}
		else
		{
			throw new IOException("unrecognized stream version!");
		}
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
	protected IContent readObject(long offset) throws IOException
	{
		stream.seek( offset );
		int size = stream.readInt( );
		byte[] buffer = new byte[size];
		stream.readFully( buffer, 0, size );
		DataInputStream oi = new DataInputStream( new ByteArrayInputStream(
				buffer ) );
		IContent object = null;
		int contentType = IOUtil.readInt( oi );
		switch ( contentType )
		{
			case IContent.CELL_CONTENT :
				CellContent cellContent = new CellContent( reportContent );
				cellContent.readContent( oi );
				object = cellContent;
				break;
			case IContent.CONTAINER_CONTENT :
				ContainerContent containerContent = new ContainerContent(
						reportContent );
				containerContent.readContent( oi );
				object = containerContent;
				break;
			case IContent.DATA_CONTENT :
				DataContent dataContent = new DataContent( reportContent );
				dataContent.readContent( oi );
				object = dataContent;
				break;
			case IContent.FOREIGN_CONTENT :
				ForeignContent foreignContent = new ForeignContent(
						reportContent );
				foreignContent.readContent( oi );
				object = foreignContent;
				break;
			case IContent.IMAGE_CONTENT :
				ImageContent imageContent = new ImageContent( reportContent );
				imageContent.readContent( oi );
				object = imageContent;
				break;
			case IContent.LABEL_CONTENT :
				LabelContent labelContent = new LabelContent( reportContent );
				labelContent.readContent( oi );
				object = labelContent;
				break;
			case IContent.PAGE_CONTENT :
				PageContent pageContent = new PageContent( reportContent );
				pageContent.readContent( oi );
				object = pageContent;
				break;
			case IContent.ROW_CONTENT :
				RowContent rowContent = new RowContent( reportContent );
				rowContent.readContent( oi );
				object = rowContent;
				break;
			case IContent.TABLE_BAND_CONTENT :
				TableBandContent tableBandContent = new TableBandContent(
						reportContent );
				tableBandContent.readContent( oi );
				object = tableBandContent;
				break;
			case IContent.TABLE_CONTENT :
				TableContent tableContent = new TableContent( reportContent );
				tableContent.readContent( oi );
				object = tableContent;
				break;
			case IContent.TEXT_CONTENT :
				TextContent textContent = new TextContent( reportContent );
				textContent.readContent( oi );
				object = textContent;
				break;
			case IContent.AUTOTEXT_CONTENT :
				AutoTextContent autoText = new AutoTextContent( reportContent );
				autoText.readContent( oi );
				object = autoText;
				break;
			case IContent.LIST_CONTENT :
				ListContent list = new ListContent( reportContent );
				list.readContent( oi );
				object = list;
				break;
			case IContent.LIST_BAND_CONTENT :
				ListBandContent listBand = new ListBandContent( reportContent );
				listBand.readContent( oi );
				object = listBand;
				break;
			case IContent.LIST_GROUP_CONTENT :
				ListGroupContent listGroup = new ListGroupContent( reportContent );
				listGroup.readContent( oi );
				object = listGroup;
				break;
			case IContent.TABLE_GROUP_CONTENT :
				TableGroupContent tableGroup = new TableGroupContent( reportContent );
				tableGroup.readContent( oi );
				object = tableGroup;
				break;
			default:
				// Not expected
				throw new IOException("Found invalid contentType" +
						contentType + " at object offset " + offset);
		}
		if( object instanceof AbstractContent )
		{
			( ( AbstractContent ) object ).setVersion( version );
		}
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

		if( VERSION_0 == version )
		{
			return readContentV0( index );
		}
		else if( VERSION_1 == version )
		{
			return readContentV1( index );
		}
		else
		{
			throw new IOException("unrecognized stream version!");
		}
	}
	
	private IContent readContentV0( long index ) throws IOException
	{
		if ( index >= stream.length( )  || index < 0)
		{
			throw new IOException("Invalid content offset:" + index);
		}
		DocumentExtension docExt  = readDocumentExtensionV0( index );
		IContent content = readObject( index + INDEX_ENTRY_SIZE_V0 );
		if (content != null)
		{
			content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);
		}
		return content;
	}
	
	private IContent readContentV1( long index ) throws IOException
	{
		if ( index >= stream.length( )  || index < 0)
		{
			throw new IOException("Invalid content offset:" + index);
		}
		DocumentExtension docExt  = readDocumentExtensionV1( index );
		IContent content = readObject( index + INDEX_ENTRY_SIZE_V1 );
		if (content != null)
		{
			content.setExtension(IContent.DOCUMENT_EXTENSION, docExt);
		}
		return content;
	}
	
	private DocumentExtension readDocumentExtension( long index )
			throws IOException
	{
		if( VERSION_0 == version )
		{
			return readDocumentExtensionV0( index );
		}
		else if( VERSION_1 == version )
		{
			return readDocumentExtensionV1( index );
		}
		else
		{
			throw new IOException("unrecognized stream version!");
		}
	}
	
	private DocumentExtension readDocumentExtensionV0( long index )
			throws IOException
	{
		if ( index >= stream.length( ) || index < 0 )
		{
			return null;
		}
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
		if ( index >= stream.length( ) || index < 0 )
		{
			return null;
		}
		if( 0 == index )
		{
			index += 4;
		}
		stream.seek( index );
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
