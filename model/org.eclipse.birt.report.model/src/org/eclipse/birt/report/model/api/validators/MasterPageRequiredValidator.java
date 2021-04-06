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
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the report should have at least one master page.
 * 
 * <h3>Rule</h3> The rule is that the report should have at least one master
 * page.
 * 
 * <h3>Applicability</h3> This validator is only applied to
 * <code>ReportDesign</code>.
 */

public class MasterPageRequiredValidator extends AbstractElementValidator {

	private static MasterPageRequiredValidator instance = new MasterPageRequiredValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static MasterPageRequiredValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the given report has one master page.
	 * 
	 * @param module  the module
	 * @param element the report to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof ReportDesign))
			return Collections.emptyList();

		List<SemanticException> list = new ArrayList<SemanticException>();

		ReportDesign report = (ReportDesign) element;

		if (report.getSlot(IModuleModel.PAGE_SLOT).getCount() == 0) {
			list.add(new SemanticError(report, SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE));
		}

		return list;
	}
}