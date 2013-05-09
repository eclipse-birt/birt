package org.eclipse.birt.report.data.adapter.api;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;


public class LinkedDataSetUtil
{
	private static String GET_LINKED_DATA_MODEL_METHOD = "getLinkedDataModel";
	
	public static boolean bindToLinkedDataSet( ReportItemHandle reportItemHandle )
	{
		Method[] methods = reportItemHandle.getClass( ).getMethods( );
		for ( int i = 0; i < methods.length; i++ )
		{
			String name = methods[i].getName( );
			if ( name.equals( GET_LINKED_DATA_MODEL_METHOD ) )
			{
				Object result = null ;
				try
				{
					result = methods[i].invoke( reportItemHandle );
				}
				catch ( Exception e )
				{
					return false;
				}
				if ( result != null )
					return true;
			}
		}
		return false;
	}
	
	public static boolean isAggregationBinding( ComputedColumnHandle computed, ReportItemHandle handle )
	{
		if( computed.getAggregateFunction( ) != null )
			return true;
		Iterator iter = handle.getAvailableBindings( );
		Set aggregationBinding = new HashSet( );
		try
		{
			while( iter.hasNext() )
			{
				ComputedColumnHandle computedHandle = (ComputedColumnHandle)iter.next( );
				if( computedHandle.getAggregateFunction( ) != null )
				{
					String columnName = null;
					columnName = ExpressionUtil.getColumnName( computedHandle.getExpression( ) );
					if( columnName == null )
					{
						columnName = ExpressionUtil.getColumnBindingName( computedHandle.getExpression( ) );
						if( columnName != null )
							aggregationBinding.add( columnName );
					}
					else
					{
						aggregationBinding.add( columnName );
					}
				}
			}
			List referedColumn = ExpressionUtil.extractColumnExpressions( computed.getExpression( ) );
			for( int i = 0 ; i < referedColumn.size( ); i++ )
			{
				if( aggregationBinding.contains( referedColumn.get( i ) ) )
				{
					return true;
				}
			}
		}
		catch( BirtException ex)
		{
			return false;
		}
		return false;
	}
}
