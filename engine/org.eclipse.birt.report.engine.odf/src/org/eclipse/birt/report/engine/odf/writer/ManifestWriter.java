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
package org.eclipse.birt.report.engine.odf.writer;

import java.io.OutputStream;

import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.odf.pkg.PackageEntry;

@SuppressWarnings("nls")
/**
 * Writer for the manifest file, containing the list of files available in an
 * ODF package, including their MIME-type.
 */
public class ManifestWriter {
	protected XMLWriter writer;

	public ManifestWriter(OutputStream out) {
		writer = new XMLWriter();
		writer.open(out);
	}

	public void start(String rootMime) {
		writer.startWriter();
		writer.openTag("manifest:manifest");
		writer.attribute("xmlns:manifest", "urn:oasis:names:tc:opendocument:xmlns:manifest:1.0");

		writeRootEntry(rootMime);
	}

	private void writeRootEntry(String rootMime) {
		writer.openTag("manifest:file-entry");
		writer.attribute("manifest:media-type", rootMime);
		writer.attribute("manifest:version", "1.2");
		writer.attribute("manifest:full-path", "/");
		writer.closeTag("manifest:file-entry");
	}

	public void writeEntry(PackageEntry entry) {
		String mediaType = entry.getContentType();
		if (mediaType == null) {
			mediaType = "";
		}
		writer.openTag("manifest:file-entry");
		writer.attribute("manifest:media-type", mediaType);
		writer.attribute("manifest:full-path", entry.getFullPath());
		writer.closeTag("manifest:file-entry");
	}

	public void end() {
		writer.closeTag("manifest:manifest");
		writer.close();
	}

}
