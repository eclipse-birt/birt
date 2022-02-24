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
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.api.util.Rectangle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the page size is invalid.
 * 
 * <h3>Rule</h3> The rule is that
 * <ul>
 * <li>the <code>MasterPage.HEIGHT_PROP</code> and
 * <code>MasterPage.WIDTH_PROP</code> must be larger than or equals zero.
 * <li>the margin space shouldn't occupy all page space.
 * </ul>
 * 
 * <h3>Applicability</h3> This validator is only applied to
 * <code>MasterPage</code>.
 */

public class MasterPageSizeValidator extends AbstractElementValidator {

	private static MasterPageSizeValidator instance = new MasterPageSizeValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static MasterPageSizeValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the page size is invalid.
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

		// Validate the size. Must be positive in both dimensions.

		Point size = toValidate.getSize(module);
		if (size.x <= 0 || size.y <= 0) {
			list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_SIZE));
		} else {
			// Check margins. Must start on the page and not be of negative
			// size.

			Rectangle margins = toValidate.getContentArea(module);
			if (margins.x >= size.x || margins.y >= size.y || margins.height <= 0 || margins.width <= 0) {
				list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_INVALID_PAGE_MARGINS));
			}
		}

		return list;
	}
}
