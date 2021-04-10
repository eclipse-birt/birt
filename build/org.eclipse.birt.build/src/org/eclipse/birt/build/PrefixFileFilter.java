/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
