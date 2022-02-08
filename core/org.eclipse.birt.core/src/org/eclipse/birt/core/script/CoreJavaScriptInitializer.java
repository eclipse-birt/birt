/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
