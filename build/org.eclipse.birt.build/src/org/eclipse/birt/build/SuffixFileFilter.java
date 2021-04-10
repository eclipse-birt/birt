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

public class SuffixFileFilter implements FileFilter {
	private String suffix;
	private String name;

	public SuffixFileFilter(String suffix) {
		this.suffix = suffix;
	}

	public boolean accept(File file) {
		if (file.isDirectory()) {
			return false;
		}

		this.name = file.getName();

		if (this.name.endsWith(this.suffix)) {
			return true;
		} else {
			return false;
		}
	}
}
