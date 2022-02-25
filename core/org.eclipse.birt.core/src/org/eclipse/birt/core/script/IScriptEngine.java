/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

import java.util.Locale;

import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.util.TimeZone;

public interface IScriptEngine {
	/**
	 * Returns the script engine factory which created this engine instance.
	 */
	IScriptEngineFactory getFactory();

	/**
	 * Returns name of script.
	 *
	 * @return
	 */
	String getScriptLanguage();

	/**
	 * Evaluates a compiled script.
	 *
	 * @param script
	 * @return
	 * @throws BirtException
	 */
	Object evaluate(ScriptContext scriptContext, ICompiledScript script) throws BirtException;

	/**
	 * Compiles the script for later execution.
	 *
	 * @param script
	 * @param id
	 * @param lineNumber
	 * @return
	 */
	ICompiledScript compile(ScriptContext scriptContext, String fileName, int lineNumber, String script)
			throws BirtException;

	/**
	 * Sets time zone.
	 */
	void setTimeZone(TimeZone zone);

	/**
	 * Sets locale.
	 */
	void setLocale(Locale locale);

	/**
	 * Sets application class loader.
	 */
	void setApplicationClassLoader(ClassLoader loader);

	/**
	 * Closes the engine.
	 */
	void close();
}
