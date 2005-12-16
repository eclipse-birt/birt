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

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.HTMLPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.ReportDocumentEmitter;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask
{

	ReportDocumentWriter writer;
	IPageHandler pageHandler;

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable instance
	 */
	public RunTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#setPageHandler(org.eclipse.birt.report.engine.api.IPageHandler)
	 */
	public void setPageHandler( IPageHandler callback )
	{
		this.pageHandler = callback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(java.lang.String)
	 */
	public void run( String reportDocName ) throws EngineException
	{
		if ( reportDocName == null || reportDocName.length( ) == 0 )
			throw new EngineException(
					"Report document name is not specified when running a report." ); //$NON-NLS-1$
		IDocArchiveWriter archive = new FileArchiveWriter( reportDocName );
		run( archive );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(org.eclipse.birt.core.archive.IDocumentArchive)
	 */
	public void run( IDocArchiveWriter archive ) throws EngineException
	{
		if ( archive == null )
			throw new EngineException(
					"Report archive is not specified when running a report." ); //$NON-NLS-1$	

		try
		{
			archive.initialize( );
		}
		catch ( IOException ex )
		{
			throw new EngineException( "Can't open the report archive.", ex ); //$NON-NLS-1$	
		}

		writer = new ReportDocumentWriter( archive );
		doRun( );
		writer.close( );
	}

	/**
	 * runs the report
	 * 
	 * @throws EngineException
	 *             throws exception when there is a run error
	 */
	protected void doRun( ) throws EngineException
	{
		// Make a deep copy of the design element and recreate the IReportRunnable
		ReportDesignHandle designHandle = (ReportDesignHandle)runnable.getDesignHandle();		 
		ReportDesign copiedReportDesign = (ReportDesign)designHandle.copy();
		ReportDesignHandle copiedDesignHandle = (ReportDesignHandle)copiedReportDesign.getHandle( null ); // null will create a new report design handle
		runnable = new ReportRunnable( copiedDesignHandle );

		if ( !validateParameters( ) )
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$

		setupExecutionContext( );

		executionContext.setReportDocWriter( writer );

		// After setting up the parameter values and before executing the
		// report, we need to call onPrepare on all items.
		// Create IReportContext and set it to execution context
		ReportContextImpl reportContext = new ReportContextImpl(
				executionContext.getParams( ), executionContext.getConfigs(),
				executionContext.getAppContext( ), executionContext.getLocale(), 
				null );
		executionContext.setReportContext( reportContext );
		// Call onPrepare in the design tree
		ScriptedDesignVisitor visitor = new ScriptedDesignVisitor( copiedDesignHandle, executionContext );
		visitor.apply( copiedDesignHandle.getRoot( ) );

		setupEmitterService( );
		IContentEmitter emitter = new HTMLPaginationEmitter( executor,
				new ReportDocumentEmitter( writer ) );

		// emitter is not null
		emitter.initialize( services );

		try
		{
			Report report = new ReportParser( )
					.parse( ( (ReportRunnable) runnable ).getReport( ) );
			writer.saveDesign( report.getReportDesign( ) );
			writer.saveParamters( inputValues );
			executionContext.openDataEngine( );
			executor.execute( report, emitter );
			executionContext.closeDataEngine( );
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happended while runntine the report", ex );
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
	}

	public void close( )
	{
		super.close( );
	}
}
