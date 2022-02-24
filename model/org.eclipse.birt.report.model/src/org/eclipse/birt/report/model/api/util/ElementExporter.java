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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.api.LibraryHandle;

/**
 * Exports elements or structures to library. This class contains the handle for
 * target library and encapsulates the main logicas for exporting.
 */

class ElementExporter extends ElementExporterImpl {

	/**
	 * Constructs the exporter with the handle of target library.
	 * 
	 * @param libraryHandle handle of the target library
	 */

	ElementExporter(LibraryHandle libraryHandle) {
		super(libraryHandle);
	}
}
