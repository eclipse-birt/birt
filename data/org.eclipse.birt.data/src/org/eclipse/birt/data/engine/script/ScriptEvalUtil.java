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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
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
	public static ExprTextAndValue newExprInfo( String exprText, Object value )
	{
		return ExprTextAndValue.newInstance( exprText, value );
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
	 * @param op1
	 * @param op2
	 * @return A Boolean result
	 * @throws DataException
	 */
	public static Object evalConditionalExpr( Object obj,
			int operator, Object o1, Object o2 )
			throws DataException
	{
		ExprTextAndValue op1 = createExprTextAndValueInstance( o1 );
		ExprTextAndValue op2 = createExprTextAndValueInstance( o2 );
		
		Object resultObject = obj;
		Object resultOp1 = op1.value;
		Object resultOp2 = op2.value;

		Object[] obArray = MiscUtil.isComparable( obj, operator, op1, op2 );
		if ( obArray != null )
		{
			resultObject = obArray[0];
			resultOp1 = obArray[1];
			resultOp2 = obArray[2];
		}
		
		if ( logger.isLoggable( Level.FINER ) )
			logger.entering( 
				ScriptEvalUtil.class.getName( ),
				"evalConditionalExpr",
				"evalConditionalExpr() resultObject="
				+ LogUtil.toString( resultObject )
				+ ", operator=" + operator
				+ ( resultOp1 == null? null: ( ", resultOp1=" + LogUtil.toString( resultOp1 ) ) )
				+ ( resultOp2 == null? null: ( ", resultOp2=" + LogUtil.toString( resultOp2 ) ) ) 
				);

		boolean result = false;
		switch ( operator )
		{
			case IConditionalExpression.OP_EQ :
			    result = compare( resultObject, resultOp1 ) == 0;
				break;
			case IConditionalExpression.OP_NE :
			    result = compare( resultObject, resultOp1 ) != 0;
				break;
			case IConditionalExpression.OP_LT :
				result = compare( resultObject, resultOp1 ) < 0;
				break;
			case IConditionalExpression.OP_LE :
				result = compare( resultObject, resultOp1 ) <= 0;
				break;
			case IConditionalExpression.OP_GE :
				result = compare( resultObject, resultOp1 ) >= 0;
				break;
			case IConditionalExpression.OP_GT :
				result = compare( resultObject, resultOp1 ) > 0;
				break;
			case IConditionalExpression.OP_BETWEEN :
				result = between( resultObject, resultOp1, resultOp2 );
				break;
			case IConditionalExpression.OP_NOT_BETWEEN :
				result = !( between( resultObject, resultOp1, resultOp2 ) );
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
				result = like( resultObject, resultOp1 );
				break;
			case IConditionalExpression.OP_NOT_LIKE :
				result = !like( resultObject, resultOp1 );
				break;
				
			case IConditionalExpression.OP_TOP_N :
			case IConditionalExpression.OP_BOTTOM_N :
			case IConditionalExpression.OP_TOP_PERCENT :
			case IConditionalExpression.OP_BOTTOM_PERCENT :
				// Top/Bottom expressions are only available in filters for now; direct evaluation is not supported
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "Top/Bottom(N) outside of row filters" );
				
		/*	case IConditionalExpression.OP_ANY :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "ANY" );*/
			case IConditionalExpression.OP_MATCH :
				result = match( resultObject, resultOp1 );
				break;
			case IConditionalExpression.OP_NOT_MATCH :
				result = !match( resultObject, resultOp1 );
				break;
			default :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, new Integer(operator) );
		}
		
		logger.exiting( ScriptEvalUtil.class.getName( ),
				"evalConditionalExpr",
				new Boolean( result ) );
		return new Boolean( result );
	}

	/**
	 * @param o1
	 * @return
	 */
	private static ExprTextAndValue createExprTextAndValueInstance( Object o )
	{
		ExprTextAndValue op;
		if(! (o instanceof ExprTextAndValue ))
			op = ExprTextAndValue.newInstance( "", o );
		else
			op = (ExprTextAndValue)o;
		return op;
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
	    if (obj1 == null || obj2 == null) 
	    {
            // all non-null values are greater than null value
	        if (obj1 == null && obj2 != null)
                return -1;
            else if (obj1 != null && obj2 == null)
                return 1;
            else
                return 0;
        }
	    
		try
		{
			if ( MiscUtil.isSameType( obj1, obj2 ) )
			{
				if ( obj1 instanceof Boolean )
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
				// most judgements should end here
				else
				{
					return obj1.toString( ).compareTo( obj2.toString( ) );
				}
			}
			else if ( MiscUtil.isNumericOrString( obj1 ) && MiscUtil.isNumericOrString( obj2 ) )
			{
				try
				{
					return DataTypeUtil.toDouble( obj1 ).compareTo(
						DataTypeUtil.toDouble( obj2 ) );
				}catch (Exception e )
				{
					return DataTypeUtil.toString( obj1 ).compareTo(
							DataTypeUtil.toString( obj2 ) );
				}
			}
			else if ( MiscUtil.isDateOrString( obj1 ) && MiscUtil.isDateOrString( obj2 ) )
			{
			try
				{
					return DataTypeUtil.toDate( obj1 )
							.compareTo( DataTypeUtil.toDate( obj2 ) );
				}
				catch ( Exception e )
				{
					return DataTypeUtil.toString( obj1 )
							.compareTo( DataTypeUtil.toString( obj2 ) );
				}
			}
			else
				throw new DataException( ResourceConstants.INVALID_TYPE_IN_EXPR );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
		}
	}
	
	/**
	 * @param resultObject
	 * @param resultOp1
	 * @param resultOp2
	 * @return true if resultObject is between resultOp1 and resultOp2, false otherwise
	 * @throws DataException
	 */
	private static boolean between( Object resultObject, Object resultOp1,
			Object resultOp2 ) throws DataException
	{
		Object min, max;
		if ( compare( resultOp1, resultOp2 ) <= 0 )
		{
			min = resultOp1;
			max = resultOp2;
		}
		else
		{
			min = resultOp2;
			max = resultOp1;
		}
		return compare( resultObject, min ) >= 0
				&& compare( resultObject, max ) <= 0;
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
		String sourceStr = (source == null)? "": source.toString();
		String patternStr = ( pattern == null )? "" : pattern.toString();

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
			return m.lookingAt(); 
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
		String sourceStr = (source == null)? "": source.toString();
		String patternStr = ( pattern == null )? "" : pattern.toString();
	
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
	 * Evaluates a IJSExpression or IConditionalExpression
	 * 	
	 * @param expr
	 * @param cx
	 * @param scope
	 * @param source
	 * @param lineNo
	 * @return 
	 * @throws DataException
	 */
	public static Object evalExpr( IBaseExpression expr, Context cx, Scriptable scope, 
				String source, int lineNo )
		throws DataException
	{
		if ( logger.isLoggable( Level.FINER ) )
			logger.entering( ScriptEvalUtil.class.getName( ),
				"evalExpr",
				"evalExpr() expr="
						+ LogUtil.toString( expr ) + ", source=" + source
						+ ", lineNo=" + lineNo );
		Object result;
		if ( expr == null )
		{
			result = null;
		}
		else if ( expr instanceof IConditionalExpression)
		{
			// If this is a prepared top(n)/bottom(n) expr, use its evaluator
			Object handle = expr.getHandle();
			if ( handle instanceof NEvaluator )
			{
				result =  Boolean.valueOf (((NEvaluator)handle).evaluate( cx, scope ));
			}
			else
			{
				ConditionalExpression ConditionalExpr = (ConditionalExpression) expr;
				Object expression = evalExpr( ConditionalExpr.getExpression( ), cx, scope, source, lineNo );
				Object Op1 = evalExpr( MiscUtil.constructValidScriptExpression ( ConditionalExpr.getOperand1() ), cx, scope, source, lineNo );
				Object Op2 = evalExpr( MiscUtil.constructValidScriptExpression ( ConditionalExpr.getOperand2() ), cx, scope, source, lineNo );
				result = evalConditionalExpr( expression, ConditionalExpr.getOperator( ), Op1, Op2 ); 
			}
		}
		else
		{
			IScriptExpression jsExpr = (IScriptExpression) expr;
			if ( jsExpr.getText( ) != null && jsExpr.getHandle( ) != null )
			{
				result = ((CompiledExpression) jsExpr.getHandle( ) ).evaluate( cx, scope );
			}
			else
			{
				result = evaluateJSAsExpr( cx,
						scope,
						jsExpr.getText( ),
						source,
						lineNo );
			}
		}
		
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( ScriptEvalUtil.class.getName( ),
					"evalExpr",
					result );
		return result;
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
	public static Object evaluateJSAsExpr(Context cx, Scriptable scope,
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
			result = JavascriptEvalUtil.evaluateScript( cx, scope, scriptText, source, lineNo );
		}
		catch (BirtException e)
		{
			throw DataException.wrap(e);
		}
		
		// the result might be a DataExceptionMocker.
		if ( result instanceof DataExceptionMocker )
		{
			throw DataException.wrap( ((DataExceptionMocker) result ).getCause( ));
		}
		
		return result;
	}
	
	
	/**
	 * Wrap the text and value of the operand
	 * 
	 */
	public static class ExprTextAndValue
	{
		String exprText;
		Object value;

		/**
		 * 
		 * @param exprText
		 * @param value
		 * @return
		 */
		public static ExprTextAndValue newInstance( String exprText,
				Object value )
		{
			return new ExprTextAndValue( exprText, value );
		}

		/**
		 * 
		 * @param exprText
		 * @param value
		 */
		public ExprTextAndValue( String exprText, Object value )
		{
			this.exprText = exprText;
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
		private static boolean isDateOrString( Object result )
		{
			return ( result instanceof Date ) || ( result instanceof String );
		}

		/**
		 * To check whether the expressions are comparable. If so, they will be
		 * formatted to the comparable.For operands, only ExprTextAndValue is
		 * acceptable.
		 * 
		 * @param obj
		 * @param operator
		 * @param op1
		 * @param op2
		 * @return
		 * @throws DataException
		 */
		private static Object[] isComparable( Object obj, int operator,
				ExprTextAndValue op1, ExprTextAndValue op2 )
				throws DataException
		{
			if ( needFormat( obj, operator, op1, op2 ) )
				return formatToComparable( obj, op1, op2 );

			return null;
		}

		/**
		 * 
		 * @param obj
		 * @param operator
		 * @param op1
		 * @param op2
		 * @return
		 */
		private static boolean needFormat( Object obj, int operator,
				ExprTextAndValue op1, ExprTextAndValue op2 )
		{
			// compare and between methods without null can get through.
			// for more information on operators,please refer to
			// /org.eclipse.birt.data/src/org/eclipse/birt/data/engine/api/IConditionalExpression.java
			if ( operator < IConditionalExpression.OP_EQ
					|| operator > IConditionalExpression.OP_NOT_BETWEEN
					|| obj == null || op1.value == null )
				return false;
			// op2.value can not be null either if it's a betteen method
			else if ( ( operator == IConditionalExpression.OP_BETWEEN || operator == IConditionalExpression.OP_NOT_BETWEEN )
					&& op2.value == null )
				return false;
			
			return true;
		}
		
		/**
		 * To ease the methods compare and between. Exception with specific
		 * explanation will be thrown if anything goes wrong.
		 * 
		 * @param obj
		 * @param op1
		 * @param op2
		 * @return
		 * @throws DataException
		 */
		private static Object[] formatToComparable( Object obj,
				ExprTextAndValue op1, ExprTextAndValue op2 )
				throws DataException
		{
			Object[] obArray = new Object[3];
			obArray[0] = obj;
			obArray[1] = op1.value;
			obArray[2] = op2.value;

			// obj will always be considered as the default data type
			// skip if op2.value!=null but is not same type as obj
			if ( isSameType( obj, obArray[1] ) )
			{
				if ( obArray[2] == null
						|| ( obArray[2] != null && isSameType( obj, obArray[2] ) ) )
				{
					return obArray;
				}
			}

			try
			{
				if ( obj instanceof Number )
				{

					obArray[0] = DataTypeUtil.toDouble( obj );

					obArray[1] = DataTypeUtil.toDouble( obArray[1] );
					if ( obArray[2] != null )
					{

						obArray[2] = DataTypeUtil.toDouble( obArray[2] );
					}

				}
				else if ( obj instanceof Date )
				{

					obArray[0] = DataTypeUtil.toDate( obj );

					obArray[1] = DataTypeUtil.toDate( obArray[1] );
					if ( obArray[2] != null )
					{

						obArray[2] = DataTypeUtil.toDate( obArray[2] );
					}

				}
				else if ( obj instanceof Boolean )
				{
					obArray[0] = DataTypeUtil.toBoolean( obj );

					obArray[1] = DataTypeUtil.toBoolean( obArray[1] );

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
		private static IScriptExpression constructValidScriptExpression ( IScriptExpression ise)
		{
		    return ise!=null && ise.getText().trim().length() > 0 ? ise : new ScriptExpression("null");
		}		
	}
}

