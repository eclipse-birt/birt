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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.core.script.BirtHashMap;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.CoreJavaScriptWrapper;
import org.eclipse.birt.core.script.IJavascriptWrapper;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLEmitterConfig;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.script.internal.element.ReportDesign;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

/**
 * Captures the report execution context. This class is needed for accessing
 * global information during execution as well as for for scripting. It
 * implements the <code>report</code> Javascript object, as well as other
 * objects such as <code>report.params</code>,<code>report.config</code>,
 * <code>report.design</code>, etc.
 * 
 * @version $Revision: 1.67 $ $Date: 2006/05/18 05:26:36 $
 */
public class ExecutionContext
{

	// for logging
	private static Logger log = Logger.getLogger( ExecutionContext.class
			.getName( ) );

	public static final String PROPERTYSEPARATOR = ";";

	// engines used to create the context
	/** the engine used to create this context */
	private IReportEngine engine;

	/**
	 * task ID string
	 */
	private String taskIDString;

	/**
	 * execution mode, in this mode, the render operation should be executed.
	 */
	private boolean presentationMode = false;

	/**
	 * execution mode, in this mode, the genreation opration should be executed.
	 */
	private boolean factoryMode = true;

	// utilitis used in this context.

	/**
	 * The scripting context, used to evaluate the script.
	 */
	private ScriptContext scriptContext;

	/**
	 * data engine, used to evaluate the data related expressions.
	 */
	private IDataEngine dataEngine;

	/**
	 * utility used to create the report content
	 */
	private ReportExecutor executor;

	/**
	 * utility used to create the TOC
	 */
	private TOCBuilder tocBuilder;

	// then is the input content
	/**
	 * report runnable used to create the report content
	 */
	private IReportRunnable runnable;

	/**
	 * the report design contained in the report runnable
	 */
	private Report report;

	/**
	 * Global configuration variables
	 */
	private Map configs = new BirtHashMap( );

	/**
	 * Report parameters used to create the report content
	 */
	private Map params = new BirtHashMap( );

	private Map persistentBeans = new HashMap( );

	private Map transientBeans = new HashMap( );

	private ReportDocumentWriter docWriter;

	/**
	 * app context
	 */
	private Map appContext;

	/**
	 * report context used to evaluate the java-based script.
	 */
	private IReportContext reportContext;

	/**
	 * options used to render the report content
	 */
	private IRenderOption renderOption;

	/**
	 * the current locale, used to localize the report content
	 */
	private Locale locale;

	// at last the output objects
	/**
	 * report document, may be the output or input.
	 */
	private IReportDocument reportDoc;

	/**
	 * the created report content
	 */
	private IReportContent reportContent;

	/**
	 * A stack of content objects, with the current one on the top
	 */
	private Stack reportContents = new Stack( );

	/**
	 * A stack of handle objects, with the current one on the top
	 */
	private Stack reportHandles = new Stack( );

	/**
	 * total page
	 */
	private long totalPage;

	/**
	 * current page number
	 */
	private long pageNumber;

	/**
	 * A list of script errors found in onPrepare. These will be added to the
	 * IReportContent, when one is available
	 * 
	 */
	private List onPrepareErrors = new ArrayList( );

	/**
	 * Flag to indicate whether task is canceled.
	 */
	private boolean isCancelled = false;
	
	/**
	 * utilities used in the report execution.
	 */
	private HashMap stringFormatters = new HashMap( );

	private HashMap numberFormatters = new HashMap( );

	private HashMap dateFormatters = new HashMap( );

	private ClassLoader applicationClassLoader;
	
	private Map classLoaderCache = new HashMap( );
	
	/**
	 * 
	 */
	private IDocArchiveReader dataSource;

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
	public ExecutionContext( IReportEngine engine, int taskID )
	{
		this.engine = engine;

		taskIDString = "Task" + new Integer( taskID ).toString( ); //$NON-NLS-1$

		locale = Locale.getDefault( );

		ScriptableObject rootScope = null;
		if ( engine != null )
		{
			Object scope = engine.getRootScope( );
			if ( scope instanceof ScriptableObject )
			{
				rootScope = (ScriptableObject) scope;
			}
		}
		if ( rootScope != null )
		{
			scriptContext = new ScriptContext( rootScope );
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
		scriptContext.registerBean( "currentPage", new Long( pageNumber ) );
		scriptContext.registerBean( "totalPage", new Long( totalPage ) );
		scriptContext.registerBean( "_jsContext", this );
		scriptContext
				.eval( "function registerGlobal( name, value) { _jsContext.registerGlobalBean(name, value); }" );
		scriptContext
				.eval( "function unregisterGlobal(name) { _jsContext.unregisterGlobalBean(name); }" );
		
		applicationClassLoader = new ApplicationClassLoader( );
		scriptContext.getContext( ).setApplicationClassLoader(
				applicationClassLoader );
		
	}

	protected void initailizeScriptContext( Context cx, Scriptable scope )
	{
		scriptContext.getContext( ).setWrapFactory( new WrapFactory( ) {

			protected IJavascriptWrapper coreWrapper = new CoreJavaScriptWrapper( );

			/**
			 * wrapper an java object to javascript object.
			 */
			public Object wrap( Context cx, Scriptable scope, Object obj,
					Class staticType )
			{
				Object object = coreWrapper.wrap( cx, scope, obj, staticType );
				if ( object != obj )
				{
					return object;
				}
				return super.wrap( cx, scope, obj, staticType );
			}
		} );

		new CoreJavaScriptInitializer( ).initialize( cx, scope );
	}

	/**
	 * get the report engine. In that engine, we create the context.
	 * 
	 * @return the report engine used to create the context.
	 */
	public IReportEngine getEngine( )
	{
		return engine;
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
		if ( dataSource != null )
		{
			try
			{
				dataSource.close( );
			}
			catch ( IOException e )
			{
				log.log( Level.SEVERE, "Failed to close the data source", e );
			}
			dataSource = null;
		}
		if ( dataEngine != null )
		{
			dataEngine.shutdown( );
			dataEngine = null;
		}
	}

	/**
	 * creates new variable scope.
	 */
	public void newScope( )
	{
		scriptContext.enterScope( );
	}

	/**
	 * create a new scope, use the object to create the curren scope.
	 * 
	 * @param object
	 *            the "this" object in the new scope
	 */
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

	/**
	 * register beans in the execution context
	 * 
	 * @param map
	 *            name value pair.
	 */
	public void registerBeans( Map map )
	{
		if ( map != null )
		{

			Iterator iter = map.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				Object keyObj = entry.getKey( );
				Object value = entry.getValue( );
				if ( keyObj != null )
				{
					String key = keyObj.toString( );
					registerBean( key, value );
				}
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
		transientBeans.put( name, value );
		scriptContext.registerBean( name, value );
	}

	public void unregisterBean( String name )
	{
		transientBeans.remove( name );
		scriptContext.registerBean( name, null );
	}

	public Map getBeans( )
	{
		return transientBeans;
	}

	public void registerGlobalBeans( Map map )
	{
		if ( map != null )
		{

			Iterator iter = map.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				Object keyObj = entry.getKey( );
				Object value = entry.getValue( );
				if ( keyObj != null && value instanceof Serializable )
				{
					String key = keyObj.toString( );
					registerGlobalBean( key, (Serializable) value );
				}
			}
		}
	}

	public void registerGlobalBean( String name, Serializable value )
	{
		persistentBeans.put( name, value );
		registerInRoot( name, value );
	}

	public void unregisterGlobalBean( String name )
	{
		persistentBeans.remove( name );
		registerInRoot( name, null );
	}

	public Map getGlobalBeans( )
	{
		return persistentBeans;
	}

	private void registerInRoot( String name, Object value )
	{
		Scriptable root = scriptContext.getRootScope( );
		Object sObj = Context.javaToJS( value, root );
		root.put( name, root, sObj );
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
		return evaluate( source, "<inline>", 1 );
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
		if ( expr != null )
		{
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
		}
		return null;
	}	

	/**
	 * evaluate conditional expression. A conditional expression can have an
	 * operator, one LHS expression, and up to two expressions on RHS, i.e.,
	 * 
	 * testExpr operator operand1 operand2 or testExpr between 1 20
	 * 
	 * Now only support comparison between the same data type
	 * 
	 * @param expr
	 *            the conditional expression to be evaluated
	 * @return a boolean value (as an Object)
	 */
	public Object evaluateCondExpr( IConditionalExpression expr )
	{
		int operator = expr.getOperator( );
		IScriptExpression testExpr = expr.getExpression( );
		IScriptExpression v1 = expr.getOperand1( );
		IScriptExpression v2 = expr.getOperand2( );

		if ( testExpr == null )
			return Boolean.FALSE;

		Object testExprValue = evaluate( testExpr.getText( ) );
		if ( IConditionalExpression.OP_NONE == operator )
		{
			return testExprValue;
		}
		Object vv1 = null;
		Object vv2 = null;
		if ( v1 != null )
		{
			vv1 = evaluate( v1.getText( ) );
		}
		if ( v2 != null )
		{
			vv2 = evaluate( v2.getText( ) );
		}

		try
		{

			return ScriptEvalUtil.evalConditionalExpr( testExprValue, expr
					.getOperator( ), vv1, vv2 );
		}
		catch ( Exception e )
		{
			log.log( Level.SEVERE, e.getMessage( ), e );
			addException( new EngineException(
					MessageConstants.INVALID_EXPRESSION_ERROR, expr, e ) );
			return Boolean.FALSE;
		}
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
	 * @param report
	 *            The report to set.
	 */
	public void setReport( Report report )
	{
		this.report = report;
	}

	public void openDataEngine( )
	{
		if ( dataEngine == null )
		{
			dataEngine = DataEngineFactory.getInstance( ).createDataEngine(
					this );
		}
	}

	/**
	 * @return Returns the dataEngine.
	 */
	public IDataEngine getDataEngine( )
	{
		if ( dataEngine == null )
		{
			dataEngine = DataEngineFactory.getInstance( ).createDataEngine(
					this );
		}
		return dataEngine;
	}

	public void closeDataEngine( )
	{
		if ( dataEngine != null )
		{
			dataEngine.shutdown( );
			dataEngine = null;
		}
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
		return (ReportDesignHandle) runnable.getDesignHandle( );
	}

	/**
	 * @return Returns the report.
	 */
	public IReportContent getReportContent( )
	{
		return reportContent;
	}

	public void setReportContent( IReportContent content )
	{
		this.reportContent = content;
		content.getErrors( ).addAll( onPrepareErrors );
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
		ReportDesignHandle reportDesign = this.getDesign( );
		if ( reportDesign != null )
		{
			URL url = reportDesign.findResource( fileName,
					IResourceLocator.LIBRARY );
			if ( url != null )
			{
				fileName = url.getFile( );
			}
		}
		File scriptFile = new File( fileName );
		// read the script in the file, and execution.
		try
		{

			FileInputStream in = new FileInputStream( scriptFile );
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			byte[] buffer = new byte[1024];
			int size = in.read( buffer );
			while ( size != -1 )
			{
				out.write( buffer, 0, size );
				size = in.read( buffer );
			}
			byte[] script = out.toByteArray( );
			execute( new String( script, "UTF-8" ), fileName, 1 ); //$NON-NLS-1$
			in.close( );
		}
		catch ( IOException ex )
		{
			log.log( Level.SEVERE,
					"loading external script file " + fileName + " failed.", //$NON-NLS-1$ //$NON-NLS-2$
					ex );
			addException( new EngineException(
					MessageConstants.SCRIPT_FILE_LOAD_ERROR, scriptFile
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

	ScriptContext getScriptContext( )
	{
		return this.scriptContext;
	}

	/**
	 * @param obj
	 */
	public void pushContent( IContent obj )
	{
		reportContents.push( obj );
		// newScope( obj );
	}

	/**
	 * @return
	 */
	public IContent popContent( )
	{
		// exitScope( );
		return (IContent) reportContents.pop( );
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
	 * @param obj
	 */
	public void pushHandle( DesignElementHandle obj )
	{
		reportHandles.push( obj );
	}

	/**
	 * @return
	 */
	public DesignElementHandle popHandle( )
	{
		return (DesignElementHandle) reportHandles.pop( );
	}

	/**
	 * @return
	 */
	public DesignElementHandle getHandle( )
	{
		if ( reportHandles.empty( ) )
		{
			return null;
		}
		return (DesignElementHandle) reportHandles.peek( );
	}

	/**
	 * Adds the exception
	 * 
	 * @param ex
	 *            the Throwable instance
	 */
	public void addException( BirtException ex )
	{
		DesignElementHandle handle = null;
		if ( !reportContents.empty( ) )
		{
			IContent content = getContent( );

			ReportItemDesign design = null;
			if ( content != null )
				design = (ReportItemDesign) content.getGenerateBy( );

			handle = design == null ? null : design.getHandle( );
		}
		else
			handle = getHandle( );
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
			else
				onPrepareErrors.add( exInfo );
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
			return scriptContext.eval( "design" );
		}

		/**
		 * get the report document.
		 * 
		 * @return report document.
		 */
		public Object getDocument( )
		{
			return reportDoc;
		}

		/**
		 * @return a map of name/value pairs for all the parameters and their
		 *         values
		 */
		public Map getParams( )
		{
			return params;
		}

		/**
		 * @return a set of data sets
		 */
		public Object getDataSets( )
		{
			return scriptContext.eval( "design.dataSets" );
		}

		/**
		 * @return a set of data sources
		 */
		public Object getDataSources( )
		{
			return scriptContext.eval( "design.dataSources" );
		}

		/**
		 * @return a map of name/value pairs for all the configuration variables
		 */
		public Map getConfig( )
		{
			return configs;
		}

		public Object getReportContext( )
		{
			return reportContext;
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

		ReportDesignHandle reportDesign = (ReportDesignHandle) runnable
				.getDesignHandle( );
		scriptContext.registerBean( "design", new ReportDesign( reportDesign ) );
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

	public String getOutputFormat( )
	{
		if ( renderOption != null )
		{
			return renderOption.getOutputFormat( );
		}
		return null;
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

		public String getName( )
		{
			if ( element == null )
			{
				return "report";
			}
			return element.getName( );
		}
		
		public String getID( )
		{
			if ( element == null )
				return null;
			else
				return String.valueOf( element.getID( ) );
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
		scriptContext.registerBean( "reportContext", reportContext );
	}

	public void setPageNumber( long pageNo )
	{
		pageNumber = pageNo;
		scriptContext.registerBean( "pageNumber", new Long( pageNumber ) );
		if ( totalPage < pageNumber )
		{
			setTotalPage( pageNumber );
		}
	}

	/**
	 * set the total page.
	 * 
	 * @param totalPage
	 *            total page
	 */
	public void setTotalPage( long totalPage )
	{
		if ( totalPage > pageNumber )
		{
			totalPage = pageNumber;
			scriptContext.registerBean( "totalPage", new Long( totalPage ) );
		}
	}

	/**
	 * get the current page number
	 * 
	 * @return current page number
	 */
	public long getPageNumber( )
	{
		return pageNumber;
	}

	/**
	 * get the total page have been created.
	 * 
	 * @return total page
	 */
	public long getTotalPage( )
	{
		return totalPage;
	}

	/**
	 * is in factory mode
	 * 
	 * @return true, factory mode, false not in factory mode
	 */
	public boolean isInFactory( )
	{
		return factoryMode;
	}

	/**
	 * is in presentation mode.
	 * 
	 * @return true, presentation mode, false otherwise
	 */
	public boolean isInPresentation( )
	{
		return presentationMode;
	}

	/**
	 * set the in factory mode
	 * 
	 * @param mode
	 *            factory mode
	 */
	public void setFactoryMode( boolean mode )
	{
		this.factoryMode = mode;
	}

	/**
	 * set in presentation mode
	 * 
	 * @param mode
	 *            presentation mode
	 */
	public void setPresentationMode( boolean mode )
	{
		this.presentationMode = mode;
	}

	/**
	 * get a string formatter object
	 * 
	 * @param value
	 *            string format
	 * @return formatter object
	 */
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

	/**
	 * get a number formatter object
	 * 
	 * @param value
	 *            number format
	 * @return formatter object
	 */
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

	/**
	 * get a date formatter object
	 * 
	 * @param value
	 *            date format
	 * @return formatter object
	 */
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

	/**
	 * set the executor used in the execution context
	 * 
	 * @param executor
	 */
	public void setExecutor( ReportExecutor executor )
	{
		this.executor = executor;
	}

	/**
	 * get the executor used to execute the report
	 * 
	 * @return report executor
	 */
	public ReportExecutor getExecutor( )
	{
		return executor;
	}

	public TOCBuilder getTOCBuilder( )
	{
		return tocBuilder;
	}

	public void setTOCBuilder( TOCBuilder builder )
	{
		this.tocBuilder = builder;
	}

	/**
	 * set the report document used in the context
	 * 
	 * @param doc
	 */
	public void setReportDocument( IReportDocument doc )
	{
		this.reportDoc = doc;
	}

	/**
	 * get the report document used in the context.
	 * 
	 * @return
	 */
	public IReportDocument getReportDocument( )
	{
		return reportDoc;
	}

	public void setReportDocWriter( ReportDocumentWriter docWriter )
	{
		this.docWriter = docWriter;
	}

	public ReportDocumentWriter getReportDocWriter( )
	{
		return docWriter;
	}

	/**
	 * @return Returns the action handler.
	 */
	public IHTMLActionHandler getActionHandler( )
	{
		if ( renderOption != null && renderOption instanceof HTMLRenderOption )
		{
			HTMLRenderOption htmlOption = (HTMLRenderOption) renderOption;
			{
				if ( htmlOption.getActionHandle( ) != null )
				{
					return htmlOption.getActionHandle( );
				}
			}
		}
		EngineConfig config = engine.getConfig( );
		if ( config != null )
		{
			HashMap emitterConfigs = config.getEmitterConfigs( );
			if ( emitterConfigs != null )
			{
				Object htmlEmitterConfig = emitterConfigs.get( "html" );
				if ( htmlEmitterConfig instanceof HTMLEmitterConfig )
				{
					HTMLEmitterConfig htmlConfig = (HTMLEmitterConfig) htmlEmitterConfig;
					return htmlConfig.getActionHandler( );
				}
			}
		}
		return null;
	}

	/**
	 * return application class loader.
	 * The application class loader is used to load the report item event handle and 
	 * java classes called in the javascript.
	 * @return class loader
	 */
	public ClassLoader getApplicationClassLoader( )
	{
		return applicationClassLoader;
	}

	/**
	 * the application class loader.
	 * 
	 *   The class loader first try to the load the class as following sequence:
	 *   1. standard java class loader,
	 *   2. classloader setted through the appContext.
	 *   3. CLASSPATH setted by WEBAPP_CLASSPATH_KEY
	 *   4. PROJECT_CLASSPATH_KEY
	 *   5. WORKSAPCE_CLASSPATH_KEY
	 *   
	 */
	private class ApplicationClassLoader extends ClassLoader
	{

		String[] classPathes = new String[]{
				EngineConstants.WEBAPP_CLASSPATH_KEY,
				EngineConstants.PROJECT_CLASSPATH_KEY,
				EngineConstants.WORKSPACE_CLASSPATH_KEY};

		public ApplicationClassLoader( )
		{
		}

		public Class loadClass( String className )
				throws ClassNotFoundException
		{
			try
			{
				try
				{
					// If not found in the cache, try creating one
					return Class.forName( className );
				}
				catch ( ClassNotFoundException ex )
				{
					if ( appContext != null )
					{
						Object appLoader = appContext
								.get( EngineConstants.APPCONTEXT_CLASSLOADER_KEY );
						if ( appLoader instanceof ClassLoader )
						{
							return ( (ClassLoader) appLoader )
									.loadClass( className );
						}
					}
					throw ex;
				}
			}
			catch ( ClassNotFoundException e )
			{
				for ( int i = 0; i < classPathes.length; i++ )
				{
					ClassLoader loader = getCustomClassLoader( classPathes[i] );
					if ( loader != null )
					{
						try
						{
							return loader.loadClass( className );
						}
						catch ( Exception ex )
						{
						}
					}
				}
				throw e;
			}
		}
	}

	protected ClassLoader getCustomClassLoader( String classPathKey )
	{
		Object o = null;

		o = classLoaderCache.get( classPathKey );
		if ( o != null )
			return (ClassLoader) o;
		String classPath = System.getProperty( classPathKey );
		if ( classPath == null || classPath.length( ) == 0 )
			return null;
		String[] classPathArray = classPath.split( PROPERTYSEPARATOR, -1 );
		URL[] urls = null;
		if ( classPathArray.length != 0 )
		{
			List l = new ArrayList( );
			for ( int i = 0; i < classPathArray.length; i++ )
			{
				String cpValue = classPathArray[i];
				File file = new File( cpValue );
				try
				{
					l.add( file.toURL( ) );
				}
				catch ( MalformedURLException e )
				{
					e.printStackTrace( );
				}
			}
			urls = (URL[]) l.toArray( new URL[l.size( )] );
		}

		if ( urls != null )
		{
			ClassLoader cl = new URLClassLoader( urls, ExecutionContext.class
					.getClassLoader( ) );
			classLoaderCache.put( classPathKey, cl );
			return cl;
		}
		return null;
	}

	/**
	 * Set the cancel flag.
	 */
	public void cancel( )
	{
		isCancelled = true;
	}

	public boolean isCanceled( )
	{
		return isCancelled;
	}
	
	public void setDataSource( IDocArchiveReader dataSource )
	{
		try
		{
			dataSource.open( );
			this.dataSource = dataSource;
		}
		catch ( IOException e )
		{
			log.log( Level.SEVERE, "Failed to open the data source", e ); //$NON-NLS-1$
		}
	}
	
	public IDocArchiveReader getDataSource()
	{
		return dataSource;
	}
}