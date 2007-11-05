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

package org.eclipse.birt.report.model.adapter.oda.impl;

/**
 * The internal utility to compare two values.
 * 
 */

class CompareUtil
{

	/**
	 * Determines two given values are equal or not.
	 * 
	 * @param value1
	 *            value1
	 * @param value2
	 *            value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	static boolean isEquals( String value1, String value2 )
	{
		return equals( value1, value2 );
	}

	/**
	 * Determines two given values are equal or not.
	 * 
	 * @param value1
	 *            value1
	 * @param value2
	 *            value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	static boolean isEquals( Boolean value1, Boolean value2 )
	{
		return equals( value1, value2 );
	}

	/**
	 * Determines two given values are equal or not.
	 * 
	 * @param value1
	 *            value1
	 * @param value2
	 *            value2
	 * @return <code>true</code> if two values are equal. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean equals( Object value1, Object value2 )
	{
		// may be same string or both null.

		if ( value1 == value2 )
			return true;

		if ( value1 != null && value2 == null )
			return false;

		if ( value1 == null && value2 != null )
			return false;

		assert value1 != null && value2 != null;

		if ( value1.getClass( ) != value2.getClass( ) )
			return false;

		if ( !( value1 instanceof Comparable ) ||
				!( value2 instanceof Comparable ) )
			return false;

		if ( !value1.equals( value2 ) )
			return false;

		return true;
	}

}
