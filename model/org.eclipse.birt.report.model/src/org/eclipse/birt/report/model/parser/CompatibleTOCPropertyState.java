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

import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.xml.sax.SAXException;

/**
 * Parsed toc string expression to toc structure.
 * <p>
 * The conversion is done from the file version 3.2.9.
 */

public class CompatibleTOCPropertyState extends CompatiblePropertyState {

	/**
	 * Default constructor.
	 *
	 * @param theHandler the parser handler
	 * @param element    the element to parse
	 * @param propDefn   the property definition
	 * @param struct     the structure of OdaDataSetParameter
	 */

	public CompatibleTOCPropertyState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.PropertyState#end()
	 */

	@Override
	public void end() throws SAXException {
		String value = text.toString();
		TOC toc = StructureFactory.createTOC(value);
		if (element instanceof ReportItem) {
			element.setProperty(IReportItemModel.TOC_PROP, toc);
		}
		if (element instanceof GroupElement) {
			element.setProperty(IGroupElementModel.TOC_PROP, toc);
		}

	}
}
