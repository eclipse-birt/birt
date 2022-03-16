/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.core.script.functionservice.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 */

public class FunctionProvider {

	private static Logger logger = Logger.getLogger(FunctionProvider.class.getName());
	private static String PROVIDER_CLASS = "org.eclipse.birt.core.internal.function.impl.FunctionProviderImpl"; //$NON-NLS-1$
	private static IFunctionProvider instance;

	/**
	 * Set the current function provider impl.
	 *
	 * @param provider
	 */
	public static void setFunctionProvider(IFunctionProvider provider) {
		if (provider == null || !isLoaded()) {
			// Only initialize and close once
			FunctionProvider.instance = provider;
		} else {
			logger.warning("FunctionProvider should not set twice."); //$NON-NLS-1$
		}
	}

	public static boolean isLoaded() {
		return FunctionProvider.instance != null;
	}

	protected synchronized static IFunctionProvider getFunctionProvider() {
		if (instance == null) {
			try {
				Class<?> clazz = Class.forName(PROVIDER_CLASS);
				if (clazz != null) {
					instance = (IFunctionProvider) clazz.newInstance();
				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, "failed to initialize IFunctionProvider instance", ex); //$NON-NLS-1$
			}
		}

		return instance;
	}

	/**
	 * Return all the categories defined by extensions.
	 *
	 * @return
	 * @throws BirtException
	 */
	public static IScriptFunctionCategory[] getCategories() throws BirtException {
		IFunctionProvider provider = getFunctionProvider();
		if (provider != null) {
			return provider.getCategories();
		}
		return new IScriptFunctionCategory[] {};
	}

	/**
	 * Return the functions that defined in a category.
	 *
	 * @param categoryName
	 * @return
	 * @throws BirtException
	 */
	public static IScriptFunction[] getFunctions(String categoryName) throws BirtException {
		IFunctionProvider provider = getFunctionProvider();
		if (provider != null) {
			return provider.getFunctions(categoryName);
		}
		return new IScriptFunction[0];
	}

	/**
	 * Register script functions to scope.
	 *
	 * @param cx
	 * @param scope
	 * @throws BirtException
	 */
	public static void registerScriptFunction(Context cx, Scriptable scope) throws BirtException {
		IFunctionProvider provider = getFunctionProvider();
		if (provider != null) {
			provider.registerScriptFunction(cx, scope);
		}
	}
}
