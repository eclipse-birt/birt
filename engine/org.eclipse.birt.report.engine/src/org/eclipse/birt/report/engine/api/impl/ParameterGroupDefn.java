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

package org.eclipse.birt.report.engine.api.impl;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.IParameterDefnBase;
import org.eclipse.birt.report.engine.api.IParameterGroupDefn;
import org.eclipse.birt.report.engine.i18n.MessageConstants;

/**
 * <code>ParameterGroupDefn</code> is an concrete subclass of
 * <code>ReportElementDesign</code> that implements the interface
 * <code>IParameterGroupDefn</code>. It is used to visually group report
 * parameters.
 *
 */
public class ParameterGroupDefn extends ParameterDefnBase implements IParameterGroupDefn {

	protected ArrayList contents = new ArrayList();

	public void addParameter(IParameterDefnBase param) {
		contents.add(param);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api2.IParameterGroupDefn#getContents()
	 */
	@Override
	public ArrayList getContents() {
		return contents;
	}

	public void setContents(ArrayList list) {
		contents = list;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		ParameterGroupDefn newParam = (ParameterGroupDefn) super.clone();
		ArrayList list = newParam.getContents();
		if (list == null) {
			return newParam;
		}

		ArrayList newList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object parameterDefn = list.get(i);
			if (parameterDefn instanceof ParameterDefn) {
				ParameterDefn p = (ParameterDefn) parameterDefn;
				newList.add(p.clone());
			} else {
				throw new CloneNotSupportedException(MessageConstants.PARAMETER_TYPE_IS_INVALID_EXCEPTION);
			}
		}
		newParam.setContents(newList);
		return newParam;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api2.IParameterGroupDefn#displayExpanded()
	 */
	@Override
	public boolean displayExpanded() {
		return true;
	}
}
