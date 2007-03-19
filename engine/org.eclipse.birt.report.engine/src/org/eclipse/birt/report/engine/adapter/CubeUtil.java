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

package org.eclipse.birt.report.engine.adapter;

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.CubeCursor;
import javax.olap.cursor.EdgeCursor;

/**
 * This is a utility class which is used by Engine to create a unique
 * locator for a cube cursor.
 */

public class CubeUtil
{

	/**
	 * Get the position id of a CubeCursor. The position id is decided by
	 * the combination of edge cursors.
	 * 
	 * @param cursor
	 * @return
	 * @throws OLAPException
	 */
	public String getPositionID( CubeCursor cursor ) throws OLAPException
	{
		String result = "";
		List ordinateEdge = cursor.getOrdinateEdge( );
		ordinateEdge.addAll( cursor.getPageEdge( ) );

		for ( int i = 0; i < ordinateEdge.size( ); i++ )
		{
			EdgeCursor edge = (EdgeCursor) ordinateEdge.get( i );
			result += edge.getPosition( );
		}

		return result;
	}
}
