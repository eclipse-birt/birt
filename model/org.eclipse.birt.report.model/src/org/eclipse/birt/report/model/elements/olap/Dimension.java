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
import org.eclipse.birt.report.model.core.ContainerContext;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.core.namespace.DimensionNameHelper;
import org.eclipse.birt.report.model.core.namespace.INameContainer;
import org.eclipse.birt.report.model.core.namespace.INameHelper;
import org.eclipse.birt.report.model.core.namespace.NameExecutor;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.IDimensionModel;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.birt.report.model.elements.strategy.CopyPolicy;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * This class represents a Dimension element. Dimension contains a list of
 * hierarchy elements and a foreign key. Use the
 * {@link org.eclipse.birt.report.model.api.olap.DimensionHandle}class to change
 * the properties.
 * 
 */

public abstract class Dimension extends ReferenceableElement implements IDimensionModel, INameContainer {

	/**
	 * Level name space id.
	 */
	public static final String LEVEL_NAME_SPACE = "dimension-level";

	protected INameHelper nameHelper = null;

	/**
	 * Default constructor.
	 * 
	 */

	public Dimension() {
		nameHelper = new DimensionNameHelper(this);
	}

	/**
	 * Constructs the dimension with the given name.
	 * 
	 * @param name name given for this dimension
	 */

	public Dimension(String name) {
		super(name);
		nameHelper = new DimensionNameHelper(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */
	public void apply(ElementVisitor visitor) {
		visitor.visitDimension(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName() {
		return ReportDesignConstants.DIMENSION_ELEMENT;
	}

	/**
	 * Gets the default hierarchy in this dimension.
	 * 
	 * @param module
	 * @return
	 */
	public DesignElement getDefaultHierarchy(Module module) {
		DesignElement hierarchy = getReferenceProperty(module, DEFAULT_HIERARCHY_PROP);
		// if hierarchy is not set or resolved, or the hierarchy does not reside
		// in this dimension, then return null
		if (hierarchy == null || !isValidHierarchy(hierarchy, module))
			return null;
		return hierarchy;
	}

	/**
	 * @param hierarchy
	 * @return
	 */

	protected boolean isValidHierarchy(DesignElement hierarchy, Module module) {
		return (hierarchy.getContainer() == this);
	}

	/**
	 * Sets the default hierarchy for this dimension.
	 * 
	 * @param defaultHierarchy
	 */
	public void setDefaultHierarchy(Hierarchy defaultHierarchy) {
		setProperty(Dimension.DEFAULT_HIERARCHY_PROP, new ElementRefValue(null, defaultHierarchy));
	}

	/**
	 * Sets the hierarchy at the specified position to be default.
	 * 
	 * @param index
	 */
	public void setDefaultHierarchy(int index) {
		List hierarchies = (List) getLocalProperty(getRoot(), HIERARCHIES_PROP);
		if (hierarchies == null || hierarchies.isEmpty())
			return;
		if (index >= 0 && index < hierarchies.size())
			setProperty(Dimension.DEFAULT_HIERARCHY_PROP,
					new ElementRefValue(null, (DesignElement) hierarchies.get(index)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.ReferenceableElement#doClone(org.eclipse
	 * .birt.report.model.elements.strategy.CopyPolicy)
	 */
	public Object doClone(CopyPolicy policy) throws CloneNotSupportedException {
		DesignElement element = (DesignElement) super.doClone(policy);
		Dimension clonedDimension = (Dimension) element;

		// initialize name helper
		DimensionNameHelper nHelper = new DimensionNameHelper(clonedDimension);
		clonedDimension.nameHelper = nHelper;

		// add all level names to cached name space rather than the real name
		// space in nameContext, the reason is: after clone the dimension, the
		// dimension does not resides in any design tree, after get handle for
		// it, user may add/remove level, so record all level name to make level
		// name unique but not add it to real name space until the dimension is
		// added to the design
		List hierarchies = (List) clonedDimension.getLocalProperty(null, IDimensionModel.HIERARCHIES_PROP);
		if (hierarchies != null) {
			for (int i = 0; i < hierarchies.size(); i++) {
				Hierarchy hierarchy = (Hierarchy) hierarchies.get(i);
				List levels = (List) hierarchy.getLocalProperty(null, IHierarchyModel.LEVELS_PROP);
				if (levels != null) {
					for (int j = 0; j < levels.size(); j++) {
						Level level = (Level) levels.get(j);
						if (level.getName() != null)
							nHelper.addElement(level);
					}
				}
			}
		}

		// adjust default hierarchy as the fixed hierarchy index
		Module module = getRoot();
		DesignElement hierarchy = getDefaultHierarchy(module);
		if (hierarchy != null) {
			int index = hierarchy.getIndex(module);
			DesignElement clonedHierarchy = new ContainerContext(element, HIERARCHIES_PROP)
					.getContent(clonedDimension.getRoot(), index);

			// for cube dimension that refers a shared dimension, at this time,
			// the clonedHierarchy may be null for the layout is not generated
			if (clonedHierarchy != null)
				element.setProperty(DEFAULT_HIERARCHY_PROP, new ElementRefValue(null, clonedHierarchy));
		}
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.namespace.INameContainer#getNameHelper ()
	 */
	public INameHelper getNameHelper() {
		return this.nameHelper;
	}

	public void makeUniqueName(DesignElement element) {
		new NameExecutor(getRoot(), this, element).makeUniqueName();
	}

	public void rename(DesignElement element) {
		new NameExecutor(getRoot(), this, element).rename();
	}

	/**
	 * Gets the default hierarchy in this dimension.
	 * 
	 * @param module
	 * @return
	 */
	public DesignElement getLocalHierarchy(Module module, String hierarchyName) {
		List<DesignElement> hierarchies = (List<DesignElement>) super.getLocalProperty(module,
				Dimension.HIERARCHIES_PROP);

		if (hierarchies == null || hierarchies.isEmpty())
			return null;

		for (int i = 0; i < hierarchies.size(); i++) {
			DesignElement tmpElement = hierarchies.get(i);
			if (hierarchyName.equalsIgnoreCase(tmpElement.getName()))
				return tmpElement;
		}

		return null;
	}
}
