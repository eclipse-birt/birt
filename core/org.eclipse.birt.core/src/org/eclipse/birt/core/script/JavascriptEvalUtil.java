/**************************************************************************
 * Copyright (c) 2005, 2024 Actuate Corporation and others
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
 *
 **************************************************************************/
package org.eclipse.birt.core.script;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.ScriptRuntime;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Undefined;
import org.mozilla.javascript.Wrapper;

/**
 * Utilities to faciliate the evaluation of Javascript expressions. Handles
 * common evaluation tasks like exception handling, data type conversion and
 * script caching
 */
public class JavascriptEvalUtil {
	private static Logger logger = Logger.getLogger(JavascriptEvalUtil.class.getName());

	/** System property of the JavaScript version */
	private static final String ECMA_SCRIPT_SECURITY_PROPERTY_KEY = "birt.ecmascript.security"; //$NON-NLS-1$

	/** Valid keys of the system property */
	private static final String ECMA_SCRIPT_SECURITY_ENABLED = "on"; //$NON-NLS-1$

	/*
	 * LRU cache for compiled scripts. For performance reasons, scripts are compiled
	 * and put in a cache. Repeated evaluation of the same script will then used the
	 * compiled binary.
	 *
	 */
	static protected final int SCRIPT_CACHE_SIZE = 200;
	// access-ordered LRU cache
	static protected Map compiledScriptCache = Collections
			.synchronizedMap(new LinkedHashMap(SCRIPT_CACHE_SIZE, (float) 0.75, true) {
				/** */
				private static final long serialVersionUID = 5787175209573500620L;

				/*
				 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
				 */
				@Override
				protected boolean removeEldestEntry(Map.Entry eldest) {
					return size() > SCRIPT_CACHE_SIZE;
				}
			});

	/**
	 * This method will not convert the data of return value, so it might the Java
	 * data type or that of Java Script.
	 *
	 * @param cx
	 * @param scope
	 * @param scriptText
	 * @param source
	 * @param lineNo
	 * @return the evaluated value
	 * @throws BirtException
	 */
	public static Object evaluateRawScript(Context cx, Scriptable scope, String scriptText, String source, int lineNo)
			throws BirtException {
		Object result = null;

		// Use provided context, or get the thread context if none provided
		boolean enterContext = cx == null;
		if (enterContext) {
			cx = Context.enter();
		}

		try {
			Script compiledScript = getCompiledScript(cx, scope, scriptText, source, lineNo);
			result = compiledScript.exec(cx, scope);
		} catch (RhinoException e) {
			// Note: use the real source and lineNo here. The source and lineNo reported
			// by e can be wrong, since we may be executing an identical compiled script
			// from a different source/line
			throw wrapRhinoException(e, scriptText, source, lineNo);
		} finally {
			if (enterContext) {
				Context.exit();
			}
		}

		return result;
	}

	/**
	 * Evaluates Javascript expression and return its result, doing the necessary
	 * Javascript -> Java data type conversion if necessary
	 *
	 * @param cx         Javascript context. If null, current thread's context is
	 *                   used
	 * @param scope      Javascript scope to evaluate script in
	 * @param scriptText text of Javascript expression
	 * @param source     descriptive text of script source (for error reporting)
	 * @param lineNo     line number of script in it source
	 * @return Evaluation result.
	 * @throws BirtException If evaluation failed
	 */
	public static Object evaluateScript(Context cx, Scriptable scope, String scriptText, String source, int lineNo)
			throws BirtException {
		return convertJavascriptValue(evaluateRawScript(cx, scope, scriptText, source, lineNo));
	}

	/**
	 * Gets a compiled script, using and updating the script cache if necessary
	 */
	protected static Script getCompiledScript(Context cx, Scriptable scope, String scriptText, String source,
			int lineNo) {
		assert scriptText != null;

		Script compiledScript = (Script) compiledScriptCache.get(scriptText);
		if (compiledScript == null) {
			compiledScript = cx.compileString(scriptText, source, lineNo, getSecurityDomain(source));
			compiledScriptCache.put(scriptText, compiledScript);
		}

		return compiledScript;
	}

	/**
	 * Creates Javascript native wrapper for Java objects, if necessary. This method
	 * currently only wraps Date/time objects. Rhino engine natively handles
	 * wrapping String, Number and Boolean objects.
	 *
	 * @param value Java object to convert from
	 * @scope A javascript scope with the proper native JS constructors defined
	 */
	public static Object convertToJavascriptValue(Object value, Scriptable scope) {
		if (value instanceof Date) {
			// Wrap in Javascript native Date class
			Context cx = Context.enter();
			try {
				if (scope == null) {
					scope = new ImporterTopLevel(cx);
				}
				// never convert java.sql.Time and java.sql.Date to java
				// script's
				// NativeDate
				if (value instanceof java.sql.Time || value instanceof java.sql.Date) {
					return Context.javaToJS(value, scope);
				}

				// Javascript and Java Date has the same conversion to/from
				// a
				// Long value
				Long timeVal = new Long(((Date) value).getTime());
				return ScriptRuntime.newObject(cx, scope, "Date", new Object[] { timeVal });

			} finally {
				Context.exit();
			}
		}

		return value;

	}

	/**
	 * If caller does not have a scope for evaluation, the caller can use this
	 * method to evaluate expression. But if caller has its own scope which can be
	 * used, the better way is call the method of convertToJavascriptValue( Object
	 * value, Scriptable scope ).
	 *
	 * @param value
	 * @return
	 */
	public static Object convertToJavascriptValue(Object value) {
		return convertToJavascriptValue(value, null);
	}

	/**
	 * Handles a Rhino script evaluation result, converting Javascript object into
	 * equivalent Java objects if necessary.
	 *
	 * @param inputObj Object returned by rhino engine.
	 * @return If inputObj is a native Javascript object, its equivalent Java object
	 *         is returned; otherwise inputObj is returned
	 */
	public static Object convertJavascriptValue(Object inputObj) {
		if (inputObj instanceof Undefined) {
			return null;
		}
		if (inputObj instanceof IdScriptableObject) {
			// Return type is possibly a Javascript native object
			// Convert to Java object with same value
			String jsClass = ((Scriptable) inputObj).getClassName();
			if ("Date".equals(jsClass)) {
				return Context.toType(inputObj, Date.class);
			} else if ("Boolean".equals(jsClass)) {
				return Boolean.valueOf(Context.toBoolean(inputObj));
			} else if ("Number".equals(jsClass)) {
				return new Double(Context.toNumber(inputObj));
			} else if ("String".equals(jsClass)) {
				return inputObj.toString();
			} else if ("Array".equals(jsClass)) {
				Object[] obj = new Object[(int) ((NativeArray) inputObj).getLength()];
				for (int i = 0; i < obj.length; i++) {
					obj[i] = convertJavascriptValue(((NativeArray) inputObj).get(i, null));
				}
				return obj;
			}
		} else if (inputObj instanceof Wrapper) {
			return ((Wrapper) inputObj).unwrap();
		} else if (inputObj instanceof Scriptable) {
			return ((Scriptable) inputObj).getDefaultValue(null);
		}

		return inputObj;
	}

	/**
	 * Converts Rhino exception (a runtime exception) to BirtException
	 *
	 * @param e          Rhino exception
	 * @param scriptText Javascript code which resulted in the exception (for error
	 *                   reporting purpose)
	 * @param source     description of the source script. If null, get this info
	 *                   from Rhino exception
	 * @param lineNo     lineNo of error location
	 * @throws
	 */
	public static BirtException wrapRhinoException(RhinoException e, String scriptText, String source, int lineNo) {
		if (source == null) {
			// Note that sourceName from RhinoException sometimes get truncated (need to
			// find out why)
			// Better some than nothing
			source = e.sourceName();
			lineNo = e.lineNumber();
		}

		if (logger.isLoggable(Level.FINE)) {
			logger.log(Level.FINE, "Unexpected RhinoException. Source=" + source + ", line=" + lineNo + ", Script=\n"
					+ scriptText + "\n", e);
		}

		return new CoreException(ResourceConstants.JAVASCRIPT_ERROR, new Object[] { e.getLocalizedMessage() }, e);
	}

	/**
	 *
	 * @param args
	 * @return
	 */
	public static Object[] convertToJavaObjects(Object[] args) {
		for (int i = 0; i < args.length; i++) {
			args[i] = JavascriptEvalUtil.convertJavascriptValue(args[i]);
		}
		return args;
	}

	/**
	 * This method transforms a string to JS string constants.
	 *
	 * @param s
	 * @return
	 */
	public static String transformToJsConstants(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder buffer = new StringBuilder();
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				buffer.append("\\\\");
				break;
			case '\b':
				buffer.append("\\b");
				break;
			case '\t':
				buffer.append("\\t");
				break;
			case '\n':
				buffer.append("\\n");
				break;
			case '\f':
				buffer.append("\\f");
				break;
			case '\r':
				buffer.append("\\r");
				break;
			case '"':
				buffer.append("\\\"");
				break;
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public static String transformToJsExpression(String s) {
		return s == null ? s : "\"" + transformToJsConstants(s) + "\"";
	}

	public static String evaluateJsConstants(String js) {
		if (js == null) {
			return null;
		}
		String result = js.substring(1, js.length() - 1);
		StringBuilder buffer = new StringBuilder();
		int length = result.length();
		int index = 0;
		while (index < length) {
			char c = result.charAt(index);
			if (c == '\\') {
				index++;
				if (index < length) {
					char nc = result.charAt(index);
					switch (nc) {
					case '\\':
						buffer.append('\\');
						break;
					case 'b':
						buffer.append('\b');
						break;
					case 't':
						buffer.append('\t');
						break;
					case 'n':
						buffer.append('\n');
						break;
					case 'f':
						buffer.append('\f');
						break;
					case 'r':
						buffer.append('\r');
						break;
					case '"':
						buffer.append('\"');
						break;
					default:
						buffer.append(c);
					}
				}
			} else {
				buffer.append(c);
			}
			index++;
		}
		return buffer.toString();
	}

	private static Object getSecurityDomain(final String file) {
		if ((file == null) || !isECMAScriptSecurityEnabled()) {
			return null;
		}
		try {
			return new CodeSource(new URL(file), (java.security.cert.Certificate[]) null);
		} catch (MalformedURLException ex) {
			try {
				return new CodeSource(new File(file).toURI().toURL(), (java.security.cert.Certificate[]) null);
			} catch (MalformedURLException e) {
				return null;
			}
		}
	}

	/**
	 * Evaluate the system property to use the JavaScript security based on
	 * certificates
	 */
	private static boolean isECMAScriptSecurityEnabled() {
		boolean scriptSecurity = false;
		/* System property: -Dbirt.ecmascript.security */
		String configuredEcmaScriptSecurityProperty = System.getProperty(ECMA_SCRIPT_SECURITY_PROPERTY_KEY);
		if (configuredEcmaScriptSecurityProperty != null
				&& configuredEcmaScriptSecurityProperty.equalsIgnoreCase(ECMA_SCRIPT_SECURITY_ENABLED)) {
			scriptSecurity = true;
		}
		return scriptSecurity;
	}
}
