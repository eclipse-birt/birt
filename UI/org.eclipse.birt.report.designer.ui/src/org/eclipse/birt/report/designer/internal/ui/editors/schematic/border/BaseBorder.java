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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.border;

import java.util.HashMap;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.swt.SWT;

/**
 * Base class for border
 * 
 * @author banian
 * @version $Revision: #3 $ $Date: 2005/02/05 $
 */

public abstract class BaseBorder extends AbstractBorder
{

	public String bottom_width;
	public String bottom_style;
	public String bottom_color;
	public String top_width;
	public String top_style;
	public String top_color;
	public String left_width;
	public String left_style;
	public String left_color;
	public String right_width;
	public String right_style;
	public String right_color;

	protected int i_bottom_style, i_bottom_width = 1;
	protected int i_top_style, i_top_width = 1;
	protected int i_left_style, i_left_width = 1;
	protected int i_right_style, i_right_width = 1;
	
	private static HashMap styleMap = new HashMap( );
	private static HashMap widthMap = new HashMap( );

	protected int leftGap, rightGap, bottomGap, topGap;
	
	static
	{
		styleMap.put( "solid", new Integer( SWT.LINE_SOLID ) );//$NON-NLS-1$
		styleMap.put( "dotted", new Integer( SWT.LINE_DOT ) );//$NON-NLS-1$
		styleMap.put( "dashed", new Integer( SWT.LINE_DASH ) );//$NON-NLS-1$
		styleMap.put( "double", new Integer( -2 ) );//$NON-NLS-1$
		styleMap.put( "none", new Integer( 0 ) );//$NON-NLS-1$

		widthMap.put( "thin", new Integer( 1 ) );//$NON-NLS-1$
		widthMap.put( "medium", new Integer( 2 ) );//$NON-NLS-1$
		widthMap.put( "thick", new Integer( 3 ) );//$NON-NLS-1$
	}

	/**
	 * Calculate left and right gap to avoid cross-line when drawing double line
	 *  
	 */
	protected void calLeftRightGap( )
	{
		leftGap = 0;
		rightGap = 0;
		if ( i_left_style == -2 )
		{
			leftGap = i_left_width + 1;
		}
		else
		{
			leftGap = i_left_width;
		}
		if ( i_right_style == -2 )
		{
			rightGap = i_right_width + 1;
		}
		else
		{
			rightGap = i_right_width;
		}
	}

	/**
	 * Calculate top and bottom gap to avoid cross-line when drawing double line
	 *  
	 */
	protected void calTopBottomGap( )
	{
		topGap = 0;
		bottomGap = 0;
		if ( i_top_style == -2 )
		{
			topGap = i_top_width + 1;
		}
		else
		{
			topGap = i_top_width;
		}
		if ( i_bottom_style == -2 )
		{
			bottomGap = i_bottom_width + 1;
		}
		else
		{
			bottomGap = i_bottom_width;
		}
	}

	/**
	 * @param obj
	 * @return
	 */
	protected String getStyeSize( Object obj )
	{
		Integer retValue = (Integer) ( styleMap.get( obj ) );
		if ( retValue == null )
		{
			retValue = new Integer( SWT.LINE_DASH );
		}
		return retValue.toString( );
	}

	/**
	 * @param obj
	 * @return
	 */
	protected String getStyeWidth( Object obj )
	{
		Integer retValue = (Integer) ( widthMap.get( obj ) );
		if ( retValue == null )
		{
			retValue = new Integer( 1 );
		}
		return retValue.toString( );
	}

}