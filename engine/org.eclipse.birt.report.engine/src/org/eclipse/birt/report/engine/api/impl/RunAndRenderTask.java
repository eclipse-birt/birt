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

import java.io.OutputStream;
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.FORenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.DefaultPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.HTMLPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.LocalizedEmitter;
import org.eclipse.birt.report.engine.script.ReportContextImpl;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask
{

	/**
	 * options for rendering the report
	 */
	protected IRenderOption renderOption;

	/**
	 * options for runninging the report
	 */
	protected HashMap runOptions = new HashMap( );

	/**
	 * the output stream for writing the output to
	 */
	protected OutputStream ostream;

	/**
	 * full path for the output file name
	 */
	protected String outputFileName;
	
	/**
	 * specifies the emitter ID used for rendering the report
	 */
	protected String emitterID;

	/**
	 * @param engine
	 *            reference to the report engine
	 * @param runnable
	 *            the runnable report design reference
	 */
	public RunAndRenderTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run( ) throws EngineException
	{
		// Make a deep copy of the design element and recreate the
		// IReportRunnable
		// This should be moved to a common place of RunTask, RunderTask and
		// InteractiveTask.
		ReportDesignHandle designHandle = (ReportDesignHandle) runnable
				.getDesignHandle( );
		ReportDesignHandle copiedDesignHandle = designHandle;
		/*
		 * TODO: uncomment the following part when the deep copy in DE is ready.
		 * ReportDesign copiedReportDesign = (ReportDesign)designHandle.copy();
		 * ReportDesignHandle copiedDesignHandle =
		 * (ReportDesignHandle)copiedReportDesign.getHandle( null ); // null
		 * will create a new report design handle
		 */

		if ( !validateParameters( ) )
		{
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		// create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices( this );
		services.setRenderOption( renderOption );

		EngineConfig config = engine.getConfig( );
		if ( config != null )
			services
					.setEmitterConfig( engine.getConfig( ).getEmitterConfigs( ) );
		services.setRenderContext( context );
		services.setReportRunnable( runnable );

		// register default parameters
		usingParameterValues( );

		// After setting up the parameter values and before executing the
		// report, we need to call onPrepare on all items.
		// Create IReportContext and set it to execution context
		ReportContextImpl reportContext = new ReportContextImpl(
				executionContext.getParams( ), config.getConfigMap( ),
				executionContext.getAppContext( ) );
		executionContext.setReportContext( reportContext );
		// Call onPrepare in the design tree
		ScriptedDesignVisitor visitor = new ScriptedDesignVisitor(
				copiedDesignHandle, executionContext );
		visitor.apply( copiedDesignHandle.getRoot( ) );

		// setup runtime configurations
		// user defined configs are overload using system properties.
		executionContext.getConfigs( ).putAll( runnable.getTestConfig( ) );
		executionContext.getConfigs( ).putAll( System.getProperties( ) );

		// Set up rendering environment and check for supported format
		executionContext.setRenderOption( renderOption );
		String format = renderOption.getOutputFormat( );
		if ( format == null || format.length( ) == 0 ) // $NON-NLS-1
		{
			renderOption.setOutputFormat( "html" ); // $NON-NLS-1
			format = "html"; // $NON-NLS-1
		}
		else if ( renderOption != null && format.equalsIgnoreCase( "fo" ) // $NON-NLS-1
				&& ( (FORenderOption) renderOption ).getTailoredForFOP( ) )
		{
			format = "fop"; // $NON-NLS-1
		}

		if ( !ExtensionManager.getInstance( ).getSupportedFormat()
				.contains( format ) )
		{
			log.log( Level.SEVERE,
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
			throw new EngineException(
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
		}

		IContentEmitter emitter = ExtensionManager.getInstance( )
				.createEmitter( format, emitterID );
		if ( emitter == null )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.",
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION );
		}

		ReportExecutor executor = new ReportExecutor( executionContext );
		services.setExecutor( executor );

		// localized emitter
		emitter = new LocalizedEmitter( executionContext, emitter );

		// if we need do the paginate, do the paginate.
		if ( format.equalsIgnoreCase( "html" ) )
		{
			emitter = new HTMLPaginationEmitter( executor, emitter );
		}
		else if ( format.equalsIgnoreCase( "fo" )
				|| format.equalsIgnoreCase( "fop" )
				|| ((format.equalsIgnoreCase( "pdf" )) && ! "org.eclipse.birt.report.engine.pdf".equals(emitterID)))
		{
			emitter = new DefaultPaginationEmitter( executor, emitter );
		}
		

		// emitter is not null
		emitter.initialize( services );

		try
		{
			Report report = new ReportParser( )
					.parse( ( (ReportRunnable) runnable ).getReport( ) );
			executor.execute( report, emitter );
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex );
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
		catch ( Throwable t )
		{
			log.log( Level.SEVERE,
					"Error happened while running the report.", t ); //$NON-NLS-1$
			new EngineException( "Error happened while running the report", t );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#setRenderOption(org.eclipse.birt.report.engine.api2.IRenderOption)
	 */
	public void setRenderOption( IRenderOption options )
	{
		renderOption = options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#getRenderOption()
	 */
	public IRenderOption getRenderOption( )
	{
		return renderOption;
	}

	public void setEmitterID( String id )
	{
		this.emitterID = id;
		
	}
}