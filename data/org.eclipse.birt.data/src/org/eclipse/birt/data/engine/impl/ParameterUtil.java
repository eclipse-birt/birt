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

package org.eclipse.birt.data.engine.impl;

import java.sql.Blob;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Merge the paramter definition and evaluate the expression of paramter 
 */
class ParameterUtil
{
	private IQueryService outerResults;
	private DataSetRuntime dsRT;
	private IQueryDefinition queryDefn;
	private Scriptable scope;

	private Logger logger;

	/**
	 * @param outerResults
	 * @param dsRT
	 * @param queryDefn
	 * @param scope
	 */
	ParameterUtil( IQueryService outerResults, DataSetRuntime dsRT,
			IQueryDefinition queryDefn, Scriptable scope )
	{
		this.outerResults = outerResults;
		this.dsRT = dsRT;
		this.queryDefn = queryDefn;
		this.scope = scope;
	}
	
	/**
	 * Resolve parameter bindings and return a Collection of ParameterHints, which merged information obtained
	 * from query parameter binding and the data set parameter definition
	 * 
	 */
	Collection resolveDataSetParameters( boolean evaluateValue )
			throws DataException
	{
		List paramDefns = this.dsRT.getParameters( );
		int nParams = paramDefns == null ? 0 : paramDefns.size( );
		
		// array of parameter hints
		ParameterHint[] paramHints = new ParameterHint[nParams];
		// whether corresponding item in paramHints has been bound
		boolean[] bindingResolved = new boolean[nParams];
		
		// First create param hints for all data set params 
		for ( int i = 0; i < nParams; i++ )
		{
			IParameterDefinition paramDefn = (IParameterDefinition) paramDefns.get( i );
			paramHints[i] = createParameterHint( paramDefn,
					paramDefn.getDefaultInputValue( ) );
			bindingResolved[i] = false;
			
			// Can the data set RT provide an input parameter value? (this has the highest
			// priority, over bindings) 
			if ( paramDefn.isInputMode( ) && paramDefn.getName( ) != null )
			{
				Object paramValue = DataSetRuntime.UNSET_VALUE;
				try
				{
					paramValue = this.dsRT.getInputParameterValue( paramDefn.getName( ) );
				}
				catch ( BirtException e )
				{
					// This is unexpected; the parameter must be in the list
					assert false;
					throw DataException.wrap( e );
				}

				if ( paramValue != DataSetRuntime.UNSET_VALUE )
				{
					String paramValueStr = this.getParameterValueString( paramHints[i].getDataType( ),
							paramValue );
					paramHints[i].setDefaultInputValue( paramValueStr );
					bindingResolved[i] = true;
				}
			}
		}
		
		Context cx = null;
		if ( evaluateValue )
			cx = Context.enter( );
		try
		{
			// Resolve parameter bindings

			// Parameter values are determined in the following order of priority
			// (1) Input param values set by scripts (already resolved above)
			// (2) Query parameter bindings
			// (3) Data set parameter bindings

			resolveParameterBindings( this.queryDefn.getInputParamBindings( ),
					paramHints,
					bindingResolved,
					cx );

			resolveParameterBindings( this.dsRT.getInputParamBindings( ),
					paramHints,
					bindingResolved,
					cx );
		}
		finally
		{
			if ( cx != null )
				Context.exit( );
		}
		
		return Arrays.asList( paramHints );
	}

	/**
	 * Resolve a list of parameter bindings and update the hints
	 * @param cx JS context to evaluate binding. If null, binding does not need to be evaluated
	 */
	private void resolveParameterBindings( Collection bindings,
			ParameterHint[] paramHints, boolean[] bindingResolved, Context cx )
			throws DataException
	{
		if ( bindings == null )
			return;
		
		Iterator it = bindings.iterator( );
		while ( it.hasNext( ) )
		{
			resolveParameterBinding( (IInputParameterBinding) it.next( ),
					paramHints,
					bindingResolved,
					cx );
		}
	}

	/**
	 * Resolve a parameter binding and update the hints
	 * @param cx JS context to evaluate binding. If null, binding does not need to be evaluated
	 */
	private void resolveParameterBinding( IInputParameterBinding binding,
			ParameterHint[] paramHints, boolean[] bindingResolved, Context cx )
			throws DataException
	{
		// Find the hint which matches the binding
		int i = findParameterHint( paramHints,
				binding.getPosition( ),
				binding.getName( ) );
		
		if ( i < 0 )
		{
			// A binding exists but the data set has no definition for the
			// bound parameter, log an error and ignore the param
			if ( logger != null )
				logger.warning( "Ignored binding defined for non-exising data set parameter: "
						+ "name="
						+ binding.getName( )
						+ ", position="
						+ binding.getPosition( ) );
		}

		// Do not set binding value if the parameter has already been resolved
		// (e.g., query binding has already been evaluated for this param, and
		// we are now checking data set binding for the same param )
		else if ( !bindingResolved[i] )
		{
			Object value = ( cx != null )
					? evaluateInputParameterValue( this.scope, cx, binding )
					: binding.getExpr( );
			String valueStr = getParameterValueString( paramHints[i].getDataType( ),
					value );
			paramHints[i].setDefaultInputValue( valueStr );
			bindingResolved[i] = true;

			// Also give the value to data set RT for script access
			if ( cx != null
					&& paramHints[i].isInputMode( )
					&& paramHints[i].getName( ) != null )
			{
				try
				{
					this.dsRT.setInputParameterValue( paramHints[i].getName( ),
							value );
				}
				catch ( BirtException e )
				{
					// Unexpected 
					assert false;
					throw DataException.wrap( e );
				}
			}
		}
	}

	/**
	 * Find index of matching parameter hint in paramHints array, based on
	 * param name or position.
	 * Returns index of param hint found in array, or -1 if no match
	 */
	private int findParameterHint( ParameterHint[] hints, int position,
			String name )
	{
		for ( int i = 0; i < hints.length; i++ )
		{
			ParameterHint paramHint = hints[i];
			if ( position <= 0 )
			{
				if ( paramHint.getName( ).equalsIgnoreCase( name ) )
					return i;
			}
			else
			{
				if ( paramHint.getPosition( ) == position )
					return i;
			}
		}
		return -1;
	}

	/**
	 * @param scope
	 * @param cx
	 * @param iParamBind
	 * @return
	 * @throws DataException
	 */
	private Object evaluateInputParameterValue( Scriptable scope, Context cx,
			IInputParameterBinding iParamBind ) throws DataException
	{
		// Evaluate Expression:
		// If the expression has been prepared, 
		// use its handle to getValue() from outerResultIterator
		// else use Rhino to evaluate in corresponding scope
		Object evaluateResult = null;
		Scriptable evaluateScope = scope;
		
		if ( outerResults != null )
		{
			try
			{
				evaluateResult = ExprEvaluateUtil.evaluateRawExpression2( iParamBind.getExpr( ),
						outerResults.getQueryScope( ) );
			}
			catch ( BirtException e )
			{
				//do not expect a exception here.
				DataException dataEx = new DataException( ResourceConstants.UNEXPECTED_ERROR,
						e );
				if ( logger != null )
					logger.logp( Level.FINE,
							PreparedOdaDSQuery.class.getName( ),
							"getMergedParameters",
							"Error occurs in IQueryResults.getResultIterator()",
							e );
				throw dataEx;
			}
		}
		
		if ( evaluateResult == null )
			evaluateResult = ScriptEvalUtil.evalExpr( iParamBind.getExpr( ),
					cx,
					evaluateScope,
					"ParamBinding(" + iParamBind.getName( ) + ")",
					0 );

		if ( evaluateResult == null )
			throw new DataException( ResourceConstants.DEFAULT_INPUT_PARAMETER_VALUE_CANNOT_BE_NULL );
		return evaluateResult;
	}

	/**
	 * Converts a parameter value to a String expected by ParameterHint
	 */
	private String getParameterValueString( Class paramType, Object paramValue )
			throws DataException
	{
		if ( paramValue instanceof String )
			return (String) paramValue;
		
		// Type conversion note: An integer constant like "1" will be
		// interpreted as a floating number by Rhino, which will then be converted
		// to "1.0", which some drivers don't like.
		// So we will first convert the value to the type required by the input
		// parameter, then convert it to String. This guarantees that an input
		// value "1" will be pass on as "1", and not "1.0"
		try
		{
			paramValue = DataTypeUtil.convert( paramValue, paramType );
			return DataTypeUtil.toString( paramValue );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
		}
	}

	/**
	 * Create a parameter hint based on Parameter definition and value
	 * @param paramDefn
	 * @param evaValue
	 */
	private ParameterHint createParameterHint( IParameterDefinition paramDefn,
			Object paramValue ) throws DataException
	{
		ParameterHint parameterHint = new ParameterHint( paramDefn.getName( ),
				paramDefn.isInputMode( ),
				paramDefn.isOutputMode( ) );
		if ( paramDefn.getPosition( ) > 0 )
			parameterHint.setPosition( paramDefn.getPosition( ) );

		// following data types is not supported by odaconsumer currently
		Class dataTypeClass = DataType.getClass( paramDefn.getType( ) );
		if ( dataTypeClass == DataType.AnyType.class
				|| dataTypeClass == Boolean.class
				|| dataTypeClass == Blob.class )
		{
			dataTypeClass = String.class;
		}
		parameterHint.setDataType( dataTypeClass );
        
        parameterHint.setNativeDataType( paramDefn.getNativeType() );
		parameterHint.setIsInputOptional( paramDefn.isInputOptional( ) );
		if ( parameterHint.isInputMode( ) )
			parameterHint.setDefaultInputValue( getParameterValueString( dataTypeClass,
					paramValue ) );
		parameterHint.setIsNullable( paramDefn.isNullable( ) );
		return parameterHint;
	}

}
