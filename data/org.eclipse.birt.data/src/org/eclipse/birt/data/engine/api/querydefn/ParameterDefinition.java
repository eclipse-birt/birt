/*
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.IParameterDefinition;

/**
 * Default implementation of the
 * {@link org.eclipse.birt.data.engine.api.IParameterDefinition} interface.
 * <p>
 */

public class ParameterDefinition implements IParameterDefinition {
	private int posn = -1;
	private String name;
	private String nativeName;
	private int type = DataType.UNKNOWN_TYPE;
	private int nativeDataType = 0; // unknown
	private boolean isInputMode = false;
	private boolean isOutputMode = false;
	private boolean isInputOptional = true;
	private String defaultInputValue;
	private boolean isNullable = true;

	/** Constructs an empty parameter definition */
	public ParameterDefinition() {
	}

	/** Constructs a name-based parameter definition with specified data type */
	public ParameterDefinition(String name, int type) {
		this.name = name;
		this.type = type;
	}

	/** Constructs a position-based parameter definition with specified data type */
	public ParameterDefinition(int position, int type) {
		this.posn = position;
		this.type = type;
	}

	/**
	 * Constructs a name-based parameter definition with specified data type, and
	 * input/output mode
	 */
	public ParameterDefinition(String name, int type, boolean isInput, boolean isOutput) {
		this.name = name;
		this.type = type;
		isInputMode = isInput;
		isOutputMode = isOutput;
	}

	/**
	 * Constructs a position-based parameter definition with specified data type,
	 * and input/output mode
	 */
	public ParameterDefinition(int position, int type, boolean isInput, boolean isOutput) {
		this.posn = position;
		this.type = type;
		isInputMode = isInput;
		isOutputMode = isOutput;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the parameter
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getNativeName()
	 */
	public String getNativeName() {
		return nativeName;
	}

	/**
	 * Sets the parameter's native name as known to the underlying ODA driver. The
	 * value may be null for unknown or undefined name.
	 */
	public void setNativeName(String nativeName) {
		this.nativeName = nativeName;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getPosition()
	 */
	public int getPosition() {
		return posn;
	}

	/**
	 * Sets the parameter position
	 */
	public void setPosition(int posn) {
		this.posn = posn;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getType()
	 */
	public int getType() {
		return type;
	}

	/**
	 * Sets the parameter data type
	 */
	public void setType(int type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getNativeType()
	 */
	public int getNativeType() {
		return nativeDataType;
	}

	/**
	 * Sets the parameter native data type
	 */
	public void setNativeType(int typeCode) {
		nativeDataType = typeCode;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isInputMode()
	 */
	public boolean isInputMode() {
		return isInputMode;
	}

	/**
	 * Sets the input mode of the parameter.
	 * 
	 * @param isInput true if the parameter is of input mode, false otherwise.
	 */
	public void setInputMode(boolean isInput) {
		this.isInputMode = isInput;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isOutputMode()
	 */
	public boolean isOutputMode() {
		return isOutputMode;
	}

	/**
	 * Sets the output mode of the parameter.
	 * 
	 * @param isOutput true if the parameter is of output mode, false otherwise.
	 */
	public void setOutputMode(boolean isOutput) {
		this.isOutputMode = isOutput;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isInputOptional()
	 */
	public boolean isInputOptional() {
		return isInputMode() ? isInputOptional : true;
	}

	/**
	 * Sets whether the parameter's input value is optional. Applies to the
	 * parameter only if it is of input mode.
	 * 
	 * @param isOptional true if the parameter input value is optional, false
	 *                   otherwise.
	 */
	public void setInputOptional(boolean isOptional) {
		if (isInputMode())
			isInputOptional = isOptional;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#getDefaultInputValue()
	 */
	public String getDefaultInputValue() {
		return isInputMode() ? defaultInputValue : null;
	}

	/**
	 * Sets the parameter's default input value. Applies to the parameter only if it
	 * is of input mode.
	 * 
	 * @param defaultValue Default input value.
	 */
	public void setDefaultInputValue(String defaultValue) {
		if (isInputMode())
			defaultInputValue = defaultValue;
	}

	/**
	 * @see org.eclipse.birt.data.engine.api.IParameterDefinition#isNullable()
	 */
	public boolean isNullable() {
		return isNullable;
	}

	/**
	 * Sets whether the parameter's value can be null.
	 * 
	 * @param isNullable true if the parameter value can be null, false otherwise.
	 */
	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}
}
