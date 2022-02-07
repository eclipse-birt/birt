/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import org.eclipse.birt.report.designer.internal.ui.extension.IEditputProvider;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.FileEditorInput;

/**
 * 
 */

public class IDEEditputProvider implements IEditputProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actuate.birt.report.designer.ui.datamart.wizards.IEditputProvider#
	 * createEditorInput(java.lang.Object)
	 */
	public IEditorInput createEditorInput(Object file) {
		if (file instanceof File) {
			File handle = (File) file;
			String fileName = handle.getAbsolutePath();

			IWorkspace space = ResourcesPlugin.getWorkspace();
			IWorkspaceRoot root = space.getRoot();
			try {
				// IFile[] resources = root.findFilesForLocationURI( new URL("file:///" +
				// fileName ).toURI( ) ); //$NON-NLS-1$
				IFile[] resources = root.findFilesForLocationURI(new File(fileName).toURI()); // $NON-NLS-1$
				if (resources != null && resources.length > 0) {
					IEditorInput input = new FileEditorInput(resources[0]);
					return input;
				} else {
					IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(fileName));
					IFileInfo fetchInfo = fileStore.fetchInfo();
					if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
						return new FileStoreEditorInput(fileStore);
					}
				}
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
