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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ContainerSlot;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IParameterGroupModel;

/**
 * This class represents a parameter group. A parameter group creates a visual
 * grouping of parameters. The developer controls the order that groups appear
 * in the UI, and the order in which parameters appear in the group. The
 * BIRT-provided runtime UI will may choose to allow the user to expand &
 * collapse parameter groups independently.
 * 
 */

public class ParameterGroup extends DesignElement implements IParameterGroupModel {

	/**
	 * Default constructor.
	 */

	public ParameterGroup() {
		initSlots();
	}

	/**
	 * Constructs the parameter group with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public ParameterGroup(String theName) {
		super(theName);
		initSlots();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getSlot(int)
	 */

	public ContainerSlot getSlot(int slot) {
		assert slot == PARAMETERS_SLOT;
		return slots[PARAMETERS_SLOT];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitParameterGroup(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.PARAMETER_GROUP_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse.birt.
	 * report.model.elements.ReportDesign)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public ParameterGroupHandle handle(Module module) {
		if (handle == null) {
			handle = new ParameterGroupHandle(module, this);
		}
		return (ParameterGroupHandle) handle;
	}

}