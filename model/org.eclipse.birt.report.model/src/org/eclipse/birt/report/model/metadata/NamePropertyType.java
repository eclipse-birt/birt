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


/**
 * Element name property type. Represents the name
 * of an element.
 *
 */

public class NamePropertyType extends TextualPropertyType
{
    /**
	 * Display name key.
	 */
	
	private static final String DISPLAY_NAME_KEY = "Property.name"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	
	public NamePropertyType( )
	{
	    super( DISPLAY_NAME_KEY );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */
	
	public int getTypeCode( )
	{
		return NAME_TYPE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getXmlName()
	 */
	
	public String getName( )
	{
		return NAME_TYPE_NAME;
	}

}
