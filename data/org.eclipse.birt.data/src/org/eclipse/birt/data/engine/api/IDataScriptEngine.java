
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

import org.eclipse.birt.core.script.ScriptContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 *
 */

public interface IDataScriptEngine {
	String ENGINE_NAME = "javascript";

	Context getJSContext(ScriptContext context);

	Scriptable getJSScope(ScriptContext context);
}
