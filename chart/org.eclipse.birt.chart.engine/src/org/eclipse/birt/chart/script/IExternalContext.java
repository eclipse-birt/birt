/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.chart.script;

import java.io.Serializable;

import org.mozilla.javascript.Scriptable;

/**
 * This interface defines an common context adapter which provide scriptable
 * context object or plain context object.
 */
public interface IExternalContext extends Serializable {

	/**
	 * Returns the scriptable context object which is used in the javascript
	 * environment.
	 *
	 * @return
	 */
	Scriptable getScriptable();

	/**
	 * Returns the plain context object which is used in pure java environment.
	 *
	 * @return
	 */
	Object getObject();
}
