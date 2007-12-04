/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
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
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.PDFRenderContext;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.UnsupportedFormatException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.IncludeScriptHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
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
	protected static int id = 0;

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
	protected IReportEngine engine;

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

	protected IReportRunnable runnable;

	/**
	 * options used to render the report design.
	 */
	protected IRenderOption renderOptions;
	/**
	 * emitter id
	 */
	protected String emitterID;

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
	protected EngineTask( IReportEngine engine, IReportRunnable runnable )
	{
		taskID = id++;

		this.engine = engine;
		this.log = engine.getLogger( );

		// create execution context used by java-script
		executionContext = new ExecutionContext( this );
		// Create IReportContext used by java-based script
		executionContext.setReportContext( new ReportContextImpl(
				executionContext ) );

		setReportRunnable( runnable );
		// set the default app context
		setAppContext( engine.getConfig( ).getAppContext( ) );

		cancelFlag = false;
		runningStatus = STATUS_NOT_STARTED;
	}

	protected EngineTask( IReportEngine engine, IReportRunnable runnable,
			int taskType )
	{
		this( engine, runnable );
		this.taskType = taskType;
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
		if ( context != null )
		{
			appContext.putAll( context );
		}

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
			this.runnable = runnable;
			executionContext.setRunnable( runnable );
			// register the properties into the scope, so the user can
			// access the config through the property name directly.
			executionContext.registerBeans( System.getProperties( ) );
			executionContext.registerBeans( runnable.getTestConfig( ) );
			// put the properties into the configs also, so the user can
			// access the config through config["name"].
			executionContext.getConfigs( ).putAll( System.getProperties( ) );
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
		return runnable;
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
		boolean result = pv.visit( (ReportDesignHandle) runnable
				.getDesignHandle( ), null );
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
			}.visit( (ReportDesignHandle) runnable.getDesignHandle( ), null );
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
					MessageConstants.NULL_PARAMETER_EXCEPTION,
					new String[]{paramName} );
		}

		String source = paramHandle.getValidate();
		if (source != null && source.length() != 0) {
			Object result = executionContext.evaluate(source);
			if (!(result instanceof Boolean)
					|| !((Boolean) result).booleanValue()) {
				throw new ParameterValidationException(
						MessageConstants.PARAMETER_SCRIPT_VALIDATION_EXCEPTION,
						new String[] { paramName });
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
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
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
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			if ( paramValue instanceof Date )
				return true;
			throw new ParameterValidationException(
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
		{
			if ( paramValue instanceof java.sql.Date )
				return true;
			throw new ParameterValidationException(
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
					new String[]{paramName, type, paramValue.getClass( ).getName( )} );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
		{
			if ( paramValue instanceof java.sql.Time )
				return true;
			throw new ParameterValidationException(
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
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
							MessageConstants.BLANK_PARAMETER_EXCEPTION,
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
					MessageConstants.INVALID_PARAMETER_TYPE_EXCEPTION,
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
							param.getDataType( ) );
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

		String format = renderOptions.getOutputFormat( );
		if ( format == null )
		{
			format = RenderOption.OUTPUT_FORMAT_HTML;
		}

		ExtensionManager extManager = ExtensionManager.getInstance( );
		boolean supported = false;
		Collection supportedFormats = extManager.getSupportedFormat( );
		Iterator iter = supportedFormats.iterator( );
		while ( iter.hasNext( ) )
		{
			String supportedFormat = (String) iter.next( );
			if ( supportedFormat != null
					&& supportedFormat.equalsIgnoreCase( format ) )
			{
				supported = true;
				break;
			}
		}
		if ( !supported )
		{
			log.log( Level.SEVERE,
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
			throw new UnsupportedFormatException(
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
		}

		pagination = extManager.getPagination( format );
		Boolean outputDisplayNone = extManager.getOutputDisplayNone( format );
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
			Object pdfBidiProcessing = renderOptions
					.getOption( IPDFRenderOption.PDF_BIDI_PROCESSING );
			if ( pdfBidiProcessing != null )
			{
				layoutEngine.setOption(
						IPDFRenderOption.PDF_BIDI_PROCESSING,
						pdfBidiProcessing );
			}
		}
		layoutEngine.setOption( TASK_TYPE,  new Integer(taskType));
		return layoutEngine;
	}

	protected void loadDesign( )
	{
		if ( runnable != null )
		{
			ReportDesignHandle reportDesign = executionContext.getDesign( );
			// execute scripts defined in include-script element of this report
			Iterator iter = reportDesign.includeScriptsIterator( );
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
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ScriptedDesignVisitor visitor = new ScriptedDesignVisitor(
				reportDesign, executionContext );
		visitor.apply( reportDesign.getRoot( ) );
		runnable.setDesignHandle( reportDesign );
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
		// try to open the dataSource as report document
		try
		{
			ReportDocumentReader document = new ReportDocumentReader( engine,
					dataSource, true );
			Map values = document.getParameterValues( );
			Map texts = document.getParameterDisplayTexts( );
			setParameterValues( values );
			setParameterDisplayTexts( texts );
			document.close( );
		}
		catch ( EngineException ex )
		{
			log.log( Level.WARNING,
					"failed to load the paremters in the data source", ex );
		}
		try
		{
			executionContext.setDataSource( dataSource );
		}
		catch ( IOException ioex )
		{
			log.log( Level.WARNING, "failed to open the data source document",
					ioex );
		}
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
	 * 
	 */
	protected void setupRenderOption( )
	{
		String format = RenderOption.OUTPUT_FORMAT_HTML;;
		if ( renderOptions != null )
		{
			format = renderOptions.getOutputFormat( );
			if ( format == null || format.length( ) == 0 )
			{
				format = RenderOption.OUTPUT_FORMAT_HTML;
				renderOptions.setOutputFormat( format );
			}
		}

		// copy the old setting to render options
		Map appContext = executionContext.getAppContext( );
		if ( IRenderOption.OUTPUT_FORMAT_PDF.equals( format ) )
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
		if ( IRenderOption.OUTPUT_FORMAT_PDF.equals( format ) )
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
}