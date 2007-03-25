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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
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
					propBindingScope );
		}
		
		if ( handle instanceof ScriptDataSetHandle )
			return new ScriptDataSetAdapter( ( ScriptDataSetHandle ) handle);
		
		if ( handle instanceof JointDataSetHandle )
			return new JointDataSetAdapter( (JointDataSetHandle) handle);

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
	 * @see org.eclipse.birt.report.data.adapter.api.IModelAdapter#adaptComputedColumn(org.eclipse.birt.report.model.api.ComputedColumnHandle)
	 */
	public ComputedColumn adaptComputedColumn( ComputedColumnHandle modelHandle )
	{
		return new ComputedColumnAdapter( modelHandle);
	}
	
	/**
	 * Adapts a Model data type (string) to Data Engine data type constant
	 * (integer) on column
	 */
	public static int adaptModelDataType( String modelDataType )
	{
		if ( modelDataType == null )
			return DataType.UNKNOWN_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
			return DataType.ANY_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER ) )
			return DataType.INTEGER_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING ) )
			return DataType.STRING_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME ) )
			return DataType.DATE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL ) )
			return DataType.DECIMAL_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT ) )
			return DataType.DOUBLE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME ) )
			return DataType.SQL_TIME_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE ) )
			return DataType.SQL_DATE_TYPE;
		if ( modelDataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) )
			return DataType.BOOLEAN_TYPE;
		return DataType.UNKNOWN_TYPE;
	}
	
}
