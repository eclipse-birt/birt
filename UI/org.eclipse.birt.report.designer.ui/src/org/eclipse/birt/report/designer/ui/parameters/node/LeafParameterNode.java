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

package org.eclipse.birt.report.designer.ui.parameters.node;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.parameters.IParameter;

/**
 * Parameter node.
 *
 */

public class LeafParameterNode implements IParameterNode {

	private IParameter parameter = null;

	/**
	 * Constructor
	 *
	 * @param parameter
	 */

	public LeafParameterNode(IParameter parameter) {
		this.parameter = parameter;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#format(java.lang
	 * .String)
	 */

	@Override
	public String format(String input) throws BirtException {
		return parameter.format(input);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#getChildren()
	 */

	@Override
	public List getChildren() {
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.parameter.node.IParamNode#getValueList()
	 */

	@Override
	public List getValueList() {
		return parameter.getValueList();
	}

}
