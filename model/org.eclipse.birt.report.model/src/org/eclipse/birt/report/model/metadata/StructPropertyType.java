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

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.elements.ReportDesign;


/**
 * Represents the property type for a list of objects
 * (structures) or a simple structure.
 *
 */

public class StructPropertyType extends PropertyType
{

    /**
     * Display name key.
     */

    private static final String DISPLAY_NAME_KEY = "Property.struct"; //$NON-NLS-1$


    /**
     * Constructor.
     */

    public StructPropertyType( )
    {
        super( DISPLAY_NAME_KEY );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
     */

    public int getTypeCode( )
    {
        return STRUCT_TYPE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
     */

    public String getName( )
    {
        return STRUCT_TYPE_NAME;
    }

    /**
     * Can not store objects of this list property type directly. Call to this
     * method will always throw an exception.
     *  
     */

    public Object validateValue( ReportDesign design, PropertyDefn defn,
            Object value ) throws PropertyValueException
    {

        if ( value == null )
            return null;

        // Cannot store objects of a list directly.

        if ( defn.isList( ) )
            throw new PropertyValueException( value,
                    PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, STRUCT_TYPE );

        if ( value instanceof Structure )
        {
            Iterator iter = ( (Structure) value ).getDefn( )
                    .getPropertyIterator( );
            while ( iter.hasNext( ) )
            {
                PropertyDefn memberDefn = (PropertyDefn) iter.next( );
                if( !memberDefn.isList() )
                {
                    Object propValue = ( (Structure) value ).getProperty( design,
                        memberDefn );
                    memberDefn.validateValue( design, propValue );
                }
            }
            
            return value;
        }

        // exception

        return null;

    }

    /**
     * Converts the structure list property type into an integer. If value is
     * null, return 0, else return the size of the list value.
     * 
     * @return the integer value of the structure list property type.
     */

    public int toInteger( ReportDesign design, Object value )
    {
        // Return the list size as the int value.

        if ( value == null )
            return 0;
        return ( (ArrayList) value ).size( );
    }

    /**
     * Can not convert a list property type to a string. This method will always
     * return null.
     *  
     */

    public String toString( ReportDesign design, PropertyDefn defn, Object value )
    {
        // Cannot convert to string

        return null;
    }

}
