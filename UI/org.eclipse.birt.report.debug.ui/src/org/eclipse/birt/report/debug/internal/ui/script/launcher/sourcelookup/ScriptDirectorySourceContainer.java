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

		if (sources.isEmpty())
			return EMPTY;
		return sources.toArray();
	}

}
