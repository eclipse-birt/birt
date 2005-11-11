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

package org.eclipse.birt.report.engine.api.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * Defines an engine task that could be executed, debugged (runs step by step),
 * inform caller for progress, etc.
 */
public abstract class EngineTask implements IEngineTask
{

	protected static Logger log = Logger
			.getLogger( EngineTask.class.getName( ) );

	protected static int id = 0;

	/**
	 * the contexts for running this task
	 */
	protected Map context;

	/**
	 * a reference to the report engine
	 */
	protected ReportEngine engine;

	/**
	 * Comment for <code>locale</code>
	 */
	protected Locale locale = Locale.getDefault( );

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
	 * @param engine
	 *            reference to report engine
	 * @param context
	 *            a user-defined object that capsulates the context for running
	 *            a task. The context object is passed to callback functions
	 *            (i.e., functions in image handlers, action handlers, etc. )
	 *            that are written by those who embeds engine in their
	 *            applications
	 */
	protected EngineTask( ReportEngine engine, IReportRunnable runnable )
	{
		this.runnable = runnable;
		this.engine = engine;
		taskID = id++;

		executionContext = new ExecutionContext( engine, taskID );
		executionContext.setRunnable( runnable );
		executionContext.registerBeans( runnable.getTestConfig( ) );
	}

	/**
	 * @return Returns the locale.
	 */
	public Locale getLocale( )
	{
		return locale;
	}

	/**
	 * sets the task locale
	 * 
	 * @param locale
	 *            the task locale
	 */
	public void setLocale( Locale locale )
	{
		this.locale = locale;
		executionContext.setLocale( locale );
	}

	/**
	 * sets the task context
	 * 
	 * @param context
	 *            the task context
	 */
	public void setAppContext( Map context )
	{
		this.context = context;
		executionContext.setAppContext( context );
		
		// add the contexts into ScriptableJavaObject
		if ( !context.isEmpty() )
		{
			Set entries = context.entrySet();
			for ( Iterator iter = entries.iterator(); iter.hasNext( ); )
			{
				Map.Entry entry = (Map.Entry) iter.next();
				if ( entry.getKey() instanceof String )
				{
					addScriptableJavaObject( (String)entry.getKey(), entry.getValue() );
				}
				else
				{
					log.log( Level.WARNING, "Map entry {0} is invalid and ignored, because its key is a string.", //$NON-NLS-1$ 
							 entry.getKey().toString() );
				}
			}
		}
	}

	/**
	 * returns the object that encapsulates the context for running the task
	 * 
	 * @return Returns the context.
	 */
	public Map getAppContext( )
	{
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getEngine()
	 */
	public ReportEngine getEngine( )
	{
		return engine;
	}

	public DataEngine getDataEngine( )
	{
		return executionContext.getDataEngine( ).getDataEngine( );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IEngineTask#getReportRunnable()
	 */
	public IReportRunnable getReportRunnable( )
	{
		return this.runnable;
	}

	protected Object convertToType( Object value, String type )
	{
		try
		{
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
			{
				return DataTypeUtil.toBoolean( value );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
			{
				return DataTypeUtil.toDate( value );
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
			return value;
		}
		catch ( BirtException e )
		{
			log.log( Level.SEVERE, e.getLocalizedMessage( ), e );
		}
		return null;
	}

	/**
	 * evaluate a script and convert the value to specified type.
	 * 
	 * @param expr
	 *            exprestion statement
	 * @param type
	 *            value type
	 * @return result with the specified type.
	 */
	protected Object evaluate( String expr, String type )
	{
		if ( expr == null || expr.length( ) == 0 )
		{
			return null;
		}
		Object value = executionContext.evaluate( expr );
		return convertToType( value, type );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#validateParameters()
	 */
	public boolean validateParameters( )
	{
		// set the parameter values into the execution context
		usingParameterValues( );

		// validate each parameter to see if it is validate
		return new ParameterVisitor( ) {

			boolean visitScalarParameter( ScalarParameterHandle param,
					Object value )
			{
				return validateScalarParameter( param );
			}

			boolean visitParameterGroup( ParameterGroupHandle group,
					Object value )
			{
				return visitParametersInGroup( group, value );
			}
		}.visit( (ReportDesignHandle) runnable.getDesignHandle( ), null );

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
	{

		String paramName = paramHandle.getName( );
		Object paramValue = runValues.get( paramName );
		String type = paramHandle.getDataType( );

		// Handle null parameter values
		if ( paramValue == null )
		{
			if ( paramHandle.allowNull( ) )
				return true;

			log.log( Level.SEVERE, "Parameter {0} doesn't allow a null value.", //$NON-NLS-1$ 
					paramName );
			return false;
		}

		/*
		 * Validate based on parameter type
		 */
		if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			if ( paramValue instanceof Number )
				return true;

			log
					.log(
							Level.SEVERE,
							"The supplied value {0} for parameter {1} is not a number.", new String[]{paramValue.toString( ), paramName} ); //$NON-NLS-1$
			return false;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			if ( paramValue instanceof Date )
				return true;
			log
					.log(
							Level.SEVERE,
							"The supplied value {0} for parameter {1} is not a valid date.", new String[]{paramValue.toString( ), paramName} ); //$NON-NLS-1$
			return false;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			String value = paramValue.toString( ).trim( );
			if ( value.equals( "" ) && !paramHandle.allowBlank( ) ) //$NON-NLS-1$
			{
				log.log( Level.SEVERE,
						"parameter {0} can't be blank.", paramName ); //$NON-NLS-1$
				return false;
			}
			return true;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			if ( paramValue instanceof Boolean )
				return true;
			log
					.log(
							Level.SEVERE,
							"The supplied value {0} for parameter {1} is not a boolean.", new String[]{paramValue.toString( ), paramName} ); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#setParameterValues(java.util.HashMap)
	 */
	public void setValues( HashMap params )
	{
		parameterChanged = true;
		inputValues.putAll( params );
	}
	
	public void setParameterValues(HashMap params)
	{
		setValues(params);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunAndRenderTask#setParameterValue(java.lang.String,
	 *      java.lang.Object)
	 */
	public void setValue( String name, Object value )
	{
		parameterChanged = true;
		inputValues.put( name, value );
	}
	
	public void setParameterValue( String name, Object value )
	{
		setValue(name, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#getParameterValues()
	 */
	public HashMap getValues( )
	{
		return (HashMap) inputValues.clone( );
	}
	
	public HashMap getParameterValues( )
	{
		return getValues();
	}

	public Object getValue( String name )
	{
		return inputValues.get( name );
	}
	
	public Object getParameterValue( String name )
	{
		return getValue(name);
	}
	

	/**
	 * class used to visit all parameters
	 * 
	 * @version $Revision: 1.19 $ $Date: 2005/11/02 02:25:16 $
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
		executionContext.getParams( ).clear( );
		runValues.clear( );

		// set the user setting values into the execution context
		executionContext.getParams( ).putAll( inputValues );
		runValues.putAll( inputValues );

		// use default value for the parameter without user value.
		new ParameterVisitor( ) {

			boolean visitScalarParameter( ScalarParameterHandle param,
					Object userData )
			{
				String name = param.getName( );
				if ( !inputValues.containsKey( name ) )
				{
					Object value = evaluate( param.getDefaultValue( ), param
							.getDataType( ) );
					executionContext.getParams( ).put( name, value );
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
}
