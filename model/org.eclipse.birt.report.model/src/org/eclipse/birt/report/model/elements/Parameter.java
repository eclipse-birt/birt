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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Base class for the various kinds of parameters.
 *
 */

public abstract class Parameter extends DesignElement
{
	/**
	 * Name of the help text property.
	 */
	
	public static final String HELP_TEXT_PROP = "helpText";	//$NON-NLS-1$ 

	/**
	 * Name of the help text key property.
	 */
	
	public static final String HELP_TEXT_KEY_PROP = "helpTextID"; //$NON-NLS-1$
	
	/**
	 * Name of the "hidden" property.
	 */
	
	public static final String HIDDEN_PROP = "hidden"; //$NON-NLS-1$
	
    /**
     * Default constructor.
     */
    
	public Parameter( )
	{
	}
	    
	/**
	 * Constructs the parameter element with a required and unique name.
	 * 
	 * @param theName the required name
	 */
	
	public Parameter( String theName )
	{
		super( theName );
	}
	    
}
