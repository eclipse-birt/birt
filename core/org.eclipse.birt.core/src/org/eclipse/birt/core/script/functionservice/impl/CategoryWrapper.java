
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.UniqueTag;
import org.mozilla.javascript.WrappedException;

import com.ibm.icu.text.Collator;

/**
 * This class wraps a function that defined by user using java to a scriptable
 * object that processable by Rhino.
 *
 */

public class CategoryWrapper extends ScriptableObject {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private transient IScriptFunctionCategory category;

	/**
	 * Constructor
	 *
	 * @param category
	 * @throws BirtException
	 */
	public CategoryWrapper(IScriptFunctionCategory category) throws BirtException {
		assert category != null;

		this.category = category;
		IScriptFunction[] functions = category.getFunctions();
		for (int i = 0; i < functions.length; i++) {
			final IScriptFunction function = functions[i];
			this.defineProperty(functions[i].getName(), new BaseFunction() {

				/**
				 *
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public Object call(Context cx, final Scriptable scope, Scriptable thisObj, java.lang.Object[] args) {
					Object[] convertedArgs = JavascriptEvalUtil.convertToJavaObjects(args);
					try {
						final IScriptFunctionContext scriptFunctionContext = getIScriptFunctionContext(scope);
						final Collator collator = getCollator(scope);

						IScriptFunctionContext wrappedScriptFunctionContext = scriptFunctionContext;
						if (wrappedScriptFunctionContext != null) {
							wrappedScriptFunctionContext = new IScriptFunctionContext() {
								@Override
								public Object findProperty(String name) {
									if ("compare_locale".equals(name)) //$NON-NLS-1$
									{
										return collator;
									}
									Object obj = scriptFunctionContext.findProperty(name);
									if (obj == null) {
										// If not found, try to get it via
										// javascript environment
										Scriptable currentScope = scope;
										while (currentScope != null) {
											obj = currentScope.get(name, scope);
											if (obj != UniqueTag.NOT_FOUND) {
												return obj;
											}
											currentScope = currentScope.getParentScope();
										}
									}
									return obj;
								}
							};
						}
						return function.execute(convertedArgs, wrappedScriptFunctionContext);
					} catch (BirtException e) {
						throw new WrappedException(e);
					}

				}

				private IScriptFunctionContext getIScriptFunctionContext(Scriptable scope) {
					if (scope == null) {
						return null;
					}
					Object obj = scope.get(
							org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCTION_BEAN_NAME,
							scope);
					if (obj == org.mozilla.javascript.UniqueTag.NOT_FOUND) {
						return getIScriptFunctionContext(scope.getParentScope());
					}
					return (IScriptFunctionContext) JavascriptEvalUtil.convertJavascriptValue(obj);
				}

				private Collator getCollator(Scriptable scope) {
					if (scope == null) {
						return null;
					}
					Scriptable prototype = scope.getPrototype();
					Object obj = org.mozilla.javascript.UniqueTag.NOT_FOUND;
					if (prototype != null) {
						obj = prototype.get("compare_locale", prototype);
					}
					if (obj == org.mozilla.javascript.UniqueTag.NOT_FOUND) {
						return getCollator(scope.getParentScope());
					}
					return (Collator) JavascriptEvalUtil.convertJavascriptValue(obj);
				}
			}, 0);
		}
	}

	@Override
	public String getClassName() {
		return this.category.getName();
	}

}
