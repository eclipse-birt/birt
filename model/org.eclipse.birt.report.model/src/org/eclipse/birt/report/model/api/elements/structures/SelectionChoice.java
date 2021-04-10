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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * This class is a structure for selection-list in ScalarParameter item.
 * 
 */

public class SelectionChoice extends Structure {

	/**
	 * Name of the value member.
	 */

	public static final String VALUE_MEMBER = "value"; //$NON-NLS-1$

	/**
	 * Name of the label member.
	 */

	public static final String LABEL_MEMBER = "label"; //$NON-NLS-1$

	/**
	 * Name of the resource key for the label member.
	 */

	public static final String LABEL_RESOURCE_KEY_MEMBER = "labelID"; //$NON-NLS-1$

	/**
	 * Name of this structure within the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "SelectionChoice"; //$NON-NLS-1$

	/**
	 * The value for the choice.
	 */

	protected String value;

	/**
	 * The label for the choice.
	 */

	protected String label = null;

	/**
	 * The resource key of the label for the choice.
	 */

	protected String labelResourceKey = null;

	/**
	 * Constructs a default selection list choice.
	 */

	public SelectionChoice() {
		super();
	}

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
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java.lang.
	 * String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (VALUE_MEMBER.equals(propName))
			return value;
		if (LABEL_MEMBER.equals(propName))
			return label;
		if (LABEL_RESOURCE_KEY_MEMBER.equals(propName))
			return labelResourceKey;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java.lang.
	 * String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (VALUE_MEMBER.equals(propName))
			this.value = (String) value;
		else if (LABEL_MEMBER.equals(propName))
			label = (String) value;
		else if (LABEL_RESOURCE_KEY_MEMBER.equals(propName))
			labelResourceKey = (String) value;
		else
			assert false;

	}

	/**
	 * Returns the value of this choice.
	 * 
	 * @return the value of this choice
	 */

	public String getValue() {
		return (String) getProperty(null, VALUE_MEMBER);
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the value to set
	 */

	public void setValue(String value) {
		setProperty(VALUE_MEMBER, value);
	}

	/**
	 * Returns the label of the choice.
	 * 
	 * @return the label of the choice
	 */

	public String getLabel() {
		return (String) getProperty(null, LABEL_MEMBER);
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the label to set
	 */

	public void setLabel(String label) {
		setProperty(LABEL_MEMBER, label);
	}

	/**
	 * Returns the resource key for the label of the choice.
	 * 
	 * @return the resource key for the label
	 */

	public String getLabelResourceKey() {
		return (String) getProperty(null, LABEL_RESOURCE_KEY_MEMBER);
	}

	/**
	 * Sets the resource key of label.
	 * 
	 * @param labelResourceKey the resource key of label to set
	 */

	public void setLabelResourceKey(String labelResourceKey) {
		setProperty(LABEL_RESOURCE_KEY_MEMBER, labelResourceKey);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.report.
	 * model.api.SimpleValueHandle, int)
	 */
	public StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		return new SelectionChoiceHandle(valueHandle, index);
	}
}
