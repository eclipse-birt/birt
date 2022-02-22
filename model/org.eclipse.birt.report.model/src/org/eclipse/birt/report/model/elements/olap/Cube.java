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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Cube element. Cube is collection of dimensions and
 * measures. It specifies a dataset to refer to o outside data set element.Use
 * the {@link org.eclipse.birt.report.model.api.olap.CubeHandle}class to change
 * the properties.
 *
 */

public abstract class Cube extends ReferenceableElement implements ICubeModel {

	/**
	 * Default constructor.
	 */

	public Cube() {
	}

	/**
	 * Constructs a cube element with the given name.
	 *
	 * @param name the name given for the element
	 */

	public Cube(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitCube(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.CUBE_ELEMENT;
	}

	/**
	 * Gets the default measure group in this cube. If the measure group is not set,
	 * the first available measure group is treated as the default measure group.
	 *
	 * @param module the root design/library
	 * @return the default measure group
	 */

	public DesignElement getDefaultMeasureGroup(Module module) {
		DesignElement measureGroup = getReferenceProperty(module, DEFAULT_MEASURE_GROUP_PROP);

		if (measureGroup != null) {
			// if measure group is not set or resolved, or the group does not
			// reside in this cube, then return null

			if (measureGroup.getContainer() != this) {
				return null;
			}
			return measureGroup;
		}

		List groups = getListProperty(module, MEASURE_GROUPS_PROP);
		if (groups == null || groups.isEmpty()) {
			return null;
		}

		return (DesignElement) groups.get(0);
	}

	/**
	 * Sets the default measure group for this cube.
	 *
	 * @param defaultMeasureGroup
	 */
	public void setDefaultMeasureGroup(MeasureGroup defaultMeasureGroup) {
		setProperty(ICubeModel.DEFAULT_MEASURE_GROUP_PROP, new ElementRefValue(null, defaultMeasureGroup));
	}

	public DesignElement findLocalElement(String name, IElementDefn type) {
		return null;
	}

	/**
	 *
	 * @param module
	 */
	public void updateLayout(Module module) {
		// do nothing
	}

	public boolean isBoundWithLayout(ElementPropertyDefn propDefn) {
		return false;
	}
}
