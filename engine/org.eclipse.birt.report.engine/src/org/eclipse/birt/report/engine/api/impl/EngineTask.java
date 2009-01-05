/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.RAInputStream;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.script.ParameterAttribute;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineConfig;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.UnsupportedFormatException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExtensionExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.extension.engine.IGenerateExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.v3.ReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.engine.util.SecurityUtil;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Defines an engine task that could be executed, debugged (runs step by step),
 * inform caller for progress, etc.
 */
public abstract class EngineTask implements IEngineTask
{
	public final static String TASK_TYPE = "task_type";
	private static int id = 0;

	protected String pagination;

	protected final static String FORMAT_HTML = "html";
	/**
	 * is cancel called
	 */
	protected boolean cancelFlag;
	protected int runningStatus;

	/**
	 * a reference to the report engine
	 */
	protected ReportEngine engine;

	/**
	 * logger used to output the message
	 */
	protected Logger log;

	/**
	 * Comment for <code>locale</code>
	 */
	protected Locale locale = Locale.getDefault( );
	
	/**
	 * define a time zone, and set a default value 
	 */
	protected TimeZone timeZone = TimeZone.getDefault( );

	/**
	 * the execution context
	 */
	protected ExecutionContext executionContext;

	/**
	 * task identifier. Could be used for logging
	 */
	protected int taskID;


	/**
	 * options used to render the report design.
	 */
	protected IRenderOption renderOptions;
	/**
	 * emitter id
	 */
	protected String emitterID;
	
	protected String format;

	/**
	 * does the parameter has been changed by the user.
	 */
	protected boolean parameterChanged = true;
	/**
	 * The parameter values that the caller has set explicitly
	 */
	protected HashMap inputValues = new HashMap( );

	/**
	 * The parameter values that will be used to run the report. It is a merged
	 * map between the input value and the default values.
	 */
	protected HashMap runValues = new HashMap( );

	/**
	 * Engine task type. for usage in BIRT scripting.
	 */
	protected int taskType = IEngineTask.TASK_UNKNOWN;
	
	private ClassLoader contextClassLoader;

	/**
	 * @param engine
	 *            reference to report engine
	 * @param appContext
	 *            a user-defined object that capsulates the context for running
	 *            a task. The context object is passed to callback functions
	 *            (i.e., functions in image handlers, action handlers, etc. )
	 *            that are written by those who embeds engine in their
	 *            applications
	 */
	protected EngineTask( ReportEngine engine, IReportRunnable runnable,
			int taskType )
	{
		this( engine, taskType );
		setReportRunnable( runnable );
	}
	
	protected EngineTask(ReportEngine engine, int taskType)
	{
		this.engine = engine;
		this.taskType = taskType;
		taskID = id++;
		this.log = engine.getLogger( );

		// create execution context used by java-script
		executionContext = new ExecutionContext( this );
		// Create IReportContext used by java-based script
		executionContext.setReportContext( new ReportContextImpl(
				executionContext ) );
		// set the default app context
		setAppContext( null );

		cancelFlag = false;
		runningStatus = STATUS_NOT_STARTED;
	}
	
	protected IReportRunnable getOnPreparedRunnable( IReportDocument doc )
	{
		IInternalReportDocument internalReportDoc = (IInternalReportDocument) doc;
		return internalReportDoc.getOnPreparedRunnable( );
	}


	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale( )
	{
		return locale;
	}

	/**
	 * @return Returns the ulocale.
	 */
	public ULocale getULocale( )
	{
		return ULocale.forLocale( locale );
	}

	/**
	 * sets the task locale
	 * 
	 * The locale must be called in the same thread which create the engine task
	 * 
	 * @param locale
	 *            the task locale
	 */
	public void setLocale( Locale locale )
	{
		log.log( Level.FINE, "EngineTask.setLocale: locale={0}", locale == null
				? null
				: locale.getDisplayName( ) );
		doSetLocale( locale );
	}

	private void doSetLocale( Locale locale )
	{
		this.locale = locale;
		executionContext.setLocale( locale );
		EngineException.setULocale( ULocale.forLocale( locale ) );
	}

	/**
	 * sets the task locale
	 * 
	 * @param locale
	 *            the task locale
	 */
	public void setLocale( ULocale uLocale )
	{
		log.log( Level.FINE, "EngineTask.setLocale: uLocale={0}",
				uLocale == null ? null : uLocale.getDisplayName( ) );
		doSetLocale( uLocale.toLocale( ) );
	}
	
	public void setTimeZone( TimeZone timeZone )
	{
		this.timeZone = timeZone;
		executionContext.setTimeZone( timeZone );
	}
	
	public TimeZone getTimeZone( )
	{
		return timeZone;
	}

	/**
	 * sets the task context
	 * 
	 * @param context
	 *            the task context
	 */
	public void setAppContext( Map context )
	{
		HashMap appContext = new HashMap( );
		HashMap sysAppContext = engine.getConfig( ).getAppContext( );
		if ( sysAppContext != null )
		{
			appContext.putAll( sysAppContext );
		}
		addAppContext( context, appContext );
		
		executionContext.setAppContext( appContext );

		StringBuffer logStr = null;
		if ( log.isLoggable( Level.FINE ) )
			logStr = new StringBuffer( );

		// add the contexts into ScriptableJavaObject
		if ( !appContext.isEmpty( ) )
		{
			Set entries = appContext.entrySet( );
			for ( Iterator iter = entries.iterator( ); iter.hasNext( ); )
			{
				Map.Entry entry = (Map.Entry) iter.next( );

				if ( entry.getKey( ) instanceof String )
				{
					executionContext.registerBean( (String) entry.getKey( ),
							entry.getValue( ) );
					if ( logStr != null )
					{
						logStr.append( entry.getKey( ) );
						logStr.append( "=" );
						logStr.append( entry.getValue( ) );
						logStr.append( ";" );
					}
				}
				else
				{
					log
							.log(
									Level.WARNING,
									"Map entry {0} is invalid and ignored, because its key is a not string.", //$NON-NLS-1$ 
									entry.getKey( ).toString( ) );
				}
			}
		}

		if ( logStr != null )
			log.log( Level.FINE, "EngineTask.setAppContext: context={0}",
					logStr );
	}

	/**
	 * Merges user specified app context to that of EngineTask. The context
	 * variables in entry with following keys will be ignored:
	 * 
	 * <ul>
	 * <li><code>EngineConstants.APPCONTEXT_CLASSLOADER_KEY</code>
	 * <li><code>EngineConstants.WEBAPP_CLASSPATH_KEY</code>
	 * <li><code>EngineConstants.PROJECT_CLASSPATH_KEY</code>
	 * <li><code>EngineConstants.WORKSPACE_CLASSPATH_KEY</code>
	 * </ul>
	 * 
	 * @param from
	 *            the source app contexts.
	 * @param to
	 *            the destination app contexts.
	 */
	private void addAppContext( Map from, Map to )
	{
		if ( from == null || to == null )
		{
			return;
		}
		Iterator iterator = from.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			//Ignore the entry that should not be set from engine task.
			if ( !isDeprecatedEntry( entry ) )
			{
				to.put( entry.getKey( ), entry.getValue( ) );
			}
		}
	}
	
	private boolean isDeprecatedEntry( Map.Entry entry )
	{
		Object key = entry.getKey( );
		if ( EngineConstants.APPCONTEXT_CLASSLOADER_KEY.equals( key )
				|| EngineConstants.WEBAPP_CLASSPATH_KEY.equals( key )
				|| EngineConstants.PROJECT_CLASSPATH_KEY.equals( key )
				|| EngineConstants.WORKSPACE_CLASSPATH_KEY.equals( key ) )
		{
			if ( entry.getValue( ) != getAppContext( ).get( key ) )
			{
				log
						.log(
								Level.WARNING,
								key
										+ " could not be set in appContext of IEngineTask, please set it in appContext of IReportEngine" );
				return true;
			}
		}
		return false;
	}

	/**
	 * returns the object that encapsulates the context for running the task
	 * 
	 * @return Returns the context.
	 */
	public Map getAppContext( )
	{
		return executionContext.getAppContext( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getEngine()
	 */
	public IReportEngine getEngine( )
	{
		return engine;
	}

	public void setReportRunnable( IReportRunnable runnable )
	{
		if ( runnable != null )
		{
			Properties systemProperties = SecurityUtil.getSystemProperties( );
			executionContext.setRunnable( runnable );
			// register the properties into the scope, so the user can
			// access the config through the property name directly.
			executionContext.registerBeans( systemProperties );
			executionContext.registerBeans( runnable.getTestConfig( ) );
			// put the properties into the configs also, so the user can
			// access the config through config["name"].
			executionContext.getConfigs( ).putAll( systemProperties );
			executionContext.getConfigs( ).putAll( runnable.getTestConfig( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable( )
	{
		return executionContext.getOriginalRunnable( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setRenderOption(org.eclipse.birt.report.engine.api.IRenderOption)
	 */
	public void setRenderOption( IRenderOption options )
	{
		if ( options == null )
		{
			throw new NullPointerException( "options can't be null" );
		}
		renderOptions = options;
	}

	public IRenderOption getRenderOption( )
	{
		return renderOptions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setEmitterID(java.lang.String)
	 */
	/**
	 * @deprecated
	 */
	public void setEmitterID( String id )
	{
		this.emitterID = id;
	}

	/**
	 * @deprecated
	 * @return the emitter ID to be used to render this report. Could be null,
	 *         in which case the engine will choose one emitter that matches the
	 *         requested output format.
	 */
	public String getEmitterID( )
	{
		return this.emitterID;
	}

	public DataRequestSession getDataSession( )
	{
		return executionContext.getDataEngine( ).getDTESession( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#addScriptableJavaObject(java.lang.String,
	 *      java.lang.Object)
	 */
	public void addScriptableJavaObject( String jsName, Object obj )
	{
		executionContext.registerBean( jsName, obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getID()
	 */
	public int getID( )
	{
		return taskID;
	}

	protected Object convertToType( Object value, String type )
	{
		try
		{
			return convertParameterType( value, type );
		}
		catch ( BirtException e )
		{
			log.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
		return null;
	}

	protected Object convertToType( Object value, String type, String paramType )
	{
		value = convertToType( value, type);
		if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE
				.equals( paramType ) )
		{
			if ( value != null )
			{
				value = new Object[]{value};
			}
		}
		return value;
	}
	
	public static Object convertParameterType( Object value, String type )
			throws BirtException
	{
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			return DataTypeUtil.toBoolean( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			return DataTypeUtil.toDate( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
		{
			return DataTypeUtil.toSqlDate( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
		{
			return DataTypeUtil.toSqlTime( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
		{
			return DataTypeUtil.toBigDecimal( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			return DataTypeUtil.toDouble( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			return DataTypeUtil.toString( value );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
		{
			return DataTypeUtil.toInteger( value );
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#validateParameters()
	 */
	public boolean validateParameters( )
	{
		IReportRunnable runnable = executionContext.getRunnable( );
		if ( runnable == null )
		{
			return false;
		}

		// set the parameter values into the execution context
		try
		{
			doValidateParameters( );
			return true;
		}
		catch(ParameterValidationException ex)
		{
			log.log( Level.SEVERE, ex.getMessage( ), ex );
		}
		return false;
	}
	
	protected boolean doValidateParameters() throws ParameterValidationException
	{
		// set the parameter values into the execution context
		usingParameterValues( );

		if ( log.isLoggable( Level.FINE ) )
		{
			loggerParamters( );
		}
		// validate each parameter to see if it is validate
		ParameterValidationVisitor pv = new ParameterValidationVisitor( );
		boolean result = pv.visit( executionContext.getDesign( ), null );
		if ( pv.engineException != null )
		{
			throw pv.engineException;
		}
		return result;
	}
	
	private class ParameterValidationVisitor extends ParameterVisitor
	{
		ParameterValidationException engineException;
		boolean visitScalarParameter( ScalarParameterHandle param,
				Object value )
		{
			try
			{
				return validateScalarParameter( param );
			}
			catch ( ParameterValidationException pe )
			{
				engineException = pe;
			}
			return false;
		}

		boolean visitParameterGroup( ParameterGroupHandle group,
				Object value )
		{
			return visitParametersInGroup( group, value );
		}
	};

	protected void loggerParamters( )
	{
		if ( log.isLoggable( Level.FINE ) )
		{
			final StringBuffer buffer = new StringBuffer( );
			// validate each parameter to see if it is validate
			new ParameterVisitor( ) {

				boolean visitScalarParameter( ScalarParameterHandle param,
						Object value )
				{
					String paramName = param.getName( );
					Object paramValue = runValues.get( paramName );
					buffer.append( paramName );
					buffer.append( ":" );
					buffer.append( paramValue );
					buffer.append( "\n" );
					return true;
				}

				boolean visitParameterGroup( ParameterGroupHandle group,
						Object value )
				{
					return visitParametersInGroup( group, value );
				}
			}.visit( executionContext.getDesign( ), null );
			log.log( Level.FINE, "Running the report with paramters: {0}",
					buffer );
		}
	}

	/**
	 * validate whether the parameter value is a valid value for the parameter
	 * 
	 * @param p
	 *            the parameter to be verified
	 * @param paramValue
	 *            the value for the parameter
	 * @return true if the given parameter value is valid; false otherwise
	 */
	private boolean validateScalarParameter( ScalarParameterHandle paramHandle )
			throws ParameterValidationException
	{

		String paramName = paramHandle.getName( );
		Object paramValue = runValues.get( paramName );
		String type = paramHandle.getDataType( );

		// Handle null parameter values
		if ( paramValue == null )
		{
			if ( !paramHandle.isRequired( ) )
				return true;

			throw new ParameterValidationException(
					MessageConstants.PARAMETER_IS_NULL_EXCEPTION,
					new String[]{paramName} );
		}

		String source = paramHandle.getValidate();
		if (source != null && source.length() != 0) {
			try
			{
				Object result = executionContext.evaluate( source );
				if ( !( result instanceof Boolean )
						|| !( (Boolean) result ).booleanValue( ) )
				{
					throw new ParameterValidationException(
							MessageConstants.PARAMETER_VALIDATION_FAILURE,
							new String[]{paramName} );
				}
			}
			catch ( ParameterValidationException pve )
			{
				throw pve;
			}
			catch ( BirtException ex )
			{
				throw new ParameterValidationException( ex );
			}
		}
		
		String paramType = paramHandle.getParamType( );
		if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE
				.equals( paramType ) )
		{
			if ( paramValue instanceof Object[] )
			{
				boolean isValid = true;
				Object[] paramValueList = (Object[]) paramValue;
				for ( int i = 0; i < paramValueList.length; i++ )
				{
					if ( paramValueList[i] != null )
					{
						if ( !validateParameterValueType( paramName,
								paramValueList[i], type, paramHandle ) )
						{
							isValid = false;
						}
					}
				}
				return isValid;
			}
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[] { paramName, "Object[]",
							paramValue.getClass().getName() });
		}
		else
		{
			return validateParameterValueType( paramName, paramValue, type,
					paramHandle );
		}
	}
	
	/*
	 * Validate parameter value based on parameter type
	 */
	private boolean validateParameterValueType( String paramName,
			Object paramValue, String type, ScalarParameterHandle paramHandle )
			throws ParameterValidationException
	{
		/*
		 * Validate based on parameter type
		 */
		if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			if ( paramValue instanceof Number )
				return true;
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			if ( paramValue instanceof Date )
				return true;
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
		{
			if ( paramValue instanceof java.sql.Date )
				return true;
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
		{
			if ( paramValue instanceof java.sql.Time )
				return true;
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			if ( paramHandle.isRequired( ) ) //$NON-NLS-1$
			{
				String value = paramValue.toString( ).trim( );
				if ( value.length( ) == 0 )
				{
					throw new ParameterValidationException(
							MessageConstants.PARAMETER_IS_BLANK_EXCEPTION,
							new String[]{paramName} );
				}
			}
			return true;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			if ( paramValue instanceof Boolean )
				return true;
			throw new ParameterValidationException(
					MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#setParameterValues(java.util.HashMap)
	 */
	public void setParameterValues( Map params )
	{
		Iterator iterator = params.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			String name = (String) entry.getKey( );
			Object value = entry.getValue( );
			setParameterValue( name, value );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#setParameterValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setParameterValue( String name, Object value )
	{
		log.log( Level.FINE, "EngineTask.setParameterValue: {0}={1} [{2}]",
				new Object[]{name, value,
						value == null ? null : value.getClass( ).getName( )} );
		parameterChanged = true;
		Object parameter = inputValues.get( name );
		if ( parameter != null )
		{
			assert parameter instanceof ParameterAttribute;
			( (ParameterAttribute) parameter ).setValue( value );
		}
		else
		{
			inputValues.put( name, new ParameterAttribute( value, null ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#setParameterValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setValue( String name, Object value )
	{
		setParameterValue( name, value );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#getParameterValues()
	 */
	public HashMap getParameterValues( )
	{
		HashMap result = new HashMap( );
		Iterator iterator = inputValues.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			ParameterAttribute parameter = (ParameterAttribute) entry
					.getValue( );
			result.put( entry.getKey( ), parameter.getValue( ) );
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#getParameterValue(java.lang.String)
	 */
	public Object getParameterValue( String name )
	{
		Object parameter = inputValues.get( name );
		if ( parameter == null )
		{
			return null;
		}
		assert parameter instanceof ParameterAttribute;
		return ( (ParameterAttribute) parameter ).getValue( );
	}

	public void setParameter( String name, Object value, String displayText )
	{
		parameterChanged = true;
		inputValues.put( name, new ParameterAttribute( value, displayText ) );
	}

	public String getParameterDisplayText( String name )
	{
		Object parameter = inputValues.get( name );
		if ( parameter != null )
		{
			assert parameter instanceof ParameterAttribute;
			return ( (ParameterAttribute) parameter ).getDisplayText( );
		}
		return null;
	}

	public void setParameterDisplayTexts( Map params )
	{
		Iterator iterator = params.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			String name = (String) entry.getKey( );
			String text = (String) entry.getValue( );
			setParameterDisplayText( name, text );
		}
	}

	public void setParameterDisplayText( String name, String displayText )
	{
		parameterChanged = true;
		Object parameter = inputValues.get( name );
		if ( parameter != null )
		{
			assert parameter instanceof ParameterAttribute;
			( (ParameterAttribute) parameter ).setDisplayText( displayText );
		}
		else
		{
			inputValues.put( name, new ParameterAttribute( null, displayText ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IEngineTask#cancel()
	 */
	public void cancel( )
	{
		cancelFlag = true;
		executionContext.cancel( );
	}

	public void cancel( Object signal )
	{
		if ( signal == null )
		{
			throw new IllegalArgumentException( "signal can't be null" );
		}
		cancelFlag = true;
		long waitingTime = 0;
		do
		{
			waitingTime += 100;
			try
			{
				Thread.sleep( 100 );
			}
			catch ( Exception ex )
			{
			}
			if ( runningStatus != STATUS_RUNNING )
			{
				return;
			}
		} while ( waitingTime < 5000 );
		return;
	}

	public boolean getCancelFlag( )
	{
		return cancelFlag;
	}

	public void setErrorHandlingOption( int option )
	{
		if ( option == CANCEL_ON_ERROR )
		{
			executionContext.setCancelOnError( true );
		}
		else
		{
			executionContext.setCancelOnError( false );
		}
	}

	/**
	 * class used to visit all parameters
	 * 
	 */
	static abstract class ParameterVisitor
	{

		boolean visitParametersInGroup( ParameterGroupHandle group, Object value )
		{
			SlotHandle parameters = group.getParameters( );
			Iterator iter = parameters.iterator( );
			while ( iter.hasNext( ) )
			{
				Object param = iter.next( );
				if ( param instanceof CascadingParameterGroupHandle )
				{
					if ( !visitCascadingParamterGroup(
							(CascadingParameterGroupHandle) param, value ) )
					{
						return false;
					}
				}
				else if ( param instanceof ParameterGroupHandle )
				{
					if ( !visitParameterGroup( (ParameterGroupHandle) param,
							value ) )
					{
						return false;
					}
				}
				else if ( param instanceof ScalarParameterHandle )
				{
					if ( !visitScalarParameter( (ScalarParameterHandle) param,
							value ) )
					{
						return false;
					}
				}
			}
			return true;
		}

		boolean visitCascadingParamterGroup(
				CascadingParameterGroupHandle group, Object value )
		{
			return visitParameterGroup( group, value );
		}

		boolean visitParameterGroup( ParameterGroupHandle group, Object value )
		{
			return false;
		}

		boolean visitScalarParameter( ScalarParameterHandle param, Object value )
		{
			return false;
		}

		boolean visit( ReportDesignHandle report )
		{
			return visit( report, null );
		}

		boolean visit( ReportDesignHandle report, Object value )
		{
			SlotHandle parameters = report.getParameters( );
			Iterator iter = parameters.iterator( );
			while ( iter.hasNext( ) )
			{
				Object param = iter.next( );
				if ( param instanceof CascadingParameterGroupHandle )
				{
					if ( !visitCascadingParamterGroup(
							(CascadingParameterGroupHandle) param, value ) )
					{
						return false;
					}
				}
				else if ( param instanceof ParameterGroupHandle )
				{
					if ( !visitParameterGroup( (ParameterGroupHandle) param,
							value ) )
					{
						return false;
					}
				}
				else if ( param instanceof ScalarParameterHandle )
				{
					if ( !visitScalarParameter( (ScalarParameterHandle) param,
							value ) )
					{
						return false;
					}
				}
			}
			return true;
		}
	}

	protected IQueryResults executeDataSet( DataSetHandle hDataSet,
			HashMap parameters )
	{
		return null;
	}

	/**
	 * use the user setting parameters values to setup the execution context.
	 * the user setting values and default values are merged here.
	 */
	protected void usingParameterValues( )
	{
		if ( !parameterChanged )
		{
			return;
		}

		parameterChanged = false;

		// clear previous settings
		executionContext.clearParameters( );
		runValues.clear( );

		// set the user setting values into the execution context
		Iterator iterator = inputValues.entrySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iterator.next( );
			Object key = entry.getKey( );
			ParameterAttribute attribute = (ParameterAttribute) entry
					.getValue( );
			runValues.put( key, attribute.getValue( ) );
			executionContext.setParameter( (String) key, attribute.getValue( ),
					attribute.getDisplayText( ) );
		}
		IReportRunnable runnable = executionContext.getRunnable( );
		if ( runnable == null )
		{
			return;
		}

		// use default value for the parameter without user value.
		new ParameterVisitor( ) {

			boolean visitScalarParameter( ScalarParameterHandle param,
					Object userData )
			{
				String name = param.getName( );
				if ( !inputValues.containsKey( name ) )
				{
					Object value = convertToType( param.getDefaultValue( ),
							param.getDataType( ), param.getParamType() );
					executionContext.setParameterValue( name, value );
					runValues.put( name, value );
				}
				return true;
			}

			boolean visitParameterGroup( ParameterGroupHandle group,
					Object value )
			{
				return visitParametersInGroup( group, value );
			}
		}.visit( (ReportDesignHandle) runnable.getDesignHandle( ) );
	}

	public void close( )
	{
		executionContext.close( );
		EngineLoggerHandler.setLogger( null );
	}
	
	protected IContentEmitter createContentEmitter( ) throws EngineException
	{

		ExtensionManager extManager = ExtensionManager.getInstance( );
		pagination = extManager.getPagination( emitterID );
		Boolean outputDisplayNone = extManager.getOutputDisplayNone( emitterID );
		if ( !renderOptions.hasOption( IRenderOption.OUTPUT_DISPLAY_NONE ) )
		{
			renderOptions.setOption( IRenderOption.OUTPUT_DISPLAY_NONE,
					outputDisplayNone );
		}
		IContentEmitter emitter = null;
		try
		{
			emitter = extManager.createEmitter( format, emitterID );
		}
		catch ( Throwable t )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.", //$NON-NLS-1$
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION, format, t );
		}
		if ( emitter == null )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.", //$NON-NLS-1$
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION, format );
		}

		return emitter;
	}

	protected IReportLayoutEngine createReportLayoutEngine( String pagination,
			IRenderOption options )
	{
		IReportLayoutEngine layoutEngine = LayoutEngineFactory
				.createLayoutEngine( pagination );
		if ( options != null )
		{
			Object fitToPage = renderOptions
					.getOption( IPDFRenderOption.FIT_TO_PAGE );
			if ( fitToPage != null )
			{
				layoutEngine
						.setOption( IPDFRenderOption.FIT_TO_PAGE, fitToPage );
			}
			Object pagebreakOnly = renderOptions
					.getOption( IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY );
			if ( pagebreakOnly != null )
			{
				layoutEngine.setOption(
						IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY,
						pagebreakOnly );
			}
			Object pageOverflow = renderOptions
					.getOption( IPDFRenderOption.PAGE_OVERFLOW );
			if ( pageOverflow != null )
			{
				layoutEngine.setOption( IPDFRenderOption.PAGE_OVERFLOW,
						pageOverflow );
			}
			Object outputDisplayNone = renderOptions
					.getOption( IPDFRenderOption.OUTPUT_DISPLAY_NONE );
			if ( outputDisplayNone != null )
			{
				layoutEngine.setOption(
						IPDFRenderOption.OUTPUT_DISPLAY_NONE,
						outputDisplayNone );
			}
			Object pdfTextWrapping = renderOptions
					.getOption( IPDFRenderOption.PDF_TEXT_WRAPPING );
			if ( pdfTextWrapping != null )
			{
				layoutEngine.setOption(
						IPDFRenderOption.PDF_TEXT_WRAPPING,
						pdfTextWrapping );
			}
			Object pdfFontSubstitution = renderOptions
					.getOption( IPDFRenderOption.PDF_FONT_SUBSTITUTION );
			if ( pdfFontSubstitution != null )
			{
				layoutEngine.setOption(
						IPDFRenderOption.PDF_FONT_SUBSTITUTION,
						pdfFontSubstitution );
			}
			
			/*
			 * bidi_hcg:
			 */
			layoutEngine.setOption( IRenderOption.RTL_FLAG, renderOptions
					.getOption( IRenderOption.RTL_FLAG ) );
					
			Object pdfBidiProcessing = renderOptions
					.getOption( IPDFRenderOption.PDF_BIDI_PROCESSING );
			if ( pdfBidiProcessing != null )
			{
				layoutEngine.setOption( IPDFRenderOption.PDF_BIDI_PROCESSING,
						pdfBidiProcessing );
			}

			Object pdfHyphenation = renderOptions
					.getOption( IPDFRenderOption.PDF_HYPHENATION );
			if ( pdfHyphenation != null )
			{
				layoutEngine.setOption( IPDFRenderOption.PDF_HYPHENATION,
						pdfHyphenation );
			}
			
			Object dpi = renderOptions
					.getOption( IPDFRenderOption.DPI );
			if ( dpi != null )
			{
				layoutEngine.setOption( IPDFRenderOption.DPI,
						dpi );
			}
			
		}
		layoutEngine.setOption( TASK_TYPE,  new Integer(taskType));
		return layoutEngine;
	}

	protected void loadDesign( )
	{
		IReportRunnable runnable = executionContext.getRunnable( );
		if ( runnable != null )
		{
			ReportDesignHandle reportDesign = executionContext.getDesign( );
			// execute scripts defined in include-script element of the libraries
			Iterator iter = reportDesign.includeLibraryScriptsIterator( );
			while ( iter.hasNext( ) )
			{
				IncludeScriptHandle includeScript = (IncludeScriptHandle) iter
						.next( );
				String fileName = includeScript.getFileName( );
				executionContext.loadScript( fileName );
			}
				
			// execute scripts defined in include-script element of this report
			iter = reportDesign.includeScriptsIterator( );
			while ( iter.hasNext( ) )
			{
				IncludeScriptHandle includeScript = (IncludeScriptHandle) iter
						.next( );
				String fileName = includeScript.getFileName( );
				executionContext.loadScript( fileName );
			}

			// Intialize the report
			ReportScriptExecutor.handleInitialize( reportDesign,
					executionContext );
		}
	}

	protected void prepareDesign( )
	{
		ReportRunnable runnable = executionContext.getRunnable( );
		if( !runnable.prepared)
		{
			ReportDesignHandle reportDesign = executionContext.getDesign( );
			ScriptedDesignSearcher searcher = new ScriptedDesignSearcher(
					reportDesign );
			searcher.apply( reportDesign );
			boolean hasOnprepare = searcher.hasOnPrepareScript( );			
			if ( hasOnprepare)
			{
				ReportRunnable newRunnable = executionContext.getRunnable( )
						.cloneRunnable( );
				ReportDesignHandle newDesign = newRunnable.designHandle;
				ScriptedDesignVisitor visitor = new ScriptedDesignHandler(
						newDesign, executionContext );
				visitor.apply( newDesign.getRoot( ) );
				newRunnable.setPrepared( true );
				executionContext.updateRunnable( newRunnable );
			}
		}
	}

	protected void startFactory( )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor.handleBeforeFactory( reportDesign,
				executionContext );
	}

	protected void closeFactory( )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor
				.handleAfterFactory( reportDesign, executionContext );

	}

	protected void startRender( )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor
				.handleBeforeRender( reportDesign, executionContext );
	}

	protected void closeRender( )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor.handleAfterRender( reportDesign, executionContext );
	}

	// TODO: throw out the IOException
	public void setDataSource( IDocArchiveReader dataSource )
	{
		setDataSource( dataSource, null );
	}

	private IDocArchiveReader dataSource;
	private String dataSourceReportlet;

	public void setDataSource( IDocArchiveReader dataSource, String reportlet )
	{
		this.dataSource = dataSource;
		this.dataSourceReportlet = reportlet;
	}
	
	public int getStatus( )
	{
		return runningStatus;
	}

	public List getErrors( )
	{
		return executionContext.getErrors( );
	}

	public IReportContext getReportContext( )
	{
		return executionContext.getReportContext( );
	}

	private void mergeOption( IRenderOption options, String name, Object value )
	{
		if ( options != null )
		{
			if ( value != null && !options.hasOption( name ) )
			{
				options.setOption( name, value );
			}
		}
	}

	/**
	 * intialize the render options used to render the report.
	 * 
	 * the render options are load from:
	 * <li> engine level default options</li>
	 * <li> engine level format options</li>
	 * <li> engine level emitter options</li>
	 * <li> task level options </li>
	 * @throws EngineException 
	 * 
	 */
	protected void setupRenderOption( ) throws EngineException
	{
		ExtensionManager extManager = ExtensionManager.getInstance( );
		if ( renderOptions != null )
		{
			format = renderOptions.getOutputFormat( );
			emitterID = renderOptions.getEmitterID( );
			if ( emitterID == null )
			{
				if ( format == null )
				{
					format = RenderOption.OUTPUT_FORMAT_HTML;
					renderOptions.setOutputFormat( format );
				}
				emitterID = engine.getConfig( ).getDefualtEmitter(format);
			}
			if(emitterID!=null)
			{
				if(!extManager.isValidEmitterID( emitterID ))
				{
					log.log( Level.SEVERE, MessageConstants.INVALID_EMITTER_ID, emitterID);
					throw new EngineException( MessageConstants.INVALID_EMITTER_ID, emitterID );
				}
				String formatOfEmitter = extManager.getFormat( emitterID );
				if ( null == format )
				{
					renderOptions.setOutputFormat( formatOfEmitter );
				}
				else if ( !format.equals( formatOfEmitter ) )
				{
					throw new EngineException( MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
				}
			}
			else
			{
				if ( format == null )
				{
					format = RenderOption.OUTPUT_FORMAT_HTML;
					renderOptions.setOutputFormat( format );
					emitterID = RenderOption.OUTPUT_EMITTERID_HTML;
				}
				else
				{
					boolean supportedFormat = extManager.isSupportedFormat( format );
					if ( !supportedFormat )
					{
						log.log( Level.SEVERE,
								MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
						throw new UnsupportedFormatException(
								MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
					}
					else
					{
						emitterID = extManager.getEmitterID( format );
					}
				}
			}
		}

		// copy the old setting to render options
		Map appContext = executionContext.getAppContext( );
		if ( IRenderOption.OUTPUT_EMITTERID_PDF.equals( emitterID ) )
		{
			Object renderContext = appContext
					.get( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT );
			if ( renderContext instanceof PDFRenderContext )
			{
				PDFRenderContext pdfContext = (PDFRenderContext) renderContext;
				mergeOption( renderOptions, PDFRenderOption.BASE_URL,
						pdfContext.getBaseURL( ) );
				mergeOption( renderOptions, PDFRenderOption.FONT_DIRECTORY,
						pdfContext.getFontDirectory( ) );
				mergeOption( renderOptions,
						PDFRenderOption.SUPPORTED_IMAGE_FORMATS, pdfContext
								.getSupportedImageFormats( ) );
				mergeOption( renderOptions, PDFRenderOption.IS_EMBEDDED_FONT,
						new Boolean( pdfContext.isEmbededFont( ) ) );
			}
		}
		else
		{
			Object renderContext = appContext
					.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );
			if ( renderContext instanceof HTMLRenderContext )
			{
				HTMLRenderContext htmlContext = (HTMLRenderContext) renderContext;

				mergeOption( renderOptions, HTMLRenderOption.BASE_IMAGE_URL,
						htmlContext.getBaseImageURL( ) );
				mergeOption( renderOptions, HTMLRenderOption.BASE_URL,
						htmlContext.getBaseURL( ) );
				mergeOption( renderOptions, HTMLRenderOption.IMAGE_DIRECTROY,
						htmlContext.getImageDirectory( ) );
				mergeOption( renderOptions,
						HTMLRenderOption.SUPPORTED_IMAGE_FORMATS, htmlContext
								.getSupportedImageFormats( ) );
			}
		}

		// setup the render options from:
		// engine default, format default, emitter default and task options
		HashMap options = new HashMap( );

		// try to get the default render option from the engine config.
		HashMap configs = engine.getConfig( ).getEmitterConfigs( );
		// get the default format of the emitters, the default format key is
		// IRenderOption.OUTPUT_FORMAT;
		IRenderOption defaultOptions = (IRenderOption) configs
				.get( IEngineConfig.DEFAULT_RENDER_OPTION );
		if ( defaultOptions == null )
		{
			defaultOptions = (IRenderOption) configs
					.get( IRenderOption.OUTPUT_FORMAT_HTML );
		}
		if ( defaultOptions != null )
		{
			options.putAll( defaultOptions.getOptions( ) );
		}

		// try to get the render options by the format
		IRenderOption formatOptions = (IRenderOption) configs.get( format );
		if ( formatOptions != null )
		{
			options.putAll( formatOptions.getOptions( ) );
		}

		// try to load the configs through the emitter id
		if ( emitterID != null )
		{
			IRenderOption emitterOptions = (IRenderOption) configs
					.get( emitterID );
			if ( emitterOptions != null )
			{
				options.putAll( emitterOptions.getOptions( ) );
			}
		}

		// load the options from task level options
		if ( renderOptions != null )
		{
			options.putAll( renderOptions.getOptions( ) );
		}

		// setup the render options used by this task
		IRenderOption allOptions = new RenderOption( options );
		executionContext.setRenderOption( allOptions );

		// copy the new setting to old APIs
		if ( IRenderOption.OUTPUT_EMITTERID_PDF.equals( emitterID ) )
		{
			Object renderContext = appContext
					.get( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT );
			if ( renderContext == null )
			{
				PDFRenderOption pdfOptions = new PDFRenderOption( allOptions );
				PDFRenderContext pdfContext = new PDFRenderContext( );
				pdfContext.setBaseURL( pdfOptions.getBaseURL( ) );
				pdfContext.setEmbededFont( pdfOptions.isEmbededFont( ) );
				pdfContext.setFontDirectory( pdfOptions.getFontDirectory( ) );
				pdfContext.setSupportedImageFormats( pdfOptions
						.getSupportedImageFormats( ) );
				appContext.put( EngineConstants.APPCONTEXT_PDF_RENDER_CONTEXT,
						pdfContext );
			}
		}
		else
		{
			Object renderContext = appContext
					.get( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT );
			if ( renderContext == null )
			{
				HTMLRenderContext htmlContext = new HTMLRenderContext( );
				HTMLRenderOption htmlOptions = new HTMLRenderOption( allOptions );
				htmlContext.setBaseImageURL( htmlOptions.getBaseImageURL( ) );
				htmlContext.setBaseURL( htmlOptions.getBaseURL( ) );
				htmlContext
						.setImageDirectory( htmlOptions.getImageDirectory( ) );
				htmlContext.setSupportedImageFormats( htmlOptions
						.getSupportedImageFormats( ) );
				htmlContext.SetRenderOption( allOptions );
				appContext.put( EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT,
						htmlContext );
			}
		}
	}

	protected void initializeContentEmitter( IContentEmitter emitter,
			IReportExecutor executor )
	{
		// create the emitter services object that is needed in the emitters.
		HashMap configs = engine.getConfig( ).getEmitterConfigs( );
		IReportContext reportContext = executionContext.getReportContext( );
		IRenderOption options = executionContext.getRenderOption( );
		EngineEmitterServices services = new EngineEmitterServices(
				reportContext, options, configs );

		// emitter is not null
		emitter.initialize( services );
	}

	public int getTaskType( )
	{
		return taskType;
	}

	protected void changeStatusToRunning( )
	{
		runningStatus = STATUS_RUNNING;
	}

	protected void changeStatusToStopped( )
	{
		if ( cancelFlag )
		{
			runningStatus = STATUS_CANCELLED;
		}
		else if ( executionContext.hasErrors( ) )
		{
			runningStatus = STATUS_FAILED;
		}
		else
		{
			runningStatus = STATUS_SUCCEEDED;
		}
	}

	public Logger getLogger( )
	{
		return log;
	}

	public void setLogger( Logger logger )
	{
		if ( logger == null || !EngineLogger.isValidLogger( logger ) )
		{
			throw new IllegalArgumentException(
					"the logger can't be NULL or children or in namespace of org.eclipse.birt" );
		}
		EngineLoggerHandler.setLogger( logger );
		this.log = logger;
		this.executionContext.setLogger( logger );
	}

	protected void switchToOsgiClassLoader( )
	{
		ClassLoader newLoader = Platform.getContextClassLoader( );
		if ( newLoader != null )
		{
			contextClassLoader = SecurityUtil.setContextClassLoader( newLoader );
		}
	}

	protected void switchClassLoaderBack( )
	{
		if ( contextClassLoader != null )
		{
			SecurityUtil.setContextClassLoader( contextClassLoader );
		}
	}

	/**
	 * 
	 * @throws EngineException
	 */
	protected void loadDataSource( ) throws EngineException
	{
		// we only need setup the data source for the task which has dataSource
		if ( dataSource == null )
		{
			return;
		}

		// try to open the dataSource as report document
		try
		{
			ReportDocumentReader document = new ReportDocumentReader( engine,
					dataSource, true );

			try
			{
				// load the parameter values from report document
				Map values = document.getParameterValues( );
				Map texts = document.getParameterDisplayTexts( );
				setParameterValues( values );
				setParameterDisplayTexts( texts );

				if ( dataSourceReportlet == null )
				{
					executionContext.setDataSource( new DocumentDataSource(
							dataSource ) );
					return;

				}
				loadReportletDataSource( document, dataSource,
						dataSourceReportlet );
			}
			finally
			{
				document.close( );
			}
		}
		catch ( IOException ioex )
		{
			throw new EngineException( ioex.getMessage( ), ioex );
		}
	}

	private void loadReportletDataSource( ReportDocumentReader document,
			IDocArchiveReader dataSource, String reportletBookmark )
			throws EngineException, IOException
	{

		InstanceID reportletIid = null;
		if ( document.isReporltetDocument( ) )
		{
			String bookmark = document.getReportletBookmark( );
			if ( !reportletBookmark.equals( bookmark ) )
			{
				throw new EngineException(
						"The user must specify the same reportlet with the one used to generate the document" );
			}
			reportletIid = document.getReportletInstanceID( );
		}
		else
		{
			// load the result set used by reportlet
			long offset = document.getBookmarkOffset( reportletBookmark );
			if ( offset == -1 )
			{
				throw new EngineException(
						"The user specified reportlet {0} doesn''t exits in the report document",
						new Object[]{reportletBookmark} );
			}

			ClassLoader loader = document.getClassLoader( );
			RAInputStream in = dataSource
					.getInputStream( ReportDocumentConstants.CONTENT_STREAM );
			try
			{
				ReportContentReaderV3 reader = new ReportContentReaderV3(
						new ReportContent( ), in, loader );
				try
				{
					LinkedList<InstanceID> iids = new LinkedList<InstanceID>( );
					while ( offset != -1 )
					{
						IContent content = reader.readContent( offset );
						iids.addFirst( content.getInstanceID( ) );
						offset = ( (DocumentExtension) content
								.getExtension( IContent.DOCUMENT_EXTENSION ) )
								.getParent( );
					}

					for ( InstanceID iid : iids )
					{
						if ( reportletIid == null )
						{
							reportletIid = iid;
						}
						else
						{
							reportletIid = new InstanceID( reportletIid, iid );
						}
					}
				}
				finally
				{
					reader.close( );
				}
			}
			finally
			{
				in.close( );
			}
		}
		// set the datasources
		executionContext.setDataSource( new DocumentDataSource( dataSource,
				reportletBookmark, reportletIid ) );
	}

	protected void updateRtLFlag( ) throws EngineException
	{
		// get RtL flag from renderOptions
		if ( renderOptions == null )
			return;
		IReportRunnable runnable = executionContext.getRunnable( );
		if ( runnable == null )
			return;

		ReportDesignHandle handle = (ReportDesignHandle) runnable
				.getDesignHandle( );
		if ( handle != null )
		{
			Object bidiFlag = renderOptions.getOption( IRenderOption.RTL_FLAG );
			String bidiOrientation = null;
			if ( bidiFlag != null )
			{
				if ( Boolean.TRUE.equals( bidiFlag ) )
				{
					bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_RTL;
				}
				else
				{
					bidiOrientation = DesignChoiceConstants.BIDI_DIRECTION_LTR;
				}
				try
				{
					handle.setBidiOrientation( bidiOrientation );
					Report report = executionContext.getReport( );
					AbstractStyle rootStyle = (AbstractStyle) report
							.getStyles( ).get( report.getRootStyleName( ) );
					if ( rootStyle != null )
					{
						rootStyle.setDirection( bidiOrientation );
					}
				}
				catch ( SemanticException e )
				{
					log
							.log(
									Level.WARNING,
									"An error happened while running the report. Cause:", e ); //$NON-NLS-1$
					throw new EngineException( "Failed to update RtL flag." );//$NON-NLS-1$
				}
			}
			// Updated renderOptions based on report design orientation.
			// XXX It seems ideally we should distinguish between null value for
			// direction/rtl flag and the explicit 'ltr' value, either here, or
			// in the block above.
			else if ( handle.isDirectionRTL( ) )
			{
				renderOptions.setOption( IRenderOption.RTL_FLAG, new Boolean(
						true ) );
				IRenderOption renderOptions2 = executionContext
						.getRenderOption( );
				if ( renderOptions2 != null )
				{
					renderOptions2.setOption( IRenderOption.RTL_FLAG,
							new Boolean( true ) );
					executionContext.setRenderOption( renderOptions2 );
				}
			}
		}
	}

	protected IReportExecutor createReportExtensionExecutor(
			IReportExecutor executor ) throws EngineException
	{
		// prepare the extension executor
		String[] extensions = executionContext.getEngineExtensions( );
		if ( extensions != null )
		{
			ArrayList<IContentProcessor> processors = new ArrayList<IContentProcessor>( );
			EngineExtensionManager manager = executionContext
					.getEngineExtensionManager( );
			for ( String extName : extensions )
			{
				IGenerateExtension genExt = manager
						.getGenerateExtension( extName );
				if ( genExt != null )
				{
					IContentProcessor processor = genExt
							.createGenerateProcessor( );
					if ( processor != null )
					{
						processors.add( processor );
					}
				}
			}
			if ( !processors.isEmpty( ) )
			{
				return new ReportExtensionExecutor( executionContext, executor,
						processors.toArray( new IContentProcessor[processors
								.size( )] ) );
			}
		}
		return executor;
	}
	
	public void setUserACL( String[] acls )
	{
		if ( acls != null )
		{
			String[] strippedAcls = strip( acls );
			executionContext.getAppContext( ).put(
					EngineConstants.USER_ACL_KEY, strippedAcls );

		}
		else
		{
			executionContext.getAppContext( ).put(
					EngineConstants.USER_ACL_KEY, null );
		}
	}

	protected String[] strip( String[] acls )
	{
		ArrayList<String> strippedAcls = new ArrayList<String>( );
		for ( int i = 0; i < acls.length; i++ )
		{
			String acl = acls[i];
			if ( acl != null )
			{
				String strippedAcl = acl.trim( );
				if ( strippedAcl.length( ) > 0 )
				{
					strippedAcls.add( strippedAcl );
				}
			}
		}
		return strippedAcls.toArray( new String[strippedAcls.size( )] );
	}
}