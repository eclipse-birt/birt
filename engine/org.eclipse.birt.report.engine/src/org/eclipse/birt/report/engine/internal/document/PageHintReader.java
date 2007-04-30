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

import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.internal.document.v1.PageHintReaderV1;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintReaderV2;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * page hint reader
 * 
 * It can support mutiple versions.
 * 
 */
public class PageHintReader implements IPageHintReader
{

	IPageHintReader reader;

	public PageHintReader( IReportDocument document ) throws IOException
	{
		if ( ReportDocumentConstants.REPORT_DOCUMENT_VERSION_1_0_0
				.equals( document.getVersion( ) ) )
		{
			this.reader = new PageHintReaderV1( document );
		}
		else
		{
			this.reader = new PageHintReaderV2( document.getArchive( ) );
		}
	}

	public int getVersion( )
	{
		return reader.getVersion( );
	}

	public void close( )
	{
		reader.close( );
	}

	public long getTotalPage( ) throws IOException
	{
		return reader.getTotalPage( );
	}

	public IPageHint getPageHint( long pageNumber ) throws IOException
	{
		return reader.getPageHint( pageNumber );
	}
}
