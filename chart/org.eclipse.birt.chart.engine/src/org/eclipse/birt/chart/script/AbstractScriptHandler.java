/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.util.ULocale;

/**
 * 
 * The abstract class defines methods to execute java function and java script
 * functions, it makes the sub-class can execute own regular java functions and
 * java script functions.
 * 
 * @since 2.5
 */

public abstract class AbstractScriptHandler<T> extends ScriptableObject {

	public static final String BEFORE_DATA_SET_FILLED = "beforeDataSetFilled"; //$NON-NLS-1$

	public static final String AFTER_DATA_SET_FILLED = "afterDataSetFilled"; //$NON-NLS-1$

	public static final String BEFORE_GENERATION = "beforeGeneration"; //$NON-NLS-1$

	public static final String AFTER_GENERATION = "afterGeneration"; //$NON-NLS-1$

	public static final String BEFORE_RENDERING = "beforeRendering"; //$NON-NLS-1$

	public static final String AFTER_RENDERING = "afterRendering"; //$NON-NLS-1$

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	// PRE-DEFINED INSTANCES AVAILABLE FOR REUSE
	protected final transient Object[] ONE_ELEMENT_ARRAY = new Object[1];

	protected final transient Object[] TWO_ELEMENT_ARRAY = new Object[2];

	protected final transient Object[] THREE_ELEMENT_ARRAY = new Object[3];

	protected transient Scriptable scope = null;

	protected transient T javahandler = null;

	/**
	 * @deprecated locale is stored in IChartScriptContext
	 */
	protected transient ULocale lcl = null;

	protected transient IScriptClassLoader iscl = null;

	protected transient List<String> javaScriptFunctionNamesCache = null;

	protected IScriptContext csc;

	/**
	 * The constructor.
	 * 
	 */
	public AbstractScriptHandler() {
		final Context cx = Context.enter();
		try {
			// scope = cx.initStandardObjects();
			scope = new ImporterTopLevel(cx);
		} finally {
			Context.exit();
		}
	}

	abstract protected ILogger getLogger();

	abstract protected Map<String, Method> getJavaFunctionMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public final String getClassName() {
		return getClass().getName();
	}

	/**
	 * @return returns the scope of current JavaScript context.
	 */
	public final Scriptable getScope() {
		return scope;
	}

	/**
	 * @deprecated Not used anymore. Use IChartScriptContext to store the locale
	 *             now. This is kept for backward compatibility only.
	 * @param lcl
	 */
	public final void setLocale(ULocale lcl) {
		this.lcl = lcl;
	}

	/**
	 * Sets the script class loader. This loader is responsible to load all user
	 * defined script class.
	 * 
	 * @param value
	 */
	public final void setScriptClassLoader(IScriptClassLoader value) {
		iscl = value;
	}

	/**
	 * Initialize the JavaScript context using given parent scope.
	 * 
	 * @param scPrototype Parent scope object. If it's null, use default scope.
	 */
	public final void init(Scriptable scPrototype) throws ChartException {
		final Context cx = Context.enter();
		try {
			if (scPrototype == null) // NO PROTOTYPE
			{
				// scope = cx.initStandardObjects();
				scope = new ImporterTopLevel(cx);
			} else {
				scope = cx.newObject(scPrototype);
				scope.setPrototype(scPrototype);
				// !don't reset the parent scope here.
				// scope.setParentScope( null );
			}

			// final Scriptable scopePrevious = scope;
			// !deprecated, remove this later. use script context instead.
			// registerExistingScriptableObject( this, "chart" ); //$NON-NLS-1$
			// scope = scopePrevious; // RESTORE

			// !deprecated, remove this later, use logger from script context
			// instead.
			// ADD LOGGING CAPABILITIES TO JAVASCRIPT ACCESS
			final Object oConsole = Context.javaToJS(getLogger(), scope);
			scope.put("logger", scope, oConsole); //$NON-NLS-1$
		} catch (RhinoException jsx) {
			throw convertException(jsx);
		} finally {
			Context.exit();
		}
	}

	/**
	 * Registers an existing scriptable object into current JavaScript context.
	 * 
	 * @param so       The existing scriptable object to be registered
	 * @param sVarName The name of the javascript variable associated with the new
	 *                 scriptable object that will be added to the scope
	 * @throws ChartException
	 */
	public final void registerExistingScriptableObject(ScriptableObject so, String sVarName) throws ChartException {
		try {
			ScriptableObject.defineClass(scope, so.getClass());
		} catch (Exception ex) {
			throw convertException(ex);
		}

		final Context cx = Context.enter();
		Scriptable soNew = null;
		try {
			soNew = cx.newObject(scope, so.getClassName(), null);
		} catch (RhinoException ex) {
			throw convertException(ex);
		} finally {
			Context.exit();
		}
		so.setPrototype(soNew.getPrototype());
		so.setParentScope(soNew.getParentScope());
		scope.put(sVarName, scope, so);
	}

	/**
	 * Registers a new scriptable object into current JavaScript context.
	 * 
	 * @param clsScriptable The class representing the new scriptable object to be
	 *                      registered
	 * @param sVarName      The name of the javascript variable associated with the
	 *                      new scriptable object that will be added to the scope
	 * @throws ChartException
	 */
	public final void registerNewScriptableObject(Class<? extends Scriptable> clsScriptable, String sVarName)
			throws ChartException {
		try {
			ScriptableObject.defineClass(scope, clsScriptable);
		} catch (Exception ex) {
			throw convertException(ex);
		}

		final Context cx = Context.enter();
		Scriptable soNew = null;
		try {
			soNew = cx.newObject(scope, clsScriptable.getName(), null);
		} catch (RuntimeException ex) {
			throw convertException(ex);
		} finally {
			Context.exit();
		}
		scope.put(sVarName, scope, soNew);
	}

	/**
	 * Registers a new variable to current JavaScript context. If the name already
	 * exists, it'll be overwritten.
	 * 
	 * @param sVarName
	 * @throws ChartException
	 */
	public final void registerVariable(String sVarName, Object var) throws ChartException {
		Context.enter();

		try {
			final Object oConsole = Context.javaToJS(var, scope);
			scope.put(sVarName, scope, oConsole);
		} finally {
			Context.exit();
		}
	}

	/**
	 * Unregister a variable from current JavaScript context.
	 * 
	 * @param sVarName
	 * @throws ChartException
	 */
	public final void unregisterVariable(String sVarName) throws ChartException {
		scope.delete(sVarName);
	}

	/**
	 * Finds the JavaScript funtion by given name.
	 * 
	 * @param sFunctionName The name of the function to be searched for
	 * @return An instance of the function being searched for or null if it isn't
	 *         found
	 */
	private final Function getJavascriptFunction(String sFunctionName) {
		// TODO: CACHE PREVIOUSLY CREATED FUNCTION REFERENCES IN A HASHTABLE?

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
	private final Object callJavaScriptFunction(Function f, Object[] oaArgs) throws ChartException

	{
		final Context cx = Context.enter();
		Object oReturnValue = null;
		// #229402
		ClassLoader oldLoader = cx.getApplicationClassLoader();
		ClassLoader appLader = SecurityUtil.getClassLoader(AbstractScriptHandler.this.getClass());
		cx.setApplicationClassLoader(appLader);

		// Initialize BIRT functions, register them into current script context.
		new CoreJavaScriptInitializer().initialize(cx, scope);

		try {
			oReturnValue = f.call(cx, scope, scope, oaArgs);
		} catch (RhinoException ex) {
			throw convertException(ex);
		} finally {
			cx.setApplicationClassLoader(oldLoader);
			Context.exit();
		}
		return oReturnValue;
	}

	private final boolean isJavaFuntion(String name) {
		return getJavaFunctionMap().get(name) != null;
	}

	private final Object callJavaFunction(String name, Object[] oaArgs) {
		if (javahandler == null) {
			return null;
		}

		Object[] tmpArgs = new Object[3];
		if (oaArgs.length > 0) {
			tmpArgs[0] = oaArgs[0];
		}
		if (oaArgs.length > 1) {
			tmpArgs[1] = oaArgs[1];
		}
		if (oaArgs.length > 2) {
			tmpArgs[2] = oaArgs[2];
		}

		if (!callRegularJavaFunction(name, tmpArgs)) {
			// Use reflect to call other methods
			Method mtd = getJavaFunctionMap().get(name);
			try {
				return SecurityUtil.invokeMethod(mtd, javahandler, oaArgs);
			} catch (Exception e) {
				getLogger().log(e);
			}
		}

		return null;
	}

	/**
	 * This method calls actual regular java function, returns true if the specified
	 * function is registered and called, else returns false.Sub-class will override
	 * this method to implement own process.
	 * 
	 * @param functionName
	 * @param arguments
	 */
	protected boolean callRegularJavaFunction(String functionName, Object[] arguments) {
		// use regular interface call instead of reflection to gain performance.
		return false;
	}

	/**
	 * Call JavaScript functions with one argument.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 */
	public static final Object callFunction(AbstractScriptHandler<?> sh, String sFunction, Object oArg1)
			throws ChartException {
		if (sh == null) {
			return null;
		}

		if (sh.javahandler != null && sh.isJavaFuntion(sFunction)) {
			sh.ONE_ELEMENT_ARRAY[0] = oArg1;
			return sh.callJavaFunction(sFunction, sh.ONE_ELEMENT_ARRAY);
		} else {
			final Function f = sh.getJavascriptFunction(sFunction);
			if (f != null) {
				sh.ONE_ELEMENT_ARRAY[0] = oArg1;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction(f, sh.ONE_ELEMENT_ARRAY);

				return oReturnValue;
			} else {
				return null;
			}
		}
	}

	/**
	 * Call JavaScript functions with two arguments.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 * @param oArg2
	 */
	public static final Object callFunction(AbstractScriptHandler<?> sh, String sFunction, Object oArg1, Object oArg2)
			throws ChartException {
		if (sh == null) {
			return null;
		}

		if (sh.javahandler != null && sh.isJavaFuntion(sFunction)) {
			sh.TWO_ELEMENT_ARRAY[0] = oArg1;
			sh.TWO_ELEMENT_ARRAY[1] = oArg2;
			return sh.callJavaFunction(sFunction, sh.TWO_ELEMENT_ARRAY);
		} else {
			final Function f = sh.getJavascriptFunction(sFunction);
			if (f != null) {
				sh.TWO_ELEMENT_ARRAY[0] = oArg1;
				sh.TWO_ELEMENT_ARRAY[1] = oArg2;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction(f, sh.TWO_ELEMENT_ARRAY);

				return oReturnValue;
			} else {
				return null;
			}
		}
	}

	/**
	 * Call JavaScript functions with three arguments.
	 * 
	 * @param sh
	 * @param sFunction
	 * @param oArg1
	 * @param oArg2
	 * @param oArg3
	 */
	public static final Object callFunction(AbstractScriptHandler<?> sh, String sFunction, Object oArg1, Object oArg2,
			Object oArg3) throws ChartException {
		if (sh == null) {
			return null;
		}

		if (sh.javahandler != null && sh.isJavaFuntion(sFunction)) {
			sh.THREE_ELEMENT_ARRAY[0] = oArg1;
			sh.THREE_ELEMENT_ARRAY[1] = oArg2;
			sh.THREE_ELEMENT_ARRAY[2] = oArg3;
			return sh.callJavaFunction(sFunction, sh.THREE_ELEMENT_ARRAY);
		} else {
			final Function f = sh.getJavascriptFunction(sFunction);
			if (f != null) {
				sh.THREE_ELEMENT_ARRAY[0] = oArg1;
				sh.THREE_ELEMENT_ARRAY[1] = oArg2;
				sh.THREE_ELEMENT_ARRAY[2] = oArg3;
				Object oReturnValue = null;
				oReturnValue = sh.callJavaScriptFunction(f, sh.THREE_ELEMENT_ARRAY);

				return oReturnValue;
			} else {
				return null;
			}
		}
	}

	/**
	 * Evaluates the given expression and returns the value.
	 * 
	 * @param sScriptContent
	 */
	public final Object evaluate(String sScriptContent) throws ChartException {
		final Context cx = Context.enter();
		try {
			return cx.evaluateString(scope, sScriptContent, "<cmd>", 1, null); //$NON-NLS-1$
		} catch (RhinoException jsx) {
			throw convertException(jsx);
		} finally {
			Context.exit();
		}
	}

	/**
	 * Register the script content for current script handler.
	 * 
	 * @param sScriptContent This is either the JavaSciprt code content or a full
	 *                       class name which has implemented
	 *                       <code>IChartItemScriptHandler</code>
	 */
	@SuppressWarnings("unchecked")
	public final void register(String sScriptName, String sScriptContent) throws ChartException {
		try {
			getLogger().log(ILogger.INFORMATION, Messages.getString("Info.try.load.java.handler")); //$NON-NLS-1$

			Class<?> handlerClass = null;

			try {
				handlerClass = Class.forName(sScriptContent);
			} catch (ClassNotFoundException ex) {
				if (iscl != null) {
					handlerClass = iscl.loadClass(sScriptContent,
							SecurityUtil.getClassLoader(AbstractScriptHandler.this.getClass()));
				} else {
					throw ex;
				}

			}

			if (getEventHandlerClass().isAssignableFrom(handlerClass)) {
				try {
					javahandler = (T) SecurityUtil.newClassInstance(handlerClass);
				} catch (InstantiationException e) {
					throw new ChartException(ChartEnginePlugin.ID, BirtException.ERROR, e);
				} catch (IllegalAccessException e) {
					throw new ChartException(ChartEnginePlugin.ID, BirtException.ERROR, e);
				}

				getLogger().log(ILogger.INFORMATION, Messages.getString("Info.java.handler.loaded", //$NON-NLS-1$
						handlerClass, ULocale.getDefault()));
			} else {
				getLogger().log(ILogger.WARNING, Messages.getString("Info.invalid.java.handler", //$NON-NLS-1$
						handlerClass, ULocale.getDefault()));
			}
		} catch (ClassNotFoundException e) {
			// Not a Java class name, so this must be JavaScript code
			javahandler = null;

			getLogger().log(ILogger.INFORMATION, Messages.getString("Info.try.register.javascript.content")); //$NON-NLS-1$

			final Context cx = Context.enter();
			try {
				cx.evaluateString(scope, sScriptContent, sScriptName == null ? "<cmd>" : sScriptName, 1, null); //$NON-NLS-1$

				getLogger().log(ILogger.INFORMATION, Messages.getString("Info.javascript.content.registered")); //$NON-NLS-1$

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

	protected abstract Class getEventHandlerClass();

	/**
	 * Sets the context object of current script handler.
	 * 
	 * @param csc
	 */
	public void setScriptContext(IScriptContext csc) {
		this.csc = csc;

	}

	/**
	 * Converts general exception to more readable format.
	 * 
	 * @param ex
	 * @return
	 */
	protected ChartException convertException(Exception ex) {
		if (ex instanceof RhinoException) {
			RhinoException e = (RhinoException) ex;
			String lineSource = e.lineSource();
			String details = e.details();
			String lineNumber = String.valueOf(e.lineNumber());
			if (lineSource == null)
				lineSource = "";//$NON-NLS-1$
			return new ChartException(ChartEnginePlugin.ID, ChartException.SCRIPT, "exception.javascript.error", //$NON-NLS-1$
					new Object[] { details, lineNumber, lineSource }, Messages.getResourceBundle(csc.getULocale()), e);
		}
		/*
		 * TODO convert those exceptions too else if ( ex instanceof
		 * IllegalAccessException ) {} else if ( ex instanceof InstantiationException )
		 * {} else if ( ex instanceof InvocationTargetException ) { }
		 */
		else
			return new ChartException(ChartEnginePlugin.ID, ChartException.SCRIPT, ex);
	}
}
