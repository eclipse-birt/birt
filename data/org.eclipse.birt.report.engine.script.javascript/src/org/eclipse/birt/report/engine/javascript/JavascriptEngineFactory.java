/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.javascript;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.IScriptEngine;
import org.eclipse.birt.core.script.IScriptEngineFactory;
import org.mozilla.javascript.ClassCache;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.SecurityController;

public class JavascriptEngineFactory implements IScriptEngineFactory {
	public static String SCRIPT_JAVASCRIPT = "javascript";

	public static final boolean USE_DYNAMIC_SCOPE = true;

	private static Logger logger = Logger.getLogger(JavascriptEngineFactory.class.getName());

	/**
	 * root script scope. contains objects shared by the whole engine.
	 */
	private LinkedList<ScriptableObject> rootScopes = new LinkedList<>();

	public static void initMyFactory() {
		ContextFactory.initGlobal(new MyFactory());
		if (System.getSecurityManager() != null) {
			SecurityController.initGlobal(ScriptUtil.createSecurityController());
		}
	}

	static class MyFactory extends ContextFactory {

		@Override
		protected boolean hasFeature(Context cx, int featureIndex) {
			if (featureIndex == Context.FEATURE_DYNAMIC_SCOPE) {
				return USE_DYNAMIC_SCOPE;
			}
			return super.hasFeature(cx, featureIndex);
		}
	}

	public JavascriptEngineFactory() {
	}

	protected ScriptableObject createRootScope() throws BirtException {
		Context context = Context.enter();
		try {
			ScriptableObject rootScope = context.initStandardObjects();
			context.evaluateString(rootScope,
					"function registerGlobal( name, value) { _jsContext.registerGlobalBean(name, value); }", "<inline>",
					0, null);
			context.evaluateString(rootScope,
					"function unregisterGlobal(name) { _jsContext.unregisterGlobalBean(name); }", "<inline>", 0, null);
			return rootScope;
		} catch (Exception ex) {
			logger.log(Level.WARNING, "Error occurs while initialze script scope", ex);
			return null;
		} finally {
			Context.exit();
		}
	}

	synchronized protected ScriptableObject getRootScope() throws BirtException {
		if (!rootScopes.isEmpty()) {
			return rootScopes.remove();
		}
		return createRootScope();
	}

	synchronized protected void releaseRootScope(ScriptableObject rootScope) {
		if (rootScope != null) {
			ClassCache classCache = ClassCache.get(rootScope);
			if (classCache != null) {
				classCache.clearCaches();
			}
			rootScopes.add(rootScope);
		}
	}

	@Override
	public IScriptEngine createScriptEngine() throws BirtException {
		ScriptableObject rootScope = getRootScope();
		return new JavascriptEngine(this, rootScope);
	}

	@Override
	public String getScriptLanguage() {
		return SCRIPT_JAVASCRIPT;
	}

	public static void destroyMyFactory() {
		ContextFactory factory = ContextFactory.getGlobal();
		if (factory instanceof MyFactory) {
			try {
				Class factoryClass = Class.forName("org.mozilla.javascript.ContextFactory");
				Field field = factoryClass.getDeclaredField("hasCustomGlobal");
				field.setAccessible(true);
				field.setBoolean(factoryClass, false);
				field = factoryClass.getDeclaredField("global");
				field.setAccessible(true);
				field.set(factoryClass, new ContextFactory());
			} catch (Exception ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
			}

		}
	}

}
