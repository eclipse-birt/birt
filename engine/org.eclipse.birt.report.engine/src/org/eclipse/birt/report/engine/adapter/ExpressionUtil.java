/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.impl.DataRequestSessionImpl;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.ir.Expression;

/**
 * This class help to manipulate expressions.
 * 
 */
public final class ExpressionUtil
{
	private static Logger logger = Logger.getLogger( ExpressionUtil.class.getName());
	
	private static final String TOTAL_PREFIX = "TOTAL_COLUMN_";

	private int totalColumnSuffix = 0;
	
	private IModelAdapter adapter = null;
	
	public ITotalExprBindings prepareTotalExpressions( List<Expression> exprs, IDataQueryDefinition queryDefn ) throws EngineException
	{
		return prepareTotalExpressions( exprs, null, queryDefn );
	}

	/**
	 * 
	 * @param exprs
	 * @return
	 * @throws EngineException 
	 */
	public ITotalExprBindings prepareTotalExpressions( List<Expression> exprs,
			String groupName, IDataQueryDefinition queryDefn )
			throws EngineException
	{
		

		TotalExprBinding result = new TotalExprBinding( );
		List l = new ArrayList( );
		boolean isCube = false;

		if ( queryDefn instanceof IBaseCubeQueryDefinition )
		{
			isCube = true;
		}

		for ( int i = 0; i < exprs.size( ); i++ )
		{

			Expression expr = exprs.get( i );

			result.addColumnBindings( l );

			if ( expr == null )
			{
				result.addNewExpression( null );
			}
			else
			{
				switch ( expr.getType( ) )
				{
					case Expression.CONSTANT :
						result.addNewExpression( expr );
						break;
					case Expression.SCRIPT :
						String newExpr = prepareTotalExpression( expr
								.getScriptText( ), l, groupName, isCube );
						result.addColumnBindings( l );
						expr.setScriptText( newExpr );
						result.addNewExpression( expr );
						break;
					case Expression.CONDITIONAL :
						addConditionalExprBindings( result,
								(Expression.Conditional) expr, l, groupName,
								isCube );
						break;
					default :
						throw new IllegalStateException(
								"invalid expression type:" + expr.getType( ) );
				}
			}
		}
		return result;
	}
	
	/**
	 * When a TopN/TopPercent/BottomN/BottomPercent ConditionalExpression is
	 * set, transform it to
	 * Total.TopN/Total.TopPercent/Total.BottomN/Total.BottomPercent
	 * aggregations with "isTrue" operator.
	 * 
	 * @param ce
	 * @return
	 */
	public static IConditionalExpression transformConditionalExpression(
			IConditionalExpression ce )
	{
		boolean needTransform = false;
		
		switch ( ce.getOperator( ) )
		{
			case IConditionalExpression.OP_TOP_N :
			case IConditionalExpression.OP_BOTTOM_N :
			case IConditionalExpression.OP_TOP_PERCENT :
			case IConditionalExpression.OP_BOTTOM_PERCENT :	 
			 		 needTransform = true;
			 		 break;
			default:
				needTransform = false;
				break;
		}
		
		if( needTransform )
		{
			ce = new ToBeTransformTopBottomConditionalExpression( ce.getExpression( ),
					ce.getOperator( ),
					ce.getOperand1( ),
					ce.getOperand2( ) );
		}
		return ce;
	}

	/**
	 * @param result
	 * @param key
	 * @throws EngineException 
	 */
	private void addConditionalExprBindings( TotalExprBinding result,
			Expression.Conditional key, List bindings, String groupName, boolean isCube ) throws EngineException
	{
		try
		{
			IConditionalExpression ce = key.getConditionalExpression( );

			if ( !hasAggregationInFilter( ce ) && !(ce instanceof ToBeTransformTopBottomConditionalExpression) )
			{
				result.addNewExpression( key );
				return;
			}
			if ( groupName != null )
				ce.setGroupName( groupName );

			String bindingName = TOTAL_PREFIX + totalColumnSuffix;
			totalColumnSuffix++;

			if( ce instanceof ToBeTransformTopBottomConditionalExpression)
			{
				List allColumnBindings = new ArrayList( );

				allColumnBindings.add( ((ToBeTransformTopBottomConditionalExpression)ce).transform( bindingName, groupName ) );
				result.addColumnBindings( allColumnBindings );
				
				result.addNewExpression(Expression.newScript( org.eclipse.birt.core.data.ExpressionUtil
						.createJSRowExpression( bindingName ) ));
				return;
			}
			Binding columnBinding = new Binding( bindingName, ce );

			if ( groupName != null )
			{
				columnBinding.addAggregateOn( groupName );
			}

			List allColumnBindings = new ArrayList( );

			allColumnBindings.add( columnBinding );

			result.addColumnBindings( allColumnBindings );

			if ( !isCube )
			{
				String script = org.eclipse.birt.core.data.ExpressionUtil
						.createJSRowExpression( bindingName );
				result.addNewExpression( Expression.newScript( script ) );
			}
			else
			{
				String script = org.eclipse.birt.core.data.ExpressionUtil
						.createJSDataExpression( bindingName );
				result.addNewExpression( Expression.newScript( script ) );
			}
		}
		catch ( DataException e )
		{
			throw new EngineException( e );
		}
	}
	
	/**
	 * 
	 * @param expr
	 * @return
	 */
	private boolean hasAggregationInFilter( IBaseExpression expr )
	{
		if ( expr == null )
			return false;
		if ( expr instanceof IScriptExpression )
		{
			return org.eclipse.birt.core.data.ExpressionUtil.hasAggregation( ( (ScriptExpression) expr ).getText( ) );
		}
		else if ( expr instanceof IConditionalExpression )
		{
			IConditionalExpression ce = (IConditionalExpression) expr;
			if ( hasAggregationInFilter( ce.getExpression( ) ) )
			{
				return true;
			}
			if ( hasAggregationInFilter( ce.getOperand1( ) ) )
			{
				return true;
			}
			if ( hasAggregationInFilter( ce.getOperand2( ) ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Translate the old expression with "row" as indicator to new expression
	 * using "dataSetRow" as indicator.
	 * 
	 * @param oldExpression
	 * @return
	 * @throws DataException
	 */
	private String prepareTotalExpression( String oldExpression,
			List columnBindings, String groupName, boolean isCube ) throws EngineException
	{
		try
		{
			if ( oldExpression == null )
				return null;

			char[] chars = oldExpression.toCharArray( );

			// 5 is the minium length of expression that can cantain a row
			// expression
			if ( chars.length < 8 )
				return oldExpression;
			else
			{
				ParseIndicator indicator = new ParseIndicator( 0,
						0,
						false,
						false,
						true,
						true );
				for ( int i = 0; i < chars.length; i++ )
				{
					indicator = getParseIndicator( chars,
							i,
							indicator.omitNextQuote( ),
							indicator.getCandidateKey1( ),
							indicator.getCandidateKey2( ) );

					i = indicator.getNewIndex( );

					if ( i >= indicator.getRetrieveSize( ) + 6 )
					{
						if ( indicator.isCandidateKey( )
								&& chars[i - indicator.getRetrieveSize( ) - 6] == 'T'
								&& chars[i - indicator.getRetrieveSize( ) - 5] == 'o'
								&& chars[i - indicator.getRetrieveSize( ) - 4] == 't'
								&& chars[i - indicator.getRetrieveSize( ) - 3] == 'a'
								&& chars[i - indicator.getRetrieveSize( ) - 2] == 'l'
								&& chars[i - indicator.getRetrieveSize( ) - 1] == '.' )
						{
							if ( i - indicator.getRetrieveSize( ) - 7 <= 0
									|| isValidProceeding( chars[i
											- indicator.getRetrieveSize( ) - 7] ) )
							{
								String firstPart = oldExpression.substring( 0,
										i - indicator.getRetrieveSize( ) - 6 );

								int startIndex = i
										- indicator.getRetrieveSize( ) - 6;
								i = advanceToNextValidEncloser( chars, i );
								String secondPart = "";
								String name = "";
								String expr = "";

								if ( i < chars.length )
								{
									int endIndex = i + 1;

									expr = oldExpression.substring( startIndex,
											endIndex );

									secondPart = prepareTotalExpression( oldExpression.substring( i
											+ 1 - indicator.getRetrieveSize( ) ),
											columnBindings,
											groupName, isCube );
								}
								else
								{
									expr = oldExpression.substring( startIndex );
								}

								boolean shouldAddToList = true;
								for ( int j = 0; j < columnBindings.size( ); j++ )
								{
									IBaseExpression expression = ( (IBinding) columnBindings.get( j ) ).getExpression( );
									if ( expression instanceof IScriptExpression )
									{
										if ( oldExpression.equals( ( (IScriptExpression) expression ).getText( ) ) )
										{
											shouldAddToList = false;
											name = ( (IBinding) columnBindings.get( j ) ).getBindingName( );
											break;
										}
									}
								}
								if ( shouldAddToList )
								{
									name = TOTAL_PREFIX + totalColumnSuffix;
									totalColumnSuffix++;
									ScriptExpression se = new ScriptExpression( expr );
									se.setGroupName( groupName );
									
									Binding columnBinding = new Binding( name, se );
									
									columnBindings.add( columnBinding );
								}
								String newExpression = null;
								if ( !isCube )
								{
									newExpression = firstPart
											+ org.eclipse.birt.core.data.ExpressionUtil.createJSRowExpression( name )
											+ secondPart;
								}
								else
								{
									newExpression = firstPart
											+ org.eclipse.birt.core.data.ExpressionUtil.createJSDataExpression( name )
											+ secondPart;
								}

								return newExpression;
							}
						}
					}

				}

			}

			return oldExpression;
		}
		catch ( DataException e )
		{
			throw new EngineException( e );
		}
	}

	/**
	 * 
	 * @param chars
	 * @param i
	 * @return
	 */
	private static int advanceToNextValidEncloser( char[] chars, int i )
	{
		boolean isTotalConstants = true;
		int numberOfOpenBracket = 0;
		while ( i < chars.length )
		{
			ParseIndicator pid = getParseIndicator( chars, i, false, true,true);
			i = pid.getNewIndex( );
			if( pid.isCandidateKey( ))
			if ( chars[i] == '(' )
			{
				isTotalConstants = false;
				numberOfOpenBracket ++;
			}

			if ( isTotalConstants )
			{
				if ( !isValidProceeding( chars[i] ) )
					i++;
				else
					break;
			}
			else
			{
				if ( chars[i] != ')' )
					i++;
				else
				{
					if( chars[i] == ')')
					{
						numberOfOpenBracket--;
					}
					if( numberOfOpenBracket == 0)
					{
						break;
					}else
					{
						i++;
					}
				}
			}
		}

		if ( isTotalConstants )
			i--;
		return i;
	}

	/**
	 * This method is used to provide information necessary for next step parsing.
	 * 
	 * @param chars
	 * @param i
	 * @param omitNextQuote
	 * @param candidateKey1
	 * @param candidateKey2
	 * @return
	 */
	private static ParseIndicator getParseIndicator( char[] chars, int i,
			boolean omitNextQuote, boolean candidateKey1, boolean candidateKey2 )
	{
		int retrieveSize = 0;

		if ( chars[i] == '/' )
		{
			if ( i > 0 && chars[i - 1] == '/' )
			{
				retrieveSize++;
				while ( i < chars.length - 2 )
				{
					i++;
					retrieveSize++;
					if ( chars[i] == '\n' )
					{
						break;
					}
				}
				retrieveSize++;
				i++;
			}
		}
		else if ( chars[i] == '*' )
		{
			if ( i > 0 && chars[i - 1] == '/' )
			{
				i++;
				retrieveSize = retrieveSize + 2;
				while ( i < chars.length - 2 )
				{
					i++;
					retrieveSize++;
					if ( chars[i - 1] == '*' && chars[i] == '/' )
					{
						break;
					}
				}
				retrieveSize++;
				i++;
			}
		}

		if ( ( !omitNextQuote ) && chars[i] == '"' )
		{
			candidateKey1 = !candidateKey1;
			if ( candidateKey1 )
				candidateKey2 = true;
		}
		if ( ( !omitNextQuote ) && chars[i] == '\'' )
		{
			candidateKey2 = !candidateKey2;
			if ( candidateKey2 )
				candidateKey1 = true;
		}
		if ( chars[i] == '\\' )
			omitNextQuote = true;
		else
			omitNextQuote = false;

		return new ParseIndicator( retrieveSize,
				i,
				candidateKey1,
				omitNextQuote,
				candidateKey1,
				candidateKey2 );
	}

	/**
	 * Test whether the char immediately before the candidate "row" key is
	 * valid.
	 * 
	 * @param operator
	 * @return
	 */
	private static boolean isValidProceeding( char operator )
	{
		if ( ( operator >= 'A' && operator <= 'Z' )
				|| ( operator >= 'a' && operator <= 'z' ) || operator == '_' )
			return false;

		return true;
	}
	
	private IScriptExpression adapterExpression(Expression expr)
	{
		if ( expr instanceof Expression.Script && 
				! Expression.SCRIPT_JAVASCRIPT.equals( 
						( (Expression.Script) expr ).getLanguage( ) ) )
		{
			// Adapts non-JS scripts using ModelAdapter service
			ScriptExpression scriptExpr = null;
			try
			{
				scriptExpr = getModelAdapter( ).adaptJSExpression(
						expr.getScriptText( ),
						( (Expression.Script) expr ).getLanguage( ) );
				return new ScriptExpression( scriptExpr.getText( ) );
			}
			catch ( Exception ex )
			{
				logger.log(Level.WARNING, "Error adapting expr " + expr.getScriptText(), ex);
			}
		}
		else
		{

			if ( expr.getType( ) == Expression.CONSTANT )
			{
				ScriptExpression jsExpr = new ScriptExpression(
						expr.getScriptText( ) );
				jsExpr.setScriptId( BaseExpression.constantId );
				jsExpr.setHandle( expr.getScriptText( ) );
				return jsExpr;
			}

		}
		return new ScriptExpression( expr.getScriptText( ) );
	}
	
	public IConditionalExpression createConditionalExpression(
			Expression testExpression, String operator, Expression value1,
			Expression value2 )
	{
		IScriptExpression expr = null, tempV1 = null, tempV2 = null;
		if ( testExpression != null )
		{
			expr = adapterExpression( testExpression );
		}
		if ( value1 != null )
		{
			tempV1 = adapterExpression( value1 );
		}
		if ( value2 != null )
		{
			tempV2 = adapterExpression( value2 );
		}
		ConditionalExpression expression = new ConditionalExpression( expr,
				DataAdapterUtil.adaptModelFilterOperator( operator ), tempV1,
				tempV2 );
		return ExpressionUtil.transformConditionalExpression( expression );
	}

	public IConditionalExpression createConditionExpression(
			Expression testExpression, String operator,
			List<Expression> valueList )
	{
		ArrayList<IScriptExpression> values = new ArrayList<IScriptExpression>( valueList.size( ) );
		for ( Expression expr : valueList )
		{
			values.add( adapterExpression( expr ) );
		}
		IScriptExpression expr = null;
		if ( testExpression != null )
		{
			expr = adapterExpression( testExpression );
		}
		ConditionalExpression expression = new ConditionalExpression( expr,
				DataAdapterUtil.adaptModelFilterOperator( operator ), values );
		return ExpressionUtil.transformConditionalExpression( expression );
	}

	private IModelAdapter getModelAdapter( ) throws BirtException
	{
		if ( adapter == null )
		{
			DataRequestSession session = new DataRequestSessionImpl( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			adapter = session.getModelAdaptor( );
		}
		return adapter;
	}
}

/**
 * 
 */
class TotalExprBinding implements ITotalExprBindings
{

	/**
	 * 
	 */
	private List newExprs;
	private List columnBindings;

	TotalExprBinding()
	{
		this.newExprs = new ArrayList();
		this.columnBindings = new ArrayList();
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getNewExpression()
	 */
	public List getNewExpression( )
	{
		return this.newExprs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.data.ITotalExprBindings#getColumnBindings()
	 */
	public IBinding[] getColumnBindings( )
	{
		IBinding[] result = new IBinding[columnBindings.size()];
		for ( int i = 0; i < result.length; i++ )
		{
			result[i] = (IBinding) columnBindings.get( i );
		}
		return result;
	}
	
	public void addNewExpression( Object expr )
	{
		this.newExprs.add( expr );
	}

	public void addColumnBindings( List columnBindingList )
	{
		for( int i = 0; i < columnBindingList.size( ); i++ )
		{
			if( !this.columnBindings.contains( columnBindingList.get( i ) ))
			{
				this.columnBindings.add( columnBindingList.get( i ) );
			}
		}
	}
}

/**
 * A utility class for internal use only.
 * 
 */
class ParseIndicator
{

	private int retrieveSize;
	private int newIndex;
	private boolean isCandidateKey;
	private boolean omitNextQuote;
	private boolean candidateKey1;
	private boolean candidateKey2;

	ParseIndicator( int retrieveSize, int newIndex, boolean isCandidateKey,
			boolean omitNextQuote, boolean candidateKey1, boolean candidateKey2 )
	{
		this.retrieveSize = retrieveSize;
		this.newIndex = newIndex;
		this.isCandidateKey = isCandidateKey;
		this.omitNextQuote = omitNextQuote;
		this.candidateKey1 = candidateKey1;
		this.candidateKey2 = candidateKey2;
	}

	public int getRetrieveSize( )
	{
		return this.retrieveSize;
	}

	public int getNewIndex( )
	{
		return this.newIndex;
	}

	public boolean isCandidateKey( )
	{
		return this.isCandidateKey;
	}

	public boolean omitNextQuote( )
	{
		return this.omitNextQuote;
	}

	public boolean getCandidateKey1( )
	{
		return this.candidateKey1;
	}

	public boolean getCandidateKey2( )
	{
		return this.candidateKey2;
	}
}

