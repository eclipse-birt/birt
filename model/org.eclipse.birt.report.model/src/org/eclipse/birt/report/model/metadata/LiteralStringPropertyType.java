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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.ReportDesign;

/**
 * Represents literal string property type. The literal string value is kept
 * exactly as what is given. No change like trim is performed on its value.
 */

public class LiteralStringPropertyType extends TextualPropertyType
{
    /**
	 * Display name key.
	 */
	
	private static final String DISPLAY_NAME_KEY = "Property.literalString"; //$NON-NLS-1$

	LiteralStringPropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getTypeCode()
	 */
	public int getTypeCode( )
	{
		return LITERAL_STRING_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#getName()
	 */
	public String getName( )
	{
		return LITERAL_STRING_TYPE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org.eclipse.birt.report.model.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */
	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;
		if ( value instanceof String )
			return (String) value;
		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				LITERAL_STRING_TYPE );
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateXml(org.eclipse.birt.report.model.elements.ReportDesign, org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.String)
	 */
	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		return validateValue( design, defn, value );
	}
}