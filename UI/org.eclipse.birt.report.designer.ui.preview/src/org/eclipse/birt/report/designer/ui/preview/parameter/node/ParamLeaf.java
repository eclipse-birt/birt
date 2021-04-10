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

package org.eclipse.birt.report.designer.ui.preview.parameter.node;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.preview.parameter.IParameter;

/**
 * Parameter node.
 * 
 */

public class ParamLeaf implements IParamNode {

	private IParameter parameter = null;

	/**
	 * Constructor
	 * 
	 * @param parameter
	 */

	public ParamLeaf(IParameter parameter) {
		this.parameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#format(java.lang
	 * .String)
	 */

	public String format(String input) throws BirtException {
		return parameter.format(input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#getChildren()
	 */

	public List getChildren() {
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#getValueList()
	 */

	public List getValueList() {
		return parameter.getValueList();
	}

}
