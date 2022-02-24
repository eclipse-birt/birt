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

package org.eclipse.birt.report.designer.ui.parameters;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ParameterGroupHandle;

/**
 * Abstract parameter group.
 */

public abstract class AbstractParameterGroup implements IParameterGroup {

	/**
	 * Children Parameter list.
	 */

	protected List childrenList = new ArrayList();

	/**
	 * Parameter group handle
	 */
	protected ParameterGroupHandle handle;

	/**
	 * Constructor
	 * 
	 * @param handle
	 * @param engineTask
	 */

	public AbstractParameterGroup(ParameterGroupHandle handle) {
		this.handle = handle;
	}

	/**
	 * Adds parameter.
	 * 
	 * @param parameter
	 */

	public void addParameter(IParameter parameter) {
		childrenList.add(parameter);
		parameter.setParentGroup(this);
	}

	/**
	 * Returns parameter group handle.
	 * 
	 * @return parameter group handle.
	 */

	public ParameterGroupHandle getHandle() {
		return handle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.preview.parameter.IParameter#getChildren(
	 * )
	 */

	public List getChildren() {
		return childrenList;
	}

}
