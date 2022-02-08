/***********************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.chart.device.pdf;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.batik.transcoder.DefaultErrorHandler;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.device.svg.ISVGConstants;
import org.eclipse.birt.chart.device.svg.SVGRendererImpl;
import org.eclipse.birt.chart.device.svg.plugin.ChartDeviceSVGPlugin;
import org.eclipse.birt.chart.device.util.ChartTextRenderer;
import org.eclipse.birt.chart.event.InteractionEvent;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.w3c.dom.Document;

/**
 * Provides a reference implementation of a PDF device renderer. It uses the
 * Batik SVG Toolkit to transcode SVG generated output to PDF format.
 */
public class PDFRendererImpl extends SVGRendererImpl {

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.device.svg/trace"); //$NON-NLS-1$

	protected void init() {
		// Do not invoke super method.
		final PluginSettings ps = PluginSettings.instance();
		try {
			_ids = ps.getDisplayServer("ds.PDF"); //$NON-NLS-1$
			// Use Generic impl instead
			_tr = new ChartTextRenderer(_ids);
			ivRenderer = new PDFInteractiveRenderer(this);
		} catch (ChartException pex) {
			logger.log(pex);
		}
	}

	public void enableInteraction(InteractionEvent ie) throws ChartException {
		// no interactions since we are rendering a static image
	}

	public void setProperty(String sProperty, Object oValue) {
		if (sProperty.equals(IDeviceRenderer.UPDATE_NOTIFIER)) {
			// ignore property
		} else if (sProperty.equals(ISVGConstants.JAVASCRIPT_CODE_LIST)) {
			// ignore property
		} else if (sProperty.equals(ISVGConstants.JAVASCRIPT_URL_REF_LIST)) {
			// ignore property
		} else if (sProperty.equals(ISVGConstants.RESIZE_SVG)) {
			// the output will be static
			_resizeSVG = false;
		} else
			super.setProperty(sProperty, oValue);
	}

	protected void addGroupStructure(Object block) {
		// no structure needed since this is a static image
	}

	public void after() throws ChartException {

		if (oOutputIdentifier instanceof OutputStream) // OUTPUT STREAM
		{
			try {
				transcode2PDF(new StringReader(serializeGeneratedDocumentToString(dom)),
						(OutputStream) oOutputIdentifier);
			} catch (Exception ex) {
				throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING, ex);
			}
		} else if (oOutputIdentifier instanceof String) {
			FileOutputStream fos = null;
			try {
				fos = SecurityUtil.newFileOutputStream((String) oOutputIdentifier);
				Reader r = new StringReader(serializeGeneratedDocumentToString(dom));

				transcode2PDF(r, fos);
				fos.close();
			} catch (Exception ex) {
				throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING, ex);
			}
		} else {
			throw new ChartException(ChartDeviceSVGPlugin.ID, ChartException.RENDERING,
					"SVGRendererImpl.exception.UnableToWriteChartImage", //$NON-NLS-1$
					new Object[] { oOutputIdentifier }, null);
		}

	}

	/**
	 * Transcode SVG format data from a transcoder input into PNG format data and
	 * write it to a transcoder output.
	 *
	 * @param tin  the TranscoderInput
	 * @param tout the TranscoderOutput
	 *
	 * @throws TranscoderException
	 */
	protected void transcode2PDF(TranscoderInput tcin, TranscoderOutput tcout) throws TranscoderException {
		// create the PNG transcoder
		PDFTranscoder t = new PDFTranscoder();

		// set the custom error handler
		t.setErrorHandler(new DefaultErrorHandler() {
			public void error(TranscoderException te) {
				te.printStackTrace();
			}

			public void warning(TranscoderException te) {
				te.printStackTrace();
			}
		});

		// transcode the data
		t.transcode(tcin, tcout);
	}

	/**
	 * Transcode a SVG format Reader and write it to an output stream as a PDF
	 * image.
	 *
	 * @param dom     the svg document object model
	 * @param ostream the output stream
	 *
	 * @throws TranscoderException
	 */
	protected void transcode2PDF(Document dom, OutputStream ostream) throws TranscoderException {
		// transcode the data
		TranscoderInput tcin = new TranscoderInput(dom);
		TranscoderOutput tcout = new TranscoderOutput(ostream);
		transcode2PDF(tcin, tcout);

		// flush the output stream
		try {
			ostream.flush();
		} catch (IOException ioe) {
			// ignore output stream flush error
		}
	}

	/**
	 * Transcode a SVG format Reader and write it to an output stream as a PDF
	 * image.
	 *
	 * @param dom     the svg document object model
	 * @param ostream the output stream
	 *
	 * @throws TranscoderException
	 */
	protected void transcode2PDF(Reader r, OutputStream ostream) throws TranscoderException {
		// transcode the data
		TranscoderInput tcin = new TranscoderInput(r);
		TranscoderOutput tcout = new TranscoderOutput(ostream);
		transcode2PDF(tcin, tcout);

		// flush the output stream
		try {
			ostream.flush();
		} catch (IOException ioe) {
			// ignore output stream flush error
		}
	}

	/**
	 * Serializes a <code>Document</code> object to a <code>String</code> in XML
	 * format. This is a convevience method to save the output of the SVG generator
	 * to a string.
	 * 
	 * @param generatedDocument the generated graphic
	 * @return String the XML-serialized form of the <code>Document</code>
	 * @throws Exception If XML serialization failed
	 */
	protected String serializeGeneratedDocumentToString(Document generatedDocument) throws Exception {
		if (generatedDocument == null) {
			return null;
		}
		OutputStreamWriter writer = null;
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		writer = SecurityUtil.newOutputStreamWriter(stream, "UTF-8"); //$NON-NLS-1$
		DOMSource source = new DOMSource(generatedDocument);
		StreamResult result = new StreamResult(writer);

		TransformerFactory transFactory = SecurityUtil.newTransformerFactory();
		Transformer transformer = transFactory.newTransformer();
		transformer.transform(source, result);

		return stream.toString();

	}

}
