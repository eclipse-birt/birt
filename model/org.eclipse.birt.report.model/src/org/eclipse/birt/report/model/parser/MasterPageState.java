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

import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * This class parses a master page.
 *
 */

public abstract class MasterPageState extends ReportElementState {

	/**
	 * The master page being created.
	 */

	protected MasterPage element = null;

	/**
	 * Constructs the master page state with the design file parser handler.
	 *
	 * @param handler the design file parser handler
	 */

	public MasterPageState(ModuleParserHandler handler) {
		super(handler, handler.getModule(), IModuleModel.PAGE_SLOT);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.DesignParseState#getElement()
	 */

	@Override
	public DesignElement getElement() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.parser.ReportElementState#end()
	 */
	@Override
	public void end() throws SAXException {

		if (handler.versionNumber < VersionUtil.VERSION_3_2_18) {
			setMargin(IMasterPageModel.LEFT_MARGIN_PROP, 1.25);
			setMargin(IMasterPageModel.RIGHT_MARGIN_PROP, 1.25);
			setMargin(IMasterPageModel.TOP_MARGIN_PROP, 1);
			setMargin(IMasterPageModel.BOTTOM_MARGIN_PROP, 1);

		}

		super.end();
	}

	/**
	 * Sets the margin properties of the master page if the values of these
	 * properties are not set.
	 *
	 * @param marginProp  the the margin property name.
	 * @param marginValue the margin value.
	 */
	private void setMargin(String marginProp, double marginValue) {
		ElementPropertyDefn prop = element.getPropertyDefn(marginProp);

		Object value = element.getStrategy().getPropertyExceptRomDefault(handler.module, element, prop);

		if (value == null) {
			DimensionValue dimension = new DimensionValue(marginValue, DesignChoiceConstants.UNITS_IN);
			element.setProperty(prop, dimension);
		}
	}
}
