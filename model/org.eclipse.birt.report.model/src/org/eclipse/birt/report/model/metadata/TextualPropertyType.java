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
 * Base class for property types stored as literal strings.
 *
 */

public abstract class TextualPropertyType extends PropertyType
{

    /**
     * Constructor 
     * 
     * @param displayNameID display name id of the property type.
     */
    
    TextualPropertyType( String displayNameID )
    {
        super( displayNameID );
    }
    
    
	/**
	 * Validates a generic string.
	 * 
	 * @return the value as a string
	 */

	public Object validateValue( ReportDesign design,    
			PropertyDefn defn, Object value )
		throws PropertyValueException
	{
		if ( value == null )
			return null;
		if ( value instanceof String )
			return StringUtil.trimString( (String) value );
		throw new PropertyValueException( value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, VARIANT_TYPE );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toString(java.lang.Object)
	 */
	
	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		return (String) value;
	}

}
