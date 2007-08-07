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

package org.eclipse.birt.report.engine.layout.emitter.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents the algorithm to calculate the positions of background images for
 * a rectangle area. The background maybe set to "no-repeat", "repeat-x",
 * "repeat-y" or no "repeat"
 * 
 */
public class BackgroundImageLayout
{

	public static final int REPEAT_NONE = 0;
	public static final int REPEAT_X = 1;
	public static final int REPEAT_Y = 2;
	public static final int REPEAT_BOTH = 3;

	private Position areaPosition, areaSize, imagePosition, imageSize;

	/**
	 * Constructor.
	 * 
	 * @param areaPosition
	 *            the left up corner of the area which need to be filled by the
	 *            background.
	 * @param areaSize
	 *            the size of the area. Width is represented by
	 *            <code>Position.x</code>, height by <code>Position.y</code>.
	 * @param imagePosition
	 *            the initial position of the image. This image is used to
	 *            specify the offset of the image.
	 * @param imageSize
	 *            the image size.
	 */
	public BackgroundImageLayout( Position areaPosition, Position areaSize,
			Position imagePosition, Position imageSize )
	{
		this.areaPosition = areaPosition;
		this.areaSize = areaSize;
		this.imagePosition = imagePosition;
		this.imageSize = imageSize;
	}

	public List getImagePositions( int repeat )
	{
		if ( repeat < 0 || repeat > 3 )
		{
			throw new IllegalArgumentException(
					" repeat should in range 0-3 : " + repeat );
		}
		Set positions = new HashSet( );
		calculateRepeatX( imagePosition, repeat, positions );
		if ( isRepeatY( repeat ) )
		{
			float x = imagePosition.x;
			float y = imagePosition.y;
			while ( y > areaPosition.y )
			{
				y = y - imageSize.y;
				calculateRepeatX( new Position( x, y ), repeat, positions );
			}
			y = imagePosition.y;
			while ( y + imageSize.y < areaPosition.y + areaSize.y )
			{
				y = y + imageSize.y;
				calculateRepeatX( new Position( x, y ), repeat, positions );
			}
		}

		// Conver set to list and sort the list.
		List list = Arrays.asList( positions.toArray( ) );
		Collections.sort( list );
		return list;
	}

	public List getImagePositions( String repeat )
	{
		int repeatMode = REPEAT_BOTH;
		if ( !( "repeat".equals( repeat ) ) )
		{
			if ( "repeat-x".equals( repeat ) )
			{
				repeatMode = REPEAT_X;
			}
			else if ( "repeat-y".equals( repeat ) )
			{
				repeatMode = REPEAT_Y;
			}
			else if ( "no-repeat".equals( repeat ) )
			{
				repeatMode = REPEAT_NONE;
			}
		}
		return getImagePositions( repeatMode );
	}

	private void calculateRepeatX( Position initPosition, int repeat,
			Set positions )
	{
		positions.add( initPosition );
		if ( isRepeatX( repeat ) )
		{
			float x = initPosition.x;
			float y = initPosition.y;
			while ( x > areaPosition.x )
			{
				x = x - imageSize.x;
				positions.add( new Position( x, y ) );
			}
			x = initPosition.x;
			while ( x + imageSize.x < areaPosition.x + areaSize.x )
			{
				x = x + imageSize.x;
				positions.add( new Position( x, y ) );
			}
		}
	}

	private static boolean isRepeatX( int repeat )
	{
		return ( repeat & REPEAT_X ) != 0;
	}

	private static boolean isRepeatY( int repeat )
	{
		return ( repeat & REPEAT_Y ) != 0;
	}

}
