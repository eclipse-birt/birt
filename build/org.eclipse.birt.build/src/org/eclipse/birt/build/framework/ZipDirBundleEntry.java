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

import java.io.InputStream;

public class ZipDirBundleEntry extends BundleEntry {

	protected ZipBundleFile zipBundleFile;

	protected ZipDirBundleEntry(ZipBundleFile bundleFile, String name) {
		super(bundleFile, name);
		this.zipBundleFile = bundleFile;
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public long getTime() {
		return 0;
	}
}
