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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * An error indicates invalid parent while setting the extends property of an
 * element.
 */

public class InvalidParentException extends ExtendsException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 4335031720216166533L;

	/**
	 * No element exists with the parent name.
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_FOUND = MessageConstants.INVALID_PARENT_EXCEPTION_PARENT_NOT_FOUND;

	/**
	 * The library of the parent element is not included.
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_INCLUDE = MessageConstants.INVALID_PARENT_EXCEPTION_PARENT_NOT_INCLUDE;

	/**
	 * The element has no parent, it can not be localized.
	 */

	public static final String DESIGN_EXCEPTION_NO_PARENT = MessageConstants.INVALID_PARENT_EXCEPTION_NO_PARENT;

	/**
	 * The parent element has no name.
	 */

	public static final String DESIGN_EXCEPTION_UNNAMED_PARENT = MessageConstants.INVALID_PARENT_EXCEPTION_UNNAMED_PARENT;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param name    the value being set for the extends property.
	 * @param errCode what went wrong.
	 */

	public InvalidParentException(DesignElement obj, String name, String errCode) {
		super(obj, name, errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param parent  the parent element.
	 * @param errCode what went wrong.
	 */

	public InvalidParentException(DesignElement obj, DesignElement parent, String errCode) {
		super(obj, parent, errCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_FOUND) {
			return ModelMessages.getMessage(sResourceKey, new String[] { extendsName });
		} else if (sResourceKey == DESIGN_EXCEPTION_NO_PARENT) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_INCLUDE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { parent.getIdentifier() });
		}

		return super.getLocalizedMessage();
	}
}
