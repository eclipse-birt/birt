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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.api.DataGroupHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.Module;

/**
 * Class to provide the data group element.
 */

public class DataGroup extends ContentElement {

	/**
	 * Default constructor.
	 */

	public DataGroup() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitDataGroup(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.DATA_GROUP_ELEMENT;
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public DataGroupHandle handle(Module module) {
		return new DataGroupHandle(module, this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */
	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

}
