/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * 
 */
public class DocumentWriter extends DesignWriter {

	/**
	 * 
	 * @param design
	 */
	public DocumentWriter(ReportDesign design) {
		super(design);
		this.enableLibraryTheme = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.writer.ModuleWriter#write(java.io.File)
	 */
	public void write(File outputFile) throws IOException {
		markLineNumber = false;

		writer = new DocumentXMLWriter(outputFile, getModule().getUTFSignature());
		writeFile();
		writer.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.writer.ModuleWriter#write(java.io.OutputStream)
	 */
	public void write(OutputStream os) throws IOException {
		markLineNumber = false;

		writer = new DocumentXMLWriter(os, getModule().getUTFSignature());
		writeFile();
	}

}
