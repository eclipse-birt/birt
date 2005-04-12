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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.validators.AbstractPropertyValidator;

/**
 * Validates the property value is required.
 * 
 * <h3>Rule</h3>
 * The rule is that the value should be provided for one required property.
 * 
 * <h3>Applicability</h3>
 * This validator is only applied to the required properties of
 * <code>DesignElement</code>.
 */

public class ValueRequiredValidator extends AbstractPropertyValidator
{

	/**
	 * Name of this validator
	 */

	public final static String NAME = "ValueRequiredValidator"; //$NON-NLS-1$

	private static ValueRequiredValidator instance = new ValueRequiredValidator( );

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static ValueRequiredValidator getInstance( )
	{
		return instance;
	}

	/**
	 * Validates whether value is set for the given required property.
	 * 
	 * @param design
	 *            the report design
	 * @param element
	 *            the element holding this required property
	 * @param propName
	 *            the name of the property to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	public List validate( ReportDesign design, DesignElement element,
			String propName )
	{
		List list = new ArrayList( );

		Object value = element.getProperty( design, propName );
		if ( value == null
				|| ( value instanceof String && StringUtil
						.isBlank( (String) value ) ) )
		{
			list.add( new PropertyValueException( element, propName, value,
					PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED ) );
		}

		return list;
	}

}