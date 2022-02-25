/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.toc;

import java.io.IOException;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.archive.RAOutputStream;
import org.eclipse.birt.report.engine.toc.document.MemTOCWriter;
import org.eclipse.birt.report.engine.toc.document.TOCWriterV3;

public class TOCWriter implements ITOCWriter, ITOCConstants {

	private ITOCWriter tocWriter;

	public TOCWriter(IDocArchiveWriter archive) throws IOException {
		if (archive == null) {
			tocWriter = new MemTOCWriter();
		} else {
			tocWriter = new TOCWriterV3(archive.createOutputStream(TOC_STREAM));
		}
	}

	public TOCWriter(RAOutputStream output) throws IOException {
		if (output == null) {
			tocWriter = new MemTOCWriter();
		} else {
			tocWriter = new TOCWriterV3(output);
		}
	}

	@Override
	public void close() throws IOException {
		if (tocWriter != null) {
			tocWriter.close();
			tocWriter = null;
		}

	}

	@Override
	public void closeTOCEntry(TOCEntry entry) throws IOException {
		if (tocWriter != null) {
			tocWriter.closeTOCEntry(entry);
		}
	}

	@Override
	public void startTOCEntry(TOCEntry entry) throws IOException {
		if (tocWriter != null) {
			tocWriter.startTOCEntry(entry);
		}
	}

	@Override
	public ITreeNode getTree() {
		if (tocWriter != null) {
			return tocWriter.getTree();
		}
		return null;
	}
}
