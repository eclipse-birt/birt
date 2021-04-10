/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal.handler;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstab;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCell;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabCellInstance;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabEventHandler;
import org.eclipse.birt.report.item.crosstab.core.script.ICrosstabInstance;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;

/**
 * CrosstabScriptHandler
 */
public final class CrosstabScriptHandler {

	public static final String ON_PREPARE_CROSSTAB = "onPrepareCrosstab"; //$NON-NLS-1$

	public static final String ON_PREPARE_CELL = "onPrepareCell"; //$NON-NLS-1$

	public static final String ON_CREATE_CROSSTAB = "onCreateCrosstab"; //$NON-NLS-1$

	public static final String ON_CREATE_CELL = "onCreateCell"; //$NON-NLS-1$

	public static final String ON_RENDER_CROSSTAB = "onRenderCrosstab"; //$NON-NLS-1$

	public static final String ON_RENDER_CELL = "onRenderCell"; //$NON-NLS-1$

	// public static final String ON_CROSSTAB_PAGE_BREAK =
	// "onCrosstabPageBreak"; //$NON-NLS-1$
	//
	// public static final String ON_CELL_PAGE_BREAK = "onCellPageBreak";
	// //$NON-NLS-1$

	private static final Logger logger = Logger.getLogger(CrosstabScriptHandler.class.getName());

	private static final Map<String, Method> JAVA_FUNTION_MAP = new HashMap<String, Method>();

	static {
		// init java function name lookup table.
		Method[] ms = ICrosstabEventHandler.class.getMethods();

		for (int i = 0; i < ms.length; i++) {
			JAVA_FUNTION_MAP.put(ms[i].getName(), ms[i]);
		}
	}

	private Object[] argCache = new Object[2];

	private Scriptable scope;

	private ICrosstabEventHandler javahandler;

	private List<String> javaScriptFunctionNamesCache;

	/**
	 * Initialize the JavaScript context using given parent scope.
	 * 
	 * @param scPrototype Parent scope object. If it's null, use default scope.
	 */
	public void init(Scriptable scPrototype) throws CrosstabException {
		final Context cx = Context.enter();
		try {
			if (scPrototype == null) {
				scope = new ImporterTopLevel(cx);
			} else {
				scope = cx.newObject(scPrototype);
				scope.setPrototype(scPrototype);
			}
		} catch (RhinoException jsx) {
			throw convertException(jsx);
		} finally {
			Context.exit();
		}
	}

	/**
	 * Finds the JavaScript funtion by given name.
	 * 
	 * @param sFunctionName The name of the function to be searched for
	 * @return An instance of the function being searched for or null if it isn't
	 *         found
	 */
	private Function getJavascriptFunction(String sFunctionName) {
		// TODO impl real function cache

		// use names cache for quick validation to improve performance
		if (javaScriptFunctionNamesCache == null || javaScriptFunctionNamesCache.indexOf(sFunctionName) < 0) {
			return null;
		}

		Context.enter();
		try {
			final Object oFunction = scope.get(sFunctionName, scope);
			if (oFunction != Scriptable.NOT_FOUND && oFunction instanceof Function) {
				return (Function) oFunction;
			}
			return null;
		} finally {
			Context.exit();
		}
	}

	/**
	 * Call JavaScript functions with an argument array.
	 * 
	 * @param f      The function to be executed
	 * @param oaArgs The Java object arguments passed to the function being executed
	 */
	private Object callJavaScriptFunction(Function f, Object[] oaArgs) throws CrosstabException

	{
		final Context cx = Context.enter();
		Object oReturnValue = null;
		try {
			oReturnValue = f.call(cx, scope, scope, oaArgs);
		} catch (RhinoException ex) {
			throw convertException(ex);
		} finally {
			Context.exit();
		}
		return oReturnValue;
	}

	private Object callJavaFunction(String name, Object[] oaArgs) {
		if (javahandler == null) {
			return null;
		}

		// use directly method call to gain performance
		if (ON_PREPARE_CROSSTAB.equals(name)) {
			javahandler.onPrepareCrosstab((ICrosstab) oaArgs[0], (IReportContext) oaArgs[1]);
		} else if (ON_PREPARE_CELL.equals(name)) {
			javahandler.onPrepareCell((ICrosstabCell) oaArgs[0], (IReportContext) oaArgs[1]);
		} else if (ON_CREATE_CROSSTAB.equals(name)) {
			javahandler.onCreateCrosstab((ICrosstabInstance) oaArgs[0], (IReportContext) oaArgs[1]);
		} else if (ON_CREATE_CELL.equals(name)) {
			javahandler.onCreateCell((ICrosstabCellInstance) oaArgs[0], (IReportContext) oaArgs[1]);
		} else if (ON_RENDER_CROSSTAB.equals(name)) {
			javahandler.onRenderCrosstab((ICrosstabInstance) oaArgs[0], (IReportContext) oaArgs[1]);
		} else if (ON_RENDER_CELL.equals(name)) {
			javahandler.onRenderCell((ICrosstabCellInstance) oaArgs[0], (IReportContext) oaArgs[1]);
		}
		// else if ( ON_CROSSTAB_PAGE_BREAK.equals( name ) )
		// {
		// javahandler.onCrosstabPageBreak( (ICrosstabInstance) oaArgs[0],
		// (IReportContext) oaArgs[1] );
		// }
		// else if ( ON_CELL_PAGE_BREAK.equals( name ) )
		// {
		// javahandler.onCellPageBreak( (ICrosstabCellInstance) oaArgs[0],
		// (IReportContext) oaArgs[1] );
		// }
		else {
			Method mtd = JAVA_FUNTION_MAP.get(name);

			try {
				return mtd.invoke(javahandler, oaArgs);
			} catch (Exception e) {
				logger.log(Level.SEVERE, e.getLocalizedMessage());
			}
		}

		return null;
	}

	public Object callFunction(String sFunction, Object oArg1, Object oArg2) throws CrosstabException {
		Object rt = null;

		if (javahandler != null && JAVA_FUNTION_MAP.containsKey(sFunction)) {
			argCache[0] = oArg1;
			argCache[1] = oArg2;
			try {
				rt = callJavaFunction(sFunction, argCache);
			} finally {
				argCache[0] = null;
				argCache[1] = null;
			}
		} else {
			Function f = getJavascriptFunction(sFunction);

			if (f != null) {
				argCache[0] = oArg1;
				argCache[1] = oArg2;
				try {
					rt = callJavaScriptFunction(f, argCache);
				} finally {
					argCache[0] = null;
					argCache[1] = null;
				}
			}
		}

		return rt;
	}

	private Class<?> loadJavaHandlerClass(String className, ClassLoader contextLoader) throws ClassNotFoundException {
		Class<?> handlerClass = null;

		try {
			handlerClass = Class.forName(className);
		} catch (ClassNotFoundException ex) {
			if (contextLoader != null) {

				try {
					handlerClass = contextLoader.loadClass(className);

					if (handlerClass != null) {
						return handlerClass;
					}
				} catch (Throwable e) {
					// app loader failed, need try dev loader
				}
			}

			// try dev loader
			ClassLoader parentLoader = CrosstabScriptHandler.class.getClassLoader();

			// Try using web application's webapplication.projectclasspath
			// to load it.
			// This would be the case where the application is deployed on
			// web server.
			handlerClass = getClassUsingCustomClassPath(className, EngineConstants.WEBAPP_CLASSPATH_KEY, parentLoader);

			if (handlerClass == null) {
				// Try using the user.projectclasspath property to load it
				// using the classpath specified. This would be the case
				// when debugging is used
				handlerClass = getClassUsingCustomClassPath(className, EngineConstants.PROJECT_CLASSPATH_KEY,
						parentLoader);

				if (handlerClass == null) {
					// The class is not on the current classpath.
					// Try using the workspace.projectclasspath property
					handlerClass = getClassUsingCustomClassPath(className, EngineConstants.WORKSPACE_CLASSPATH_KEY,
							parentLoader);
				}
			}

			if (handlerClass == null) {
				// Didn't find the class using any method, so throw the
				// exception
				throw ex;
			}

		}

		return handlerClass;
	}

	private Class<?> getClassUsingCustomClassPath(String className, String classPathKey, ClassLoader parentLoader) {
		String classPath = System.getProperty(classPathKey);
		if (classPath == null || classPath.length() == 0 || className == null)
			return null;
		String[] classPathArray = classPath.split(EngineConstants.PROPERTYSEPARATOR, -1);
		URL[] urls = null;
		if (classPathArray.length != 0) {
			List<URL> l = new ArrayList<URL>();
			for (int i = 0; i < classPathArray.length; i++) {
				String cpValue = classPathArray[i];
				File file = new File(cpValue);
				try {
					l.add(file.toURL());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
			urls = l.toArray(new URL[l.size()]);
		}

		if (urls != null) {
			ClassLoader cl = new URLClassLoader(urls, parentLoader);

			try {
				return cl.loadClass(className);
			} catch (ClassNotFoundException e) {
				// Ignore
			}
		}
		return null;
	}

	/**
	 * Register the script content for current script handler.
	 * 
	 * @param sScriptContent This is either the JavaSciprt code content or a full
	 *                       class name which has implemented
	 *                       <code>IChartItemScriptHandler</code>
	 */
	public void register(String sScriptName, String sScriptContent, ClassLoader contextLoader)
			throws CrosstabException {
		try {
			logger.log(Level.INFO, Messages.getString("CrosstabScriptHandler.info.try.load.crosstab.java.handler")); //$NON-NLS-1$

			Class<?> handlerClass = loadJavaHandlerClass(sScriptContent, contextLoader);

			if (ICrosstabEventHandler.class.isAssignableFrom(handlerClass)) {
				try {
					javahandler = (ICrosstabEventHandler) handlerClass.newInstance();
				} catch (InstantiationException e) {
					throw new CrosstabException(e);
				} catch (IllegalAccessException e) {
					throw new CrosstabException(e);
				}

				logger.log(Level.INFO, Messages.getString("CrosstabScriptHandler.info.crosstab.java.handler.loaded", //$NON-NLS-1$
						handlerClass));
			} else {
				logger.log(Level.WARNING, Messages.getString("CrosstabScriptHandler.info.invalid.crosstab.java.handler", //$NON-NLS-1$
						handlerClass));
			}
		} catch (ClassNotFoundException e) {
			// Not a Java class name, so this must be JavaScript code
			javahandler = null;

			logger.log(Level.INFO,
					Messages.getString("CrosstabScriptHandler.info.try.register.crosstab.javascript.content")); //$NON-NLS-1$

			final Context cx = Context.enter();
			try {
				cx.evaluateString(scope, sScriptContent, sScriptName == null ? "<cmd>" : sScriptName, 1, null); //$NON-NLS-1$

				logger.log(Level.INFO,
						Messages.getString("CrosstabScriptHandler.info.crosstab.javascript.content.registered")); //$NON-NLS-1$

				// prepare function name cache.
				Object[] objs = scope.getIds();

				if (objs != null) {
					javaScriptFunctionNamesCache = new ArrayList<String>();

					for (int i = 0; i < objs.length; i++) {
						javaScriptFunctionNamesCache.add(String.valueOf(objs[i]));
					}
				} else {
					javaScriptFunctionNamesCache = null;
				}

			} catch (RhinoException jsx) {
				throw convertException(jsx);
			} finally {
				Context.exit();
			}
		}

	}

	/**
	 * Converts general exception to more readable format.
	 * 
	 * @param ex
	 * @return
	 */
	protected CrosstabException convertException(Exception ex) {
		if (ex instanceof RhinoException) {
			RhinoException e = (RhinoException) ex;
			String lineSource = e.lineSource();
			String details = e.details();
			String lineNumber = String.valueOf(e.lineNumber());
			if (lineSource == null)
				lineSource = "";//$NON-NLS-1$
			return new CrosstabException(Messages.getString("CrosstabScriptHandler.error.javascript", //$NON-NLS-1$
					new Object[] { details, lineNumber, lineSource }));
		} else {
			return new CrosstabException(ex);
		}
	}
}