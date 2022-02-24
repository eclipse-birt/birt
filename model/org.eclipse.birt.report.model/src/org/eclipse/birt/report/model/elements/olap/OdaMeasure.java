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
import org.eclipse.birt.report.model.api.olap.OdaMeasureHandle;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;

/**
 * This class represents a Measure element. Measure is a quantity that you are
 * interested in measures.Use the
 * {@link org.eclipse.birt.report.model.api.olap.MeasureHandle}class to change
 * the properties.
 * 
 */

public class OdaMeasure extends Measure {

	/**
	 * Default constructor.
	 */

	public OdaMeasure() {

	}

	/**
	 * Constructs the measure element with a given name.
	 * 
	 * @param name the optional name of the measure element
	 */

	public OdaMeasure(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */
	public void apply(ElementVisitor visitor) {
		visitor.visitOdaMeasure(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName() {
		return ReportDesignConstants.ODA_MEASURE_ELEMENT;
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

	/**
	 * Returns an API handle for this element.
	 * 
	 * @param module the module of the measure
	 * 
	 * @return an API handle for this element.
	 */

	public OdaMeasureHandle handle(Module module) {
		if (handle == null) {
			handle = new OdaMeasureHandle(module, this);
		}
		return (OdaMeasureHandle) handle;
	}
}
