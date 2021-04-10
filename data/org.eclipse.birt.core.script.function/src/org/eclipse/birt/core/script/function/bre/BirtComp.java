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
public class BirtComp implements IScriptFunctionExecutor {

	private static final long serialVersionUID = 1L;

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
	BirtComp(String functionName) throws BirtException {
		if (ANY_OF.equals(functionName))
			this.executor = new Function_AnyOf();
		else if (BETWEEN.equals(functionName))
			this.executor = new Function_Between(true);
		else if (NOT_BETWEEN.equals(functionName))
			this.executor = new Function_Between(false);
		else if (EQUAL_TO.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_EQUAL);
		else if (GREATER_THAN.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_GREATERTHAN);
		else if (LESS_THAN.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_LESSTHAN);
		else if (GREATER_OR_EQUAL.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_GREATEROREQUAL);
		else if (LESS_OR_EQUAL.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_LESSOREQUAL);
		else if (NOT_EQUAL.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_NOT_EQUAL);
		else if (LIKE.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_LIKE);
		else if (NOT_LIKE.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_NOT_LIKE);
		else if (MATCH.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_MATCH);
		else if (COMPARE_STRING.equals(functionName))
			this.executor = new Function_Compare(Function_Compare.MODE_COMPARE_STRING);
		else
			throw new BirtException(PACKAGE_ID, null,
					Messages.getString("invalid.function.name") + "BirtComp." + functionName);
	}

	private static Collator myCollator = Collator.getInstance();

	/**
	 * @param obj1
	 * @param obj2
	 * @return -1,0 and 1 standing for <,= and > respectively
	 * @throws BirtException
	 * @throws DataException
	 */
	private static int compare(Object obj1, Object obj2, Collator collator) throws BirtException {
		if (obj1 == null || obj2 == null) {
			// all non-null values are greater than null value
			if (obj1 == null && obj2 != null)
				return -1;
			else if (obj1 != null && obj2 == null)
				return 1;
			else
				return 0;
		}

		if (isSameType(obj1, obj2)) {
			if (obj1 instanceof Boolean) {
				if (obj1.equals(obj2))
					return 0;

				Boolean bool = (Boolean) obj1;
				if (bool.equals(Boolean.TRUE))
					return 1;
				else
					return -1;
			} else if (obj1 instanceof Comparable) {
				if (obj1 instanceof String) {
					if (collator == null)
						return ((String) obj1).compareTo((String) obj2);
					return compareAsString(obj1, obj2, collator);
				} else {
					return ((Comparable) obj1).compareTo(obj2);
				}
			}
			// most judgements should end here
			else {
				return compareAsString(obj1, obj2, collator);
			}
		} else if (obj1 instanceof BigDecimal || obj2 instanceof BigDecimal) {
			BigDecimal a = DataTypeUtil.toBigDecimal(obj1);
			BigDecimal b = DataTypeUtil.toBigDecimal(obj2);
			return a.compareTo(b);
		} else if (isNumericOrString(obj1) && isNumericOrString(obj2)) {
			return DataTypeUtil.toDouble(obj1).compareTo(DataTypeUtil.toDouble(obj2));
		} else if (isTimeOrString(obj1) && isTimeOrString(obj2)) {
			return DataTypeUtil.toSqlTime(obj1).compareTo(DataTypeUtil.toSqlTime(obj2));
		} else if (isSQLDateOrString(obj1) && isSQLDateOrString(obj2)) {
			return DataTypeUtil.toSqlDate(obj1).compareTo(DataTypeUtil.toSqlDate(obj2));
		} else if (isDateOrString(obj1) && isDateOrString(obj2)) {
			return DataTypeUtil.toDate(obj1).compareTo(DataTypeUtil.toDate(obj2));
		} else {
			String object1 = null;
			String object2 = null;
			if (obj1 instanceof ScriptableObject)
				object1 = DataTypeUtil.toString(((ScriptableObject) obj1).getDefaultValue(null));
			else
				object1 = DataTypeUtil.toString(obj1);

			if (obj2 instanceof ScriptableObject)
				object2 = DataTypeUtil.toString(((ScriptableObject) obj2).getDefaultValue(null));
			else
				object2 = DataTypeUtil.toString(obj2);

			return compare(object1, object2, collator);
		}

	}

	private static int compareAsString(Object obj1, Object obj2, Collator comp) throws BirtException {
		return comp == null ? DataTypeUtil.toString(obj1).compareTo(DataTypeUtil.toString(obj2))
				: comp.compare(DataTypeUtil.toString(obj1), DataTypeUtil.toString(obj2));
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
	private static int compareString(Object obj1, Object obj2, boolean ignoreCase, boolean trimed)
			throws BirtException {
		if (obj1 == null && obj2 == null) {
			return 0;
		}
		if (obj1 == null) {
			return -1;
		}
		if (obj2 == null) {
			return 1;
		}
		if (!(obj1 instanceof String) || !(obj2 instanceof String)) {
			throw new IllegalArgumentException();
		}
		String str1 = DataTypeUtil.toString(obj1);
		String str2 = DataTypeUtil.toString(obj2);
		if (ignoreCase) {
			if (trimed) {
				return str1.trim().compareToIgnoreCase(str2.trim());
			}
			return str1.compareToIgnoreCase(str2);
		} else {
			if (trimed) {
				return str1.trim().compareTo(str2.trim());
			}
			return str1.compareTo(str2);
		}
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isTimeOrString(Object result) {
		return (result instanceof Time) || (result instanceof String);
	}

	// Pattern to determine if a Match operation uses Javascript regexp syntax
	private static Pattern s_JSReExprPattern;

	// Gets a matcher to determine if a match pattern string is of JavaScript syntax
	// The pattern matches string like "/regexpr/gmi", which is used in JavaScript
	// to construct a RegExp object
	private static Matcher getJSReExprPatternMatcher(String patternStr) {
		if (s_JSReExprPattern == null)
			s_JSReExprPattern = Pattern.compile("^/(.*)/([a-zA-Z]*)$");
		return s_JSReExprPattern.matcher(patternStr);
	}

	/**
	 * @param obj1
	 * @param obj2
	 * @return true x matches Javascript pattern y
	 * @throws BirtException
	 * @throws DataException
	 */
	private static boolean match(Object obj1, Object obj2) throws BirtException {
		if (obj2 == null) {
			return false;
		}
		if (obj1 == null) {
			return false;
		}
		String sourceStr = obj1.toString();
		String pattern = obj2.toString();

		// Pattern can be one of the following:
		// (1)Java regular expression pattern
		// (2)JavaScript RegExp construction syntax: "/RegExpr/[flags]", where flags
		// can be a combination of 'g', 'm', 'i'
		Matcher jsReExprMatcher = getJSReExprPatternMatcher(pattern);
		int flags = 0;
		if (jsReExprMatcher.matches()) {
			// This is a Javascript syntax
			// Get the flags; we only expect "m", "i", "g"
			String flagStr = pattern.substring(jsReExprMatcher.start(2), jsReExprMatcher.end(2));
			for (int i = 0; i < flagStr.length(); i++) {
				switch (flagStr.charAt(i)) {
				case 'm':
					flags |= Pattern.MULTILINE;
					break;
				case 'i':
					flags |= Pattern.CASE_INSENSITIVE;
					break;
				case 'g':
					break; // this flag has no effect

				default:
					throw new BirtException(PLUGIN_ID, ResourceConstants.INVALID_REGULAR_EXPRESSION, pattern);
				}
			}
			pattern = pattern.substring(jsReExprMatcher.start(1), jsReExprMatcher.end(1));
		}

		try {
			Matcher m = Pattern.compile(pattern, flags).matcher(sourceStr);
			return m.find();
		} catch (PatternSyntaxException e) {
			throw new BirtException(PLUGIN_ID, ResourceConstants.INVALID_REGULAR_EXPRESSION, e);
		}
	}

	/**
	 * @param obj1
	 * @param obj2
	 * @return true x matches SQL pattern y
	 * @throws BirtException
	 * @throws DataException
	 */
	private static boolean like(Object source, Object pattern, boolean ignorecase) throws BirtException {
		String sourceStr = null;
		sourceStr = (source == null) ? "" : DataTypeUtil.toLocaleNeutralString(source);
		String patternStr;
		patternStr = (pattern == null) ? "" : DataTypeUtil.toLocaleNeutralString(pattern);

		// As per Bugzilla 115940, LIKE operator's pattern syntax is SQL-like: it
		// recognizes '_' and '%'. Backslash '\' escapes the next character.

		// Construct a Java RegExp pattern based on input. We need to translate
		// unescaped '%' to '.*', and '_' to '.'
		// Also need to escape any RegExp metacharacter in the source pattern.

		final String reservedChars = "([{^$|)?*+.";
		int patternLen = patternStr.length();
		StringBuffer buffer = new StringBuffer(patternLen * 2);

		for (int i = 0; i < patternLen; i++) {
			char c = patternStr.charAt(i);
			if (c == '\\') {
				// Escape char; copy next character to new pattern if
				// it is '\', '%' or '_'
				++i;
				if (i < patternLen) {
					c = patternStr.charAt(i);
					if (c == '%' || c == '_')
						buffer.append(c);
					else if (c == '\\')
						buffer.append("\\\\"); // Need to escape \
				} else {
					buffer.append("\\\\"); // Leave last \ and escape it
				}
			} else if (c == '%') {
				buffer.append(".*");
			} else if (c == '_') {
				buffer.append(".");
			} else {
				// Copy this char to target, escape if it is a metacharacter
				if (reservedChars.indexOf(c) >= 0) {
					buffer.append('\\');
				}
				buffer.append(c);
			}
		}

		try {
			String newPatternStr = buffer.toString();
			Pattern p = null;
			// Support search in multiple lines
			if (!ignorecase) {
				p = Pattern.compile(newPatternStr, Pattern.DOTALL);
			} else {
				p = Pattern.compile(newPatternStr, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
			}
			Matcher m = p.matcher(sourceStr.toString());
			return m.matches();
		} catch (PatternSyntaxException e) {
			throw new BirtException(e.getMessage());
		}
	}

	private static class Function_AnyOf implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			Collator collator = (Collator) context.findProperty("compare_locale");
			if (args == null || args.length < 2)
				throw new IllegalArgumentException(MessageFormat.format(WRONG_ARGUMENT, new Object[] { ANY_OF }));

			for (int i = 1; i < args.length; i++) {
				try {
					if (compare(args[0], args[i], collator) == 0)
						return Boolean.TRUE;
				} catch (Exception e) {
					// If two values cannot be compare, simply do nothing.
				}
			}

			if (args.length == 2 && args[1] instanceof Object[]) {
				Object[] objs = (Object[]) args[1];
				for (int i = 0; i < objs.length; i++) {
					try {
						if (compare(args[0], objs[i], collator) == 0)
							return Boolean.TRUE;
					} catch (Exception e) {
						// If two values cannot be compare, simply do nothing.
					}
				}
			}
			return Boolean.FALSE;
		}
	}

	private static class Function_Between implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private boolean mode;

		/**
		 * @param mode: if true, use Between mode, else use NotBetween mode.
		 */
		Function_Between(boolean mode) {
			this.mode = mode;
		}

		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			Collator collator = (Collator) context.findProperty("compare_locale");
			if (args == null || args.length != 3)
				throw new IllegalArgumentException(MessageFormat.format(WRONG_ARGUMENT, new Object[] { BETWEEN }));

			try {
				return this.mode
						? Boolean.valueOf(
								compare(args[0], args[1], collator) >= 0 && compare(args[0], args[2], collator) <= 0)
						: Boolean.valueOf(!(compare(args[0], args[1], collator) >= 0
								&& compare(args[0], args[2], collator) <= 0));
			} catch (BirtException e) {
				throw new IllegalArgumentException(e.getLocalizedMessage());
			}
		}
	}

	private static class Function_Compare implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
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

		private int mode;

		Function_Compare(int mode) {
			this.mode = mode;
		}

		/**
		 * 
		 */
		private void throwException() {
			String func = null;
			switch (this.mode) {
			case MODE_EQUAL:
				func = EQUAL_TO;
				break;
			case MODE_NOT_EQUAL:
				func = NOT_EQUAL;
				break;
			case MODE_GREATERTHAN:
				func = GREATER_THAN;
				break;
			case MODE_LESSTHAN:
				func = LESS_THAN;
				break;
			case MODE_GREATEROREQUAL:
				func = GREATER_OR_EQUAL;
				break;
			case MODE_LESSOREQUAL:
				func = LESS_OR_EQUAL;
				break;
			case MODE_LIKE:
				func = LIKE;
				break;
			case MODE_MATCH:
				func = MATCH;
				break;
			case MODE_NOT_LIKE:
				func = NOT_LIKE;
				break;
			case MODE_COMPARE_STRING:
				func = COMPARE_STRING;
				break;
			default:
				func = "Unknown";
				break;
			}
			throw new IllegalArgumentException(MessageFormat.format(WRONG_ARGUMENT, new Object[] { func }));
		}

		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			Collator collator = (Collator) context.findProperty("compare_locale");
			try {
				if (this.mode == MODE_COMPARE_STRING) {
					if (args.length == 3) {
						if (!(args[2] instanceof Boolean)) {
							throwException();
						}
						return compareString(args[0], args[1], (Boolean) args[2], false) == 0;
					}
					if (args.length == 4) {
						if (!(args[2] instanceof Boolean) || !(args[3] instanceof Boolean)) {
							throwException();
						}
						return compareString(args[0], args[1], (Boolean) args[2], (Boolean) args[3]) == 0;
					} else {
						return compareString(args[0], args[1], false, false) == 0;
					}
				}
				if (args == null || (args.length != 2 && this.mode != MODE_LIKE)) {
					throwException();
				}

				switch (this.mode) {
				case MODE_EQUAL:
					return new Boolean(compare(args[0], args[1], collator) == 0);
				case MODE_NOT_EQUAL:
					try {
						return new Boolean(compare(args[0], args[1], collator) != 0);
					} catch (BirtException e) {
						if (e.getErrorCode().equals(ResourceConstants.CONVERT_FAILS))
							return Boolean.TRUE;
						else
							return Boolean.FALSE;
					}
				case MODE_GREATERTHAN:
					return new Boolean(compare(args[0], args[1], collator) > 0);
				case MODE_LESSTHAN:
					return new Boolean(compare(args[0], args[1], collator) < 0);
				case MODE_GREATEROREQUAL:
					return new Boolean(compare(args[0], args[1], collator) >= 0);
				case MODE_LESSOREQUAL:
					return new Boolean(compare(args[0], args[1], collator) <= 0);
				case MODE_LIKE:
					if (args.length == 2) {
						return new Boolean(like(args[0], args[1], false));
					} else {
						return new Boolean(like(args[0], args[1], DataTypeUtil.toBoolean(args[2])));
					}
				case MODE_MATCH:
					return new Boolean(match(args[0], args[1]));
				case MODE_NOT_LIKE:
					return Boolean.valueOf(!like(args[0], args[1], false));
				default:
					return null;
				}

			} catch (BirtException e) {
				if (e.getErrorCode().equals(ResourceConstants.CONVERT_FAILS))
					return false;
				else
					throw new IllegalArgumentException(e.getLocalizedMessage());
			}
		}
	}

	/**
	 * 
	 * @param resultExpr
	 * @param resultOp1
	 * @return
	 */
	private static boolean isSameType(Object resultExpr, Object resultOp1) {
		return resultExpr.getClass().equals(resultOp1.getClass());
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isNumericOrString(Object result) {
		return (result instanceof Number) || (result instanceof String);
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isDateOrString(Object result) {
		return (result instanceof Date) || (result instanceof String);
	}

	/**
	 * 
	 * @param result
	 * @return
	 */
	private static boolean isSQLDateOrString(Object result) {
		return (result instanceof java.sql.Date) || (result instanceof String);
	}

	public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
		return this.executor.execute(arguments, context);
	}

}
