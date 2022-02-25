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
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;

/**
 * Represents a group for list of Measure elements.
 */
public class TabularMeasureGroup extends MeasureGroup {

	/**
	 * Default constructor.
	 */

	public TabularMeasureGroup() {
	}

	/**
	 * Constructs measure group with optional name.
	 *
	 * @param name
	 */
	public TabularMeasureGroup(String name) {
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
		visitor.visitTabularMeasureGroup(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	@Override
	public String getElementName() {
		return ReportDesignConstants.TABULAR_MEASURE_GROUP_ELEMENT;
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
	 * @param module the module of the cube
	 *
	 * @return an API handle for this element.
	 */

	public TabularMeasureGroupHandle handle(Module module) {
		if (handle == null) {
			handle = new TabularMeasureGroupHandle(module, this);
		}
		return (TabularMeasureGroupHandle) handle;
	}

}
