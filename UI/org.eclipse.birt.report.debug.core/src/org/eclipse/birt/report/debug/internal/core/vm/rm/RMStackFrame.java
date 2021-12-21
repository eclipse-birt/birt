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

package org.eclipse.birt.report.debug.internal.core.vm.rm;

import java.io.Serializable;

import org.eclipse.birt.report.debug.internal.core.vm.VMStackFrame;
import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;

/**
 * RMStackFrame
 */
public class RMStackFrame implements VMStackFrame, Serializable {

	private static final long serialVersionUID = 1L;

	private int lineNo;
	private String name;
	private VMVariable[] vars;

	public RMStackFrame(String name, VMVariable[] vars, int lineNo) {
		this.name = name;
		this.vars = vars;
		this.lineNo = lineNo;
	}

	public VMValue evaluate(String expression) {
		return null;
	}

	public String getName() {
		return name;
	}

	public VMVariable[] getVariables() {
		return vars;
	}

	public int getLineNumber() {
		return lineNo;
	}

}
