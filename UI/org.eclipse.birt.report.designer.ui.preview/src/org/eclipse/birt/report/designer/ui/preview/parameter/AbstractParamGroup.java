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

package org.eclipse.birt.report.designer.ui.preview.parameter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.ParameterGroupHandle;

/**
 * Abstract parameter group.
 */

public abstract class AbstractParamGroup implements IParamGroup {
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

	public AbstractParamGroup(ParameterGroupHandle handle) {
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
