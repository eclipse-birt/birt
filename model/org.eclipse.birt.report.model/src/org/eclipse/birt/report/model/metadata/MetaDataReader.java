/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.metadata;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.SAXParser;

import org.eclipse.birt.report.model.util.ParserFactory;

/**
 * Reads the meta-data definition file. The parser populates the singleton
 * dictionary instance.
 */

public final class MetaDataReader {

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger(MetaDataReader.class.getName());

	/**
	 * Parses the source metadata config file, retrieve the data into data
	 * structures. <code>MetaLogManager</code> will be loaded to do the meta data
	 * error logging, don't forget to call {@link MetaLogManager#shutDown()}after
	 * reading of the metadata.
	 * 
	 * 
	 * @param fileName meta source file name.
	 * @throws MetaDataParserException
	 */

	public static void read(String fileName) throws MetaDataParserException {
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, e.getMessage());
			MetaLogManager.log("Metadata definition file not found", e); //$NON-NLS-1$
			throw new MetaDataParserException(fileName, MetaDataParserException.DESIGN_EXCEPTION_FILE_NOT_FOUND);
		}

		try {
			read(inputStream);
		} catch (MetaDataParserException e) {
			e.setFileName(fileName);
			throw e;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				// Do nothing.
			}
		}
	}

	/**
	 * Parses the source metadata config file, retrieve the data into data
	 * structures. <code>MetaLogManager</code> will be loaded to do the meta data
	 * error logging, don't forget to call {@link MetaLogManager#shutDown()}after
	 * reading of the metadata.
	 * 
	 * @param inputStream meta source file stream.
	 * @throws MetaDataParserException
	 */

	public static void read(InputStream inputStream) throws MetaDataParserException {
		InputStream internalStream = inputStream;
		if (inputStream != null && !inputStream.markSupported()) {
			internalStream = new BufferedInputStream(inputStream);
			assert internalStream.markSupported();
		}

		assert MetaDataDictionary.getInstance().isEmpty();
		MetaDataHandler handler = new MetaDataHandler();

		SAXParser parser = null;
		try {
			parser = ParserFactory.getInstance().getParser(null);
			parser.parse(internalStream, handler);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
			MetaLogManager.log("Metadata parsing error", e); //$NON-NLS-1$
			throw new MetaDataParserException(e, MetaDataParserException.DESIGN_EXCEPTION_PARSER_ERROR);
		} finally {
			// even there is XML exception, need to release the resource.
			try {
				ParserFactory.getInstance().releaseParser(parser, null);
			} catch (Exception e1) {

			}
		}

	}

}
