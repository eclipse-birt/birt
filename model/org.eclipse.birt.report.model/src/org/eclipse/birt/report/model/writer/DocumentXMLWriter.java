/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.model.util.XMLWriter;

/**
 *
 */
public class DocumentXMLWriter extends XMLWriter {

	/**
	 *
	 * @param outputFile
	 * @param signature
	 * @throws IOException
	 */
	public DocumentXMLWriter(File outputFile, String signature) throws IOException {
		super(outputFile, signature);
		markLineNumber = false;
	}

	/**
	 *
	 * @param os
	 * @param signature
	 * @throws IOException
	 */
	public DocumentXMLWriter(OutputStream os, String signature) throws IOException {
		super(os, signature);
		markLineNumber = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.XMLWriter#checkAttribute()
	 */
	@Override
	protected void checkAttribute() {
		// Write any conditional elements waiting for content. If we get
		// here, we're about to write an attribute, so the elements do
		// have content.

		flushPendingElements();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.XMLWriter#doPrintLine()
	 */
	@Override
	protected void doPrintLine() {
		out.print('\n');
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.XMLWriter#printLine()
	 */
	@Override
	protected void printLine() {
		// do nothing
	}
}
