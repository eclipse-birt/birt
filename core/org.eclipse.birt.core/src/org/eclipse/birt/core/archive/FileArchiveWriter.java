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
