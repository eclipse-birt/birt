/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

/**
 * Provides data convert and format services
 * 
 */
public class DataUtil
{

	/**
	 * Convert Object to String
	 * 
	 * @param object
	 * @return String
	 */
	public static String getString( Object object )
	{
		if ( object == null )
			return null;

		return object.toString( );
	}
}
