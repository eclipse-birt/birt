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
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.Point;
import org.eclipse.birt.report.model.api.util.Rectangle;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the multiple columns and content width should be consistent. This
 * validator should be performed after <code>MasterPageTypeValidator</code> and
 * <code>MasterPageSizeValidator</code>.
 * 
 * <h3>Rule</h3> The rule is that the width occupied by column spacing is less
 * than the content width.
 * <p>
 * column spacing width = (COLUMNS_PROP - 1) * COLUMN_SPACING_PROP
 * <p>
 * content width = WIDTH_PROP - LEFT_MARGIN_PROP - RIGHT_MARGIN_PROP
 * <p>
 * column spacing width < content width
 * 
 * <h3>Applicability</h3> This validator is only applied to
 * <code>GraphicMasterPage</code>.
 * 
 * @see MasterPageTypeValidator
 * @see MasterPageSizeValidator
 */

public class MasterPageMultiColumnValidator extends AbstractElementValidator {

	private final static MasterPageMultiColumnValidator instance = new MasterPageMultiColumnValidator();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static MasterPageMultiColumnValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether multiple columns and content width are consistent.
	 * 
	 * @param module  the module
	 * @param element the graphic master page to validate
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

		// Check margins. Must start on the page and not be of negative
		// size.
		Rectangle margins = toValidate.getContentArea(module);
		Point size = toValidate.getSize(module);
		if (!(margins.x >= size.x || margins.y >= size.y || margins.height <= 0 || margins.width <= 0)) {
			int columns = toValidate.getIntProperty(module, IMasterPageModel.COLUMNS_PROP);
			double columnSpacing = toValidate.getFloatProperty(module, IMasterPageModel.COLUMN_SPACING_PROP);
			if (margins.width < (columns - 1) * columnSpacing) {
				list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN));
			}
		}
		return list;
	}
}