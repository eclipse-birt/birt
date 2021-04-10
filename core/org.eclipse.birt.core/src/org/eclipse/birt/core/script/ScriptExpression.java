/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import org.mozilla.javascript.Script;

/**
 * 
 */

public class ScriptExpression {

	public static String defaultID = "<inline>";

	protected String scriptText;

	/**
	 * Source file name or the ID gotten from Model.
	 */
	protected String id = defaultID;

	protected int lineNumber = 1;

	protected Script compiledScript;

	public ScriptExpression(String scriptText) {
		this.scriptText = scriptText;
	}

	public ScriptExpression(String scriptText, String id) {
		this.scriptText = scriptText;
		this.id = id;
	}

	public ScriptExpression(String scriptText, String id, int lineNumber) {
		this.scriptText = scriptText;
		this.id = id;
		this.lineNumber = lineNumber;
	}

	public void setScriptText(String scriptText) {
		if (scriptText == null || !scriptText.equals(this.scriptText)) {
			this.scriptText = scriptText;
			compiledScript = null;
		}
	}

	public String getScriptText() {
		return scriptText;
	}

	public void setId(String id) {
		if (id == null || !id.equals(this.id)) {
			this.id = id;
			compiledScript = null;
		}
	}

	public String getId() {
		return id;
	}

	public void setLineNumber(int number) {
		if (number != lineNumber) {
			lineNumber = number;
			compiledScript = null;
		}
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setCompiledScript(Script script) {
		compiledScript = script;
	}

	public Script getCompiledScript() {
		return compiledScript;
	}
}
