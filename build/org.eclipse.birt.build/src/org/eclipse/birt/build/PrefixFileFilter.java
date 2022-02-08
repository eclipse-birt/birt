/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.build;

import java.io.File;
import java.io.FileFilter;

public class PrefixFileFilter implements FileFilter {
	private String prefix;
	private String name;

	public PrefixFileFilter(String prefix) {
		this.prefix = prefix;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return false;
		}

		this.name = file.getName();

		int index = this.name.indexOf(this.prefix);
		if (index == -1) {
			return false;
		} else {
			return true;
		}
	}
}
