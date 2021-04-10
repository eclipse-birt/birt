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

	public String getLanguage() {
		return "javascript"; // FIXME: return a constant
	}

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
