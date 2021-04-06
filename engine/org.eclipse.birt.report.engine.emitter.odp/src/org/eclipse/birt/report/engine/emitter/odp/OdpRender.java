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
package org.eclipse.birt.report.engine.emitter.odp;

import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.odp.device.OdpPage;
import org.eclipse.birt.report.engine.emitter.odp.device.OdpPageDevice;
import org.eclipse.birt.report.engine.emitter.odp.util.OdpUtil;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.odf.OdfConstants;
import org.eclipse.birt.report.engine.odf.writer.MetaWriter;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The ODP render class.
 */
public class OdpRender extends PageDeviceRender {
	private OutputStream bodyOut = null;
	private OutputStream mpOut = null;
	private OdpContext odfContext;

	/** The default output ODP file name. */
	public static final String REPORT_FILE = "Report.odp"; //$NON-NLS-1$

	public OdpRender(IEmitterServices services, OdpContext context, OutputStream out, OutputStream mpOut)
			throws EngineException {
		initialize(services);
		this.odfContext = context;
		this.bodyOut = out;
		this.mpOut = mpOut;
	}

	public IPageDevice createPageDevice(String title, String author, String subject, String description,
			IReportContext context, IReportContent report) throws Exception {
		try {
			writeMetaProperties(author, title, description, subject);
			return new OdpPageDevice(bodyOut, mpOut, odfContext);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	/**
	 * Returns the output format, always is "odp".
	 * 
	 * @return the output format
	 */
	public String getOutputFormat() {
		return "odp";
	}

	/**
	 * Initializes the ODPEmitter.
	 * 
	 * @param services the emitter services object.
	 * @throws EngineException
	 */
	public void initialize(IEmitterServices services) throws EngineException {
		this.services = services;
		reportRunnable = services.getReportRunnable();

		if (reportRunnable != null) {
			reportDesign = (ReportDesignHandle) reportRunnable.getDesignHandle();
		}
		this.context = services.getReportContext();
		this.bodyOut = EmitterUtil.getOuputStream(services, REPORT_FILE);
	}

	public void visitImage(IImageArea imageArea) {
		OdpPage odpPage = (OdpPage) pageGraphic;
		odpPage.setLink(OdpUtil.getHyperlink(imageArea, services, reportRunnable, context));
		super.visitImage(imageArea);
		odpPage.setLink(null);
	}

	@Override
	public void visitText(ITextArea textArea) {
		OdpPage odpPage = (OdpPage) pageGraphic;
		odpPage.setLink(OdpUtil.getHyperlink(textArea, services, reportRunnable, context));
		super.visitText(textArea);
		odpPage.setLink(null);
	}

	protected void drawTextAt(ITextArea text, int x, int y, int width, int height, TextStyle textStyle) {
		pageGraphic.drawText(text.getLogicalOrderText(), x, y, width, height, textStyle);
	}

	protected void writeMetaProperties(String creator, String title, String description, String subject)
			throws IOException, BirtException {
		// TODO: refactor with ODF plugin
		MetaWriter writer = new MetaWriter(odfContext.getPackage()
				.addEntry(OdfConstants.FILE_META, OdfConstants.CONTENT_TYPE_XML).getOutputStream());
		writer.start();
		writer.writeMeta(creator, title, description, subject);
		writer.end();
	}
}
