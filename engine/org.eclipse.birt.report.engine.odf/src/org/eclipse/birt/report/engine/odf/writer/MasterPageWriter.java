/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.odf.writer;

import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.emitter.XMLWriter;
import org.eclipse.birt.report.engine.odf.IOdfMasterPageWriter;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

@SuppressWarnings("nls")
/**
 * Writer for the master styles section of styles.xml. It extends the body
 * writer in order to write the header/footer bodies.
 */
public class MasterPageWriter extends AbstractOdfWriter implements IOdfMasterPageWriter {
	public static final Logger logger = Logger.getLogger(MasterPageWriter.class.getName());

	public MasterPageWriter(OutputStream out) {
		this(out, "UTF-8");
	}

	public MasterPageWriter(OutputStream out, String encoding) {
		writer = new XMLWriter();
		// no indent or newlines, because newlines inside paragraphs are
		// considered as white spaces
		writer.setIndent(false);
		writer.open(out, encoding);
	}

	public MasterPageWriter(XMLWriter writer) {
		this.writer = writer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#start()
	 */
	public void start() {
		writer.openTag("office:master-styles");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#end()
	 */
	public void end() {
		writer.closeTag("office:master-styles");
		try {
			close();
		} catch (Exception e) {
			logger.log(Level.WARNING, e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#
	 * startMasterPage(org.eclipse.birt.report.engine.odf.style.StyleEntry,
	 * java.lang.String, java.lang.String)
	 */
	public void startMasterPage(StyleEntry pageLayout, String masterPageName, String displayName) {
		writer.openTag("style:master-page");
		writer.attribute("style:name", masterPageName);

		if (displayName != null) {
			writer.attribute("style:display-name", displayName);
		}

		writer.attribute("style:page-layout-name", pageLayout.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#endMasterPage(
	 * )
	 */
	public void endMasterPage() {
		writer.closeTag("style:master-page");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#startHeader()
	 */
	public void startHeader() {
		writer.openTag("style:header");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#endHeader()
	 */
	public void endHeader() {
		writer.closeTag("style:header");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#startFooter()
	 */
	public void startFooter() {
		writer.openTag("style:footer");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.engine.odf.writer.IOdfMasterPageWriter#endFooter()
	 */
	public void endFooter() {
		writer.closeTag("style:footer");
	}
}
