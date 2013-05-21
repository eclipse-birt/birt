package org.eclipse.birt.report.data.adapter.api;

import java.util.HashSet;
import java.util.Iterator;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class DataModelAdapterUtil 
{
	public static boolean isAggregationBinding( ComputedColumnHandle computedColumnHandle, ReportItemHandle itemHandle )
	{
		if( computedColumnHandle.getAggregateFunction( ) != null )
		{
			return true;
		}
		
		ExpressionHandle expressionHandle = computedColumnHandle.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER );		
		if( expressionHandle != null )
		{
			HashSet<String> names = getBindingRefNames( expressionHandle );
			
			DataSetHandle dataSetHandle = itemHandle.getDataSet();
			for( String name: names )
			{
				Iterator iter = itemHandle.columnBindingsIterator();
				while ( iter.hasNext() )
				{
					ComputedColumnHandle handle = (ComputedColumnHandle)iter.next();
					if ( handle.getName().equals( name ) 
							&& isAggregationBinding( handle, itemHandle ) )
					{
						return true;
					}
				}
				if( dataSetHandle != null && dataSetHandle.computedColumnsIterator() != null )
				{
					iter = dataSetHandle.computedColumnsIterator();
					while ( iter.hasNext() )
					{
						ComputedColumnHandle handle = (ComputedColumnHandle)iter.next();
						if ( handle.getName().equals( name ) 
								&& isAggregationBinding( handle, itemHandle ) )
						{
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	private static HashSet<String> getBindingRefNames( ExpressionHandle expressionHandle ) 
	{
		HashSet<String> result = new HashSet<String>();
		String expressionType = expressionHandle.getType();
		String expression = expressionHandle.getStringExpression();
		
		try 
		{
			result.addAll( ExpressionUtil.extractColumnExpressions( expression,
					ExpressionUtil.DATASET_ROW_INDICATOR ) );
			result.addAll( ExpressionUtil.extractColumnExpressions( expression,
					ExpressionUtil.ROW_INDICATOR ) );
		}
		catch (BirtException e) 
		{
		}
		
		return result;
	}
}
