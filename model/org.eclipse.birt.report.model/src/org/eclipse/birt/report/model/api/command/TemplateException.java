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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Reports an error during a template related operation.
 * 
 */

public class TemplateException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 9034821979184205988L;

	/**
	 * Only report items and data sets can be transformed to template elements,
	 * other types of element are not supported.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE = MessageConstants.TEMPLATE_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE;

	/**
	 * Template elements are not supported by libraries.
	 */

	public static final String DESIGN_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED = MessageConstants.TEMPLATE_EXCEPTION_TEMPLATE_ELEMENT_NOT_SUPPORTED;

	/**
	 * The current element is not a template report item or the template report item
	 * has no template definition.
	 */

	public static final String DESIGN_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN = MessageConstants.TEMPLATE_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN;

	/**
	 * The current element is not a template data set or the template data set has
	 * no template definition.
	 */

	public static final String DESIGN_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN = MessageConstants.TEMPLATE_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN;

	/**
	 * The current element is not a template data set or the template data set has
	 * no template definition.
	 */

	public static final String DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN = MessageConstants.TEMPLATE_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN;

	/**
	 * The current report item or data set or template element is not in the design,
	 * it can not do any transformation.
	 */

	public static final String DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN = MessageConstants.TEMPLATE_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN;

	/**
	 * Constructs the exception with focus elementand error code.
	 * 
	 * @param element The design element of this exception focuses.
	 * @param errCode What went wrong.
	 */

	public TemplateException(DesignElement element, String errCode) {
		super(element, errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param element the element which has errors
	 * @param values  value array used for error message
	 * @param errCode the error code
	 */

	public TemplateException(DesignElement element, String[] values, String errCode) {
		super(element, values, errCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.exception.BirtException#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_INVALID_TEMPLATE_ELEMENT_TYPE
				|| sResourceKey == DESIGN_EXCEPTION_TRANSFORM_TO_DATA_SET_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_TRANSFORM_TO_REPORT_ITEM_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_CREATE_TEMPLATE_ELEMENT_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_REVERT_TO_TEMPLATE_FORBIDDEN) {
			assert element != null;
			return ModelMessages.getMessage(sResourceKey, new String[] { element.getIdentifier() });
		}
		return super.getLocalizedMessage();
	}
}
