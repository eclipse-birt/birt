/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * used to intialize the java script context.
 * 
 * To use script intializer, the callers should: 1) create instance of
 * IJavascriptInitializer 2) call ScriptContext.registerInitializer() to
 * register that instance.
 * 
 * After that, the new ScriptContext instance will call the registed initializer
 * in the constructor.
 * 
 */
public interface IJavascriptInitializer {
	/**
	 * intialize the context and scope.
	 * 
	 * @param cx    context.
	 * @param scope scope.
	 */
	public void initialize(Context cx, Scriptable scope);
}
