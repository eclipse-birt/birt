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

package org.eclipse.birt.data.engine.executor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.IParameterDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptDataSourceDesign;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.odaconsumer.ParameterHint;

/**
 * Wrap the design of data source and data set. This class will determins
 * whether the equality of two data sets, which will be used if data set needs
 * to be put into map. To ensure it works correctly, a comprehensive junit test
 * case should be designed for it.
 */
public class DataSourceAndDataSet
{
	/** current data source and data set for cache*/
	private IBaseDataSourceDesign dataSourceDesign;
	private IBaseDataSetDesign dataSetDesign;
	private Collection paramterHints;

	private final static int B_FALSE = 0;
	private final static int B_UNKNOWN = 1;
	private final static int B_TRUE = 2;

	/**
	 * @param dataSourceDesign
	 * @param dataSetDesign
	 * @return
	 */
	public static DataSourceAndDataSet newInstance(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSetDesign dataSetDesign, Collection paramterHints )
	{
		DataSourceAndDataSet dataSourceAndSet = new DataSourceAndDataSet( );
		dataSourceAndSet.dataSourceDesign = dataSourceDesign;
		dataSourceAndSet.dataSetDesign = dataSetDesign;
		dataSourceAndSet.paramterHints = paramterHints;
		
		return dataSourceAndSet;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode( )
	{
		int hashCode = 0;
		if ( dataSourceDesign != null )
			hashCode += dataSourceDesign.getName( ).hashCode( );
		if ( dataSetDesign != null )
			hashCode += dataSetDesign.getName( ).hashCode( );
		return hashCode;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
		if ( obj == null || obj instanceof DataSourceAndDataSet == false )
			return false;

		if ( this == obj )
			return true;

		IBaseDataSourceDesign dataSourceDesign2 = ( (DataSourceAndDataSet) obj ).dataSourceDesign;
		IBaseDataSetDesign dataSetDesign2 = ( (DataSourceAndDataSet) obj ).dataSetDesign;
		Collection paramterHints2 = ( (DataSourceAndDataSet) obj ).paramterHints;

		if ( this.dataSourceDesign == dataSourceDesign2 )
		{
			if ( this.dataSetDesign == dataSetDesign2 )
			{
				if ( isEqualParamterHints( this.paramterHints,
						paramterHints2 ) )
					return true;
			}
			else if ( this.dataSetDesign == null || dataSetDesign2 == null )
			{
				return false;
			}
		}
		else if ( this.dataSourceDesign == null || dataSourceDesign2 == null )
		{
			return false;
		}
		else
		{
			if ( ( this.dataSetDesign != dataSetDesign2 )
					&& ( this.dataSetDesign == null || dataSetDesign2 == null ) )
				return false;
		}

		// data source compare
		if ( isEqualDataSourceDesign( dataSourceDesign, dataSourceDesign2 ) == false )
			return false;

		// data set compare
		if ( isEqualDataSetDesign( dataSetDesign, dataSetDesign2 ) == false )
			return false;

		// parameter bindings compare
		if ( this.isEqualParamterHints( this.paramterHints,
				paramterHints2 ) == false )
			return false;

		return true;
	}

	/**
	 * Only for non-collection object
	 * 
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	private boolean isEqualObject( Object ob1, Object ob2 )
	{
		if ( ob1 == ob2 )
			return true;
		else if ( ob1 == null || ob2 == null )
			return false;

		return ob1.equals( ob2 );
	}

	/**
	 * @param str1
	 * @param str2
	 * @return
	 */
	private boolean isEqualString( String str1, String str2 )
	{
		return isEqualObject( str1, str2 );
	}

	/**
	 * @param map1
	 * @param map2
	 * @return
	 */
	private boolean isEqualProps( Map map1, Map map2 )
	{
		if ( map1 == map2 )
		{
			return true;
		}
		else if ( map1 == null || map2 == null )
		{
			if ( map1 == null )
			{
				if ( map2.size( ) != 0 )
					return false;
				else
					return true;
			}
			else
			{
				if ( map1.size( ) != 0 )
					return false;
				else
					return true;
			}
		}
		else if ( map1.keySet( ).size( ) != map2.keySet( ).size( ) )
		{
			return false;
		}

		Set set = map1.keySet( );
		Iterator it = set.iterator( );
		while ( it.hasNext( ) )
		{
			Object ob = it.next( );
			Object value1 = map1.get( ob );
			Object value2 = map1.get( ob );

			if ( isEqualObject( value1, value2 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @param col1
	 * @param col2
	 * @return
	 */
	private int isEqualBasicCol( Collection col1, Collection col2 )
	{
		if ( col1 == col2 )
		{
			return B_TRUE;
		}
		else if ( col1 == null || col2 == null )
		{
			if ( col1 == null )
			{
				if ( col2.size( ) == 0 )
					return B_TRUE;
				else
					return B_FALSE;
			}
			else
			{
				if ( col1.size( ) == 0 )
					return B_TRUE;
				else
					return B_FALSE;
			}
		}
		else
		{
			if ( col1.size( ) == col2.size( ) )
				return B_UNKNOWN;
			else
				return B_FALSE;
		}
	}

	/**
	 * Filter does not affect the raw data and metadata
	 * @param filter1
	 * @param filter2
	 * @return
	 */
	private boolean isEqualFilters( List filter1, List filter2 )
	{
		return true;
	}

	/**
	 * 
	 * @param be
	 * @param be2
	 * @return
	 */
	private boolean isEqualExpression( IBaseExpression be, IBaseExpression be2 )
	{
		if ( be == be2 )
			return true;
		else if ( be == null || be2 == null )
			return false;

		if ( be instanceof IScriptExpression
				&& be2 instanceof IScriptExpression )
		{
			IScriptExpression se = (IScriptExpression) be;
			IScriptExpression se2 = (IScriptExpression) be2;
			return isEqualExpression2( se, se2 );
		}
		else if ( be instanceof IConditionalExpression
				&& be2 instanceof IConditionalExpression )
		{
			IConditionalExpression ce = (IConditionalExpression) be;
			IConditionalExpression ce2 = (IConditionalExpression) be2;
			return ce.getDataType( ) == ce2.getDataType( )
					&& ce.getOperator( ) == ce2.getOperator( )
					&& isEqualExpression2( ce.getExpression( ),
							ce2.getExpression( ) )
					&& isEqualExpression2( ce.getOperand1( ), ce2.getOperand1( ) )
					&& isEqualExpression2( ce.getOperand2( ), ce2.getOperand2( ) );
		}

		return false;
	}

	/**
	 * @param se
	 * @param se2
	 * @return
	 */
	private boolean isEqualExpression2( IScriptExpression se,
			IScriptExpression se2 )
	{
		if ( se == se2 )
			return true;
		else if ( se == null || se2 == null )
			return false;

		return se.getDataType( ) == se2.getDataType( )
				&& isEqualString( se.getText( ), se2.getText( ) );
	}

	/**
	 * compare data source design
	 * @param dataSourceDesign
	 * @param dataSourceDesign2
	 * @return
	 */
	private boolean isEqualDataSourceDesign(
			IBaseDataSourceDesign dataSourceDesign,
			IBaseDataSourceDesign dataSourceDesign2 )
	{
		if ( dataSourceDesign == dataSourceDesign2 )
			return true;

		if ( dataSourceDesign == null || dataSourceDesign2 == null )
			return false;
		
		if ( !isEqualString( dataSourceDesign.getName( ), dataSourceDesign2.getName( )) )
			return false;
		
		if ( isEqualString( dataSourceDesign.getBeforeOpenScript( ),
				dataSourceDesign2.getBeforeOpenScript( ) ) == false
				|| isEqualString( dataSourceDesign.getAfterOpenScript( ),
						dataSourceDesign2.getAfterOpenScript( ) ) == false
				|| isEqualString( dataSourceDesign.getBeforeCloseScript( ),
						dataSourceDesign2.getBeforeCloseScript( ) ) == false
				|| isEqualString( dataSourceDesign.getAfterCloseScript( ),
						dataSourceDesign2.getAfterCloseScript( ) ) == false )
			return false;

		if ( dataSourceDesign instanceof IOdaDataSourceDesign
				&& dataSourceDesign2 instanceof IOdaDataSourceDesign )
		{
			IOdaDataSourceDesign dataSource = (IOdaDataSourceDesign) dataSourceDesign;
			IOdaDataSourceDesign dataSource2 = (IOdaDataSourceDesign) dataSourceDesign2;

			if ( isEqualString( dataSource.getExtensionID( ),
					dataSource2.getExtensionID( ) ) == false )
				return false;

			if ( isEqualProps( dataSource.getPublicProperties( ),
					dataSource2.getPublicProperties( ) ) == false
					|| isEqualProps( dataSource.getPrivateProperties( ),
							dataSource2.getPrivateProperties( ) ) == false )
				return false;
		}
		else if ( dataSourceDesign instanceof IScriptDataSourceDesign
				&& dataSourceDesign2 instanceof IScriptDataSourceDesign )
		{
			IScriptDataSourceDesign dataSource = (IScriptDataSourceDesign) dataSourceDesign;
			IScriptDataSourceDesign dataSource2 = (IScriptDataSourceDesign) dataSourceDesign2;

			if ( isEqualString( dataSource.getOpenScript( ),
					dataSource2.getOpenScript( ) ) == false
					|| isEqualString( dataSource.getCloseScript( ),
							dataSource2.getCloseScript( ) ) == false )
				return false;
		}
		return true;
	}

	/**
	 * compare data set design
	 * @param dataSetDesign
	 * @param dataSetDesign2
	 * @return
	 */
	private boolean isEqualDataSetDesign( IBaseDataSetDesign dataSetDesign,
			IBaseDataSetDesign dataSetDesign2 )
	{
		if ( dataSetDesign == dataSetDesign2 )
			return true;

		if ( dataSetDesign == null || dataSetDesign2 == null )
			return false;

		if ( !isEqualString( dataSetDesign.getName( ), dataSetDesign2.getName( )))
			return false;
		
		if ( isEqualString( dataSetDesign.getBeforeOpenScript( ),
				dataSetDesign2.getBeforeOpenScript( ) ) == false
				|| isEqualString( dataSetDesign.getAfterOpenScript( ),
						dataSetDesign2.getAfterOpenScript( ) ) == false
				|| isEqualString( dataSetDesign.getBeforeCloseScript( ),
						dataSetDesign2.getBeforeCloseScript( ) ) == false
				|| isEqualString( dataSetDesign.getAfterCloseScript( ),
						dataSetDesign2.getAfterCloseScript( ) ) == false )
			return false;

		if ( isEqualComputedColumns( dataSetDesign.getComputedColumns( ),
				dataSetDesign2.getComputedColumns( ) ) == false
				|| isEqualFilters( dataSetDesign.getFilters( ),
						dataSetDesign2.getFilters( ) ) == false
				|| isEqualParameters( dataSetDesign.getParameters( ),
						dataSetDesign2.getParameters( ) ) == false
				|| isEqualResultHints( dataSetDesign.getResultSetHints( ),
						dataSetDesign2.getResultSetHints( ) ) == false )
			return false;

		if ( dataSetDesign instanceof IOdaDataSetDesign
				&& dataSetDesign2 instanceof IOdaDataSetDesign )
		{
			IOdaDataSetDesign dataSet = (IOdaDataSetDesign) dataSetDesign;
			IOdaDataSetDesign dataSet2 = (IOdaDataSetDesign) dataSetDesign2;

			if ( isEqualString( dataSet.getQueryText( ),
					dataSet2.getQueryText( ) ) == false
					|| isEqualString( dataSet.getExtensionID( ),
							dataSet2.getExtensionID( ) ) == false
					|| isEqualString( dataSet.getPrimaryResultSetName( ),
							dataSet2.getPrimaryResultSetName( ) ) == false
					|| isEqualProps( dataSet.getPublicProperties( ),
							dataSet2.getPublicProperties( ) ) == false
					|| isEqualProps( dataSet.getPrivateProperties( ),
							dataSet2.getPrivateProperties( ) ) == false )
				return false;
		}
		else if ( dataSetDesign instanceof IScriptDataSetDesign
				&& dataSetDesign2 instanceof IScriptDataSetDesign )
		{
			IScriptDataSetDesign dataSet = (IScriptDataSetDesign) dataSetDesign;
			IScriptDataSetDesign dataSet2 = (IScriptDataSetDesign) dataSetDesign2;

			if ( isEqualString( dataSet.getOpenScript( ),
					dataSet2.getOpenScript( ) ) == false
					|| isEqualString( dataSet.getFetchScript( ),
							dataSet2.getFetchScript( ) ) == false
					|| isEqualString( dataSet.getCloseScript( ),
							dataSet2.getCloseScript( ) ) == false
					|| isEqualString( dataSet.getDescribeScript( ),
							dataSet2.getDescribeScript( ) ) == false )
				return false;
		}
		else if ( dataSetDesign instanceof IJointDataSetDesign
				&& dataSetDesign2 instanceof IJointDataSetDesign )
		{
			IJointDataSetDesign design1 = (IJointDataSetDesign) dataSetDesign;
			IJointDataSetDesign design2 = (IJointDataSetDesign) dataSetDesign2;
			if ( isEqualString( design1.getLeftDataSetDesignName( ),
					design2.getLeftDataSetDesignName( ) ) == false
					|| isEqualString( design1.getRightDataSetDesignName( ),
							design2.getRightDataSetDesignName( ) ) == false
					|| design1.getJoinType( ) != design2.getJoinType( )
					|| isEqualJointCondition( design1.getJoinConditions( ),
							design2.getJoinConditions( ) ) == false )
				return false;
		}
		else
		{
			return false;
		}
		return true;
	}

	/**
	 * Very special for computed column, temp computed column can not be counted
	 * as the real computed column
	 * 
	 * @param computedCol1
	 * @param computedCol2
	 * @return
	 */
	private boolean isEqualComputedColumns( List computedCol1, List computedCol2 )
	{
		if ( computedCol1 == computedCol2 )
			return true;

		List newComputedCol1 = getRealComputedColumn( computedCol1 );
		List newComputedCol2 = getRealComputedColumn( computedCol2 );

		int basicCol = isEqualBasicCol( newComputedCol1, newComputedCol2 );
		if ( basicCol == B_TRUE )
			return true;
		else if ( basicCol == B_FALSE )
			return false;

		Iterator it = newComputedCol1.iterator( );
		Iterator it2 = newComputedCol2.iterator( );
		while ( it.hasNext( ) )
		{
			IComputedColumn cc = (IComputedColumn) it.next( );
			IComputedColumn cc2 = (IComputedColumn) it2.next( );
			if ( isEqualComputedCol( cc, cc2 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @param computedCols
	 * @return
	 */
	private List getRealComputedColumn( List computedCols )
	{
		if ( computedCols == null )
			return null;

		List list = new ArrayList( );
		for ( int i = 0; i < computedCols.size( ); i++ )
		{
			IComputedColumn cc = (IComputedColumn) computedCols.get( i );
			if ( cc.getName( ).matches( "\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E" )
					|| cc.getName( ).matches( "\\Q_{$TEMP_SORT_\\E\\d*\\Q$}_\\E" )
					|| cc.getName( ).matches( "\\Q_{$TEMP_FILTER_\\E\\d*\\Q$}_\\E" ))
				continue;
			else
				list.add( cc );
		}

		return list;
	}
	
	/**
	 * @param cc
	 * @param cc2
	 * @return
	 */
	private boolean isEqualComputedCol( IComputedColumn cc, IComputedColumn cc2 )
	{
		return cc.getDataType( ) == cc2.getDataType( )
				&& isEqualString( cc.getName( ), cc2.getName( ) )
				&& isEqualExpression( cc.getExpression( ), cc2.getExpression( ) );
	}

	/**
	 * @param params1
	 * @param params2
	 * @return
	 */
	private boolean isEqualParameters( List params1, List params2 )
	{
		if ( params1 == params2 )
			return true;

		int basicCol = isEqualBasicCol( params1, params2 );
		if ( basicCol == B_TRUE )
			return true;
		else if ( basicCol == B_FALSE )
			return false;

		Iterator it = params1.iterator( );
		Iterator it2 = params2.iterator( );
		while ( it.hasNext( ) )
		{
			IParameterDefinition pd = (IParameterDefinition) it.next( );
			IParameterDefinition pd2 = (IParameterDefinition) it2.next( );
			if ( isEqualParameter( pd, pd2 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @param pd
	 * @param pd2
	 * @return
	 */
	private boolean isEqualParameter( IParameterDefinition pd,
			IParameterDefinition pd2 )
	{
		return pd.getPosition( ) == pd2.getPosition( )
				&& pd.getType( ) == pd2.getType( )
                && pd.getNativeType( ) == pd2.getNativeType( )
				&& pd.isInputMode( ) == pd2.isInputMode( )
				&& pd.isInputOptional( ) == pd2.isInputOptional( )
				&& pd.isNullable( ) == pd2.isNullable( )
				&& pd.isOutputMode( ) == pd2.isOutputMode( )
				&& isEqualString( pd.getDefaultInputValue( ),
						pd2.getDefaultInputValue( ) );
	}

	/**
	 * @param paramsBinding1
	 * @param paramsBinding2
	 * @return
	 */
	private boolean isEqualParamterHints( Collection paramsBinding1,
			Collection paramsBinding2 )
	{
		if ( paramsBinding1 == paramsBinding2 )
			return true;

		int basicCol = isEqualBasicCol( paramsBinding1, paramsBinding2 );
		if ( basicCol == B_TRUE )
			return true;
		else if ( basicCol == B_FALSE )
			return false;

		Iterator it = paramsBinding1.iterator( );
		Iterator it2 = paramsBinding2.iterator( );
		while ( it.hasNext( ) )
		{
			ParameterHint pb = (ParameterHint) it.next( );
			ParameterHint pb2 = (ParameterHint) it2.next( );
			if ( isEqualParameterHint( pb, pb2 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @param pb
	 * @param pb2
	 * @return
	 */
	private boolean isEqualParameterHint( ParameterHint pb,
			ParameterHint pb2 )
	{
		return pb.getPosition( ) == pb2.getPosition( )
				&& isEqualString( pb.getName( ), pb2.getName( ) )
				&& isEqualString( pb.getDefaultInputValue( ),
						pb2.getDefaultInputValue( ) )
				&& isEqualString( pb.getDataType( ).toString( ),
						pb2.getDataType( ).toString( ) )
                && pb.getNativeDataType() == pb2.getNativeDataType();
	}

	/**
	 * 
	 * @param resultHints1
	 * @param resultHints2
	 * @return
	 */
	private boolean isEqualResultHints( List resultHints1, List resultHints2 )
	{
		if ( resultHints1 == resultHints2 )
			return true;

		int basicCol = isEqualBasicCol( resultHints1, resultHints2 );
		if ( basicCol == B_TRUE )
			return true;
		else if ( basicCol == B_FALSE )
			return false;

		Iterator it = resultHints1.iterator( );
		Iterator it2 = resultHints2.iterator( );
		while ( it.hasNext( ) )
		{
			IColumnDefinition cd = (IColumnDefinition) it.next( );
			IColumnDefinition cd2 = (IColumnDefinition) it2.next( );
			if ( isEqualColumnDefn( cd, cd2 ) == false )
				return false;
		}

		return true;
	}

	/**
	 * @param cd
	 * @param cd2
	 * @return
	 */
	private boolean isEqualColumnDefn( IColumnDefinition cd,
			IColumnDefinition cd2 )
	{
		if ( cd == cd2 )
			return true;
		else if ( cd == null || cd2 == null )
			return false;

		return cd.getColumnPosition( ) == cd2.getColumnPosition( )
				&& cd.getDataType( ) == cd2.getDataType( )
                && cd.getNativeDataType( ) == cd2.getNativeDataType( )
				&& cd.getExportHint( ) == cd2.getExportHint( )
				&& cd.getSearchHint( ) == cd2.getSearchHint( )
				&& isEqualString( cd.getAlias( ), cd2.getAlias( ) )
				&& isEqualString( cd.getColumnName( ), cd2.getColumnName( ) );
	}

	/**
	 * compare joint condition
	 * @param joinConditions1
	 * @param joinConditions2
	 * @return
	 */
	private boolean isEqualJointCondition( List joinConditions1,
			List joinConditions2 )
	{
		if ( joinConditions1 == joinConditions2 )
			return true;
		int basicCol = isEqualBasicCol( joinConditions1, joinConditions2 );
		if ( basicCol == B_TRUE )
			return true;
		else if ( basicCol == B_FALSE )
			return false;

		Iterator it = joinConditions1.iterator( );
		Iterator it2 = joinConditions2.iterator( );

		while ( it.hasNext( ) || it2.hasNext( ) )
		{
			IJoinCondition cc = (IJoinCondition) it.next( );
			IJoinCondition cc2 = (IJoinCondition) it2.next( );
			if ( isEqualJointConditionItem( cc, cc2 ) == false )
				return false;
		}
		return true;
	}

	/**
	 * compare 
	 * @param cc1
	 * @param cc2
	 * @return
	 */
	private boolean isEqualJointConditionItem( IJoinCondition cc1,
			IJoinCondition cc2 )
	{
		if ( cc1 == cc2 )
			return true;
		else if ( cc1 == null || cc2 == null )
			return false;

		return isEqualExpression2( cc1.getLeftExpression( ),
				cc2.getLeftExpression( ) )
				&& isEqualExpression2( cc1.getRightExpression( ),
						cc2.getRightExpression( ) )
				&& cc1.getOperator( ) == cc2.getOperator( );
	}
}
