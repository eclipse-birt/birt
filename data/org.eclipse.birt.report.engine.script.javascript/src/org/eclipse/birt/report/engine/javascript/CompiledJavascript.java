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

public class CompiledJavascript implements ICompiledScript {
	private int lineNo;
	private String scriptText;
	private Script compiledScript;

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

	public int getLineNo() {
		return lineNo;
	}

	public String getScriptText() {
		return scriptText;
	}
}
