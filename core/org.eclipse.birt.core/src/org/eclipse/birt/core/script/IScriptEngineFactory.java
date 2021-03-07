/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import org.eclipse.birt.core.exception.BirtException;

public interface IScriptEngineFactory {

	String getScriptLanguage();

	/**
	 * Returns an instance of IScriptEngine associated with this factory.
	 */
	IScriptEngine createScriptEngine() throws BirtException;
}
