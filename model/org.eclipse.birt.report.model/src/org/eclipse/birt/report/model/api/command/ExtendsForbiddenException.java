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
 * An error indicates not supported extension while setting the extends property
 * of an element.
 */

public class ExtendsForbiddenException extends ExtendsException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 7129821443284155448L;

	/**
	 * The element does not allow to set extends explicitly.
	 */

	public static final String DESIGN_EXCEPTION_EXTENDS_FORBIDDEN = MessageConstants.EXTENDS_FORBIDDEN_EXCEPTION_EXTENDS_FORBIDDEN;

	/**
	 * The element does not allow extensions.
	 */

	public static final String DESIGN_EXCEPTION_CANT_EXTEND = MessageConstants.EXTENDS_FORBIDDEN_EXCEPTION_CANT_EXTEND;

	/**
	 * The parent element not in component slot of report design.
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT = MessageConstants.EXTENDS_FORBIDDEN_EXCEPTION_PARENT_NOT_IN_COMPONENT;

	/**
	 * The shared result set report item cannot be extended.
	 */

	public static final String DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND = MessageConstants.EXTENDS_FORBIDDEN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND;

	/**
	 * Constructor.
	 *
	 * @param obj     the element being changed.
	 * @param name    the value being set for the extends property.
	 * @param errCode what went wrong.
	 */

	public ExtendsForbiddenException(DesignElement obj, String name, String errCode) {
		super(obj, name, errCode);
	}

	/**
	 * Constructor.
	 *
	 * @param obj     the element being changed.
	 * @param parent  the parent element.
	 * @param errCode what went wrong.
	 */

	public ExtendsForbiddenException(DesignElement obj, DesignElement parent, String errCode) {
		super(obj, parent, errCode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	@Override
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_CANT_EXTEND || sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT
				|| sResourceKey == DESIGN_EXCEPTION_RESULT_SET_SHARED_CANT_EXTEND) {
			return ModelMessages.getMessage(sResourceKey, new String[] { extendsName });
		} else if (sResourceKey == DESIGN_EXCEPTION_EXTENDS_FORBIDDEN) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		}

		return super.getLocalizedMessage();
	}
}
