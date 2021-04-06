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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the page size against page type.
 * 
 * <h3>Rule</h3> The rule is that if the value of
 * <code>MasterPage.TYPE_PROP</code> is
 * <code>DesignChoiceConstants.PAGE_SIZE_CUSTOM</code>, the
 * <code>MasterPage.HEIGHT_PROP</code> and <code>MasterPage.WIDTH_PROP</code>
 * must be set; otherwise, they should be empty.
 * 
 * <h3>Applicability</h3> This validator is only applied to
 * <code>MasterPage</code>.
 */

public class MasterPageTypeValidator extends AbstractElementValidator {

	private static MasterPageTypeValidator instance = new MasterPageTypeValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static MasterPageTypeValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the page type and size are consistent.
	 * 
	 * @param module  the module
	 * @param element the master page to validate
	 * 
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof MasterPage))
			return Collections.emptyList();

		return doValidate(module, (MasterPage) element);
	}

	private List<SemanticException> doValidate(Module module, MasterPage toValidate) {
		List<SemanticException> list = new ArrayList<SemanticException>();

		String type = toValidate.getStringProperty(module, IMasterPageModel.TYPE_PROP);
		String height = toValidate.getStringProperty(module, IMasterPageModel.HEIGHT_PROP);
		String width = toValidate.getStringProperty(module, IMasterPageModel.WIDTH_PROP);

		// if type is CUSTOM type, height and width must be specified.

		if (DesignChoiceConstants.PAGE_SIZE_CUSTOM.equalsIgnoreCase(type)) {
			// if type is CUSTOM type, height and width must be specified.

			if (StringUtil.isBlank(height) || StringUtil.isBlank(width)) {
				list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_MISSING_PAGE_SIZE));
			}
		}

		return list;
	}

}
