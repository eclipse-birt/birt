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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.TemplateDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * This class parses a template data set.
 */

public class TemplateDataSetState extends ReportElementState {

	/**
	 * The template data set being created.
	 */

	protected TemplateDataSet element = null;

	/**
	 * Constructs the template data set state with the design parser handler.
	 * 
	 * @param handler the design file parser handler
	 */

	public TemplateDataSetState(ModuleParserHandler handler, Module module, int slot) {
		super(handler, module, slot);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs(Attributes attrs) throws XMLParserException {
		element = new TemplateDataSet();

		initElement(attrs, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		DesignElement refTemplateParam = element.getTemplateParameterElement(handler.getModule());
		if (refTemplateParam != null) {
			DesignElement defaultElement = element.getDefaultElement(handler.getModule());
			if (!(defaultElement instanceof SimpleDataSet)) {
				handler.getErrorHandler()
						.semanticError(new DesignParserException(
								new String[] { element.getIdentifier(), refTemplateParam.getIdentifier() },
								DesignParserException.DESIGN_EXCEPTION_INCONSISTENT_TEMPLATE_ELEMENT_TYPE));
			} else {
				if (handler.versionNumber < VersionUtil.VERSION_3_2_2) {
					List dataSetColumns = (List) element.getProperty(handler.module, IDataSetModel.RESULT_SET_PROP);
					Object dataSetHints = element.getProperty(handler.module, IDataSetModel.RESULT_SET_HINTS_PROP);
					if (dataSetHints == null && dataSetColumns != null)
						element.setProperty(IDataSetModel.RESULT_SET_HINTS_PROP, ModelUtil.copyValue(
								element.getPropertyDefn(IDataSetModel.RESULT_SET_HINTS_PROP), dataSetColumns));
				}
			}
		} else {
			// fire an error
		}
		super.end();
	}

}
