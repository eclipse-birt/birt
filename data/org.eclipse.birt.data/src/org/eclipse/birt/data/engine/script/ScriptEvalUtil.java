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

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.LogUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;


/**
 * Static utility methods to help evaluating Javascript expressions 
 */
public class ScriptEvalUtil
{
	private static Logger logger = Logger.getLogger( ScriptEvalUtil.class.getName( ) );
	/**
	 * Evaluates a conditional expression. A conditional expression comprises of
	 * a Javascript expression, an operator, and up to 2 operands (which are 
	 * Javascript expressions themselves).
	 * @return A Boolean result
	 */
	public static Object evalConditionalExpr( Object resultObject,
			int operator, Object resultOp1, Object resultOp2 )
			throws DataException
	{
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
				result = isTrue( resultObject );
				break;
			case IConditionalExpression.OP_FALSE :
				result = isFalse( resultObject );
				break;
			case IConditionalExpression.OP_LIKE :
				result = like( resultObject, resultOp1 );
				break;
			case IConditionalExpression.OP_TOP_N :
			//TODO
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "TopN" );
			case IConditionalExpression.OP_BOTTOM_N :
			//TODO
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "BottomN" );
			case IConditionalExpression.OP_TOP_PERCENT :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "TopNPercent" );
			case IConditionalExpression.OP_BOTTOM_PERCENT :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "BottomNPercent" );
			case IConditionalExpression.OP_ANY :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, "ANY" );
			default :
				throw new DataException(
						ResourceConstants.UNSUPPORTTED_COND_OPERATOR, new Integer(operator) );
		}
		
		logger.exiting( ScriptEvalUtil.class.getName( ),
				"evalConditionalExpr",
				new Boolean( result ) );
		return new Boolean( result );
	}


	private static boolean isSameType( Object resultExpr, Object resultOp1 )
	{
		return resultExpr.getClass( ).equals( resultOp1.getClass( ) );
	}

	private static boolean isNumericOrString( Object result )
	{
		return ( result instanceof Number ) || ( result instanceof String );
	}

	private static boolean isDateOrString( Object result )
	{
		return ( result instanceof Date ) || ( result instanceof String );
	}
	
	private static boolean isTrue( Object obj )
	{
		if ( obj == null )
		    return false;
		try
		{
			return DataTypeUtil.toBoolean( obj ).equals( Boolean.TRUE );
		}
		catch ( BirtException e )
		{
			return false;
		}
	}
	
	private static boolean isFalse( Object obj )
	{
		if ( obj == null )
		    return false;
		try
		{
			return DataTypeUtil.toBoolean( obj ).equals( Boolean.FALSE );
		}
		catch ( BirtException e )
		{
			return false;
		}
	}

	private static int compare( Object obj1, Object obj2 ) throws DataException
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
			if ( isSameType( obj1, obj2 ) )
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
				else
				{
					return obj1.toString( ).compareTo( obj2.toString( ) );
				}
			}
			else if ( isNumericOrString( obj1 ) && isNumericOrString( obj2 ) )
			{
				return DataTypeUtil.toDouble( obj1 ).compareTo(
						DataTypeUtil.toDouble( obj2 ) );
			}
			else if ( isDateOrString( obj1 ) && isDateOrString( obj2 ) )
			{
				return DataTypeUtil.toDate( obj1 ).compareTo(
						DataTypeUtil.toDate( obj2 ) );
			}
			else
				throw new DataException( ResourceConstants.INVALID_TYPE_IN_EXPR );
		}
		catch ( BirtException e )
		{
			throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR, e );
		}
	}
	
	public static Object evaluateJSAsMethod (Context cx, Scriptable scope, String scriptText, String source, int lineNo) throws DataException
	{
	    if ( logger.isLoggable( Level.FINER ) )
			logger.entering( ScriptEvalUtil.class.getName( ),
				"evaluateJSExpr",
				"evaluateJSExpr() scriptText=" + scriptText 
				+ ", source=" + source 
				+ ", lineNo=" + lineNo);
	    
	    Object result = evaluateJSScript (cx, scope, ScriptUtil.getTailoredScript( scriptText , scope ) , source, lineNo);
	    
	    if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( ScriptEvalUtil.class.getName( ),
					"evaluateJSExpr",
					convertNativeObjToJavaObj( result ) );
	    return result;
	}
	/**
	 * Evaluates a ROM script and converts the result type into one accepted by BIRT:
	 * Double (for all numeric types), java.util.Date, String, Boolean. Converts 
	 * Javascript exception and script runtime exceptions to DataException
	 * 
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
		
		Object result = evaluateJSScript( cx, scope, scriptText, source, lineNo);
		
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( ScriptEvalUtil.class.getName( ),
					"evaluateJSExpr",
					convertNativeObjToJavaObj( result ) );
		return convertNativeObjToJavaObj(result);
	}
	
	/**
	 * Converts the result type into one accepted by BIRT:
	 * Double (for all numeric types), java.util.Date, String, Boolean. 
	 */	
	public static Object convertNativeObjToJavaObj(Object inputObj){
		if ( logger.isLoggable( Level.FINER ) )
			logger.entering( ScriptEvalUtil.class.getName( ),
					"convertNativeObjToJavaObj",
					LogUtil.toString( inputObj ) );
		if ( inputObj instanceof Scriptable) 
		{
			// Return type is a Javascript native object
			// Convert to Java object with same value
			String jsClass = ((Scriptable) inputObj).getClassName();
			if (jsClass.equals("Date")) 
			{
				if ( logger.isLoggable( Level.FINER ) )
					logger.exiting( ScriptEvalUtil.class.getName( ),
							"convertNativeObjToJavaObj",
							new Date( (long) Context.toNumber( inputObj ) ) );
					return new Date( (long) Context.toNumber( inputObj ) );
			} 
			else if (jsClass.equals("Boolean")) 
			{
				if ( logger.isLoggable( Level.FINER ) )
					logger.exiting( ScriptEvalUtil.class.getName( ),
							"convertNativeObjToJavaObj",
							new Boolean( Context.toBoolean( inputObj ) ) );
				return new Boolean(Context.toBoolean(inputObj));
			} 
			else if (jsClass.equals("Number")) 
			{
				if ( logger.isLoggable( Level.FINER ) )
					logger.exiting( ScriptEvalUtil.class.getName( ),
							"convertNativeObjToJavaObj",
							new Double( Context.toNumber( inputObj ) ) );
				return new Double(Context.toNumber(inputObj));
			} 
			else if(jsClass.equals("String"))
			{
				// For JS "String" type, toString gives the correct result
				// For all other types that we cannot handle, toString is the best we can do
				if ( logger.isLoggable( Level.FINER ) )
					logger.exiting( ScriptEvalUtil.class.getName( ),
							"convertNativeObjToJavaObj",
							inputObj.toString( ).trim() );
				return inputObj.toString();
			}
			else
			{
				return Context.toType( inputObj, Object.class );
			}
		}
		else if ( inputObj!= null && inputObj.toString().equalsIgnoreCase("NaN") )
		    inputObj = null;
		
		if ( logger.isLoggable( Level.FINER ) )
			logger.exiting( ScriptEvalUtil.class.getName( ),
					"convertNativeObjToJavaObj",
					inputObj );
		return inputObj;
	}
	
	/**
	 * Evaluates a IJSExpression or IConditionalExpression
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
		if ( expr == null )
		{
			if ( logger.isLoggable( Level.FINER ) )
				logger.exiting( ScriptEvalUtil.class.getName( ),
						"evalExpr",
						null );
			return null;
		}
		else if ( expr instanceof IConditionalExpression)
		{
			ConditionalExpression ConditionalExpr = (ConditionalExpression) expr;
			Object expression = evalExpr( ConditionalExpr.getExpression( ), cx, scope, source, lineNo );
			Object Op1 = evalExpr( constructValidScriptExpression ( ConditionalExpr.getOperand1() ), cx, scope, source, lineNo );
			Object Op2 = evalExpr( constructValidScriptExpression ( ConditionalExpr.getOperand2() ), cx, scope, source, lineNo );
			if ( logger.isLoggable( Level.FINER ) )
				logger.exiting( ScriptEvalUtil.class.getName( ),
						"evalExpr",
						evalConditionalExpr( expression,
								ConditionalExpr.getOperator( ),
								Op1,
								Op2 ) );
			return evalConditionalExpr( expression, ConditionalExpr.getOperator( ), Op1, Op2 );
		}
		else
		{
			IScriptExpression jsExpr = (IScriptExpression) expr;
			if ( logger.isLoggable( Level.FINER ) )
				logger.exiting( ScriptEvalUtil.class.getName( ),
						"evalExpr",
						evaluateJSAsExpr( cx,
								scope,
								jsExpr.getText( ),
								source,
								lineNo ) );
			return evaluateJSAsExpr( cx, scope, jsExpr.getText(), source, lineNo );
		}
	}
	
	
	/**
	 * Converts an exception which occurred in the evaluation of a ROM script to a DataException,
	 * and rethrows such exception. This method never returns.
	 */
	public static void RethrowJSEvalException( RhinoException e, String scriptText, 
			String source, int lineNo ) 
		throws DataException
	{
		if ( source == null )
		{
			// Note that sourceName from RhinoException sometimes get truncated (need to find out why)
			// Better some than nothing
			source = e.sourceName();
			lineNo = e.lineNumber();
		}
		
		if ( logger.isLoggable( Level.FINE ) )
			logger.log( Level.FINE, 
					"Unexpected RhinoException. Source=" + source + ", line=" + lineNo+ ", Script=\n"
					+ scriptText + "\n",
					e );

        throw new DataException( ResourceConstants.JSSCRIPT_INVALID,
				new Object[]{
						scriptText, source, new Integer( lineNo ), e.getLocalizedMessage()
				} );
	}

	private static boolean like( Object obj1, Object pattern ) throws DataException
	{
		if (obj1 == null || pattern == null) 
		{
		    if( obj1 == null && pattern == null)
			    return true;
			else
			    return false;
        }
		
		boolean result = false;
		try
		{
			Pattern p = Pattern.compile( pattern.toString( ) );
			Matcher m = p.matcher( obj1.toString( ) );
			result = m.matches( );
		}
		catch ( RuntimeException e )
		{
			throw new DataException( ResourceConstants.MATCH_ERROR, e );
		}
		return result;
	}
	
	private static boolean between( Object resultObject, Object resultOp1,
			Object resultOp2 ) throws DataException
	{
		// For "Date between String and String
		// First we should convert all operands to Date
		if ( resultObject instanceof Date )
		{
			try
			{
				resultOp1 = DataTypeUtil.toDate( resultOp1 );
				resultOp2 = DataTypeUtil.toDate( resultOp2 );
			}
			catch ( BirtException e )
			{
				throw new DataException( ResourceConstants.DATATYPEUTIL_ERROR,
						e );
			}
		}
		
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
	
	private static IScriptExpression constructValidScriptExpression ( IScriptExpression ise)
	{
	    return ise!=null && ise.getText().trim().length() > 0 ? ise : new ScriptExpression("null");
	}
	
	private static Object evaluateJSScript(Context cx, Scriptable scope,
			String scriptText, String source, int lineNo)
			throws DataException 
	{
		Object result = null;
		try
		{
			Script compiledScript = ScriptUtil.getCompiledScript( scriptText,
					source,
					lineNo );
			result = compiledScript.exec( cx, scope );
			
			// the result might be a DataExceptionMocker.
			if ( result instanceof DataExceptionMocker )
			{
				throw ( (DataExceptionMocker) result ).getCause( );
			}
			// It seems Rhino 1.6 has changed its way to process incorrect expression.
			// When there is an error, exception will not be thrown, but rather an Undefined
			// instance will be returned. Here its return value is changed to null.
			if ( result instanceof Undefined )
			{
				//throw new Exception( scriptText + " is not valid expression." );
				return null;
		
			}
		}
		catch ( RhinoException e)
		{
			RethrowJSEvalException( e, scriptText, source, lineNo );
		}
		return convertNativeObjToJavaObj(result);
	}
	
	
}
