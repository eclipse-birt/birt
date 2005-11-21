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

import java.util.logging.Level;

import org.eclipse.birt.core.archive.DocumentArchive;
import org.eclipse.birt.core.archive.IDocumentArchive;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.HtmlPaginateEmitter;
import org.eclipse.birt.report.engine.presentation.ReportDocumentEmitter;

public class RunTask extends AbstractRunTask implements IRunTask
{

	ReportDocument reportDoc;
	IPageHandler pageHandler;

	RunTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );

	}

	public void setPageHandler( IPageHandler callback )
	{
		this.pageHandler = callback;
	}

	public void run( String reportDocName ) throws EngineException
	{
		IDocumentArchive archive = new DocumentArchive( reportDocName );
		reportDoc = (ReportDocument) engine.openReportDocument( archive );
		run( );
	}

	public void run( IDocumentArchive archive ) throws EngineException
	{

		reportDoc = (ReportDocument) engine.openReportDocument( archive );
		run( );
	}

	protected void run( ) throws EngineException
	{
		if ( !validateParameters( ) )
		{
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		setupExecutionContext( );

		executionContext.setReportDocument( reportDoc );

		setupEmitterService();
		// we need output: TOC, BookMark, PageHint
		IContentEmitter emitter = new ReportDocumentEmitter( reportDoc );

		emitter = new HtmlPaginateEmitter( executor, emitter );

		// emitter is not null
		emitter.initialize( services );

		try
		{
			Report report = new ReportParser( )
					.parse( ( (ReportRunnable) runnable ).getReport( ) );
			reportDoc.saveDesign( report.getReportDesign( ) );
			reportDoc.saveParamters( inputValues );
			executor.execute( report, emitter );
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
	}
}
