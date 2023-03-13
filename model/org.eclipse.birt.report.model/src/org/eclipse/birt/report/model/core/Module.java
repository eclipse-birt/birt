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

package org.eclipse.birt.report.model.core;

/**
 * Core representation of the module element. The module contains:
 * <ul>
 * <li>the name spaces for various kinds of elements
 * <li>a set of slots defined
 * <li>the library list
 * </ul>
 * by the derived class.
 * <p>
 * The Design Engine may be used in the web environment in which it is necessary
 * to identify elements using a unique ID separate from their object pointer.
 * The root class maintains the object ID counter, as well as an id-to-element
 * map. Because the map is costly, it is enabled only if ID support is enabled
 * in the data dictionary.
 */

public abstract class Module extends ModuleImpl {

	/**
	 * Default constructor.
	 *
	 * @param theSession the session of the report
	 */

	protected Module(DesignSessionImpl theSession) {
		super(theSession);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.ModuleImpl#getModule()
	 */
	@Override
	protected Module getModule() {
		return this;
	}
}
