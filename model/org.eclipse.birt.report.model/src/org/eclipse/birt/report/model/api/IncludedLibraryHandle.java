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

import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;

/**
 * Represents the handle of the included library.
 * 
 * <p>
 * <dl>
 * <dt><strong>File Name </strong></dt>
 * <dd>File name is required for an include library.</dd>
 * <dt><strong>Namespace</strong></dt>
 * <dd>Namespace is required for an include library.</dd>
 * </dl>
 * 
 */

public class IncludedLibraryHandle extends StructureHandle {

	/**
	 * Constructs the handle of the included library.
	 * 
	 * @param valueHandle the value handle for the included library list of one
	 *                    property
	 * @param index       the position of this included library in the list
	 */

	public IncludedLibraryHandle(SimpleValueHandle valueHandle, int index) {
		super(valueHandle, index);
	}

	/**
	 * Gets the file name of the include library.
	 * 
	 * @return the file name of the include library
	 */

	public String getFileName() {
		return getStringProperty(IncludedLibrary.FILE_NAME_MEMBER);
	}

	/**
	 * Returns the namespace of the included library. The namespace identify one
	 * library uniquely in design file.
	 * 
	 * @return the namespace of the included library.
	 */

	public String getNamespace() {
		return getStringProperty(IncludedLibrary.NAMESPACE_MEMEBR);
	}
}
