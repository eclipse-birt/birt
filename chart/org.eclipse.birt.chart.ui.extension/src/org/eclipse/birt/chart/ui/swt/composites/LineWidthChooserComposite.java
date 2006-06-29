/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * LineWidthChooserComposite
 */
public class LineWidthChooserComposite extends CustomChooserComposite
{

	private static final int[] iLineWidths = new int[]{
			1, 2, 3, 4
	};

	static class LineWidthChoice extends LineCanvas implements ICustomChoice
	{

		LineWidthChoice( Composite parent, int iStyle, int iLineWidth )
		{
			super( parent, iStyle, SWT.LINE_SOLID, iLineWidth );
		}

		public int getValue( )
		{
			return getLineWidth( );
		}

		public void setValue( int value )
		{
			setLineWidth( value );
		}

	}

	public LineWidthChooserComposite( Composite parent, int style, int iWidth )
	{
		super( parent, style, iWidth );
	}

	protected ICustomChoice createChoice( Composite parent, int choiceValue )
	{
		return new LineWidthChoice( parent, SWT.NONE, choiceValue );
	}

	protected int[] getChoiceValues( )
	{
		return iLineWidths;
	}

	/**
	 * Returns the currently selected line width
	 * 
	 * @return currently selected line width
	 */
	public int getLineWidth( )
	{
		return getChoiceValue( );
	}

	public void setLineWidth( int iWidth )
	{
		setChoiceValue( iWidth );
	}

}