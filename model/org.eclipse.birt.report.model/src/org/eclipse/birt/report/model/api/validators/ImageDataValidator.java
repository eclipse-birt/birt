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
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * If the image source is set, the corresponding image data should be set.
 *
 */

public class ImageDataValidator extends AbstractElementValidator {

	private final static ImageDataValidator instance = new ImageDataValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static ImageDataValidator getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.validators.AbstractElementValidator#validate
	 * (org.eclipse.birt.report.model.core.Module,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement image) {

		List<SemanticException> list = new ArrayList<>();

		String refType = image.getStringProperty(module, IImageItemModel.SOURCE_PROP);

		if (DesignChoiceConstants.IMAGE_REF_TYPE_EXPR.equalsIgnoreCase(refType)) {
			String valueExpr = image.getStringProperty(module, IImageItemModel.VALUE_EXPR_PROP);
			if (StringUtil.isEmpty(valueExpr)) {
				list.add(new SemanticError(image, SemanticError.DESIGN_EXCEPTION_INVALID_IMAGEREF_EXPR_VALUE));
			}
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(refType)
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(refType)) {
			String uri = image.getStringProperty(module, IImageItemModel.URI_PROP);
			if (StringUtil.isEmpty(uri)) {
				list.add(new SemanticError(image, SemanticError.DESIGN_EXCEPTION_INVALID_IMAGE_URL_VALUE));
			}
		} else if (DesignChoiceConstants.IMAGE_REF_TYPE_EMBED.equalsIgnoreCase(refType)) {
			String name = image.getStringProperty(module, IImageItemModel.IMAGE_NAME_PROP);

			if (StringUtil.isEmpty(name)) {
				list.add(new SemanticError(image, SemanticError.DESIGN_EXCEPTION_INVALID_IMAGE_NAME_VALUE));
			}
		}

		return list;
	}
}
