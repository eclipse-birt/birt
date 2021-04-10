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

package org.eclipse.birt.report.model.elements.olap;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;

/**
 * This class represents a Measure element. Measure is a quantity that you are
 * interested in measures.Use the
 * {@link org.eclipse.birt.report.model.api.olap.MeasureHandle}class to change
 * the properties.
 * 
 */

public abstract class Measure extends ReferenceableElement implements IMeasureModel {

	/**
	 * Default constructor.
	 */

	public Measure() {

	}

	/**
	 * Constructs the measure element with a given name.
	 * 
	 * @param name the optional name of the measure element
	 */

	public Measure(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt.
	 * report.model.elements.ElementVisitor)
	 */
	public void apply(ElementVisitor visitor) {
		visitor.visitMeasure(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName() {
		return ReportDesignConstants.MEASURE_ELEMENT;
	}
}
