/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public String getName() throws DebugException {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	public String getReferenceTypeName() throws DebugException {
		return typeName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	public IValue getValue() throws DebugException {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */
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
	public void setValue(IValue value) throws DebugException {
		// don't support
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	public boolean supportsValueModification() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */
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
	public String getDisplayName() {
		try {
			return getName();
		} catch (DebugException e) {
			return name;
		}
	}
}
