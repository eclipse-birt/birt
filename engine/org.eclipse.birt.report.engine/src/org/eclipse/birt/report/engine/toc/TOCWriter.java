/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void close() throws IOException {
		if (tocWriter != null) {
			tocWriter.close();
			tocWriter = null;
		}

	}

	public void closeTOCEntry(TOCEntry entry) throws IOException {
		if (tocWriter != null) {
			tocWriter.closeTOCEntry(entry);
		}
	}

	public void startTOCEntry(TOCEntry entry) throws IOException {
		if (tocWriter != null) {
			tocWriter.startTOCEntry(entry);
		}
	}

	public ITreeNode getTree() {
		if (tocWriter != null) {
			return tocWriter.getTree();
		}
		return null;
	}
}
