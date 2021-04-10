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
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Indicates an error while setting the extends property of an element.
 * 
 */

public class ExtendsException extends SemanticException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 4652036899546927124L;

	/**
	 * The new extends value.
	 */

	protected String extendsName = null;

	/**
	 * The parent element to set.
	 */

	protected DesignElement parent = null;

	/**
	 * No element exists with the parent name.
	 * 
	 * @deprecated pushed down to sub class <code>InvalidParentException</code>
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_FOUND = InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_FOUND;

	/**
	 * The parent element does not have the same type as the target element.
	 * 
	 * @deprecated pushed down to sub class <code>WrongTypeException</code>
	 */

	public static final String DESIGN_EXCEPTION_WRONG_TYPE = WrongTypeException.DESIGN_EXCEPTION_WRONG_TYPE;

	/**
	 * The parent element does not have the same type of extension as the target
	 * element. Both the parent and the target element are ExtendedItem.
	 * 
	 * @deprecated pushed down to sub class <code>WrongTypeException</code>
	 */

	public static final String DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE = WrongTypeException.DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE;

	/**
	 * The element does not allow to set extends explicitly.
	 * 
	 * @deprecated pushed down to sub class <code>ExtendsForbiddenException</code>
	 */

	public static final String DESIGN_EXCEPTION_EXTENDS_FORBIDDEN = ExtendsForbiddenException.DESIGN_EXCEPTION_EXTENDS_FORBIDDEN;

	/**
	 * The element does not allow extensions.
	 * 
	 * @deprecated pushed down to sub class <code>ExtendsForbiddenException</code>
	 */

	public static final String DESIGN_EXCEPTION_CANT_EXTEND = ExtendsForbiddenException.DESIGN_EXCEPTION_CANT_EXTEND;

	/**
	 * The element cannot extend from itself.
	 * 
	 * @deprecated pushed down to sub class <code>CircularExtendsException</code>
	 */

	public static final String DESIGN_EXCEPTION_SELF_EXTEND = CircularExtendsException.DESIGN_EXCEPTION_SELF_EXTEND;

	/**
	 * The extension would create a cycle: a extends b extends a.
	 * 
	 * @deprecated pushed down to sub class <code>CircularExtendsException</code>
	 */

	public static final String DESIGN_EXCEPTION_CIRCULAR = CircularExtendsException.DESIGN_EXCEPTION_CIRCULAR;

	/**
	 * The parent element has no name.
	 * 
	 * @deprecated pushed down to sub class <code>InvalidParentException</code>
	 */

	public static final String DESIGN_EXCEPTION_UNNAMED_PARENT = InvalidParentException.DESIGN_EXCEPTION_UNNAMED_PARENT;

	/**
	 * The parent element not in component slot of report design.
	 * 
	 * @deprecated pushed down to sub class <code>ExtendsForbiddenException</code>
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT = ExtendsForbiddenException.DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT;

	/**
	 * The library of the parent element is not included.
	 * 
	 * @deprecated pushed down to sub class <code>InvalidParentException</code>
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_INCLUDE = InvalidParentException.DESIGN_EXCEPTION_PARENT_NOT_INCLUDE;

	/**
	 * The element has no parent, it can not be localized.
	 * 
	 * @deprecated pushed down to sub class <code>InvalidParentException</code>
	 */

	public static final String DESIGN_EXCEPTION_NO_PARENT = InvalidParentException.DESIGN_EXCEPTION_NO_PARENT;

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param name    the value being set for the extends property.
	 * @param errCode what went wrong.
	 */

	public ExtendsException(DesignElement obj, String name, String errCode) {
		super(obj, errCode);
		extendsName = name;
	}

	/**
	 * Constructor.
	 * 
	 * @param obj     the element being changed.
	 * @param parent  the parent element.
	 * @param errCode what went wrong.
	 */

	public ExtendsException(DesignElement obj, DesignElement parent, String errCode) {
		super(obj, errCode);
		this.parent = parent;
		this.extendsName = parent.getFullName();
	}

	/**
	 * Returns the attempted new value for the extends property.
	 * 
	 * @return the parent element name.
	 */

	public Object getExtends() {
		return extendsName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		// For backward compatibility
		if (sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_FOUND || sResourceKey == DESIGN_EXCEPTION_CANT_EXTEND
				|| sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT) {
			return ModelMessages.getMessage(sResourceKey, new String[] { extendsName });
		} else if (sResourceKey == DESIGN_EXCEPTION_WRONG_TYPE || sResourceKey == DESIGN_EXCEPTION_CIRCULAR
				|| sResourceKey == DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE) {
			return ModelMessages.getMessage(sResourceKey,
					new String[] { getElementName(parent), getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_SELF_EXTEND || sResourceKey == DESIGN_EXCEPTION_EXTENDS_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_NO_PARENT) {
			return ModelMessages.getMessage(sResourceKey, new String[] { getElementName(element) });
		} else if (sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_INCLUDE) {
			return ModelMessages.getMessage(sResourceKey, new String[] { parent.getIdentifier() });
		}

		return ModelMessages.getMessage(sResourceKey);
	}
}