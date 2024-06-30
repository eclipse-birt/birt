/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.pkg;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Entry inside an ODF package.
 *
 * @see Package
 */
public class PackageEntry {
	private Package pkg;
	private String uri;
	private boolean cached;
	protected String contentType;

	private OutputStream out;

	PackageEntry(Package pkg, String uri, String contentType, boolean cached) {
		this.uri = uri;
		this.pkg = pkg;
		this.cached = cached;
		this.contentType = contentType;
	}

	public Package getPackage() {
		return pkg;
	}

	public OutputStream getOutputStream() throws IOException {
		if (out == null) {
			if (cached) {
				out = pkg.getCachedOutputStream(uri);
			} else {
				out = pkg.getEntryOutputStream(uri);
			}
		}
		return out;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	public String getFullPath() {
		return uri;
	}

	public String getContentType() {
		return contentType;
	}

	/**
	 * @return the cached
	 */
	public boolean isCached() {
		return cached;
	}

}
