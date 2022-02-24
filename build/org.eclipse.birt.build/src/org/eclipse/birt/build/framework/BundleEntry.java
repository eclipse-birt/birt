/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.build.framework;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

abstract public class BundleEntry {

	protected BundleFile bundleFile;
	protected String name;

	public abstract InputStream getInputStream() throws IOException;

	public abstract int getSize();

	public abstract long getTime();

	public BundleFile getBundleFile() {
		return bundleFile;
	}

	public String getName() {
		return name;
	}

	BundleEntry(BundleFile bundleFile, String name) {
		this.bundleFile = bundleFile;
		this.name = name;
	}

	public String getBundleID() {
		if (bundleFile != null && bundleFile.getBundle() != null) {
			return bundleFile.getBundle().getBundleID();
		}
		return null;
	}

	public String toString() {
		return (getName());
	}

	public byte[] getBytes() throws IOException {
		InputStream in = getInputStream();
		try {

			ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
			byte[] buffer = new byte[4096];
			int size = in.read(buffer);
			while (size > 0) {
				out.write(buffer, 0, size);
				size = in.read(buffer);
			}
			return out.toByteArray();
		} finally {
			in.close();
		}
	}
}
