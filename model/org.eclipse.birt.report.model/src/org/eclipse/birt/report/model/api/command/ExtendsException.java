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
 * Indicates an error while setting the extends property of an element.
 *  
 */

public class ExtendsException extends SemanticException
{

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
	 */

	public static final String DESIGN_EXCEPTION_NOT_FOUND = MessageConstants.EXTENDS_EXCEPTION_NOT_FOUND;

	/**
	 * The parent element does not have the same type as the target element.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_TYPE = MessageConstants.EXTENDS_EXCEPTION_WRONG_TYPE;

	/**
	 * The parent element does not have the same type of extension as the target
	 * element. Both the parent and the target element are ExtendedItem.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE = MessageConstants.EXTENDS_EXCEPTION_WRONG_EXTENSION_TYPE;

	/**
	 * The element does not allow extensions.
	 */

	public static final String DESIGN_EXCEPTION_CANT_EXTEND = MessageConstants.EXTENDS_EXCEPTION_CANT_EXTEND;

	/**
	 * The element cannot extend from itself.
	 */

	public static final String DESIGN_EXCEPTION_SELF_EXTEND = MessageConstants.EXTENDS_EXCEPTION_SELF_EXTEND;

	/**
	 * The extension would create a cycle: a extends b extends a.
	 */

	public static final String DESIGN_EXCEPTION_CIRCULAR = MessageConstants.EXTENDS_EXCEPTION_CIRCULAR;

	/**
	 * The parent element has no name.
	 */

	public static final String DESIGN_EXCEPTION_UNNAMED_PARENT = MessageConstants.EXTENDS_EXCEPTION_UNNAMED_PARENT;

	/**
	 * The parent element not in component slot of report design.
	 */

	public static final String DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT = MessageConstants.EXTENDS_EXCEPTION_PARENT_NOT_IN_COMPONENT;

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element being changed.
	 * @param name
	 *            the value being set for the extends property.
	 * @param errCode
	 *            what went wrong.
	 */

	public ExtendsException( DesignElement obj, String name, String errCode )
	{
		super( obj, errCode );
		extendsName = name;
	}

	/**
	 * Constructor.
	 * 
	 * @param obj
	 *            the element being changed.
	 * @param parent
	 *            the parent element.
	 * @param errCode
	 *            what went wrong.
	 */

	public ExtendsException( DesignElement obj, DesignElement parent,
			String errCode )
	{
		super( obj, errCode );
		this.parent = parent;
		this.extendsName = parent.getName( );
	}

	/**
	 * Returns the attempted new value for the extends property.
	 * 
	 * @return the parent element name.
	 */

	public Object getExtends( )
	{
		return extendsName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage( )
	{
		if ( sResourceKey == DESIGN_EXCEPTION_NOT_FOUND
				|| sResourceKey == DESIGN_EXCEPTION_CANT_EXTEND
				|| sResourceKey == DESIGN_EXCEPTION_PARENT_NOT_IN_COMPONENT )
		{
			return ModelMessages.getMessage( sResourceKey,
					new String[]{extendsName} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_WRONG_TYPE
				|| sResourceKey == DESIGN_EXCEPTION_CIRCULAR
				|| sResourceKey == DESIGN_EXCEPTION_WRONG_EXTENSION_TYPE )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{
					getElementName( parent ), getElementName( element )} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_SELF_EXTEND )
		{
			return ModelMessages.getMessage( sResourceKey,
					new String[]{getElementName( element )} );
		}

		return ModelMessages.getMessage( sResourceKey );
	}
}