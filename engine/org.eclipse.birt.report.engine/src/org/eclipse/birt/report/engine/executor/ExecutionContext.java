/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.core.script.ScriptableParameters;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.ILinkedResult;
import org.eclipse.birt.report.engine.adapter.ProgressMonitorProxy;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IStatusHandler;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.executor.optimize.ExecutionOptimize;
import org.eclipse.birt.report.engine.executor.optimize.ExecutionPolicy;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Captures the report execution context. This class is needed for accessing
 * global information during execution as well as for for scripting. It
 * implements the <code>report</code> Javascript object, as well as other
 * objects such as <code>report.params</code>,<code>report.config</code>,
 * <code>report.design</code>, etc.
 * 
 */
public class ExecutionContext
{
	/**
	 * how many errors or exceptions will be registered.
	 */
	protected static final int ERROR_TOTAL_COUNT = 60;

	// engines used to create the context
	/** the engine used to create this context */
	private ReportEngine engine;

	/**
	 * task which uses this context.
	 */
	private EngineTask task;

	/**
	 * logger used to log out the excepitons
	 */
	private Logger log;

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
	private IReportExecutor executor;

	/**
	 * utility used to create the TOC
	 */
	private TOCBuilder tocBuilder;

	// then is the input content
	/**
	 * report runnable used to create the report content
	 */
	protected ReportRunnable runnable;
	
	protected ReportRunnable originalRunnable;

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

	private Map<String, PageVariable> pageVariables = new HashMap<String, PageVariable>( );

	private ReportDocumentWriter docWriter;
	
	private Report reportIR;

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
	
	/**
	 * define a time zone
	 */
	private TimeZone timeZone;

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
	 * the current executed design.
	 */
	private ReportItemDesign design;
	/**
	 * The current content element to be executed or loaded
	 */
	private IContent content;
	
	/**
	 * the current opened result set 
	 */
	private IBaseResultSet[] rsets; 

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

	private long filteredTotalPage;
	private long filteredPageNumber;

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
	 * flag to indicate if the task should be canceled on error
	 */
	private boolean cancelOnError = false;
	
	/**
	 * utilities used in the report execution.
	 */
	private HashMap stringFormatters = new HashMap( );

	private HashMap numberFormatters = new HashMap( );

	private HashMap dateFormatters = new HashMap( );

	private ClassLoader applicationClassLoader;
	private boolean closeClassLoader;
	
	private int MAX_ERRORS = 100;
	/**
	 * 
	 */
	private DocumentDataSource dataSource;
	
	/**
	 * All page break listeners.
	 */
	private List pageBreakListeners;
	
	/**
	 * an instance of ExtendedItemManager
	 */
	private ExtendedItemManager extendedItemManager = new ExtendedItemManager( );
	
	/**
	 * an instance of engine extension manager
	 */
	private EngineExtensionManager engineExtensionManager = new EngineExtensionManager( this );
	
	
	/**
	 * max rows per query. An initial value -1 means it is not set
	 */
	private int maxRowsPerQuery = -1;
	
	private EventHandlerManager eventHandlerManager;
	
	private IProgressMonitor progressMonitor;

	private boolean needOutputResultSet;
	
	private boolean isFixedLayout = false;

	/**
	 * create a new context. Call close to finish using the execution context
	 */
	public ExecutionContext( )
	{
		this( null );
	}

	/**
	 * create a new context. Call close to finish using the execution context
	 */
	public ExecutionContext( EngineTask engineTask )
	{
		if ( engineTask != null )
		{
			task = engineTask;
			engine = (ReportEngine)task.getEngine( );
			log = task.getLogger( );
		}
		else
		{
			log = Logger.getLogger( ExecutionContext.class.getName( ) );
		}

		locale = Locale.getDefault( );
		
		timeZone = TimeZone.getDefault( );
		eventHandlerManager = new EventHandlerManager( );
	}

	private void initializeScriptContext( )
	{
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
		if (runnable != null && runnable instanceof ReportRunnable)
		{
			scriptContext.setCompiledScripts( ((ReportRunnable)runnable).getScriptCache() );
		}
		
		Context context = scriptContext.getContext( );
//		try
//		{
//			context.setSecurityController( ScriptUtil
//					.createSecurityController( ) );
//		}
//		catch ( Throwable throwable )
//		{
//		}
		context.setLocale( locale );

		initializeScriptContext( context, scriptContext
				.getRootScope( ) );

		// create script context used to execute the script statements
		// register the global variables in the script context
		scriptContext.registerBean( "report", new ReportObject( ) );
		scriptContext
				.registerBean(
						"params", new ScriptableParameters( params, scriptContext.getScope( ) ) ); //$NON-NLS-1$
		scriptContext.registerBean( "config", configs ); //$NON-NLS-1$
		scriptContext.registerBean( "currentPage", new Long( pageNumber ) );
		scriptContext.registerBean( "totalPage", new Long( totalPage ) );
		scriptContext.registerBean( "_jsContext", this );
		scriptContext.registerBean( "vars", new ScriptablePageVariables(
				pageVariables, scriptContext.getScope( ) ) );
		if ( runnable != null )
		{
			registerDesign( runnable );
		}
		if ( reportContext != null )
		{
			scriptContext.registerBean( "reportContext", reportContext );
		}
		scriptContext.getContext( ).setLocale( locale );
		scriptContext.registerBean( "pageNumber", new Long( pageNumber ) );
		scriptContext.registerBean( "totalPage", new Long( totalPage ) );
		if ( task != null )
		{
			IStatusHandler handler = task.getStatusHandler( );
			if ( handler != null )
			{
				handler.initialize( );
				scriptContext.registerBean( "_statusHandle", handler );
				try
				{
					scriptContext
							.eval( "function writeStatus(msg) { _statusHandle.showStatus(msg); }" );
				}
				catch ( BirtException e )
				{
					addException( e );
				}
			}
		}
		if (transientBeans != null )
		{
			Iterator entries = transientBeans.entrySet( ).iterator( );
			while( entries.hasNext( ))
			{
				Map.Entry entry = (Map.Entry)entries.next( );
				scriptContext.registerBean( (String) entry.getKey( ), entry
						.getValue( ) );
			}
		}
		if (persistentBeans != null )
		{
			Iterator entries = persistentBeans.entrySet( ).iterator( );
			while( entries.hasNext( ))
			{
				Map.Entry entry = (Map.Entry)entries.next( );
				registerInRoot( (String) entry.getKey( ), entry.getValue( ) );
			}
		}
		scriptContext.setApplicationClassLoader( getApplicationClassLoader( ) );
	}

	protected void initializeScriptContext( Context cx, Scriptable scope )
	{
		WrapFactory factory = new WrapFactory( )
		{
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
		};
		//factory.setJavaPrimitiveWrap( false );
		
		scriptContext.getContext( ).setWrapFactory( factory );

		new CoreJavaScriptInitializer( ).initialize( cx, scope );
	}
	
	/**
	 * get the report engine. In that engine, we create the context.
	 * 
	 * @return the report engine used to create the context.
	 */
	public ReportEngine getEngine( )
	{
		return engine;
	}

	/**
	 * Clean up the execution context before finishing using it
	 */
	public void close( )
	{
		if ( extendedItemManager != null )
		{
			extendedItemManager.close( );
			extendedItemManager = null;
		}
		
		if ( engineExtensionManager != null )
		{
			engineExtensionManager.close( );
			engineExtensionManager = null;
		}

		if ( scriptContext != null )
		{
			scriptContext.exit( );
			scriptContext = null;
		}
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
		if ( closeClassLoader
				&& applicationClassLoader instanceof ApplicationClassLoader )
		{
			( (ApplicationClassLoader) applicationClassLoader ).close( );
		}
		
		IStatusHandler handler = task.getStatusHandler( );
		if ( handler != null )
		{
			handler.finish( );
		}
		
		closeClassLoader = false;
		applicationClassLoader = null;
	}

	/**
	 * creates new variable scope.
	 */
	public void newScope( )
	{
		getScriptContext( ).enterScope( );
	}

	/**
	 * create a new scope, use the object to create the curren scope.
	 * 
	 * @param object
	 *            the "this" object in the new scope
	 */
	public void newScope( Object object )
	{
		ScriptContext scriptContext = getScriptContext( );
		Object jsObject = scriptContext.javaToJs( object );
		if ( jsObject instanceof Scriptable )
		{
			NativeObject nativeObj = new NativeObject();
			nativeObj.setPrototype(  (Scriptable)jsObject );
			scriptContext.enterScope( nativeObj );
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
		getScriptContext( ).exitScope( );
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
		if( scriptContext != null )
		{
			scriptContext.registerBean( name, value );
		}
	}

	public void unregisterBean( String name )
	{
		transientBeans.remove( name );
		if( scriptContext != null )
		{
			scriptContext.registerBean( name, null );
		}
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
		if ( scriptContext != null )
		{
			registerInRoot( name, value );
		}
	}

	public void unregisterGlobalBean( String name )
	{
		persistentBeans.remove( name );
		if ( scriptContext != null )
		{
			registerInRoot( name, null );
		}
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

	public Object evaluate( Expression expr ) throws BirtException
	{
		if ( expr != null )
		{
			switch ( expr.getType( ) )
			{
				case Expression.CONSTANT :
					Expression.Constant cs = (Expression.Constant) expr;
					return cs.getValue( );

				case Expression.SCRIPT :
					ScriptExpression se = ( (Expression.Script) expr )
							.getScriptExpression( );
					return evaluate( se );

				case Expression.CONDITIONAL :
					IConditionalExpression ce = ( (Expression.Conditional) expr )
							.getConditionalExpression( );
					return evaluateCondExpr( ce );

			}
		}
		return null;
	}

	public Object evaluate( String scriptText ) throws BirtException
	{
		return getScriptContext( ).eval( scriptText );
	}

	public Object evaluate( ScriptExpression expr ) throws BirtException
	{
		return getScriptContext( ).eval( expr );
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
	public Object evaluateCondExpr( IConditionalExpression expr ) throws BirtException
	{
		IScriptExpression testExpr = expr.getExpression( );
		ScriptContext scriptContext = getScriptContext( );
		if ( testExpr == null )
			return Boolean.FALSE;
		try
		{
			return ScriptEvalUtil.evalExpr( expr,
					scriptContext,
					scriptContext.getScope( ),
					ScriptExpression.defaultID,
					0 );
		}
		catch ( Throwable e )
		{
			throw new EngineException(
					MessageConstants.INVALID_EXPRESSION_ERROR, testExpr.getText( ), e );
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
	public void execute( ScriptExpression expr )
	{
		try
		{
			getScriptContext( ).eval( expr );
		}
		catch ( BirtException ex )
		{
			addException( this.design, ex );
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
		this.getScriptContext( ).setLocale( locale );
	}
	
	public TimeZone getTimeZone()
	{
		return this.timeZone;
	}

	public void setTimeZone( TimeZone timeZone )
	{
		this.timeZone = timeZone;
		this.getScriptContext( ).setTimeZone( timeZone );
	}


	public void openDataEngine( )
	{
		if ( dataEngine == null )
		{
			try
			{
				dataEngine = DataEngineFactory.getInstance( ).createDataEngine(
						this, needOutputResultSet );
			}
			catch ( BirtException bex )
			{
				addException( bex );
			}
			catch ( Exception ex )
			{
				addException( new EngineException( ex.getLocalizedMessage( ),
						ex ) );
			}
		}
	}

	/**
	 * @return Returns the dataEngine.
	 */
	public IDataEngine getDataEngine( )
	{
		if ( dataEngine == null )
		{
			openDataEngine( );
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
	public void setParameterValue( String name, Object value )
	{
		Object parameter = params.get( name );
		if ( parameter instanceof ParameterAttribute )
		{
			( (ParameterAttribute) parameter ).setValue( value );
		}
		else
		{
			params.put( name, new ParameterAttribute( value, null ) );
		}
	}

	/**
	 * @param name
	 * @param value
	 */
	public void setParameter( String name, Object value, String displayText )
	{
		params.put( name, new ParameterAttribute( value, displayText ) );
	}

	public void clearParameters( )
	{
		params.clear( );
	}

	public Object getParameterValue( String name )
	{
		Object parameter = params.get( name );
		if ( parameter != null )
		{
			return ( (ParameterAttribute) parameter ).getValue( );
		}
		return null;		
	}

	public Map getParameterValues( )
	{
		HashMap result = new HashMap( );
		Set entries = params.entrySet( );
		Iterator iterator = entries.iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			ParameterAttribute parameter = (ParameterAttribute) entry
					.getValue( );
			result.put( entry.getKey( ), parameter.getValue( ) );
		}
		return result;

	}
	
	public Map getParameterDisplayTexts( )
	{
		Map result = new HashMap( );
		Set entries = params.entrySet( );
		Iterator iterator = entries.iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			String name = (String) entry.getKey( );
			ParameterAttribute value = (ParameterAttribute) entry.getValue( );
			result.put( name, value.getDisplayText( ) );
		}
		return result;
	}

	public String getParameterDisplayText( String name )
	{
		Object parameter = params.get( name );
		if ( parameter != null )
		{
			return ( (ParameterAttribute) parameter ).getDisplayText( );
		}
		return null;		
	}
	
	public void setParameterDisplayText( String name, String displayText )
	{
		Object parameter = params.get( name );
		if ( parameter != null )
		{
			( (ParameterAttribute) parameter ).setDisplayText( displayText );
		}
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

	public void setReportContent( ReportContent content )
	{
		this.reportContent = content;
		content.setReportContext( reportContext );
		content.getErrors( ).addAll( onPrepareErrors );
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
		URL url = null;
		if ( reportDesign != null )
		{
			url = reportDesign.findResource( fileName,
					IResourceLocator.LIBRARY, appContext );
		}
		if (url == null)
		{
			log.log( Level.SEVERE, "loading external script file " + fileName
					+ " failed." );
			addException( new EngineException(
					MessageConstants.SCRIPT_FILE_LOAD_ERROR, fileName ) ); //$NON-NLS-1$
			return;
		}
		
		// read the script in the URL, and execution.
		try
		{

			InputStream in = url.openStream( );
			ByteArrayOutputStream out = new ByteArrayOutputStream( );
			byte[] buffer = new byte[1024];
			int size = in.read( buffer );
			while ( size != -1 )
			{
				out.write( buffer, 0, size );
				size = in.read( buffer );
			}
			byte[] script = out.toByteArray( );
			ScriptExpression scriptExpr = new ScriptExpression( new String( script, "UTF-8" ), fileName, 1 );
			execute( scriptExpr ); //$NON-NLS-1$
			in.close( );
		}
		catch ( IOException ex )
		{
			log.log( Level.SEVERE,
					"loading external script file " + fileName + " failed.", //$NON-NLS-1$ //$NON-NLS-2$
					ex );
			addException( new EngineException(
					MessageConstants.SCRIPT_FILE_LOAD_ERROR, url.toString( ),
					ex ) ); //$NON-NLS-1$
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
		return getScriptContext( ).getScope( );
	}

	public Scriptable getSharedScope( )
	{
		return getScriptContext( ).getSharedScope( );
	}

	public ScriptContext getScriptContext( )
	{
		if ( scriptContext == null )
		{
			initializeScriptContext( );
		}
		return this.scriptContext;
	}

	/**
	 * @return
	 */
	public IContent getContent( )
	{
		return content;
	}
	
	public void setContent(IContent content)
	{
		this.content = content;
	}

	public ReportItemDesign getItemDesign()
	{
		return design;
	}
	
	public void setItemDesign(ReportItemDesign design)
	{
		this.design = design;
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
		DesignElementHandle handle = getDesign( );
		if ( design != null )
		{
			handle = design.getHandle( );
		}
		addException( handle, ex );
	}

	protected HashMap elementExceptions = new HashMap( );
	
	public void addException( ReportElementDesign design, BirtException ex )
	{
		DesignElementHandle handle = null;
		if ( null != design )
		{
			handle = design.getHandle( );
		}
		addException( handle, ex );
	}

	public void addException( DesignElementHandle element, BirtException ex )
	{
		ElementExceptionInfo exInfo = (ElementExceptionInfo) elementExceptions
				.get( element );
		if ( exInfo == null )
		{
			exInfo = new ElementExceptionInfo( element );
			if ( reportContent != null )
			{
				if ( reportContent.getErrors( ).size( ) < ERROR_TOTAL_COUNT )
				{
					reportContent.getErrors( ).add( exInfo );
				}
			}
			else
			{
				if ( onPrepareErrors.size( ) < ERROR_TOTAL_COUNT )
				{
					onPrepareErrors.add( exInfo );
				}
			}
			if ( elementExceptions.size( ) < ERROR_TOTAL_COUNT )
			{
				elementExceptions.put( element, exInfo );
			}
		}
		EngineException engineEx = null;
		if ( ex instanceof EngineException )
		{
			engineEx = (EngineException) ex;
		}
		else
		{
			engineEx = new EngineException( ex );
		}
		if ( element != null )
			engineEx.setElementID( element.getID( ) );
		exInfo.addException( engineEx );
		
		if ( cancelOnError && task != null )
		{
			task.cancel( );
		}
	}

	/**
	 * report object is the script object used in the script context.
	 * 
	 * All infos can get from this object.
	 * 
	 * 
	 */
	public class ReportObject
	{

		/**
		 * get the report design handle
		 * 
		 * @return report design object.
		 */
		public Object getDesign( )
		{
			try
			{
				return getScriptContext( ).eval( "design" );
			}
			catch ( BirtException ex )
			{
				ExecutionContext.this.addException( ex );
			}
			return null;
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
			try
			{
				return getScriptContext( ).eval( "design.dataSets" );
			}
			catch ( BirtException ex )
			{
				ExecutionContext.this.addException( ex );
			}
			return null;
		}

		/**
		 * @return a set of data sources
		 */
		public Object getDataSources( )
		{
			try
			{
				return getScriptContext( ).eval( "design.dataSources" );
			}
			catch ( BirtException ex )
			{
				ExecutionContext.this.addException( ex );
			}
			return null;
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
	public ReportRunnable getRunnable( )
	{
		return runnable;
	}

	/**
	 * @param runnable
	 *            The runnable to set.
	 */
	public void setRunnable( IReportRunnable runnable )
	{
		this.runnable = (ReportRunnable)runnable;
		if (scriptContext != null)
		{
			scriptContext.setCompiledScripts( ((ReportRunnable)runnable).getScriptCache( ) );
			registerDesign( runnable );
		}
	}
	
	public void updateRunnable( IReportRunnable newRunnable )
	{
		if ( originalRunnable == null )
		{
			this.originalRunnable = this.runnable;
		}
		this.runnable = (ReportRunnable) newRunnable;
		if (scriptContext != null)
		{
			scriptContext.setCompiledScripts( ((ReportRunnable)runnable).getScriptCache( ) );
			registerDesign(runnable);
		}
		reportIR = null;
	}
	
	public ReportRunnable getOriginalRunnable()
	{
		if ( originalRunnable != null )
		{
			return originalRunnable;
		}
		return runnable;
	}
	

	private void registerDesign( IReportRunnable runnable )
	{
		ReportDesignHandle reportDesign = (ReportDesignHandle) runnable
			.getDesignHandle( );
		IDesignElement element = SimpleElementFactory.getInstance( ).getElement( reportDesign );
		scriptContext.registerBean( "design", element );
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
		String outputFormat = null;
		if ( renderOption != null )
		{
			outputFormat = renderOption.getOutputFormat( );
		}
		if ( outputFormat == null )
		{
			if ( isFixedLayout( ) )
			{
				outputFormat = IRenderOption.OUTPUT_FORMAT_PDF;
			}
			else
			{
				outputFormat = IRenderOption.OUTPUT_FORMAT_HTML;
			}
		}
		return outputFormat;
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
		if ( scriptContext != null)
		{
			scriptContext.registerBean( "reportContext", reportContext );
		}
	}

	public void setPageNumber( long pageNo )
	{
		pageNumber = pageNo;
		if ( scriptContext != null)
		{
			scriptContext.registerBean( "pageNumber", new Long( pageNumber ) );
		}
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
		if ( totalPage > this.totalPage )
		{
			this.totalPage = totalPage;
			if ( scriptContext != null )
			{
				scriptContext.registerBean( "totalPage", new Long( totalPage ) );
			}
			if ( reportContent instanceof ReportContent )
			{
				( (ReportContent) reportContent ).setTotalPage( totalPage );
			}
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

	public void setFilteredPageNumber( long pageNo )
	{
		filteredPageNumber = pageNo;
	}

	public void setFilteredTotalPage( long totalPage )
	{
		filteredTotalPage = totalPage;
	}

	public long getFilteredPageNumber( )
	{
		if ( filteredPageNumber <= 0 )
		{
			return pageNumber;
		}
		return filteredPageNumber;
	}

	public long getFilteredTotalPage( )
	{
		if ( filteredTotalPage <= 0 )
		{
			return totalPage;
		}
		return filteredTotalPage;
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
	
	public boolean getFactoryMode( )
	{
		return this.factoryMode;
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
	public StringFormatter getStringFormatter( String pattern )
	{
		return getStringFormatter( pattern, ULocale.forLocale( locale ) );
	}

	public StringFormatter getStringFormatter( String pattern, ULocale locale )
	{
		if ( locale == null )
		{
			locale = ULocale.forLocale( this.locale );
		}
		String key = pattern + ":" + locale;
		StringFormatter fmt = (StringFormatter) stringFormatters.get( key );
		if ( fmt == null )
		{
			fmt = new StringFormatter( pattern, locale );
			stringFormatters.put( key, fmt );
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
	public NumberFormatter getNumberFormatter( String pattern )
	{
		return getNumberFormatter( pattern, ULocale.forLocale( locale ) );
	}

	public NumberFormatter getNumberFormatter( String pattern, ULocale locale )
	{
		if ( locale == null )
		{
			locale = ULocale.forLocale( this.locale );
		}
		String key = pattern + ":" + locale;
		NumberFormatter fmt = (NumberFormatter) numberFormatters.get( key );
		if ( fmt == null )
		{
			fmt = new NumberFormatter( pattern, locale );
			numberFormatters.put( key, fmt );
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
	public DateFormatter getDateFormatter( String pattern )
	{
		return getDateFormatter( pattern, ULocale.forLocale( locale ) );
	}
	
	public DateFormatter getDateFormatter( String pattern, ULocale locale )
	{
		if ( locale == null )
		{
			locale = ULocale.forLocale( this.locale );
		}
		String key = pattern + ":" + locale;
		DateFormatter fmt = (DateFormatter) dateFormatters.get( key );
		if ( fmt == null )
		{
			fmt = new DateFormatter( pattern, locale, timeZone );
			dateFormatters.put( key, fmt );
		}

		return fmt;
	}

	/**
	 * set the executor used in the execution context
	 * 
	 * @param executor
	 */
	public void setExecutor( IReportExecutor executor )
	{
		this.executor = executor;
	}

	/**
	 * get the executor used to execute the report
	 * 
	 * @return report executor
	 */
	public IReportExecutor getExecutor( )
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
		return renderOption.getActionHandler( );
	}
	
	/**
	 * @return Returns the action handler.
	 */
	public IHTMLImageHandler getImageHandler( )
	{
		return renderOption.getImageHandler( );
	}
	

	/**
	 * return application class loader.
	 * The application class loader is used to load the report item event handle and 
	 * java classes called in the javascript.
	 * @return class loader
	 */
	public ClassLoader getApplicationClassLoader( )
	{
		if ( applicationClassLoader == null )
		{
			closeClassLoader = true;
			applicationClassLoader = new ApplicationClassLoader( engine,
					runnable, appContext );
			if ( scriptContext != null )
			{
				scriptContext
						.setApplicationClassLoader( applicationClassLoader );
			}
		}
		return applicationClassLoader;
	}

	public void setApplicationClassLoader( ClassLoader classLoader )
	{
		if ( classLoader == null )
		{
			throw new NullPointerException( "null classloader" );
		}
		if ( closeClassLoader
				&& applicationClassLoader instanceof ApplicationClassLoader )
		{
			( (ApplicationClassLoader) applicationClassLoader ).close( );
		}
		closeClassLoader = false;
		this.applicationClassLoader = classLoader;
		if ( scriptContext != null )
		{
			scriptContext.setApplicationClassLoader( applicationClassLoader );
		}
	}
	
	/**
	 * Set the cancel flag.
	 */
	public void cancel( )
	{
		isCancelled = true;
		// cancel the dte's session
		if ( dataEngine != null )
		{
			DataRequestSession session = dataEngine.getDTESession( );
			if ( session != null )
			{
				session.cancel( );
			}
		}
	}

	public boolean isCanceled( )
	{
		return isCancelled;
	}
	
	public void setCancelOnError( boolean cancel )
	{
		cancelOnError = cancel;
	}
	
	public void setDataSource( DocumentDataSource dataSource )
			throws IOException
	{
		this.dataSource = dataSource;
		this.dataSource.open( );
	}
	
	public DocumentDataSource getDataSource()
	{
		return dataSource;
	}
	
	public IBaseResultSet executeQuery( IBaseResultSet parent,
			IDataQueryDefinition query, Object queryOwner, boolean useCache ) throws BirtException
	{
		IDataEngine dataEngine = getDataEngine( );
		return dataEngine.execute( parent, query, queryOwner, useCache);
	}

	public IBaseResultSet getResultSet( )
	{
		if ( rsets != null )
		{
			return rsets[0];
		}
		return null;
	}
	
	public void setResultSet( IBaseResultSet rset )
	{
		if ( rset != null )
		{
			if ( rsets != null && rsets.length == 1 && rsets[0] == rset )
			{
				return;
			}
			setResultSets( new IBaseResultSet[]{rset} );
		}
		else
		{
			setResultSets( null );
		}
	}

	public IBaseResultSet[] getResultSets( )
	{
		return rsets;
	}

	public void setResultSets( IBaseResultSet[] rsets )
	{
		if ( this.rsets == rsets )
		{
			return;
		}
		if ( rsets != null )
		{
			this.rsets = rsets;
			if ( rsets[0] != null )
			{
				Scriptable scope = getScriptContext( ).getRootScope( );
				DataAdapterUtil.registerJSObject( scope,
						new ResultIteratorTree( rsets[0] ), this.getScriptContext( ) );
			}
		}
		else
		{
			this.rsets = null;
			// FIXME: we should also remove the JSObject from scope
			// Scriptable scope = scriptContext.getRootScope( );
			// DataAdapterUtil.registerJSObject( scope,
			// new ResultIteratorTree( rsets[0] ) );
		}
	}
	
	private class ResultIteratorTree implements ILinkedResult
	{

		IBaseResultSet currentRset;
		int resultType = -1;

		public ResultIteratorTree( IBaseResultSet rset )
		{
			this.currentRset = rset;
			if ( rset instanceof IQueryResultSet )
			{
				resultType = ILinkedResult.TYPE_TABLE;
			}
			else if ( rset instanceof ICubeResultSet )
			{
				resultType = ILinkedResult.TYPE_CUBE;
			}
		}

		public ILinkedResult getParent( )
		{
			return new ResultIteratorTree( currentRset.getParent( ) );
		}

		public Object getCurrentResult( )
		{
			if ( resultType == ILinkedResult.TYPE_TABLE )
			{
				return ( (IQueryResultSet) currentRset ).getResultIterator( );
			}
			else if ( resultType == ILinkedResult.TYPE_CUBE )
			{
				return ( (ICubeResultSet) currentRset ).getCubeCursor( );
			}
			return null;
		}

		public int getCurrentResultType( )
		{
			return resultType;
		}
	}

	public boolean hasErrors( )
	{
		return !elementExceptions.isEmpty( );
	}
	/**
	 * Returns list or errors, the max count of the errors is
	 * <code>MAX_ERRORS</code>
	 * 
	 * @return error list which has max error size limited to
	 *         <code>MAX_ERRORS</code>
	 */
	public List getErrors( )
	{
		List errors = this.getAllErrors( );
		if ( errors.size( ) > MAX_ERRORS )
		{
			errors = errors.subList( 0, MAX_ERRORS - 1 );
		}
		return errors;
	}
	
	/**
	 * Returns all errors.
	 * 
	 * @return list of all the errors.
	 */
	public List getAllErrors( )
	{
		List errors = new ArrayList( );
		Iterator entries = elementExceptions.entrySet( ).iterator( );
		while( entries.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) entries.next( );
			List elementExceptions = ( (ElementExceptionInfo) entry.getValue( ) )
					.getErrorList( );
			errors.addAll( elementExceptions );
		}
		return errors;
	}

	/**
	 * @return the mAX_ERRORS
	 */
	public int getMaxErrors( )
	{
		return MAX_ERRORS;
	}
	
	/**
	 * @param max_errors the mAX_ERRORS to set
	 */
	public void setMaxErrors( int maxErrors )
	{
		MAX_ERRORS = maxErrors;
	}
	
	/**
	 *  to remember the current report item is in master page or not.
	 */
	boolean isExecutingMasterPage = false;
	
	/**
	 * Since the data set in master page will be executed in each page
	 * and while the data set in report body will only be executed once,
	 * we need to remember the current report item is in master page or not.
	 * This will be used to help store the executed resultSetID and load it
	 * to distinguish them.
	 */
	public void setExecutingMasterPage( boolean isExecutingMasterPage )
	{
		this.isExecutingMasterPage = isExecutingMasterPage;
	}
	
	public boolean isExecutingMasterPage( )
	{
		return isExecutingMasterPage;
	}
	
	/**
	 * Add a page break listener.
	 * 
	 * @param listener
	 *            the page break listener.
	 */
	public void addPageBreakListener( IPageBreakListener listener )
	{
		if ( pageBreakListeners == null )
		{
			pageBreakListeners = new ArrayList( );
		}
		pageBreakListeners.add( listener );
	}
	
	/**
	 * Notify page break listeners that page is broken.
	 */
	public void firePageBreakEvent( )
	{
		if ( pageBreakListeners != null )
		{
			for ( int i = 0; i < pageBreakListeners.size( ); i++ )
			{
				( (IPageBreakListener) pageBreakListeners.get( i ) )
						.onPageBreak( );
			}
		}
	}
	
	/**
	 * Remove a page break listener.
	 * 
	 * @param listener
	 *            the page break listener.
	 */
	public void removePageBreakListener( IPageBreakListener listener )
	{
		if ( pageBreakListeners != null )
		{
			pageBreakListeners.remove( listener );
		}
	}
	
	public IEngineTask getEngineTask()
	{
		return task;
	}
	
	public Logger getLogger( )
	{
		return log;
	}
	
	public void setLogger( Logger logger )
	{
		log = logger;
	}
	
	protected ExecutionPolicy executionPolicy;

	public void optimizeExecution( )
	{
		if ( ( task != null ) && ( task.getTaskType( ) == IEngineTask.TASK_RUN )
				&& !isFixedLayout )
		{
			String[] engineExts = getEngineExtensions( );
			if ( engineExts == null || engineExts.length == 0 )
			{
				executionPolicy = new ExecutionOptimize( )
						.optimize( getReport( ) );
			}
		}
	}

	public ExecutionPolicy getExecutionPolicy( )
	{
		return executionPolicy;
	}
	
	public Report getReport( )
	{
		if ( reportIR != null )
		{
			return reportIR;
		}
		if ( runnable != null )
		{
			reportIR = new ReportParser( ).parse( (ReportDesignHandle) runnable
					.getDesignHandle( ) );
		}
		return reportIR;
	}
	
	public void setReport( Report reportIR )
	{
		this.reportIR = reportIR;
	}
	
	public URL getResource( String resourceName )
	{
		if ( getDesign( ) != null )
		{
			return getDesign( ).findResource( resourceName,
					IResourceLocator.OTHERS, appContext );
		}
		return null;
	}
	
	public ExtendedItemManager getExtendedItemManager( )
	{
		return extendedItemManager;
	}
	
	public EngineExtensionManager getEngineExtensionManager( )
	{
		return engineExtensionManager;
	}
	
	public void setMaxRowsPerQuery( int maxRows )
	{
		if ( maxRows >= 0 )
		{
			maxRowsPerQuery = maxRows;
		}
	}

	public int getMaxRowsPerQuery( )
	{
		return maxRowsPerQuery;
	}

	private String[] engineExts;

	public String[] getEngineExtensions( )
	{
		if ( engineExts != null )
		{
			return engineExts;
		}

		ArrayList<String> resultList = new ArrayList<String>( );
		ReportDesignHandle design = this.getDesign( );
		if ( design.isEnableACL( ) )
		{
			resultList.add( "PLS" );
		}

		String engineExtensions = (String) design
				.getProperty( "Engine extensions" );

		if ( engineExtensions != null )
		{
			String[] exts = engineExtensions.split( "," );
			for ( String ext : exts )
			{
				if ( ext != null )
				{
					ext = ext.trim( );
					if ( ext.length( ) > 0 )
					{
						resultList.add( ext );
					}
				}
			}
		}
		engineExts = resultList.toArray( new String[resultList.size( )] );

		return engineExts;
	}
	
	private boolean enableProgreesiveViewing = true;

	public void enableProgressiveViewing( boolean enabled )
	{
		enableProgreesiveViewing = enabled;
	}

	public boolean isProgressiveViewingEnable( )
	{
		return enableProgreesiveViewing;
	}

	public EventHandlerManager getEventHandlerManager( )
	{
		return eventHandlerManager;
	}
	
	public void setProgressMonitor( IProgressMonitor monitor )
	{
		progressMonitor = new ProgressMonitorProxy( monitor );
	}

	public IProgressMonitor getProgressMonitor( )
	{
		if ( progressMonitor == null )
		{
			progressMonitor = new ProgressMonitorProxy( null );
		}
		return progressMonitor;
	}

	public boolean needOutputResultSet( )
	{
		return needOutputResultSet;
	}

	public void setNeedOutputResultSet( boolean needOutputResultSet )
	{
		this.needOutputResultSet = needOutputResultSet;
	}

	public Object getPageVariable( String name )
	{
		if ( "totalPage".equals( name ) )
		{
			return Long.valueOf( totalPage );
		}
		if ( "pageNumber".equals( name ) )
		{
			return Long.valueOf( totalPage );
		}
		PageVariable var = pageVariables.get( name );
		if ( var != null )
		{
			return var.getValue( );
		}
		return null;
	}

	public void setPageVariable( String name, Object value )
	{
		PageVariable var = pageVariables.get( name );
		if ( var != null )
		{
			var.setValue( value );
		}
	}

	public void addPageVariables( Collection<PageVariable> vars )
	{
		for ( PageVariable var : vars )
		{
			pageVariables.put( var.getName( ), var );
		}
	}

	public Collection<PageVariable> getPageVariables( )
	{
		return pageVariables.values( );
	}

	public void addPageVariable( PageVariable var )
	{
		pageVariables.put( var.getName( ), var );
	}
	
	public boolean isFixedLayout( )
	{
		return isFixedLayout;
	}

	public void setFixedLayout( boolean isFixedLayout )
	{
		this.isFixedLayout = isFixedLayout;
	}
	
	public int getTaskType( )
	{
		return task.getTaskType( );
	}
}