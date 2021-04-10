/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound;

import java.io.IOException;

public interface IArchiveFileFactory {

	/**
	 * Open the archive or view. The mode can be either: - "r" the archive file is
	 * opened for read only. - "rw" the archive file is opened for read and write. -
	 * "rw+" the archive file is opened for read and append.
	 * 
	 * 1. in "r" mode a. view: open view in r mode, and open archive in r mode at
	 * the same time. b. archive: open archive in r mode directly.
	 * 
	 * 2. in "rw" mode a. view&archive: only create new archive file
	 * 
	 * 3. in "rw+" a. view: open view in rw+ mode, and open archive in r mode at the
	 * same time. b. archive: open archive in rw+ mode.
	 * 
	 * @param archiveId the system id of the opening archive
	 * @param mode      opening mode
	 * @return opened archive
	 * @throws IOException
	 * 
	 * 
	 */
	IArchiveFile openArchive(String archiveId, String mode) throws IOException;

	/**
	 * Open the view with viewId in <code>mode</code> mode, the depend archive file
	 * is opened in r mode, and is shared. The mode can be either: - "r" the view
	 * file is opened for read only. - "rw" the view file is opened for read and
	 * write. - "rw+" the view file is opened for read and append.
	 * 
	 * The depend archive file will not be closed when view file is closed.
	 * 
	 * @param viewId  the system id of the opening view
	 * @param mode    opening mode
	 * @param archive depend archive file
	 * @return opened view
	 * @throws IOException
	 */
	IArchiveFile openView(String viewId, String mode, IArchiveFile archive) throws IOException;

	/**
	 * Create an archive file. The created archive uses <code>archiveId</code> as
	 * the identifier. If the file has exist already, the file is removed first. It
	 * can only be used to create an archive. To create a view, the user needs use
	 * createView.
	 * 
	 * @param archiveId the system id of the new archive file
	 * @param fileName  the file name of the archive file
	 * @return an archive file with the <code>archiveId</code>
	 * @throws IOException
	 */
	IArchiveFile createArchive(String archiveId) throws IOException;

	/**
	 * Create an archive in transient mode.
	 * 
	 * @param archiveId
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	IArchiveFile createTransientArchive(String archiveId) throws IOException;

	/**
	 * Create a view, the view uses "viewId" as the identifier and saved into file
	 * "filename". The base archive "archive" can be either a view or an archive. If
	 * it is a view, the new generated view is based on the original archive.
	 * 
	 * @param viewId  the system id of the new view file
	 * @param archive the depended archive file instance
	 * @return
	 * @throws IOException
	 */
	IArchiveFile createView(String viewId, IArchiveFile archive) throws IOException;

	/**
	 * Create a transient view.
	 * 
	 * @param viewId  the system id of the new view file
	 * @param archive the depended archive file instance
	 * @return
	 * @throws IOException
	 */
	IArchiveFile createTransientView(String viewId, IArchiveFile archive) throws IOException;
}
