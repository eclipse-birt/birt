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

package org.eclipse.birt.report.model.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Utility class to do the operations of the design file version.
 */

public class VersionUtil
{

	protected Map parsedVersions = new HashMap( );

	private static final int SUPPORTED_VERSION_TOKEN_LENGTH = 3;

	/**
	 * Default constructor.
	 */
	
	public VersionUtil( )
	{
		
	}
	
	/**
	 * 
	 * @param version
	 * @return the parsed version number
	 */

	public Integer parseVersion( String version )
	{
		if ( StringUtil.isBlank( version ) )
			return new Integer( 0 );

		// read the version number from the cached map first

		Integer cacheNumber = (Integer) parsedVersions.get( version );
		if ( cacheNumber != null )
			return cacheNumber;

		// parse the version string and cache it

		String[] versionTokers = version.split( "\\." ); //$NON-NLS-1$
		int parsedVersionNumber = 0;
		for ( int i = 0, j = SUPPORTED_VERSION_TOKEN_LENGTH - 1; i < versionTokers.length; i++, j-- )
		{
			if ( i > SUPPORTED_VERSION_TOKEN_LENGTH )
				break;

			byte versionShort = Byte.parseByte( versionTokers[i] );
			parsedVersionNumber += versionShort << ( 8 * j );
		}
		// add the parsed version to the cache map

		Integer versionInteger = new Integer( parsedVersionNumber );
		parsedVersions.put( version, versionInteger );

		return versionInteger;
	}

	/**
	 * Compares the given two versions. The version is the string containing
	 * numbers and periods. For example:
	 * <p>
	 * <ul>
	 * <li>12
	 * <li>1.2.3
	 * <li>3.5.0
	 * </ul>
	 * <p>
	 * Note:This implementation treats "1.0.0" and "1" are same version.The
	 * result is unexpectable if the version string contains alphabetic
	 * character.
	 * 
	 * @param versionA
	 *            the given version string
	 * @param versionB
	 *            the given version string
	 * @return <code>-1</code> if <code>versionA</code> is lower than
	 *         <code>versionB</code>;<code>0</code> if
	 *         <code>versionA</code> is equal to <code>versionB</code>;
	 *         otherwise, return <code>1</code>.
	 */

	public int compareVersion( String versionA, String versionB )
	{
		Integer versionNumberA = parseVersion( versionA );
		Integer versionNumberB = parseVersion( versionB );
		assert versionNumberA != null;
		return versionNumberA.compareTo( versionNumberB );
	}
}
