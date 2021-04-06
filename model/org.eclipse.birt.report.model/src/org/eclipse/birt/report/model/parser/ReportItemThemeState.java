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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportItemTheme;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractThemeModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemThemeModel;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * This class parses a theme in the library.
 */

class ReportItemThemeState extends ReportElementState {

	/**
	 * The report item theme being created.
	 */

	protected ReportItemTheme element;

	/**
	 * Constructs the report item theme state with the design parser handler, the
	 * container element and the container slot of the table row.
	 * 
	 * @param handler      the design file parser handler
	 * @param theContainer the element that contains this one
	 * @param slot         the slot in which this element appears
	 */

	ReportItemThemeState(ModuleParserHandler handler, DesignElement theContainer, int slot) {
		super(handler, theContainer, slot);

	}

	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement(String tagName) {
		int tagValue = tagName.toLowerCase().hashCode();

		if (ParserSchemaConstants.STYLES_TAG == tagValue)
			return new StylesState(handler, getElement(), IAbstractThemeModel.STYLES_SLOT);
		return super.startElement(tagName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new ReportItemTheme();

		String type = getAttrib(attrs, DesignSchemaConstants.TYPE_ATTRIB);
		type = StringUtil.trimString(type);

		if (!ReportItemTheme.isValidType(type)) {
			RecoverableError.dealInvalidPropertyValue(handler,
					new PropertyValueException(element, IReportItemThemeModel.TYPE_PROP, type,
							PropertyValueException.DESIGN_EXCEPTION_NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE));
		}
		setProperty(IReportItemThemeModel.TYPE_PROP, type);

		initElement(attrs, true);
	}

}
