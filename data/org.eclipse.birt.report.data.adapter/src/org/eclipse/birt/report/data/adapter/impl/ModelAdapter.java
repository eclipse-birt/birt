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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ColumnAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ComputedColumnAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ConditionAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ExpressionAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.FilterAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.GroupAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.InputParamBindingAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.JointDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.OdaDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.OdaDataSourceAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ParameterAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ScriptDataSetAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.ScriptDataSourceAdapter;
import org.eclipse.birt.report.data.adapter.internal.adapter.SortAdapter;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.mozilla.javascript.Scriptable;

/**
 * An adaptor to create Data Engine request objects based on Model element definitions
 */
public class ModelAdapter implements IModelAdapter
{
	private static Logger logger = Logger.getLogger( ModelAdapter.class.getName( ) );
	DataSessionContext context;
	
	ModelAdapter( DataSessionContext context)
	{
		this.context = context;
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSource(org.eclipse.birt.report.model.api.DataSourceHandle)
	 */
	public BaseDataSourceDesign adaptDataSource( DataSourceHandle handle ) throws BirtException
	{
		if ( handle instanceof OdaDataSourceHandle )
		{
			// If an external top level scope is available (i.e., our
			// consumer
			// is the report engine), use it to resolve property bindings.
			// Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope( )
					? context.getTopScope( ) : null;
			return new OdaDataSourceAdapter( (OdaDataSourceHandle) handle,
					propBindingScope );
		}

		if ( handle instanceof ScriptDataSourceHandle )
		{
			return new ScriptDataSourceAdapter( (ScriptDataSourceHandle) handle );
		}

		logger.warning( "handle type: " + (handle == null ? "" : handle.getClass( ).getName( )) ); //$NON-NLS-1$
		return null;

	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSet(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public BaseDataSetDesign adaptDataSet( DataSetHandle handle ) throws BirtException
	{
		if ( handle instanceof OdaDataSetHandle )
		{
			// If an external top level scope is available (i.e., our
			// consumer
			// is the report engine), use it to resolve property bindings.
			// Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope( )
					? context.getTopScope( ) : null;
			return new OdaDataSetAdapter( (OdaDataSetHandle) handle,
					propBindingScope,
					this );
		}

		if ( handle instanceof ScriptDataSetHandle )
			return new ScriptDataSetAdapter( (ScriptDataSetHandle) handle, this );

		if ( handle instanceof JointDataSetHandle )
			return new JointDataSetAdapter( (JointDataSetHandle) handle, this );
		logger.warning( "handle type: " + (handle == null ? "" : handle.getClass( ).getName( )) ); //$NON-NLS-1$
		return null;
	
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptConditionalExpression(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ConditionalExpression adaptConditionalExpression( 
			String mainExpr, String operator, String operand1, String operand2 )
	{
		return new ConditionAdapter( mainExpr, operator, operand1, operand2);
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptExpression(java.lang.String, java.lang.String)
	 */
	public ScriptExpression adaptExpression( Expression expr, String dataType )
	{
		if( expr == null )
			return null;
		return new ExpressionAdapter( expr, dataType );
	}
	
/*	*//**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptExpression(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 *//*
	public ScriptExpression adaptExpression( ComputedColumnHandle ccHandle )
	{
		return new ExpressionAdapter( ccHandle );
	}
*/
	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptFilter(org.eclipse.birt.report.model.api.FilterConditionHandle)
	 */
	public FilterDefinition adaptFilter( FilterConditionHandle modelFilter  )
	{
		try
		{
			return new FilterAdapter( this, modelFilter );
		}
		catch ( AdapterException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}
	
	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptGroup(org.eclipse.birt.report.model.api.GroupHandle)
	 */
	public GroupDefinition adaptGroup( GroupHandle groupHandle )
	{
		try
		{
			return new GroupAdapter( this, groupHandle );
		}
		catch ( AdapterException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}

	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(org.eclipse.birt.report.model.api.SortKeyHandle)
	 */
	public SortDefinition adaptSort( SortKeyHandle sortHandle )
	{
		try
		{
			return new SortAdapter( this, sortHandle );
		}
		catch ( AdapterException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}

	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(java.lang.String, java.lang.String)
	 */
	public SortDefinition adaptSort( Expression expr, String direction )
	{
		try
		{
			return new SortAdapter( this, expr, direction );
		}
		catch ( AdapterException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptParameter(org.eclipse.birt.report.model.api.DataSetParameterHandle)
	 */
	public ParameterDefinition adaptParameter( DataSetParameterHandle paramHandle )
	{
		return new ParameterAdapter( paramHandle );
	}
	
	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptInputParamBinding(org.eclipse.birt.report.model.api.ParamBindingHandle)
	 */
	public InputParameterBinding adaptInputParamBinding( ParamBindingHandle modelHandle )
	{
		try
		{
			return new InputParamBindingAdapter( this, modelHandle);
		}
		catch ( AdapterException e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#ColumnAdaptor(org.eclipse.birt.report.model.api.ResultSetColumnHandle)
	 */
	public ColumnDefinition ColumnAdaptor( ResultSetColumnHandle modelColumn )
	{
		return new ColumnAdapter( modelColumn);
	}
	
	/**
	 * @throws AdapterException 
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptComputedColumn(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public ComputedColumn adaptComputedColumn( ComputedColumnHandle modelHandle ) throws AdapterException
	{
		return new ComputedColumnAdapter( this, modelHandle);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptBinding(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public IBinding adaptBinding( ComputedColumnHandle handle )
	{
		try
		{
			if ( handle == null )
				return null;
			Binding result = new Binding( handle.getName( ) );
			if ( handle.getExpression( ) != null )
			{
				ScriptExpression expr = this.adaptExpression( (Expression) handle.getExpressionProperty( org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.EXPRESSION_MEMBER )
						.getValue( ),
						handle.getDataType( ) );
				expr.setGroupName( handle.getAggregateOn( ) );
				result.setExpression( expr );
			}
			result.setDisplayName( handle.getDisplayName( ) );
			result.setDataType( org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( handle.getDataType( ) ) );
			result.setAggrFunction( org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelAggregationType( handle.getAggregateFunction( ) ) );
			result.setFilter( handle.getFilterExpression( ) == null
					? null
					: this.adaptExpression( (Expression) handle.getExpressionProperty( org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.FILTER_MEMBER )
							.getValue( ),
							DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) );
			populateArgument( result, handle );

			populateAggregateOns( result, handle );
			return result;
		}
		catch ( Exception e )
		{
			logger.log( Level.WARNING, e.getMessage( ), e );
			return null;
		}

	}

	/**
	 * 
	 * @param handle
	 * @param result
	 * @throws AdapterException
	 */
	private void populateAggregateOns( IBinding result,
			ComputedColumnHandle handle ) throws AdapterException
	{
		List aggrOns = handle.getAggregateOnList( );
		if ( aggrOns == null )
			return;
		for ( int i = 0; i < aggrOns.size( ); i++ )
		{
			try
			{
				result.addAggregateOn( aggrOns.get( i ).toString( ) );
			}
			catch ( DataException e )
			{
				throw new AdapterException( e.getLocalizedMessage( ), e );
			}
		}
	}

	/**
	 * 
	 * @param binding
	 * @param modelCmptdColumn
	 * @throws AdapterException
	 */
	private void populateArgument( IBinding binding,
			ComputedColumnHandle modelCmptdColumn ) throws AdapterException
	{

		Iterator it = modelCmptdColumn.argumentsIterator( );
		while ( it != null && it.hasNext( ) )
		{
			AggregationArgumentHandle arg = (AggregationArgumentHandle) it.next( );
			try
			{
				Expression expr = (Expression)arg.getExpressionProperty( AggregationArgument.VALUE_MEMBER ).getValue( );
				if( expr != null )
					binding.addArgument( this.adaptExpression( expr ));
			}
			catch ( DataException e )
			{
				throw new AdapterException( e.getLocalizedMessage( ), e );
			}
		}

	}

	public ConditionalExpression adaptConditionalExpression(
			Expression mainExpr, String operator,
			Expression operand1, Expression operand2 )
	{
		return new ConditionAdapter( this.adaptExpression( mainExpr ), operator, this.adaptExpression( operand1 ), this.adaptExpression( operand2 ));
	}

	public ScriptExpression adaptExpression( Expression expr )
	{
		if( expr == null )
			return null;
		return new ExpressionAdapter( expr );
	}

	public ScriptExpression adaptExpression( String jsExpr, String dataType )
	{
		if( jsExpr == null )
			return null;
		return new ExpressionAdapter( jsExpr, dataType );
	}
	
}
