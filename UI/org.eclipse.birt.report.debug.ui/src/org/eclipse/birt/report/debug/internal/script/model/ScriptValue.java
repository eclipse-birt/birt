/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.debug.internal.script.model;

import org.eclipse.birt.report.debug.internal.core.vm.VMValue;
import org.eclipse.birt.report.debug.internal.core.vm.VMVariable;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * ScriptValue
 */
public class ScriptValue extends ScriptDebugElement implements IValue {

	private VMValue value;
	private ScriptStackFrame frame;

	/**
	 * Constructor
	 *
	 * @param frame
	 * @param value
	 */
	public ScriptValue(ScriptStackFrame frame, VMValue value) {
		super((ScriptDebugTarget) frame.getDebugTarget());

		this.value = value;
		this.frame = frame;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IValue#getReferenceTypeName()
	 */
	@Override
	public String getReferenceTypeName() throws DebugException {
		return value.getTypeName();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IValue#getValueString()
	 */
	@Override
	public String getValueString() throws DebugException {
		return value.getValueString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IValue#getVariables()
	 */
	@Override
	public IVariable[] getVariables() throws DebugException {
		VMVariable[] variables = value.getMembers();

		IVariable[] retValue = new IVariable[variables.length];

		for (int i = 0; i < variables.length; i++) {
			VMVariable variable = variables[i];
			ScriptVariable debugVariable = new ScriptVariable(frame, variable.getName(), variable.getTypeName());

			VMValue value = variable.getValue();

			ScriptValue debugValue = new ScriptValue(frame, value);

			debugVariable.setOriVale(debugValue);

			retValue[i] = debugVariable;
		}

		return retValue;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IValue#hasVariables()
	 */
	@Override
	public boolean hasVariables() throws DebugException {
		return getVariables().length > 0;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IValue#isAllocated()
	 */
	@Override
	public boolean isAllocated() throws DebugException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.debug.internal.script.model.ScriptDebugElement#
	 * getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		try {
			return getValueString();
		} catch (DebugException e) {
			return ""; //$NON-NLS-1$
		}
	}

}
