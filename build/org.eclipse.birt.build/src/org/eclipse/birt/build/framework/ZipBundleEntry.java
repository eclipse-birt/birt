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

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;

public class ZipBundleEntry extends BundleEntry {

	protected final ZipEntry zipEntry;

	ZipBundleEntry(ZipBundleFile bundleFile, ZipEntry zipEntry) {
		super(bundleFile, zipEntry.getName());
		this.zipEntry = zipEntry;
	}

	/**
	 * Return an InputStream for the entry.
	 *
	 * @return InputStream for the entry
	 * @exception java.io.IOException
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		return ((ZipBundleFile) bundleFile).getZipFile().getInputStream(zipEntry);
	}

	@Override
	public int getSize() {
		return (int) zipEntry.getSize();
	}

	@Override
	public long getTime() {
		return zipEntry.getTime();
	}

}
