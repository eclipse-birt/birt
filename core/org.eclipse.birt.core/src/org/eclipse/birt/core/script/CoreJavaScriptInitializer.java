/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import org.eclipse.birt.core.script.functionservice.impl.FunctionProvider;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class CoreJavaScriptInitializer implements IJavascriptInitializer {

	public void initialize(Context cx, Scriptable scope) {
		try {
			FunctionProvider.registerScriptFunction(cx, scope);
		} catch (Exception ex) {
			assert false;
		}
	}

}
