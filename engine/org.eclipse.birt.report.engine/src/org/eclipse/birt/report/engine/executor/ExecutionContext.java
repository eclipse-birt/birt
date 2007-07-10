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
import java.io.IOException;
import java.io.InputStream;
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
import java.util.Set;
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
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.ILinkedResult;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.impl.ParameterAttribute;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.DataEngineFactory;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.toc.TOCBuilder;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

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
	public static final String PROPERTYSEPARATOR = File.pathSeparator;

	// engines used to create the context
	/** the engine used to create this context */
	private IReportEngine engine;

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
	private ReportRunnable runnable;

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
	
	private int MAX_ERRORS = 100;
	/**
	 * 
	 */
	private IDocArchiveReader dataSource;
	
	/**
	 * All page break listeners.
	 */
	private List pageBreakListeners;
	
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
			engine = task.getEngine( );
			log = task.getLogger( );
		}
		else
		{
			log = Logger.getLogger( ExecutionContext.class.getName( ) );
		}

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
		
		Context context = scriptContext.getContext( );
		try
		{
			context.setSecurityController( ScriptUtil
					.createSecurityController( ) );
		}
		catch ( Throwable throwable )
		{
		}
		context.setLocale( locale );

		initailizeScriptContext( context, scriptContext
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
		scriptContext
				.eval( "function registerGlobal( name, value) { _jsContext.registerGlobalBean(name, value); }" );
		scriptContext
				.eval( "function unregisterGlobal(name) { _jsContext.unregisterGlobalBean(name); }" );
		
		applicationClassLoader = new ApplicationClassLoader( this );
		context.setApplicationClassLoader(
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
			catch ( Throwable e )
			{
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
		this.scriptContext.getContext( ).setLocale( locale );
	}

	/**
	 * @return Returns the report.
	 */
	public Report getReport( )
	{
		if ( runnable != null )
		{
			return runnable.getReportIR( );
		}
		return null;
	}

	public void openDataEngine( )
	{
		if ( dataEngine == null )
		{
			try
			{
				dataEngine = DataEngineFactory.getInstance( ).createDataEngine(
						this );
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

	public void setReportContent( IReportContent content )
	{
		this.reportContent = content;
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
					IResourceLocator.LIBRARY );
		}
		if (url == null)
		{
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
			execute( new String( script, "UTF-8" ), fileName, 1 ); //$NON-NLS-1$
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
		if ( renderOption != null )
		{
			return renderOption.getOutputFormat( );
		}
		return IRenderOption.OUTPUT_FORMAT_HTML;
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
		if ( totalPage > this.totalPage )
		{
			this.totalPage = totalPage;
			scriptContext.registerBean( "totalPage", new Long( totalPage ) );
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
	public StringFormatter getStringFormatter( String value )
	{
		StringFormatter fmt = (StringFormatter) stringFormatters.get( value );
		if ( fmt == null )
		{
			fmt = new StringFormatter( value, ULocale.forLocale(locale) );
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
			fmt = new NumberFormatter( value, ULocale.forLocale(locale) );
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
		DateFormatter fmt = null;
		if ( value != null )
		{
			fmt = (DateFormatter) dateFormatters.get( value );
			if ( fmt == null )
			{
				fmt = new DateFormatter( value, ULocale.forLocale( locale ) );
				dateFormatters.put( value, fmt );
			}
			return fmt;
		}
		return new DateFormatter( value, ULocale.forLocale( locale ) );
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
		return applicationClassLoader;
	}

	/**
	 * the application class loader.
	 * 
	 * The class loader first try to the load the class as following sequence:
	 * <li>1. standard java class loader,
	 * <li>2. classloader setted through the appContext.
	 * <li>3. CLASSPATH setted by WEBAPP_CLASSPATH_KEY
	 * <li>4. PROJECT_CLASSPATH_KEY
	 * <li>5. WORKSAPCE_CLASSPATH_KEY
	 * <li>6. JARs define in the report design
	 */
	static private class ApplicationClassLoader extends ClassLoader
	{

		private static String[] classPathes = new String[]{
				EngineConstants.WEBAPP_CLASSPATH_KEY,
				EngineConstants.PROJECT_CLASSPATH_KEY,
				EngineConstants.WORKSPACE_CLASSPATH_KEY};

		private ExecutionContext context = null;
		private ClassLoader loader = null;

		public ApplicationClassLoader( ExecutionContext context )
		{
			this.context = context;
		}

		public Class loadClass( String className )
				throws ClassNotFoundException

		{
			try
			{
				return Class.forName( className );
			}
			catch ( ClassNotFoundException ex )
			{
				if ( loader == null )
				{
					createWrappedClassLoaders( );
				}
				return loader.loadClass( className );
			}
		}

		public URL getResource( String name )
		{
			URL url = ApplicationClassLoader.class.getClassLoader( )
					.getResource( name );
			if ( url == null )
			{
				if ( loader == null )
				{
					createWrappedClassLoaders( );
				}
				return loader.getResource( name );
			}
			return null;
		}

		protected void createWrappedClassLoaders( )
		{
			ClassLoader root = getAppClassLoader( );
			if ( root == null )
			{
				root = ExecutionContext.class.getClassLoader( );
			}
			loader = createClassLoaderFromProperty( root );
			loader = createClassLoaderFromDesign( loader );
		}

		protected ClassLoader createClassLoaderFromProperty( ClassLoader parent )
		{
			EngineConfig config = context.getEngine( ).getConfig( );
			ArrayList urls = new ArrayList( );
			for ( int i = 0; i < classPathes.length; i++ )
			{
				String classPath = null;
				if ( config != null )
				{
					Object propValue = config.getProperty( classPathes[i] );
					if ( propValue instanceof String )
					{
						classPath = (String) propValue;
					}
				}

				if ( classPath == null )
				{
					classPath = System.getProperty( classPathes[i] );
				}

				if ( classPath != null && classPath.length( ) != 0 )
				{
					String[] jars = classPath.split( PROPERTYSEPARATOR, -1 );
					if ( jars != null && jars.length != 0 )
					{
						for ( int j = 0; j < jars.length; j++ )
						{
							File file = new File( jars[j] );
							try
							{
								urls.add( file.toURL( ) );
							}
							catch ( MalformedURLException e )
							{
								e.printStackTrace( );
							}
						}
					}
				}
			}
			if ( urls.size( ) != 0 )
			{
				return new URLClassLoader( (URL[]) urls.toArray( new URL[0] ),
						parent );
			}
			return parent;
		}

		protected ClassLoader getAppClassLoader( )
		{
			Map appContext = context.getAppContext( );
			if ( appContext != null )
			{
				Object appLoader = appContext
						.get( EngineConstants.APPCONTEXT_CLASSLOADER_KEY );
				if ( appLoader instanceof ClassLoader )
				{
					return (ClassLoader) appLoader;
				}
			}
			return null;
		}

		protected ClassLoader createClassLoaderFromDesign( ClassLoader parent )
		{
			IReportRunnable runnable = context.getRunnable( );
			if ( runnable != null )
			{
				ModuleHandle module = (ModuleHandle) runnable.getDesignHandle( );
				ArrayList urls = new ArrayList( );
				Iterator iter = module.scriptLibsIterator( );
				while ( iter.hasNext( ) )
				{
					ScriptLibHandle lib = (ScriptLibHandle) iter.next( );
					String libPath = lib.getName( );
					URL url = module.findResource( libPath,
							IResourceLocator.LIBRARY );
					if ( url != null )
					{
						urls.add( url );
					}
				}
				if ( urls.size( ) != 0 )
				{
					URL[] jarUrls = (URL[]) urls.toArray( new URL[]{} );
					return new URLClassLoader( jarUrls, parent );
				}
			}
			return parent;
		}
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
	
	public void setCancelOnError( boolean cancel )
	{
		cancelOnError = cancel;
	}
	
	public void setDataSource( IDocArchiveReader dataSource )
			throws IOException
	{
		dataSource.open( );
		this.dataSource = dataSource;
	}
	
	public IDocArchiveReader getDataSource()
	{
		return dataSource;
	}
	
	public IBaseResultSet executeQuery( IBaseResultSet parent,
			IDataQueryDefinition query, boolean useCache ) throws BirtException
	{
		IDataEngine dataEngine = getDataEngine( );
		return dataEngine.execute( parent, query, useCache);
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
		if ( rsets != null && rsets.length == 1 && rsets[0] == rset )
		{
			return;
		}
		setResultSets( new IBaseResultSet[]{rset} );
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
		this.rsets = rsets;
		if ( rsets[0] != null )
		{
			Scriptable scope = scriptContext.getRootScope( );
			DataAdapterUtil.registerJSObject( scope, new ResultIteratorTree(
					rsets[0] ) );
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
}