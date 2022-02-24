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

package org.eclipse.birt.report.debug.internal.ui.script.launcher.sourcelookup;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.sourcelookup.containers.DirectorySourceContainer;

/**
 * ScriptDirectorySourceContainer
 */
public class ScriptDirectorySourceContainer extends DirectorySourceContainer {

	/**
	 * Constructor
	 *
	 * @param dir
	 * @param subfolders
	 */
	public ScriptDirectorySourceContainer(File dir, boolean subfolders) {
		super(dir, subfolders);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.debug.core.sourcelookup.containers.DirectorySourceContainer#
	 * findSourceElements(java.lang.String)
	 */
	@Override
	public Object[] findSourceElements(String name) throws CoreException {
		// int index = name.lastIndexOf( File.separator );
		int index = name.indexOf(File.separator);
		if (index < 0) {
			return EMPTY;
		}
		String id = name.substring(index + 1);

		String tName = name.substring(0, index);

		ArrayList sources = new ArrayList();
		File directory = getDirectory();
		File file = new File(directory, tName);
		if (file.exists() && file.isFile()) {
			sources.add(new ScriptLocalFileStorage(file, id));
		}

		if (sources.isEmpty()) {
			return EMPTY;
		}
		return sources.toArray();
	}

}
