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
package org.eclipse.birt.report.engine.emitter.odp.device;

import java.awt.Color;
import java.io.OutputStream;

import org.eclipse.birt.report.engine.emitter.odp.OdpWriter;
import org.eclipse.birt.report.engine.emitter.odp.OdpContext;
import org.eclipse.birt.report.engine.emitter.odp.util.OdpUtil;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.emitter.IPage;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.odf.IOdfMasterPageWriter;
import org.eclipse.birt.report.engine.odf.MasterPageManager;
import org.eclipse.birt.report.engine.odf.style.StyleBuilder;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.style.StyleEntry;
import org.eclipse.birt.report.engine.odf.writer.MasterPageWriter;

public class OdpPageDevice implements IPageDevice {
	private OdpWriter writer;
	private IOdfMasterPageWriter mpWriter;
	private OdpPage currentPage;
	private OdpContext context;

	/**
	 * 
	 * @param output   body output
	 * @param mpOutput master page output
	 * @param context
	 * @throws Exception
	 */
	public OdpPageDevice(OutputStream output, OutputStream mpOutput, OdpContext context) throws Exception {
		writer = new OdpWriter(output);
		writer.start();
		mpWriter = new MasterPageWriter(mpOutput);
		mpWriter.start();
		this.context = context;
	}

	public void close() throws Exception {
		writer.end();
		mpWriter.end();
	}

	public IPage newPage(int width, int height, Color backgroundColor) {
		MasterPageManager mpManager = context.getMasterPageManager();
		if (currentPage != null) {
			currentPage.dispose();
		}

		mpManager.newPage("Standard"); //$NON-NLS-1$

		StyleEntry pageLayout = StyleBuilder.createEmptyStyleEntry(StyleConstant.TYPE_PAGE_LAYOUT);
		// TODO: convert units
		pageLayout.setProperty(StyleConstant.WIDTH, (width / PDFConstants.LAYOUT_TO_PDF_RATIO) + "pt");
		pageLayout.setProperty(StyleConstant.HEIGHT, (height / PDFConstants.LAYOUT_TO_PDF_RATIO) + "pt");
		if (backgroundColor != null) {
			pageLayout.setProperty(StyleConstant.BACKGROUND_COLOR_PROP, OdpUtil.getColorString(backgroundColor));
		}

		context.addGlobalStyle(pageLayout);

		mpWriter.startMasterPage(pageLayout, mpManager.getCurrentMasterPage(), null);
		mpWriter.endMasterPage();

		currentPage = new OdpPage(width, height, backgroundColor, writer, context);
		return currentPage;
	}
}
