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
import org.eclipse.birt.report.model.elements.interfaces.IOdaOlapElementModel;

/**
 * Represents a dimension element in the cube element.
 *
 * @see org.eclipse.birt.report.model.elements.olap.Dimension
 */

public class OdaDimensionHandle extends DimensionHandle implements IOdaOlapElementModel {

	/**
	 * Constructs a handle for the given design and design element. The application
	 * generally does not create handles directly. Instead, it uses one of the
	 * navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public OdaDimensionHandle(Module module, DesignElement element) {
		super(module, element);
	}

	/**
	 * Returns the ODA defined cube name.
	 *
	 * @return the ODA defined cube name
	 */

	public String getNativeName() {
		return getStringProperty(NATIVE_NAME_PROP);
	}

	/**
	 * Sets the ODA defined cube name.
	 *
	 * @param nativeName the ODA defined cube name
	 *
	 * @throws SemanticException
	 */

	public void setNativeName(String nativeName) throws SemanticException {
		setProperty(NATIVE_NAME_PROP, nativeName);
	}
}
