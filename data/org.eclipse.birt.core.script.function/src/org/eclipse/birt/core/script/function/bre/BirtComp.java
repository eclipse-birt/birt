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

package org.eclipse.birt.core.script.function.bre;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.MessageFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.text.Collator;

/**
 * This class implements comparison methods.
 */
public class BirtComp implements IScriptFunctionExecutor
{


	/**
	 * 
	 */
	private static final long serialVersionUID = 29055295217417372L;

	private static final String WRONG_ARGUMENT = "Wrong number of arguments for BirtComp function: {0}";

	private static final String ANY_OF = "anyOf";
	private static final String BETWEEN = "between";
	private static final String NOT_BETWEEN = "notBetween";
	private static final String EQUAL_TO = "equalTo";
	private static final String GREATER_THAN = "greaterThan";
	private static final String LESS_THAN = "lessThan";
	private static final String GREATER_OR_EQUAL = "greaterOrEqual";
	private static final String LESS_OR_EQUAL = "lessOrEqual";
	private static final String NOT_EQUAL = "notEqual";
	private static final String LIKE = "like";
	private static final String NOT_LIKE = "notLike";
	private static final String MATCH = "match";
	private static final String COMPARE_STRING = "compareString";
	private static final String PLUGIN_ID = "org.eclipse.birt.core";
	private static final String PACKAGE_ID = "org.eclipse.birt.core.script.function.bre";

	private IScriptFunctionExecutor executor;
	/**
	 * @throws BirtException 
	 * 
	 * 
	 */
	BirtComp( String functionName ) throws BirtException
	{
		if( ANY_OF.equals( functionName ))
			this.executor = new Function_AnyOf();
		else if( BETWEEN.equals( functionName ))
			this.executor = new Function_Between( true );
		else if( NOT_BETWEEN.equals( functionName ))
			this.executor = new Function_Between( false );
		else if( EQUAL_TO.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_EQUAL );
		else if( GREATER_THAN.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_GREATERTHAN );
		else if( LESS_THAN.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_LESSTHAN );
		else if( GREATER_OR_EQUAL.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_GREATEROREQUAL );
		else if( LESS_OR_EQUAL.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_LESSOREQUAL );
		else if( NOT_EQUAL.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_NOT_EQUAL );
		else if( LIKE.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_LIKE );
		else if( NOT_LIKE.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_NOT_LIKE );
		else if( MATCH.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_MATCH );
		else if( COMPARE_STRING.equals( functionName ))
			this.executor = new Function_Compare( Function_Compare.MODE_COMPARE_STRING );
		else
			throw new BirtException( PACKAGE_ID,
					null,
					Messages.getString( "invalid.function.name" )
							+ "BirtComp." + functionName );	}

	private static Collator myCollator = Collator.getInstance( );

	/**
	 * @param obj1
	 * @param obj2
	 * @return -1,0 and 1 standing for <,= and > respectively
	 * @throws BirtException
	 * @throws DataException
	 */
	private static int compare( Object obj1, Object obj2 ) throws BirtException
	{
		if ( obj1 == null || obj2 == null )
		{
			// all non-null values are greater than null value
			if ( obj1 == null && obj2 != null )
				return -1;
			else if ( obj1 != null && obj2 == null )
				return 1;
			else
				return 0;
		}

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
				if ( obj1 instanceof String )
				{
					return myCollator.compare( obj1, obj2 );
				}
				else
				{
					return ( (Comparable) obj1 ).compareTo( obj2 );
				}
			}
			// most judgements should end here
			else
			{
				return myCollator.compare( obj1.toString( ), obj2.toString( ) );
			}
		}
		else if ( obj1 instanceof BigDecimal || obj2 instanceof BigDecimal )
		{
			BigDecimal a = DataTypeUtil.toBigDecimal( obj1 );
			BigDecimal b = DataTypeUtil.toBigDecimal( obj2 );
			return a.compareTo( b );
		}
		else if ( isNumericOrString( obj1 ) && isNumericOrString( obj2 ) )
		{
			return DataTypeUtil.toDouble( obj1 )
					.compareTo( DataTypeUtil.toDouble( obj2 ) );
		}
		else if ( isTimeOrString( obj1 ) && isTimeOrString( obj2) )
		{
			return DataTypeUtil.toSqlTime( obj1 )
					.compareTo( DataTypeUtil.toSqlTime( obj2 ) );
		}
		else if ( isSQLDateOrString( obj1 ) && isSQLDateOrString( obj2 ))
		{
			return DataTypeUtil.toSqlDate( obj1 )
					.compareTo( DataTypeUtil.toSqlDate( obj2 ) );
		}
		else if ( isDateOrString( obj1 ) && isDateOrString( obj2 ) )
		{
			return DataTypeUtil.toDate( obj1 )
					.compareTo( DataTypeUtil.toDate( obj2 ) );
		}
		else 
		{
			String object1 = null;
			String object2 = null;
			if (obj1 instanceof ScriptableObject)
				object1 = DataTypeUtil.toString(((ScriptableObject) obj1)
						.getDefaultValue(null));
			else
				object1 = DataTypeUtil.toString(obj1);

			if (obj2 instanceof ScriptableObject)
				object2 = DataTypeUtil.toString(((ScriptableObject) obj2)
						.getDefaultValue(null));
			else
				object2 = DataTypeUtil.toString(obj2);
			
			return compare( object1, object2 );
		}

	}


	/**
	 * Compare 2 object of String type by the given condition
	 * 
	 * @param obj1
	 * @param obj2
	 * @param ignoreCase
	 * @param trimed
	 * @return
	 * @throws BirtException
	 */
	private static int compareString( Object obj1, Object obj2,
			boolean ignoreCase, boolean trimed ) throws BirtException
	{
		if ( obj1 == null && obj2 == null )
		{
			return 0;
		}
		if ( obj1 == null )
		{
			return -1;
		}
		if ( obj2 == null )
		{
			return 1;
		}		
		if ( !( obj1 instanceof String ) || !( obj2 instanceof String ) )
		{
			throw new IllegalArgumentException( );
		}
		String str1 = DataTypeUtil.toString( obj1 );
		String str2 = DataTypeUtil.toString( obj2 );
		if ( ignoreCase )
		{
			if ( trimed )
			{
				return str1.trim( ).compareToIgnoreCase( str2.trim( ) );
			}
			return str1.compareToIgnoreCase( str2 );
		}
		else
		{
			if ( trimed )
			{
				return str1.trim( ).compareTo( str2.trim( ) );
			}
			return str1.compareTo( str2 );
		}
	}
	
	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isTimeOrString( Object result )
	{
		return ( result instanceof Time ) || ( result instanceof String );
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
	
	/**
	 * @param obj1
	 * @param obj2
	 * @return true x matches Javascript pattern y
	 * @throws BirtException
	 * @throws DataException
	 */
	private static boolean match( Object obj1, Object obj2 )
			throws BirtException
	{
		if ( obj2 == null )
		{
			throw new java.lang.IllegalArgumentException( Messages.getString( "error.BirtComp.match.invalid.pattern" ) );
		}
		if ( obj1 == null )
		{
			return false;
		}
		String sourceStr = obj1.toString( );
		String pattern = obj2.toString( );
		
		// Pattern can be one of the following:
		// (1)Java regular expression pattern
		// (2)JavaScript RegExp construction syntax: "/RegExpr/[flags]", where flags 
		// can be a combination of 'g', 'm', 'i'
		Matcher jsReExprMatcher = getJSReExprPatternMatcher( pattern ); 
		int flags = 0;
		if ( jsReExprMatcher.matches( ) )
		{
			// This is a Javascript syntax
			// Get the flags; we only expect "m", "i", "g"
			String flagStr = pattern.substring( jsReExprMatcher.start( 2 ),
					jsReExprMatcher.end( 2 ) );
			for ( int i = 0; i < flagStr.length( ); i++ )
			{
				switch ( flagStr.charAt( i ) )
				{
					case 'm' :
						flags |= Pattern.MULTILINE;
						break;
					case 'i' :
						flags |= Pattern.CASE_INSENSITIVE;
						break;
					case 'g' :
						break; // this flag has no effect

					default :
						throw new BirtException( PLUGIN_ID,
								ResourceConstants.INVALID_REGULAR_EXPRESSION,
								pattern );
				}
			}
			pattern = pattern.substring( jsReExprMatcher.start( 1 ),
					jsReExprMatcher.end( 1 ) );
		}

		try
		{
			Matcher m = Pattern.compile( pattern, flags ).matcher( sourceStr );
			return m.find( );
		}
		catch ( PatternSyntaxException e )
		{
			throw new BirtException( PLUGIN_ID,
					ResourceConstants.INVALID_REGULAR_EXPRESSION,
					e );
		}
	}

	/**
	 * @param obj1
	 * @param obj2
	 * @return true x matches SQL pattern y
	 * @throws BirtException
	 * @throws DataException
	 */
	private static boolean like( Object obj1, Object obj2 )
	{
		if ( obj2 == null )
		{
			throw new java.lang.IllegalArgumentException( Messages.getString( "error.BirtComp.like.invalid.pattern" ) );
		}
		if ( obj1 == null )
		{
			return false;
		}
		String str = obj1.toString( );
		String pattern = toPatternString( obj2.toString( ) );
		return str.matches( pattern );
	}
	
	/**
	 * Transfers the user-input string to the Pattern regular expression
	 * 
	 * @param regex
	 * @return
	 */
	private static String toPatternString( String regex )
	{
		String pattern = "";
		boolean preserveFlag = false;
		for( int i = 0; i < regex.length( ); i++ )
		{
			char c = regex.charAt( i );
			if ( c == '\\' )
			{
				pattern = handlePreservedString( preserveFlag, pattern );
				preserveFlag = false;
				pattern += c;
				i++;
				if ( i < regex.length( ) )
				{
					pattern += regex.charAt( i );
				}
			}
			else if ( c == '%' )
			{
				pattern = handlePreservedString( preserveFlag, pattern );
				preserveFlag = false;
				pattern += ".*";
			}
			else if ( c == '_' )
			{
				pattern = handlePreservedString( preserveFlag, pattern );
				preserveFlag = false;
				pattern += ".";
			}
			else
			{
				if( preserveFlag )
				{
					pattern += c;
				}
				else
				{
					pattern = pattern + "\\Q" + c;
					preserveFlag = true;
				}
			}
		}
		if( preserveFlag )
		{
			pattern += "\\E";
		}
		return pattern;		
	}
	
	private static String handlePreservedString( boolean preserveFlag, String pattern )
	{
		if( preserveFlag )
		{
			pattern += "\\E";
		}
		return pattern;
	}

	private class Function_AnyOf implements IScriptFunctionExecutor
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Object execute( Object[] args, IScriptFunctionContext context ) throws BirtException
		{
			if ( args == null || args.length < 2 )
				throw new IllegalArgumentException( MessageFormat.format( WRONG_ARGUMENT,
						new Object[]{
							ANY_OF
						} ) );

			for ( int i = 1; i < args.length; i++ )
			{
				try
				{
					if ( compare( args[0], args[i] ) == 0 )
						return Boolean.TRUE;
				}
				catch ( Exception e )
				{
					// If two values cannot be compare, simply do nothing.
				}
			}
			
			if( args.length == 2 && args[1] instanceof Object[])
			{
				Object[] objs = (Object[])args[1];
				for ( int i = 0; i < objs.length; i++ )
				{
					try
					{
						if ( compare( args[0], objs[i] ) == 0 )
							return Boolean.TRUE;
					}
					catch ( Exception e )
					{
						// If two values cannot be compare, simply do nothing.
					}
				}
			}
			return Boolean.FALSE;
		}
	}

	private class Function_Between implements IScriptFunctionExecutor
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private boolean mode;

		/**
		 * @param mode:
		 *            if true, use Between mode, else use NotBetween mode.
		 */
		Function_Between( boolean mode )
		{
			this.mode = mode;
		}


		public Object execute( Object[] args, IScriptFunctionContext context ) throws BirtException
		{
			if ( args == null || args.length != 3 )
				throw new IllegalArgumentException( MessageFormat.format( WRONG_ARGUMENT,
						new Object[]{
							BETWEEN
						} ) );
			
			try
			{
				Object min, max;
				if ( compare( args[1], args[2] ) <= 0 )
				{
					min = args[1];
					max = args[2];
				}
				else
				{
					min = args[2];
					max = args[1];
				}
				return this.mode
						? new Boolean( compare( args[0], min ) >= 0
								&& compare( args[0], max ) <= 0 )
						: new Boolean( !( compare( args[0], min ) >= 0 && compare( args[0],
								max ) <= 0 ) );
			}
			catch ( BirtException e )
			{
				throw new IllegalArgumentException( e.getLocalizedMessage( ) );
			}
		}
	}

	private class Function_Compare implements IScriptFunctionExecutor
	{

		/**
		 * 
		 */
		public static final int MODE_EQUAL = 0;
		public static final int MODE_NOT_EQUAL = 1;
		public static final int MODE_GREATERTHAN = 2;
		public static final int MODE_LESSTHAN = 3;
		public static final int MODE_GREATEROREQUAL = 4;
		public static final int MODE_LESSOREQUAL = 5;
		public static final int MODE_LIKE = 6;
		public static final int MODE_MATCH = 7;
		public static final int MODE_NOT_LIKE = 8;
		public static final int MODE_COMPARE_STRING = 9;

		private static final long serialVersionUID = 1L;
		private int mode;

		Function_Compare( int mode )
		{
			this.mode = mode;
		}

		/**
		 * 
		 */
		private void throwException( )
		{
			String func = null;
			switch ( this.mode )
			{
				case MODE_EQUAL :
					func = EQUAL_TO;
					break;
				case MODE_NOT_EQUAL :
					func = NOT_EQUAL;
					break;
				case MODE_GREATERTHAN :
					func = GREATER_THAN;
					break;
				case MODE_LESSTHAN :
					func = LESS_THAN;
					break;
				case MODE_GREATEROREQUAL :
					func = GREATER_OR_EQUAL;
					break;
				case MODE_LESSOREQUAL :
					func = LESS_OR_EQUAL;
					break;
				case MODE_LIKE :
					func = LIKE;
					break;
				case MODE_MATCH :
					func = MATCH;
					break;
				case MODE_NOT_LIKE :
					func = NOT_LIKE;
					break;
				case MODE_COMPARE_STRING :
					func = COMPARE_STRING;
				default :
					func = "Unknown";
					break;
			}
			throw new IllegalArgumentException( MessageFormat.format( WRONG_ARGUMENT,
					new Object[]{
						func
					} ) );
		}

		public Object execute( Object[] args, IScriptFunctionContext context  ) throws BirtException
		{
			try
			{
				if ( this.mode == MODE_COMPARE_STRING )
				{
					if ( args.length == 3 )
					{
						if ( !( args[2] instanceof Boolean ) )
						{
							throwException( );
						}
						return compareString( args[0],
								args[1],
								(Boolean) args[2],
								false ) == 0;
					}
					if ( args.length == 4 )
					{
						if ( !( args[2] instanceof Boolean )
								|| !( args[3] instanceof Boolean ) )
						{
							throwException( );
						}
						return compareString( args[0],
								args[1],
								(Boolean) args[2],
								(Boolean) args[3] ) == 0;
					}
					else
					{
						return compareString( args[0], args[1], false, false ) == 0;
					}
				}
				if ( args == null || args.length != 2 )
				{
					throwException( );
				}

				switch ( this.mode )
				{
					case MODE_EQUAL :
						return new Boolean( compare( args[0], args[1] ) == 0 );
					case MODE_NOT_EQUAL :
						return new Boolean( compare( args[0], args[1] ) != 0 );
					case MODE_GREATERTHAN :
						return new Boolean( compare( args[0], args[1] ) > 0 );
					case MODE_LESSTHAN :
						return new Boolean( compare( args[0], args[1] ) < 0 );
					case MODE_GREATEROREQUAL :
						return new Boolean( compare( args[0], args[1] ) >= 0 );
					case MODE_LESSOREQUAL :
						return new Boolean( compare( args[0], args[1] ) <= 0 );
					case MODE_LIKE :
						return new Boolean( like( args[0], args[1] ) );
					case MODE_MATCH :
						return new Boolean( match( args[0], args[1] ) );
					case MODE_NOT_LIKE :
						return new Boolean( !like( args[0], args[1] ) );
					default :
						return null;
				}

			}
			catch ( BirtException e )
			{
				throw new IllegalArgumentException( e.getLocalizedMessage( ) );
			}
		}
	}

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
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isSQLDateOrString( Object result )
	{
		return ( result instanceof java.sql.Date ) || ( result instanceof String );
	}
	
	public Object execute( Object[] arguments, IScriptFunctionContext context ) throws BirtException
	{
		return this.executor.execute( arguments, context );
	}

}
