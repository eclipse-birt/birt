/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script.function.bre;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

/**
 * String functions.
 */
class BirtStr implements IScriptFunctionExecutor {

	private static final long serialVersionUID = 1L;
	private IScriptFunctionExecutor executor;

	BirtStr(String functionName) throws BirtException {
		if ("left".equals(functionName)) {
			this.executor = new Function_Left();
		} else if ("right".equals(functionName)) {
			this.executor = new Function_Right();
		} else if ("concat".equals(functionName)) {
			this.executor = new Function_Concat();
		} else if ("toUpper".equals(functionName)) {
			this.executor = new Function_ToUpper();
		} else if ("toLower".equals(functionName)) {
			this.executor = new Function_ToLower();
		} else if ("trim".equals(functionName)) {
			this.executor = new Function_Trim();
		} else if ("trimLeft".equals(functionName)) {
			this.executor = new Function_TrimLeft();
		} else if ("trimRight".equals(functionName)) {
			this.executor = new Function_TrimRight();
		} else if ("indexOf".equals(functionName)) {
			this.executor = new Function_IndexOf();
		} else if ("search".equals(functionName)) {
			this.executor = new Function_Search();
		} else if ("charLength".equals(functionName)) {
			this.executor = new Function_CharLength();
		} else {
			throw new BirtException("org.eclipse.birt.core.script.function.bre", null,
					Messages.getString("invalid.function.name") + "BirtStr." + functionName);
		}
	}

	private static class Function_Left implements IScriptFunctionExecutor {
		private static final long serialVersionUID = 1L;
		private static final int maxArgumentNum = 2;

		/**
		 * Returns the first n characters of the string str. If n is 0, an empty string
		 * is returned. If n is greater than the length of str, the entire string is
		 * returned.
		 *
		 * @param str
		 * @param n
		 * @return
		 */
		private String left(String str, int n) {
			if (n < 0) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.BirtStr.left.invalidArgument", new Object[] { n }));
			}
			if (str == null) {
				return null;
			}
			if (n == 0) {
				return "";
			}
			if (n >= str.length()) {
				return str;
			} else {
				return str.substring(0, n);
			}
		}

		/**
		 * Returns the first characters of the string str.
		 *
		 * @param str
		 * @return
		 */
		private String left(String str) {
			return left(str, 1);
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}

			if (args.length > maxArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.variableArgument",
								new Object[] { maxArgumentNum, args.length }));
			}

			if (args.length == 1) {
				return left(toJavaString(args[0]));
			} else {
				return left(toJavaString(args[0]), ((Number) args[1]).intValue());
			}
		}
	}

	private static class Function_Right implements IScriptFunctionExecutor {
		private static final long serialVersionUID = 1L;
		private static final int maxArgumentNum = 2;

		/**
		 * Returns the last n characters of the string str. If n is 0, an empty string
		 * is returned. If n is greater than the length of str, the entire string is
		 * returned.
		 *
		 * @param str
		 * @param n
		 * @return
		 */
		public String right(String str, int n) {
			if (n < 0) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.BirtStr.right.invalidArgument", new Object[] { n }));
			}
			if (str == null) {
				return null;
			}
			if (n == 0) {
				return "";
			}
			if (n >= str.length()) {
				return str;
			} else {
				return str.substring(str.length() - n);
			}
		}

		/**
		 * Returns the last characters of the string str.
		 *
		 * @param str
		 * @return
		 */
		public String right(String str) {
			return right(str, 1);
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}

			if (args.length > maxArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.variableArgument",
								new Object[] { maxArgumentNum, args.length }));
			}

			if (args.length == 1) {
				return right(toJavaString(args[0]));
			} else {
				return right(toJavaString(args[0]), ((Number) args[1]).intValue());
			}
		}
	}

	private static class Function_Concat implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}

			StringBuilder buf = new StringBuilder();

			for (int i = 0; i < args.length; i++) {
				buf.append(args[i]);
			}
			return buf.toString();
		}
	}

	private static class Function_ToUpper implements IScriptFunctionExecutor {
		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			if (args[0] instanceof Object[]) {
				Object[] objArray = (Object[]) args[0];
				String[] strArray = new String[objArray.length];
				for (int i = 0; i < objArray.length; i++) {
					String value = toJavaString(objArray[i]);
					if (value != null) {
						strArray[i] = value.toUpperCase();
					}
				}
				return strArray;
			} else {
				if (args[0] instanceof Integer) {
					return args[0];
				}

				String value = toJavaString(args[0]);
				if (value != null) {
					return value.toUpperCase();
				}
				return null;
			}
		}
	}

	private static class Function_ToLower implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			if (args[0] instanceof Object[]) {
				Object[] objArray = (Object[]) args[0];
				String[] strArray = new String[objArray.length];
				for (int i = 0; i < objArray.length; i++) {
					String value = toJavaString(objArray[i]);
					if (value != null) {
						strArray[i] = value.toLowerCase();
					}
				}
				return strArray;
			} else {
				String value = toJavaString(args[0]);
				if (value != null) {
					return value.toLowerCase();
				}
				return null;
			}
		}
	}

	private static class Function_Trim implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		/**
		 * Removes all leading and trailing blank characters (space, TAB etc.). Also,
		 * all consecutive blank characters are consolidated into one.
		 *
		 * @param str
		 * @return
		 */
		private String trim(String str) {
			if (str == null) {
				return null;
			} else {
				String trimStr = str.trim();
				return trimStr.replaceAll("\\s+", " ");
			}
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			return trim(toJavaString(args[0]));
		}
	}

	private static class Function_TrimLeft implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		/**
		 * Removes all leading blanks. Trailing blanks and blanks between words are not
		 * removed.
		 *
		 * @param str
		 * @return
		 */
		private String trimLeft(String str) {
			if (str == null) {
				return null;
			} else {
				byte[] value = str.getBytes();
				int st = 0;
				while ((st < str.length()) && (value[st] <= ' ')) {
					st++;
				}
				return (st > 0) ? str.substring(st) : str;
			}
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			return trimLeft(toJavaString(args[0]));
		}
	}

	private static class Function_TrimRight implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		/**
		 * Removes all trailing blanks. Leading blanks and blanks between words are not
		 * removed.
		 *
		 * @param str
		 * @return
		 */
		private String trimRight(String str) {
			if (str == null) {
				return null;
			} else {
				byte[] value = str.getBytes();
				int end = str.length();
				while ((end > 0) && (value[end - 1] <= ' ')) {
					end--;
				}
				return (end < str.length()) ? str.substring(0, end) : str;
			}
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			return trimRight(toJavaString(args[0]));
		}

	}

	private static class Function_IndexOf implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		private static final int minArgumentNum = 2;

		private static final int maxArgumentNum = 3;

		/**
		 * Searches for find_text in str and returns the index of first occurrence of
		 * pattern. Search starts at position start. All index values are 0-based.If
		 * start is omitted, a value of 0 is assumed. String search is case sensitive.
		 *
		 * @param find_text
		 * @param str
		 * @param start
		 * @return
		 */
		private int indexOf(String find_text, String str, int start) {
			if (start < 0) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.BirtStr.indexOf.invalidArgument", new Object[] { start }));
			}
			if (find_text == null || str == null || str.indexOf(find_text) < 0) {
				return -1;
			} else {
				return str.indexOf(find_text, start);
			}
		}

		/**
		 * Searches for find_text in str and returns the index of first occurrence of
		 * pattern. Search starts at position 0. All index values are 0-based. If no
		 * matched string found, return -1 String search is case sensitive.
		 *
		 * @param find_text
		 * @param str
		 * @return
		 */
		private int indexOf(String find_text, String str) {
			return indexOf(find_text, str, 0);
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length > 3 || args.length < 2) {
				throw new IllegalArgumentException(Messages.getFormattedString("error.argument.number.outofValidRange",
						new Object[] { minArgumentNum, maxArgumentNum, args.length }));
			}

			if (args.length == 3) {
				return new Integer(
						indexOf(toJavaString(args[0]), toJavaString(args[1]), ((Number) args[2]).intValue()));
			} else {
				return Integer.valueOf(indexOf(toJavaString(args[0]), toJavaString(args[1])));
			}
		}
	}

	private static class Function_Search implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		private static final int minArgumentNum = 2;

		private static final int maxArgumentNum = 3;

		/**
		 * Similar to indexOf function, except that: (1) string comparison is
		 * case-insensitive (2) pattern string can contain wildcard characters: *
		 * matches any sequence of characters (including empty); ? matches any single
		 * character.
		 *
		 * @param pattern
		 * @param str
		 * @param start
		 * @return
		 */
		private int search(String pattern, String str, int start) {
			if (start < 0) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.BirtStr.indexOf.invalidArgument", new Object[] { start }));
			}
			if (pattern == null || str == null) {
				return -1;
			} else {
				String subStr = str.substring(start);

				Pattern p = Pattern.compile(toPatternString(pattern), Pattern.CASE_INSENSITIVE);
				Matcher matcher = p.matcher(subStr);
				if (matcher.find()) {
					return matcher.start() + start;
				}

				return -1;
			}
		}

		/**
		 * Transfers the user-input string to the Pattern regular expression
		 *
		 * @param regex
		 * @return
		 */
		private String toPatternString(String regex) {
			String pattern = "";
			boolean preserveFlag = false;
			for (int i = 0; i < regex.length(); i++) {
				char c = regex.charAt(i);
				if (c == '\\') {
					pattern = handlePreservedString(preserveFlag, pattern);
					preserveFlag = false;
					pattern += c;
					i++;
					if (i < regex.length()) {
						pattern += regex.charAt(i);
					}
				} else if (c == '*') {
					pattern = handlePreservedString(preserveFlag, pattern);
					preserveFlag = false;
					pattern += ".*";
				} else if (c == '?') {
					pattern = handlePreservedString(preserveFlag, pattern);
					preserveFlag = false;
					pattern += ".";
				} else if (preserveFlag) {
					pattern += c;
				} else {
					pattern = pattern + "\\Q" + c;
					preserveFlag = true;
				}
			}
			if (preserveFlag) {
				pattern += "\\E";
			}
			return pattern;
		}

		private String handlePreservedString(boolean preserveFlag, String pattern) {
			if (preserveFlag) {
				pattern += "\\E";
			}
			return pattern;
		}

		/**
		 * Similar to indexOf function, except that: (1) string comparison is
		 * case-insensitive (2) pattern string can contain wildcard characters: *
		 * matches any sequence of characters (including empty); ? matches any single
		 * character.
		 *
		 * @param pattern
		 * @param str
		 * @return
		 */
		private int search(String pattern, String str) {
			return search(pattern, str, 0);
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length > 3 || args.length < 2) {
				throw new IllegalArgumentException(Messages.getFormattedString("error.argument.number.outofValidRange",
						new Object[] { minArgumentNum, maxArgumentNum, args.length }));
			}

			if (args.length == 3) {
				return new Integer(search(toJavaString(args[0]), toJavaString(args[1]), ((Number) args[2]).intValue()));
			} else {
				return Integer.valueOf(search(toJavaString(args[0]), toJavaString(args[1])));
			}
		}
	}

	private static class Function_CharLength implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;
		private static final int fixedArgumentNum = 1;

		/**
		 * Returns the number of characters in string.
		 *
		 * @param str
		 * @return
		 */
		private int charLength(String str) {
			if (str == null) {
				return 0;
			} else {
				return str.length();
			}
		}

		@Override
		public Object execute(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (args == null) {
				throw new IllegalArgumentException(Messages.getString("error.arguement.cannot.empty"));
			}
			if (args.length != fixedArgumentNum) {
				throw new IllegalArgumentException(
						Messages.getFormattedString("error.incorrect.number.function.fixedArgument",
								new Object[] { fixedArgumentNum, args.length }));
			}

			return Integer.valueOf(charLength(toJavaString(args[0])));
		}
	}

	@Override
	public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
		return this.executor.execute(arguments, context);
	}

	/**
	 * convert any javascript object to java string.
	 *
	 * @param arg
	 * @return
	 */
	private static String toJavaString(Object arg) {
		if (arg == null) {
			return null;
		}
		if (arg instanceof String) {
			return (String) arg;
		}
		return arg.toString();
	}
}
