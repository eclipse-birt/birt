/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
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

	@Override
	public VMValue evaluate(String expression) {
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public VMVariable[] getVariables() {
		return vars;
	}

	@Override
	public int getLineNumber() {
		return lineNo;
	}

}
