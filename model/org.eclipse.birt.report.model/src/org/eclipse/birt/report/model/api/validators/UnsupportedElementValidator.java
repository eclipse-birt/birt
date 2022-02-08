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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates whether the element is unsupported in the current release. This
 * validator is only used for semantic check after opening a design file.
 * 
 * <h3>Rule</h3> The rule is that the element in the unsupported list is
 * unsupported now.
 * 
 * <h3>Applicability</h3> This validator is applied to
 * <code>DesignElement</code>.
 * 
 */

public class UnsupportedElementValidator extends AbstractElementValidator {

	private static final String[] unSupportedElements = { ReportDesignConstants.GRAPHIC_MASTER_PAGE_ELEMENT,
			ReportDesignConstants.FREE_FORM_ITEM, ReportDesignConstants.LINE_ITEM,
			ReportDesignConstants.RECTANGLE_ITEM };

	/**
	 * Name of this validator.
	 */

	public static final String NAME = "UnsupportedElementValidator"; //$NON-NLS-1$

	private final static UnsupportedElementValidator instance = new UnsupportedElementValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static UnsupportedElementValidator getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.core.AbstractElementValidator
	 * #validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		// Check whether this element is unsupported element.

		String elementName = element.getElementName();

		List<SemanticException> list = new ArrayList<SemanticException>();
		for (int i = 0; i < unSupportedElements.length; i++) {
			if (unSupportedElements[i].equalsIgnoreCase(elementName)) {
				list.add(new SemanticError(element, SemanticError.DESIGN_EXCEPTION_UNSUPPORTED_ELEMENT,
						SemanticError.WARNING));
			}
		}

		return list;
	}

}
