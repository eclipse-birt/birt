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