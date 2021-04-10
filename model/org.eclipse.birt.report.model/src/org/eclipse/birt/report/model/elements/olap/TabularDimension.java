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

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.strategy.TabularDimensionPropSearchStrategy;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * This class represents a Dimension element. Dimension contains a list of
 * hierarchy elements and a foreign key. Use the
 * {@link org.eclipse.birt.report.model.api.olap.DimensionHandle}class to change
 * the properties.
 * 
 */

public class TabularDimension extends Dimension implements ITabularDimensionModel {

	protected TabularDimensionProvider provider = null;

	/**
	 * Default constructor.
	 * 
	 */

	public TabularDimension() {
		this(null);
	}

	/**
	 * Constructs the dimension with the given name.
	 * 
	 * @param name name given for this dimension
	 */

	public TabularDimension(String name) {
		super(name);
		cachedPropStrategy = TabularDimensionPropSearchStrategy.getInstance();
		provider = new TabularDimensionProvider(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */
	public void apply(ElementVisitor visitor) {
		visitor.visitTabularDimension(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */
	public String getElementName() {
		return ReportDesignConstants.TABULAR_DIMENSION_ELEMENT;
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
	 * @param module the module of the dimension
	 * 
	 * @return an API handle for this element.
	 */

	public TabularDimensionHandle handle(Module module) {
		if (handle == null) {
			handle = new TabularDimensionHandle(module, this);
		}
		return (TabularDimensionHandle) handle;
	}

	/**
	 * Returns the data set element, if any, for this element.
	 * 
	 * @param module the report design of the report item
	 * 
	 * @return the data set element defined on this specific element
	 */

	public DesignElement getSharedDimension(Module module) {
		ElementRefValue dataSetRef = (ElementRefValue) getProperty(module, INTERNAL_DIMENSION_RFF_TYPE_PROP);
		if (dataSetRef == null)
			return null;
		return dataSetRef.getElement();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.olap.Dimension#isValidHierarchy
	 * (org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.core.Module)
	 */
	protected boolean isValidHierarchy(DesignElement hierarchy, Module module) {
		return hierarchy.getContainer() == this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#isManagedByNameSpace()
	 */
	public boolean isManagedByNameSpace() {
		// if dimension refers a shared dimension whether it is resolved or not,
		// not managed by name space
		if (hasSharedDimension(getRoot()))
			return false;
		return super.isManagedByNameSpace();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#baseClone()
	 */

	protected Object baseClone() throws CloneNotSupportedException {
		TabularDimension clonedElement = (TabularDimension) super.baseClone();
		clonedElement.provider = new TabularDimensionProvider(clonedElement);
		clonedElement.provider.updateLayout(clonedElement.getRoot());

		return clonedElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getLocalProperty(org
	 * .eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	public Object getLocalProperty(Module module, ElementPropertyDefn prop) {
		assert prop != null;

		if (prop.getTypeCode() != IPropertyType.ELEMENT_TYPE)
			return super.getLocalProperty(module, prop);

		if (hasSharedDimension(module))
			return provider.getLayoutProperty(module, prop);

		return super.getLocalProperty(module, prop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#setProperty(org.eclipse
	 * .birt.report.model.metadata.ElementPropertyDefn, java.lang.Object)
	 */
	public void setProperty(ElementPropertyDefn prop, Object value) {
		assert prop != null;
		if (prop.getTypeCode() == IPropertyType.ELEMENT_TYPE && hasSharedDimension(getRoot())) {
			provider.setLayoutProperty(prop, value);
		} else {
			if (INTERNAL_DIMENSION_RFF_TYPE_PROP.equals(prop.getName())) {
				propValues.put(HIERARCHIES_PROP, null);
				setProperty(DEFAULT_HIERARCHY_PROP, null);
			}
			super.setProperty(prop, value);
		}
	}

	/**
	 * 
	 * @param module
	 */
	public void updateLayout(Module module) {
		provider.updateLayout(module);
	}

	public boolean hasSharedDimension(Module module) {
		return getLocalProperty(module, INTERNAL_DIMENSION_RFF_TYPE_PROP) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#getDynamicExtends(org
	 * .eclipse.birt.report.model.core.Module)
	 */
	public DesignElement getDynamicExtendsElement(Module module) {
		return getSharedDimension(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.DesignElement#canDynamicExtends()
	 */
	public boolean canDynamicExtends() {
		return hasSharedDimension(getRoot());
	}

	/**
	 * Gets the default hierarchy in this dimension.
	 * 
	 * @param module
	 * @return
	 */
	public DesignElement getDefaultHierarchy(Module module) {
		if (hasSharedDimension(module)) {
			String name = getStringProperty(module, DEFAULT_HIERARCHY_PROP);
			return provider.findLocalElement(name,
					MetaDataDictionary.getInstance().getElement(ReportDesignConstants.HIERARCHY_ELEMENT));
		}

		return super.getDefaultHierarchy(module);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.olap.Dimension#getLocalHierarchy
	 * (org.eclipse.birt.report.model.core.Module, java.lang.String)
	 */

	public DesignElement getLocalHierarchy(Module module, String hierarchyName) {
		if (hasSharedDimension(module)) {
			return provider.findLocalElement(hierarchyName,
					MetaDataDictionary.getInstance().getElement(ReportDesignConstants.HIERARCHY_ELEMENT));
		}

		return super.getLocalHierarchy(module, hierarchyName);
	}
}
