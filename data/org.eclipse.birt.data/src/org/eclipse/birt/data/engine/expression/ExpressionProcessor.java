/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.data.engine.expression;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.impl.aggregation.JSAggrValueObject;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * This class handles the compilation of ROM JavaScript expressions of the
 * multiple pass level Each expression is compiled to generate a handle, which
 * is an instance of CompiledExpression or its derived class. The expression
 * handle is used by the factory to evaluate the expression after the report
 * query is executed. <br>
 * ExpressionProcessor compiles the expression into Rhino byte code for faster
 * evaluation at runtime.
 *  
 */
public class ExpressionProcessor implements IExpressionProcessor
{
	/**
	 * the available aggregate list in expression processor
	 */
	private List availabeAggrList;
	/**
	 * data set runtime
	 */
	private DataSetRuntime dataset;
	/**
	 * result set populator
	 */
	private ResultSetPopulator rsPopulator;
	/**
	 * is dataset mode or result set mode
	 */
	private boolean isDataSetMode = true;
	/**
	 * resultset iterator
	 */
	private IResultIterator resultIterator;
	/**
	 * base query
	 */
	private BaseQuery baseQuery;
	
	/**
	 * 
	 * @return the instance of ExpressionProcessor
	 */
	public ExpressionProcessor( DataSetRuntime dataSet )
	{
		this.dataset = dataSet;
		availabeAggrList = new ArrayList( );
	}

	/**
	 * 
	 * @param computedColumns
	 * @param rsPopulator
	 * @param dataSetRuntime
	 * @throws DataException
	 */
	public void evaluateMultiPassExprOnCmp( IComputedColumnsState iccState,
			boolean useResultSetMeta ) throws DataException
	{
		assert ( iccState != null );
		Context context = Context.enter( );
		try
		{
			int exprType = COMPUTED_COLUMN_EXPR;
			int currentGroupLevel = 0;
			ExpressionInfo exprInfo;

			MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
					baseQuery,
					dataset.getScriptScope( ),
					availabeAggrList );
			helper.setDataSetMode( isDataSetMode );

			for ( int i = 0; i < iccState.getCount( ); i++ )
			{
				if ( iccState.isValueAvailable( i ) )
				{
					compileBaseExpression( iccState.getExpression( i ),
							helper );
					helper.addAvailableCmpColumn( iccState.getName( i ) );
				}
			}

			for ( int i = 0; i < iccState.getCount( ); i++ )
			{

				if ( !iccState.isValueAvailable( i ) )
				{
					IBaseExpression baseExpression = iccState.getExpression( i );

					String name = iccState.getName( i );
					if ( useResultSetMeta
							&& name.matches( "\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E" ) )
					{
						exprType = GROUP_COLUMN_EXPR;
						// group level is 1-based
						currentGroupLevel = getCurrentGroupLevel( name,
								currentGroupLevel,
								rsPopulator.getQuery( ) );

						iccState.setValueAvailable( i );
					}

					if ( baseExpression instanceof IScriptExpression )
					{
						exprInfo = new ExpressionInfo( (IScriptExpression) baseExpression,
								exprType,
								currentGroupLevel,
								useResultSetMeta );

						baseExpression.setHandle( helper.compileExpression( exprInfo,
								context ) );
					}
					else if ( baseExpression instanceof IConditionalExpression )
					{
						compileConditionalExpression( (IConditionalExpression) baseExpression,
								helper,
								rsPopulator,
								exprType,
								currentGroupLevel,
								context );
					}

					// if the expression is group column , compile it then
					// return, do not compile the next expression
					if ( exprType == GROUP_COLUMN_EXPR )
					{
						return;
					}

					// if this computed column can be caculated, set value
					// available.
					if ( helper.getExpressionPassLevel( ) <= 1 )
					{
						iccState.setValueAvailable( i );
					}
					else
					{
						break;
					}
				}
			}

			calculate( helper );
		}
		finally
		{
			Context.exit( );
		}
	}
	
	/**
	 * 
	 * @param exprArray
	 * @param arrayType
	 * @param rsPopulator
	 * @param dataSetRuntime
	 * @param useResultSetMeta
	 * @param isDataSetMode
	 * @throws DataException
	 */
	public void evaluateMultiPassExprOnGroup( Object[] exprArray,
			int[] currentGroupLevel, int arrayType ) throws DataException
	{
		assert exprArray != null;
		IBaseExpression baseExpression = null;
		Context context = Context.enter( );

		MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
				baseQuery,
				dataset.getScriptScope( ),
				availabeAggrList );
		helper.setDataSetMode( isDataSetMode );
		
		try
		{
			ExpressionInfo exprInfo;
			for ( int i = 0; i < exprArray.length; i++ )
			{
				baseExpression = (IBaseExpression) exprArray[i];
				if ( baseExpression instanceof IConditionalExpression )
				{
					compileConditionalExpression( (IConditionalExpression) baseExpression,
							helper,
							rsPopulator,
							arrayType,
							currentGroupLevel[i],
							context );
				}
				else if ( baseExpression instanceof IScriptExpression )
				{
					exprInfo = new ExpressionInfo( (IScriptExpression) baseExpression,
							arrayType,
							currentGroupLevel[i],
							true );
					baseExpression.setHandle( helper.compileExpression( exprInfo,
							context ) );
				}
			}
		}
		finally
		{
			Context.exit( );
		}

		calculate( helper );
	}

	/**
	 * 
	 * @param list
	 * @return
	 * @throws DataException
	 */
	public boolean hasAggregateExpr( List list )
			throws DataException
	{
		boolean hasAggregate = false;

		MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
				baseQuery,
				dataset.getScriptScope( ),
				availabeAggrList );
		helper.setDataSetMode( isDataSetMode );

		IBaseExpression baseExpression = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			baseExpression = (IBaseExpression) list.get( i );
			compileBaseExpression( baseExpression, helper );
		}
		hasAggregate = helper.getAggregateStatus( );
		clear( );
		return hasAggregate;
	}

	/**
	 * 
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public boolean hasAggregation( IBaseExpression expression ) throws DataException
	{
		boolean hasAggregate = false;

		MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
				baseQuery,
				null,
				null );

		helper.setDataSetMode( isDataSetMode );
		IBaseExpression baseExpression = expression;
		compileBaseExpression( baseExpression, helper );
		hasAggregate = helper.getAggregateStatus( );
		clear( );
		return hasAggregate;
	}

	/**
	 * calculate the aggregate in available iccstates
	 * 
	 * @param computedColumns
	 * @param helper
	 * @throws DataException
	 */
	private void calculate( MultiPassExpressionCompiler helper )
			throws DataException
	{
		List aggrList = helper.getAggregateList( 1 );
		AggregateTable table = helper.getAggregateTable( );
		if ( aggrList == null || table == null )
		{
			return;
		}

		// if the expression has the nested aggregate object, the pre_value of
		// the temp_aggregate object should be populated in aggregate caculator
		if ( helper.hasNestedAggregate( ) )
		{
			table.calculate( resultIterator,
					dataset.getScriptScope( ),
					(JSAggrValueObject) dataset.getJSTempAggrValueObject( ) );
		}
		else
		{
			table.calculate( resultIterator, dataset.getScriptScope( ) );
		}
		Scriptable aggrObj = table.getJSAggrValueObject( );
		dataset.setJSTempAggrValueObject( aggrObj );;
		for ( int i = 0; i < aggrList.size( ); i++ )
		{
			AggregateObject obj = (AggregateObject) aggrList.get( i );
			obj.setAvailable( true );
			if ( availabeAggrList == null )
				availabeAggrList = new ArrayList( );
			availabeAggrList.add( obj );
		}
	}
		
	/**
	 * compile conditional expression
	 * 
	 * @param baseExpression
	 * @param parser
	 * @throws DataException 
	 * @throws DataException
	 */
	private void compileConditionalExpression(
			IConditionalExpression baseExpression,
			MultiPassExpressionCompiler helper, ResultSetPopulator rsPopulator,
			int exprType, int currentGroupLevel, Context context ) throws DataException
	{
		ExpressionInfo exprInfo = null;
		if ( baseExpression instanceof IConditionalExpression )
		{
			IConditionalExpression condition = (IConditionalExpression) baseExpression;
			IScriptExpression op = condition.getExpression( );
			IScriptExpression op1 = condition.getOperand1( );
			IScriptExpression op2 = condition.getOperand2( );
			if ( op != null )
			{
				exprInfo = new ExpressionInfo( op,
						exprType,
						currentGroupLevel,
						true );
				op.setHandle( helper.compileExpression( exprInfo,
						context ) );
			}
			if ( op1 != null )
			{
				exprInfo = new ExpressionInfo( op1,
						exprType,
						currentGroupLevel,
						true );
				op1.setHandle( helper.compileExpression( op1, context ) );
			}
			if ( op2 != null )
			{
				exprInfo = new ExpressionInfo( op2,
						exprType,
						currentGroupLevel,
						true );
				op2.setHandle( helper.compileExpression( op2, context ) );
			}
		}
	}

	/**
	 * 
	 * @param baseExpression
	 * @throws DataException
	 */
	private void compileBaseExpression( IBaseExpression baseExpression,
			MultiPassExpressionCompiler helper ) throws DataException
	{
		Context context = Context.enter( );
		try
		{
			if ( baseExpression instanceof IConditionalExpression )
			{
				IConditionalExpression condition = (IConditionalExpression) baseExpression;
				IScriptExpression op = condition.getExpression( );
				IScriptExpression op1 = condition.getOperand1( );
				IScriptExpression op2 = condition.getOperand2( );
				if ( op != null )
					helper.compileExpression( op, context );
				if ( op1 != null )
					helper.compileExpression( op1, context );
				if ( op2 != null )
					helper.compileExpression( op2, context );
			}
			else if ( baseExpression instanceof IScriptExpression )
			{
				IScriptExpression scriptExpr = (IScriptExpression) baseExpression;
				helper.compileExpression( scriptExpr, context );
			}
		}
		finally
		{
			Context.exit( );
		}
	}
	
	/**
	 * Return the index of group according to the given group text.
	 * @param groupText
	 * @return The index of group 
	 */
	private int getCurrentGroupLevel( String groupText, int start, BaseQuery query )
	{
		assert groupText != null;
		assert query != null;

		GroupSpec[] groups = query.getGrouping( );
		for ( int i = start; i < groups.length; i++ )
		{
			GroupSpec group = groups[i];
			if ( groupText.equals( group.getName( ) )
					|| groupText.equals( group.getKeyColumn( ) ) )
			{
				return i + 1; // Note that group index is 1-based
			}
		}
		return -1;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#getScope()
	 */
	public Scriptable getScope( )
	{
		return this.dataset.getScriptScope( );
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setResultSetPopulator(org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator)
	 */
	public void setResultSetPopulator( ResultSetPopulator rsPopulator )
	{
		this.rsPopulator = rsPopulator;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setDataSetMode(boolean)
	 */
	public void setDataSetMode( boolean isDataSetMode )
	{
		this.isDataSetMode = isDataSetMode;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setQuery(org.eclipse.birt.data.engine.executor.BaseQuery)
	 */
	public void setQuery( BaseQuery query )
	{
		this.baseQuery = query;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator)
	 */
	public void setResultIterator( IResultIterator resultIterator )
	{
		this.resultIterator = resultIterator;
		// TODO: this code needs further review
		dataset.setResultSet( this.resultIterator, false );
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#clear()
	 */
	public void clear( )
	{
		if ( availabeAggrList != null )
			availabeAggrList.clear( );
		availabeAggrList = null;
	}
}

/**
 * 
 * 
 */
class ExpressionInfo
{

	IScriptExpression scriptExpr;
	int exprType;
	int currentGroupLevel;
	boolean customerChecked = false;

	/**
	 * 
	 * @param scriptExpression
	 * @param exprType
	 * @param currentGroupLevel
	 * @param useCustomerChecked
	 */
	ExpressionInfo( IScriptExpression scriptExpression, int exprType,
			int currentGroupLevel, boolean useCustomerChecked )
	{
		this.scriptExpr = scriptExpression;
		this.exprType = exprType;
		this.currentGroupLevel = currentGroupLevel;
		this.customerChecked = useCustomerChecked;
	}

	/**
	 * 
	 * @return
	 */
	IScriptExpression getScriptExpression( )
	{
		return this.scriptExpr;
	}

	/**
	 * 
	 * @return
	 */
	int getExprType( )
	{
		return this.exprType;
	}

	/**
	 * 
	 * @return
	 */
	int getCurrentGroupLevel( )
	{
		return this.currentGroupLevel;
	}

	/**
	 * 
	 * @return
	 */
	boolean useCustomerChecked( )
	{
		return this.customerChecked;
	}
}