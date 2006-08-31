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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IReportContentWriter;

public class ReportContentWriterV3 implements IReportContentWriter
{

	protected static Logger logger = Logger
			.getLogger( IReportContentWriter.class.getName( ) );

	/**
	 * report document used to save the contents
	 */
	protected ReportDocumentWriter document;

	/**
	 * stream in the document, used to save the contents
	 */
	protected RAOutputStream cntStream;
	
	/**
	 * the offset of current node
	 */
	protected long cntOffset;
	
	/**
	 * the previous root offset.
	 */
	protected long rootOffset;

	public ReportContentWriterV3( ReportDocumentWriter document )
	{
		this.document = document;
	}

	/**
	 * open the content writer.
	 */
	public void open( String name ) throws IOException
	{
		IDocArchiveWriter archive = document.getArchive( );
		cntStream = archive.createRandomAccessStream( name );
		//write the version information
		cntStream.writeInt( VERSION_1 );
		cntOffset = 4;
		rootOffset = -1;
	}

	/**
	 * close the content writer
	 */
	public void close( )
	{
		if ( cntStream != null )
		{
			try
			{
				cntStream.close( );
			}
			catch ( Exception ex )
			{
				logger.log( Level.SEVERE, "Failed in close the writer", ex );
			}
			cntStream = null;
		}
	}

	/**
	 * get the current offset.
	 * 
	 * @return
	 */
	public long getOffset( )
	{
		return cntOffset;
	}

	/**
	 * buffer used to save the report content.
	 */
	private ByteArrayOutputStream buffer = new ByteArrayOutputStream( );
	/**
	 * data output stream used to save the report content.
	 */
	private DataOutputStream bufferStream = new DataOutputStream( buffer );

	/**
	 * stack used to save the parent's offset
	 */
	protected Stack contents = new Stack( );

	/**
	 * write the content into the stream.
	 * 
	 * @param content
	 *            the content object.
	 * @return the content object's offset.
	 * @throws IOException
	 */
	public long writeContent( IContent content ) throws IOException
	{
		//write the index into the stream
		updateIndex(content);
		
		// get the byte[] of the content
		buffer.reset( );
		IOUtil.writeInt( bufferStream, content.getContentType( ) );
		content.writeContent( bufferStream );
		bufferStream.flush( );
		byte[] values = buffer.toByteArray( );
		// write the content out as: length, data
		cntStream.seek( cntOffset );
		cntStream.writeInt( values.length );
		cntStream.write( values );
		cntOffset = cntOffset + 4 + values.length;
		
		DocumentExtension docExt = (DocumentExtension) content
				.getExtension( IContent.DOCUMENT_EXTENSION );
		if ( docExt != null )
		{
			return docExt.getIndex( );
		}
		return -1;
	}

	/**
	 * save the content and its children into the streams.
	 * 
	 * @param content
	 *            the content object
	 * @return the offset of this content object.
	 * @throws IOException
	 */
	public long writeFullContent( IContent content ) throws IOException
	{
		long offset = writeContent( content );
		Iterator iter = content.getChildren( ).iterator( );
		while ( iter.hasNext( ) )
		{
			IContent child = (IContent) iter.next( );
			writeFullContent( child );
		}
		return offset;
	}

	/**
	 * parent index
	 */
	final static long OFFSET_PARENT = 0;
	/**
	 * next index
	 */
	final static long OFFSET_NEXT = 8;
	/**
	 * first child index
	 */
	final static long OFFSET_CHILD = 16;
	
	final static int INDEX_ENTRY_SIZE = 24;
	
	protected final static int VERSION_1 = 1;
	
	/**
	 * There is a content start from the offset, which parent start from
	 * the parentOffset.
	 * 
	 * update the index for that object. 
	 * 
	 * @param parentOffset
	 * @param offset
	 * @throws IOException
	 */
	protected void updateIndex(IContent content) throws IOException
	{
		long index = cntOffset;
		long parent = -1;
		long previous = -1;
		
		IContent pContent = (IContent) content.getParent( );
		if ( pContent != null )
		{
			DocumentExtension pDocExt = (DocumentExtension) pContent
					.getExtension( IContent.DOCUMENT_EXTENSION );
			if ( pDocExt != null )
			{
				parent = pDocExt.getIndex( );
				long lastChild = pDocExt.getLastChild( );
				if ( lastChild != -1 )
				{
					previous = lastChild;
				}
				pDocExt.setLastChild( index );
			}
			else
			{
				previous = rootOffset;
				rootOffset = index;
			}
		}
		else
		{
			previous = rootOffset;
			rootOffset = index;
		}
		DocumentExtension docExt = new DocumentExtension(index);
		docExt.setParent(parent);
		docExt.setPrevious(previous);
		content.setExtension( IContent.DOCUMENT_EXTENSION, docExt);

		cntStream.seek( index );
		cntStream.writeLong(parent);	//parent
		cntStream.writeLong(-1);		//next
		cntStream.writeLong(-1);		//first child
		cntOffset += INDEX_ENTRY_SIZE;
		
		// update the links refer to this content
		if ( previous == -1 )
		{
			// it has no previous sibling, so it is the first
			// element of its parent
			if ( parent != -1 )
			{
				cntStream.seek( parent + OFFSET_CHILD );
				cntStream.writeLong( index );
			}
		}
		else
		{
			// update the previou's link
			cntStream.seek( previous + OFFSET_NEXT );
			cntStream.writeLong( index );
		}
	}
}
