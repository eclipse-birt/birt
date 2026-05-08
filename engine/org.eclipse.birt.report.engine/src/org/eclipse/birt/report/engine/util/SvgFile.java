/*******************************************************************************
 * Copyright (c)2008, 2025 Actuate Corporation and others
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

package org.eclipse.birt.report.engine.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.XMLConstants;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.eclipse.birt.core.util.CommonUtil;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * SVG utility class to support SVG images
 *
 * @since 3.3
 *
 */
public class SvgFile {

	private static final String FILE_EXTENSION_SVG = ".svg";
	private static final String URL_IMAGE_TYPE_SVG = "image/svg+xml";
	private static final String URL_PROTOCOL_TYPE_DATA = "data:";
	private static final String URL_PROTOCOL_TYPE_DATA_BASE = ";base64,";
	private static final String URL_PROTOCOL_TYPE_DATA_UTF8 = ";utf8,";

	/**
	 * Validation of SVG image
	 *
	 * @param uri URI to be validated
	 * @return the validation result of SVG image
	 */
	public static boolean isSvg(String uri) {
		return uri != null && (uri.endsWith(FILE_EXTENSION_SVG) || uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG));
	}

	/**
	 * Validation of SVG image
	 *
	 * @param mimeType  mime type to be validated
	 * @param uri       URI to be validated
	 * @param extension extension to be validated
	 * @return the validation result of SVG image
	 */
	public static boolean isSvg(String mimeType, String uri, String extension) {
		return isSvg(mimeType, uri, extension, null);
	}

	/**
	 * Validation of SVG image.
	 *
	 * @param mimeType  mime type to be validated
	 * @param uri       URI to be validated
	 * @param extension extension to be validated
	 * @param data      raw image data (bytes) for content-based detection
	 * @return the validation result of SVG image
	 * @since 4.24
	 */
	public static boolean isSvg(String mimeType, String uri, String extension, byte[] data) {
		return ((mimeType != null)
				&& mimeType.equalsIgnoreCase(URL_IMAGE_TYPE_SVG)) // $NON-NLS-1$
				|| ((uri != null)
				&& (uri.toLowerCase().endsWith(FILE_EXTENSION_SVG)
				|| uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG))) // $NON-NLS-1$
				|| ((extension != null) && extension.toLowerCase().endsWith(FILE_EXTENSION_SVG)) // $NON-NLS-1$
				|| isSvgContent(data);
	}

	/**
	 * Transformation of SVG image to PNG raster image
	 *
	 * @param uri SVG URI
	 * @return the PNG raster image
	 * @throws Exception
	 */
	public static byte[] transSvgToArray(String uri) throws Exception {
		byte[] data = null;

		if (uri != null && uri.toLowerCase().contains(URL_PROTOCOL_TYPE_DATA)
				&& uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
			String encodedImg = null;
			String decodedImg = null;
			if (uri.toLowerCase().contains(URL_IMAGE_TYPE_SVG)) {
				String svgSplitter = "svg\\+xml,";
				if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_UTF8)) {
					svgSplitter = "svg\\+xml;utf8,";
				} else if (uri.contains(URL_IMAGE_TYPE_SVG + URL_PROTOCOL_TYPE_DATA_BASE)) {
					svgSplitter = "svg\\+xml;base64,";
				}
				String[] uriParts = uri.split(svgSplitter);
				if (uriParts.length >= 2) {
					encodedImg = uriParts[1];
					decodedImg = encodedImg;
					if (uri.contains(URL_PROTOCOL_TYPE_DATA_BASE)) {
						decodedImg = new String(
								Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8)));
					}
				}
			}
			try {
				decodedImg = java.net.URLDecoder.decode(decodedImg, StandardCharsets.UTF_8);
			} catch (IllegalArgumentException iae) {
				// do nothing
			}
			data = SvgFile.transSvgToArray(new ByteArrayInputStream(decodedImg.getBytes()));
		} else {
			InputStream in = new URL(uri).openStream();
			try (in) {
				data = transSvgToArray(in);
			}
		}
		return data;
	}

	/**
	 * Transformation of SVG image to PNG raster image
	 *
	 * @param inputStream SVG input stream
	 * @return the PNG raster image
	 * @throws Exception
	 */
	public static byte[] transSvgToArray(InputStream inputStream) throws Exception {
		PNGTranscoder transcoder = new PNGTranscoder();
		// create the transcoder input
		TranscoderInput input = new TranscoderInput(inputStream);
		// create the transcoder output
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		TranscoderOutput output = new TranscoderOutput(ostream);
		transcoder.transcode(input, output);
		// flush the stream
		ostream.flush();
		// use the output stream as Image input stream.
		return ostream.toByteArray();
	}

	/**
	 * Validation of SVG image by content inspection
	 *
	 * @param data raw image data (bytes)
	 * @return true if the content looks like an SVG/XML document
	 */
	private static boolean isSvgContent(byte[] data) {
		if (data == null) {
			return false;
		}
		SvgHandler handler = new SvgHandler();
		try {
			SAXParser saxParser = createSecureSaxParser();
			saxParser.parse(new InputSource(new ByteArrayInputStream(data)), handler);
			// If we get here, there was no first real XML element.
			// Then this is not a valid SVG file!
			return false;
		} catch (StopParsingException spe) {
			// Check the handler, whether the first element was the svg-Tag.
			return handler.svgTagFound();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// all other exceptions a due to broken documents or other problems
			return false;
		}
	}


	/**
	 * The default SAXParser provided by CommonUtil does not allow DOCTYPE declarations at all.
	 * In order to be able to parse SVGs containing a DOCTYPE declaration, we create our own SAXParser.
         * We use the recommendations from
	 *   https://xmltools.github.io/xml-security-essentials
	 * and
	 *   https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html#jaxp-documentbuilderfactory-saxparserfactory-and-dom4j
	 * for the case where DTDs are needed.
	 */
	private static SAXParser createSecureSaxParser() throws ParserConfigurationException, SAXException {
		SAXParserFactory spf = SAXParserFactory.newInstance();

		// If DTDs are needed, at minimum disable these:
		spf.setFeature("http://xml.org/sax/features/external-general-entities", false);  //$NON-NLS-1$
		spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);  //$NON-NLS-1$
		spf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);  //$NON-NLS-1$
		spf.setXIncludeAware(false);
		spf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

		return spf.newSAXParser();
	}

	/**
	 * Custom exception thrown in order to abort further parsing.
	 */
	private static class StopParsingException extends SAXException {
		public StopParsingException(String message) {
			super(message);
		}
	}

	/**
	 * SAX-Handler to fast determine whether content is a valid SVG file.
	 */
	private static class SvgHandler extends DefaultHandler {

		private boolean svgTagFound = false;

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			if (!svgTagFound && qName.equalsIgnoreCase("svg")) {
				// this is the "normal" case: first real tag of the document is a svg tag
				svgTagFound = true;
			}
			// End checks fast!
			throw new StopParsingException("Parsing finished");
		}

		public boolean svgTagFound() {
			return this.svgTagFound;
		}
	}
}
