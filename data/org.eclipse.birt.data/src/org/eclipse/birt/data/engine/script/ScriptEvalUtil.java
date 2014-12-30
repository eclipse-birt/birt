/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.script;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompareHints;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.LogUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;




/**
 * Static utility methods to help evaluating Javascript expressions 
 */
public class ScriptEvalUtil
{	
	private static Logger logger = Logger.getLogger( ScriptEvalUtil.class.getName( ) );
	
	/**
	 * No instance
	 */
	private ScriptEvalUtil( )
	{
	}
	
	/**
	 * @param exprText
	 * @param value
	 * @return an instance of ExprTextAndValue
	 */
	public static ExprTextAndValue newExprInfo( Object value )
	{
		return ExprTextAndValue.newInstance( value );
	}
	
	/**
	 * Evaluates a conditional expression. A conditional expression comprises of
	 * a Javascript expression, an operator, and up to 2 operands (which are
	 * Javascript expressions themselves).<br>
	 * Both op1 and op2 will be encapsulated to ExprTextAndValue type to show
	 * specific message in case anything goes wrong, they are assumed not to be
	 * null as well.
	 * <p>
	 * The basic rule for comparison: obj will always be considered as the
	 * default data type,i.e. obj, op1 and op2 will be formatted to the superset
	 * of obj (or Double if obj is numeric)on the condition they are comparable.<br>
	 * e.g.<br>
	 * obj: Integer=>obj, op1 and op2 will be formatted to Double.<br>
	 * obj: Timestamp=>obj, op1 and op2 will be formatted to Date.<br>
	 * obj: Boolean=>obj and op1 will be formatted to Boolean.<br>
	 * obj: String=>obj, op1 and op2 will remain the same
	 * 
	 * @param obj
	 * @param operator
	 * @param Op1
	 * @param Op2
	 * @return
	 * @throws DataException
	 */
	public static Object evalConditionalExpr( Object obj, int operator,
			Object Op1, Object Op2 ) throws DataException
	{
		return evalConditionalExpr( obj, operator, Op1, Op2, null );
	}
	
	/**
	 * 
	 * @param obj
	 * @param operator
	 * @param Op1
	 * @param Op2
	 * @param compareHints the hints for comparison
	 * @return
	 * @throws DataException
	 */
	public static Object evalConditionalExpr( Object obj, int operator,
			Object Op1, Object Op2, CompareHints compareHints )
			throws DataException
	{
		return evalConditionalExpr( obj, operator, new Object[]{
				Op1, Op2
		}, compareHints );
	}

	/**
	 * 
	 * @param obj
	 * @param operator
	 * @param ops
	 * @return
	 * @throws DataException
	 */
	public static Object evalConditionalExpr( Object obj, int operator,
			Object[] ops ) throws DataException
	{
		return evalConditionalExpr( obj, operator, ops, null );
	}

	/**
	 * 
	 * @param obj
	 * @param operator
	 * @param op1
	 * @param op2
	 * @return A Boolean result
	 * @throws DataException
	 */
	public static Object evalConditionalExpr( Object obj, int operator,
			Object[] ops, CompareHints compareHints ) throws DataException
	{
		ExprTextAndValue[] opTextAndValue = new ExprTextAndValue[ops.length];
		for ( int i = 0; i < ops.length; i++ )
		{
			opTextAndValue[i] = createExprTextAndValueInstance( ops[i] );
		}
		
		Object resultObject = obj;
		Object[] resultOp = new Object[ops.length];
		for ( int i = 0; i < ops.length; i++ )
		{
			resultOp[i] = opTextAndValue[i].value;
			if ( operator != IConditionalExpression.OP_IN 
					&& operator != IConditionalExpression.OP_NOT_IN )
			{
				if ( opTextAndValue[i].value != null 
						&& opTextAndValue[i].value.getClass( ).isArray( ))
				{
					//For case multi-value type report parameter is involved in signle-value-required filters 
					
					//more than 1 values are provided for multi-value parameter
					if ( Array.getLength( opTextAndValue[i].value ) > 1 )
					{
						throw new DataException(
								ResourceConstants.BAD_COMPARE_SINGLE_WITH_MULITI, toStringForMultiValues( opTextAndValue[i].value ) );
					}
					//no or only one value is provided for multi-value parameter
					if ( Array.getLength( opTextAndValue[i].value ) == 0 )
					{
						resultOp[i] = null;
					}
					else if ( Array.getLength( opTextAndValue[i].value ) == 1 )
					{
						resultOp[i] = Array.get( opTextAndValue[i].value, 0 );
					}
					opTextAndValue[i].value = resultOp[i];
				}
			}
		}

		Object[] obArray = MiscUtil.isComparable( obj, operator, opTextAndValue );
		if ( obArray != null )
		{
			resultObject = obArray[0];
			for ( int i = 1; i < obArray.length; i++ )
			{
				resultOp[i - 1] = obArray[i];
			}
		}
		
		if ( logger.isLoggable( Level.FINER ) )
		{
			String logStr = "";
			for ( int i = 0; i < ops.length; i++ )
			{
				logStr += resultOp[i] == null
						? null
						: ( ", resultOp" + i + "=" + LogUtil.toString( resultOp[i] ) );
			}
			logger.entering( ScriptEvalUtil.class.getName( ),
					"evalConditionalExpr",
					"evalConditionalExpr() resultObject="
							+ LogUtil.toString( resultObject ) + ", operator="
							+ operator + logStr );
		}
		boolean result = false;

		if ( compareHints != null
				&& IBaseDataSetDesign.NULLS_ORDERING_EXCLUDE_NULLS.equals( compareHints.getNullType( ) ) )
		{
			if ( resultObject == null )
				return false;
		}
		switch ( operator )
		{
			case IConditionalExpression.OP_EQ :
				result = compare( resultObject, resultOp[0], compareHints ) == 0;
				break;
			case IConditionalExpression.OP_NE :
				result = compare( resultObject, resultOp[0], compareHints ) != 0;
				break;
			case IConditionalExpression.OP_LT :
				result = compare( resultObject, resultOp[0], compareHints ) < 0;
				break;
			case IConditionalExpression.OP_LE :
				result = compare( resultObject, resultOp[0], compareHints ) <= 0;
				break;
			case IConditionalExpression.OP_GE :
				result = compare( resultObject, resultOp[0], compareHints ) >= 0;
				break;
			case IConditionalExpression.OP_GT :
				result = compare( resultObject, resultOp[0], compareHints ) > 0;
				break;
			case IConditionalExpression.OP_BETWEEN :
				result = between( resultObject,
						resultOp[0],
						resultOp[1],
						compareHints );
				break;
			case IConditionalExpression.OP_NOT_BETWEEN :
				result = !( between( resultObject,
						resultOp[0],
						resultOp[1],
						compareHints ) );
				break;
			case IConditionalExpression.OP_NULL :
				result = resultObject == null;
				break;
			case IConditionalExpression.OP_NOT_NULL :
				result = resultObject != null;
				break;
			case IConditionalExpression.OP_TRUE :
				result = isTrueOrFalse( resultObject, Boolean.TRUE );
				break;
			case IConditionalExpression.OP_FALSE :
				result = isTrueOrFalse( resultObject, Boolean.FALSE );
				break;
			case IConditionalExpression.OP_LIKE :
				result = like( resultObject, resultOp[0] );
				break;
			case IConditionalExpression.OP_NOT_LIKE :
				result = !like( resultObject, resultOp[0] );
				break;
				
			case IConditionalExpression.OP_TOP_N :
			case IConditionalExpression.OP_BOTTOM_N :
			case IConditionalExpression.OP_TOP_PERCENT :
			case IConditionalExpression.OP_BOTTOM_PERCENT :
				// Top/Bottom expressions are only available in filters for now; direct evaluation is not supported
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "Top/Bottom(N) outside of row filters" );
				
		/*
		 * case IConditionalExpression.OP_ANY : throw new DataException(
		 * ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "ANY" );
		 */
			case IConditionalExpression.OP_MATCH :
				result = match( resultObject, resultOp[0] );
				break;
			case IConditionalExpression.OP_NOT_MATCH :
				result = !match( resultObject, resultOp[0] );
				break;
			case IConditionalExpression.OP_IN :
				result = in( resultObject, resultOp );
				break;
			case IConditionalExpression.OP_NOT_IN :
				result = !in( resultObject, resultOp );
				break;
			case IConditionalExpression.OP_JOINT :
				result = joint( resultObject, resultOp[0] );
				break;
			default :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, Integer.valueOf( operator) );
		}
		
		logger.exiting( ScriptEvalUtil.class.getName( ),
				"evalConditionalExpr",
				 Boolean.valueOf( result ) );
		return Boolean.valueOf( result );
	}

	/**
	 * @param o1
	 * @return
	 */
	private static ExprTextAndValue createExprTextAndValueInstance( Object o )
	{
		ExprTextAndValue op;
		if(! (o instanceof ExprTextAndValue ))
			op = ExprTextAndValue.newInstance( o );
		else
			op = (ExprTextAndValue)o;
		return op;
	}

	/**
	 * Compare two value according to given comparator.
	 * @param obj1
	 * @param obj2
	 * @param comp
	 * @return
	 * @throws DataException
	 */
	public static int compare( Object obj1, Object obj2,
			CompareHints compareHints ) throws DataException
	{
		if ( obj1 == null || obj2 == null )
		{
			return CompareNullValue( obj1, obj2, compareHints );
		}
		try
		{
			if ( MiscUtil.isSameType( obj1, obj2 ) )
			{
				if ( obj1 instanceof String )
				{
					if ( compareHints == null )
                        return ( (String)obj1 ).compareTo( (String)obj2 );
					return compareAsString( obj1, obj2, compareHints );
				}
				else if ( obj1 instanceof Boolean )
				{
					if ( obj1.equals( obj2 ) )
						return 0;

					Boolean bool = (Boolean) obj1;
					if ( bool.equals( Boolean.TRUE ) )
						return 1;
					else
						return -1;
				}
				else if ( obj1 instanceof Comparable )
				{
					return ( (Comparable) obj1 ).compareTo( obj2 );
				}
				else if ( obj1 instanceof Collection )
				{
					Collection o1 = (Collection) obj1;
					Collection o2 = (Collection) obj2;
					if ( o1.size( ) != o2.size( ) )
						return -1;
					Iterator it1 = o1.iterator( );
					Iterator it2 = o2.iterator( );
					while ( it1.hasNext( ) )
					{
						int result = compare( it1.next( ), it2.next( ) );
						if ( result != 0 )
							return result;
					}
					return 0;
				}
				// most judgements should end here
				else
				{
					return compareAsString( obj1, obj2, compareHints );
				}
			}
			else if ( MiscUtil.isBigDecimal( obj1 )
					|| MiscUtil.isBigDecimal( obj2 ) )
			{
				BigDecimal a = DataTypeUtil.toBigDecimal( obj1 );
				BigDecimal b = DataTypeUtil.toBigDecimal( obj2 );
				return a.compareTo( b );
			}
			else if ( MiscUtil.isNumericOrString( obj1 )
					&& MiscUtil.isNumericOrString( obj2 ) )
			{
				try
				{
					return DataTypeUtil.toDouble( obj1 )
							.compareTo( DataTypeUtil.toDouble( obj2 ) );
				}
				catch ( Exception e )
				{
					return compareAsString( obj1, obj2, compareHints );
				}
			}
			else if ( MiscUtil.isDateOrString( obj1 )
					&& MiscUtil.isDateOrString( obj2 ) )
			{
				try
				{
					return DataTypeUtil.toDate( obj1 )
							.compareTo( DataTypeUtil.toDate( obj2 ) );
				}
				catch ( Exception e )
				{
					return compareAsString( obj1, obj2, compareHints );
				}
			}
			else if ( MiscUtil.isBooleanOrString( obj1 )
					&& MiscUtil.isBooleanOrString( obj2 ) )
			{
				try
				{
					boolean b1 = DataTypeUtil.toBoolean( obj1 ).booleanValue( );
					boolean b2 = DataTypeUtil.toBoolean( obj2 ).booleanValue( );
					if ( b1 == b2 )
					{
						return 0;
					}
					else if ( b1 == false && b2 == true )
					{
						return -1;
					}
					else
					{
						return 1;
					}
				}
				catch ( Exception e )
				{
					return compareAsString( obj1, obj2, compareHints );
				}
			}
			else if ( obj1 instanceof String || obj2 instanceof String )
			{
				return compareAsString( obj1, obj2, compareHints );
			}
			else
				throw new DataException( ResourceConstants.BAD_COMPARE_EXPR,
						new Object[]{
								obj1, obj2
						} );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}

	}
	
	private static String toStringForMultiValues( Object o )
	{
		if ( o == null )
		{
			return null;
		}
		if ( o.getClass( ).isArray( ) && Array.getLength( o ) > 1 )
		{
			StringBuilder buf = new StringBuilder( );
			buf.append(Array.get( o, 0 ));
			buf.append(", ");
			buf.append(Array.get( o, 1));
			buf.append( "...");
			return buf.toString( );
		}
		return o.toString( );
	}

	private static int CompareNullValue( Object obj1, Object obj2,
			CompareHints compareHints )
	{
		if ( compareHints == null )
		{
			// all non-null values are greater than null value
			if ( obj1 == null && obj2 != null )
				return -1;
			else if ( obj1 != null && obj2 == null )
				return 1;
			else
				return 0;
		}
		else
		{
			String type = compareHints.getNullType( );
			if ( IBaseDataSetDesign.NULLS_ORDERING_NULLS_HIGHEST.equals( type ) )
			{
				// all non-null values are less than null value
				if ( obj1 == null && obj2 != null )
					return 1;
				else if ( obj1 != null && obj2 == null )
					return -1;
				else
					return 0;
			}
			else if ( IBaseDataSetDesign.NULLS_ORDERING_NULLS_LOWEST.equals( type ) )
			{
				// all non-null values are greater than null value
				if ( obj1 == null && obj2 != null )
					return -1;
				else if ( obj1 != null && obj2 == null )
					return 1;
				else
					return 0;
			}
			else
			{
				// all non-null values are greater than null value
				if ( obj1 == null && obj2 != null )
					return -1;
				else if ( obj1 != null && obj2 == null )
					return 1;
				else
					return 0;
			}
		}
	}

	private static int compareAsString( Object obj1, Object obj2,
			CompareHints comp ) throws BirtException
	{
		return ( comp == null || comp.getComparator( ) == null )
				? DataTypeUtil.toString( obj1 )
						.compareTo( DataTypeUtil.toString( obj2 ) )
				: comp.getComparator( ).compare( DataTypeUtil.toString( obj1 ),
						DataTypeUtil.toString( obj2 ) );
	}

	/**
	 * Most objects should already be formatted to the same type by method
	 * formatToComparable at this point if neither of them is null. This method
	 * will therefore be terminated pretty soon except for calling from method
	 * between with weird parameters like obj:String, op1:Double and op2:Date.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return -1,0 and 1 standing for <,= and > respectively
	 * @throws DataException
	 */
	public static int compare( Object obj1, Object obj2 ) throws DataException
	{
		return compare( obj1, obj2, null );
	}

	/**
	 * @param resultObject
	 * @param resultOp1
	 * @param resultOp2
	 * @return true if resultObject is between resultOp1 and resultOp2, false
	 *         otherwise
	 * @throws DataException
	 */
	private static boolean between( Object resultObject, Object resultOp1,
			Object resultOp2, CompareHints compareHints ) throws DataException
	{
		return compare( resultObject, resultOp1, compareHints ) >= 0
				&& compare( resultObject, resultOp2, compareHints ) <= 0;
	}

	/**
	 * @param obj
	 * @param bln
	 * @return true if obj equals to bln, false otherwise
	 */
	private static boolean isTrueOrFalse( Object obj, Boolean bln )
	{
		if ( obj == null )
			return false;
		try
		{
			return DataTypeUtil.toBoolean( obj ).equals( bln );
		}
		catch ( BirtException e )
		{
			return false;
		}
	}
	// Pattern to determine if a Match operation uses Javascript regexp syntax
	private static Pattern s_JSReExprPattern;
	
	// Gets a matcher to determine if a match pattern string is of JavaScript syntax
	// The pattern matches string like "/regexpr/gmi", which is used in JavaScript to construct a RegExp object
	private static Matcher getJSReExprPatternMatcher( String patternStr )
	{
		if ( s_JSReExprPattern == null )
			s_JSReExprPattern = Pattern.compile("^/(.*)/([a-zA-Z]*)$");
		return s_JSReExprPattern.matcher( patternStr );
	}
	
	private static boolean match( Object source, Object pattern ) throws DataException
	{
		String sourceStr = null;
		try
		{
			sourceStr = (source == null)? "": DataTypeUtil.toLocaleNeutralString( source );
		}
		catch ( BirtException e1 )
		{
			throw new DataException( e1.getLocalizedMessage( ), e1 );
		}
		String patternStr;
		try
		{
			patternStr = ( pattern == null )? "" : DataTypeUtil.toLocaleNeutralString( pattern );
		}
		catch ( BirtException e1 )
		{
			throw new DataException( e1.getLocalizedMessage( ), e1 );
		}

		// Pattern can be one of the following:
		// (1)Java regular expression pattern
		// (2)JavaScript RegExp construction syntax: "/RegExpr/[flags]", where flags 
		// can be a combination of 'g', 'm', 'i'
		Matcher jsReExprMatcher = getJSReExprPatternMatcher( patternStr ); 
		int flags = 0;
		if ( jsReExprMatcher.matches() )
		{
			// This is a Javascript syntax
			// Get the flags; we only expect "m", "i", "g"
			String flagStr = patternStr.substring( jsReExprMatcher.start(2), 
					jsReExprMatcher.end(2) );
			for ( int i = 0; i < flagStr.length(); i++)
			{
				switch ( flagStr.charAt(i) )
				{
					case 'm': flags |= Pattern.MULTILINE; break;
					case 'i': flags |= Pattern.CASE_INSENSITIVE; break;
					case 'g': break;			// this flag has no effect
						
					default:
						throw new DataException( ResourceConstants.MATCH_ERROR, patternStr );
				}
			}
			patternStr = patternStr.substring( jsReExprMatcher.start(1), 
					jsReExprMatcher.end(1) );
		}
		
		try
		{
			Matcher m = Pattern.compile( patternStr, flags ).matcher( sourceStr);
			return m.find(); 
		}
		catch ( PatternSyntaxException e )
		{
			throw new DataException( ResourceConstants.MATCH_ERROR, e, patternStr );
		}
	}

	/**
	 * @return true if obj1 matches the given pattern, false otherwise
	 * @throws DataException
	 */
	private static boolean like( Object source, Object pattern ) throws DataException
	{
		String sourceStr = null;
		try
		{
			sourceStr = (source == null)? "": DataTypeUtil.toLocaleNeutralString( source );
		}
		catch ( BirtException e1 )
		{
			throw new DataException( e1.getLocalizedMessage( ), e1 );
		}
		String patternStr;
		try
		{
			patternStr = ( pattern == null )? "" : DataTypeUtil.toLocaleNeutralString( pattern );
		}
		catch ( BirtException e1 )
		{
			throw new DataException( e1.getLocalizedMessage( ), e1 );
		}
	
		// As per Bugzilla 115940, LIKE operator's pattern syntax is SQL-like: it
		// recognizes '_' and '%'. Backslash '\' escapes the next character.
		
		// Construct a Java RegExp pattern based on input. We need to translate 
		// unescaped '%' to '.*', and '_' to '.'
		// Also need to escape any RegExp metacharacter in the source pattern.
		
		final String reservedChars = "([{^$|)?*+.";
		int patternLen = patternStr.length();
		StringBuffer buffer = new StringBuffer( patternLen * 2 );
		
		for ( int i = 0; i < patternLen; i++)
		{
			char c = patternStr.charAt(i);
			if ( c == '\\' )
			{
				// Escape char; copy next character to new pattern if 
				// it is '\', '%' or '_'
				++i;
				if ( i < patternLen )
				{
					c = patternStr.charAt( i );
					if ( c == '%' || c == '_' )
						buffer.append( c );
					else if ( c == '\\' )
						buffer.append( "\\\\");		// Need to escape \
				}
				else
				{
					buffer.append( "\\\\" );  	// Leave last \ and escape it
				}
			}
			else if ( c == '%')
			{
				buffer.append(".*");
			}
			else if ( c == '_')
			{
				buffer.append(".");
			}
			else
			{
				// Copy this char to target, escape if it is a metacharacter
				if ( reservedChars.indexOf(c) >= 0 )
				{
					buffer.append('\\');
				}
				buffer.append(c);
			}
		}
		
		try
		{
			String newPatternStr = buffer.toString();
			Pattern p = Pattern.compile( newPatternStr );
			Matcher m = p.matcher( sourceStr.toString( ) );
			return m.matches( );
		}
		catch ( PatternSyntaxException e )
		{
			throw new DataException( ResourceConstants.MATCH_ERROR,  e, pattern );
		}
	}
	
	/**
	 * 
	 * @param resultObj
	 * @return
	 * @throws DataException
	 */
	private static boolean in( Object target, Object[] resultObj )
			throws DataException
	{
		if ( resultObj == null )
			return false;
		for ( int i = 0; i < resultObj.length; i++ )
		{
			if ( compare( target, resultObj[i] ) == 0 )
				return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @param resultObj
	 * @return
	 * @throws DataException
	 */
	private static boolean joint( Object target, Object resultObj )
			throws DataException
	{
		if ( resultObj == null || target == null )
			return false;
		return !java.util.Collections.disjoint( Arrays.asList( target.toString( )
				.split( "," )),
				Arrays.asList( resultObj.toString( ).split( "," ) ) ) ;
	}

	/**
	 * Evaluates a IJSExpression or IConditionalExpression
	 * 
	 * @param expr
	 * @param cx
	 * @param scope
	 * @param source
	 * @param lineNo
	 * @return 
	 * @throws BirtException 
	 */
	public static Object evalExpr( IBaseExpression expr, ScriptContext cx,
			String source, int lineNo ) throws DataException
	{
		try
		{
			if ( logger.isLoggable( Level.FINER ) )
				logger.entering( ScriptEvalUtil.class.getName( ),
						"evalExpr",
						"evalExpr() expr="
								+ LogUtil.toString( expr ) + ", source="
								+ source + ", lineNo=" + lineNo );
			Object result;
			if ( expr == null )
			{
				result = null;
			}
			else if ( expr instanceof IConditionalExpression )
			{
				// If this is a prepared top(n)/bottom(n) expr, use its
				// evaluator
				Object handle = expr.getHandle( );
				if ( handle instanceof NEvaluator )
				{
					result = Boolean.valueOf( ( (NEvaluator) handle ).evaluate( cx,
							( (IDataScriptEngine) cx.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( cx ), null ) );
				}
				else
				{
					ConditionalExpression conditionalExpr = (ConditionalExpression) expr;
					Object expression = evalExpr( conditionalExpr.getExpression( ),
							cx,
							source,
							lineNo );
					if ( conditionalExpr.getOperand1( ) instanceof IExpressionCollection )
					{
						IExpressionCollection combinedExpr = (IExpressionCollection) ( (IConditionalExpression) expr ).getOperand1( );
						Object[] exprs = combinedExpr.getExpressions( )
								.toArray( );

						Object[] opValues = new Object[exprs.length];

						for ( int i = 0; i < opValues.length; i++ )
						{
							opValues[i] = evalExpr( (IBaseExpression) exprs[i],
									cx,
									source,
									lineNo );
						}
						result = evalConditionalExpr( expression,
								conditionalExpr.getOperator( ),
								MiscUtil.flatternMultipleValues( opValues ),
								null );
					}
					else
					{

						Object Op1 = evalExpr( MiscUtil.constructValidScriptExpression( (IScriptExpression) conditionalExpr.getOperand1( ) ),
								cx,
								source,
								lineNo );
						Object Op2 = evalExpr( MiscUtil.constructValidScriptExpression( (IScriptExpression) conditionalExpr.getOperand2( ) ),
								cx,
								source,
								lineNo );
						result = evalConditionalExpr( expression,
								conditionalExpr.getOperator( ),
								new Object[]{
										Op1, Op2
								},
								null );
					}
				}
			}
			else if ( expr instanceof ICollectionConditionalExpression )
			{
				Collection<IScriptExpression> testExpr = ((ICollectionConditionalExpression)expr).getExpr( );
				Collection<Collection<IScriptExpression>> operand = ((ICollectionConditionalExpression)expr).getOperand( );
				List<Object> testObj = new ArrayList<Object>( );
				boolean in = false;
				for( IScriptExpression se : testExpr )
				{
					testObj.add( evalExpr( se, cx, source, lineNo  ) );
				}
				for( Collection<IScriptExpression> op : operand )
				{
					List<Object> targetObj = new ArrayList<Object>( );
					for( IScriptExpression se : op )
					{
						if( se == null )
						{
							targetObj.add( null );
						}
						else
						{
							if( se.getHandle( )== null )
							{
								se.setHandle( evalExpr( se, cx, source, lineNo ) );
							}
							targetObj.add( se.getHandle( ) );
						}
					}
					if( compareIgnoreNull( testObj, targetObj ) == 0 )
					{
						in = Boolean.TRUE;
						break;
					}
				}
				result = ( ( (ICollectionConditionalExpression) expr ).getOperator( ) == ICollectionConditionalExpression.OP_IN )
						? in : ( !in );
			}
			else
			{
				IScriptExpression jsExpr = (IScriptExpression) expr;
				if( BaseExpression.constantId.equals( jsExpr.getScriptId( ) ) && jsExpr.getHandle( ) != null )
				{
					result = jsExpr.getHandle( );
				}
				else
				{
					if( BaseExpression.constantId.equals( jsExpr.getScriptId( ) ) )
					{
						result = jsExpr.getText( );
						jsExpr.setHandle( result );
					}
					else if ( jsExpr.getText( ) != null && jsExpr.getHandle( ) != null )
					{
						if ( jsExpr.getHandle( ) instanceof ICompiledScript )
						{
							result = cx.evaluate( (ICompiledScript) jsExpr.getHandle( ) );
						}
						else
						{
							result = ( (CompiledExpression) jsExpr.getHandle( ) ).evaluate( cx,
									( (IDataScriptEngine) cx.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( cx ) );
						}
					}
					else
					{
						result = evaluateJSAsExpr( cx,
								( (IDataScriptEngine) cx.getScriptEngine( IDataScriptEngine.ENGINE_NAME ) ).getJSScope( cx ),
								jsExpr.getText( ),
								source,
								lineNo );
					}
					
				}
			}
			
			if ( logger.isLoggable( Level.FINER ) )
				logger.exiting( ScriptEvalUtil.class.getName( ),
						"evalExpr",
						result );
			return result;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}

	}	
	
	public static int compareIgnoreNull( List<Object> valueList, List<Object> targetList ) throws DataException
	{
		for( int i = 0; i < valueList.size( ); i++ )
		{
			if( targetList.get( i ) == null )
				continue;
			int result = compare( valueList.get( i ), targetList.get( i ) ); 
			if(  result != 0 )
				return result;
		}
		return 0;
	}
	
	/**
	 * Evaluates a ROM script and converts the result type into one accepted by
	 * BIRT: Double (for all numeric types), java.util.Date, String, Boolean.
	 * Converts Javascript exception and script runtime exceptions to
	 * DataException
	 * 
	 * @param cx
	 * @param scope
	 * @param scriptText
	 * @param source
	 * @param lineNo
	 * @return
	 * @throws DataException
	 */
	public static Object evaluateJSAsExpr( ScriptContext cx, Scriptable scope,
			String scriptText, String source, int lineNo)
			throws DataException 
	{
		if ( logger.isLoggable( Level.FINER ) )
			logger.entering( ScriptEvalUtil.class.getName( ),
				"evaluateJSExpr",
				"evaluateJSExpr() scriptText=" + scriptText 
				+ ", source=" + source 
				+ ", lineNo=" + lineNo);
		
		Object result;
		try
		{
			result = JavascriptEvalUtil.evaluateScript( Context.getCurrentContext( ), scope, scriptText, source, 0 );
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
		return result;
	}
	
	
	/**
	 * Wrap the text and value of the operand
	 * 
	 */
	public static class ExprTextAndValue
	{
		Object value;

		/**
		 * 
		 * @param exprText
		 * @param value
		 * @return
		 */
		public static ExprTextAndValue newInstance(	Object value )
		{
			return new ExprTextAndValue( value );
		}

		/**
		 * 
		 * @param exprText
		 * @param value
		 */
		public ExprTextAndValue( Object value )
		{
			this.value = value;
		}
	}	
	
	/**
	 * Utility for miscellaneous use
	 * 
	 */
	private static class MiscUtil
	{
		/**
		 * 
		 * @param resultExpr
		 * @param resultOp1
		 * @return
		 */
		private static boolean isSameType( Object resultExpr, Object resultOp1 )
		{
			return resultExpr.getClass( ).equals( resultOp1.getClass( ) );
		}
		
		/**
		 * 
		 * @param result
		 * @return
		 */
		private static boolean isNumericOrString( Object result )
		{
			return ( result instanceof Number ) || ( result instanceof String );
		}
		/**
		 * 
		 * @param result
		 * @return
		 */
		private static boolean isBigDecimal( Object result )
		{
			return result instanceof BigDecimal;
		}

		/**
		 * 
		 * @param result
		 * @return
		 */
		private static boolean isDateOrString( Object result )
		{
			return ( result instanceof Date ) || ( result instanceof String );
		}
		
		/**
		 * 
		 * @param result
		 * @return
		 */
		private static boolean isBooleanOrString( Object result )
		{
			return ( result instanceof Boolean ) || ( result instanceof String );
		}
		
		/**
		 * 
		 * @param obj
		 * @param operator
		 * @param operands
		 * @return
		 */
		private static Object[] isComparable( Object obj, int operator,
				ExprTextAndValue[] operands )
		{
			if ( needFormat( obj, operator, operands ) )
				return formatToComparable( obj, operands );
			return null;
		}

		/**
		 * 
		 * @param obj
		 * @param operator
		 * @param ops
		 * @return
		 */
		private static boolean needFormat( Object obj, int operator,
				ExprTextAndValue[] ops )
		{
			if ( operator < IConditionalExpression.OP_EQ
					|| ( operator > IConditionalExpression.OP_NOT_BETWEEN && operator < IConditionalExpression.OP_IN )
					|| obj == null || ops.length == 0 || ops[0].value == null )
				return false;
			// op2.value can not be null either if it's a between method
			else if ( ( operator == IConditionalExpression.OP_BETWEEN || operator == IConditionalExpression.OP_NOT_BETWEEN ) &&
					ops.length < 2 )
				return false;

			return true;
		}
		
		/**
		 * To ease the methods compare and between. Exception with specific
		 * explanation will be thrown if anything goes wrong.
		 * 
		 * @param obj
		 * @param operands
		 * @return
		 */
		private static Object[] formatToComparable( Object obj,
				ExprTextAndValue[] operands )
		{
			Object[] obArray = new Object[operands.length + 1];
			obArray[0] = obj;
			for ( int i = 0; i < operands.length; i++ )
			{
				obArray[i + 1] = operands[i].value;
			}
			boolean isSameType = true;
			
			// obj will always be considered as the default data type
			// skip if op2.value!=null but is not same type as obj
			if ( isSameType( obj, obArray[1] ) )
			{
				for ( int i = 1; i < operands.length; i++ )
				{
					if ( obArray[i + 1] != null &&
							!isSameType( obj, obArray[i + 1] ) )
					{
						isSameType = false;
						break;
					}
				}
			}
			else
			{
				isSameType = false;
			}

			if ( isSameType )
				return obArray;
			else if ( obj instanceof Boolean )
				populateObArray( obArray[1], obArray );
			else
				populateObArray( obj, obArray );
			return obArray;
		}

		private static Object[] populateObArray( Object obj, Object[] obArray )
		{
			try
			{
				for ( int i = 0; i < obArray.length; i++ )
				{
					if( obArray[i] instanceof Object[] )
						return obArray;
				}
				if ( obj instanceof Number && !( obj instanceof BigDecimal ) )
				{
					for ( int i = 0; i < obArray.length; i++ )
					{
						obArray[i] = DataTypeUtil.toDouble( obArray[i] );
					}
				}
				else if ( obj instanceof java.sql.Date )
				{
					for ( int i = 0; i < obArray.length; i++ )
					{
						obArray[i] = DataTypeUtil.toSqlDate( obArray[i] );
					}
				}
				else if ( obj instanceof java.sql.Time )
				{
					for ( int i = 0; i < obArray.length; i++ )
					{
						obArray[i] = DataTypeUtil.toSqlTime( obArray[i] );
					}
				}
				else if ( obj instanceof Date )
				{
					for ( int i = 0; i < obArray.length; i++ )
					{
						obArray[i] = DataTypeUtil.toDate( obArray[i] );
					}
				}
			}
			catch ( BirtException e )
			{
				// If failed to convert to same date type for comparation,
				// simply convert them to String.
				try
				{
					makeObjectArrayStringArray( obArray );
				}
				catch ( BirtException e1 )
				{
					//should never reach here.
				}
			}
			// obArray will remain the same if obj is String rather than
			// Date,Number or Boolean
			return obArray;
		}

		/**
		 * 
		 * @param obArray
		 * @throws BirtException 
		 */
		private static void makeObjectArrayStringArray( Object[] obArray ) throws BirtException
		{
			for ( int i = 0; i < obArray.length; i++ )
			{
				if ( obArray[i] != null )
					obArray[i] = DataTypeUtil.toString( obArray[i] );
			}
		}
		
		/**
		 * @param ise
		 * @return
		 */
		private static IScriptExpression constructValidScriptExpression(
				IScriptExpression ise )
		{
			if( ise != null && BaseExpression.constantId.equals( ise.getScriptId( ) ) )
				return ise;
			
			return ise != null
					&& ise.getText( ) != null
					&& ise.getText( ).trim( ).length( ) > 0 ? ise
					: new ScriptExpression( "null" );
		}
		
		/**
		 * 
		 * @return
		 */
		private static Object[] flatternMultipleValues( Object[] values )
		{
			if ( values == null || values.length == 0 )
				return new Object[0];
			List flattern = new ArrayList( );
			for ( int i = 0; i < values.length; i++ )
			{
				if ( values[i] instanceof Object[] )
				{
					Object[] flatternObj = (Object[]) values[i];
					flattern.addAll( Arrays.asList( flatternMultipleValues( flatternObj ) ) );
				}
				else
				{
					flattern.add( values[i] );
				}
			}
			return flattern.toArray( );
		}
	}
}

