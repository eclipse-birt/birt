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
package org.eclipse.birt.report.engine.emitter.odt.writer;

import java.io.OutputStream;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.odf.IOdfMasterPageWriter;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;

public class MasterPageWriter extends BodyWriter implements IOdfMasterPageWriter {
	public static final Logger logger = Logger.getLogger(MasterPageWriter.class.getName());

	/**
	 * Wrap a real master page writer.
	 */
	private org.eclipse.birt.report.engine.odf.writer.MasterPageWriter mpWriter;

	public MasterPageWriter(OutputStream out) throws Exception {
		this(out, "UTF-8");
	}

	public MasterPageWriter(OutputStream out, String encoding) throws Exception {
		super(out, encoding);
		mpWriter = new org.eclipse.birt.report.engine.odf.writer.MasterPageWriter(writer);
	}

	public void start(boolean rtl) {
		start();
	}

	public void start() {
		mpWriter.start();
	}

	@Override
	public void end() {
		mpWriter.end();
		/*
		 * try { close( ); } catch ( Exception e ) { logger.log( Level.WARNING,
		 * e.getLocalizedMessage( ) ); }
		 */
	}

	public void startMasterPage(StyleEntry pageLayout, String masterPageName, String displayName) {
		mpWriter.startMasterPage(pageLayout, masterPageName, displayName);
	}

	public void endMasterPage() {
		mpWriter.endMasterPage();
	}

	public void startHeader() {
		mpWriter.startHeader();
	}

	public void endHeader() {
		mpWriter.endHeader();
	}

	public void startFooter() {
		mpWriter.startFooter();
	}

	public void endFooter() {
		mpWriter.endFooter();
	}

	public void writeString(String s) {
		mpWriter.writeString(s);
	}

}
