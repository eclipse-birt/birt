/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.api.script;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.Scriptable;

/**
 * Top-level scope created by Data Adaptor. This scope provides implementation of some
 * report level script object (e.g., "params")
 */
public class DataAdapterTopLevelScope extends ImporterTopLevel
{
	private static final long serialVersionUID = 4230948829384l;
	
	private static final String PROP_PARAMS = "params";
	private static final String PROP_REPORTCONTEXT = "reportContext";
	private ModuleHandle designModule;
	private Scriptable paramsProp;
	private Object reportContextProp;

	
	/**
	 * Constructor; initializes standard objects in scope
	 * @param cx Context used to initialze scope
	 * @param module Module associated with this scope; can be null
	 */
	public DataAdapterTopLevelScope( Context cx, ModuleHandle module )
	{
		super(cx);
		// init BIRT native objects
		new CoreJavaScriptInitializer( ).initialize( cx, this );
		designModule = module;
	}
	
    public boolean has(String name, Scriptable start) 
    {
    	if ( super.has(name, start) )
    		return true;
    	// "params" is available only if we have a module
    	if ( designModule != null && PROP_PARAMS.equals(name) )
    		return true;
    	
    	if ( PROP_REPORTCONTEXT.equals( name ) )
			return true;
    	return false;
    }

    public Object get(String name, Scriptable start) 
    {
        Object result = super.get(name, start);
        if (result != NOT_FOUND)
            return result;
        
    	if ( designModule != null && PROP_PARAMS.equals(name) )
    	{
    		return getParamsScriptable();
    	}
    	
    	if ( PROP_REPORTCONTEXT.equals( name ) )
			return getReportContext( );
    	
    	return NOT_FOUND;
    }
    
    /**
     * Gets a object which implements the "reportContext" property
     * @throws InvocationTargetException 
     * @throws InstantiationException 
     * @throws IllegalAccessException 
     */
    private Object getReportContext()
    {
		if ( reportContextProp != null )
			return reportContextProp;

		assert designModule != null;
		
		reportContextProp = new ReportContextObject( designModule );
		
		return reportContextProp;
    }
    
    /**
	 * Gets a scriptable object which implements the "params" property
	 */
	private Scriptable getParamsScriptable( )
	{
		if ( paramsProp != null )
			return paramsProp;

		assert designModule != null;

		Map parameters = new HashMap( );
		List paramsList = designModule.getAllParameters( );
		for ( int i = 0; i < paramsList.size( ); i++ )
		{
			Object parameterObject = paramsList.get( i );
			if ( parameterObject instanceof ScalarParameterHandle )
			{
				Object value = getParamDefaultValue( parameterObject );
				parameters.put( ( (ScalarParameterHandle) parameterObject ).getQualifiedName( ),
						new DummyParameterAttribute( value, "" ) );
			}
		}
		paramsProp = new ReportParameters( parameters, this );
		return paramsProp;
	}
	
    
	/**
	 * Gets the default value of a parameter. If a usable default value is
	 * defined, use it; otherwise use a default value appropriate for the data
	 * type
	 */
	private Object getParamDefaultValue( Object params )
	{
		if ( !( params instanceof ScalarParameterHandle ) )
			return null;

		ScalarParameterHandle sp = (ScalarParameterHandle) params;
		String defaultValue = sp.getDefaultValue( );
		String type = sp.getDataType( );
		if ( defaultValue == null )
		{
			// No default value; if param allows null value, null is used
			if ( sp.allowNull( ) )
				return null;

			// Return a fixed default value appropriate for the data type
			if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
			{
				if ( sp.allowBlank( ) )
					return "";
				else
					return "null";
			}
			if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
				return new Double( 0 );
			if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
				return new BigDecimal( (double) 0 );
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
				return new Date( 0 );
			if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
				return new java.sql.Date( 0 );
			if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
				return new java.sql.Time( 0 );
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
				return Boolean.FALSE;
			if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
				return new Integer( 0 );

			// unknown parameter type; unexpected
			assert false;
			return null;
		}

		// Convert default value to the correct data type
		int typeNum = DataType.ANY_TYPE;
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
			typeNum = DataType.STRING_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
			typeNum = DataType.DOUBLE_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
			typeNum = DataType.DECIMAL_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
			typeNum = DataType.DATE_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
			typeNum = DataType.BOOLEAN_TYPE;
		else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
			typeNum = DataType.INTEGER_TYPE;

		try
		{
			return DataTypeUtil.convert( defaultValue, typeNum );
		}
		catch ( BirtException e )
		{
			return null;
		}
	}
    	
}
