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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * An error indicates wrong type while setting the extends property of an
 * element.
 */

public class WrongTypeException extends ExtendsException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 8660438792837420578L;

	/**
	 * The parent element does not have the same type as the target element.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_TYPE = MessageConstants.WRONG_TYPE_EXCEPTION_WRONG_TYPE;

	/**
	 * The parent element does not have the same type of extension as the target
	 * element. Both the parent and the target element are ExtendedItem.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE = MessageConstants.WRONG_TYPE_EXCEPTION_WRONG_EXTENSION_TYPE;

	/**
	 * Constructor.
	 *
	 * @param obj     the element being changed.
	 * @param name    the value being set for the extends property.
	 * @param errCode what went wrong.
	 */

	public WrongTypeException(DesignElement obj, String name, String errCode) {
		super(obj, name, errCode);
	}

	/**
	 * Constructor.
	 *
	 * @param obj     the element being changed.
	 * @param parent  the parent element.
	 * @param errCode what went wrong.
	 */

	public WrongTypeException(DesignElement obj, DesignElement parent, String errCode) {
		super(obj, parent, errCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_WRONG_TYPE || sResourceKey == DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(parent), getElementName(element) });
		}

		return super.getLocalizedMessage();
	}
}
