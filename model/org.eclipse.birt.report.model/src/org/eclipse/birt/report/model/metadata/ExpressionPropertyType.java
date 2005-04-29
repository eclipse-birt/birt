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
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;


/**
 * Expression property type. Expressions are stored as strings. Expressions
 * are not validated at design time; instead we rely on the runtime
 * script engine for validation. This allows the user to temporarily
 * store incorrect expressions while they work on a report. Doing so
 * is like storing an invalid Java file while writing code.
 */

public class ExpressionPropertyType extends TextualPropertyType
{
    /**
	 * Display name key.
	 */
	
	private static final String DISPLAY_NAME_KEY = "Property.expression"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	
	public ExpressionPropertyType( )
	{
	    super( DISPLAY_NAME_KEY );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */
	
	public int getTypeCode( )
	{
		return EXPRESSION_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */
	
	public String getName( )
	{
		return EXPRESSION_TYPE_NAME;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java.lang.Object)
	 */
	
	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		return (String) value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.metadata.PropertyType#validateValue(org.eclipse.birt.report.model.elements.ReportDesign, org.eclipse.birt.report.model.metadata.PropertyDefn, java.lang.Object)
	 */
    
	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
        if ( value == null )
            return null;
        if ( value instanceof String )
        	return value;
            
        throw new PropertyValueException( value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, EXPRESSION_TYPE );
    }
}
