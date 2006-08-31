/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformServletContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IRequestInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.IResultSetItem;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.soapengine.api.Column;
import org.eclipse.birt.report.soapengine.api.ResultSet;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Provides all the services from Engine.
 */

public class ReportEngineService
{

	private static ReportEngineService instance;

	/**
	 * Report engine instance.
	 */
	private IReportEngine engine = null;

	/**
	 * Static engine config instance.
	 */
	private EngineConfig config = null;

	/**
	 * Image directory for report images and charts.
	 */
	private String imageDirectory = null;

	/**
	 * URL accesses images.
	 */
	private String imageBaseUrl = null;

	/**
	 * Image handler instance.
	 */
	private HTMLServerImageHandler imageHandler = null;

	/**
	 * Web app context path.
	 */
	private String contextPath = null;

	/**
	 * Constructor.
	 * 
	 * @param servletConfig
	 * @param config
	 */
	private ReportEngineService( ServletConfig servletConfig )
	{
		System.setProperty( "RUN_UNDER_ECLIPSE", "false" ); //$NON-NLS-1$ //$NON-NLS-2$

		if ( servletConfig == null )
		{
			return;
		}

		config = new EngineConfig( );

		// Register new image handler
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( );
		emitterConfig.setActionHandler( new HTMLActionHandler( ) );
		imageHandler = new HTMLServerImageHandler( );
		emitterConfig.setImageHandler( imageHandler );
		config.getEmitterConfigs( ).put( "html", emitterConfig ); //$NON-NLS-1$

		// Prepare image directory.
		imageDirectory = servletConfig.getServletContext( ).getInitParameter(
				ParameterAccessor.INIT_PARAM_IMAGE_DIR );

		if ( imageDirectory == null || imageDirectory.trim( ).length( ) <= 0
				|| ParameterAccessor.isRelativePath( imageDirectory ) )
		{
			imageDirectory = ParameterAccessor.getRealPath( servletConfig
					.getServletContext( ), "/report/images" ); //$NON-NLS-1$
		}

		// Prepare image base url.
		imageBaseUrl = "/run?__imageID="; //$NON-NLS-1$

		// Prepare log directory.
		String logDirectory = servletConfig.getServletContext( )
				.getInitParameter( ParameterAccessor.INIT_PARAM_LOG_DIR );

		if ( logDirectory == null || logDirectory.trim( ).length( ) <= 0
				|| ParameterAccessor.isRelativePath( logDirectory ) )
		{
			logDirectory = ParameterAccessor.getRealPath( servletConfig
					.getServletContext( ), "/logs" ); //$NON-NLS-1$
		}

		// Prepare log level.
		String logLevel = servletConfig.getServletContext( ).getInitParameter(
				ParameterAccessor.INIT_PARAM_LOG_LEVEL );
		Level level = Level.OFF;
		if ( "SEVERE".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.SEVERE;
		}
		else if ( "WARNING".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.WARNING;
		}
		else if ( "INFO".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.INFO;
		}
		else if ( "CONFIG".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.CONFIG;
		}
		else if ( "FINE".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.FINE;
		}
		else if ( "FINER".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.FINER;
		}
		else if ( "FINEST".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.FINEST;
		}
		else if ( "OFF".equalsIgnoreCase( logLevel ) ) //$NON-NLS-1$
		{
			level = Level.OFF;
		}
		config.setLogConfig( logDirectory, level );

		// Prepare ScriptLib location
		String scriptLibDir = servletConfig.getServletContext( )
				.getInitParameter( ParameterAccessor.INIT_PARAM_SCRIPTLIB_DIR );
		if ( scriptLibDir == null || scriptLibDir.trim( ).length( ) <= 0
				|| ParameterAccessor.isRelativePath( scriptLibDir ) )
		{
			scriptLibDir = ParameterAccessor.getRealPath( servletConfig
					.getServletContext( ), "/scriptlib" ); //$NON-NLS-1$
		}

		ArrayList jarFileList = new ArrayList( );
		if ( scriptLibDir != null )
		{
			File dir = new File( scriptLibDir );
			getAllJarFiles( dir, jarFileList );
		}

		String scriptlibClassPath = ""; //$NON-NLS-1$
		for ( int i = 0; i < jarFileList.size( ); i++ )
			scriptlibClassPath += EngineConstants.PROPERTYSEPARATOR
					+ ( (File) jarFileList.get( i ) ).getAbsolutePath( );

		if ( scriptlibClassPath.startsWith( EngineConstants.PROPERTYSEPARATOR ) )
			scriptlibClassPath = scriptlibClassPath
					.substring( EngineConstants.PROPERTYSEPARATOR.length( ) );

		System.setProperty( EngineConstants.WEBAPP_CLASSPATH_KEY,
				scriptlibClassPath );

		config.setEngineHome( "" ); //$NON-NLS-1$
	}

	/**
	 * Get engine instance.
	 * 
	 * @return the single report engine service
	 */
	public static ReportEngineService getInstance( )
	{
		return instance;
	}

	/**
	 * Get engine instance.
	 * 
	 * @param servletConfig
	 * @throws BirtException
	 * 
	 */
	public synchronized static void initEngineInstance(
			ServletConfig servletConfig ) throws BirtException
	{
		if ( ReportEngineService.instance != null )
		{
			return;
		}
		ReportEngineService.instance = new ReportEngineService( servletConfig );
	}

	/**
	 * Get all the files under the specified folder (including all the files
	 * under sub-folders)
	 * 
	 * @param dir -
	 *            the folder to look into
	 * @param fileList -
	 *            the fileList to be returned
	 */
	private void getAllJarFiles( File dir, ArrayList fileList )
	{
		if ( dir.exists( ) && dir.isDirectory( ) )
		{
			File[] files = dir.listFiles( );
			if ( files == null )
				return;

			for ( int i = 0; i < files.length; i++ )
			{
				File file = files[i];
				if ( file.isFile( ) )
				{
					if ( file.getName( ).endsWith( ".jar" ) ) //$NON-NLS-1$
						fileList.add( file );
				}
				else if ( file.isDirectory( ) )
				{
					getAllJarFiles( file, fileList );
				}
			}
		}
	}

	/**
	 * Set Engine context.
	 * 
	 * @param servletContext
	 * @param request
	 */
	synchronized public void setEngineContext( ServletContext servletContext,
			HttpServletRequest request )
	{
		if ( engine == null )
		{
			IPlatformContext platformContext = new PlatformServletContext(
					servletContext );
			config.setPlatformContext( platformContext );

			try
			{
				Platform.startup( config );
			}
			catch ( BirtException e )
			{
				// TODO remove this output.

				e.printStackTrace( );
			}

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine( config );

			contextPath = request.getContextPath( );
		}
	}

	/**
	 * Open report design.
	 * 
	 * @param report
	 * @param options
	 *            the config options in the report design
	 * @return the report runnable
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign( String report, Map options )
			throws EngineException
	{
		File file = new File( report );
		if ( !file.exists( ) )
		{
			throw new EngineException(
					MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, report );
		}

		try
		{
			InputStream in = new FileInputStream( file );
			String systemId = report;
			try
			{
				systemId = file.toURL( ).toString( );
			}
			catch ( MalformedURLException ue )
			{
				systemId = report;
			}
			return engine.openReportDesign( systemId, in, options );
		}
		catch ( FileNotFoundException ioe )
		{
			throw new EngineException(
					MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION, report );
		}
	}

	/**
	 * Open report design by using the input stream
	 * 
	 * @param systemId
	 *            the system Id of the report design
	 * @param reportStream -
	 *            the input stream
	 * @param options
	 *            the config options in the report design
	 * @return IReportRunnable
	 * @throws EngineException
	 */
	public IReportRunnable openReportDesign( String systemId,
			InputStream reportStream, Map options ) throws EngineException
	{
		return engine.openReportDesign( systemId, reportStream, options );
	}

	/**
	 * createGetParameterDefinitionTask.
	 * 
	 * @param runnable
	 * @return the get parameter definition task
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(
			IReportRunnable runnable )
	{
		IGetParameterDefinitionTask task = null;

		try
		{
			task = engine.createGetParameterDefinitionTask( runnable );
		}
		catch ( Exception e )
		{
		}

		return task;
	}

	/**
	 * Open report document from archive,
	 * 
	 * @param docName
	 *            the name of the report document
	 * @param systemId
	 *            the system ID to search the resource in the document,
	 *            generally it is the file name of the report design
	 * @param options
	 *            the config options used in document
	 * @return the report docuement
	 */

	public IReportDocument openReportDocument( String systemId, String docName,
			Map options )
	{

		IReportDocument document = null;

		try
		{
			document = engine.openReportDocument( systemId, docName, options );
		}
		catch ( Exception e )
		{
		}

		return document;
	}

	/**
	 * Render image.
	 * 
	 * @param imageId
	 * @param outputStream
	 * @throws RemoteException
	 */
	public void renderImage( String imageId, OutputStream outputStream )
			throws RemoteException
	{
		assert ( this.imageHandler != null );

		try
		{
			this.imageHandler.getImage( outputStream, this.imageDirectory,
					imageId );
		}
		catch ( EngineException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.renderImage( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}

	}

	/**
	 * Create HTML render context.
	 * 
	 * @param svgFlag
	 * @param servletPath
	 * @return HTML render context from the given arguments
	 */
	private HTMLRenderContext createHTMLrenderContext( boolean svgFlag,
			String servletPath )
	{
		HTMLRenderContext renderContext = new HTMLRenderContext( );
		renderContext.setImageDirectory( imageDirectory );
		renderContext.setBaseImageURL( contextPath + imageBaseUrl );
		if ( servletPath != null && servletPath.length( ) > 0 )
		{
			renderContext.setBaseURL( this.contextPath + servletPath );
		}
		else
		{
			renderContext.setBaseURL( this.contextPath
					+ IBirtConstants.SERVLET_PATH_RUN );
		}

		renderContext.setImageDirectory( imageDirectory );
		renderContext.setSupportedImageFormats( svgFlag
				? "PNG;GIF;JPG;BMP;SVG" : "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$ //$NON-NLS-2$
		return renderContext;
	}

	/**
	 * Create PDF render context.
	 * 
	 * @param servletPath
	 * 
	 * @return the PDF render context
	 */
	private PDFRenderContext createPDFrenderContext( String servletPath )
	{
		PDFRenderContext renderContext = new PDFRenderContext( );
		if ( servletPath != null && servletPath.length( ) > 0 )
		{
			renderContext.setBaseURL( this.contextPath + servletPath );
		}
		else
		{
			renderContext.setBaseURL( this.contextPath
					+ IBirtConstants.SERVLET_PATH_RUN );
		}
		renderContext.setSupportedImageFormats( "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$
		return renderContext;
	}

	/**
	 * Run and render a report,
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, OutputStream outputStream, String format,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag ) throws RemoteException
	{
		runAndRenderReport( request, runnable, outputStream, format, locale,
				rtl, parameters, masterPage, svgFlag, null, null, null, null );
	}

	/**
	 * Run and render a report,
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param outputStream
	 * @param format
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param displayTexts
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, OutputStream outputStream, String format,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, Map displayTexts ) throws RemoteException
	{
		runAndRenderReport( request, runnable, outputStream, format, locale,
				rtl, parameters, masterPage, svgFlag, null, null, null,
				displayTexts );
	}

	/**
	 * Run and render a report,
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param outputStream
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param htmlRenderContext
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, ByteArrayOutputStream outputStream,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, List activeIds, HTMLRenderContext htmlRenderContext )
			throws RemoteException
	{
		runAndRenderReport( request, runnable, outputStream,
				ParameterAccessor.PARAM_FORMAT_HTML, locale, rtl, parameters,
				masterPage, svgFlag, Boolean.TRUE, activeIds,
				htmlRenderContext, null );
	}

	/**
	 * Run and render a report,
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param outputStream
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param htmlRenderContext
	 * @param displayTexts
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, ByteArrayOutputStream outputStream,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, List activeIds,
			HTMLRenderContext htmlRenderContext, Map displayTexts )
			throws RemoteException
	{
		runAndRenderReport( request, runnable, outputStream,
				ParameterAccessor.PARAM_FORMAT_HTML, locale, rtl, parameters,
				masterPage, svgFlag, Boolean.TRUE, activeIds,
				htmlRenderContext, displayTexts );
	}

	/**
	 * Run and render a report,
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param outputStream
	 * @param locale
	 * @param rtl
	 * @param parameters
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param htmlRenderContext
	 * @param displayTexts
	 * @throws RemoteException
	 * @throws IOException
	 */
	private void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, OutputStream outputStream, String format,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, Boolean embeddable, List activeIds,
			HTMLRenderContext htmlRenderContext, Map displayTexts )
			throws RemoteException
	{
		assert runnable != null;

		// Render options
		HTMLRenderOption option = new HTMLRenderOption( );
		option.setOutputStream( outputStream );
		option.setOutputFormat( format );
		option.setMasterPageContent( masterPage );
		option.setHtmlRtLFlag( rtl );
		option.setActionHandle( new ViewerHTMLActionHandler( locale, rtl,
				masterPage ) );
		// set a default title for the html page
		option.setHtmlTitle( BirtResources
				.getMessage( ResourceConstants.BIRT_VIEWER_TITLE ) );

		if ( embeddable != null )
		{
			option.setEmbeddable( embeddable.booleanValue( ) );
		}

		if ( activeIds != null )
		{
			option.setInstanceIDs( activeIds );
		}

		IRunAndRenderTask runAndRenderTask = null;

		runAndRenderTask = engine.createRunAndRenderTask( runnable );

		runAndRenderTask.setLocale( locale );
		if ( parameters != null )
		{
			runAndRenderTask.setParameterValues( parameters );
		}

		// Set display Text for select parameters
		if ( displayTexts != null )
		{
			Iterator keys = displayTexts.keySet( ).iterator( );
			while ( keys.hasNext( ) )
			{
				String paramName = DataUtil.getString( keys.next( ) );
				String displayText = DataUtil.getString( displayTexts
						.get( paramName ) );
				runAndRenderTask.setParameterDisplayText( paramName,
						displayText );
			}
		}

		runAndRenderTask.setRenderOption( option );

		HashMap context = new HashMap( );

		// context.put(DataEngine.DATASET_CACHE_OPTION, Boolean.TRUE )running in
		// designer enviroment; if running in deployment, set it to false
		Boolean isDesigner = Boolean.valueOf( ParameterAccessor
				.isDesigner( request ) );
		context.put( "org.eclipse.birt.data.engine.dataset.cache.option", //$NON-NLS-1$
				isDesigner );
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );

		if ( ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase( format ) )
		{
			context.put( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
					createPDFrenderContext( request.getServletPath( ) ) );
		}
		else if ( htmlRenderContext != null )
		{
			context.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
					htmlRenderContext );
		}
		else
		{
			context
					.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
							createHTMLrenderContext( svgFlag, request
									.getServletPath( ) ) );
		}

		runAndRenderTask.setAppContext( context );

		try
		{
			runAndRenderTask.run( );
		}
		catch ( BirtException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.runAndRenderReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			runAndRenderTask.close( );
		}
	}

	/**
	 * Run report.
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param archive
	 * @param documentName
	 * @param locale
	 * @param parameters
	 * @throws RemoteException
	 */
	public void runReport( HttpServletRequest request,
			IReportRunnable runnable, String documentName, Locale locale,
			Map parameters ) throws RemoteException
	{
		runReport( request, runnable, documentName, locale, parameters, null );
	}

	/**
	 * Run report.
	 * 
	 * @param request
	 * 
	 * @param runnable
	 * @param archive
	 * @param documentName
	 * @param locale
	 * @param parameters
	 * @param displayTexts
	 * @throws RemoteException
	 */
	public void runReport( HttpServletRequest request,
			IReportRunnable runnable, String documentName, Locale locale,
			Map parameters, Map displayTexts ) throws RemoteException
	{
		assert runnable != null;

		// Preapre the run report task.
		IRunTask runTask = null;
		runTask = engine.createRunTask( runnable );
		runTask.setLocale( locale );
		runTask.setParameterValues( parameters );

		// Set display Text for select parameters
		if ( displayTexts != null )
		{
			Iterator keys = displayTexts.keySet( ).iterator( );
			while ( keys.hasNext( ) )
			{
				String paramName = DataUtil.getString( keys.next( ) );
				String displayText = DataUtil.getString( displayTexts
						.get( paramName ) );
				runTask.setParameterDisplayText( paramName, displayText );
			}
		}

		HashMap context = new HashMap( );
		// context.put(DataEngine.DATASET_CACHE_OPTION, Boolean.TRUE )running in
		// designer enviroment; if running in deployment, set it to false
		Boolean isDesigner = Boolean.valueOf( ParameterAccessor
				.isDesigner( request ) );
		context
				.put(
						"org.eclipse.birt.data.engine.dataset.cache.option", isDesigner ); //$NON-NLS-1$
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );
		runTask.setAppContext( context );

		// Run report.
		try
		{
			runTask.run( documentName );
		}
		catch ( BirtException e )
		{
			// Any Birt exception.
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.runReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			runTask.close( );
		}
	}

	/**
	 * Render report page.
	 * 
	 * @param request
	 * 
	 * @param reportDocument
	 * @param pageNumber
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @return report page content
	 * @throws RemoteException
	 */
	public ByteArrayOutputStream renderReport( HttpServletRequest request,
			IReportDocument reportDocument, long pageNumber,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		renderReport( out, request, reportDocument, pageNumber, masterPage,
				svgFlag, activeIds, locale, rtl );
		return out;
	}

	/**
	 * Render report page.
	 * 
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @throws RemoteException
	 */

	public void renderReport( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, long pageNumber,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		assert reportDocument != null;
		assert pageNumber > 0 && pageNumber <= reportDocument.getPageCount( );

		OutputStream out = os;
		if ( out == null )
			out = new ByteArrayOutputStream( );

		// Create render task.
		IRenderTask renderTask = engine.createRenderTask( reportDocument );

		HashMap context = new HashMap( );
		String format = ParameterAccessor.getFormat( request );
		if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF ) )
		{
			context.put( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
					createPDFrenderContext( request.getServletPath( ) ) );
		}
		else
		{
			context
					.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
							createHTMLrenderContext( svgFlag, request
									.getServletPath( ) ) );
		}
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );
		renderTask.setAppContext( context );

		// Render option
		HTMLRenderOption setting = new HTMLRenderOption( );
		setting.setOutputStream( out );
		setting.setOutputFormat( format );
		if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF ) )
		{
			setting
					.setActionHandle( new ViewerHTMLActionHandler(
							reportDocument, pageNumber, locale, false, rtl,
							masterPage ) );
		}
		else
		{
			boolean isEmbeddable = false;
			if ( ParameterAccessor.SERVLET_PATH_FRAMESET
					.equalsIgnoreCase( request.getServletPath( ) ) )
				isEmbeddable = true;
			setting.setEmbeddable( isEmbeddable );
			setting.setHtmlRtLFlag( rtl );
			setting.setInstanceIDs( activeIds );
			setting.setMasterPageContent( masterPage );
			setting.setActionHandle( new ViewerHTMLActionHandler(
					reportDocument, pageNumber, locale, isEmbeddable, rtl,
					masterPage ) );
		}

		renderTask.setRenderOption( setting );
		renderTask.setLocale( locale );

		// Render designated page.
		try
		{
			if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF )
					|| IBirtConstants.SERVLET_PATH_RUN
							.equalsIgnoreCase( request.getServletPath( ) ) )
				renderTask.render( );
			else
			{
				renderTask.setPageNumber( pageNumber );
				renderTask.render( );
			}
		}
		catch ( BirtException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			renderTask.close( );
		}
	}

	/**
	 * Render report page.
	 * 
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param masterPage
	 * @param pageNumber
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @throws RemoteException
	 */

	public void renderReportlet( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, String reportletId,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		assert reportDocument != null;

		OutputStream out = os;
		if ( out == null )
			out = new ByteArrayOutputStream( );

		// Create render task.
		IRenderTask renderTask = engine.createRenderTask( reportDocument );

		HashMap context = new HashMap( );
		String format = ParameterAccessor.getFormat( request );
		if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF ) )
		{
			context.put( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
					createPDFrenderContext( request.getServletPath( ) ) );
		}
		else
		{
			context
					.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
							createHTMLrenderContext( svgFlag, request
									.getServletPath( ) ) );
		}
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );
		renderTask.setAppContext( context );

		// Render option
		HTMLRenderOption setting = new HTMLRenderOption( );
		setting.setOutputStream( out );
		if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF ) )
		{
			setting.setOutputFormat( IBirtConstants.PDF_RENDER_FORMAT );
			setting.setActionHandle( new ViewerHTMLActionHandler(
					reportDocument, -1, locale, false, rtl, masterPage ) );
		}
		else
		{
			setting.setOutputFormat( IBirtConstants.HTML_RENDER_FORMAT );
			boolean isEmbeddable = false;
			if ( ParameterAccessor.SERVLET_PATH_FRAMESET
					.equalsIgnoreCase( request.getServletPath( ) ) )
				isEmbeddable = true;
			setting.setEmbeddable( isEmbeddable );
			setting.setHtmlRtLFlag( rtl );
			setting.setInstanceIDs( activeIds );
			setting.setMasterPageContent( masterPage );
			setting
					.setActionHandle( new ViewerHTMLActionHandler(
							reportDocument, -1, locale, isEmbeddable, rtl,
							masterPage ) );
		}

		renderTask.setRenderOption( setting );
		renderTask.setLocale( locale );

		// Render designated page.
		try
		{
			if ( ParameterAccessor.isIidReportlet( request ) )
			{
				InstanceID instanceId = InstanceID.parse( reportletId );
				renderTask.setInstanceID( instanceId );
			}
			else
			{
				renderTask.setReportlet( reportletId );
			}
			if ( format.equalsIgnoreCase( ParameterAccessor.PARAM_FORMAT_PDF ) )
				renderTask.render( );
			else
			{
				renderTask.render( );
			}
		}
		catch ( BirtException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			renderTask.close( );
		}
	}

	/**
	 * Get query result sets.
	 * 
	 * @param document
	 * @return the result sets from the document
	 * @throws RemoteException
	 */
	public ResultSet[] getResultSets( IReportDocument document )
			throws RemoteException
	{
		assert document != null;

		ResultSet[] resultSetArray = null;

		IDataExtractionTask dataTask = engine
				.createDataExtractionTask( document );

		try
		{
			List resultSets = dataTask.getResultSetList( );
			resultSetArray = new ResultSet[resultSets.size( )];

			if ( resultSets.size( ) > 0 )
			{
				for ( int k = 0; k < resultSets.size( ); k++ )
				{
					resultSetArray[k] = new ResultSet( );
					IResultSetItem resultSetItem = (IResultSetItem) resultSets
							.get( k );
					assert resultSetItem != null;

					resultSetArray[k].setQueryName( resultSetItem
							.getResultSetName( ) );

					IResultMetaData metaData = resultSetItem
							.getResultMetaData( );
					assert metaData != null;

					Column[] columnArray = new Column[metaData.getColumnCount( )];
					for ( int i = 0; i < metaData.getColumnCount( ); i++ )
					{
						columnArray[i] = new Column( );

						String name = metaData.getColumnName( i );
						columnArray[i].setName( name );

						String label = metaData.getColumnLabel( i );
						if ( label == null || label.length( ) <= 0 )
						{
							label = name;
						}
						columnArray[i].setLabel( label );

						columnArray[i].setVisibility( new Boolean( true ) );
					}
					resultSetArray[k].setColumn( columnArray );
				}
			}
		}
		catch ( BirtException e )
		{
			e.printStackTrace( );
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.getResultSets( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		catch ( Exception e )
		{
			e.printStackTrace( );
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.getResultSets( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			dataTask.close( );
		}

		return resultSetArray;
	}

	/**
	 * Extract data.
	 * 
	 * @param document
	 * @param resultSetName
	 * @param id
	 * @param columns
	 * @param filters
	 * @param locale
	 * @param outputStream
	 * @throws RemoteException
	 */
	public void extractData( IReportDocument document, String resultSetName,
			Collection columns, Locale locale, OutputStream outputStream )
			throws RemoteException
	{
		assert document != null;
		assert resultSetName != null && resultSetName.length( ) > 0;
		assert columns != null && !columns.isEmpty( );

		String[] columnNames = new String[columns.size( )];
		Iterator iSelectedColumns = columns.iterator( );
		for ( int i = 0; iSelectedColumns.hasNext( ); i++ )
		{
			columnNames[i] = (String) iSelectedColumns.next( );
		}

		IDataExtractionTask dataTask = null;
		IExtractionResults result = null;
		IDataIterator iData = null;
		try
		{
			dataTask = engine.createDataExtractionTask( document );
			dataTask.selectResultSet( resultSetName );
			dataTask.selectColumns( columnNames );
			dataTask.setLocale( locale );

			result = dataTask.extract( );
			if ( result != null )
			{
				iData = result.nextResultIterator( );

				if ( iData != null && columnNames.length > 0 )
				{
					StringBuffer buf = new StringBuffer( );

					// Captions
					buf.append( columnNames[0] );

					for ( int i = 1; i < columnNames.length; i++ )
					{
						buf.append( ',' );
						buf.append( columnNames[i] );
					}

					buf.append( '\n' );
					outputStream.write( buf.toString( ).getBytes(
							ParameterAccessor.UTF_8_ENCODE ) );

					buf.delete( 0, buf.length( ) );

					// Data
					while ( iData.next( ) )
					{
						String value = null;

						try
						{
							value = cvsConvertor( (String) DataTypeUtil
									.convert( iData.getValue( columnNames[0] ),
											DataType.STRING_TYPE ) );
						}
						catch ( Exception e )
						{
							// do nothing
						}

						if ( value != null )
						{
							buf.append( value );
						}

						for ( int i = 1; i < columnNames.length; i++ )
						{
							buf.append( ',' );

							try
							{
								value = cvsConvertor( (String) DataTypeUtil
										.convert( iData
												.getValue( columnNames[i] ),
												DataType.STRING_TYPE ) );
							}
							catch ( Exception e )
							{
								value = null;
							}

							if ( value != null )
							{
								buf.append( value );
							}
						}

						buf.append( '\n' );
						outputStream.write( buf.toString( ).getBytes(
								ParameterAccessor.UTF_8_ENCODE ) );
						buf.delete( 0, buf.length( ) );
					}
				}
			}
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.extractData( )" ) ); //$NON-NLS-1$
			fault.setFaultString( e.getLocalizedMessage( ) );
			throw fault;
		}
		finally
		{
			if ( iData != null )
			{
				iData.close( );
			}

			if ( result != null )
			{
				result.close( );
			}

			if ( dataTask != null )
			{
				dataTask.close( );
			}
		}
	}

	/**
	 * CSV format convertor. Here is the rule.
	 * 
	 * 1) Fields with embedded commas must be delimited with double-quote
	 * characters. 2) Fields that contain double quote characters must be
	 * surounded by double-quotes, and the embedded double-quotes must each be
	 * represented by a pair of consecutive double quotes. 3) A field that
	 * contains embedded line-breaks must be surounded by double-quotes. 4)
	 * Fields with leading or trailing spaces must be delimited with
	 * double-quote characters.
	 * 
	 * @param value
	 * @return the cvs format string value
	 * @throws RemoteException
	 */
	private String cvsConvertor( String value ) throws RemoteException
	{
		if ( value == null )
		{
			return null;
		}

		value = value.replaceAll( "\"", "\"\"" ); //$NON-NLS-1$  //$NON-NLS-2$

		boolean needQuote = false;
		needQuote = ( value.indexOf( ',' ) != -1 )
				|| ( value.indexOf( '"' ) != -1 )
				|| ( value.indexOf( 0x0A ) != -1 )
				|| value.startsWith( " " ) || value.endsWith( " " ); //$NON-NLS-1$ //$NON-NLS-2$
		value = needQuote ? "\"" + value + "\"" : value; //$NON-NLS-1$ //$NON-NLS-2$

		return value;
	}

	/**
	 * Prepare the report parameters.
	 * 
	 * @param request
	 * @param task
	 * @param configVars
	 * @param locale
	 * @return map of the request parameters
	 */
	public HashMap parseParameters( HttpServletRequest request,
			IGetParameterDefinitionTask task, Map configVars, Locale locale )
	{
		assert task != null;
		HashMap params = new HashMap( );

		Collection parameterList = task.getParameterDefns( false );
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			IScalarParameterDefn parameterObj = (IScalarParameterDefn) iter
					.next( );

			String paramValue = null;
			Object paramValueObj = null;

			// ScalarParameterHandle paramHandle = ( ScalarParameterHandle )
			// parameterObj
			// .getHandle( );
			String paramName = parameterObj.getName( );
			String format = parameterObj.getDisplayFormat( );

			// Get default value from task
			ReportParameterConverter converter = new ReportParameterConverter(
					format, locale );

			if ( ParameterAccessor.isReportParameterExist( request, paramName ) )
			{
				// Get value from http request
				paramValue = ParameterAccessor.getReportParameter( request,
						paramName, paramValue );
				paramValueObj = converter.parse( paramValue, parameterObj
						.getDataType( ) );
			}
			else if ( ParameterAccessor.isDesigner( request )
					&& configVars.containsKey( paramName ) )
			{
				// Get value from test config
				String configValue = (String) configVars.get( paramName );
				ReportParameterConverter cfgConverter = new ReportParameterConverter(
						format, Locale.US );
				paramValueObj = cfgConverter.parse( configValue, parameterObj
						.getDataType( ) );
			}
			else
			{
				paramValueObj = task.getDefaultValue( parameterObj.getName( ) );
			}

			params.put( paramName, paramValueObj );
		}

		return params;
	}

	/**
	 * Check whether missing parameter or not.
	 * 
	 * @param task
	 * @param parameters
	 * @return true if all the parameter values are valid, otherwise false
	 */
	public boolean validateParameters( IGetParameterDefinitionTask task,
			Map parameters )
	{
		assert task != null;
		assert parameters != null;

		boolean missingParameter = false;

		Collection parameterList = task.getParameterDefns( false );
		for ( Iterator iter = parameterList.iterator( ); iter.hasNext( ); )
		{
			IScalarParameterDefn parameterObj = (IScalarParameterDefn) iter
					.next( );
			// ScalarParameterHandle paramHandle = ( ScalarParameterHandle )
			// parameterObj
			// .getHandle( );

			String parameterName = parameterObj.getName( );
			Object parameterValue = parameters.get( parameterName );

			if ( parameterObj.isHidden( ) )
			{
				continue;
			}

			if ( parameterValue == null && !parameterObj.allowNull( ) )
			{
				missingParameter = true;
				break;
			}

			if ( IScalarParameterDefn.TYPE_STRING == parameterObj.getDataType( ) )
			{
				String parameterStringValue = (String) parameterValue;
				if ( parameterStringValue != null
						&& parameterStringValue.length( ) <= 0
						&& !parameterObj.allowBlank( ) )
				{
					missingParameter = true;
					break;
				}
			}
		}

		return missingParameter;
	}

	/**
	 * uses to clear the data cach.
	 * 
	 * @param dataSet
	 *            the dataset handle
	 * @throws BirtException
	 */
	public void clearCache( DataSetHandle dataSet ) throws BirtException
	{
		DataSessionContext context = new DataSessionContext(
				DataSessionContext.MODE_DIRECT_PRESENTATION, dataSet
						.getModuleHandle( ), null );

		DataRequestSession requestSession = DataRequestSession
				.newSession( context );

		IModelAdapter modelAdaptor = requestSession.getModelAdaptor( );
		DataSourceHandle dataSource = dataSet.getDataSource( );

		IBaseDataSourceDesign sourceDesign = modelAdaptor
				.adaptDataSource( dataSource );
		IBaseDataSetDesign dataSetDesign = modelAdaptor.adaptDataSet( dataSet );

		requestSession.clearCache( sourceDesign, dataSetDesign );
	}

	/**
	 * @param maxRows
	 */
	public void setMaxRows( int maxRows )
	{
		if ( config != null )
		{
			config.setMaxRowsPerQuery( maxRows );
		}
	}

	/**
	 * Collects all the distinct values for the given element and
	 * bindColumnName. This method will traverse the design tree for the given
	 * element and get the nearest binding column holder of it. The nearest
	 * binding column holder must be a list or table item, and it defines a
	 * distinct data set and bingding columns in it. If the element is null,
	 * binding name is empty or the binding column holder is not found, then
	 * return <code>Collections.EMPTY_LIST</code>. Caller can specify the max
	 * row number and start row number by implement the interface IRequestInfo.
	 * 
	 * @param bindingName
	 * @param elementHandle
	 * @param requestInfo
	 * @return list of available column value
	 * @throws BirtException
	 */

	public List getColumnValueSet( String bindingName,
			DesignElementHandle elementHandle, IRequestInfo requestInfo )
			throws BirtException
	{
		if ( bindingName == null || elementHandle == null
				|| !( elementHandle instanceof ReportItemHandle ) )
			return Collections.EMPTY_LIST;

		// if there is no effective holder of bindings, return empty
		ReportItemHandle reportItem = getBindingHolder( elementHandle );
		if ( reportItem == null )
			return Collections.EMPTY_LIST;

		List selectValueList = new ArrayList( );
		DataRequestSession session = DataRequestSession
				.newSession( new DataSessionContext(
						DataSessionContext.MODE_DIRECT_PRESENTATION, reportItem
								.getModuleHandle( ) ) );
		selectValueList.addAll( session.getColumnValueSet( reportItem
				.getDataSet( ), reportItem.paramBindingsIterator( ), reportItem
				.columnBindingsIterator( ), bindingName, requestInfo ) );
		session.shutdown( );

		return selectValueList;
	}

	/**
	 * Collects all the distinct values for the given element and
	 * bindColumnName. This method will traverse the design tree for the given
	 * element and get the nearest binding column holder of it. The nearest
	 * binding column holder must be a list or table item, and it defines a
	 * distinct data set and bingding columns in it. If the element is null,
	 * binding name is empty or the binding column holder is not found, then
	 * return <code>Collections.EMPTY_LIST</code>.
	 * 
	 * @param bindingName
	 * @param elementHandle
	 * @return list of the avaliable column value
	 * @throws BirtException
	 */

	public List getColumnValueSet( String bindingName,
			DesignElementHandle elementHandle ) throws BirtException
	{
		if ( bindingName == null || elementHandle == null
				|| !( elementHandle instanceof ReportItemHandle ) )
			return Collections.EMPTY_LIST;

		// if there is no effective holder of bindings, return empty
		ReportItemHandle reportItem = getBindingHolder( elementHandle );
		if ( reportItem == null )
			return Collections.EMPTY_LIST;

		List selectValueList = new ArrayList( );
		DataRequestSession session = DataRequestSession
				.newSession( new DataSessionContext(
						DataSessionContext.MODE_DIRECT_PRESENTATION, reportItem
								.getModuleHandle( ) ) );
		selectValueList.addAll( session.getColumnValueSet( reportItem
				.getDataSet( ), reportItem.paramBindingsIterator( ), reportItem
				.columnBindingsIterator( ), bindingName ) );
		session.shutdown( );

		return selectValueList;
	}

	/**
	 * Returns the element handle which can save binding columns the given
	 * element
	 * 
	 * @param handle
	 *            the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */

	private ReportItemHandle getBindingHolder( DesignElementHandle handle )
	{
		if ( handle instanceof ReportElementHandle )
		{
			if ( handle instanceof ListingHandle )
			{
				return (ReportItemHandle) handle;
			}
			if ( handle instanceof ReportItemHandle )
			{
				if ( ( (ReportItemHandle) handle ).getDataSet( ) != null
						|| ( (ReportItemHandle) handle )
								.columnBindingsIterator( ).hasNext( ) )
				{
					return (ReportItemHandle) handle;
				}
			}
			ReportItemHandle result = getBindingHolder( handle.getContainer( ) );
			if ( result == null && handle instanceof ReportItemHandle )
			{
				result = (ReportItemHandle) handle;
			}
			return result;
		}
		return null;
	}

	/**
	 * Gets the mime-type of the given emitter format.
	 * 
	 * @param format
	 * @return mime-type of the extended emitter format
	 */

	public String getMIMEType( String format )
	{
		return engine.getMIMEType( format );
	}

	/**
	 * Shutdown ReportEngineService, set instance as null
	 */
	public static void shutdown( )
	{
		instance = null;
	}

}