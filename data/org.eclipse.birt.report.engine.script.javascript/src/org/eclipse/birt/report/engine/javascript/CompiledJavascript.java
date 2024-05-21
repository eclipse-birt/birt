/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.javascript;

import org.eclipse.birt.core.script.ICompiledScript;
import org.mozilla.javascript.Script;

/**
 * Class of compiled JavaScript
 *
 * @since 3.3
 *
 */
public class CompiledJavascript implements ICompiledScript {
	private int lineNo;
	private String scriptText;
	private Script compiledScript;

	/**
	 * Constructor
	 *
	 * @param source     JavaScript source
	 * @param lineNo     line number
	 * @param scriptText JavaScript text
	 * @param script     script
	 */
	public CompiledJavascript(String source, int lineNo, String scriptText, Script script) {
		this.compiledScript = script;
		this.scriptText = scriptText;
		this.lineNo = lineNo;
	}

	@Override
	public String getLanguage() {
		return "javascript"; // FIXME: return a constant
	}

	@Override
	public Script getCompiledScript() {
		return compiledScript;
	}

	/**
	 * Get the line number
	 *
	 * @return the line number
	 */
	public int getLineNo() {
		return lineNo;
	}

	/**
	 * Get the script text
	 *
	 * @return the script text
	 */
	public String getScriptText() {
		return scriptText;
	}
}
