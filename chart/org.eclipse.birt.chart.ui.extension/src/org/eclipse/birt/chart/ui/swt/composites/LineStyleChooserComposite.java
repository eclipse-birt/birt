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
 * LineStyleChooserComposite
 */
public class LineStyleChooserComposite extends CustomChooserComposite
{

	private static final int[] iLineStyles = new int[]{
			SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DASHDOT, SWT.LINE_DOT
	};

	static class LineStyleChoice extends LineCanvas implements ICustomChoice
	{

		LineStyleChoice( Composite parent, int iStyle, int iLineStyle )
		{
			super( parent, iStyle, iLineStyle, 1 );
		}

		public int getValue( )
		{
			return getLineStyle( );
		}

		public void setValue( int value )
		{
			setLineStyle( value );
		}

	}

	public LineStyleChooserComposite( Composite parent, int style,
			int iLineStyle )
	{
		super( parent, style, iLineStyle );

	}

	protected ICustomChoice createChoice( Composite parent, int choiceValue )
	{
		return new LineStyleChoice( parent, SWT.NONE, choiceValue );
	}

	protected int[] getChoiceValues( )
	{
		return iLineStyles;
	}

	/**
	 * Returns the current selected line style as an integer corresponding to
	 * the appropriate SWT constants.
	 * 
	 */
	public int getLineStyle( )
	{
		return getChoiceValue( );
	}

	public void setLineStyle( int iStyle )
	{
		setChoiceValue( iStyle );
	}

}
