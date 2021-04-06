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

package org.eclipse.birt.report.model.elements.strategy;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.PropertySearchStrategy;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.Dimension;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Provides the specific property searching route for <code>ReportItem</code>,
 * especially about how to apply CSS rules on <code>ReportItem</code>.
 */

public class TabularDimensionPropSearchStrategy extends PropertySearchStrategy {

	private final static TabularDimensionPropSearchStrategy instance = new TabularDimensionPropSearchStrategy();

	/**
	 * Protected constructor.
	 */

	protected TabularDimensionPropSearchStrategy() {
	}

	/**
	 * Returns the instance of <code>ReportItemPropSearchStrategy</code> which
	 * provide the specific property searching route for <code>ReportItem</code> .
	 * 
	 * @return the instance of <code>ReportItemPropSearchStrategy</code>
	 */

	public static PropertySearchStrategy getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.PropertySearchStrategy#getPropertyFromSelf
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement,
	 * org.eclipse.birt.report.model.metadata.ElementPropertyDefn)
	 */

	protected Object getPropertyFromSelf(Module module, DesignElement element, ElementPropertyDefn prop) {
		if (element.getContainer() instanceof Module)
			return super.getPropertyFromSelf(module, element, prop);

		if (ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP.equalsIgnoreCase(prop.getName())
				|| prop.getTypeCode() == IPropertyType.ELEMENT_TYPE)
			return super.getPropertyFromSelf(module, element, prop);

		Dimension tmpDimension = getSharedDimension(module, element);
		if (tmpDimension == null || !(tmpDimension.getContainer() instanceof Module))
			return super.getPropertyFromSelf(module, element, prop);

		// if there is the shared dimension. Properties on shared dimension
		// should be transparent to the given dimension.

		return super.getPropertyFromSelf(module, tmpDimension, prop);
	}

	/**
	 * Checks if the property is data binding property.
	 * 
	 * @param root
	 * 
	 * @param element  the design element
	 * @param propName the property name
	 * @return true if this property is the data binding property, false otherwise
	 */

	public static Dimension getSharedDimension(Module root, DesignElement element) {
		if (!(element instanceof TabularDimension))
			return null;

		ElementRefValue tmpRef = (ElementRefValue) element.getLocalProperty(root,
				ITabularDimensionModel.INTERNAL_DIMENSION_RFF_TYPE_PROP);

		if (tmpRef == null)
			return null;

		return (Dimension) tmpRef.getElement();
	}
}
