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

package org.eclipse.birt.report.model.writer;

import org.eclipse.birt.report.model.elements.Library;

/**
 * Represents the writer for writing library file.
 */

public class LibraryWriter extends LibraryWriterImpl {

	/**
	 * Contructs one library writer with the library instance.
	 *
	 * @param library the library to write
	 */

	public LibraryWriter(Library library) {
		this.library = library;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTheme(org.
	 * eclipse.birt.report.model.elements.Theme)
	 */
}
