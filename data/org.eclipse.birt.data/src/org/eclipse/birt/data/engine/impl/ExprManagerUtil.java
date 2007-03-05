/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * This is a utility class which is used to validate the colum bindings defined 
 * in ExprManager instance.
 */
public class ExprManagerUtil
{
	private ExprManager exprManager;
	
	/**
	 * No external instance
	 */
	private ExprManagerUtil( ExprManager em )
	{
		this.exprManager = em;
	}
	
	/**
	 *	This method tests whether column bindings in ExprManager is valid or not.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	public static void validateColumnBinding( ExprManager exprManager,
			IBaseQueryDefinition baseQueryDefn ) throws DataException
	{
		ExprManagerUtil util = new ExprManagerUtil( exprManager );
		
		util.checkColumnBindingExpression( baseQueryDefn );
		util.checkDependencyCycle( );
		util.checkGroupNameValidation( );
	}

	/**
	 *	Test whether high level group keys are depended on low level group keys.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException 
	 */
	private void checkGroupNameValidation( ) throws DataException
	{
		HashMap map = this.getGroupKeys( );
		Iterator it = map.keySet( ).iterator( );
		while ( it.hasNext( ) )
		{
			Integer level = (Integer)it.next( );
			exprManager.setEntryGroupLevel( level.intValue( ) );
			
			if(!ExpressionCompilerUtil.hasColumnRow( map.get( level ).toString( ), exprManager ))
			{
				exprManager.setEntryGroupLevel( ExprManager.OVERALL_GROUP );
				if( !isColumnBindingExist(map.get( level ).toString( ) ))
				{
					throw new DataException( ResourceConstants.COLUMN_BINDING_NOT_EXIST, map.get( level ).toString( ));
				}
				throw new DataException( ResourceConstants.INVALID_GROUP_KEY_COLUMN, new Object[]{ map.get( level ).toString( ), level});
			}
		}
		exprManager.setEntryGroupLevel( ExprManager.OVERALL_GROUP );
	}
	
	/**
	 * 
	 * @param columnName
	 * @return
	 */
	private boolean isColumnBindingExist( String columnName )
	{
		List bindings = exprManager.getBindingExprs( );
		
		for( int i = 0; i < bindings.size( ); i++ )
		{
			GroupBindingColumn gbc = (GroupBindingColumn)bindings.get( i );
			if(gbc.getExpression( columnName) != null)
				return true;
		}
		return false;
	}
	/**
	 * Test whether there are dependency cycles in exprManager.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private void checkDependencyCycle( ) throws DataException
	{
		List result = new ArrayList( );
		Iterator it = this.getColumnNames( ).iterator( );

		while ( it.hasNext( ) )
		{
			String name = it.next( ).toString( );
			Node n = new Node( name );
			IBaseExpression expr = exprManager.getExpr( name );
			if ( expr != null )
			{
				if ( !( expr instanceof IScriptExpression || expr instanceof IConditionalExpression ) )
				{
					throw new DataException( ResourceConstants.BAD_DATA_EXPRESSION );
				}

				List l = null;
				try
				{
					if ( expr instanceof IScriptExpression )
						l = ExpressionCompilerUtil.extractColumnExpression( (IScriptExpression) expr );
					else if ( expr instanceof IConditionalExpression )
						l = ExpressionCompilerUtil.extractColumnExpression( (IConditionalExpression) expr );
				}
				catch ( DataException e )
				{
					// Do nothing.The mal-formatted expression should not prevent
					//other correct expression from being evaluated and displayed.
				}
				
				if ( l != null )
				{
					for ( int j = 0; j < l.size( ); j++ )
					{
						n.addChild( new Node( l.get( j ) == null ? null
								: l.get( j ).toString( ) ) );
					}
				}
			}
			result.add( n );
		}
		Node[] source = new Node[result.size( )];
		for ( int i = 0; i < source.length; i++ )
		{
			source[i] = (Node) result.get( i );
		}

		validateNodes( source );
	}
	
	/**
	 * Check whether the expression of all the column bindings is valid.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private void checkColumnBindingExpression( IBaseQueryDefinition baseQueryDefn ) throws DataException
	{
		List list = this.getColumnNames( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			String name = list.get( i ).toString( );
			IBaseExpression expr = exprManager.getExpr( name );
			if ( expr != null )
			{
				if ( !( expr instanceof IScriptExpression || expr instanceof IConditionalExpression ) )
				{
					throw new DataException( ResourceConstants.BAD_DATA_EXPRESSION );
				}

				List l = null;
				try
				{
					if ( expr instanceof IScriptExpression )
						l = ExpressionCompilerUtil.extractColumnExpression( (IScriptExpression) expr );
					else if ( expr instanceof IConditionalExpression )
						l = ExpressionCompilerUtil.extractColumnExpression( (IConditionalExpression) expr );
				}
				catch ( DataException e )
				{
					// Do nothing.The mal-formatted expression should not
					// prevent
					// other correct expression from being evaluated and
					// displayed.
				}

				if ( l != null )
				{
					for ( int j = 0; j < l.size( ); j++ )
					{
						checkColumnBindingExist( l.get( j ).toString( ), list );
					}
				}
				List usedBindings = null;

				if ( expr instanceof IScriptExpression )
				{
					try
					{
						usedBindings = ExpressionUtil.extractColumnExpressions( ( (IScriptExpression) expr ).getText( ),
								true );
					}
					catch ( BirtException e )
					{
						return;
					}
					validateReferredColumnBinding( name,
							usedBindings,
							baseQueryDefn );
				}
			}
		}
	}
	
	/**
	 * Test whether all the column bindings exist.
	 * @param bindingName
	 * @param binding
	 * @throws DataException
	 */
	private void checkColumnBindingExist( String bindingName, List binding )
			throws DataException
	{
		if ( "__rownum".equals( bindingName ) || "_outer".equals( bindingName ) )
		{
			return;
		}
		for ( int i = 0; i < binding.size( ); i++ )
		{
			if ( bindingName.equals( binding.get( i ).toString( ) ) )
				return;
		}
		throw new DataException( ResourceConstants.COLUMN_BINDING_NOT_EXIST,
				bindingName );
	}
	
	/**
	 * 
	 * @param map
	 * @param usedBindings
	 * @throws DataException
	 */
	private void validateReferredColumnBinding( String bindingName,
			List usedBindings, IBaseQueryDefinition baseQueryDefn )
			throws DataException
	{
		List nameList = this.getColumnNames( );
		for ( int i = 0; i < usedBindings.size( ); i++ )
		{
			IColumnBinding cb = (IColumnBinding) usedBindings.get( i );
			if ( useDefinedKeyWord( cb ) )
				continue;

			String name = ( (IColumnBinding) usedBindings.get( i ) ).getResultSetColumnName( );
			if ( !nameList.contains( name ) && baseQueryDefn != null )
			{
				String expr = findExpression( name,
						baseQueryDefn.getParentQuery( ) );
				if ( expr == null )
				{
					throw new DataException( ResourceConstants.COLUMN_BINDING_REFER_TO_INEXIST_COLUMN );
				}
				else if ( ExpressionUtil.hasAggregation( expr ) )
				{
					throw new DataException( ResourceConstants.COLUMN_BINDING_REFER_TO_AGGREGATION_COLUMN_BINDING_IN_PARENT_QUERY,
							bindingName );
				}
			}
		}
	}
	
	/**
	 * 
	 * @param columnBindingName
	 * @param queryDefn
	 * @return
	 */
	private String findExpression ( String columnBindingName, IBaseQueryDefinition queryDefn )
	{
		if ( queryDefn == null )
		{
			return null;
		}
		
		if ( queryDefn.getResultSetExpressions( ).get( columnBindingName ) == null )
		{
			return findExpression ( columnBindingName, queryDefn.getParentQuery( ));
		}
		
		IBaseExpression expr = (IBaseExpression)queryDefn.getResultSetExpressions( ).get( columnBindingName );
		if ( expr instanceof IScriptExpression )
			return ((IScriptExpression)expr).getText( );
		else 
			return null;
	}

	/**
	 * 
	 * @param cb
	 * @return
	 */
	private boolean useDefinedKeyWord( IColumnBinding cb )
	{
		return cb.getOuterLevel( ) > 0
				|| cb.getResultSetColumnName( )
						.equals( "__rownum" )
				|| cb.getResultSetColumnName( )
						.equals( "_rowPosition" );
	}

	/**
	 * 
	 * @param source
	 * @return
	 * @throws DataException 
	 */
	private void validateNodes( Node[] source ) throws DataException
	{
		Node[] preparedNodes = populateNodeList( source );
		for ( int i = 0; i < preparedNodes.length; i++ )
		{
			isValidNode( preparedNodes[i], preparedNodes[i] );
		}
	}

	/**
	 * 
	 * @param startNode
	 * @param candidateNode
	 * @return
	 * @throws DataException 
	 */
	private void isValidNode( Node startNode, Node candidateNode ) throws DataException
	{
		//boolean result = true;
		Object[] nodes = startNode.getChildren( ).toArray( );
		for ( int i = 0; i < nodes.length; i++ )
		{
			if ( candidateNode.equals( nodes[i] )
					|| startNode.equals( nodes[i] ) )
			{
				throw new DataException( ResourceConstants.COLUMN_BINDING_CYCLE,((Node)nodes[i]).getValue( ));
			}
			else
			{
				isValidNode( (Node) nodes[i], candidateNode );
			}
		}
	}

	/**
	 * 
	 * @param source
	 * @return
	 */
	private Node[] populateNodeList( Node[] source )
	{
		Node[] result = new Node[source.length];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = new Node( source[i].getValue( ) );
		}

		for ( int i = 0; i < result.length; i++ )
		{
			List l = source[i].getChildren( );
			for ( int j = 0; j < l.size( ); j++ )
			{
				Node n = getMatchedNode( (Node) l.get( j ), result );
				//If matched node found.
				if( n!= null )
					result[i].addChild( n );
			}
		}
		return result;
	}

	/**
	 * 
	 * @param node
	 * @param nodes
	 * @return
	 */
	private Node getMatchedNode( Node node, Node[] nodes )
	{
		for ( int i = 0; i < nodes.length; i++ )
		{
			if ( nodes[i].equals( node ) )
				return nodes[i];
		}

		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private List getColumnNames( )
	{
		List bindingExprs = exprManager.getBindingExprs( );
		Map autoBindingExprMap = exprManager.getAutoBindingExprMap( );

		List l = new ArrayList( );
		l.addAll( autoBindingExprMap.keySet( ) );
		for ( int i = 0; i < bindingExprs.size( ); i++ )
		{
			l.addAll( ( (GroupBindingColumn) bindingExprs.get( i ) ).getColumnNames( ) );
		}
		return l;
	}
	
	/**
	 * 
	 * @return
	 */
	private HashMap getGroupKeys( )
	{
		List bindingExprs = exprManager.getBindingExprs( );

		HashMap l = new HashMap( );
		for ( int i = 0; i < bindingExprs.size( ); i++ )
		{
			String key = ( (GroupBindingColumn) bindingExprs.get( i ) ).getGroupKey( );
			Integer groupLevel = new Integer( ( (GroupBindingColumn) bindingExprs.get( i ) ).getGroupLevel( ) );
			if ( key != null )
				l.put( groupLevel, key );
		}
		return l;
	}
	
	/**
	 *
	 */
	public static class Node
	{
		private List children;
		private String value;

		/**
		 * @param value
		 */
		public Node( String value )
		{
			this.value = value;
			this.children = new ArrayList( );
		}

		/**
		 * @return
		 */
		String getValue( )
		{
			return this.value;
		}

		/**
		 * @param n
		 */
		void addChild( Node n )
		{
			this.children.add( n );
		}

		/** 
		 * @return
		 */
		List getChildren( )
		{
			return this.children;
		}

		/*
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		public boolean equals( Object o )
		{
			if ( ( o instanceof Node ) && ( (Node) o ).value.equals( this.value ) )
				return true;
			return false;
		}		
	}
	
}