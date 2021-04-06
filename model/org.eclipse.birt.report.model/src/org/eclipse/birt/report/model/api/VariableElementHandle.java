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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.command.ContentElementInfo;
import org.eclipse.birt.report.model.command.NameCommand;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.VariableElement;
import org.eclipse.birt.report.model.elements.interfaces.IVariableElementModel;

/**
 * Represents a variable.
 */

public class VariableElementHandle extends ContentElementHandle implements IVariableElementModel {

	/**
	 * Constructs a variable handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 * 
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public VariableElementHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the name of the variable.
	 * 
	 * @return the variable name
	 * 
	 */

	public String getVariableName() {
		return getName();
	}

	/**
	 * Sets the name of the variable.
	 * 
	 * @param name the name to set
	 * 
	 * @throws SemanticException
	 * 
	 * @see #getVariableName()
	 */

	public void setVariableName(String name) throws SemanticException {
		setName(name);
	}

	/**
	 * Returns the value of the variable.
	 * 
	 * 
	 * @return the variable value
	 */

	public String getValue() {
		return getStringProperty(VALUE_PROP);
	}

	/**
	 * Sets the value of the variable.
	 * 
	 * @param value the value to set
	 * @throws SemanticException
	 * 
	 * @Deprecated by getExpressionProperty(VALUE_PROP)
	 */

	public void setValue(String value) throws SemanticException {
		setStringProperty(VALUE_PROP, value);
	}

	/**
	 * Gets the value of work mode property.The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>VARIABLE_TYPE_REPORT</code>
	 * <li><code>VARIABLE_TYPE_PAGE</code>
	 * </ul>
	 * 
	 * 
	 * @return the work mode property value.
	 */
	public String getType() {
		return getStringProperty(TYPE_PROP);
	}

	/**
	 * Sets the value of work mode property. The value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 * <ul>
	 * <li><code>VARIABLE_TYPE_REPORT</code>
	 * <li><code>VARIABLE_TYPE_PAGE</code>
	 * </ul>
	 * 
	 * @param workMode the work mode property value.
	 * @throws SemanticException
	 */
	public void setType(String workMode) throws SemanticException {
		setStringProperty(TYPE_PROP, workMode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ContentElementHandle#getName()
	 */
	public String getName() {
		return element.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.ContentElementHandle#setName(java.lang
	 * .String)
	 */
	public void setName(String name) throws NameException {
		// use valuecontainer's module so the variable element can make a local copy if
		// its name is changed
		ContentElementInfo valueContainer = ((VariableElement) element).getValueContainer();
		NameCommand cmd = null;
		if (((VariableElement) element).isLocal()) {
			cmd = new NameCommand(module, getElement());
		} else {
			cmd = new NameCommand(valueContainer.getElement().getRoot(), getElement());
		}
		cmd.setName(name);
	}
}
