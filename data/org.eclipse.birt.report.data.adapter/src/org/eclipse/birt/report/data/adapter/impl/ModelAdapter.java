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
import org.mozilla.javascript.Scriptable;

/**
 * An adaptor to create Data Engine request objects based on Model element definitions
 */
public class ModelAdapter implements IModelAdapter
{
	DataSessionContext context;
	
	ModelAdapter( DataSessionContext context)
	{
		this.context = context;
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSource(org.eclipse.birt.report.model.api.DataSourceHandle)
	 */
	public BaseDataSourceDesign adaptDataSource( DataSourceHandle handle ) 
		throws BirtException
	{
		if ( handle instanceof OdaDataSourceHandle )
		{
			// If an external top level scope is available (i.e., our consumer
			// is the report engine), use it to resolve property bindings. Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope() ?
					context.getTopScope() : null;
			return new OdaDataSourceAdapter( ( OdaDataSourceHandle ) handle, 
					propBindingScope );
		}
		
		if ( handle instanceof ScriptDataSourceHandle )
			return new ScriptDataSourceAdapter( ( ScriptDataSourceHandle ) handle); 
		
		assert false;
		return null;
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptDataSet(org.eclipse.birt.report.model.api.DataSetHandle)
	 */
	public BaseDataSetDesign adaptDataSet( DataSetHandle handle ) 
		throws BirtException
	{
		if ( handle instanceof OdaDataSetHandle )
		{
			// If an external top level scope is available (i.e., our consumer
			// is the report engine), use it to resolve property bindings. Otherwise
			// property bindings are not resolved
			Scriptable propBindingScope = context.hasExternalScope() ?
					context.getTopScope() : null;
			return new OdaDataSetAdapter( ( OdaDataSetHandle ) handle,
					propBindingScope, this );
		}
		
		if ( handle instanceof ScriptDataSetHandle )
			return new ScriptDataSetAdapter( ( ScriptDataSetHandle ) handle, this );
		
		if ( handle instanceof JointDataSetHandle )
			return new JointDataSetAdapter( (JointDataSetHandle) handle, this );

		// other types are not supported
		assert false;
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
	public ScriptExpression adaptExpression( String exprText, String dataType )
	{
		return new ExpressionAdapter( exprText, dataType );
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptExpression(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public ScriptExpression adaptExpression( ComputedColumnHandle ccHandle )
	{
		return new ExpressionAdapter( ccHandle );
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptFilter(org.eclipse.birt.report.model.api.FilterConditionHandle)
	 */
	public FilterDefinition adaptFilter( FilterConditionHandle modelFilter  )
	{
		return new FilterAdapter( modelFilter );
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptGroup(org.eclipse.birt.report.model.api.GroupHandle)
	 */
	public GroupDefinition adaptGroup( GroupHandle groupHandle )
	{
		return new GroupAdapter( groupHandle );
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(org.eclipse.birt.report.model.api.SortKeyHandle)
	 */
	public SortDefinition adaptSort( SortKeyHandle sortHandle )
	{
		return new SortAdapter( sortHandle );
	}

	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptSort(java.lang.String, java.lang.String)
	 */
	public SortDefinition adaptSort( String sortKeyExpr, String direction )
	{
		return new SortAdapter( sortKeyExpr, direction );
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptParameter(org.eclipse.birt.report.model.api.DataSetParameterHandle)
	 */
	public ParameterDefinition adaptParameter( DataSetParameterHandle paramHandle )
	{
		return new ParameterAdapter( paramHandle );
	}
	
	/**
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptInputParamBinding(org.eclipse.birt.report.model.api.ParamBindingHandle)
	 */
	public InputParameterBinding adaptInputParamBinding( ParamBindingHandle modelHandle )
	{
		return new InputParamBindingAdapter( modelHandle);
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
		return new ComputedColumnAdapter( modelHandle);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptBinding(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public IBinding adaptBinding( ComputedColumnHandle handle ) throws AdapterException
	{
		if( handle == null )
			return null;
		Binding result = new Binding( handle.getName( ));
		if ( handle.getExpression( )!= null )
			result.setExpression( new ScriptExpression( handle.getExpression( ) ) );
		result.setDataType( org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType( handle.getDataType( ) ) );
		result.setAggrFunction( org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelAggregationType( handle.getAggregateFunction( ) ) );
		result.setFilter( handle.getFilterExpression( ) == null ? null
				: new ScriptExpression( handle.getFilterExpression( ) ) );
		populateArgument( result, handle );

		populateAggregateOns( result, handle );
		return result;
		
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
	private static void populateArgument( IBinding binding,
			ComputedColumnHandle modelCmptdColumn ) throws AdapterException
	{

		Iterator it = modelCmptdColumn.argumentsIterator( );
		while ( it != null && it.hasNext( ) )
		{
			AggregationArgumentHandle arg = (AggregationArgumentHandle) it.next( );
			try
			{
				binding.addArgument( new ScriptExpression( arg.getValue( ) ) );
			}
			catch ( DataException e )
			{
				throw new AdapterException( e.getLocalizedMessage( ), e );
			}
		}

	}
	
}
