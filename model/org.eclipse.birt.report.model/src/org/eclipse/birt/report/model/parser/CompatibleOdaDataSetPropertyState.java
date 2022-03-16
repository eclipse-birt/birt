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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.xml.sax.SAXException;

/**
 * Represents the property state for OdaDataSet.
 *
 * <pre>
 *
 *
 *
 *    Old design file:
 *
 *      &lt;oda-data-set name=&quot;myDataSet1&quot;&gt;
 *        &lt;property name=&quot;type&quot;&gt;JdbcSelectDataSet&lt;/property&gt;
 *      &lt;/oda-data-set&gt;
 *
 *     New design file:
 *
 *      &lt;oda-data-set extensionID=&quot;org.eclipse.birt.report.data.oda.jdbc&quot; name=&quot;myDataSet1&quot;&gt;
 *      &lt;/oda-data-set&gt;
 *
 *
 *
 * </pre>
 */

public class CompatibleOdaDataSetPropertyState extends CompatiblePropertyState {

	final static String JDBC_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"; //$NON-NLS-1$
	final static String FLAT_FILE_EXTENSION_ID = "org.eclipse.birt.report.data.oda.flatfile.dataSet"; //$NON-NLS-1$

	CompatibleOdaDataSetPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

		assert element instanceof OdaDataSet;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	@Override
	public void end() throws SAXException {
		if ("type".equals(name)) //$NON-NLS-1$
		{
			String convertedValue = convertToExtensionID(text.toString());

			setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, convertedValue);

			return;
		}

		super.end();
	}

	/**
	 * Convert type name to extension ID.
	 *
	 * @param value type name
	 * @return extension ID
	 */

	private String convertToExtensionID(String value) {
		if ("JdbcSelectDataSet".equalsIgnoreCase(value)) { //$NON-NLS-1$
			return JDBC_EXTENSION_ID;
		} else if ("FlatFileSelectDataSet".equalsIgnoreCase(value)) { //$NON-NLS-1$
			return FLAT_FILE_EXTENSION_ID;
		}

		return null;
	}
}
