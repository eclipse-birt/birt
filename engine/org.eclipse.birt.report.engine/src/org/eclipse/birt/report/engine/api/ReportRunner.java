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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.FORenderOption;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.ReportEngine;

/**
 * Defines a standalone reporting application that uses <code>StandaloneReportEngine</code> 
 * class. This application allows running a report to output in one or multiple formats.
 * <p>
 * Report parameters are handled as command line parameters. Currently, only scalar parameters 
 * are handled.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/05/10 00:07:13 $
 */
public class ReportRunner
{
	//	The static logger
	static protected Logger logger = Logger.getLogger( ReportRunner.class.getName() );

	//	Handle commandline arguments
	protected ReportRunnerCommandlineHandler commandlineHandler;
	protected String locale = "en"; // Application running locale //$NON-NLS-1$
	protected String[] args; // The command line arguments
	protected String designName; // Name of the design file
	protected String format = "html"; // The output format, defaults to HTML
	protected String htmlType = "HTML"; // The type of html
	protected String targetFile = null; // The target file name
	protected int pageNum;//Name of the number of page
	protected Options option = new Options( );//Collection of all the options

	// Create a standalone engine
	ReportEngine engine;

	/**
	 * The list of pages to be processed for HTMP, PDF and other formats. We
	 * currently do not support page list.
	 */
	protected String pageList = ""; //$NON-NLS-1$

	/**
	 * Constructor of ReportRunner
	 * @param args - application arguments
	 */
	ReportRunner( String[] args )
	{
		this.args = args;
		commandlineHandler = new ReportRunnerCommandlineHandler( );
		engine = new ReportEngine(new EngineConfig());
		
		// JRE default level is INFO, which may reveal too much internal logging information.
		engine.changeLogLevel(Level.WARNING);	
	}

	/**
	 * Main function.
	 * @param args - application argumetns.
	 */
	public static void main( String[] args )
	{
		new ReportRunner( args ).execute( );
	}

	/**
	 * Check if the arguments are valid. If yes, continue to execuate the report. If no, simply return.
	 */
	protected void execute( )
	{
		if ( args.length==0 )
		{
			printUsage();
			return;
		}
		//Process command line arguments
		commandlineHandler.parseOptions( );
		if ( !validateAndPrepareArguments( ) )
			return;

		// Generate the output content
		executeReport(  );
	}

	/**
	 * Execute the report design which includes: <br>
	 * 1. Read the input design and create the task. <br>
	 * 2. Set report render options (including format, locale, output file name etc). <br>
	 * 3. Run the task.
	 */
	protected void executeReport()
	{
		try {
			IReportRunnable design = engine.openReportDesign( designName );
			IRunAndRenderTask task = engine.createRunAndRenderTask( design );
			
			// set report render options
			IRenderOption options;
			if(format.equalsIgnoreCase("pdf")){
				options = new FORenderOption();   

			}
			else if(format.equalsIgnoreCase("fo")){
				options = new FORenderOption();
			}
			else{
				options = new HTMLRenderOption();
			}			
			
			if(format.equalsIgnoreCase("html") ){
				HTMLRenderContext renderContext = new HTMLRenderContext();
				renderContext.setImageDirectory("image"); //$NON-NLS-1$
				task.setContext(renderContext);
			}
			
			if(locale != null) 
			{
				Locale loc = getLocale( locale );
				options.setLocale(loc);
				task.setLocale(loc);
			}
			
			if(targetFile == null)
			{
                // target file is same as design file
				targetFile = new String(designName.substring(0, designName.indexOf('.'))
							+"."+((format == null) ? "html" : format));
			}
			options.setOutputFileName(targetFile);
			
			options.setOutputFormat((format == null) ? "html" : format);
			
			if(htmlType == "ReportletNoCSS" )
				((HTMLRenderOption )options).setEmbeddable(true);
			
			task.setRenderOption(options);
			
			task.run();
		} catch (org.eclipse.birt.report.engine.api.EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*protected void executeReport( String targetFile, String format )
	{
		// Obtain a report handle
		IReportHandle reportHandle = engine.getReportHandle( designName, getLocale( locale ));
		if ( reportHandle == null )
		{
			System.out.println( "The report design file contains errors. Please fix it and try again. " ); //$NON-NLS-1$
			return;
		}

		// Parse parameters. Note that we do not care about parameter groups,
		// so we pass in false in the first function call.
		Collection parameters = reportHandle.getParameters( false );
		HashMap paramMap = commandlineHandler
				.parseParameters( (ArrayList) parameters );
		if ( paramMap == null )
			return;

		// Set parameters
		reportHandle.setParameterValues( paramMap );
		if ( reportHandle.validateParameters( ) )
		{
			IViewOptions viewOptions = reportHandle.getViewOptions( format );
			viewOptions.setOption( IViewOptions.FORMAT, format );
			viewOptions.setOption( IViewHTMLOptions.HTML_TYPE, htmlType );
			try
			{
				reportHandle.viewReport( targetFile, viewOptions );
			}
			catch ( EngineException e )
			{
				System.out
						.println( "There are errors generating the report in HTML format." ); //$NON-NLS-1$
				e.printStackTrace( );
			}
		}
		else
		{
			System.out.println( "There exists parameter error." ); //$NON-NLS-1$
		}
		reportHandle.release( true );

	}*/

	/**
	 * @return validate if design name is legal. Prepare various file names if
	 *         1. PDF file name is specified but the FO file name is not. 2. No
	 *         file name is specified, which means all 3 types of files are
	 *         requested
	 */
	private boolean validateAndPrepareArguments( )
	{
		if ( designName == null )
		{
			printUsage( );
			return false;
		}

		/*format = normalizeFormat( );
		if ( format == null )
		{
			logger.log( Level.SEVERE, "[ReportRunner] Invalid output format." ); //$NON-NLS-1$
			return false;
		}
		
		htmlType = normalizeHTMLType( );
		if ( htmlType == null )
		{
			logger.log( Level.SEVERE, "[ReportRunner] Invalid html type." ); //$NON-NLS-1$
			return false;
		}
*/
		checkTargetFileName();

		return true;
	}
	
	/**
	 * @return normalized format string if format is one of the three formats; null otherwise
	 */
	/*private String normalizeFormat( )
	{
		if (IViewOptions.FORMAT_HTML.equalsIgnoreCase( format ) )
		{
			return IViewOptions.FORMAT_HTML;
		}
		
		if (IViewOptions.FORMAT_FO.equalsIgnoreCase( format ) )
		{
			return IViewOptions.FORMAT_FO;
		}

		if (IViewOptions.FORMAT_PDF.equalsIgnoreCase( format ) )
		{
			return IViewOptions.FORMAT_PDF;
		}
		
		return null;
	}*/

	/**
	 * @return normalized html type string
	 */
	/*private String normalizeHTMLType( )
	{
		if( IViewHTMLOptions.HTML.equalsIgnoreCase( htmlType ) )
		{
			return IViewHTMLOptions.HTML;
		}
		
		if( IViewHTMLOptions.HTML_NOCSS.equalsIgnoreCase( htmlType ) )
		{
			return IViewHTMLOptions.HTML_NOCSS;
		}
		
		return null;
	}*/
	
	/**
	 * print out the command line usage.
	 *  
	 */
	protected void printUsage( )
	{
		System.out.println( "org.eclipse.birt.report.engine.impl.ReportRunner" ); //$NON-NLS-1$
		System.out.println( "" ); //$NON-NLS-1$
		System.out.println( "\t we should add it in the end<design file>" ); //$NON-NLS-1$
		System.out.println( "\t --format/-f [ HTML | FO | PDF ]" ); //$NON-NLS-1$
		System.out.println( "\t --output/-o <target file>" ); //$NON-NLS-1$
		System.out.println( "\t --htmltype/-t < HTML | ReportletNoCSS >" ); //$NON-NLS-1$
		System.out.println( "\t --locale /-l<locale>" ); //$NON-NLS-1$
		//TODO: output all pages
		System.out.println( "\t --page/-g <page number>" ); //$NON-NLS-1$
		System.out.println( "\nLocale: default is english\n" ); //$NON-NLS-1$
	}

	/**
	 * Private function to convert a locale name string to a locale object 
	 * @param locale - locale name string
	 * @return A locale object
	 */
	private Locale getLocale( String locale )
	{
		int index = locale.indexOf( '_' );
		if ( index != -1 )
		{
			//e.g, zh_CN (language_country)
			String language = locale.substring( 0, index );
			String country = locale.substring( index + 1 );
			return new Locale( language, country );
		}

		//e.g, en (language)
		return new Locale( locale );
	}

	protected class ReportRunnerCommandlineHandler
	{
		/**
		 * parse the arguments.
		 * 
		 * -html html-file-name -pdf pdf-file-name -fo fo-file-name -l
		 * locale-name -p page-number design-file-name
		 * 
		 * @param args - arguments
		 */
		protected void parseOptions( )
		{
			/*
			 * Add these basic argument into instance option
			 */
			option.addOption( "f", "format", true, "" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "t", "type", true, "" ); //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "o", "output", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "l", "locale", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			option.addOption( "g", "page", true, "" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

			// Parse
			BasicParser parser = new BasicParser( );
			try
			{
				designName = args[args.length - 1];
				args[args.length - 1] = ""; //$NON-NLS-1$
				CommandLine result = parser.parse( option, args, true );

				if ( result.hasOption( 'f' ) )
				{
					format = result.getOptionValue( 'f' );
				}

				if ( result.hasOption( 't' ) )
				{
					htmlType = result.getOptionValue( 't' );
				}

				if ( result.hasOption( 'o' ) )
				{
					targetFile = result.getOptionValue( 'o' );
				}

				pageList = result.getOptionValue( "page" ); //$NON-NLS-1$

				if ( result.hasOption( 'l' ) )
				{
					locale = result.getOptionValue( 'l' );
				}
			}
			catch ( org.apache.commons.cli.ParseException pe )
			{
			    logger.log( Level.SEVERE, pe.getMessage( ), pe );
				printUsage( );
			}
		}

		protected HashMap parseParameters( ArrayList paramDefns )
		{

			HashMap params = new HashMap( );
			/*BasicParser parser = new BasicParser( );

			
			 * Add parameters into instance option
			 

			for ( int i = 0; i < paramDefns.size( ); i++ )
			{
				option.addOption( new Integer( i ).toString( ), "param" + i, //$NON-NLS-1$
						true, "" ); //$NON-NLS-1$
			}
			try
			{
				CommandLine results = parser.parse( option, args, false );
				//add the parameter values into the param hash map.
				for ( int i = 0; i < paramDefns.size( ); i++ )
				{
					IScalarParameterDefn paramHandle = (IScalarParameterDefn) paramDefns
							.get( i );
					String paramName = paramHandle.getName( );
					assert paramName != null;
					Object result = null;
					if ( results.hasOption( new Integer( i ).toString( ) ) )
					{
						result = results.getOptionValue( "param" + i ); //$NON-NLS-1$
					}
					if ( result == null )
					{
						result = paramHandle.getDefaultValue( );
						if ( ( result != null ) && ( result.equals( "" ) ) ) //$NON-NLS-1$
						{
							result = null;
						}
					}
					if ( result != null )
					{
						int type = paramHandle.getType( );
						if ( type == IParameterDefn.TYPE_BOOLEAN )
						{
							result = new Boolean( (String) result );
						}
						params.put( paramName, result );
					}
				}
			}
			catch ( org.apache.commons.cli.ParseException pe )
			{
			    logger.log( Level.SEVERE, "CLI parseException " + pe.getMessage( ), pe ); //$NON-NLS-1$
			}*/
			return params;
		}
	}
	
	/**
	 * If -o (targetFile) is not specified, assume same directory as
	 * inputfile, and inputfile.*** as output file name (where ***
	 * is the output format.
	 * 
	 * If -o specifies a directory, assume the file name is the same as
	 * inputfile.***.
	 * 
	 * If -o specifies a file, it has a path part and a filename part.
	 * Take the path as the directory to store the file and other resources
	 * (for example image). 
	 */
	protected void checkTargetFileName ( )
	{
		String fileExt = '.' + format;
		File designFile = new File( designName );
		String designFileName = designFile.getName( );
		int n = designFileName.lastIndexOf( '.' );
		if ( n != -1 )
		{
			designFileName = designFileName.substring( 0, n );
		}

		if ( targetFile == null )
		{
			targetFile = designFile.getParent() + File.separatorChar + designFileName + fileExt;
		}
		else if ( !targetFile.toLowerCase().endsWith( fileExt ) ) 
		{
			targetFile = targetFile + File.separatorChar + designFileName + fileExt;
		}
	}
}

