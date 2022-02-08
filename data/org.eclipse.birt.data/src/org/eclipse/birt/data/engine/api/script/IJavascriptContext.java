/*
 *************************************************************************
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * Represents a Data Engine runtime object (e.g., data source or data set) which
 * emits events that can be handled by Javascript code. This interface provides
 * the necessary context to execute Javascript event handler code
 */
public interface IJavascriptContext {
	/**
	 * Returns a Scriptable object that should be used as the scope in which to
	 * execute Javascript code associated with the runtime object.
	 */
	public Scriptable getScriptScope() throws DataException;
}
