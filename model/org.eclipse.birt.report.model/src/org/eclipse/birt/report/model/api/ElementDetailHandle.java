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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Abstract base class for property handles, slot handles, structure handles an
 * other handles that provide detail about a report element.
 */

public abstract class ElementDetailHandle {

	/**
	 * The handle to the report element.
	 */

	protected final DesignElementHandle elementHandle;

	/**
	 * Constructs a detail handle given a handle to a report element.
	 *
	 * @param element a handle to a report element
	 */

	public ElementDetailHandle(DesignElementHandle element) {
		elementHandle = element;
	}

	/**
	 * Returns the report design.
	 *
	 * @return the report design
	 * @deprecated
	 */

	@Deprecated
	public ReportDesign getDesign() {
		return elementHandle.getDesign();
	}

	/**
	 * Returns the module
	 *
	 * @return the module
	 */

	public Module getModule() {
		return elementHandle.getModule();
	}

	/**
	 * Returns the design element.
	 *
	 * @return the design element
	 */

	public DesignElement getElement() {
		return elementHandle.getElement();
	}

	/**
	 * Returns a handle to the design element.
	 *
	 * @return handle to the design element
	 */

	public DesignElementHandle getElementHandle() {
		return elementHandle;
	}
}
