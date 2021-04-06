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
