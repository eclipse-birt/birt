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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.core.MemberRef;

import com.ibm.icu.util.ULocale;

/**
 * Represents a format value in the style or the highlight rule.
 * 
 */

public class FormatValueHandle extends StructureHandle
{

	/**
	 * Construct an handle to deal with the action structure.
	 * 
	 * @param element
	 *            the element that defined the action.
	 * @param ref
	 *            reference to the action property.
	 */

	public FormatValueHandle( DesignElementHandle element, MemberRef ref )
	{
		super( element, ref );
	}

	/**
	 * Constructs the handle of configuration variable.
	 * 
	 * @param valueHandle
	 *            the value handle for configuration variable list of one
	 *            property
	 * @param index
	 *            the position of this configuration variable in the list
	 */

	public FormatValueHandle( SimpleValueHandle valueHandle, int index )
	{
		super( valueHandle, index );
	}

	/**
	 * Returns the category of the format.
	 * 
	 * @return the category of the format
	 */

	public String getCategory( )
	{
		return getStringProperty( FormatValue.CATEGORY_MEMBER );
	}

	/**
	 * Sets the category of the format.
	 * 
	 * @param pattern
	 *            the category of the format
	 * @throws SemanticException
	 *             if <code>pattern</code> is not one of the BIRT defined.
	 * 
	 */

	public void setCategory( String pattern ) throws SemanticException
	{
		setProperty( FormatValue.CATEGORY_MEMBER, pattern );
	}

	/**
	 * Returns the pattern of the format.
	 * 
	 * @return the pattern of the format
	 */

	public String getPattern( )
	{
		return getStringProperty( FormatValue.PATTERN_MEMBER );
	}

	/**
	 * Sets the pattern of the format.
	 * 
	 * @param value
	 *            the pattern of the format
	 */

	public void setPattern( String value )
	{
		setPropertySilently( FormatValue.PATTERN_MEMBER, value );
	}

	/**
	 * Sets the locale of the format.
	 * 
	 * @param locale
	 *            the locale of the format.
	 */
	public void setLocale( ULocale locale ) throws SemanticException
	{
		setProperty( FormatValue.LOCALE_MEMBER, locale );
	}

	/**
	 * Gets the locale of the format.
	 * 
	 * @return the locale of the format.
	 */
	public ULocale getLocale( )
	{
		return (ULocale) getProperty( FormatValue.LOCALE_MEMBER );
	}
}
