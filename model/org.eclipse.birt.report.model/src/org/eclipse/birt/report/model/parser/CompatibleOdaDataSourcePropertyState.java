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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Represents the OdaDataSource property state.
 * <p>
 * The compatible version is 0.
 * <p>
 * 
 * <pre>
 *      
 *       
 *        
 *        Old design file:
 *        
 *               &lt;oda-data-source extensionName=&quot;jdbc&quot; name=&quot;myDataSource1&quot;&gt;
 *                 &lt;property name=&quot;ODA:driver-class&quot;&gt;Driver Class&lt;/property&gt;
 *                 &lt;property name=&quot;ODA:url&quot;&gt;URL&lt;/property&gt;
 *                 &lt;property name=&quot;ODA:data-source&quot;&gt;Data Source&lt;/property&gt;
 *                 &lt;property name=&quot;ODA:user&quot;&gt;User&lt;/property&gt;
 *                 &lt;property name=&quot;ODA:password&quot;&gt;Password&lt;/property&gt;
 *               &lt;/oda-data-source&gt;
 *              
 *               &lt;oda-data-source extensionName=&quot;jdbc&quot; name=&quot;myDataSource1&quot;&gt;
 *                 &lt;property name=&quot;odaDriverClass&quot;&gt;Driver Class&lt;/property&gt;
 *                 &lt;property name=&quot;odaURL&quot;&gt;URL&lt;/property&gt;
 *                 &lt;property name=&quot;odaDataSource&quot;&gt;Data Source&lt;/property&gt;
 *                 &lt;property name=&quot;odaUser&quot;&gt;User&lt;/property&gt;
 *                 &lt;property name=&quot;odaPassword&quot;&gt;Password&lt;/property&gt;
 *               &lt;/oda-data-source&gt;
 *               
 *               &lt;oda-data-source name=&quot;myDataSource1&quot;&gt;
 *                 &lt;property name=&quot;driverName&quot;&gt;jdbc&lt;/property&gt;
 *               &lt;/oda-data-source&gt;
 *        
 *        New design file:
 *        
 *               &lt;oda-data-source extensionID=&quot;org.eclipse.birt.report.data.oda.jdbc&quot; name=&quot;myDataSource1&quot;&gt;
 *                 &lt;property name=&quot;odaDriverClass&quot;&gt;Driver Class&lt;/property&gt;
 *                 &lt;property name=&quot;odaURL&quot;&gt;URL&lt;/property&gt;
 *                 &lt;property name=&quot;odaDataSource&quot;&gt;Data Source&lt;/property&gt;
 *                 &lt;property name=&quot;odaUser&quot;&gt;User&lt;/property&gt;
 *                 &lt;property name=&quot;odaPassword&quot;&gt;Password&lt;/property&gt;
 *               &lt;/oda-data-source&gt;
 * 
 * 
 * </pre>
 */

public class CompatibleOdaDataSourcePropertyState extends PropertyState {

	final static String JDBC_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc"; //$NON-NLS-1$
	final static String SAMPLE_DB_EXTENSION_ID = "org.eclipse.birt.report.data.oda.sampledb"; //$NON-NLS-1$
	final static String FLAT_FILE_EXTENSION_ID = "org.eclipse.birt.report.data.oda.flatfile"; //$NON-NLS-1$

	CompatibleOdaDataSourcePropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);

		assert element instanceof OdaDataSource;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		if (isOldOdaDriverProperty(name) || isOdaDriverModelProperty(name)) {
			// The extension ID is set, but the given property is not
			// converted.

			setProperty(name, text.toString());

			return;
		} else if ("extensionName".equals(name) || "driverName".equals(name)) //$NON-NLS-1$ //$NON-NLS-2$
		{
			String convertedValue = convertToExtensionID(text.toString());

			setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, convertedValue);

			return;
		}

		super.end();
	}

	/**
	 * Convert driver name or extension name to extension ID.
	 * 
	 * @param value driver name or extension name
	 * @return extension ID
	 */

	private String convertToExtensionID(String value) {
		if ("jdbc".equalsIgnoreCase(value)) //$NON-NLS-1$
			return JDBC_EXTENSION_ID;
		else if ("flatfile".equalsIgnoreCase(value)) //$NON-NLS-1$
			return FLAT_FILE_EXTENSION_ID;
		else if ("sampledb".equalsIgnoreCase(value)) //$NON-NLS-1$
			return SAMPLE_DB_EXTENSION_ID;

		return null;
	}

	static String getNewOdaDriverProperty(String oldPropertyName) {
		if ("ODA:user".equalsIgnoreCase(oldPropertyName)) //$NON-NLS-1$
			return "odaUser";//$NON-NLS-1$
		else if ("ODA:url".equalsIgnoreCase(oldPropertyName))//$NON-NLS-1$
			return "odaURL";//$NON-NLS-1$
		else if ("ODA:driver-class".equalsIgnoreCase(oldPropertyName))//$NON-NLS-1$
			return "odaDriverClass";//$NON-NLS-1$
		else if ("ODA:data-source".equalsIgnoreCase(oldPropertyName))//$NON-NLS-1$
			return "odaDataSource";//$NON-NLS-1$
		else if ("ODA:password".equalsIgnoreCase(oldPropertyName))//$NON-NLS-1$
			return "odaPassword";//$NON-NLS-1$

		return oldPropertyName;
	}

	static boolean isOldOdaDriverProperty(String propertyName) {
		return !propertyName.equals(getNewOdaDriverProperty(propertyName));
	}

	static boolean isOdaDriverModelProperty(String propertyName) {
		if ("odaUser".equalsIgnoreCase(propertyName)) //$NON-NLS-1$
			return true;
		else if ("odaURL".equalsIgnoreCase(propertyName))//$NON-NLS-1$
			return true;
		else if ("odaDriverClass".equalsIgnoreCase(propertyName))//$NON-NLS-1$
			return true;
		else if ("odaDataSource".equalsIgnoreCase(propertyName))//$NON-NLS-1$
			return true;
		else if ("odaPassword".equalsIgnoreCase(propertyName))//$NON-NLS-1$
			return true;

		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.parser.AbstractPropertyState#parseAttrs(org.xml
	 * .sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		super.parseAttrs(attrs);

		if (isOldOdaDriverProperty(name) || isOdaDriverModelProperty(name)) {
			setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, "org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$

			name = getNewOdaDriverProperty(name);
		}
	}
}
