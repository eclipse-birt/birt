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
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.report.engine.api.impl.EngineFileContext;

/**
 * Defines a standalone reporting application that uses <code>StandaloneReportEngine</code> 
 * class. This application allows running a report to output in one or multiple formats.
 * <p>
 * Report parameters are handled as command line parameters. Currently, only scalar parameters 
 * are handled.
 * 
 * @version $Revision: 1.5 $ $Date: 2005/05/20 19:17:55 $
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
	protected Options option = new Options( );//Collection of all the options
	protected String encoding = null;

	// Create a standalone engine
	ReportEngine engine;

	
	/**
	 * Constructor of ReportRunner
	 * @param args - application arguments
	 */
	ReportRunner( String[] args )
	{
		this.args = args;
		commandlineHandler = new ReportRunnerCommandlineHandler( );
		EngineConfig config = new EngineConfig();
		IPlatformContext context = new EngineFileContext();
		config.setEngineContext( context );
		engine = new ReportEngine( config );
		
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
		IReportRunnable design;
		try
		{
			design = engine.openReportDesign( designName );
		}
		catch (EngineException e)
		{
			logger.log( Level.SEVERE, "There are errors generating the report in design file.", e); //$NON-NLS-1$
			return;
		}
		if ( !validateAndPrepareArguments( ) )
			return;
		HashMap params = commandlineHandler.parseParameters(design);
		// Generate the output content
		executeReport( design , params);
	}

	
	/**
	 * Execute the report design which includes: <br>
	 * 1. Read the input design and create the task. <br>
	 * 2. Set report render options (including format, locale, output file name etc). <br>
	 * 3. Run the task.
	 */
	protected void executeReport(IReportRunnable design, HashMap params)
	{
		try {
			
			IRunAndRenderTask task = engine.createRunAndRenderTask( design );
			task.setParameterValues(params);
			
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
			
			options.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, encoding);
			
			task.setRenderOption(options);
			
			task.run();
		} catch (org.eclipse.birt.report.engine.api.EngineException e)
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
	}

	

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
		checkTargetFileName();
		return true;
	}
	
	
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
		System.out.println( "\t --parameter/-p <parameterName=parameterValue>" ); //$NON-NLS-1$
		System.out.println( "\t --file/-F <parameter file>" ); //$NON-NLS-1$
		System.out.println( "\t --encoding/-e <target encoding>" ); //$NON-NLS-1$
		
		System.out.println( "\nLocale: default is english\n" ); //$NON-NLS-1$
		System.out.println( "\nparameters in command line will overide parameters in parameter file" ); //$NON-NLS-1$
		System.out.println( "\nparameter name can't include characters such as ' ', '=', ':' " ); //$NON-NLS-1$
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
			option.addOption( "e", "encoding", true, ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			
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

				if ( result.hasOption( 'l' ) )
				{
					locale = result.getOptionValue( 'l' );
				}
				if (result.hasOption( 'e' ) )
				{
					encoding = result.getOptionValue('e');
				}
			}
			catch ( org.apache.commons.cli.ParseException pe )
			{
			    logger.log( Level.SEVERE, pe.getMessage( ), pe );
				printUsage( );
			}
		}

		protected HashMap parseParameters(IReportRunnable design  )
		{
			HashMap params = new HashMap();
			
			option.addOption("p", "parameter", true, "parameter");
			option.addOption("F", "file", true, "parameter file");
			BasicParser parser = new BasicParser( );
			CommandLine results = null; 
			try
			{
				results = parser.parse( option, args, false );
			}
			catch ( org.apache.commons.cli.ParseException pe )
			{
			    logger.log( Level.SEVERE, "CLI parseException " + pe.getMessage( ), pe ); //$NON-NLS-1$
			}
			if(results==null)
			{
				return params;
			}
			HashMap strParams = new HashMap();
			if(results.hasOption('F'))
			{
				String fileName = results.getOptionValue('F');
				File file = new File(fileName);
				Properties ps = new Properties();
				try
				{
					ps.load(new FileInputStream(file));
				}
				catch (FileNotFoundException e)
				{
					logger.log( Level.SEVERE, "file: " + file.getAbsolutePath()+" not exists!" ); //$NON-NLS-1$
				}
				catch (IOException e)
				{
					logger.log( Level.SEVERE, "Can't open file: " + file.getAbsolutePath()); //$NON-NLS-1
				}
				strParams.putAll(ps);
				
			}
			if(results.hasOption('p'))
			{
				String[] stringParams = results.getOptionValues('p');

				if(stringParams!=null)
				{
					for(int i=0; i<stringParams.length; i++)
					{
						addParameter(stringParams[i], strParams);
					}
					
				}
			}
			if(strParams.size()==0)
			{
				return params;
			}
			
			IGetParameterDefinitionTask task = engine.createGetParameterDefinitionTask(design);
			Collection paramDefn = task.getParameterDefns(false);
			Iterator iter = paramDefn.iterator();
			while(iter.hasNext())
			{
				//now only support scalar parameter
				IParameterDefnBase pBase = (IParameterDefnBase)iter.next();
				if(pBase instanceof IScalarParameterDefn)
				{
					Object value = this.evaluateDefault((IScalarParameterDefn)pBase, (String)strParams.get(pBase.getName()));
					if(value!=null)
					{
						params.put(pBase.getName(), value);
					}
				}
			}
			

			
			return params;
		}
		
		protected void addParameter(String param, HashMap params)
		{
			if(param==null || param.length()<2)
			{
				return;
			}
			int index = param.indexOf("=");
			if(index<1)
			{
				return;
			}
			String name = param.substring(0, index).trim();
			String value = param.substring(index+1).trim();
			if(value.startsWith("\"")&& value.endsWith("\"") && value.length()>2)
			{
				value = value.substring(1, value.length()-1);
			}
			params.put(name, value);
		}
		
		/**
		 * @param p the scalar parameter
		 * @param expr the default value expression 
		 */
		protected Object evaluateDefault(IScalarParameterDefn p, String value)
		{
			int type = p.getDataType();
			if(value==null)
			{
				return null;
			}
			Object ret = null;
			try
			{
				switch (type)
				{
					case IScalarParameterDefn.TYPE_BOOLEAN :
						ret = DataTypeUtil.toBoolean(value);
						break;
					case IScalarParameterDefn.TYPE_DATE_TIME :
						ret = DataTypeUtil.toDate(value);
						break;
					case IScalarParameterDefn.TYPE_DECIMAL :
						ret = DataTypeUtil.toBigDecimal(value);
						break;
					case IScalarParameterDefn.TYPE_FLOAT :
						ret = DataTypeUtil.toDouble(value);
						break;
					case IScalarParameterDefn.TYPE_STRING:
						ret = DataTypeUtil.toString(value);
						break;
					default:
						break;
				}

			}
			catch (BirtException e)
			{
				logger.log( Level.SEVERE, "the value of parameter " + p.getName() + " is invalid", e );
				value = null;
			}

			return ret;
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
		File designFile =new File( new File( designName ).getAbsolutePath());
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

