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
 * An error indicates circular extension while setting the extends property of
 * an element.
 */

public class CircularExtendsException extends ExtendsException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -3188813948002142970L;

	/**
	 * The element cannot extend from itself.
	 */

	public static final String DESIGN_EXCEPTION_SELF_EXTEND = MessageConstants.CIRCULAR_EXTENDS_EXCEPTION_SELF_EXTEND;

	/**
	 * The extension would create a cycle: a extends b extends a.
	 */

	public static final String DESIGN_EXCEPTION_CIRCULAR = MessageConstants.CIRCULAR_EXTENDS_EXCEPTION_CIRCULAR;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param name    the value being set for the extends property.
	 * @param errCode what went wrong.
	 */

	public CircularExtendsException(DesignElement obj, String name, String errCode) {
		super(obj, name, errCode);
	}

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param parent  the parent element.
	 * @param errCode what went wrong.
	 */

	public CircularExtendsException(DesignElement obj, DesignElement parent, String errCode) {
		super(obj, parent, errCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_CIRCULAR) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(parent), getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_SELF_EXTEND) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		}

		return super.getLocalizedMessage();
	}
}
