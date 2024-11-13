/*******************************************************************************
 * Copyright (c) 2013, 2024 Actuate Corporation and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation	- initial API and implementation
 *  Thomas Gutmann		- add option to handle footer wrapper
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.docx.writer;

import java.io.IOException;

import org.eclipse.birt.report.engine.ooxml.IPart;

public class Footer extends BasicComponent {

	Document document;
	int footerHeight;
	int footerWidth;
	boolean wrapFooter;

	/**
	 * Constructor 1
	 */
	Footer(IPart part, Document document, int footerHeight, int footerWidth) throws IOException {
		this(part, document, footerHeight, footerWidth, true);
	}

	/**
	 * Constructor 2
	 */
	Footer(IPart part, Document document, int footerHeight, int footerWidth, boolean wrapFooter) throws IOException {
		super(part);
		this.document = document;
		this.footerHeight = footerHeight;
		this.footerWidth = footerWidth;
		this.wrapFooter = wrapFooter;
	}

	@Override
	void start() {
		writer.startWriter();
		writer.openTag("w:ftr");
		writeXmlns();
		if (this.wrapFooter)
			startHeaderFooterContainer(footerHeight, footerWidth);
	}

	@Override
	void end() {
		if (wrapFooter)
			endHeaderFooterContainer();
		writer.closeTag("w:ftr");
		writer.endWriter();
		writer.close();
	}

	@Override
	protected int getImageID() {
		return document.getImageID();
	}

	@Override
	protected int getMhtTextId() {
		return document.getMhtTextId();
	}
}
