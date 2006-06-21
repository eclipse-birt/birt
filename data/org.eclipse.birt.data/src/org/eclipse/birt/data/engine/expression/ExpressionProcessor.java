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
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateCalculator;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateCalculator.JSAggrValueObject;
import org.eclipse.birt.data.engine.odi.IResultClass;
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
	private List tempRefactorCmpList;
	
	private IComputedColumnsState computedColumnState;

	// The expression's type
	private int exprType = COMPUTED_COLUMN_EXPR;
	// Expression parser helper
	private ExpressionParseHelper helper;
	private IResultIterator resultIterator;
	private IResultClass metaData;
	// Base query
	private BaseQuery query;
	// The expression's current group level
	private int currentGroupLevel;
	private DataSetRuntime dataSet;
	private boolean isDataSetMode = true;
	private ResultSetPopulator rsPopulator;
	
	/**
	 * @param resultSetMetaData
	 * @param resultIterator
	 * @param scope
	 */
	public ExpressionProcessor(IResultClass metaData,
			IResultIterator resultIterator, DataSetRuntime dataSet,
			BaseQuery query ) {
		helper = new ExpressionParseHelper(metaData, dataSet.getScriptScope());
		helper.setDataSetMode( this.isDataSetMode );
		helper.setQuery(query);
		this.metaData = metaData;
		this.query = query;
		this.resultIterator = resultIterator;
		this.dataSet = dataSet;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setResultSetPopulator(org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator)
	 */
	public void setResultSetPopulator( ResultSetPopulator rsPopulator )
	{
		this.rsPopulator = rsPopulator;
		helper.setResultSetPopulator( rsPopulator );
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#setResultIterator(org.eclipse.birt.data.engine.odi.IResultIterator)
	 */
	public void setResultIterator( IResultIterator it )
	{
		this.resultIterator = it;
		// TODO: this code needs further review
		dataSet.setResultSet( it, false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#setResultSetMetaData(org.eclipse.birt.data.engine.odi.IResultClass)
	 */
	public void setResultSetMetaData( IResultClass rsMetaData )
	{
		helper.setMetaData( rsMetaData );
		this.metaData = rsMetaData;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#setQuery(org.eclipse.birt.data.engine.executor.BaseQuery)
	 */
	public void setQuery( BaseQuery query )
	{
		this.query = query;
		helper.setQuery( query );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#getScope()
	 */
	public Scriptable getScope( )
	{
		return this.dataSet.getScriptScope();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#compile(org.eclipse.birt.data.engine.executor.IComputedColumnState)
	 */
	public void compileComputedColumn( IComputedColumnsState computedColumns )
	{
		assert ( computedColumns != null );	
		this.computedColumnState = computedColumns;
		
		IScriptExpression cmptdColumn = null;
		helper.useResultSetMetaData( true );
		Context context = Context.enter( );
		try
		{
			for ( int i = 0; i < computedColumnState.getCount( ); i++ )
			{

				// if the computed column is not available, parse it to get the
				// pass
				// level. If pass level<=1, this expression
				// can be evaluate. This expression will be added into a temp
				// list
				// for the later expression.
				if ( !computedColumnState.isValueAvailable( i ) )
				{
					String name = computedColumnState.getName( i );
					if ( name.matches( "\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E" ) )
					{
						exprType = GROUP_COLUMN_EXPR;
						// group level is 1-based
						currentGroupLevel = getCurrentGroupLevel( name,
								currentGroupLevel );
						helper.setExpressionType( exprType, currentGroupLevel );
						if ( tempRefactorCmpList == null )
							tempRefactorCmpList = new ArrayList( );
						tempRefactorCmpList.add( new Integer( i ) );
					}
					cmptdColumn = (IScriptExpression) computedColumnState.getExpression( i );
					compileScriptExpression( cmptdColumn, context );
					// if the expression is group column , compile it then
					// return, do not compile the next expression
					if ( exprType == GROUP_COLUMN_EXPR )
					{
						exprType = 0;
						helper.setExpressionType( 0, 0 );
						helper.resetPassLevel( );
						return;
					}
				}

				// if this computed column can be caculated, set value
				// available.
				if ( helper.getExpressionPassLevel( ) <= 1 )
				{
					if ( tempRefactorCmpList == null )
						tempRefactorCmpList = new ArrayList( );
					tempRefactorCmpList.add( new Integer( i ) );
				}
				else
				{
					helper.resetPassLevel( );
					helper.setExpressionType( exprType, 0 );
					return;
				}

				helper.setExpressionType( exprType, 0 );
				helper.resetPassLevel( );
			}
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#compileFilter(java.util.List,
	 *      org.eclipse.birt.data.engine.executor.IComputedColumnsState)
	 */
	public void compileFilter( List filters,
			IComputedColumnsState computedColumns ) throws DataException
	{
		IBaseExpression baseExpression = null;
		String expression = "";
		IScriptExpression operator = null;
		FilterExpressionParser parser = new FilterExpressionParser( metaData,
				computedColumns );
		parser.setDataSetMode( this.isDataSetMode );
		parser.setResultSetPopulator( this.rsPopulator );

		for ( int i = 0; i < filters.size( ); i++ )
		{
			try
			{
				baseExpression = ( (FilterDefinition) filters.get( i ) ).getExpression( );

				if ( baseExpression instanceof IConditionalExpression )
				{
					IConditionalExpression condition = (IConditionalExpression) baseExpression;
					
					operator = condition.getExpression( );
					if ( operator != null )
					{
						expression = operator.getText( );
						operator.setHandle( parser.compileFilterExpression( expression ) );
					}
					operator = condition.getOperand1( );
					if ( operator != null )
					{
						expression = operator.getText( );
						operator.setHandle( parser.compileFilterExpression( expression ) );
					}
					operator = condition.getOperand2( );

					if ( operator != null )
					{
						expression = operator.getText( );
						operator.setHandle( parser.compileFilterExpression( expression ) );
					}
				}

				else if ( baseExpression instanceof IScriptExpression )
				{
					operator = (IScriptExpression) baseExpression;
					if ( baseExpression != null && operator != null )
					{
						expression = operator.getText( );
						baseExpression.setHandle( parser.compileFilterExpression( expression ) );
					}
				}
				else
				{
					// never get here
					throw new DataException( ResourceConstants.INVALID_EXPRESSION_IN_FILTER );
				}
			}
			catch ( DataException e )
			{
				DataException dataException = new DataException( ResourceConstants.INVALID_EXPRESSION_IN_FILTER,
						e,
						new Object[]{
							expression
						} );
				throw dataException;
			} 
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#compileExpression(org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState)
	 */
	public void compileExpression( IComputedColumnsState iccStates )
	{
		IBaseExpression baseExpression = null;
		this.computedColumnState = iccStates;
		Context context = Context.enter( );
		helper.useResultSetMetaData( false );
		try
		{
			for ( int i = 0; i < iccStates.getCount( ); i++ )
			{
				if ( !iccStates.isValueAvailable( i ) )
				{
					baseExpression = computedColumnState.getExpression( i );
					if ( baseExpression instanceof IScriptExpression )
					{
						compileScriptExpression( (IScriptExpression) baseExpression,
								context );
					}
					else if ( baseExpression instanceof IConditionalExpression )
					{
						compileConditionalExpression( (IConditionalExpression) baseExpression,
								context );
					}
				}
				// if this computed column can be caculated, set value
				// available.
				if ( helper.getExpressionPassLevel( ) <= 1 )
				{
					if ( tempRefactorCmpList == null )
						tempRefactorCmpList = new ArrayList( );
					tempRefactorCmpList.add( new Integer( i ) );
				}
				else
				{
					helper.resetPassLevel( );
					helper.setExpressionType( exprType, 0 );
					return;
				}

				helper.setExpressionType( exprType, 0 );
				helper.resetPassLevel( );
			}
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.IExpressionProcessor#hasAggregateExpr(java.util.List)
	 */
	public boolean hasAggregateExpr( List list ) throws DataException
	{
		boolean hasAggregate = false;
		helper.setAggregateStatus( hasAggregate );
		
		IBaseExpression baseExpression = null;
		for ( int i = 0; i < list.size( ); i++ )
		{
			baseExpression = (IBaseExpression)list.get( i );
			compileBaseExpression(baseExpression);
		}
		hasAggregate = helper.getAggregateStatus( );
		this.clear( );
		return hasAggregate;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#hasAggregation(org.eclipse.birt.data.engine.api.IBaseExpression)
	 */
	public boolean hasAggregation(IBaseExpression expression) throws DataException
	{
		boolean hasAggregate = false;
		IBaseExpression baseExpression = expression;
		
		compileBaseExpression(baseExpression);
		
		hasAggregate = helper.getAggregateStatus( );
		this.clear( );
		return hasAggregate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#calculate()
	 */
	public void calculate( ) throws DataException
	{
		List aggrList = helper.getAggregateList( 1 );
		AggregateTable table = helper.getAggregateTable( );
		if ( aggrList == null || table == null )
		{
			setExpressionState( );
			return;
		}		
		AggregateCalculator calculator = new AggregateCalculator( table,
				this.resultIterator );

		// if the expression has the nested aggregate object, the pre_value of
		// the temp_aggregate object should be populated in aggregate caculator
		if ( helper.hasNestedAggregate( ) )
			calculator.populateValue( (JSAggrValueObject) dataSet.getJSTempAggrValueObject( ) );
		Scriptable aggrObj = calculator.getJSAggrValueObject( );
		dataSet.setJSTempAggrValueObject( aggrObj );

		calculator.calculate( dataSet.getScriptScope( ) );
		for ( int i = 0; i < aggrList.size( ); i++ )
		{
			AggregateObject obj = (AggregateObject) aggrList.get( i );
			obj.setAvailable( true );
			helper.addAvailableAggregateObj( obj );
		}
		setExpressionState( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#calculate(java.lang.Object[], int[], int)
	 */
	public void calculate( Object[] exprArray, int[] groupArray, int arrayType )
			throws DataException
	{
		assert exprArray != null;
		assert groupArray != null;
		assert exprArray.length == groupArray.length;

		IBaseExpression baseExpression = null;
		int groupLevel, level = helper.getCurrentGroupLevel( ), exprType = helper.getCurrentExpressionType( );

		Context context = Context.enter( );
		try
		{
			for ( int i = 0; i < exprArray.length; i++ )
			{
				baseExpression = (IBaseExpression) exprArray[i];
				groupLevel = groupArray[i];
				helper.setExpressionType( arrayType, groupLevel );
				if ( baseExpression instanceof IConditionalExpression )
				{
					IConditionalExpression condition = (IConditionalExpression) baseExpression;
					IScriptExpression op = condition.getExpression( );
					IScriptExpression op1 = condition.getOperand1( );
					IScriptExpression op2 = condition.getOperand2( );
					if ( op != null )
						op.setHandle( helper.compileExpression( op, context ) );
					if ( op1 != null )
						op1.setHandle( helper.compileExpression( op1, context ) );
					if ( op2 != null )
						op2.setHandle( helper.compileExpression( op2, context ) );
				}
				else if ( baseExpression instanceof IScriptExpression )
				{
					IScriptExpression scriptExpr = (IScriptExpression) baseExpression;
					scriptExpr.setHandle( helper.compileExpression( scriptExpr,
							context ) );
				}
			}
		}
		finally
		{
			Context.exit( );
		}
		
		calculate( );
		helper.setExpressionType( exprType, level );
	}
		
	/**
	 * compile script expression
	 * 
	 * @param scriptExpr
	 * @param name
	 * @param cx
	 * @throws DataException
	 */
	private void compileScriptExpression( IScriptExpression scriptExpr, Context cx )
	{
		IScriptExpression cmptdColumn = scriptExpr;
		if ( cmptdColumn.getText( ) == null
				|| cmptdColumn.getText( ).trim( ).length( ) == 0 )
		{
			DataException dataException = new DataException( ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK );
			cmptdColumn.setHandle( new InvalidExpression( dataException ) );
			return;
		}
		try
		{
			CompiledExpression expr = helper.compileExpression( scriptExpr, cx );
			cmptdColumn.setHandle( expr );
		}
		catch ( Exception e )
		{
			DataException dataException = new DataException( ResourceConstants.INVALID_JS_EXPR,
					e,
					scriptExpr.getText( ) );
			cmptdColumn.setHandle( new InvalidExpression( dataException ) );
		}
	}
		
	/**
	 * compile conditional expression
	 * 
	 * @param baseExpression
	 * @param parser
	 * @throws DataException
	 */
	private void compileConditionalExpression(
			IConditionalExpression baseExpression, Context context )
	{
		IConditionalExpression condition = (IConditionalExpression) baseExpression;
		IScriptExpression operator = null;
		try
		{
			operator = condition.getExpression( );
			if ( operator != null )
			{
				operator.setHandle( helper.compileExpression( operator, context ) );
			}
			operator = condition.getOperand1( );
			if ( operator != null )
			{
				operator.setHandle( helper.compileExpression( operator, context ) );
			}
			operator = condition.getOperand2( );

			if ( operator != null )
			{
				operator.setHandle( helper.compileExpression( operator, context ) );
			}
		}
		catch ( Exception e )
		{
			DataException dataException = new DataException( ResourceConstants.INVALID_JS_EXPR,
					e,
					operator.getText( ) );
			operator.setHandle( new InvalidExpression( dataException ) );
		}
	}


	/**
	 * 
	 * @param baseExpression
	 * @throws DataException
	 */
	private void compileBaseExpression(IBaseExpression baseExpression ) throws DataException {
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
	 * Set the expression state. if the expression can be evaluated, its state
	 * should be set true
	 */
	private void setExpressionState( )
	{
		if ( this.tempRefactorCmpList != null )
		{
			for ( int i = 0; i < tempRefactorCmpList.size( ); i++ )
			{
				int index = ( (Integer) tempRefactorCmpList.get( i ) ).intValue( );
				helper.addAvailableCmpColumn( this.computedColumnState.getName( index ) );
				this.computedColumnState.setValueAvailable( index );
			}
		}
	}
	
	/**
	 * Return the index of group according to the given group text.
	 * @param groupText
	 * @return The index of group 
	 */
	private int getCurrentGroupLevel( String groupText, int start )
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
	 * Clear the resources used in parser
	 */
	public void clear( )
	{
		tempRefactorCmpList = null;
		exprType =  COMPUTED_COLUMN_EXPR;
		if ( this.helper != null )
			this.helper.clear( );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#prepareComputedColumns(org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState)
	 */
	public void prepareComputedColumns( IComputedColumnsState iccState )
	{
		this.computedColumnState = iccState;
		for ( int i = 0; i < iccState.getCount( ); i++ )
		{
			if ( iccState.isValueAvailable( i ) )
				helper.addAvailableCmpColumn( iccState.getName( i ) );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor#setDataSetMode(boolean)
	 */
	public void setDataSetMode( boolean isDataSetMode )
	{
		this.isDataSetMode = isDataSetMode;
		this.helper.setDataSetMode( isDataSetMode );
	}
}