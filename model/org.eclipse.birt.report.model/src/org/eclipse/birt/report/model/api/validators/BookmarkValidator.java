/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

public class BookmarkValidator extends AbstractElementValidator {

	private static final BookmarkValidator instance = new BookmarkValidator();

	private static final String BOOKMARK_PATTERN = "[A-Za-z]{1}[A-Za-z0-9\\-_\\:\\.]*";

	public static BookmarkValidator getInstance() {
		return instance;
	}

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		List<SemanticException> ret = new ArrayList<SemanticException>();
		Object obj = element.getProperty(module, IReportItemModel.BOOKMARK_PROP);
		if (obj != null && obj instanceof Expression) {
			Expression expr = (Expression) obj;
			if (ExpressionType.CONSTANT.equals(expr.getType())) {
				if (!Pattern.matches(BOOKMARK_PATTERN, expr.getStringExpression())) {
					ret.add(new SemanticException(element,
							"The bookmark is invalid, must begin with a letter(A-Za-z) and be followed by these chars (A-Za-z0-9-_:.)"));
				}
			}
			return ret;
		}
		return Collections.emptyList();
	}
}
