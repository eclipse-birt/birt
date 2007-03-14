
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.util;

/**
 * 
 */

public class CompareUtil
{
	public static int compare( Object[] objs1, Object[] objs2 )
	{
		for ( int i = 0; i < objs1.length; i++ )
		{
			int result = 0;
			if ( objs1[i] != null && objs2[i] != null )
			{
				result = ( (Comparable) objs1[i] ).compareTo( objs2[i] );
				if ( result != 0 )
				{
					return result;
				}
			}
			else if ( objs1[i] != null && objs2[i] == null )
				return 1;
			else if ( objs1[i] == null && objs2[i] != null )
				return -1;
		}
		return 0;
	}
}
