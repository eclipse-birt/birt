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

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
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
	private List availableAggrList;
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
	 * helper compiler object to fetch the pass level
	 */
	private MultiPassExpressionCompiler currentHelper;
	
	/**
	 * 
	 * @return the instance of ExpressionProcessor
	 */
	public ExpressionProcessor( DataSetRuntime dataSet )
	{
		this.dataset = dataSet;
		availableAggrList = new ArrayList( );
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
		int exprType = COMPUTED_COLUMN_EXPR;
		int currentGroupLevel = 0;

		MultiPassExpressionCompiler helper = this.getMultiPassCompilerHelper( );
		helper.setDataSetMode( isDataSetMode );

		for ( int i = 0; i < iccState.getCount( ); i++ )
		{
			if ( iccState.isValueAvailable( i ) )
			{
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

				setHandle( this.rsPopulator.getSession( )
						.getEngineContext( ).getScriptContext( ),
						exprType,
						currentGroupLevel,
						helper,
						baseExpression,
						useResultSetMeta );

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
				// reset the pass level flag
				helper.reSetPassLevelFlag( );
			}
		}

		calculate( helper );
		
	}

	/**
	 * 
	 * @param context
	 * @param exprType
	 * @param currentGroupLevel
	 * @param helper
	 * @param baseExpression
	 * @param useResultSetMeta
	 * @throws DataException
	 */
	private void setHandle(  ScriptContext context,
			int exprType, int currentGroupLevel,
			MultiPassExpressionCompiler helper, IBaseExpression baseExpression, boolean useResultSetMeta )
			throws DataException
	{
		if( baseExpression == null )
			return;
		ExpressionInfo exprInfo;
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
		else if ( baseExpression instanceof IExpressionCollection )
		{
			Object[] exprs = ( (IExpressionCollection) baseExpression ).getExpressions( ).toArray( );
			for ( int i = 0; i < exprs.length; i++ )
			{
				this.setHandle( context,
						exprType,
						currentGroupLevel,
						helper,
						(IBaseExpression)exprs[i],
						useResultSetMeta );
			}
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
		MultiPassExpressionCompiler helper = this.getMultiPassCompilerHelper( );
		helper.setDataSetMode( isDataSetMode );

		for ( int i = 0; i < exprArray.length; i++ )
		{
			baseExpression = (IBaseExpression) exprArray[i];
			this.setHandle( this.rsPopulator.getSession( )
					.getEngineContext( ).getScriptContext( ),
					arrayType,
					currentGroupLevel[i],
					helper,
					baseExpression,
					true );
		}

		calculate( helper );

	}

	/**
	 * 
	 * @param list
	 * @return
	 * @throws DataException
	 */
	public boolean hasAggregateExpr( List list ) throws DataException
	{
		boolean hasAggregate = false;

		MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
				baseQuery,
				dataset.getScriptScope( ),
				availableAggrList,
				dataset.getSession( ).getEngineContext( ).getScriptContext( ));
		helper.setDataSetMode( isDataSetMode );

		IBaseExpression baseExpression = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			baseExpression = (IBaseExpression) list.get( i );
			compileBaseExpression( baseExpression,
					helper,
					this.rsPopulator.getSession( )
							.getEngineContext( ).getScriptContext( ));
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
	public boolean hasAggregation( IBaseExpression expression )
	{
		boolean hasAggregate = false;

		try
		{
			MultiPassExpressionCompiler helper = new MultiPassExpressionCompiler( rsPopulator,
					baseQuery,
					null,
					null,
					dataset.getSession( ).getEngineContext( ).getScriptContext( ));

			helper.setDataSetMode( isDataSetMode );
			IBaseExpression baseExpression = expression;
			compileBaseExpression( baseExpression,
					helper,
					this.dataset.getSession( ).getEngineContext( ).getScriptContext( ));

			hasAggregate = helper.getAggregateStatus( );
			clear( );
		}
		catch ( DataException ex )
		{
			clear( );
			return false;
		}
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

					dataset.getSession( ).getEngineContext( ).getScriptContext( ),
					(JSAggrValueObject) dataset.getJSTempAggrValueObject( ));
		}
		else
		{
			table.calculate( resultIterator, dataset.getScriptScope( ), 
					dataset.getSession( ).getEngineContext( ).getScriptContext( ) );
		}
		Scriptable aggrObj = table.getJSAggrValueObject( );
		dataset.setJSTempAggrValueObject( aggrObj );;
		for ( int i = 0; i < aggrList.size( ); i++ )
		{
			AggregateObject obj = (AggregateObject) aggrList.get( i );
			obj.setAvailable( true );
			if ( availableAggrList == null )
				availableAggrList = new ArrayList( );
			if ( !availableAggrList.contains( obj ) )
				availableAggrList.add( obj );
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
			int exprType, int currentGroupLevel, ScriptContext context )
			throws DataException
	{
		IConditionalExpression condition = (IConditionalExpression) baseExpression;
		IScriptExpression op = condition.getExpression( );
		IBaseExpression op1 = condition.getOperand1( );
		IBaseExpression op2 = condition.getOperand2( );
		this.setHandle( context, exprType, currentGroupLevel, helper, op, true );
		this.setHandle( context, exprType, currentGroupLevel, helper, op1, true );
		this.setHandle( context, exprType, currentGroupLevel, helper, op2, true );
	}

	/**
	 * 
	 * @param baseExpression
	 * @throws DataException
	 */
	private void compileBaseExpression( IBaseExpression baseExpression,
			MultiPassExpressionCompiler helper, ScriptContext context )
			throws DataException
	{
		if ( baseExpression instanceof IConditionalExpression )
		{
			IConditionalExpression condition = (IConditionalExpression) baseExpression;
			IScriptExpression op = condition.getExpression( );
			IBaseExpression op1 = condition.getOperand1( );
			IBaseExpression op2 = condition.getOperand2( );
			if ( op != null )
				helper.compileExpression( op, context );
			if ( op1 != null )
				compileBaseExpression( op1, helper, context );
			if ( op2 != null )
				compileBaseExpression( op2, helper, context );
		}
		else if ( baseExpression instanceof IScriptExpression )
		{
			IScriptExpression scriptExpr = (IScriptExpression) baseExpression;
			helper.compileExpression( scriptExpr, context );
		}
		else if ( baseExpression instanceof IExpressionCollection )
		{
			IExpressionCollection combinedExpr = (IExpressionCollection) baseExpression;
			Object[] exprs = combinedExpr.getExpressions( ).toArray( );
			for ( int i = 0; i < exprs.length; i++ )
			{
				compileBaseExpression( (IBaseExpression)exprs[i],
						helper,
						context );
			}
		}
	}
	
	/**
	 * Return the index of group according to the given group text.
	 * 
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
	
	/**
	 * 
	 * @return
	 * @throws DataException 
	 */
	private MultiPassExpressionCompiler getMultiPassCompilerHelper( ) throws DataException
	{
		if ( currentHelper == null )
		{
			currentHelper = new MultiPassExpressionCompiler( rsPopulator,
					baseQuery,
					dataset.getScriptScope( ),
					availableAggrList,
					dataset.getSession( ).getEngineContext( ).getScriptContext( ));
		}
		else
		{
			currentHelper.setCompilerStatus( availableAggrList );
		}
		return currentHelper;

	}
	
	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#getScope()
	 */
	public Scriptable getScope( ) throws DataException
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
		if ( availableAggrList != null )
			availableAggrList.clear( );
		availableAggrList = null;
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