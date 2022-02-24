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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.BackRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Cube element. Cube is collection of dimensions and
 * measures. It specifies a dataset to refer to o outside data set element.Use
 * the {@link org.eclipse.birt.report.model.api.olap.CubeHandle}class to change
 * the properties.
 * 
 */

public class TabularCube extends Cube {

	/**
	 * Default constructor.
	 */

	public TabularCube() {
	}

	/**
	 * Constructs a cube element with the given name.
	 * 
	 * @param name the name given for the element
	 */

	public TabularCube(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	public void apply(ElementVisitor visitor) {
		visitor.visitTabularCube(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	public String getElementName() {
		return ReportDesignConstants.TABULAR_CUBE_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.core.IDesignElement#getHandle(org.eclipse
	 * .birt.report.model.core.Module)
	 */

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

	public TabularCubeHandle handle(Module module) {
		if (handle == null) {
			handle = new TabularCubeHandle(module, this);
		}
		return (TabularCubeHandle) handle;
	}

	/**
	 * Sets the measure group at the specified position to be default.
	 * 
	 * @param index
	 */

	public void setDefaultMeasureGroup(int index) {
		List groups = getListProperty(getRoot(), MEASURE_GROUPS_PROP);
		if (groups == null || groups.isEmpty())
			return;
		if (index >= 0 && index < groups.size())
			setProperty(Cube.DEFAULT_MEASURE_GROUP_PROP, new ElementRefValue(null, (DesignElement) groups.get(index)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.olap.Cube#findLocalElement(java
	 * .lang.String, org.eclipse.birt.report.model.api.metadata.IElementDefn)
	 */

	public DesignElement findLocalElement(String name, IElementDefn type) {
		if (StringUtil.isBlank(name) || type == null)
			return null;

		Module root = getRoot();
		if (root == null)
			return null;

		ElementDefn targetDefn = (ElementDefn) type;
		String nameSpaceID = targetDefn.getNameSpaceID();

		NameSpace tmpNS = root.getNameHelper().getNameSpace(nameSpaceID);
		DesignElement tmpSharedElement = tmpNS.getElement(name);

		// tmpSharedElement can be local elements or elements in the shared
		// dimension

		if (tmpSharedElement instanceof TabularHierarchy) {
			Dimension tmpCubeDim = findLocalDimension((Dimension) tmpSharedElement.getContainer());

			if (tmpCubeDim == null)
				return null;

			return tmpCubeDim.getLocalHierarchy(root, name);
		} else if (tmpSharedElement instanceof Dimension) {
			return findLocalDimension((Dimension) tmpSharedElement);
		}
		return null;
	}

	private Dimension findLocalDimension(Dimension sharedDim) {
		if (sharedDim.getContainer() == this)
			return sharedDim;

		Dimension tmpCubeDim = null;
		List<BackRef> cubeDims = sharedDim.getClientList();
		for (int i = 0; i < cubeDims.size(); i++) {
			BackRef cubeDim = cubeDims.get(i);
			if (cubeDim.getElement().getContainer() == this) {
				tmpCubeDim = (Dimension) cubeDim.getElement();
				break;
			}
		}

		return tmpCubeDim;
	}
}
