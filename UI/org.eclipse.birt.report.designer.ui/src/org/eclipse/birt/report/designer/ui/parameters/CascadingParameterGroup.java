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

package org.eclipse.birt.report.designer.ui.parameters;

import org.eclipse.birt.report.model.api.ParameterGroupHandle;

/**
 * Cascading parameter group.
 * 
 */
public class CascadingParameterGroup extends AbstractParameterGroup implements ICascadingParameterGroup {

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */

	public CascadingParameterGroup(ParameterGroupHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.parameter.ICascadingGroup#
	 * getParameter(int)
	 */

	public IParameter getParameter(int index) {
		if (index >= 0 && index < childrenList.size())
			return (IParameter) childrenList.get(index);

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.parameter.ICascadingGroup#
	 * getPostParameter(org.eclipse.birt.report.designer.ui.preview.parameter.
	 * IParameter)
	 */

	public IParameter getPostParameter(IParameter parameter) {
		int index = childrenList.indexOf(parameter);
		return getParameter(++index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.preview.parameter.ICascadingGroup#
	 * getPreParameter(org.eclipse.birt.report.designer.ui.preview.parameter.
	 * IParameter)
	 */

	public IParameter getPreParameter(IParameter parameter) {
		int index = childrenList.indexOf(parameter);
		return getParameter(--index);
	}

}
