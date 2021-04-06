/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.javascript;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.script.CoreJavaScriptInitializer;
import org.eclipse.birt.core.script.CoreJavaScriptWrapper;
import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.core.script.IJavascriptWrapper;
import org.eclipse.birt.core.script.IScriptEngine;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.report.model.core.JavaScriptExecutionStatus;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.LazilyLoadedCtor;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Wraps around the Rhino Script context
 * 
 */
public class JavascriptEngine implements IScriptEngine, IDataScriptEngine {

	/**
	 * for logging
	 */
	protected static Logger logger = Logger.getLogger(JavascriptEngine.class.getName());

	private static Script cachedScript;

	/**
	 * the JavaScript Context
	 */
	protected Context context;

	protected ImporterTopLevel global;

	protected ScriptableObject root;

	private Map<String, Object> propertyMap = new HashMap<String, Object>();

	private JavascriptEngineFactory factory;

	static {
		try {
			Context context = Context.enter();
			cachedScript = context.compileString("function writeStatus(msg) { _statusHandle.showStatus(msg); }",
					"<inline>", 1, null);
			context.exit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public JavascriptEngine(JavascriptEngineFactory factory, ScriptableObject root) throws BirtException {
		this.factory = factory;
		try {
			this.context = Context.enter();
			this.global = new ImporterTopLevel();
			this.root = root;
			if (root != null) {
				// can not put this object to root, because this object will
				// cache package and classloader information.
				// so we need rewrite this property.
				new LazilyLoadedCtor(global, "Packages", "org.mozilla.javascript.NativeJavaTopPackage", false);
				global.exportAsJSClass(3, global, false);
				global.delete("constructor");
				global.setPrototype(root);
			} else {
				global.initStandardObjects(context, true);
			}
			if (global.get(org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCTION_BEAN_NAME,
					global) == org.mozilla.javascript.UniqueTag.NOT_FOUND) {
				IScriptFunctionContext functionContext = new IScriptFunctionContext() {

					public Object findProperty(String name) {
						return propertyMap.get(name);
					}
				};

				Object sObj = Context.javaToJS(functionContext, global);
				global.put(org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCTION_BEAN_NAME,
						global, sObj);
			}
			initWrapFactory();
		} catch (Exception ex) {
			Context.exit();
			throw new BirtException();
		}
	}

	private void initWrapFactory() {
		WrapFactory wrapFactory = new WrapFactory() {

			protected IJavascriptWrapper coreWrapper = new CoreJavaScriptWrapper();

			/**
			 * wrapper an java object to javascript object.
			 */
			public Object wrap(Context cx, Scriptable scope, Object obj, Class staticType) {
				Object object = coreWrapper.wrap(cx, scope, obj, staticType);
				if (object != obj) {
					return object;
				}
				return super.wrap(cx, scope, obj, staticType);
			}
		};
		context.setWrapFactory(wrapFactory);
		new CoreJavaScriptInitializer().initialize(context, global);
	}

	public void setTimeZone(TimeZone zone) {
		propertyMap.put(IScriptFunctionContext.TIMEZONE, zone);
	}

	public void setLocale(Locale locale) {
		context.setLocale(locale);
		propertyMap.put(IScriptFunctionContext.LOCALE, ULocale.forLocale(locale));
	}

	public String getScriptLanguage() {
		return JavascriptEngineFactory.SCRIPT_JAVASCRIPT;
	}

	/**
	 * exit the scripting context
	 */
	public void close() {
		if (root != null) {
			factory.releaseRootScope(root);
			root = null;
		}
		if (context != null) {
			Context.exit();
			context = null;
		}
	}

	/**
	 * creates a new scripting scope
	 */
	private Scriptable createJsScope(Scriptable parent, Object object) {
		Scriptable jsScope = null;
		if (object != null) {
			if (!(object instanceof Scriptable)) {
				object = javaToJs(parent, object);
			}
		}
		if (object instanceof Scriptable) {
			jsScope = new NativeObject();
			jsScope.setPrototype((Scriptable) object);
		} else {
			jsScope = context.newObject(parent);
		}
		jsScope.setParentScope(parent);
		return jsScope;
	}

	public JavascriptEngineFactory getFactory() {
		return factory;
	}

	public CompiledJavascript compile(ScriptContext scriptContext, final String id, final int lineNumber,
			final String script) throws BirtException {
		Script scriptObject = AccessController.doPrivileged(new PrivilegedAction<Script>() {

			public Script run() {
				return context.compileString(script, id, lineNumber, ScriptUtil.getSecurityDomain(id));
			}
		});
		return new CompiledJavascript(id, lineNumber, script, scriptObject);
	}

	private JavascriptContext createJsContext(ScriptContext context) {
		ScriptContext parent = context.getParent();
		Scriptable parentJsScope = global;
		if (parent != null) {
			JavascriptContext parentJsContext = (JavascriptContext) parent
					.getScriptContext(JavascriptEngineFactory.SCRIPT_JAVASCRIPT);
			if (parentJsContext == null) {
				parentJsContext = createJsContext(parent);
			}
			parentJsScope = parentJsContext.getScope();
		}

		Object scope = context.getScopeObject();
		Scriptable jsScope = createJsScope(parentJsScope, scope);
		JavascriptContext jsContext = new JavascriptContext(context, jsScope);
		// Register writeStatus method in root context.
		if (parent == null) {
			cachedScript.exec(this.context, jsScope);
		}
		Map<String, Object> attrs = context.getAttributes();
		for (Entry<String, Object> entry : attrs.entrySet()) {
			jsContext.setAttribute(entry.getKey(), entry.getValue());
		}
		context.setScriptContext(JavascriptEngineFactory.SCRIPT_JAVASCRIPT, jsContext);
		return jsContext;
	}

	public Object evaluate(ScriptContext scriptContext, ICompiledScript compiledScript) throws BirtException {
		assert (compiledScript instanceof CompiledJavascript);
		// String source = ( (CompiledJavascript) compiledScript )
		// .getScriptText( );
		try {
			JavaScriptExecutionStatus.setExeucting(true);
			Script script = ((CompiledJavascript) compiledScript).getCompiledScript();
			Object value = script.exec(context, getJSScope(scriptContext));
			return jsToJava(value);
		} catch (Throwable e) {
			// Do not include javascript source code
			// throw new CoreException(
			// ResourceConstants.JAVASCRIPT_COMMON_ERROR,
			// new Object[]{source, e.getMessage( )}, e );
			throw new CoreException(ResourceConstants.INVALID_EXPRESSION, e.getMessage(), e);
		} finally {
			JavaScriptExecutionStatus.remove();
		}
	}

	private Object javaToJs(Scriptable scope, Object value) {
		return Context.javaToJS(value, scope);
	}

	/**
	 * converts a JS object to a Java object
	 * 
	 * @param jsValue javascript object
	 * @return Java object
	 */
	public Object jsToJava(Object jsValue) {
		return JavascriptEvalUtil.convertJavascriptValue(jsValue);
	}

	public void setApplicationClassLoader(final ClassLoader appLoader) {
		if (appLoader == null) {
			return;
		}
		ClassLoader loader = appLoader;
		try {
			appLoader.loadClass("org.mozilla.javascript.Context");
		} catch (ClassNotFoundException e) {
			loader = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

				public ClassLoader run() {
					return new RhinoClassLoaderDecoration(appLoader, JavascriptEngine.class.getClassLoader());
				}
			});
		}
		context.setApplicationClassLoader(loader);
	}

	private static class RhinoClassLoaderDecoration extends ClassLoader {

		private ClassLoader applicationClassLoader;
		private ClassLoader rhinoClassLoader;

		public RhinoClassLoaderDecoration(ClassLoader applicationClassLoader, ClassLoader rhinoClassLoader) {
			this.applicationClassLoader = applicationClassLoader;
			this.rhinoClassLoader = rhinoClassLoader;
		}

		public Class<?> loadClass(String name) throws ClassNotFoundException {
			try {
				return applicationClassLoader.loadClass(name);
			} catch (ClassNotFoundException e) {
				return rhinoClassLoader.loadClass(name);
			}
		}
	}

	public Context getJSContext(ScriptContext scriptContext) {
		return context;
	}

	public Scriptable getJSScope(ScriptContext scriptContext) {
		JavascriptContext jsContext = (JavascriptContext) scriptContext
				.getScriptContext(JavascriptEngineFactory.SCRIPT_JAVASCRIPT);
		if (jsContext == null) {
			jsContext = createJsContext(scriptContext);
		}

		return jsContext.getScope();
	}
}