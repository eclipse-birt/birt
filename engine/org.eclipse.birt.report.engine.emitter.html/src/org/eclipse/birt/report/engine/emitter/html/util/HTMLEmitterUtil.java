/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.html.util;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.HTMLTags;
import org.eclipse.birt.report.engine.emitter.html.HTMLWriter;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;


/**
 * Utility class for html emitter.
 *
 */
public class HTMLEmitterUtil
{
	/**
	 * display type of Block
	 */
	public static final int DISPLAY_BLOCK = 1;

	/**
	 * display flag which contains all display types
	 */
	public static final int DISPLAY_FLAG_ALL = 0xffff;

	/**
	 * display type of Inline
	 */
	public static final int DISPLAY_INLINE = 2;

	/**
	 * display type of Inline-Block
	 */
	public static final int DISPLAY_INLINE_BLOCK = 4;

	/**
	 * display type of none
	 */
	public static final int DISPLAY_NONE = 8;

	public static int getElementType( IContent content )
	{
		return getElementType( content.getX( ), content.getY( ), content
				.getWidth( ), content.getHeight( ), content.getStyle( ) );
	}

	public static String getTagByType( int display, int mask )
	{
		int flag = display & mask;
		String tag = null;
		if ( ( flag & DISPLAY_BLOCK ) > 0 )
		{
			tag = HTMLTags.TAG_DIV;
		}

		if ( ( flag & DISPLAY_INLINE ) > 0 )
		{
			tag = HTMLTags.TAG_SPAN;
		}

		return tag;
	}

	/**
	 * Outputs the 'bookmark' property. Destination anchors in HTML documents
	 * may be specified either by the A element (naming it with the 'name'
	 * attribute), or by any other elements (naming with the 'id' attribute).
	 * 
	 * @param tagName
	 *            The tag's name.
	 * @param bookmark
	 *            The bookmark value.
	 */
	public static void setBookmark( HTMLWriter writer, String tagName, String htmlIDNamespace, String bookmark )
	{
		String htmlBookmark;
		if ( null != htmlIDNamespace
				&& null != bookmark && bookmark.length( ) > 0 )
		{
			htmlBookmark = htmlIDNamespace + bookmark;
		}
		else
		{
			htmlBookmark = bookmark;
		}
		
		if ( tagName == null || !HTMLTags.TAG_A.equalsIgnoreCase( tagName ) )
		{
			writer.attribute( HTMLTags.ATTR_ID, htmlBookmark );
		}
		else
		{
			writer.attribute( HTMLTags.ATTR_ID, htmlBookmark );
			writer.attribute( HTMLTags.ATTR_NAME, htmlBookmark );
		}
	}
	
	private static int getElementType( DimensionType x, DimensionType y,
			DimensionType width, DimensionType height, IStyle style )
	{
		int type = 0;
		String display = null;
		if ( style != null )
		{
			display = style.getDisplay( );
		}

		if ( EngineIRConstants.DISPLAY_NONE.equalsIgnoreCase( display ) )
		{
			type |= DISPLAY_NONE;
		}
		if ( x != null || y != null )
		{
			return type | DISPLAY_BLOCK;
		}
		else if ( EngineIRConstants.DISPLAY_INLINE.equalsIgnoreCase( display ) )
		{
			type |= DISPLAY_INLINE;
			if ( width != null || height != null )
			{
				type |= DISPLAY_INLINE_BLOCK;
			}
			return type;
		}
		return type | DISPLAY_BLOCK;
	}
}
