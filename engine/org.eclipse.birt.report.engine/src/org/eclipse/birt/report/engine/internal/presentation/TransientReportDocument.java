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

package org.eclipse.birt.report.engine.internal.presentation;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;

public class TransientReportDocument implements IReportDocument
{

	protected IReportDocument document;
	protected Map globalVariables;
	protected Map parameters;
	protected long pageNumber;

	TransientReportDocument( IReportDocument document, long pageNumber,
			Map paramters, Map globalVariables )
	{
		this.document = document;
		this.pageNumber = pageNumber;
		this.parameters = paramters;
		this.globalVariables = globalVariables;
	}

	public IDocArchiveReader getArchive( )
	{
		return document.getArchive( );
	}

	public void close( )
	{
		document.close( );
	}

	public String getVersion( )
	{
		return ReportDocumentConstants.REPORT_DOCUMENT_VERSION_1_2_1;
	}

	public String getName( )
	{
		return document.getName( );
	}

	public InputStream getDesignStream( )
	{
		return document.getDesignStream( );
	}

	public IReportRunnable getReportRunnable( )
	{
		return document.getReportRunnable( );
	}

	public Map getParameterValues( )
	{
		return parameters;
	}

	public long getPageCount( )
	{
		return pageNumber;
	}

	public long getPageNumber( InstanceID iid )
	{
		return -1;
	}

	public long getInstanceOffset( InstanceID iid )
	{
		return -1;
	}

	public long getPageNumber( String bookmark )
	{
		return -1;
	}

	public List getBookmarks( )
	{
		return new ArrayList( );
	}

	public List getChildren( String tocNodeId )
	{
		return new ArrayList( );
	}

	public TOCNode findTOC( String tocNodeId )
	{
		return null;
	}

	public List findTOCByName( String tocName )
	{
		return new ArrayList( );
	}

	public Map getGlobalVariables( String option )
	{
		return globalVariables;
	}
}
