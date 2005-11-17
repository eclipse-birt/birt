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

package org.eclipse.birt.report.engine.executor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.script.BirtHashMap;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.CoreJavaScriptWrapper;
import org.eclipse.birt.core.script.IJavascriptWrapper;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.script.ModelJavaScriptInitializer;
import org.eclipse.birt.report.model.script.ModelJavaScriptWrapper;
import org.eclipse.birt.report.model.script.ReportDefinition;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.WrapFactory;

/**
 * Captures the report execution context. This class is needed for accessing
 * global information during execution as well as for for scripting. It
 * implements the <code>report</code> Javascript object, as well as other
 * objects such as <code>report.params</code>,<code>report.config</code>,
 * <code>report.design</code>, etc.
 * 
 * @version $Revision: 1.39 $ $Date: 2005/11/16 07:47:54 $
 */
public class ExecutionContext
{

	// for logging
	private static Logger log = Logger.getLogger( ExecutionContext.class
			.getName( ) );

	// The scripting context
	private ScriptContext scriptContext;

	// data engine used by this context
	private IDataEngine dataEngine;

	// A stack of content objects, with the current one on the top
	private Stack reportContents = new Stack( );

	// Global configuration variables
	private Map configs = new BirtHashMap( );

	// Report parameters
	private Map params = new BirtHashMap( );

	// the report design
	private Report report;

	// the current locale
	private Locale locale;

	private IReportRunnable runnable;

	private ReportExecutor executor;

	private IReportDocument reportDoc;

	private IRenderOption renderOption;

	/** the engine used to create this context */
	private ReportEngine engine;

	private String taskIDString;

	private IReportContent reportContent;
	private int totalPage;
	private int currentPage;
	private HashMap stringFormatters = new HashMap( );
	private HashMap numberFormatters = new HashMap( );
	private HashMap dateFormatters = new HashMap( );

	private Object designObj;

	private Map appContext;

	private IReportContext reportContext;

	/**
	 * create a new context. Call close to finish using the execution context
	 */
	public ExecutionContext( int taskID )
	{
		this( null, taskID );
	}

	/**
	 * create a new context. Call close to finish using the execution context
	 */
	public ExecutionContext( ReportEngine engine, int taskID )
	{
		this.engine = engine;

		taskIDString = "Task" + new Integer( taskID ).toString( ); //$NON-NLS-1$

		locale = Locale.getDefault( );

		if ( engine != null )
		{
			scriptContext = new ScriptContext( engine.getRootScope( ) );
		}
		else
		{
			scriptContext = new ScriptContext( );
		}

		initailizeScriptContext( scriptContext.getContext( ), scriptContext
				.getRootScope( ) );

		// create script context used to execute the script statements
		// register the global variables in the script context
		scriptContext.registerBean( "report", new ReportObject( ) );
		scriptContext.registerBean( "params", params ); //$NON-NLS-1$
		scriptContext.registerBean( "config", configs ); //$NON-NLS-1$
		scriptContext.registerBean( "currentPage", new Integer( currentPage ) );
		scriptContext.registerBean( "totalPage", new Integer( totalPage ) );
	}

	protected void initailizeScriptContext( Context cx, Scriptable scope )
	{
		scriptContext.getContext( ).setWrapFactory( new WrapFactory( ) {

			protected IJavascriptWrapper modelWrapper = new ModelJavaScriptWrapper( );
			protected IJavascriptWrapper coreWrapper = new CoreJavaScriptWrapper( );

			/**
			 * wrapper an java object to javascript object.
			 */
			public Object wrap( Context cx, Scriptable scope, Object obj,
					Class staticType )
			{
				Object object = modelWrapper.wrap( cx, scope, obj, staticType );
				if ( object != obj )
				{
					return object;
				}
				object = coreWrapper.wrap( cx, scope, obj, staticType );
				if ( object != obj )
				{
					return object;
				}
				return super.wrap( cx, scope, obj, staticType );
			}
		} );

		new ModelJavaScriptInitializer( ).initialize( cx, scope );
		new CoreJavaScriptInitializer( ).initialize( cx, scope );
	}

	public ReportEngine getEngine( )
	{
		return this.engine;
	}

	/**
	 * returns taskID as string. The value can be used for logging.
	 * 
	 * @return taskID as string
	 */
	public String getTaskIDString( )
	{
		return taskIDString;
	}

	/**
	 * Clean up the execution context before finishing using it
	 */
	public void close( )
	{
		scriptContext.exit( );
	}

	/**
	 * creates new variable scope.
	 */
	public void newScope( )
	{
		scriptContext.enterScope( );
	}

	public void newScope( Object object )
	{
		Object jsObject = scriptContext.javaToJs( object );
		if ( jsObject instanceof Scriptable )
		{
			scriptContext.enterScope( (Scriptable) jsObject );
		}
		else
		{
			scriptContext.enterScope( );
		}
	}

	/**
	 * exits a variable scope.
	 */
	public void exitScope( )
	{
		scriptContext.exitScope( );
	}

	public void registerBeans( HashMap map )
	{
		if ( map != null )
		{
			Iterator iter = map.keySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				String key = (String) iter.next( );
				registerBean( key, map.get( key ) );
			}

		}
	}

	/**
	 * declares a variable in the current scope. The variable is then accessible
	 * through JavaScript.
	 * 
	 * @param name
	 *            variable name
	 * @param value
	 *            variable value
	 */
	public void registerBean( String name, Object value )
	{
		scriptContext.registerBean( name, value );
	}

	/**
	 * Evaluate a BIRT expression
	 * 
	 * @param source
	 *            the expression to be evaluated
	 * @return the result if no error exists, otherwise null.
	 * 
	 * @see evaluate(String,String,int)
	 */
	public Object evaluate( String source )
	{
		if ( source == null )
		{
			EngineException e = new EngineException(
					"Failed to evaluate " + source );//$NON-NLS-1$
			addException( e );
			log.log( Level.SEVERE, e.getMessage( ), e );
			return null;
		}
		try
		{
			return scriptContext.eval( source );
		}
		catch ( Exception e )
		{
			log.log( Level.SEVERE, e.getMessage( ), e );
			addException( new EngineException(
					"Failed to evaluate " + source, e ) ); //$NON-NLS-1$
		}
		return null;

	}

	/**
	 * @param jsValue
	 *            a Javascript object
	 * @return A Java object
	 */
	public Object jsToJava( Object jsValue )
	{
		return scriptContext.jsToJava( jsValue );
	}

	/**
	 * Evaluate a BIRT expression
	 * 
	 * @param expr
	 *            the expression to be evaluated
	 * @param name
	 *            the file name
	 * @param lineNo
	 *            the line number
	 * 
	 * @return the result if no error exists, otherwise null.
	 */
	public Object evaluate( String expr, String name, int lineNo )
	{
		if ( expr == null )
		{
			EngineException e = new EngineException(
					MessageConstants.SCRIPT_EVALUATION_ERROR, expr );
			addException( e );
			log.log( Level.SEVERE, e.getMessage( ), e );
			return null;
		}
		try
		{
			return scriptContext.eval( expr, name, lineNo );
		}
		catch ( Exception e )
		{
			// TODO eval may throw RuntimeException, which may also need
			// logging. May need to log more info.
			log.log( Level.SEVERE, e.getMessage( ), e );
			addException( new EngineException(
					MessageConstants.SCRIPT_EVALUATION_ERROR, expr, e ) ); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * @param expr
	 *            an expression handle used to evaluate DtE expression
	 * @return the evaluated result of the expression
	 */
	public Object evaluate( IBaseExpression expr )
	{
		try
		{
			return getDataEngine().evaluate( expr );
		}
		catch ( Throwable t )
		{
			// May throw the run-time exception etc.
			log.log( Level.SEVERE, t.getMessage( ), t );
			addException( new EngineException(
					MessageConstants.INVALID_EXPRESSION_ERROR, expr, t ) ); //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * execute the script. simply evaluate the script, and drop the return value
	 * 
	 * @param script
	 *            script to be executed
	 */
	public void execute( String script )
	{
		if ( script != null )
		{
			evaluate( script );
		}
	}

	public void execute( IBaseExpression script )
	{
		if ( script != null )
		{
			evaluate( script );
		}
	}

	/**
	 * execute the script. Simply evaluate the script, then drop the return
	 * value
	 * 
	 * @param script
	 *            script statement
	 * @param fileName
	 *            file name
	 * @param lineNo
	 *            line no
	 */
	public void execute( String script, String fileName, int lineNo )
	{
		if ( script != null )
		{
			evaluate( script, fileName, lineNo );
		}
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale( )
	{
		return locale;
	}

	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale( Locale locale )
	{
		this.locale = locale;
	}

	/**
	 * @return Returns the report.
	 */
	public Report getReport( )
	{
		return report;
	}

	/**
	 * @return Returns the report.
	 */
	public IReportContent getReportContent( )
	{
		return reportContent;
	}

	/**
	 * @param report
	 *            The report to set.
	 */
	public void setReport( Report report )
	{
		this.report = report;
	}

	/**
	 * @return Returns the dataEngine.
	 */
	public IDataEngine getDataEngine( )
	{
		if (dataEngine == null)
		{
			dataEngine = DataEngineFactory.getInstance( ).createDataEngine( this );
		}
		return dataEngine;
	}

	/**
	 * @param name
	 * @param value
	 */
	public void setParamter( String name, Object value )
	{
		params.put( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IFactoryContext#getConfigs()
	 */
	public Map getConfigs( )
	{
		return configs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IFactoryContext#getReportDesign()
	 */
	public ReportDesignHandle getDesign( )
	{
		return report.getReportDesign( );
	}

	public IReportContent getReportCotent( )
	{
		return this.reportContent;
	}

	public void setReportContent( IReportContent content )
	{
		this.reportContent = content;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IPrensentationContext#getItemState()
	 */
	public IContent getItemState( )
	{
		return (IContent) reportContents.peek( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.executor.IPrensentationContext#getParams()
	 */
	public Map getParams( )
	{
		return params;
	}

	/**
	 * Loads scripts that are stored in an external file. Used to support
	 * include-script. Each script file should be load only once. and the script
	 * in the file must be encoded in UTF-8.
	 * 
	 * @param fileName
	 *            script file name
	 */
	public void loadScript( String fileName )
	{
		File script = new File( report.getBasePath( ), fileName );
		// read the script in the file, and execution.
		try
		{

			FileInputStream in = new FileInputStream( script );
			byte[] buffer = new byte[in.available( )];
			in.read( buffer );
			execute( new String( buffer, "UTF-8" ), fileName, 1 ); //$NON-NLS-1$
			in.close( );
		}
		catch ( IOException ex )
		{
			log.log( Level.SEVERE,
					"loading external script file " + fileName + " failed.", //$NON-NLS-1$ //$NON-NLS-2$
					ex );
			addException( new EngineException(
					MessageConstants.SCRIPT_FILE_LOAD_ERROR, script
							.getAbsolutePath( ), ex ) ); //$NON-NLS-1$
			// TODO This is a fatal error. Should throw an exception.
		}
	}

	/**
	 * get current scope
	 * 
	 * @return scope object
	 */
	public Scriptable getScope( )
	{
		return scriptContext.getScope( );
	}

	public Scriptable getSharedScope( )
	{
		return scriptContext.getSharedScope( );
	}

	/**
	 * @param obj
	 */
	public void pushContent( IContent obj )
	{
		reportContents.push( obj );
		newScope( obj );
	}

	/**
	 * @return
	 */
	public void popContent( )
	{
		reportContents.pop( );
		exitScope( );
	}

	/**
	 * @return
	 */
	public IContent getContent( )
	{
		if ( reportContents.empty( ) )
		{
			return null;
		}
		return (IContent) reportContents.peek( );
	}

	/**
	 * Adds the exception
	 * 
	 * @param ex
	 *            the Throwable instance
	 */
	public void addException( BirtException ex )
	{
		IContent content = getContent( );
		
		ReportItemDesign design = null;
		if ( content != null )
			design = (ReportItemDesign) content.getGenerateBy( );
		
		DesignElementHandle handle = design == null ? null : design.getHandle( );
		addException( handle, ex );
	}

	protected HashMap elementExceptions = new HashMap( );

	public void addException( DesignElementHandle element, BirtException ex )
	{
		ElementExceptionInfo exInfo = (ElementExceptionInfo) elementExceptions
				.get( element );
		if ( exInfo == null )
		{
			exInfo = new ElementExceptionInfo( element );
			if ( reportContent != null )
				reportContent.getErrors( ).add( exInfo );
			elementExceptions.put( element, exInfo );
		}
		exInfo.addException( ex );
	}

	/**
	 * report object is the script object used in the script context.
	 * 
	 * All infos can get from this object.
	 * 
	 * 
	 */
	private class ReportObject
	{

		/**
		 * get the report design handle
		 * 
		 * @return report design object.
		 */
		public Object getDesign( )
		{
			return designObj;
		}

		/**
		 * get the report document.
		 * 
		 * @return report document.
		 */
		public Object getDocument( )
		{
			return null;
		}

		/**
		 * @return a map of name/value pairs for all the parameters and their
		 *         values
		 */
		public Map getParams( )
		{
			return params;
		}

		// TODO DTE should return the datasets and datasources.
		/**
		 * @return a set of data sets
		 */
		public SlotHandle getDataSets( )
		{
			return null;
		}

		/**
		 * @return a set of data sources
		 */
		public SlotHandle getDataSources( )
		{
			return null;
		}

		/**
		 * @return a map of name/value pairs for all the configuration variables
		 */
		public Map getConfig( )
		{
			return configs;
		}
	}

	/**
	 * @return Returns the runnable.
	 */
	public IReportRunnable getRunnable( )
	{
		return runnable;
	}

	/**
	 * @param runnable
	 *            The runnable to set.
	 */
	public void setRunnable( IReportRunnable runnable )
	{
		this.runnable = runnable;
		ReportDefinition designDefn = new ReportDefinition(
				(ReportDesignHandle) runnable.getDesignHandle( ) );
		scriptContext.registerBean( "design", designDefn );
		this.designObj = scriptContext.eval( "design" );
	}

	/**
	 * @return Returns the renderOption.
	 */
	public IRenderOption getRenderOption( )
	{
		return renderOption;
	}

	/**
	 * @param renderOption
	 *            The renderOption to set.
	 */
	public void setRenderOption( IRenderOption renderOption )
	{
		this.renderOption = renderOption;
	}

	public class ElementExceptionInfo
	{

		DesignElementHandle element;
		ArrayList exList = new ArrayList( );
		ArrayList countList = new ArrayList( );

		public ElementExceptionInfo( DesignElementHandle element )
		{
			this.element = element;
		}

		public void addException( BirtException e )
		{
			for ( int i = 0; i < exList.size( ); i++ )
			{
				BirtException err = (BirtException) exList.get( i );
				if ( e.getErrorCode( ) != null
						&& e.getErrorCode( ).equals( err.getErrorCode( ) )
						&& e.getLocalizedMessage( ) != null
						&& e.getLocalizedMessage( ).equals(
								err.getLocalizedMessage( ) ) )
				{
					countList.set( i, new Integer( ( (Integer) countList
							.get( i ) ).intValue( ) + 1 ) );
					return;
				}
			}
			exList.add( e );
			countList.add( new Integer( 1 ) );

		}

		public String getType( )
		{
			if ( element == null )
			{
				return "report";
			}
			return element.getDefn( ).getName( );
		}

		public String getElementInfo( )
		{
			if ( element == null )
			{
				return "report";
			}
			return element.getName( );
		}

		public ArrayList getErrorList( )
		{
			return exList;
		}

		public ArrayList getCountList( )
		{
			return countList;
		}

	}

	public Map getAppContext( )
	{
		return appContext;
	}

	public void setAppContext( Map appContext )
	{
		this.appContext = appContext;
	}

	public IReportContext getReportContext( )
	{
		return reportContext;
	}

	public void setReportContext( IReportContext reportContext )
	{
		this.reportContext = reportContext;
	}

	public void setCurrentPage( int pageNo )
	{
		currentPage = pageNo;
		scriptContext.registerBean( "currentPage", new Integer( currentPage ) );
		if ( totalPage < currentPage )
		{
			totalPage = currentPage;
			scriptContext.registerBean( "totalPage", new Integer( totalPage ) );
		}
	}

	public int getCurrentPage( )
	{
		return currentPage;
	}

	public int getTotalPage( )
	{
		return totalPage;
	}

	public boolean isInFactory( )
	{
		return true;
	}

	public boolean isInPresentation( )
	{
		return false;
	}

	public StringFormatter getStringFormatter( String value )
	{
		StringFormatter fmt = (StringFormatter) stringFormatters.get( value );
		if ( fmt == null )
		{
			fmt = new StringFormatter( value, locale );
			stringFormatters.put( value, fmt );
		}
		return fmt;
	}

	public NumberFormatter getNumberFormatter( String value )
	{
		NumberFormatter fmt = (NumberFormatter) numberFormatters.get( value );
		if ( fmt == null )
		{
			fmt = new NumberFormatter( value, locale );
			numberFormatters.put( value, fmt );
		}
		return fmt;
	}

	public DateFormatter getDateFormatter( String value )
	{
		DateFormatter fmt = (DateFormatter) dateFormatters.get( value );
		if ( fmt == null )
		{
			fmt = new DateFormatter( value, locale );
			dateFormatters.put( value, fmt );
		}
		return fmt;
	}

	public void setExecutor( ReportExecutor executor )
	{
		this.executor = executor;
	}

	public ReportExecutor getExecutor( )
	{
		return executor;
	}

	public void setReportDocument( IReportDocument doc )
	{
		this.reportDoc = doc;
	}

	public IReportDocument getReportDocument( )
	{
		return reportDoc;
	}

}