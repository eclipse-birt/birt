
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
package org.eclipse.birt.data.engine.olap.util;

/**
 * 
 */

public class OlapExpressionUtil
{
	/**
	 * This method is used to get the level name that reference by a level reference expression of
	 * following format: dimension["dimensionName"]["levelName"].
	 * @param expr
	 * @return
	 */
	public static String  getTargetLevel( String expr )
	{
		//TODO enhance me.
		if ( expr == null )
			return null;
		if ( !expr.matches( "\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E" ))
			return null;
		
		return expr.replaceFirst( "\\Qdimension[\"\\E.*\\Q\"][\"\\E", "" )
				.replaceAll( "\\Q\"]\\E", "" ); 
	}
}
