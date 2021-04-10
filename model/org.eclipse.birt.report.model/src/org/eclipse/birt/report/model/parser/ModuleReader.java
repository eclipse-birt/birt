/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.ParserFactory;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class provides the reader for the design file, library file and template
 * file. Encapsulates the SAX parser. The parser attempts to read the file even
 * if errors occur.
 * <p>
 * <code>DesignFileException</code> will be thrown if fatal error occurs, such
 * as file not found, syntax error or invalid xml file. Semantic error is along
 * with the returned module.
 */

public abstract class ModuleReader {

	/**
	 * 
	 */

	private static Logger logger = Logger.getLogger(ModuleReader.class.getName());

	/**
	 * Parses an XML design file given an input stream. Creates and returns the
	 * internal representation of the report design
	 * 
	 * @param handler     the parser handler
	 * @param inputStream the input stream that reads the design file
	 * @return the internal representation of the design
	 * 
	 * @throws DesignFileException if the input stream is not well-formed xml, there
	 *                             is unsupported tags and there is run-time
	 *                             exception.
	 */

	protected Module readModule(ModuleParserHandler handler, InputStream inputStream) throws DesignFileException {
		assert handler != null;

		InputStream internalStream = inputStream;
		if (!inputStream.markSupported())
			internalStream = new BufferedInputStream(inputStream);

		assert internalStream.markSupported();

		String signature = null;
		SAXParser parser = null;
		Map<String, Object> properties = null;

		try {
			signature = checkUTFSignature(internalStream, handler.getFileName());
			properties = new HashMap<String, Object>(2);
			properties.put("http://xml.org/sax/properties/lexical-handler", //$NON-NLS-1$
					new ModuleParserHandler.ModuleLexicalHandler(handler));

			parser = ParserFactory.getInstance().getParser(properties);
			InputSource inputSource = new InputSource(internalStream);
			inputSource.setEncoding(signature);
			parser.parse(inputSource, handler);
			// log warning message
			// TED 57401
			List<XMLParserException> warningExceptionList = handler.getErrorHandler().getWarnings();
			if (handler.isLaterVersion()
					&& (handler.getModule().getOptions() == null
							|| handler.getModule().getOptions().isSupportedUnknownVersion())
					&& warningExceptionList != null && warningExceptionList.size() > 0) {
				for (XMLParserException xmlException : warningExceptionList) {
					logger.log(Level.WARNING, xmlException.getMessage());
				}
			}

		} catch (SAXException e) {
			// output errors to the logger

			List errors = handler.getErrorHandler().getErrors();
			for (int i = 0; i < errors.size(); i++) {
				Exception exception = (Exception) errors.get(i);
				logger.log(Level.SEVERE, exception.getMessage());
			}

			// Syntax error is found

			if (e.getException() instanceof DesignFileException) {
				throw (DesignFileException) e.getException();
			}

			// Invalid xml error is found

			throw new DesignFileException(handler.getFileName(), errors, e);
		} catch (ParserConfigurationException e) {
			throw new DesignFileException(handler.getFileName(), handler.getErrorHandler().getErrors(), e);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "IO error occurs " //$NON-NLS-1$
					+ e.getLocalizedMessage() + " on line " //$NON-NLS-1$
					+ handler.getCurrentLineNo());

			throw new DesignFileException(handler.getFileName(), handler.getErrorHandler().getErrors(), e);
		} finally {
			// the parser may be null in checkUTFSignature call.
			if (parser != null)
				// even there is XML exception, need to release the resource.
				try {
					ParserFactory.getInstance().releaseParser(parser, properties);
				} catch (Exception e1) {

				}

			// clear lexical handler
			if (properties != null)
				properties.clear();
		}

		Module module = handler.getModule();
		module.setUTFSignature(signature);
		module.setValid(true);

		return module;
	}

	/**
	 * Parses an XML design file given a file name. Creates and returns the internal
	 * representation of the report design
	 * 
	 * @param handler the parser handler
	 * 
	 * @return the internal representation of the design
	 * @throws DesignFileException if file is not found
	 */

	public Module readModule(ModuleParserHandler handler) throws DesignFileException {
		assert handler != null;

		// support the url syntax such as file://, http://,
		// bundleresource://, jar://

		InputStream in = null;

		try {
			// if the hander if the generic module reader, the module has not be
			// initialized.

			if (handler instanceof GenericModuleParserHandler) {
				URL url = ModelUtil.getURLPresentation(handler.getFileName());

				if (url != null) {
					((GenericModuleParserHandler) handler).location = url;
					in = url.openStream();
				}
			} else {
				String location = handler.getModule().getLocation();

				if (location == null)
					throw new IOException();

				URL url = new URL(location);
				in = url.openStream();

			}
		} catch (IOException e) {
			DesignParserException ex = new DesignParserException(DesignParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND,
					new String[] { handler.getFileName() }, e);
			List exceptionList = new ArrayList();
			exceptionList.add(ex);
			// Ted 50667
//			logger.log( Level.SEVERE, "Parsed file was not found." ); //$NON-NLS-1$

			throw new DesignFileException(handler.getFileName(), exceptionList);
		}

		InputStream inputStream = new BufferedInputStream(in);
		assert inputStream.markSupported();
		Module module = readModule(handler, inputStream);

		try {
			inputStream.close();
		} catch (IOException e) {
			// ignore this exception.
		}
		inputStream = null;

		return module;
	}

	/**
	 * Checks whether the input stream has a compatible encoding signature with
	 * BIRT. Currently, BIRT only supports UTF-8 encoding.
	 * 
	 * @param inputStream the input stream to check
	 * @param fileName    the design file name
	 * @return the signature from the UTF files.
	 * @throws IOException  if errors occur during opening the design file
	 * @throws SAXException if the stream has unexpected encoding signature
	 */

	protected static String checkUTFSignature(InputStream inputStream, String fileName)
			throws IOException, SAXException {
		// This may fail if there are a lot of space characters before the end
		// of the encoding declaration

		String encoding = UnicodeUtil.checkUTFSignature(inputStream);

		if (encoding != null && !UnicodeUtil.SIGNATURE_UTF_8.equals(encoding)) {
			Exception cause = new DesignParserException(DesignParserException.DESIGN_EXCEPTION_UNSUPPORTED_ENCODING);
			Exception fileException = new DesignFileException(fileName, cause);

			throw new SAXException(fileException);
		}

		return encoding;
	}
}