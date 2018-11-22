/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.impl.IInternalReportDocument;
import org.eclipse.birt.report.engine.api.impl.RunStatusReader;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.engine.IReportDocumentExtension;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.toc.TOCReader;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TransientReportDocument implements IInternalReportDocument
{

	protected IReportDocument document;
	protected Map globalVariables;
	protected Map parameters;
	protected Map parameterDisplayTexts;
	protected long pageNumber;
	protected boolean isComplete;
	protected ExecutionContext context;

	TransientReportDocument( IReportDocument document,
			ExecutionContext context, long pageNumber, Map paramters,
			Map parameterDisplayTexts, Map globalVariables, boolean isComplete )
	{
		this.document = document;
		this.context = context;
		this.pageNumber = pageNumber;
		this.parameters = paramters;
		this.parameterDisplayTexts = parameterDisplayTexts;
		this.globalVariables = globalVariables;
		this.isComplete = isComplete;
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
		return document.getVersion( );
	}

	public String getProperty( String key )
	{
		return document.getProperty( key );
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
	
	public IReportRunnable getPreparedRunnable( )
	{
		return document.getPreparedRunnable( );
	}

	public Map getParameterValues( )
	{
		return parameters;
	}

	public Map getParameterDisplayTexts( )
	{
		return parameterDisplayTexts;
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

	public long getBookmarkOffset( String bookmark )
	{
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#isComplete()
	 */
	public boolean isComplete( )
	{
		return isComplete;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IReportDocument#refresh()
	 */
	public void refresh( )
	{
		return;
	}

	public ITOCTree getTOCTree( String format, ULocale locale )
	{
		return null;
	}

	public ITOCTree getTOCTree( String format, ULocale locale, TimeZone timeZone )
	{
		return null;
	}

	public ClassLoader getClassLoader( )
	{
		return context.getApplicationClassLoader( );
	}

	public ReportDesignHandle getReportDesign( )
	{
		return document.getReportDesign( );
	}

	public Report getReportIR( ReportDesignHandle designHandle )
	{
		return ( (IInternalReportDocument) document )
				.getReportIR( designHandle );
	}

	public IReportRunnable getOnPreparedRunnable( )
	{
		return ( (IInternalReportDocument) document ).getOnPreparedRunnable( );
	}

	public IReportRunnable getDocumentRunnable( )
	{
		return ( (IInternalReportDocument) document ).getDocumentRunnable( );
	}

	public InstanceID getBookmarkInstance( String bookmark )
	{
		return null;
	}

	public IReportDocumentExtension getDocumentExtension( String name )
			throws EngineException
	{
		return ( (IInternalReportDocument) document )
				.getDocumentExtension( name );
	}

	public Map<String, ParameterAttribute> loadParameters( ClassLoader loader )
			throws EngineException
	{
		return new HashMap<String, ParameterAttribute>( );
	}

	public Map<String, Object> loadVariables( ClassLoader loader )
			throws EngineException
	{
		return new HashMap<String, Object>( );
	}

	public TOCReader getTOCReader( ClassLoader loader ) throws EngineException
	{
		return null;
	}

	public String getSystemId( )
	{
		return document.getSystemId();
	}
	
	public List<String> getDocumentErrors( )
	{
		RunStatusReader statusReader = new RunStatusReader( this );
		try
		{
			return statusReader.getGenerationErrors( );
		}
		finally
		{
			statusReader.close( );
		}
	}
	
}
