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

package org.eclipse.birt.report.designer.internal.ui.layout;

import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author David Michonneau
 *  
 */
public class ReportItemConstraint extends Rectangle
{

	public static final int NONE = 0;

	public static final int INLINE = 1;

	public static final int BLOCK = 2;

	private int m_display = NONE;

	public int getDisplay( )
	{
		return m_display;
	}

	/**
	 * @param isInline
	 *            The isInline to set.
	 */
	public void setDisplay( String display )
	{
		if ( display.equals( DesignChoiceConstants.DISPLAY_INLINE ) )
			m_display = INLINE;
		if ( display.equals( DesignChoiceConstants.DISPLAY_BLOCK ) )
			m_display = BLOCK;
	}

	/**
	 * @return true if the element display is inline
	 */
	public boolean isInJline( )
	{
		return m_display == INLINE;
	}

	/**
	 * @return true if the element display is block
	 */
	public boolean isBlock( )
	{
		return m_display == BLOCK;
	}

	/**
	 * @return true if the display is none
	 */
	public boolean isNone( )
	{
		return m_display == NONE;
	}
}