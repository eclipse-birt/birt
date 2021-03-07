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

package org.eclipse.birt.core.archive;

import java.io.IOException;

import org.eclipse.birt.core.archive.compound.ArchiveWriter;

public class FileArchiveWriter extends ArchiveWriter {

	/**
	 * @param absolute fileName the archive file name
	 */
	public FileArchiveWriter(String fileName) throws IOException {
		super(fileName);
	}
}