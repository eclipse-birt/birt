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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.layout.emitter.PageDeviceRender;
import org.eclipse.birt.report.engine.layout.emitter.PageEmitter;
import org.eclipse.birt.report.engine.odf.OdfConstants;
import org.eclipse.birt.report.engine.odf.pkg.Package;
import org.eclipse.birt.report.engine.odf.style.StyleConstant;
import org.eclipse.birt.report.engine.odf.writer.ContentWriter;
import org.eclipse.birt.report.engine.odf.writer.StylesWriter;

public class OdpEmitter extends PageEmitter implements OdfConstants {
	public static final String MIME_TYPE = "application/vnd.oasis.opendocument.presentation"; //$NON-NLS-1$

	private ByteArrayOutputStream bodyOut;
	private ByteArrayOutputStream masterPageOut;
	private OutputStream out;
	private Package pkg;
	private OdpContext context;

	public void initialize(IEmitterServices service) throws EngineException {
		context = new OdpContext();
		bodyOut = new ByteArrayOutputStream();
		masterPageOut = new ByteArrayOutputStream();

		String tempFileDir = service.getReportEngine().getConfig().getTempDir();
		context.setTempFileDir(service.getReportEngine().getConfig().getTempDir());

		this.out = EmitterUtil.getOuputStream(service, "report.odp"); //$NON-NLS-1$
		pkg = Package.createInstance(out, tempFileDir, MIME_TYPE);
		context.setPackage(pkg);
		super.initialize(service);
	}

	public PageDeviceRender createRender(IEmitterServices service) throws EngineException {
		return new OdpRender(service, context, bodyOut, masterPageOut);
	}

	public void end(IReportContent report) {
		super.end(report);
		save();
	}

	private void save() {
		// TODO: somehow refactor with ODF's save method
		try {
			// output stream for real content
			ContentWriter docContentWriter = new ContentWriter(
					pkg.addEntry(FILE_CONTENT, CONTENT_TYPE_XML).getOutputStream(), context.getReportDpi());
			docContentWriter.write(context.getStyleManager().getStyles(),
					new ByteArrayInputStream(bodyOut.toByteArray()));

			StylesWriter stylesWriter = new OdpStylesWriter(
					pkg.addEntry(FILE_STYLES, CONTENT_TYPE_XML).getOutputStream(), context.getReportDpi());

			// write the styles.xml file
			// including the global styles
			stylesWriter.start();
			stylesWriter.writeStyles(context.getGlobalStyleManager().getStyles());
			stylesWriter.writeMasterPage(new ByteArrayInputStream(masterPageOut.toByteArray()));
			stylesWriter.end();

			pkg.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private class OdpStylesWriter extends StylesWriter {
		public OdpStylesWriter(OutputStream out, int reportDpi) {
			super(out, reportDpi);
		}

		public void writeDefaultStyles() {
			// TODO: also write "style:default-style" entries?

			// imported from an ODF file saved by OpenOffice 3.2
			writer.openTag("style:style");
			writer.attribute("style:name", "Standard");
			writer.attribute("style:family", "paragraph");
			writer.attribute("style:class", "text");
			writer.closeTag("style:style");

			writer.openTag("style:style");
			writer.attribute("style:name", "Graphics");
			writer.attribute("style:family", "graphic");

			writer.openTag("style:graphic-properties");
			writer.attribute("text:anchor-type", "paragraph");
			writer.attribute("svg:x", "0in");
			writer.attribute("svg:y", "0in");
			writer.attribute("style:wrap", "dynamic");
			writer.attribute("style:number-wrapped-paragraphs", "no-limit");
			writer.attribute("style:wrap-contour", "false");
			writer.attribute("style:vertical-pos", "top");
			writer.attribute("style:vertical-rel", "paragraph");
			writer.attribute("style:horizontal-pos", "center");
			writer.attribute("style:horizontal-rel", "paragraph");
			writer.attribute("draw:auto-grow-height", "false");
			writer.attribute("draw:auto-grow-width", "true");
			writer.closeTag("style:graphic-properties");
			writer.closeTag("style:style");

			writer.openTag("style:style");
			writer.attribute("style:name", StyleConstant.HIDDEN_STYLE_NAME);
			writer.attribute("style:family", "paragraph");
			writer.openTag("style:paragraph-properties");
			writer.attribute("text:display", "none");
			writer.closeTag("style:paragraph-properties");
			writer.closeTag("style:style");
		}
	}
}
