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
import java.util.Map.Entry;
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
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
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
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.ReportParameterConverter;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.exception.ViewerException;
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
import org.eclipse.birt.report.utility.BirtUtility;
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
	 * @param servletContext
	 * @param config
	 */
	private ReportEngineService( ServletContext servletContext )
	{
		System.setProperty( "RUN_UNDER_ECLIPSE", "false" ); //$NON-NLS-1$ //$NON-NLS-2$

		if ( servletContext == null )
		{
			return;
		}

		// Init context parameters
		ParameterAccessor.initParameters( servletContext );

		config = new EngineConfig( );

		// Register new image handler
		HTMLRenderOption emitterConfig = new HTMLRenderOption( );
		emitterConfig.setActionHandler( new HTMLActionHandler( ) );
		imageHandler = new HTMLServerImageHandler( );
		emitterConfig.setImageHandler( imageHandler );
		config.getEmitterConfigs( ).put( "html", emitterConfig ); //$NON-NLS-1$

		// Prepare image base url.
		imageBaseUrl = IBirtConstants.SERVLET_PATH_PREVIEW + "?__imageID="; //$NON-NLS-1$

		// Prepare log level.
		String logLevel = ParameterAccessor.logLevel;
		Level level = logLevel != null && logLevel.length( ) > 0 ? Level
				.parse( logLevel ) : Level.OFF;
		config.setLogConfig( ParameterAccessor.logFolder, level );

		// Prepare ScriptLib location
		String scriptLibDir = ParameterAccessor.scriptLibDir;

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
		initEngineInstance( servletConfig.getServletContext( ) );
	}

	/**
	 * Get engine instance.
	 * 
	 * @param servletContext
	 * @throws BirtException
	 * 
	 */
	public synchronized static void initEngineInstance(
			ServletContext servletContext ) throws BirtException
	{
		if ( ReportEngineService.instance != null )
		{
			return;
		}
		ReportEngineService.instance = new ReportEngineService( servletContext );
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
	 * @throws BirtException
	 */
	public synchronized void setEngineContext( ServletContext servletContext,
			HttpServletRequest request ) throws BirtException
	{
		if ( engine == null )
		{
			IPlatformContext platformContext = new PlatformServletContext(
					servletContext );
			config.setPlatformContext( platformContext );

			// Startup OSGI Platform
			Platform.startup( config );

			IReportEngineFactory factory = (IReportEngineFactory) Platform
					.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			if ( factory == null )
			{
				// if null, throw exception
				throw new ViewerException(
						ResourceConstants.REPORT_SERVICE_EXCEPTION_STARTUP_REPORTENGINE_ERROR );
			}
			engine = factory.createReportEngine( config );

			// Get supported output formats
			ParameterAccessor.supportedFormats = engine.getSupportedFormats( );

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
	 * @param request
	 * @param outputStream
	 * @throws RemoteException
	 */
	public void renderImage( String imageId, HttpServletRequest request,
			OutputStream outputStream ) throws RemoteException
	{
		assert ( this.imageHandler != null );

		try
		{
			this.imageHandler.getImage( outputStream, ParameterAccessor
					.getImageTempFolder( request ), imageId );
		}
		catch ( EngineException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.renderImage( )" ) ); //$NON-NLS-1$
			throw fault;
		}

	}

	/**
	 * Create HTML render option.
	 * 
	 * @param svgFlag
	 * @param servletPath
	 * @param request
	 * @return HTML render option from the given arguments
	 */
	private HTMLRenderOption createHTMLRenderOption( boolean svgFlag,
			String servletPath, HttpServletRequest request )
	{
		String baseURL = null;
		boolean isDesigner = ParameterAccessor.isDesigner( request );

		// try to get base url from config file
		if ( !isDesigner )
			baseURL = ParameterAccessor.getBaseURL( );

		if ( baseURL == null )
			baseURL = ""; //$NON-NLS-1$

		// append application context path
		baseURL += this.contextPath;

		HTMLRenderOption renderOption = new HTMLRenderOption( );
		renderOption.setImageDirectory( ParameterAccessor
				.getImageTempFolder( request ) );
		renderOption.setBaseImageURL( baseURL + imageBaseUrl );
		if ( servletPath != null && servletPath.length( ) > 0 )
		{
			renderOption.setBaseURL( baseURL + servletPath );
		}
		else
		{
			renderOption.setBaseURL( baseURL + IBirtConstants.SERVLET_PATH_RUN );
		}
		renderOption.setSupportedImageFormats( svgFlag
				? "PNG;GIF;JPG;BMP;SVG" : "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$ //$NON-NLS-2$
		return renderOption;
	}

	/**
	 * Create PDF render option.
	 * 
	 * @param servletPath
	 * @param request
	 * @param isDesigner
	 * @return the PDF render option
	 */
	private PDFRenderOption createPDFRenderOption( String servletPath,
			HttpServletRequest request, boolean isDesigner )
	{
		String baseURL = null;
		// try to get base url from config file
		if ( !isDesigner )
			baseURL = ParameterAccessor.getBaseURL( );

		if ( baseURL == null )
			baseURL = ""; //$NON-NLS-1$

		// append application context path
		baseURL += this.contextPath;

		PDFRenderOption renderOption = new PDFRenderOption( );
		if ( servletPath != null && servletPath.length( ) > 0 )
		{
			renderOption.setBaseURL( baseURL + servletPath );
		}
		else
		{
			renderOption.setBaseURL( baseURL + IBirtConstants.SERVLET_PATH_RUN );
		}
		renderOption.setSupportedImageFormats( "PNG;GIF;JPG;BMP" ); //$NON-NLS-1$

		// fit to page setting
		renderOption.setOption( PDFRenderOption.FIT_TO_PAGE, new Boolean(
				ParameterAccessor.isFitToPage( request ) ) );

		// pagebreak pagination only setting
		renderOption.setOption( PDFRenderOption.PAGEBREAK_PAGINATION_ONLY,
				new Boolean( ParameterAccessor.isPagebreakOnly( request ) ) );

		return renderOption;
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
				rtl, parameters, masterPage, svgFlag, null, null, null, null,
				null, null );
	}

	/**
	 * Run and render a report with certain servlet path
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
	 * @param servletPath
	 * @param reportTitle
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, OutputStream outputStream, String format,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, Map displayTexts, String servletPath,
			String reportTitle ) throws RemoteException
	{
		runAndRenderReport( request, runnable, outputStream, format, locale,
				rtl, parameters, masterPage, svgFlag, null, null, null,
				displayTexts, servletPath, reportTitle );
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
	 * @param renderOption
	 * @param displayTexts
	 * @param iServletPath
	 * @param reportTitle
	 * @throws RemoteException
	 * @throws IOException
	 */
	public void runAndRenderReport( HttpServletRequest request,
			IReportRunnable runnable, OutputStream outputStream, String format,
			Locale locale, boolean rtl, Map parameters, boolean masterPage,
			boolean svgFlag, Boolean embeddable, List activeIds,
			RenderOption renderOption, Map displayTexts, String iServletPath,
			String reportTitle ) throws RemoteException
	{
		assert runnable != null;

		String servletPath = iServletPath;
		if ( servletPath == null )
			servletPath = request.getServletPath( );

		IRunAndRenderTask runAndRenderTask = engine
				.createRunAndRenderTask( runnable );
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

		// Client DPI setting
		context.put( EngineConstants.APPCONTEXT_CHART_RESOLUTION,
				ParameterAccessor.getDpi( request ) );

		// Push user-defined application context
		ParameterAccessor.pushAppContext( context, request );
		runAndRenderTask.setAppContext( context );

		// Render options
		if ( renderOption == null )
		{
			if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( format )
					|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT
							.equalsIgnoreCase( format ) )
			{
				renderOption = createPDFRenderOption( servletPath, request,
						isDesigner.booleanValue( ) );
			}
			else
			{
				renderOption = createHTMLRenderOption( svgFlag, servletPath,
						request );
			}
		}

		renderOption.setOutputStream( outputStream );
		renderOption.setOutputFormat( format );
		renderOption.setOption( IHTMLRenderOption.MASTER_PAGE_CONTENT,
				new Boolean( masterPage ) );
		renderOption.setOption( IHTMLRenderOption.HTML_RTL_FLAG, new Boolean(
				rtl ) );

		ViewerHTMLActionHandler handler = new ViewerHTMLActionHandler( locale,
				rtl, masterPage, format );
		String resourceFolder = ParameterAccessor.getParameter( request,
				ParameterAccessor.PARAM_RESOURCE_FOLDER );
		handler.setResourceFolder( resourceFolder );
		renderOption.setActionHandler( handler );

		if ( reportTitle != null )
			renderOption.setOption( IHTMLRenderOption.HTML_TITLE, reportTitle );

		if ( renderOption instanceof IHTMLRenderOption )
		{
			boolean isEmbeddable = false;
			if ( embeddable != null )
				isEmbeddable = embeddable.booleanValue( );

			if ( IBirtConstants.SERVLET_PATH_RUN.equalsIgnoreCase( servletPath ) )
				isEmbeddable = true;

			( (IHTMLRenderOption) renderOption ).setEmbeddable( isEmbeddable );
		}

		renderOption.setOption( IHTMLRenderOption.INSTANCE_ID_LIST, activeIds );

		// initialize emitter configs
		initializeEmitterConfigs( request, renderOption.getOptions( ) );

		runAndRenderTask.setRenderOption( renderOption );

		// add task into session
		BirtUtility.addTask( request, runAndRenderTask );

		try
		{
			runAndRenderTask.run( );
		}
		catch ( BirtException e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.runAndRenderReport( )" ) ); //$NON-NLS-1$			
			throw fault;
		}
		finally
		{
			// Remove task from http session
			BirtUtility.removeTask( request );
			runAndRenderTask.close( );
		}
	}

	/**
	 * Fills dynamic options with parameters from request.
	 */
	private void initializeEmitterConfigs( HttpServletRequest request,
			Map config )
	{
		if ( config == null )
		{
			return;
		}

		for ( Iterator itr = request.getParameterMap( ).entrySet( ).iterator( ); itr
				.hasNext( ); )
		{
			Entry entry = (Entry) itr.next( );

			String name = String.valueOf( entry.getKey( ) );

			// only process parameters start with "__"
			if ( name.startsWith( "__" ) ) //$NON-NLS-1$
			{
				config.put( name.substring( 2 ), ParameterAccessor
						.getParameter( request, name ) );
			}
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
	 * @deprecated
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

		// add task into session
		BirtUtility.addTask( request, runTask );

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

		// Push user-defined application context
		ParameterAccessor.pushAppContext( context, request );

		runTask.setAppContext( context );

		// Run report.
		try
		{
			runTask.run( documentName );
		}
		catch ( BirtException e )
		{
			// clear document file
			File doc = new File( documentName );
			if ( doc != null )
				doc.delete( );

			// Any Birt exception.
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.runReport( )" ) ); //$NON-NLS-1$			
			throw fault;
		}
		finally
		{
			// Remove task from http session
			BirtUtility.removeTask( request );
			runTask.close( );
		}
	}

	/**
	 * Render report page.
	 * 
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @deprecated
	 * @return report page content
	 * @throws RemoteException
	 */
	public ByteArrayOutputStream renderReport( HttpServletRequest request,
			IReportDocument reportDocument, long pageNumber,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		renderReport( out, request, reportDocument, null, pageNumber, null,
				masterPage, svgFlag, activeIds, locale, rtl, null );
		return out;
	}

	/**
	 * Render report page.
	 * 
	 * @param request
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
			IReportDocument reportDocument, String format, long pageNumber,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream( );
		renderReport( out, request, reportDocument, format, pageNumber, null,
				masterPage, svgFlag, activeIds, locale, rtl, null );
		return out;
	}

	/**
	 * Render report page.
	 * 
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param pageNumber
	 * @param pageRange
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @deprecated
	 * @throws RemoteException
	 */
	public void renderReport( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, long pageNumber, String pageRange,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl, String iServletPath ) throws RemoteException
	{
		renderReport( os, request, reportDocument, null, pageNumber, pageRange,
				masterPage, svgFlag, activeIds, locale, rtl, iServletPath );
	}

	/**
	 * Render report page.
	 * 
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param format
	 * @param pageNumber
	 * @param pageRange
	 * @param masterPage
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @throws RemoteException
	 */
	public void renderReport( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, String format, long pageNumber,
			String pageRange, boolean masterPage, boolean svgFlag,
			List activeIds, Locale locale, boolean rtl, String iServletPath )
			throws RemoteException
	{
		if ( reportDocument == null )
		{
			AxisFault fault = new AxisFault(
					BirtResources
							.getMessage( ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			throw fault;
		}

		OutputStream out = os;
		if ( out == null )
			out = new ByteArrayOutputStream( );

		// get servlet path
		String servletPath = iServletPath;
		if ( servletPath == null )
			servletPath = request.getServletPath( );

		// Create render task.
		IRenderTask renderTask = engine.createRenderTask( reportDocument );

		// add task into session
		BirtUtility.addTask( request, renderTask );

		HashMap context = new HashMap( );
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );

		// Client DPI setting
		context.put( EngineConstants.APPCONTEXT_CHART_RESOLUTION,
				ParameterAccessor.getDpi( request ) );

		// Push user-defined application context
		ParameterAccessor.pushAppContext( context, request );
		renderTask.setAppContext( context );

		RenderOption renderOption = null;
		if ( format == null )
			format = ParameterAccessor.getFormat( request );

		if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( format )
				|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT
						.equalsIgnoreCase( format ) )
		{
			renderOption = createPDFRenderOption( servletPath, request,
					ParameterAccessor.isDesigner( request ) );
		}
		else
		{
			renderOption = createHTMLRenderOption( svgFlag, servletPath,
					request );
		}

		renderOption.setOutputStream( out );
		renderOption.setOutputFormat( format );
		ViewerHTMLActionHandler handler = null;
		if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( format )
				|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT
						.equalsIgnoreCase( format ) )
		{
			handler = new ViewerHTMLActionHandler( reportDocument, pageNumber,
					locale, false, rtl, masterPage, format );
		}
		else
		{
			boolean isEmbeddable = false;
			if ( IBirtConstants.SERVLET_PATH_FRAMESET
					.equalsIgnoreCase( servletPath )
					|| IBirtConstants.SERVLET_PATH_RUN
							.equalsIgnoreCase( servletPath ) )
				isEmbeddable = true;
			if ( renderOption instanceof IHTMLRenderOption )
				( (IHTMLRenderOption) renderOption )
						.setEmbeddable( isEmbeddable );

			renderOption.setOption( IHTMLRenderOption.HTML_RTL_FLAG,
					new Boolean( rtl ) );
			renderOption.setOption( IHTMLRenderOption.INSTANCE_ID_LIST,
					activeIds );
			renderOption.setOption( IHTMLRenderOption.MASTER_PAGE_CONTENT,
					new Boolean( masterPage ) );
			handler = new ViewerHTMLActionHandler( reportDocument, pageNumber,
					locale, isEmbeddable, rtl, masterPage, format );
		}
		String resourceFolder = ParameterAccessor.getParameter( request,
				ParameterAccessor.PARAM_RESOURCE_FOLDER );
		handler.setResourceFolder( resourceFolder );
		renderOption.setActionHandler( handler );

		// initialize emitter configs
		initializeEmitterConfigs( request, renderOption.getOptions( ) );

		renderTask.setRenderOption( renderOption );
		renderTask.setLocale( locale );

		// Render designated page.
		try
		{
			if ( pageNumber > 0 )
				renderTask.setPageNumber( pageNumber );

			if ( pageRange != null )
			{
				if ( !IBirtConstants.SERVLET_PATH_FRAMESET
						.equalsIgnoreCase( servletPath )
						|| !ParameterAccessor.PARAM_FORMAT_HTML
								.equalsIgnoreCase( format ) )
					renderTask.setPageRange( pageRange );
			}

			renderTask.render( );
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$			
			throw fault;
		}
		finally
		{
			// Remove task from http session
			BirtUtility.removeTask( request );
			renderTask.close( );
		}
	}

	/**
	 * Render reportlet page with certain servlet path
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
	 * @deprecated
	 * @throws RemoteException
	 */

	public void renderReportlet( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, String reportletId,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl ) throws RemoteException
	{
		renderReportlet( os, request, reportDocument, reportletId, null,
				masterPage, svgFlag, activeIds, locale, rtl, null );
	}

	/**
	 * Render reportlet page.
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
	 * @param iServletPath
	 * @deprecated
	 * @throws RemoteException
	 */

	public void renderReportlet( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, String reportletId,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl, String iServletPath ) throws RemoteException
	{
		renderReportlet( os, request, reportDocument, reportletId, null,
				masterPage, svgFlag, activeIds, locale, rtl, iServletPath );
	}

	/**
	 * Render reportlet page.
	 * 
	 * @param os
	 * @param request
	 * @param reportDocument
	 * @param reportletId
	 * @param format
	 * @param masterPage
	 * @param pageNumber
	 * @param svgFlag
	 * @param activeIds
	 * @param locale
	 * @param rtl
	 * @param iServletPath
	 * @throws RemoteException
	 */

	public void renderReportlet( OutputStream os, HttpServletRequest request,
			IReportDocument reportDocument, String reportletId, String format,
			boolean masterPage, boolean svgFlag, List activeIds, Locale locale,
			boolean rtl, String iServletPath ) throws RemoteException
	{
		if ( reportDocument == null )
		{
			AxisFault fault = new AxisFault(
					BirtResources
							.getMessage( ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReportlet( )" ) ); //$NON-NLS-1$
			throw fault;
		}

		OutputStream out = os;
		if ( out == null )
			out = new ByteArrayOutputStream( );

		String servletPath = iServletPath;
		if ( servletPath == null )
			servletPath = request.getServletPath( );

		// Create render task.
		IRenderTask renderTask = engine.createRenderTask( reportDocument );

		// add task into session
		BirtUtility.addTask( request, renderTask );

		HashMap context = new HashMap( );
		context.put( EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST,
				request );
		context.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
				ReportEngineService.class.getClassLoader( ) );

		// Client DPI setting
		context.put( EngineConstants.APPCONTEXT_CHART_RESOLUTION,
				ParameterAccessor.getDpi( request ) );

		// Push user-defined application context
		ParameterAccessor.pushAppContext( context, request );
		renderTask.setAppContext( context );

		// Render option
		RenderOption renderOption = null;
		if ( format == null )
			format = ParameterAccessor.getFormat( request );

		if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( format )
				|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT
						.equalsIgnoreCase( format ) )
		{
			renderOption = createPDFRenderOption( servletPath, request,
					ParameterAccessor.isDesigner( request ) );
		}
		else
		{
			renderOption = createHTMLRenderOption( svgFlag, servletPath,
					request );
		}

		renderOption.setOutputFormat( format );
		renderOption.setOutputStream( out );
		ViewerHTMLActionHandler handler = null;
		if ( IBirtConstants.PDF_RENDER_FORMAT.equalsIgnoreCase( format )
				|| IBirtConstants.POSTSCRIPT_RENDER_FORMAT
						.equalsIgnoreCase( format ) )
		{
			handler = new ViewerHTMLActionHandler( reportDocument, -1, locale,
					false, rtl, masterPage, format );
		}
		else
		{
			boolean isEmbeddable = false;
			if ( IBirtConstants.SERVLET_PATH_FRAMESET
					.equalsIgnoreCase( servletPath )
					|| IBirtConstants.SERVLET_PATH_FRAMESET
							.equalsIgnoreCase( servletPath ) )
				isEmbeddable = true;
			if ( renderOption instanceof IHTMLRenderOption )
				( (IHTMLRenderOption) renderOption )
						.setEmbeddable( isEmbeddable );
			renderOption.setOption( IHTMLRenderOption.HTML_RTL_FLAG,
					new Boolean( rtl ) );
			renderOption.setOption( IHTMLRenderOption.INSTANCE_ID_LIST,
					activeIds );
			renderOption.setOption( IHTMLRenderOption.MASTER_PAGE_CONTENT,
					new Boolean( masterPage ) );
			handler = new ViewerHTMLActionHandler( reportDocument, -1, locale,
					isEmbeddable, rtl, masterPage, format );
		}
		String resourceFolder = ParameterAccessor.getParameter( request,
				ParameterAccessor.PARAM_RESOURCE_FOLDER );
		handler.setResourceFolder( resourceFolder );
		renderOption.setActionHandler( handler );

		renderTask.setRenderOption( renderOption );
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

			renderTask.render( );
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.renderReport( )" ) ); //$NON-NLS-1$
			throw fault;
		}
		finally
		{
			// Remove task from http session
			BirtUtility.removeTask( request );
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
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault.setFaultCode( new QName(
					"ReportEngineService.getResultSets( )" ) ); //$NON-NLS-1$			
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
	 * @param encoding
	 * @throws RemoteException
	 */
	public void extractData( IReportDocument document, String resultSetName,
			Collection columns, Locale locale, OutputStream outputStream,
			String encoding ) throws RemoteException
	{
		extractData( document, resultSetName, columns, locale, outputStream,
				encoding, ParameterAccessor.DEFAULT_SEP );
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
	 * @param encoding
	 * @param sep
	 * @throws RemoteException
	 */
	public void extractData( IReportDocument document, String resultSetName,
			Collection columns, Locale locale, OutputStream outputStream,
			String encoding, char sep ) throws RemoteException
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
						buf.append( sep );
						buf.append( columnNames[i] );
					}

					buf.append( '\n' );
					if ( encoding != null && encoding.trim( ).length( ) > 0 )
					{
						outputStream.write( buf.toString( ).getBytes(
								encoding.trim( ) ) );
					}
					else
					{
						outputStream.write( buf.toString( ).getBytes( ) );
					}
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
							buf.append( sep );

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
						if ( encoding != null && encoding.trim( ).length( ) > 0 )
						{
							outputStream.write( buf.toString( ).getBytes(
									encoding.trim( ) ) );
						}
						else
						{
							outputStream.write( buf.toString( ).getBytes( ) );
						}
						buf.delete( 0, buf.length( ) );
					}
				}
			}
		}
		catch ( Exception e )
		{
			AxisFault fault = new AxisFault( e.getLocalizedMessage( ), e
					.getCause( ) );
			fault
					.setFaultCode( new QName(
							"ReportEngineService.extractData( )" ) ); //$NON-NLS-1$
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
	 * @deprecated
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