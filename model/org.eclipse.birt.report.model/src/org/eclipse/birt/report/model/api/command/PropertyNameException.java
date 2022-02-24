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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Exception thrown when a property name is invalid.
 * 
 */

public class PropertyNameException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 8269697054523876373L;

	/**
	 * The property/member name that caused the error.
	 */

	protected String name;

	/**
	 * A structure object.
	 */

	protected IStructure struct;

	/**
	 * The property name is not defined on the element.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_NAME_INVALID = MessageConstants.PROPERTY_NAME_EXCEPTION_PROPERTY_NOT_VALID;

	/**
	 * The member name is not defined on a structure.
	 */

	public static final String DESIGN_EXCEPTION_MEMBER_NAME_INVALID = MessageConstants.PROPERTY_NAME_EXCEPTION_MEMBER_NOT_VALID;

	/**
	 * Constructor.
	 * 
	 * @param obj      the element that has the property.
	 * @param propName the property name that caused the error
	 */

	public PropertyNameException(DesignElement obj, String propName) {
		super(obj, DESIGN_EXCEPTION_PROPERTY_NAME_INVALID);
		name = propName;
	}

	/**
	 * Constructs a exception given a structure and its element and the invalid
	 * member name.
	 * 
	 * @param obj        the element that has the structure.
	 * @param struct     the structure that doesn't contain the member.
	 * @param memberName the member name that caused the exception.
	 */

	public PropertyNameException(DesignElement obj, IStructure struct, String memberName) {
		super(obj, DESIGN_EXCEPTION_MEMBER_NAME_INVALID);
		this.name = memberName;
		this.struct = struct;
	}

	/**
	 * Returns the invalid property name.
	 * 
	 * @return the invalid property name.
	 */

	public String getPropertyName() {
		return name;
	}

	/**
	 * Return the invalid member name.
	 * 
	 * @return the invalid member name.
	 */

	public String getMemberName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage() {
		if (sResourceKey == DESIGN_EXCEPTION_PROPERTY_NAME_INVALID) {
			String elementName = element == null ? "" : element.getFullName(); //$NON-NLS-1$
			return ModelMessages.getMessage(DESIGN_EXCEPTION_PROPERTY_NAME_INVALID, new String[] { name, elementName });
		} else if (sResourceKey == DESIGN_EXCEPTION_MEMBER_NAME_INVALID) {
			String structName = struct == null ? "" : struct.getStructName(); //$NON-NLS-1$
			return ModelMessages.getMessage(DESIGN_EXCEPTION_MEMBER_NAME_INVALID, new String[] { name, structName });
		}
		return ""; //$NON-NLS-1$
	}
}
