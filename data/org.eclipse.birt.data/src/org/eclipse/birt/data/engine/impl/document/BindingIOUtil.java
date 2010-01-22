
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved.
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class BindingIOUtil
{
	/**
	 * 
	 * @param dos
	 * @param binding
	 * @throws DataException
	 */
	public static void saveBinding( DataOutputStream dos, IBinding binding ) throws DataException
	{
		int type = binding.getDataType( );
		String name = binding.getBindingName( );
		String function = binding.getAggrFunction( );
		IBaseExpression expr = binding.getExpression( );
		IBaseExpression filter = binding.getFilter( );
		List arguments = binding.getArguments( );
		List aggregateOn = binding.getAggregatOns( );
		
		try
		{
			//First write data type.
			IOUtil.writeInt( dos, type );
			
			//Then write Name
			IOUtil.writeString( dos, name );
			
			//Then write function
			IOUtil.writeString( dos, function );
			
			//Then write base expr
			ExprUtil.saveBaseExpr( dos, expr );
			
			//Then write filter
			ExprUtil.saveBaseExpr( dos, filter );
			
			//Then write argument size
			IOUtil.writeInt( dos, arguments.size( ) );
			
			for( int i = 0; i < arguments.size( ); i++ )
			{
				ExprUtil.saveBaseExpr( dos, (IBaseExpression)arguments.get( i ) );
			}
			
			IOUtil.writeInt( dos, aggregateOn.size( ) );
			
			for( int i = 0; i < aggregateOn.size( ); i++ )
			{
				IOUtil.writeString( dos, aggregateOn.get( i ).toString( ) );
			}
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ), e );
		}
	}
	
	public static IBinding loadBinding( DataInputStream dis ) throws IOException, DataException
	{
		int type = IOUtil.readInt( dis );
		String name = IOUtil.readString( dis );
		String function = IOUtil.readString( dis );
		IBaseExpression expr = ExprUtil.loadBaseExpr( dis );
		IBaseExpression filter = ExprUtil.loadBaseExpr( dis );
		
		Binding binding = new Binding( name );
		binding.setAggrFunction( function );
		binding.setDataType( type );
		binding.setExpression( expr );
		binding.setFilter( filter );
		
		int argSize = IOUtil.readInt( dis );
		for ( int i = 0; i < argSize; i++ )
		{
			binding.addArgument( ExprUtil.loadBaseExpr( dis ) );
		}
		
		int aggrSize = IOUtil.readInt( dis );
		for( int i = 0; i < aggrSize; i++ )
		{
			binding.addAggregateOn( IOUtil.readString( dis ) );
		}
		return binding;
	}
}
