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

package org.eclipse.birt.report.model.api.olap;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.ITabularDimensionModel;
import org.eclipse.birt.report.model.elements.olap.TabularDimension;

/**
 * Represents a dimension element in the cube element.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Dimension
 */

public class TabularDimensionHandle extends DimensionHandle implements ITabularDimensionModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public TabularDimensionHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Checks if this dimension is time type.
	 */
	@Override
	public boolean isTimeType() {
		DimensionHandle sharedDimension = getSharedDimension();
		if (sharedDimension == null) {
			return getBooleanProperty(IS_TIME_TYPE_PROP);
		} else {
			return sharedDimension.isTimeType();
		}
	}

	/**
	 * Returns the dimension object that this dimension refers to.
	 *
	 * @return the dimension object
	 */

	public DimensionHandle getSharedDimension() {
		DesignElement refDim = ((TabularDimension) getElement()).getSharedDimension(module);
		if (refDim == null) {
			return null;
		}

		return (DimensionHandle) refDim.getHandle(refDim.getRoot());
	}

	/**
	 * Sets the dimension object this dimension refers to.
	 *
	 * @param handle the dimension object
	 *
	 * @throws SemanticException if the property is locked, or the dimension object
	 *                           is invalid
	 */

	public void setSharedDimension(DimensionHandle handle) throws SemanticException {
		if (handle == null) {
			setStringProperty(INTERNAL_DIMENSION_RFF_TYPE_PROP, null);
		} else {
			setProperty(INTERNAL_DIMENSION_RFF_TYPE_PROP, handle);
		}
	}

}
