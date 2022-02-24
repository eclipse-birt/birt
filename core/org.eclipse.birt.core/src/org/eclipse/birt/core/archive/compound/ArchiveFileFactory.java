/*******************************************************************************
 * Copyright (c) 2008,2009 Actuate Corporation.
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

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

public class ArchiveFileFactory implements IArchiveFileFactory {

	public IArchiveFile createArchive(String archiveId) throws IOException {
		String fileName = getPhysicalFile(archiveId);
		return doCreateArchive(archiveId, fileName, "rw");
	}

	public IArchiveFile createTransientArchive(String archiveId) throws IOException {
		String fileName = getPhysicalFile(archiveId);
		return doCreateArchive(archiveId, fileName, "rwt");
	}

	private IArchiveFile doCreateArchive(String archiveId, String fileName, String mode) throws IOException {
		ArchiveFileV3 af = new ArchiveFileV3(fileName, mode);
		af.setSystemId(archiveId);
		return af;
	}

	public IArchiveFile createView(String viewId, IArchiveFile archive) throws IOException {
		String fileName = getPhysicalFile(viewId);
		return doCreateView(fileName, viewId, archive, "rw");
	}

	public IArchiveFile createTransientView(String viewId, IArchiveFile archive) throws IOException {
		String fileName = getPhysicalFile(viewId);
		return doCreateView(fileName, viewId, archive, "rwt");
	}

	private IArchiveFile doCreateView(String viewId, String fileName, IArchiveFile archive, String mode)
			throws IOException {
		ArchiveFileV3 view = new ArchiveFileV3(fileName, mode);
		view.setSystemId(viewId);
		view.setDependId(archive.getSystemId());
		return new ArchiveView(view, archive, true);
	}

	/*
	 * Open the archive with <code>archiveId</code> in <code>mode</code>. The
	 * <code>mode</code> could be: - r read - rw read & write (Here should first
	 * create a new file) - rw+ read & append
	 * 
	 * 1. in "r" mode a. view: open view in r mode, and open archive in r mode at
	 * the same time. b. archive: open archive in r mode directly.
	 * 
	 * 2. in "rw" mode a. view: create new view file, so no depend file exists. b.
	 * the same as above.
	 * 
	 * 3. in "rw+" a. view: open view in rw+ mode, and open archive in r mode at the
	 * same time. b. archive: open archive in rw+ mode.
	 */
	public IArchiveFile openArchive(String archiveId, String mode) throws IOException {
		String fileName = getPhysicalFile(archiveId);
		ArchiveFile file = new ArchiveFile(fileName, archiveId, mode);
		String dependId = file.getDependId();
		if (dependId != null && dependId.length() > 0) {
			IArchiveFile archive = openArchive(dependId, "r");
			return new ArchiveView(file, archive, false);
		}
		return file;
	}

	public IArchiveFile openView(String viewId, String mode, IArchiveFile archive) throws IOException {
		String fileName = getPhysicalFile(viewId);
		ArchiveFile view = new ArchiveFile(fileName, viewId, mode);
		return new ArchiveView(view, archive, true);
	}

	/**
	 * The sub class should override this method to implement its own systemId
	 * resolver.
	 * 
	 * @param systemId
	 * @return the physical file name
	 */
	protected String getPhysicalFile(String systemId) {
		return systemId;
	}
}
