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

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * ScriptVariable
 */
public class ScriptVariable extends ScriptDebugElement implements IVariable {

	private String name;
	private String typeName;
	private ScriptStackFrame frame;
	IValue value;

	/**
	 * Constructor
	 *
	 * @param frame
	 * @param name
	 * @param typeName
	 */
	public ScriptVariable(ScriptStackFrame frame, String name, String typeName) {
		super((ScriptDebugTarget) frame.getDebugTarget());
		this.frame = frame;
		this.name = name;
		this.typeName = typeName;
	}

	/**
	 * Sets the ori value
	 *
	 * @param value
	 */
	public void setOriVale(IValue value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	@Override
	public String getName() throws DebugException {
		return name;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	@Override
	public String getReferenceTypeName() throws DebugException {
		return typeName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	@Override
	public IValue getValue() throws DebugException {
		return value;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	@Override
	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String expression) throws DebugException {
		// don't support
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse.debug.
	 * core.model.IValue)
	 */
	@Override
	public void setValue(IValue value) throws DebugException {
		// don't support
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	@Override
	public boolean supportsValueModification() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */
	@Override
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse.debug
	 * .core.model.IValue)
	 */
	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

	/**
	 * Gets the script stack frame.
	 *
	 * @return
	 */
	protected ScriptStackFrame getScriptStackFrame() {
		return frame;
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
			return getName();
		} catch (DebugException e) {
			return name;
		}
	}
}
