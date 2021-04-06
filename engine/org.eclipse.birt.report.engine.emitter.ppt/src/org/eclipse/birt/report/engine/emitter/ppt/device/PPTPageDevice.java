/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt.device;

import java.awt.Color;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.emitter.ppt.PPTWriter;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;

public class PPTPageDevice implements IPageDevice {

	private PPTWriter writer;
	private PPTPage currentPage;

	public PPTPageDevice(OutputStream output, String title, String author, String description, String subject) {
		writer = new PPTWriter(output);
		writer.start(title, author, description, subject);
	}

	public void close() throws Exception {
		writer.end();
	}

	public IPage newPage(int width, int height, Color backgroundColor) {
		if (currentPage != null) {
			currentPage.dispose();
		}
		currentPage = new PPTPage(width, height, backgroundColor, writer);
		return currentPage;
	}
}
