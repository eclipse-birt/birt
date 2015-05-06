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

package org.eclipse.birt.data.engine.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

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
class MultiPassExpressionCompiler extends AbstractExpressionCompiler
{
	// the pass level of the expression
	private int totalPassLevel = 0;
	// the pass level of aggregate
	private int passLevel = 0;
	// the aggregate object list for compile
	private ArrayList aggrObjList = null;
	// contain the exprssions that can be calculated
	private List availableCmpList;
	// aggregate object that can be calculated
	private List caculatedAggregateList;

	private int currentGroupLevel, exprType;
	// aggregate table
	private AggregateTable table;
	private boolean hasAggregate = false;
	// whether the expression has nested aggregate
	private boolean hasNesetedAggregate = false;
	private Scriptable scope;
	private ResultSetPopulator rsPopulator;

    private final String ROW_INDICATOR = "row";
    private final String TOTAL_OVERALL = "Total.OVERALL";
    
    
    // this list to save the current group level if there is nested column
	// expression. it's last element always keep the newest group level.
	private List currentGroupLevelList = new ArrayList( );

	private final static String AGGR_VALUE = "_temp_aggr_value";
	private boolean useRsMetaData = true;
	private BaseQuery baseQuery;
	private ScriptContext cx;
	//Cache the visited Available Computed Column list in each aggregation parsing
	private List visitedList;
	
	/**
	 * ExpressionParseHelper to help user parse common expression.
	 * 
	 * @param metaData
	 */
	public MultiPassExpressionCompiler( ResultSetPopulator rsPopulator,
			BaseQuery query, Scriptable scope, List availableAggrObj, ScriptContext cx )
	{
		this.rsPopulator = rsPopulator;
		this.baseQuery = query;
		this.scope = scope;
		this.hasAggregate = false;
		this.hasNesetedAggregate = false;
		this.caculatedAggregateList = availableAggrObj;
		this.aggrObjList = new ArrayList( );
		this.cx = cx;
	}
	
	/**
	 * reset the helper status
	 * @param availableAggrObj
	 */
	void setCompilerStatus( List availableAggrObj )
	{
		this.hasAggregate = false;
		this.hasNesetedAggregate = false;
		this.caculatedAggregateList = availableAggrObj;
		this.aggrObjList = new ArrayList( );
		this.passLevel = 0;
		this.totalPassLevel = 0;
	}
	
	/**
	 * 
	 * reset pass level flag
	 */
	void reSetPassLevelFlag( )
	{
		this.passLevel = 0;
		this.totalPassLevel = 0;
	}
	
	/**
	 * 
	 * @param exprInfo
	 * @param cx
	 * @return
	 */
	public CompiledExpression compileExpression( ExpressionInfo exprInfo,
			ScriptContext cx )
	{
		try
		{
			currentGroupLevel = exprInfo.getCurrentGroupLevel( );
			exprType = exprInfo.getExprType( );
			useRsMetaData = exprInfo.useCustomerChecked( );
			
			CompiledExpression expr = compileExpression( exprInfo.getScriptExpression( ),
					cx );
			
			return expr;
		}
		catch ( Exception e )
		{
			DataException dataException = new DataException( ResourceConstants.INVALID_JS_EXPR,
					e,
					exprInfo.getScriptExpression( ).getText( ) );
			return new InvalidExpression( dataException );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.AbstractExpressionParser#compileDirectColRefExpr(org.mozilla.javascript.Node,
	 *      boolean)
	 */
	protected CompiledExpression compileDirectColRefExpr( Node parent, Node refNode,
			Node grandfather, boolean customerChecked, Context context )
			throws DataException
	{

		if ( ( refNode.getType( ) == Token.GETPROP || refNode.getType( ) == Token.GETELEM )
				&& ( !this.getDataSetMode( ) ) )
		{
			String columnBindingName;
			if ( refNode.getFirstChild( ).getType( ) == Token.NAME
					&& refNode.getLastChild( ).getType( ) == Token.STRING
					&& refNode.getFirstChild( )
							.getString( )
							.equals( this.ROW_INDICATOR ) )
			{
				{
					columnBindingName = refNode.getLastChild( ).getString( );
					if ( columnBindingName != null
							&& !columnBindingName.equals( ScriptConstants.OUTER_RESULT_KEYWORD )
							&& !columnBindingName.equals( ScriptConstants.ROW_NUM_KEYWORD )
							&& !columnBindingName.equals( "0" ) )
					{
						IBinding binding = this.rsPopulator.getEventHandler( )
								.getBinding( columnBindingName );
						if ( binding == null )
							throw new DataException( ResourceConstants.BAD_DATA_EXPRESSION );

						if ( binding.getAggrFunction( ) == null )
						{
							IScriptExpression expression = (IScriptExpression) binding.getExpression( );
							currentGroupLevelList.add( expression.getGroupName( ) );
							AstRoot tree = parse( expression.getText( ),
									context );
							if ( tree.getFirstChild( ) != null
									&& tree.getFirstChild( ).getFirstChild( ) != null
									&& tree.getFirstChild( )
											.getFirstChild( )
											.getType( ) != Token.IFNE
									&& tree.getFirstChild( )
											.getFirstChild( )
											.getType( ) != Token.IFEQ )
							{

								CompiledExpression expr = null;
								if ( grandfather != null )
								{
									if ( tree.getFirstChild( ) == tree.getLastChild( ) )
									{
										grandfather.replaceChild( parent,
												tree.getFirstChild( ) );
										expr = processChild( context,
												false,
												tree.getFirstChild( ),
												tree.getFirstChild( )
														.getFirstChild( ),
												grandfather );
									}
									else
									{
										grandfather.replaceChild( grandfather.getFirstChild( ),
												tree.getFirstChild( ) );
										grandfather.replaceChild( grandfather.getLastChild( ),
												tree.getLastChild( ) );
										expr = this.compileComplexExpr( context,
												tree,
												false );
									}
								}
								else
								{
									if ( tree.getFirstChild( ) == tree.getLastChild( ) )
									{
										parent.replaceChild( refNode,
												tree.getFirstChild( )
														.getFirstChild( ) );
										expr = processChild( context,
												false,
												parent,
												tree.getFirstChild( )
														.getFirstChild( ),
												grandfather );

									}
									else
									{
										expr = this.compileComplexExpr( context,
												tree,
												false );
									}
								}
								currentGroupLevelList.remove( currentGroupLevelList.size( ) - 1 );
								if ( expr != null )
								{
									if ( ( expr instanceof ColumnReferenceExpression ) )
									{
										( (ColumnReferenceExpression) expr ).setDataType( expression.getDataType( ) );
										return expr;
									}
									return expr;
								}
							}
						}
					}
				}
			}
		}

		ColumnReferenceExpression expr = super.compileColRefExpr( refNode,
				customerChecked );

		if ( customerChecked && expr != null )
		{
			if ( expr.getColumnName( ) != null
					&& expr.getColumnName( ).trim( ).length( ) > 0 )
				checkAvailableCmpColumn( expr.getColumnName( ) );
		}
		return expr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.impl.AbstractExpressionParser#compileAggregateExpr(org.mozilla.javascript.Context,
	 *      org.mozilla.javascript.Node, org.mozilla.javascript.Node)
	 */
	protected AggregateExpression compileAggregateExpr( Context context, Node parent,
			Node callNode ) throws DataException
	{
		assert ( callNode.getType( ) == Token.CALL );
		IAggrFunction aggregation = getAggregationFunction( callNode );
		// not an aggregation function being called, then it's considered
		// a complex expression
		if ( aggregation == null )
		{
			return null;
		}
		this.visitedList = new ArrayList();

		this.passLevel = 0;
		AggregateExpression aggregateExpression = new AggregateExpression( aggregation );

		AggregateObject aggregateObj = new AggregateObject( aggregateExpression );
		this.hasAggregate = true;
		
		extractArguments( context, aggregateExpression, callNode );
		
		Iterator iter = aggregateExpression.getArguments( ).iterator( );
		while ( iter.hasNext( ) )
		{
			CompiledExpression argumentExpr = (CompiledExpression) iter.next( );
			// the argument contains the nested aggregate expression
			if ( argumentExpr instanceof AggregateExpression )
			{
				if ( canBeCalculated( new AggregateObject( (AggregateExpression) argumentExpr ) ) )
					passLevel--;
				hasNesetedAggregate = true;
				// throw new DataException(
				// ResourceConstants.UNSUPPORTED_DIRECT_NESTED_AGGREGATE );
			}
			else if ( argumentExpr instanceof ComplexExpression )
			{
				flatternAggregateExpression( (ComplexExpression) argumentExpr );
			}
		}
		// get the aggregate current group level
		currentGroupLevel = getCurrentGroupLevel( aggregateObj, context );
		
		aggregateExpression.setGroupLevel( currentGroupLevel );
				
		if ( exprType == IExpressionProcessor.GROUP_COLUMN_EXPR
				|| exprType == IExpressionProcessor.FILTER_ON_GROUP_EXPR
				|| exprType == IExpressionProcessor.SORT_ON_GROUP_EXPR )
		{
			aggregateObj.setPassLevel( 1 );
		}
		else
			aggregateObj.setPassLevel( ++passLevel );
		
		//All the group level in nested total should follow the rule that the child aggregate group level should 
		//greater than parent group level. 
		if ( aggregateExpression.isNestedAggregation( )
				&& aggregateExpression.getCalculationLevel( ) < aggregateExpression.getGroupLevel( )
				&& aggregateExpression.getCalculationLevel( ) != 0 )
		{
			throw new DataException( ResourceConstants.INVALID_TOTAL_EXPRESSION );
		}
		
		int id = registerAggregate( aggregateObj,
				aggregateExpression.isNestedAggregation( )
						? aggregateExpression.getCalculationLevel( ) : 0 );

		if ( id >= 0 )
			replaceAggregateNode( id, parent, callNode );
		setTotalPassLevel( passLevel );
		this.visitedList.clear( );
		return aggregateExpression;
	}

	/**
	 * Check the aggregate object on group,if the aggregate object's type is
	 * GROUP_COLUMN_EXPR, its group level must be less than current group level.
	 * if type is FILTER_ON_GROUP_EXPR, its group level must be equals to
	 * current group level. if type is SORT_ON_GROUP_EXPR, its group level must
	 * be less or equal with current group level.
	 * 
	 * @param aggregateObj
	 */
	private int getCurrentGroupLevel( AggregateObject aggregateObj,
			Context context ) throws DataException
	{
		AggregateExpression expr = aggregateObj.getAggregateExpr( );
		IAggrFunction aggr = expr.getAggregation( );
		List argList = expr.getArguments( );
		int nFixedArgs = aggr.getParameterDefn( ).length;
		int groupLevel = currentGroupLevel;
		// Verify that the expression has the right # of arguments
		int nArgs = argList.size( );
		CompiledExpression groupExpr = null;
		if ( nArgs > nFixedArgs + 2 || nArgs < nFixedArgs )
		{
			if ( nFixedArgs > 0 && !aggr.getParameterDefn( )[0].isOptional( ) )
			{
				DataException e = new DataException( ResourceConstants.INVALID_AGGR_PARAMETER,
						expr.getAggregation( ).getName( ) );
				throw e;
			}
		}
		if ( nArgs == nFixedArgs + 2 )
		{
			groupExpr = (CompiledExpression) argList.get( nArgs - 1 );
		}
		if ( groupExpr != null && !( groupExpr instanceof ConstantExpression ) )
		{

			DataException e = new DataException( ResourceConstants.INVALID_AGGR_GROUP_EXPRESSION,
					aggr.getName( ) );
			throw e;
		}
		// evaluate group level
		Object groupLevelObj;

		if ( groupExpr != null )
			groupLevelObj = groupExpr.evaluate( cx, scope );
		else
		{
			String currentGroupName = null;
			if ( this.currentGroupLevelList.size( ) == 0 )
				currentGroupName = this.getScriptExpression( ).getGroupName( );
			else
				currentGroupName = this.currentGroupLevelList.get( this.currentGroupLevelList.size( ) - 1 )
						.toString( );

			if ( currentGroupName.equals( TOTAL_OVERALL ) )
				groupLevelObj = 0;
			else
				groupLevelObj = currentGroupName;
		}
		

		if ( groupLevelObj == null )
		{
			//do nothing
		}
		else if ( groupLevelObj instanceof String )
		{
			int level = AggregationConstantsUtil.getGroupLevel( (String) groupLevelObj,
					currentGroupLevel,
					this.baseQuery.getGrouping( ) == null ? 0
							: this.baseQuery.getGrouping( ).length,
					false );
			// When the groupLevelObj can be recognized, it will return a
			// non-negative value.Else return -1.
			if ( level != -1 )
			{
				groupLevel = level;
			}
			else
			{
				groupLevel = getGroupIndex( (String) groupLevelObj );
			}
		}
		else if ( groupLevelObj instanceof Number )
		{
			int offset = ( (Number) groupLevelObj ).intValue( );
			if ( offset < 0 )
				groupLevel = currentGroupLevel + offset;
			else
				groupLevel = offset;
		}
		switch ( exprType )
		{
			case IExpressionProcessor.FILTER_ON_GROUP_EXPR :
				if ( groupLevel != currentGroupLevel )
				{
					DataException e = new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
							aggr.getName( ) );
					throw e;
				}
				break;
			case IExpressionProcessor.SORT_ON_GROUP_EXPR :
				if ( groupLevel < 0 || groupLevel > currentGroupLevel )
				{
					DataException e = new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
							aggr.getName( ) );
					throw e;
				}
				break;
			default :
				// TODO gourpLevel>= currentGorupLevel is also invalid
				if ( groupLevel < 0 )
				{
					DataException e = new DataException( ResourceConstants.INVALID_GROUP_LEVEL,
							aggr.getName( ) );
					throw e;
				}
				break;
		}

		return groupLevel;
	}

	/**
	 * Return the index of group according to the given group text.
	 * 
	 * @param groupText
	 * @return The index of group
	 */
	private int getGroupIndex( String groupText )
	{
		assert groupText != null;
		assert baseQuery != null;
		GroupSpec[] groups = baseQuery.getGrouping( );
		for ( int i = 0; i < groups.length; i++ )
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
	 * if the column field is custom field and it is not available, the total
	 * pass level++
	 * 
	 * @param string
	 * @throws DataException
	 */
	private void checkAvailableCmpColumn( String rowColumnName )
			throws DataException
	{
		if ( !useRsMetaData || this.rsPopulator == null )
			return;
		else if ( ( this.rsPopulator.getResultSetMetadata( ) == null || this.rsPopulator.getResultSetMetadata( )
				.isCustomField( rowColumnName ) )
				&& ( this.availableCmpList == null || !this.availableCmpList.contains( rowColumnName ) ) )
		{
			this.passLevel++;
		}
	}

	/**
	 * get the expression total pass level
	 * 
	 * @return
	 */
	int getExpressionPassLevel( )
	{
		return this.totalPassLevel;
	}

	/**
	 * parse the aggregate expression's arguments
	 * 
	 * @param context
	 * @param aggregateExpression
	 * @param callNode
	 * @throws DataException
	 */
	private void extractArguments( Context context,
			AggregateExpression aggregateExpression, Node callNode )
			throws DataException
	{
		Node arg = callNode.getFirstChild( ).getNext( );
						
		while ( arg != null )
		{
			// need to hold on to the next argument because the tree extraction
			// will cause us to lose the reference otherwise
			Node nextArg = arg.getNext( );
			CompiledExpression expr = processChild( context,
					true,
					callNode,
					arg,
					null );
			if ( !( expr instanceof BytecodeExpression ) )
			{
				aggregateExpression.addArgument( expr );
				arg = nextArg;
				continue;
			}

			AstRoot tree = new AstRoot( Token.SCRIPT );
			Node exprNode = new Node( Token.EXPR_RESULT );
			exprNode.addChildToFront( arg );
			tree.addChildrenToFront( exprNode );
			if ( expr instanceof AggregateExpression )
			{
				int registry = getRegisterId( new AggregateObject( (AggregateExpression) expr ) );
				if ( registry >= 0 )
					replaceAggregateNode( registry, exprNode, arg );
			}
			
			compileForBytecodeExpr( context, tree, expr );
			aggregateExpression.addArgument( expr );
			arg = nextArg;
		}
	}
	
		
	/**
	 * get the register id form aggregate object list
	 * 
	 * @param obj1
	 * @return
	 */
	private int getRegisterId( AggregateObject obj1 )
	{
		if ( this.aggrObjList == null )
			return -1;
		else
			for ( int i = 0; i < this.aggrObjList.size( ); i++ )
			{
				AggregateObject obj2 = (AggregateObject) this.aggrObjList.get( i );
				if ( obj1.equals( obj2 ) )
					return obj2.getRegisterId( );
			}
		return -1;
	}

	/**
	 * if the aggregate object is available, return true.
	 * 
	 * @param aggregateObj
	 * @return
	 */
	private boolean canBeCalculated( AggregateObject aggregateObj )
	{
		if ( this.caculatedAggregateList == null )
			return false;
		else
		{
			for ( int i = 0; i < this.caculatedAggregateList.size( ); i++ )
			{
				AggregateObject obj = (AggregateObject) caculatedAggregateList.get( i );
				if ( obj.equals( aggregateObj ) )
				{
					if ( visitedList.contains( aggregateObj ) )
						return false;
					else
					{
						visitedList.add( obj );
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * get the aggregate object list with pass level equals 'level'.
	 * 
	 * @param level
	 * @return
	 */
	List getAggregateList( int level )
	{
		if ( this.aggrObjList == null )
			return null;
		else
		{
			List levelList = new ArrayList( );
			for ( int i = 0; i < this.aggrObjList.size( ); i++ )
			{
				AggregateObject aggrObj = (AggregateObject) aggrObjList.get( i );
				if ( aggrObj.getPassLevel( ) <= level && !aggrObj.isAvailable( ) )
					levelList.add( aggrObj );
			}
			return levelList;
		}
	}

	/**
	 * if the agrument contains aggregate expression , return true. TODO
	 * Unsupported direct nested aggregate
	 * 
	 * @param expression
	 * @return
	 */
	private void flatternAggregateExpression( ComplexExpression complexExpr )
			throws DataException
	{
		Collection subExprs = complexExpr.getSubExpressions( );
		Iterator iter = subExprs.iterator( );
		while ( iter.hasNext( ) )
		{
			CompiledExpression childExpr = (CompiledExpression) iter.next( );
			if ( childExpr instanceof AggregateExpression )
			{
				if ( canBeCalculated( new AggregateObject( (AggregateExpression) childExpr ) ) )
				{
					passLevel--;
					return;
				}
				hasNesetedAggregate = true;
//				throw new DataException( ResourceConstants.UNSUPPORTED_DIRECT_NESTED_AGGREGATE );
			}
			else if ( childExpr instanceof ComplexExpression )
			{
				Collection childSubExprs = ( (ComplexExpression) childExpr ).getSubExpressions( );
				Iterator childIter = childSubExprs.iterator( );
				while ( childIter.hasNext( ) )
				{
					CompiledExpression childChildExpr = (CompiledExpression) childIter.next( );
					if ( childChildExpr instanceof AggregateExpression )
					{
						if ( canBeCalculated( new AggregateObject( (AggregateExpression) childChildExpr ) ) )
						{
							passLevel--;
							return;
						}
					}
				}
			}
		}
	}

	/**
	 * register the aggregate object to aggrObjList and get the only id.
	 * 
	 * @param aggregateObj
	 * @return register id
	 */
	private int registerAggregate( AggregateObject aggregateObj, int calculationLevel )
			throws DataException
	{
		if ( rsPopulator == null )
			return -1;
		int index = -1;
		if ( table == null )
			table = AggregationTablePopulator.createAggregateTable( rsPopulator.getSession( ).getTempDir( ), baseQuery );
		try
		{
			if ( aggregateObj.getPassLevel( ) <= 1 )
			{
				index = AggregationTablePopulator.populateAggregationTable( table,
						aggregateObj,
						currentGroupLevel,
						calculationLevel,
						false,
						false,
						cx);
				if ( aggrObjList == null )
				{
					aggrObjList = new ArrayList( );
				}
				aggregateObj.setRegisterId( index );
				aggrObjList.add( aggregateObj );
			}
		}
		catch ( DataException e )
		{
			throw e;
		}
		return index;
	}

	/**
	 * get aggregate table
	 * 
	 * @return
	 */
	AggregateTable getAggregateTable( )
	{
		return this.table;
	}

	/**
	 * 
	 * @return
	 */
	boolean hasNestedAggregate( )
	{
		return this.hasNesetedAggregate;
	}

	/**
	 * set total pass level. the total pass level must be the max of passLevel
	 * 
	 * @param passLevel
	 */
	private void setTotalPassLevel( int passLevel )
	{
		if ( this.totalPassLevel < passLevel )
			this.totalPassLevel = passLevel;
	}

	/**
	 * if the computed column has been calculated, it will be added into
	 * available list.
	 * 
	 * @param name
	 */
	void addAvailableCmpColumn( String name )
	{
		if ( this.availableCmpList == null )
			availableCmpList = new ArrayList( );
		availableCmpList.add( name );
	}

	/**
	 * get aggregate status, if has aggregate, return true. or return false
	 * 
	 * @return
	 */
	public boolean getAggregateStatus( )
	{
		return this.hasAggregate;
	}
	

	/**
	 * replace the aggregate node with AGGR_VALUE <id>
	 * 
	 * @param registry
	 * @param aggregateExpression
	 * @param parent
	 * @param aggregateCallNode
	 * @throws DataException
	 */
	private void replaceAggregateNode( int registry, Node parent,
			Node aggregateCallNode ) throws DataException
	{
		if ( registry < 0 )
			throw new DataException( ResourceConstants.INVALID_CALL_AGGR );

		int aggregateId = registry;
		Node newFirstChild = Node.newString( Token.NAME, AGGR_VALUE );
		Node newSecondChild = Node.newNumber( aggregateId );
		Node aggregateNode = new Node( Token.GETELEM,
				newFirstChild,
				newSecondChild );
		parent.replaceChild( aggregateCallNode, aggregateNode );
	}
	
}