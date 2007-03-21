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

package org.eclipse.birt.report.designer.ui.preview.editors;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.designer.ui.preview.extension.IViewer;
import org.eclipse.birt.report.designer.ui.preview.parameter.ParameterFactory;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;

/**
 * 
 */

public abstract class AbstractViewer implements IViewer
{

	private IReportEngine engine;

	public void init( )
	{
		EngineConfig engineConfig = getEngineConfig( );
		if ( engineConfig == null )
		{
			engineConfig = new EngineConfig( );
		}
		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
		this.engine = factory.createReportEngine( engineConfig );
		engine.changeLogLevel( Level.WARNING );
	}

	public void close( )
	{
		this.engine.destroy( );
	}

	protected abstract IRenderOption getRenderOption( );

	protected abstract EngineConfig getEngineConfig( );

	//	protected String createReportDocument( String reportDesignFile,
	//			Map parameters ) throws IOException, EngineException
	//	{
	//		File designFile = new File( reportDesignFile );
	//
	//		File reportDocumentFile = File.createTempFile( designFile.getName( ),
	//				"rptdocument",
	//				designFile.getParentFile( ) );
	//
	//		IDocArchiveWriter archive = new FileArchiveWriter( reportDocumentFile.getAbsolutePath( ) );
	//		IReportRunnable report = engine.openReportDesign( reportDesignFile );
	//		IRunTask runTask = engine.createRunTask( report );
	//		try
	//		{
	//			if ( parameters != null )
	//			{
	//				runTask.setParameterValues( parameters );
	//			}
	//			runTask.setAppContext( Collections.EMPTY_MAP );
	//			runTask.run( archive );
	//		}
	//		catch ( EngineException e )
	//		{
	//			throw e;
	//		}
	//		finally
	//		{
	//			runTask.close( );
	//			report = null;
	//			runTask = null;
	//		}
	//		return reportDocumentFile.getAbsolutePath( );
	//	}


	protected long createReportOutput( String reportDesignFile,
			String outputFolder, String outputFileName, Map parameters,
			long pageNumber ) throws EngineException, IOException
	{
		File designFile = new File( reportDesignFile );

		//create report document
		File reportDocumentFile = new File( outputFolder, designFile.getName( )
				+ ".rptdocument" );

		IDocArchiveWriter archive = new FileArchiveWriter( reportDocumentFile.getAbsolutePath( ) );
		IReportRunnable report = engine.openReportDesign( reportDesignFile );
		IRunTask runTask = engine.createRunTask( report );
		try
		{
			if ( parameters != null )
			{
				runTask.setParameterValues( parameters );
			}
			runTask.setAppContext( Collections.EMPTY_MAP );
			runTask.run( archive );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		finally
		{
			runTask.close( );
			report = null;
			runTask = null;
		}

		IReportDocument document = engine.openReportDocument( reportDocumentFile.getAbsolutePath( ) );
		long pageCount = document.getPageCount( );
		IRenderTask task = engine.createRenderTask( document );

		IRenderOption renderOption = getRenderOption( );
		renderOption.setOutputFileName( outputFolder
				+ File.separator
				+ outputFileName );

		try
		{
			task.setRenderOption( renderOption );
			task.setPageNumber( pageNumber );
			task.render( );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		finally
		{
			task.close( );
			task = null;
			document = null;
		}
		return pageCount;
	}
	
	protected List getInputParameters( String reportDesignFile )
	{
		try
		{
			IEngineTask task = engine.createRunTask( engine.openReportDesign( reportDesignFile ) );
			ParameterFactory factory = new ParameterFactory( task );
			List parameters = factory.getRootChildren( );
			task.close( );
			task = null;
			return parameters;
		}
		catch ( EngineException e )
		{
		}
		return null;
	}

}
