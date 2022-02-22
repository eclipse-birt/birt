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

package org.eclipse.birt.doc.schema;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipse.birt.doc.schema.CSSDocParser.ParseException;
import org.eclipse.birt.report.model.api.metadata.IMetaDataDictionary;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataParserException;
import org.eclipse.birt.report.model.metadata.MetaDataReader;

/**
 * Generate schema doc
 *
 */

public class SchemaDoc {
	private static String outputDir = "romdoc/gen/css"; //$NON-NLS-1$

	/**
	 * @param args
	 */

	public static void main(String[] args) {
		CSSDocParser parser = new CSSDocParser();
		try {
			parser.parse();
			Map cssMap = parser.cssMap;
			try {
				loadModel();
			} catch (MetaDataParserException e) {
				return;
			}
			IMetaDataDictionary dict = MetaDataDictionary.getInstance();
			ISchemaWriter writer = null;
			try {
				File output = makeFile("CssProperty.html"); //$NON-NLS-1$
				writer = new CssSchemaWriter(output);
				IFilter filter = new CssStyleFilter();
				SchemaUtil.writeSchema(dict, writer, filter, cssMap);
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Make a file under output folder.
	 *
	 * @param relativeDir relative to the output folder. For example, if current
	 *                    output folder is "d:\romdoc",
	 *                    <code>makeFile( "structs", "action.html")</code> will
	 *                    return a File instance to "d:\romdoc\structs\action.html".
	 * @param fileName    name of the file
	 * @return File instance to the file.
	 * @throws IOException
	 */

	private static File makeFile(String fileName) throws IOException {
		File dir = new File(outputDir + "/"); //$NON-NLS-1$
		if (!dir.exists()) {
			dir.mkdir();
		}

		File output = new File(dir, fileName);
		if (!output.exists()) {
			output.createNewFile();
		}

		return output;
	}

	/**
	 * Load rom.def metadata
	 *
	 * @throws MetaDataParserException
	 */

	private static void loadModel() throws MetaDataParserException {
		try {
			MetaDataReader.read(ReportDesign.class.getResourceAsStream("rom.def")); //$NON-NLS-1$
		} catch (MetaDataParserException e) {
			System.out.println("rom.def load failed."); //$NON-NLS-1$
			throw e;
		}
	}
}
