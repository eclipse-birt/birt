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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IAutoTextModel;

/**
 * Represents a autotext report item. A autotext item supports page number and
 * total page . The autotext has the following properties:
 *
 * <ul>
 * <li>An autotext choice type counts the page number or total page number
 * </ul>
 */

public class AutoTextHandle extends ReportItemHandle implements IAutoTextModel {

	/**
	 * Constructs a autotext handle with the given design and the element. The
	 * application generally does not create handles directly. Instead, it uses one
	 * of the navigation methods available on other element handles.
	 *
	 * @param module  the module
	 * @param element the model representation of the element
	 */

	public AutoTextHandle(Module module, DesignElement element) {
		super(module, element);

	}

	/**
	 * Returns the autotext type for this parameter. The autotext type counts the
	 * page number or total page number. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 *
	 * <ul>
	 * <li><code>AUTO_TEXT_PAGE_NUMBER</code>
	 * <li><code>AUTO_TEXT_TOTAL_PAGE</code>
	 * <li><code>AUTO_TEXT_PAGE_NUMBER_UNFILTERED</code>
	 * <li><code>AUTO_TEXT_TOTAL_PAGE_UNFILTERED</code>
	 * <li><code>AUTO_TEXT_PAGE_VARIABLE</code>
	 * </ul>
	 *
	 * @return the type for the parameter
	 *
	 * @see #setAutoTextType(String)
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public String getAutoTextType() {
		return getStringProperty(IAutoTextModel.AUTOTEXT_TYPE_PROP);
	}

	/**
	 * Sets the autotext type for this parameter. The autotext type counts the page
	 * number or total page number. Types are defined in
	 * <code>DesignChoiceConstants</code> can be one of the followings:
	 *
	 * <ul>
	 * <li><code>AUTO_TEXT_PAGE_NUMBER</code>
	 * <li><code>AUTO_TEXT_TOTAL_PAGE</code>
	 * <li><code>AUTO_TEXT_PAGE_NUMBER_UNFILTERED</code>
	 * <li><code>AUTO_TEXT_TOTAL_PAGE_UNFILTERED</code>
	 * <li><code>AUTO_TEXT_PAGE_VARIABLE</code>
	 * </ul>
	 *
	 * @param type the type for the parameter
	 *
	 * @throws SemanticException if the input type is not one of above choices.
	 * @see #getAutoTextType()
	 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
	 */

	public void setAutoTextType(String type) throws SemanticException {
		setStringProperty(IAutoTextModel.AUTOTEXT_TYPE_PROP, type);
	}

	/**
	 * Gets the page variable property value.
	 *
	 * @return the page variable property value.
	 */
	public String getPageVariable() {
		return getStringProperty(PAGE_VARIABLE_PROP);
	}

	/**
	 * Sets the page variable property value.
	 *
	 * @param pageVariable page variable property value.
	 * @throws SemanticException
	 */
	public void setPageVariable(String pageVariable) throws SemanticException {
		setStringProperty(PAGE_VARIABLE_PROP, pageVariable);
	}

}
