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
