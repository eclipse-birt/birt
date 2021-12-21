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

package org.eclipse.birt.report.debug.internal.core.vm.js;

/**
 * JsFunctionSource
 */
public class JsFunctionSource {

	private String functionName;
	private String sourceName;
	private String source;
	private int startLineNumber;

	public JsFunctionSource(String sourceName, String functionName, String source, int startLineNumber) {
		this.sourceName = sourceName;
		this.functionName = functionName;
		this.source = source;
		this.startLineNumber = startLineNumber;
	}

	public String getFunctionName() {
		return functionName;
	}

	public String getSourceName() {
		return sourceName;
	}

	public String getSource() {
		return source;
	}

	public int getStartLineNumber() {
		return startLineNumber;
	}

}
