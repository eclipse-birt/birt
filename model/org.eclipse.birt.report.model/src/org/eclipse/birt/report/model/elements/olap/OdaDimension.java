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

package org.eclipse.birt.report.model.elements.olap;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.olap.OdaDimensionHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;

/**
 * This class represents a Dimension element. Dimension contains a list of
 * hierarchy elements and a foreign key. Use the
 * {@link org.eclipse.birt.report.model.api.olap.DimensionHandle}class to change
 * the properties.
 *
 */

public class OdaDimension extends Dimension {

	/**
	 * Default constructor.
	 *
	 */

	public OdaDimension() {
	}

	/**
	 * Constructs the dimension with the given name.
	 *
	 * @param name name given for this dimension
	 */

	public OdaDimension(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */
	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitOdaDimension(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	@Override
	public String getElementName() {
		return ReportDesignConstants.ODA_DIMENSION_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse.
	 * birt.report.model.core.Module)
	 */
	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the module of the dimension
	 *
	 * @return an API handle for this element.
	 */

	public OdaDimensionHandle handle(Module module) {
		if (handle == null) {
			handle = new OdaDimensionHandle(module, this);
		}
		return (OdaDimensionHandle) handle;
	}
}
