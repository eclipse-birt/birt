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
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Used to group a list of cascading parameters. Each parameter in the group is
 * a scalar parameter, type is "dynamic".
 * 
 */

public class CascadingParameterGroupImpl extends ParameterGroup implements ICascadingParameterGroupModel {

	/**
	 * Default constructor.
	 */

	public CascadingParameterGroupImpl() {
	}

	/**
	 * Constructs the cascading parameter group with an optional name.
	 * 
	 * @param theName the optional name
	 */

	public CascadingParameterGroupImpl(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT;
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module the report design of the report item
	 * 
	 * @return the data set element defined on this cascading parameter group.
	 */

	public DataSet getDataSetElement(Module module) {
		ElementRefValue dataSetRef = (ElementRefValue) getProperty(module, DATA_SET_PROP);
		if (dataSetRef == null)
			return null;
		return (DataSet) dataSetRef.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */

	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

}
