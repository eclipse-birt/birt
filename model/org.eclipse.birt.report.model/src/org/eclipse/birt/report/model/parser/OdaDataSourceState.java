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

import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.extension.oda.OdaDummyProvider;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses the oda data source element.
 * 
 */

public class OdaDataSourceState extends DataSourceState {

	/**
	 * Old extension id of flat file in BIRT 1.0 or before.
	 */

	private static final String OBSOLETE_FLAT_FILE_ID = "org.eclipse.birt.report.data.oda.flatfile"; //$NON-NLS-1$

	/**
	 * Extension id of flat file in BIRT 2.0.
	 */

	private static final String NEW_FLAT_FILE_ID = "org.eclipse.datatools.connectivity.oda.flatfile"; //$NON-NLS-1$

	/**
	 * <code>true</code> if the extension can be found. Otherwise
	 * <code>false</code>.
	 */

	private boolean isValidExtensionId = true;

	/**
	 * The provider of the element.
	 */

	private ODAProvider provider = null;

	/**
	 * Constructs the oda data source state with the design parser handler, the
	 * container element and the container slot of the oda data source.
	 * 
	 * @param handler the design file parser handler
	 */

	public OdaDataSourceState(ModuleParserHandler handler, int slot) {
		super(handler, slot);
		element = new OdaDataSource();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		parseODADataSourceExtensionID(attrs, false);

		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */
	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();
		if (ParserSchemaConstants.PROPERTY_TAG == tagValue) {
			if (handler.isVersion(VersionUtil.VERSION_0) || handler.isVersion(VersionUtil.VERSION_1_0_0)) {
				return new CompatibleOdaDataSourcePropertyState(handler, getElement());
			}
		}

		// if the extension id is OK, use normal procedure to parse the design
		// file. Otherwise, use dummy state to parse.

		if (isValidExtensionId)
			return super.startElement(tagName);

		return ParseStateFactory.getInstance().createParseState(tagName, handler, element,
				((OdaDummyProvider) provider).getContentTree());
	}

	/**
	 * Parse the attribute of "extensionId" for extendable element.
	 * 
	 * @param attrs                 the SAX attributes object
	 * @param extensionNameRequired whether extension name is required
	 */

	private void parseODADataSourceExtensionID(Attributes attrs, boolean extensionNameRequired) {
		String extensionID = getAttrib(attrs, DesignSchemaConstants.EXTENSION_ID_ATTRIB);

		if (StringUtil.isBlank(extensionID)) {
			if (!extensionNameRequired)
				return;

			SemanticError e = new SemanticError(element, SemanticError.DESIGN_EXCEPTION_MISSING_EXTENSION);
			RecoverableError.dealMissingInvalidExtension(handler, e);

			return;
		}
		if (handler.versionNumber < VersionUtil.VERSION_3_0_0) {
			if (OBSOLETE_FLAT_FILE_ID.equalsIgnoreCase(extensionID))
				extensionID = NEW_FLAT_FILE_ID;
		}

		setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, extensionID);

		// get the provider to check whether this is a valid oda extension

		provider = ((OdaDataSource) element).getProvider();

		if (provider == null)
			return;

		if (provider instanceof OdaDummyProvider) {
			SemanticError e = new SemanticError(element, new String[] { extensionID },
					SemanticError.DESIGN_EXCEPTION_EXTENSION_NOT_FOUND);
			RecoverableError.dealMissingInvalidExtension(handler, e);
			isValidExtensionId = false;
		} else {
			// After version 3.2.7 , add convert fuction.

			String newExtensionID = provider.convertExtensionID();
			if (!extensionID.equals(newExtensionID)) {
				setProperty(IOdaExtendableElementModel.EXTENSION_ID_PROP, newExtensionID);
			}
		}
	}
}