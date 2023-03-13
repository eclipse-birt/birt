/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.ppt;

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPage;
import org.eclipse.birt.report.engine.emitter.ppt.device.PPTPageDevice;
import org.eclipse.birt.report.engine.emitter.ppt.util.PPTUtil;
import org.eclipse.birt.report.engine.layout.emitter.IPageDevice;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * The PPT render class.
 */
public class PPTRender extends PageDeviceRender {

	private OutputStream pptOutput = null;

	/** The default output PPT file name. */
	public static final String REPORT_FILE = "Report.ppt"; //$NON-NLS-1$

	public PPTRender(IEmitterServices services) throws EngineException {
		initialize(services);
	}

	@Override
	public IPageDevice createPageDevice(String title, String author, String subject, String description,
			IReportContext context, IReportContent report) throws Exception {
		try {
			return new PPTPageDevice(pptOutput, title, author, description, subject);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return null;
	}

	/**
	 * Returns the output format, always is "ppt".
	 *
	 * @return the output format
	 */
	@Override
	public String getOutputFormat() {
		return "ppt";
	}

	/**
	 * Initializes the PPTEmitter.
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
		this.pptOutput = EmitterUtil.getOuputStream(services, REPORT_FILE);
	}

	@Override
	public void visitImage(IImageArea imageArea) {
		PPTPage pptPage = (PPTPage) pageGraphic;
		pptPage.setLink(PPTUtil.getHyperlink(imageArea, services, reportRunnable, context));
		super.visitImage(imageArea);
		pptPage.setLink(null);
	}

	@Override
	public void visitText(ITextArea textArea) {
		PPTPage pptPage = (PPTPage) pageGraphic;
		pptPage.setLink(PPTUtil.getHyperlink(textArea, services, reportRunnable, context));
		super.visitText(textArea);
		pptPage.setLink(null);
	}

	@Override
	protected void drawTextAt(ITextArea text, int x, int y, int width, int height, TextStyle textStyle) {
		pageGraphic.drawText(text.getLogicalOrderText(), x, y, width, height, textStyle);
	}
}
