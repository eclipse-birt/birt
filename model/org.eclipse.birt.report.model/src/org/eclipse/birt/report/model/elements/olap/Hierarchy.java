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

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;

/**
 * This class represents a Hierarchy element. Hierarchy contains list of Level
 * elements. It also can define its own dataset which can join with the outer
 * dataset defined on the cube by the foreign key defined on dimension. Use the
 * {@link org.eclipse.birt.report.model.api.olap.HierarchyHandle}class to change
 * the properties.
 *
 */

public abstract class Hierarchy extends ReferenceableElement implements IHierarchyModel {

	/**
	 * Default constructor.
	 */

	public Hierarchy() {
	}

	/**
	 * Constructs the hierarchy with a name.
	 *
	 * @param name name of the hierarchy element
	 */

	public Hierarchy(String name) {
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
		visitor.visitHierarchy(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	@Override
	public String getElementName() {
		return ReportDesignConstants.HIERARCHY_ELEMENT;
	}
}
