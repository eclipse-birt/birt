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

package org.eclipse.birt.report.engine.internal.document.v2;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public class PageHintWriterV2 implements IPageHintWriter
{

	protected ReportDocumentWriter document;
	protected RAOutputStream indexStream;
	protected RAOutputStream hintsStream;

	public PageHintWriterV2( ReportDocumentWriter document )
	{
		this.document = document;
	}

	public void open( ) throws IOException
	{
		IDocArchiveWriter writer = document.getArchive( );
		hintsStream = writer
				.createRandomAccessStream( ReportDocumentConstants.PAGEHINT_STREAM );
		indexStream = writer
				.createRandomAccessStream( ReportDocumentConstants.PAGEHINT_INDEX_STREAM );;
	}

	public void close( )
	{
		try
		{
			if ( hintsStream != null )
			{
				hintsStream.close( );
				hintsStream = null;
			}
		}
		catch ( IOException ex )
		{
		}

		try
		{
			if ( indexStream != null )
			{
				indexStream.close( );
				indexStream = null;
			}
		}
		catch ( IOException ex )
		{
		}
	}

	private ByteArrayOutputStream writeBuffer = new ByteArrayOutputStream( );
	private DataOutputStream hintBuffer = new DataOutputStream( writeBuffer );

	public void writePageHint( IPageHint pageHint ) throws IOException
	{
		long offset = hintsStream.getOffset( );
		indexStream.seek( pageHint.getPageNumber( ) * 8 );
		indexStream.writeLong( offset );
		writeBuffer.reset( );
		pageHint.writeObject( hintBuffer );
		hintsStream.write( writeBuffer.toByteArray( ) );
	}

	public void writeTotalPage( long totalPage ) throws IOException
	{
		indexStream.seek( 0 );
		indexStream.writeLong( totalPage );
	}

}
