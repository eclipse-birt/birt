/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.util.DataUtil;

public class Expression<T>
{
	protected T value;
	protected Class<T> type;

	public static <Type> Expression<Type> newConstant( Type value )
	{
		if ( value == null )
		{
			return null;
		}
		return new Expression<Type>( value, (Class<Type>)value.getClass( ) );
	}
	
	public static <Type> JSExpression<Type> newExpression( String expression, Class<Type> type )
	{
		return new JSExpression<Type>( expression, type );
	}

	public static JSExpression<String> newExpression( String expression )
	{
		return new JSExpression<String>( expression, String.class );
	}

	public static JSExpression<DimensionType> newDimensionExpression( String expression )
	{
		return new JSExpression<DimensionType>( expression, DimensionType.class );
	}

	protected Expression( T value, Class<T> type )
	{
		this.value = value;
		this.type = type;
	}

	public boolean isExpression( )
	{
		return false;
	}

	public T evaluate( ExecutionContext context ) throws BirtException
	{
		return value;
	}

	public T getValue( )
	{
		return value;
	}

	public Object getDesignValue( )
	{
		return value;
	}
	
	public Class<T> getType( )
	{
		return type;
	}
	
	public boolean equals( Object obj )
	{
		if ( obj == this )
			return true;
		if ( !( obj instanceof Expression<?> ) )
			return false;
		Expression<?> value = (Expression<?>)obj;
		return equals( value.isExpression( ), isExpression( ) )
				&& isSameType( value.getType( ), getType( ) )
				&& equals( value.getDesignValue( ), getDesignValue( ) );
	}
	
	public String toString( )
	{
		Object value = getDesignValue( );
		return value == null ? null : value.toString( );
	}

	private boolean equals( Object obj1, Object obj2 )
	{
		return obj1 == null ? obj2 == null : obj1.equals( obj2 );
	}
	
	private boolean isSameType(Class<?> class1, Class<?> clazz2 )
	{
		if ( class1 == clazz2 )
		{
			return true;
		}
		return isAssignableTo( class1, clazz2, Map.class ) || isAssignableTo( class1, clazz2, List.class);
	}

	private boolean isAssignableTo( Class<?> clazz1, Class<?> clazz2,
			Class<?> testClass )
	{
		return testClass.isAssignableFrom( clazz1 ) && testClass.isAssignableFrom( clazz2 );
	}
	
	public static class JSExpression<T> extends Expression<T>
	{

		private static final long serialVersionUID = -7309705570432813674L;
		private String expression;

		public JSExpression( String expression, Class<T> type )
		{
			super( null, type );
			this.expression = expression;
		}

		public boolean isExpression( )
		{
			return true;
		}

		@SuppressWarnings("unchecked")
		public T evaluate( ExecutionContext context ) throws BirtException
		{
			if ( expression != null )
			{
				Object tempValue = context.evaluate( expression );
				return DataUtil.convertType( tempValue, type );
			}
			return null;
		}

		public String getDesignValue( )
		{
			return expression;
		}

		public void setExpression( String expression )
		{
			this.expression = expression;
		}
	}
}
