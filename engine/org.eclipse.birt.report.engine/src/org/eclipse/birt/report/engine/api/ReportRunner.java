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

package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;

/**
 * Defines a standalone reporting application that uses
 * <code>StandaloneReportEngine</code> class. This application allows running
 * a report to output in one or multiple formats.
 * <p>
 * Report parameters are handled as command line parameters. Currently, only
 * scalar parameters are handled.
 * 
 * @version $Revision: 1.26 $ $Date: 2006/08/08 09:59:09 $
 */
public class ReportRunner
{

	// The static logger
	static protected Logger logger = Logger.getLogger( ReportRunner.class
			.getName( ) );

	/**
	 * the input paramters
	 */
	protected String[] args; // The command line arguments
	/**
	 * the source input ,can be designName or reportArchive name
	 */
	protected String source; // Name of the design file
	/**
	 * The target file name
	 */
	protected String targetFile = null;
	/**
	 * execution mode, one of the Run, RunAndRender and Render. the default mode
	 * is RunAndRender
	 */
	protected String mode = "RunAndRender"; // the execution mode, one of the
	/**
	 * the output locale, used in Run and RunAndRender modes.
	 */
	protected String locale = "en"; // Application running locale //$NON-NLS-1$

	/**
	 * the output format, used in Render and RunAndRender.
	 */
	protected String format = "html"; // The output format, defaults to HTML
	/**
	 * used to decorate the HTML output, used in Render and RunAndRender mode.
	 */
	protected String htmlType = "HTML"; // The type of html
	/**
	 * the output encoding, used in html format in Render and RunAndRender
	 * modes.
	 */
	protected String encoding = "utf-8"; // the targe encoding
	/**
	 * paramters used to execute the report, used in Run and RunAndRender modes.
	 */
	protected HashMap params = new HashMap( ); // the input params
	/**
	 * output page number, used in Render mode.
	 */
	protected long pageNumber = -1;

	/**
	 * engine used to execute the tasks.
	 */
	private IReportEngine engine;

	/**
	 * Constructor of ReportRunner
	 * 
	 * @param args -
	 *            application arguments
	 */
	public ReportRunner( String[] args )
	{
		this.args = (String[]) args.clone( );
	}

	/**
	 * Main function.
	 * 
	 * @param args -
	 *            application argumetns.
	 */
	public static void main( String[] args )
	{
		int result = new ReportRunner( args ).execute( );
		System.exit( result );
	}

	/**
	 * Check if the arguments are valid. If yes, continue to execuate the
	 * report. If no, simply return.
	 */
	public int execute( )
	{
		if ( args.length == 0 )
		{
			printUsage( );
			return 0;
		}
		try
		{
			// startup the platform
			if ( engine == null )
			{
				EngineConfig config = new EngineConfig( );
				Platform.startup( config );
				IReportEngineFactory factory = (IReportEngineFactory) Platform
						.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
				engine = factory.createReportEngine( config );

				// JRE default level is INFO, which may reveal too much internal
				// logging
				// information.
				engine.changeLogLevel( Level.WARNING );
			}

			// Process command line arguments
			parseOptions( );
			if ( "Run".equalsIgnoreCase( mode ) )
			{
				return runReport( );
			}
			else if ( "Render".equalsIgnoreCase( mode ) )
			{
				return renderReport( );
			}
			else
			{
				return runAndRenderReport( );
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "exception in parsing the paramters", ex );
			return -1;
		}
		finally
		{
			Platform.shutdown( );
		}

	}

	/**
	 * Execute the report design which includes: <br>
	 * 1. Read the input design and create the task. <br>
	 * 2. Set report render options (including format, locale, output file name
	 * etc). <br>
	 * 3. Run the task.
	 */
	protected int runAndRenderReport( )
	{
		try
		{
			// parse the source to get the report runnable
			IReportRunnable runnable = engine.openReportDesign( source );
			// validate the input paramter values
			HashMap inputValues = evaluateParameterValues( runnable );
			//
			IRunAndRenderTask task = engine.createRunAndRenderTask( runnable );
			task.setParameterValues( inputValues );

			// set report render options
			IRenderOption options;
			if ( format.equalsIgnoreCase( "html" ) )
			{
				options = new HTMLRenderOption( );
				if ( "ReportletNoCSS".equals( htmlType ) )
					( (HTMLRenderOption) options ).setEmbeddable( true );
				((HTMLRenderOption)options).setHtmlPagination( true );
			}
			else
			{
				options = new RenderOptionBase();
			}

			options.setOutputFormat( format );

			// setup the output file
			options.setOutputFileName( targetFile );

			// setup the output encoding
			options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
					encoding );

			// set the render options
			task.setRenderOption( options );

			// setup the application context
			if ( format.equalsIgnoreCase( "html" ) )
			{
				HTMLRenderContext renderContext = new HTMLRenderContext( );
				renderContext.setImageDirectory( "image" ); //$NON-NLS-1$

				HashMap appContext = new HashMap( );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );
				task.setAppContext( appContext );
			}

			// setup the locale
			task.setLocale( getLocale( locale ) );

			task.run( );
			return 0;
		}
		catch ( EngineException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			return -1;
		}
	}

	/**
	 * running the report to create the report document
	 */
	protected int runReport( )
	{
		try
		{
			// parse the source to get the report runnable
			IReportRunnable runnable = engine.openReportDesign( source );
			// create the report task
			IRunTask task = engine.createRunTask( runnable );

			// set the paramter values
			HashMap inputValues = evaluateParameterValues( runnable );
			task.setParameterValues( inputValues );

			// set the application context
			task.setAppContext( new HashMap( ) );

			// run the task to create the report document
			task.run( targetFile );

			// close the task.
			task.close( );

			return 0;
		}
		catch ( org.eclipse.birt.report.engine.api.EngineException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			return -1;
		}

	}

	/**
	 * render the report.
	 */
	protected int renderReport( )
	{
		try
		{
			// use the archive to open the report document
			IReportDocument document = engine.openReportDocument( source );

			// create the render task
			IRenderTask task = engine.createRenderTask( document );

			// set report render options
			IRenderOption options;
			if ( format.equalsIgnoreCase( "html" ) )
			{
				options = new HTMLRenderOption( );
				if ( "ReportletNoCSS".equals( htmlType ) )
					( (HTMLRenderOption) options ).setEmbeddable( true );
			}
			else
			{
				options = new RenderOptionBase();
			}

			// set the output format
			options.setOutputFormat( format );

			// setup the output encoding
			options.getOutputSetting( ).put( HTMLRenderOption.URL_ENCODING,
					encoding );

			// set the render options
			task.setRenderOption( options );

			// setup the application context
			if ( format.equalsIgnoreCase( "html" ) )
			{
				HTMLRenderContext renderContext = new HTMLRenderContext( );
				renderContext.setImageDirectory( "image" ); //$NON-NLS-1$

				HashMap appContext = new HashMap( );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						renderContext );
				task.setAppContext( appContext );
			}

			// setup the locale
			task.setLocale( getLocale( locale ) );

			// setup the output file
			if ( pageNumber <= 0 )
			{
				int extPos = targetFile.lastIndexOf( '.' );
				assert extPos != -1;
				String pathName = targetFile.substring( 0, extPos );
				String extName = targetFile.substring( extPos );
				for ( long i = 1; i <= document.getPageCount( ); i++ )
				{
					String fileName = pathName + '_' + ( i ) + extName;
					options.setOutputFileName( fileName );
					task.render( i );
				}
			}
			else
			{
				options.setOutputFileName( targetFile );
				task.render( pageNumber );
			}
			task.close( );
			return 0;
		}
		catch ( org.eclipse.birt.report.engine.api.EngineException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
			return -1;
		}
	}

	/**
	 * print out the command line usage.
	 * 
	 */
	protected void printUsage( )
	{
		System.out.println( "org.eclipse.birt.report.engine.impl.ReportRunner" ); //$NON-NLS-1$
		System.out.println( "" ); //$NON-NLS-1$

		System.out
				.println( "--mode/-m [ run | render | runrender] the default is runrender" );
		System.out.println( " for runrender mode:" );
		System.out.println( "\t we should add it in the end<design file>" ); //$NON-NLS-1$
		System.out.println( "\t --format/-f [ HTML | PDF ]" ); //$NON-NLS-1$
		System.out.println( "\t --output/-o <target file>" ); //$NON-NLS-1$
		System.out.println( "\t --htmlType/-t < HTML | ReportletNoCSS >" ); //$NON-NLS-1$
		System.out.println( "\t --locale /-l<locale>" ); //$NON-NLS-1$
		System.out.println( "\t --parameter/-p <parameterName=parameterValue>" ); //$NON-NLS-1$
		System.out.println( "\t --file/-F <parameter file>" ); //$NON-NLS-1$
		System.out.println( "\t --encoding/-e <target encoding>" ); //$NON-NLS-1$

		System.out.println( "\nLocale: default is english\n" ); //$NON-NLS-1$
		System.out
				.println( "\nparameters in command line will overide parameters in parameter file" ); //$NON-NLS-1$
		System.out
				.println( "\nparameter name can't include characters such as ' ', '=', ':' " ); //$NON-NLS-1$

		System.out.println( "For RUN mode:" );
		System.out.println( "\t we should add it in the end<design file>" ); //$NON-NLS-1$
		System.out.println( "\t --output/-o <target file>" ); //$NON-NLS-1$
		System.out.println( "\t --locale /-l<locale>" ); //$NON-NLS-1$
		System.out.println( "\t --parameter/-p <parameterName=parameterValue>" ); //$NON-NLS-1$
		System.out.println( "\t --file/-F <parameter file>" ); //$NON-NLS-1$

		System.out.println( "\nLocale: default is english\n" ); //$NON-NLS-1$
		System.out
				.println( "\nparameters in command line will overide parameters in parameter file" ); //$NON-NLS-1$
		System.out
				.println( "\nparameter name can't include characters such as ' ', '=', ':' " ); //$NON-NLS-1$

		System.out.println( "For RENDER mode:" );
		System.out.println( "\t we should add it in the end<design file>" ); //$NON-NLS-1$
		System.out.println( "\t --output/-o <target file>" ); //$NON-NLS-1$
		System.out.println( "\\t --page/-p <pageNumber>" );
		System.out.println( "\t --locale /-l<locale>" ); //$NON-NLS-1$

		System.out.println( "\nLocale: default is english\n" ); //$NON-NLS-1$

	}

	/**
	 * Private function to convert a locale name string to a locale object
	 * 
	 * @param locale -
	 *            locale name string
	 * @return A locale object
	 */
	private Locale getLocale( String locale )
	{
		int index = locale.indexOf( '_' );
		if ( index != -1 )
		{
			// e.g, zh_CN (language_country)
			String language = locale.substring( 0, index );
			String country = locale.substring( index + 1 );
			return new Locale( language, country );
		}

		// e.g, en (language)
		return new Locale( locale );
	}

	CommandLine results;

	protected void parseRunOptions( ) throws Exception
	{
		if ( results.hasOption( 'o' ) )
		{
			targetFile = results.getOptionValue( 'o' );
		}

		parseParameterOptions( );

	}

	protected void parseRenderOptions( ) throws Exception
	{
		assert ( mode.equalsIgnoreCase( "Render" ) );

		if ( results.hasOption( 'f' ) )
		{
			format = results.getOptionValue( 'f' );
		}

		if ( results.hasOption( 't' ) )
		{
			htmlType = results.getOptionValue( 't' );
		}

		if ( results.hasOption( 'o' ) )
		{
			targetFile = results.getOptionValue( 'o' );
		}

		if ( results.hasOption( 'l' ) )
		{
			locale = results.getOptionValue( 'l' );
		}
		if ( results.hasOption( 'e' ) )
		{
			encoding = results.getOptionValue( 'e' );
		}
		if ( results.hasOption( 'p' ) )
		{
			pageNumber = Long.parseLong( results.getOptionValue( 'p' ) );
		}
		parseParameterOptions( );

	}

	protected void parseRunAndRenderOptions( ) throws Exception
	{
		assert ( mode.equalsIgnoreCase( "RunAndRender" ) );

		if ( results.hasOption( 'f' ) )
		{
			format = results.getOptionValue( 'f' );
		}

		if ( results.hasOption( 't' ) )
		{
			htmlType = results.getOptionValue( 't' );
		}

		if ( results.hasOption( 'o' ) )
		{
			targetFile = results.getOptionValue( 'o' );
		}

		if ( results.hasOption( 'l' ) )
		{
			locale = results.getOptionValue( 'l' );
		}
		if ( results.hasOption( 'e' ) )
		{
			encoding = results.getOptionValue( 'e' );
		}
		parseParameterOptions( );
	}

	/**
	 * parse the arguments.
	 * 
	 * -html html-file-name -pdf pdf-file-name -fo fo-file-name -l locale-name
	 * -p page-number design-file-name
	 * 
	 * @param args -
	 *            arguments
	 */
	protected void parseOptions( )
	{

		source = args[args.length - 1];
		args[args.length - 1] = ""; //$NON-NLS-1$

		try
		{

			Options option = new Options( );// Collection of all the options
			/*
			 * Add these basic argument into instance option
			 */
			option.addOption( "m", "mode", true, "RunAndRender" );
			option.addOption( "o", "output", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "f", "format", true, "html" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "t", "htmlType", true, "" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "l", "locale", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "e", "encoding", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "p", "parameter", true, "" );
			option.addOption( "p", "page", true, "" );
			option.addOption( "F", "file", true, "parameter file" );

			results = new BasicParser( ).parse( option, args, true );
			if ( results.hasOption( 'm' ) )
			{
				mode = results.getOptionValue( 'm' );
			}

			if ( "Run".equalsIgnoreCase( mode ) )
			{
				parseRunOptions( );
			}
			else if ( "Render".equalsIgnoreCase( mode ) )
			{
				parseRenderOptions( );
			}
			else
			{
				parseRunAndRenderOptions( );
			}
			checkTargetFileName( );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, ex.getMessage( ), ex );
			printUsage( );
		}
	}

	/**
	 * read paramters from the param and add it into the params.
	 * 
	 * @param param
	 *            parameter string line. use '=' to separate the name and value.
	 * @param params
	 *            maps contains all the paramter name and value.
	 */
	protected void readParamString( String param, HashMap params )
	{
		if ( param == null || param.length( ) < 2 )
		{
			return;
		}
		int index = param.indexOf( "=" );
		if ( index < 1 )
		{
			return;
		}
		String name = param.substring( 0, index ).trim( );
		String value = param.substring( index + 1 ).trim( );
		if ( value.startsWith( "\"" ) && value.endsWith( "\"" )
				&& value.length( ) > 2 )
		{
			value = value.substring( 1, value.length( ) - 1 );
		}
		params.put( name, value );
	}

	/**
	 * paraser the paramter command line inputs.
	 * 
	 * @return the HashMap contains all the paramter name and values.
	 */
	protected void parseParameterOptions( )
	{
		if ( results.hasOption( 'F' ) )
		{
			String fileName = results.getOptionValue( 'F' );
			readParamFile( fileName, params );
		}
		if ( results.hasOption( 'p' ) )
		{
			String[] stringParams = results.getOptionValues( 'p' );

			if ( stringParams != null )
			{
				for ( int i = 0; i < stringParams.length; i++ )
				{
					readParamString( stringParams[i], params );
				}
			}
		}
	}

	protected void readParamFile( String fileName, HashMap params )
	{
		File file = new File( fileName );
		Properties ps = new Properties( );
		try
		{
			ps.load( new FileInputStream( file ) );
		}
		catch ( FileNotFoundException e )
		{
			logger.log( Level.SEVERE,
					"file: " + file.getAbsolutePath( ) + " not exists!" ); //$NON-NLS-1$
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE,
					"Can't open file: " + file.getAbsolutePath( ) ); //$NON-NLS-1
		}
		params.putAll( ps );
	}

	private HashMap evaluateParameterValues( IReportRunnable runnable )
	{

		HashMap inputValues = new HashMap( );
		IGetParameterDefinitionTask task = engine
				.createGetParameterDefinitionTask( runnable );
		Collection paramDefns = task.getParameterDefns( false );
		Iterator iter = paramDefns.iterator( );
		while ( iter.hasNext( ) )
		{
			// now only support scalar parameter
			IParameterDefnBase pBase = (IParameterDefnBase) iter.next( );
			if ( pBase instanceof IScalarParameterDefn )
			{

				IScalarParameterDefn paramDefn = (IScalarParameterDefn) pBase;

				String paramName = paramDefn.getName( );
				String inputValue = (String) params.get( paramName );
				int paramType = paramDefn.getDataType( );
				try
				{
					Object paramValue = stringToObject( paramType, inputValue );
					if ( paramValue != null )
					{
						inputValues.put( paramName, paramValue );
					}
				}
				catch ( BirtException ex )
				{
					logger.log( Level.SEVERE, "the value of parameter "
							+ paramName + " is invalid", ex );
				}
			}
		}
		return inputValues;
	}

	/**
	 * @param p
	 *            the scalar parameter
	 * @param expr
	 *            the default value expression
	 */
	protected Object stringToObject( int type, String value )
			throws BirtException
	{
		if ( value == null )
		{
			return null;
		}
		switch ( type )
		{
			case IScalarParameterDefn.TYPE_BOOLEAN :
				return DataTypeUtil.toBoolean( value );

			case IScalarParameterDefn.TYPE_DATE_TIME :
				return DataTypeUtil.toDate( value );

			case IScalarParameterDefn.TYPE_DECIMAL :
				return DataTypeUtil.toBigDecimal( value );

			case IScalarParameterDefn.TYPE_FLOAT :
				return DataTypeUtil.toDouble( value );

			case IScalarParameterDefn.TYPE_STRING :
				return DataTypeUtil.toString( value );
			
			case IScalarParameterDefn.TYPE_INTEGER :
				return DataTypeUtil.toInteger( value );
		}
		return null;

	}

	/**
	 * If -o (targetFile) is not specified, assume same directory as inputfile,
	 * and inputfile.*** as output file name (where *** is the output format.
	 * 
	 * If -o specifies a directory, assume the file name is the same as
	 * inputfile.***.
	 * 
	 * If -o specifies a file, it has a path part and a filename part. Take the
	 * path as the directory to store the file and other resources (for example
	 * image).
	 */
	protected void checkTargetFileName( )
	{
		String fileExt = "." + format;
		if ( "Run".equalsIgnoreCase( mode ) )
		{
			fileExt = ".rptdocument";
		}
		File designFile = new File( new File( source ).getAbsolutePath( ) );

		String designFileName = designFile.getName( );

		int n = designFileName.lastIndexOf( '.' );
		if ( n != -1 )
		{
			designFileName = designFileName.substring( 0, n );
		}

		if ( targetFile == null )
		{
			targetFile = designFile.getParent( ) + File.separatorChar
					+ designFileName + fileExt;
		}
		else if ( !targetFile.toLowerCase( ).endsWith( fileExt.toLowerCase( ) ) )
		{
			targetFile = targetFile + File.separatorChar + designFileName
					+ fileExt;
			File file = new File( targetFile );
			if ( !file.getParentFile( ).exists( ) )
			{
				file.getParentFile( ).mkdir( );
			}
		}
	}
}
