/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.CursorType;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.emf.common.util.EList;

/**
 * The class defines fields and methods for Cascading Style Sheet.
 * @since 2.5
 */

public class CSSHelper
{
	public static final String CURSOR_STYLE_PREFIX = "cursor:"; //$NON-NLS-1$
	
	public static final Map<CursorType, String> CSS_CURSOR_MAP = new HashMap<org.eclipse.birt.chart.model.attribute.CursorType, String>();
	static
	{
		CSS_CURSOR_MAP.put( CursorType.AUTO, "auto" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.CROSSHAIR, "crosshair" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.DEFAULT, "default" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.POINTER, "pointer" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.MOVE, "move" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.TEXT, "text" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.WAIT, "wait" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.ERESIZE, "e-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.NE_RESIZE, "ne-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.NW_RESIZE, "nw-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.NRESIZE, "n-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.SE_RESIZE, "se-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.SW_RESIZE, "sw-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.SRESIZE, "s-resize" ); //$NON-NLS-1$
		CSS_CURSOR_MAP.put( CursorType.WRESIZE, "w-resize" ); //$NON-NLS-1$
	}
	
	public static String getCSSCursorValue( Cursor cursor )
	{
		if ( cursor == null || cursor.getType( ) == null )
		{
			return null;
		}
		
		String value = CSSHelper.CURSOR_STYLE_PREFIX + " "; //$NON-NLS-1$
		if ( cursor.getType( ) != CursorType.CUSTOM )
		{
			value += CSSHelper.CSS_CURSOR_MAP.get( cursor.getType( ) ) + ";";	 //$NON-NLS-1$
		}
		else
		{
			// Custom cursors.
			EList<Image> cursorImages = cursor.getImage( );
			int i = 0;
			for ( Image uri : cursorImages )
			{
				if ( uri.getURL( ) == null || uri.getURL( ).trim( ).length( ) == 0 )
				{
					continue;
				}
				
				String sUri = uri.getURL( );
				if ( sUri.startsWith( "\"" ) && sUri.endsWith( "\"" )) //$NON-NLS-1$ //$NON-NLS-2$
				{
					sUri = sUri.substring( 1, sUri.length( ) - 1 );
				}
				if ( sUri.trim( ).length( ) == 0 )
				{
					continue;
				}
				
				if ( i != 0 )
				{
					value += ","; //$NON-NLS-1$
				}
				
				value += "url(" + sUri + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				i++;
			}
			if ( cursorImages.size( ) > 0 )
			{
				value += ",auto;"; //$NON-NLS-1$
			}
			else
			{
				value += "auto;"; //$NON-NLS-1$
			}
		}

		return value ;
	}
}
