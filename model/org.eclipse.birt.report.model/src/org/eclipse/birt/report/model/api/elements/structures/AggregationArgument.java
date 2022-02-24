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

package org.eclipse.birt.report.model.api.elements.structures;

import java.util.List;

import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.Structure;

/**
 * AggregationArgument.
 */
public class AggregationArgument extends Structure {

	/**
	 * Name of this structure. Matches the definition in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "AggregationArgument"; //$NON-NLS-1$

	/**
	 * Name of the argument name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the value member.
	 */

	public static final String VALUE_MEMBER = "value"; //$NON-NLS-1$

	/**
	 * The argument name.
	 */

	private String name = null;

	/**
	 * The argument value.
	 */

	private Expression value = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (NAME_MEMBER.equals(propName))
			return name;
		if (VALUE_MEMBER.equals(propName))
			return value;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (NAME_MEMBER.equals(propName))
			name = (String) value;
		else if (VALUE_MEMBER.equals(propName))
			this.value = (Expression) value;
		else
			assert false;
	}

	/**
	 * Returns the argument name.
	 * 
	 * @return the argument name.
	 */

	public String getName() {
		return (String) getProperty(null, NAME_MEMBER);
	}

	/**
	 * Sets the argument name.
	 * 
	 * @param argumentName the argument name to set
	 */

	public void setName(String argumentName) {
		setProperty(NAME_MEMBER, argumentName);
	}

	/**
	 * Returns the argument value.
	 * 
	 * @return the argument value.
	 */

	public String getValue() {
		return getStringProperty(null, VALUE_MEMBER);
	}

	/**
	 * Sets the argument value.
	 * 
	 * @param argumentValue the argument value to set
	 */

	public void setValue(String argumentValue) {
		setProperty(VALUE_MEMBER, argumentValue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new AggregationArgumentHandle(valueHandle, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#validate(org.eclipse.birt
	 * .report.model.core.Module, org.eclipse.birt.report.model.core.DesignElement)
	 */
	public List validate(Module module, DesignElement element) {
		List list = super.validate(module, element);

		if (StringUtil.isBlank(name)) {
			list.add(new PropertyValueException(element, getDefn().getMember(NAME_MEMBER), name,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

}
