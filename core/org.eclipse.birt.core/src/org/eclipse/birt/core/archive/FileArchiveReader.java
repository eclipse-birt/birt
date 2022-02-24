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

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.core.archive.compound.ArchiveReader;

/**
 * file based archive reader. It reads multiple streams from a single physical
 * file. the file is created by FileArchiveWriter.
 */
public class FileArchiveReader extends ArchiveReader {

	/**
	 * @param fileName - the absolute name of the file archive
	 */
	public FileArchiveReader(String fileName) throws IOException {
		super(fileName);
	}

	/**
	 * Explode the existing compound file archive to a folder that contains
	 * corresponding files in it. NOTE: The original file archive will NOT be
	 * deleted. However, if the specified folder archive exists already, its old
	 * content will be totally erased first.
	 *
	 * @param folderArchiveName - the name of the folder archive.
	 * @throws IOException
	 */
	public void expandFileArchive(String folderArchiveName) throws IOException {
		File folder = new File(folderArchiveName);
		folderArchiveName = folder.getCanonicalPath();

		ArchiveUtil.deleteAllFiles(folder); // Clean up the folder if it
		// exists.
		folder.mkdirs(); // Create archive folder

		FolderArchiveWriter writer = new FolderArchiveWriter(folderArchiveName);
		try {
			writer.initialize();
			ArchiveUtil.copy(this, writer);
		} finally {
			writer.finish();
		}
	}
}
